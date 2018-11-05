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
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.packtpub.mygdx.retromario.util.CameraHelper;


public class WorldController extends InputAdapter {

	//instance variables and objects
	private static final String TAG = WorldController.class.getName();
	public Sprite[] testSprites;
	public int selectedSprite;
	public CameraHelper cameraHelper;
	
	/**
	 * constructor for world controller
	 * @param game instance of the game/BunnyMain
	 */
	public WorldController() {
	
		init();
	}
	
	/**
	 * Initialize the world controller. Set lives.
	 * Get a CameraHelper.
	 */
	private void init() {
		Gdx.input.setInputProcessor(this);
		cameraHelper = new CameraHelper();
		initTestObjects();
	}
	
	/*
	 * initialize test objects
	 */
	private void initTestObjects() {
		// Create new array for 5 Sprites
		testSprites = new Sprite[5];
		// Create a list of texture regions
		Array<TextureRegion> regions = new Array<TextureRegion>();
		regions.add(Assets.instance.mario.head);
		regions.add(Assets.instance.leaf.base);
		regions.add(Assets.instance.koopa.enemy2);
		regions.add(Assets.instance.goomba.enemy1);
		//create new sprites using the just created texture
		for(int i = 0; i < testSprites.length; i++)
		{
			Sprite spr = new Sprite(regions.random());
			//define sprite size to be 1m X 1m in game world
			spr.setSize(1,1);
			//set origin to Sprites center
			spr.setOrigin(spr.getWidth()/2.0f, spr.getHeight()/2.0f);
			//calculate random position for sprite
			float randomX = MathUtils.random(-2.0f,2.0f);
			float randomY = MathUtils.random(-2.0f,2.0f);
			spr.setPosition(randomX, randomY);
			//put new sprite into array
			testSprites[i] = spr;
		}
		//set first sprite as selected one
		selectedSprite = 0;
	}
	
	/*
	 * 
	 */
	private Pixmap createProceduralPixmap(int width, int height){
		Pixmap pixmap = new Pixmap(width, height,Format.RGBA8888);
		//fill square w/ red color at 50% opacity
		pixmap.setColor(1,0,0,0.5f);
		pixmap.fill();
		//Draw a yellow colored x shape on square
		pixmap.setColor(1,1,0,1);
		pixmap.drawLine(0,0, width, height);
		pixmap.drawLine(width, 0, 0, height);
		//draw a cyan colored border around square
		pixmap.setColor(0,1,1,1);
		pixmap.drawRectangle(0, 0, width, height);
		return pixmap;
	}
	
	/*
	 * 
	 */
	public void update(float deltaTime){
		handleDebugInput(deltaTime);
		updateTestObjects(deltaTime);
		cameraHelper.update(deltaTime);
		
	}
	
	/*
	 * 
	 */
	private void updateTestObjects(float deltaTime){
		//get current rotation from selected sprite
		float rotation = testSprites[selectedSprite].getRotation();
		//rotate sprite by 90 degrees per second
		rotation += 90 * deltaTime;
		//wrap around at 360 degrees
		rotation %= 360;
		//set new rotation val to selected sprite
		testSprites[selectedSprite].setRotation(rotation);
	}
	
	/*
	 * 
	 */
	private void handleDebugInput(float deltaTime){
		if(Gdx.app.getType()!= ApplicationType.Desktop) return;
		//selected sprite controls
		float sprMoveSpeed = 5*deltaTime;
		if(Gdx.input.isKeyPressed(Keys.A)) moveSelectedSprite(
				-sprMoveSpeed,0);
		if(Gdx.input.isKeyPressed(Keys.D)) moveSelectedSprite(
				sprMoveSpeed,0);
		if(Gdx.input.isKeyPressed(Keys.W)) moveSelectedSprite(
				0,sprMoveSpeed);
		if(Gdx.input.isKeyPressed(Keys.S)) moveSelectedSprite(
				0,-sprMoveSpeed);
		//Camera Controls(move)
		float camMoveSpeed = 5*deltaTime;
		float camMoveSpeedAccelerationFactor =5;
		if(Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) camMoveSpeed*=
				camMoveSpeedAccelerationFactor;
		if(Gdx.input.isKeyPressed(Keys.LEFT)) moveCamera(-camMoveSpeed,
				0);
		if(Gdx.input.isKeyPressed(Keys.RIGHT)) moveCamera(camMoveSpeed,
				0);
		if(Gdx.input.isKeyPressed(Keys.UP)) moveCamera(0,camMoveSpeed);
		if(Gdx.input.isKeyPressed(Keys.DOWN)) moveCamera(0,
				-camMoveSpeed);
		if(Gdx.input.isKeyPressed(Keys.BACKSPACE)) 
			cameraHelper.setPosition(0, 0);
		//Camera controls(zoom)
		float camZoomSpeed = 1*deltaTime;
		float camZoomSpeedAccelerationFactor =5;
		if(Gdx.input.isKeyPressed(Keys.SHIFT_LEFT))camZoomSpeed *=
				camZoomSpeedAccelerationFactor;
		if(Gdx.input.isKeyPressed(Keys.COMMA))
			cameraHelper.addZoom(camZoomSpeed);
		if(Gdx.input.isKeyPressed(Keys.PERIOD))cameraHelper.addZoom(
				-camZoomSpeed);
		if(Gdx.input.isKeyPressed(Keys.SLASH))cameraHelper.setZoom(1);
			
	}
	
	/*
	 * 
	 */
	private void moveSelectedSprite(float x, float y){
		testSprites[selectedSprite].translate(x, y);
	}
	
	/*
	 * 
	 */
	private void moveCamera(float x, float y){
	 x += cameraHelper.getPosition().x;
	 y += cameraHelper.getPosition().y;
	 cameraHelper.setPosition(x,y);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.badlogic.gdx.InputAdapter#keyUp(int)
	 */
	@Override
	public boolean keyUp(int keycode){
		//Reset game world
		if(keycode ==Keys.R){
			init();
			Gdx.app.debug(TAG, "Game world restarted");
		}
		else if(keycode ==Keys.SPACE){
			selectedSprite = (selectedSprite + 1) % testSprites.length;
			//Update cameras target to follow current sprite
			if(cameraHelper.hasTarget()){
				cameraHelper.setTarget(testSprites[selectedSprite]);
			}
			Gdx.app.debug(TAG, "Sprite #"+ selectedSprite+"selected");
		}
		//Toggle camera follow
		else if(keycode == Keys.ENTER){
			cameraHelper.setTarget(cameraHelper.hasTarget() ? null:
				testSprites[selectedSprite]);
			Gdx.app.debug(TAG, "Camera follow enabled:" +
				cameraHelper.hasTarget());
		}
		return false;
	}
}
	
