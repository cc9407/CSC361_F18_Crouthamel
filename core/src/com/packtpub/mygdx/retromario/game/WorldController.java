/*
 * Author: Christian Crouthamel
 * Project: Retro Mario
 * Class: VGP 
 * Professor: Dr.Girard
 * Description: world controller class
 */

package com.packtpub.mygdx.retromario.game;


import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.packtpub.mygdx.retromario.game.objects.AbstractGameObject;
import com.packtpub.mygdx.retromario.game.objects.Goal;
import com.packtpub.mygdx.retromario.game.objects.GoldCoin;
import com.packtpub.mygdx.retromario.game.objects.Leaf;
import com.packtpub.mygdx.retromario.game.objects.Rock;
import com.packtpub.mygdx.retromario.screens.MenuScreen;
import com.packtpub.mygdx.retromario.util.AudioManager;
import com.packtpub.mygdx.retromario.util.CameraHelper;
import com.packtpub.mygdx.retromario.util.Constants;


public class WorldController extends InputAdapter implements Disposable, ContactListener{
	
	//instance variables and objects 
	private static final String TAG = WorldController.class.getName();
	public LevelOne level;
	public int lives;
	public int score;
	private Game game;
	public CameraHelper cameraHelper;
	public AbstractGameObject contactObject;
	public float livesVisual;
	public float scoreVisual;
	public boolean done;
	public AbstractGameObject destroy;
	private float timeLeftGameOverDelay;
	private boolean goalReached; //has the goal been reached?
	public static World b2world;
	public boolean grounded = false;
	public int airTime = 10;

	
	/**
	 * Initialize the world controller. Set lives.
	 * Get a CameraHelper.
	 */
	private void init() {
		b2world = new World(new Vector2(0, -20.0f),true);
		Gdx.input.setInputProcessor(this);
		b2world.setContactListener(this);
		cameraHelper = new CameraHelper();
		lives = Constants.LIVES_START;
		livesVisual = lives;
		timeLeftGameOverDelay = 0;
		initLevel();
	}
	
	/**
	 * constructor for world controller
	 * @param game instance of the game/BunnyMain
	 */
	public WorldController(Game game) {
		this.game = game;
		init();
	}
	
	/**
	 * Level initialization method
	 */
	private void initLevel() {
		score = 0;
		scoreVisual = score;
	    goalReached = false; //set goal reached to false at each init
		level = new LevelOne(Constants.LEVEL_01);
		System.out.println("InitLevel: " + level.mario.body.getPosition() + " : " + level.mario.position);
		cameraHelper.setTarget(level.mario);
		initPhysics();
		System.out.println("InitPhys: " + level.mario.body.getPosition() + " : " + level.mario.position);
	}
	
	/**
	 * Boolean checker method for if the game has ended
	 * @return true if lives are < 0
	 */
	public boolean isGameOver() {
		return lives < 0;
	}
	
	/*
	 * returns goal reached variable
	 */
	public boolean isGameOverTwo() {
		return goalReached;
	}

	
	/**
	 * Checks if the player fell off screen
	 * @return
	 */
	public boolean isPlayerInWater() {
		return level.mario.position.y < -5;
	}

	/**
	 * Initialize the physics inside of the world
	 * using box2d assets
	 * dispose of excess to free memory
	 */
	private void initPhysics() {
//		if(b2world != null) b2world.dispose(); //destroy if already init
//		b2world = new World(new Vector2(0, -9.81f),true);
		//Rocks
		Vector2 origin = new Vector2();
		for(Rock rock : level.rocks) { //for each rock
			BodyDef bodyDef = new BodyDef();
			bodyDef.type = BodyType.StaticBody;
			bodyDef.position.set(rock.position);
			 Body body = b2world.createBody(bodyDef);
			rock.body = body;
			PolygonShape polygonShape = new PolygonShape();
			origin.x = rock.bounds.width / 2.0f;
			origin.y = rock.bounds.height / 2.0f;
			polygonShape.setAsBox(rock.bounds.width / 2.0f,
					rock.bounds.height / 2.0f, origin,0);
			System.out.println(rock.bounds.height +" "+ rock.bounds.width);
			FixtureDef fixtureDef = new FixtureDef();
			fixtureDef.shape = polygonShape;
			FixtureDef fixtureSensor = new FixtureDef();
			fixtureSensor.shape = polygonShape;
			fixtureSensor.isSensor = true;
			rock.body.createFixture(fixtureDef);
			rock.body.createFixture(fixtureSensor);
			polygonShape.dispose();
			rock.body.setUserData(rock);
		}
		//coins
		for(GoldCoin goldCoins : level.goldcoins) { 
			BodyDef bodyDef = new BodyDef();
			bodyDef.type = BodyType.KinematicBody;
			bodyDef.position.set(goldCoins.position);
			 Body body = b2world.createBody(bodyDef);
			goldCoins.body = body;
			PolygonShape polygonShape = new PolygonShape();
			origin.x = goldCoins.bounds.width / 2.0f;
			origin.y = goldCoins.bounds.height / 2.0f;
			polygonShape.setAsBox(goldCoins.bounds.width / 2.0f,
					goldCoins.bounds.height / 2.0f, origin,0);
			FixtureDef fixtureDef = new FixtureDef();
			fixtureDef.shape = polygonShape;
			fixtureDef.isSensor = true;
			goldCoins.body.createFixture(fixtureDef);
			polygonShape.dispose();
			body.setUserData(goldCoins);
		}
		
		//leaves
		for(Leaf leaves : level.leaves) { 
			BodyDef bodyDef = new BodyDef();
			bodyDef.type = BodyType.KinematicBody;
			bodyDef.position.set(leaves.position);
			 Body body = b2world.createBody(bodyDef);
			leaves.body = body;
			PolygonShape polygonShape = new PolygonShape();
			origin.x = leaves.bounds.width / 2.0f;
			origin.y = leaves.bounds.height / 2.0f;
			polygonShape.setAsBox(leaves.bounds.width / 2.0f,
					leaves.bounds.height / 2.0f, origin,0);
			FixtureDef fixtureDef = new FixtureDef();
			fixtureDef.shape = polygonShape;
			fixtureDef.isSensor = true;
			body.createFixture(fixtureDef);
			polygonShape.dispose();
			body.setUserData(leaves);
			}
		
//		BodyDef bodyDef = new BodyDef();
//		bodyDef.type = BodyType.StaticBody;
//		bodyDef.position.set(level.goal.position);
//		 Body body = b2world.createBody(bodyDef);
//		level.goal.body = body;
//		PolygonShape polygonShape = new PolygonShape();
//		origin.x = level.goal.bounds.width / 2.0f;
//		origin.y = level.goal.bounds.height / 2.0f;
//		polygonShape.setAsBox(level.goal.bounds.width / 2.0f,
//				level.goal.bounds.height / 2.0f, origin,0);
//		System.out.println(level.goal.bounds.height +" "+ level.goal.bounds.width);
//		FixtureDef fixtureDef = new FixtureDef();
//		fixtureDef.shape = polygonShape;
//		FixtureDef fixtureSensor = new FixtureDef();
//		fixtureSensor.shape = polygonShape;
//		fixtureSensor.isSensor = true;
//		level.goal.body.createFixture(fixtureDef);
//		level.goal.body.createFixture(fixtureSensor);
//		polygonShape.dispose();
//		level.goal.body.setUserData(level.goal);
	}

	/**
	 * Build a pixmap
	 * @param width width of the procedural pixmap
	 * @param height height of the procedural pixmap
	 * @return the built pixmap
	 */
	private Pixmap createProceduralPixmap(int width, int height) {
		Pixmap pixmap = new Pixmap(width, height, Format.RGBA8888);
		// fill square w/ red color at 50% opacity
		pixmap.setColor(1, 0, 0, 0.5f);
		pixmap.fill();
		// Draw a yellow colored x shape on square
		pixmap.setColor(1, 1, 0, 1);
		pixmap.drawLine(0, 0, width, height);
		pixmap.drawLine(width, 0, 0, height);
		// draw a cyan colored border around square
		pixmap.setColor(0, 1, 1, 1);
		pixmap.drawRectangle(0, 0, width, height);
		return pixmap;
	}

	/**
	 * Updates the level (and its objects), update the 
	 * camera, handle game inputs, handle collisions.
	 * @param deltaTime the game time
	 */
	public void update(float deltaTime) {
		b2world.step(Gdx.graphics.getDeltaTime(), 4, 4);
		
		//System.out.println(level.mario.body.getPosition() + " : " + level.mario.position);
		
		if(destroy != null)
		{
			b2world.destroyBody(destroy.body);
			destroy = null;
		}
		handleDebugInput(deltaTime);
		if (isGameOver()) {
			timeLeftGameOverDelay -= deltaTime;
			if (timeLeftGameOverDelay < 0)
				System.out.println("No menu yet");
		} else {
			handleInputGame(deltaTime);
		}
		level.update(deltaTime);
		cameraHelper.update(deltaTime);
		if (!isGameOver() && isPlayerInWater()) {
			AudioManager.instance.play(Assets.instance.sounds.liveLost);
			lives--;
			if (isGameOver())
				timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_OVER;
			else
				initLevel();
		}
		// mountains scroll at different speeds
		level.mountains.updateScrollPosition(cameraHelper.getPosition());
		// life lost animation goes as livesVisual catches up to lives
		if (livesVisual > lives)
			livesVisual = Math.max(lives,  livesVisual - 1 * deltaTime);
		// score gain animation goes as scoreVisual catches up to new score
		if (scoreVisual < score)
			scoreVisual = Math.min(score,  scoreVisual 
					+ 250 * deltaTime);
	}

	/**
	 * Add functionality behind every user input.
	 * @param deltaTime game time
	 */
	private void handleDebugInput(float deltaTime) {
		if (Gdx.app.getType() != ApplicationType.Desktop)
			return;

		if (!cameraHelper.hasTarget(level.mario)) {
			// Camera Controls(move)
			float camMoveSpeed = 5 * deltaTime;
			float camMoveSpeedAccelerationFactor = 5;
			if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT))
				camMoveSpeed *= camMoveSpeedAccelerationFactor;
			if (Gdx.input.isKeyPressed(Keys.LEFT))
				moveCamera(-camMoveSpeed, 0);
			if (Gdx.input.isKeyPressed(Keys.RIGHT))
				moveCamera(camMoveSpeed, 0);
			if (Gdx.input.isKeyPressed(Keys.UP))
				moveCamera(0, camMoveSpeed);
			if (Gdx.input.isKeyPressed(Keys.DOWN))
				moveCamera(0, -camMoveSpeed);
			if (Gdx.input.isKeyPressed(Keys.BACKSPACE))
				cameraHelper.setPosition(0, 0);
			// Camera controls(zoom)
			float camZoomSpeed = 1 * deltaTime;
			float camZoomSpeedAccelerationFactor = 5;
			if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT))
				camZoomSpeed *= camZoomSpeedAccelerationFactor;
			if (Gdx.input.isKeyPressed(Keys.COMMA))
				cameraHelper.addZoom(camZoomSpeed);
			if (Gdx.input.isKeyPressed(Keys.PERIOD))
				cameraHelper.addZoom(-camZoomSpeed);
			if (Gdx.input.isKeyPressed(Keys.SLASH))
				cameraHelper.setZoom(1);

		}
	}

	/**
	 * Controls the x and y movements of the camera.
	 * @param x horizontal position
	 * @param y vertical position
	 */
	private void moveCamera(float x, float y) {
		x += cameraHelper.getPosition().x;
		y += cameraHelper.getPosition().y;
		cameraHelper.setPosition(x, y);
	}

	/**
	 * Checks for special key inputs that restart the world
	 * and change how the camera follows the player. Also
	 * handles escaping back to the menu screen.
	 */
	@Override
	public boolean keyUp(int keycode) {
		// Reset game world
		if (keycode == Keys.R) {
			init();
			Gdx.app.debug(TAG, "Game world restarted");
		}
		// Toggle camera follow
		else if (keycode == Keys.ENTER) {
			cameraHelper.setTarget(cameraHelper.hasTarget() ? null : level.mario);
			Gdx.app.debug(TAG, "Camera follow enabled: " + cameraHelper.hasTarget());
		}
		// Back to Menu
		else if (keycode == Keys.ESCAPE || keycode == Keys.BACK) {
			backToMenu();
		}
		return false;
	}

	/**
	 * Handles the input keys for the game. These affect
	 * player movements in-game and change the physics affecting
	 * the player.
	 * @param deltaTime game time
	 */
	private void handleInputGame(float deltaTime) {
		Vector2 velocity = new Vector2(0,0);
		Vector2 pos = level.mario.body.getPosition();
		if(Gdx.input.isKeyPressed(Keys.RIGHT))
		{
			velocity.x += 6;
		}
			else if(Gdx.input.isKeyPressed(Keys.LEFT)) 
			{
				velocity.x -= 6;
			}
		
		if(Gdx.input.isKeyPressed(Keys.SPACE) && grounded == true && airTime-- > 0)
		{
			System.out.println("JUMPING");
			System.out.println("AirTime: " + airTime);
			//grounded = false;
//			velocity.y += 30;
			level.mario.body.setLinearVelocity(0,velocity.y += 10);
			
			if(level.mario.hasLeafPowerup)
			{
				AudioManager.instance.play(Assets.instance.sounds.jumpWithLeaf);
			}
			else {
				AudioManager.instance.play(Assets.instance.sounds.jump);
			}
			//level.mario.body.setLinearVelocity(velocity.x,0);
			//level.mario.body.setTransform(pos.x,pos.y + 0.01f, 0);
			//level.mario.body.applyLinearImpulse(0 , 10, pos.x, pos.y, true);
		}
		
//		if(!grounded)
//		{
			velocity.y -= 4.0f;
//		}
			
		level.mario.body.setLinearVelocity(velocity);
		
		/*if (cameraHelper.hasTarget(level.mario)) {
			// player movement
			if (Gdx.input.isKeyPressed(Keys.LEFT)) {
				level.mario.velocity.x = -level.mario.terminalVelocity.x;
			} else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
				level.mario.velocity.x = level.mario.terminalVelocity.x;
			} else {
				// Execute auto-forward movement on non-desktop platform
				if (Gdx.app.getType() != ApplicationType.Desktop) {
					level.mario.velocity.x = level.mario.terminalVelocity.x;
				}
			}
			// Bunny Jump
			if (Gdx.input.isTouched() || Gdx.input.isKeyPressed(Keys.SPACE)) {
				level.mario.setJumping(true);
			} else {
				level.mario.setJumping(false);
			}*/
		
	}

	/**
	 * save a reference to the game instance
	 */
	private void backToMenu() {
		// switch to menu screen
		game.setScreen(new MenuScreen(game));
	}
	
	/**
	 * Overridden dispose method
	 * destorys b2world if its extant
	 */
	@Override
	public void dispose() {
		if(b2world != null)b2world.dispose();
		
	}

	/*
	 * (non-Javadoc)
	 * @see com.badlogic.gdx.physics.box2d.ContactListener#beginContact(com.badlogic.gdx.physics.box2d.Contact)
	 */
	@Override
	public void beginContact(Contact contact) {
		//System.out.println("SENSOR");
		if(contact.getFixtureA().getBody().getUserData() == level.mario && contact.getFixtureB().getBody().getUserData().getClass() == Goal.class
				&& contact.getFixtureB().isSensor())
		{
			 System.out.println("GOAAAAALLLLLL");
			if(!done)
			{
				
				contactObject = (AbstractGameObject) contact.getFixtureB().getBody().getUserData();
//				((Goal) contactObject).
				destroy = contactObject;
				done = true;
				goalReached = true;
			}
		}
		
		if (contact.getFixtureA().getBody().getUserData() == level.mario && contact.getFixtureB().getBody().getUserData().getClass() == Rock.class
				&& contact.getFixtureB().isSensor())
		{
			//if(!done)
			//{
				grounded = true;
				if(!level.mario.hasLeafPowerup)
				{
					airTime = 20;
				} else {
					airTime = 35;
				}
			//	done = true;
			//}
		}
		
		if(contact.getFixtureA().getBody().getUserData() == level.mario && contact.getFixtureB().getBody().getUserData().getClass() == GoldCoin.class 
				&& contact.getFixtureB().isSensor())
		{
			if(!done)
			{
				contactObject = (AbstractGameObject) contact.getFixtureB().getBody().getUserData();
				((GoldCoin) contactObject).collected = true; 
				destroy = contactObject;
				done = true;
				score += ((GoldCoin) contactObject).getScore();
				AudioManager.instance.play(Assets.instance.sounds.pickupCoin);
			}
		}
		else if(contact.getFixtureB().getBody().getUserData() == level.mario && contact.getFixtureA().getBody().getUserData() == AbstractGameObject.class)
		{
			contactObject = (AbstractGameObject) contact.getFixtureA().getBody().getUserData();
		}
			else if(contact.getFixtureA().getBody().getUserData() == level.mario && contact.getFixtureB().getBody().getUserData().getClass() == Leaf.class
					&& contact.getFixtureB().isSensor())
			{
				if(!done)
				{
					contactObject = (AbstractGameObject) contact.getFixtureB().getBody().getUserData();
					((Leaf) contactObject).collected = true;
					destroy = contactObject;
					done = true;
					
					level.mario.setLeafPowerup(true);
					airTime = 35;
					AudioManager.instance.play(Assets.instance.sounds.pickupLeaf);
				}
			}
	}

	@Override
	public void endContact(Contact contact) {
		if(contact.getFixtureA().getBody().getUserData() == contactObject)
		{
			done = false;
			contactObject = null;
			//grounded = false;
		}
			else if(contact.getFixtureB().getBody().getUserData() == contactObject)
			{
				done = false;
				contactObject = null;
			}
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
	}
}