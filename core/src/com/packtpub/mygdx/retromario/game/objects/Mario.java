package com.packtpub.mygdx.retromario.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.packtpub.mygdx.retromario.game.Assets;
import com.packtpub.mygdx.retromario.game.WorldController;
import com.packtpub.mygdx.retromario.util.CharacterSkin;
import com.packtpub.mygdx.retromario.util.Constants;
import com.packtpub.mygdx.retromario.util.GamePreferences;

public class Mario extends AbstractGameObject
{

	//instance variables and objects 
	public static final String TAG = Mario.class.getName();
	private final float JUMP_TIME_MAX = 0.3f;
	private final float JUMP_TIME_MIN = 0.1f;
	private final float JUMP_TIME_OFFSET_FLYING = JUMP_TIME_MAX - 0.018f;
	
	public enum VIEW_DIRECTION { LEFT, RIGHT }
	public enum JUMP_STATE {GROUNDED, FALLING, JUMP_RISING, JUMP_FALLING}
	
	private TextureRegion regHead;
	public VIEW_DIRECTION viewDirection;
	public float timeJumping;
	public JUMP_STATE jumpState;
	public boolean hasLeafPowerup;
	public float timeLeftLeafPowerup;
	
	public Fixture playerPhysicsFixture;
	public Fixture playerSensorFixture;
	
	private Animation<TextureRegion> animMario;
	
	public ParticleEffect marioParticles = new ParticleEffect();
	
	/*
	 * mario constructor 
	 */
	public Mario() {
	    super();  
		init();
	    }
	
	/*
	 * init method for mario
	 */
	public void init () {
		
		dimension.set(1, 1);
		regHead = Assets.instance.mario.head;
		// Center image on game object
		origin.set(dimension.x / 2, dimension.y / 2);
		// Bounding box for collision detection
		bounds.set(0, 0, dimension.x, dimension.y);
		
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		Body box = WorldController.b2world.createBody(bodyDef);
		PolygonShape poly = new PolygonShape();
		poly.setAsBox(0.4f, 0.4f);
		playerPhysicsFixture = box.createFixture(poly, 1);
		playerSensorFixture = box.createFixture(poly, 0);
		poly.dispose();
		
		body = box;
		body.setFixedRotation(true);
		body.setUserData(this);
		
		// Set physics values
		terminalVelocity.set(3.0f, 4.0f);
		friction.set(12.0f, 0.0f);
		acceleration.set(0.0f, -25.0f);
		// View direction
		viewDirection = VIEW_DIRECTION.RIGHT;
		// Jump state
		jumpState = JUMP_STATE.FALLING;
		timeJumping = 0;
		// Power-ups
		hasLeafPowerup = false;
		timeLeftLeafPowerup = 0;
		
		animMario = Assets.instance.mario.animMario;
		setAnimation(animMario);
		
		marioParticles.load(Gdx.files.internal("particles/leaf_particle.pfx"), Gdx.files.internal("particles"));
		//stateTime = MathUtils.random(0.0f,1.0f);
	};
	
	/*
	 * setter for jumping 
	 */
	public void setJumping (boolean jumpKeyPressed) {
		
		//chapter 6
		switch (jumpState) {
		case GROUNDED: // Character is standing on a platform
		if (jumpKeyPressed) {
		// Start counting jump time from the beginning
		timeJumping = 0;
		jumpState = JUMP_STATE.JUMP_RISING;
		}
		break;
		case JUMP_RISING: // Rising in the air
		if (!jumpKeyPressed)
		jumpState = JUMP_STATE.JUMP_FALLING;
		break;
		case FALLING:// Falling down
		case JUMP_FALLING: // Falling down after jump
			
		     if (jumpKeyPressed && hasLeafPowerup) {
				timeJumping = JUMP_TIME_OFFSET_FLYING;
				jumpState = JUMP_STATE.JUMP_RISING;
				}
		break;
		}
	};
	
	/*
	 * setter for leaf power up
	 */
	public void setLeafPowerup (boolean pickedUp) {
		
		hasLeafPowerup = pickedUp;
		if (pickedUp) {
		timeLeftLeafPowerup =Constants.ITEM_LEAF_POWERUP_DURATION;
		marioParticles.setPosition(body.getPosition().x + dimension.x / 2,body.getPosition().y);
		marioParticles.start();
		}
	};
	
	/*
	 * boolean for leaf power up
	 */
	public boolean hasLeafPowerup () {
		
		return hasLeafPowerup && timeLeftLeafPowerup > 0;
	};
	
	/*
	 * (non-Javadoc)
	 * @see com.packtpub.mygdx.retromario.game.objects.AbstractGameObject#update(float)
	 * update method 
	 */
	@Override
	public void update (float deltaTime) {
	super.update(deltaTime);
	if (velocity.x != 0) {
	viewDirection = velocity.x < 0 ? VIEW_DIRECTION.LEFT :
	VIEW_DIRECTION.RIGHT;
	}
//	
//	setAnimation(animMario);
	
	if (timeLeftLeafPowerup > 0) {
	timeLeftLeafPowerup -= deltaTime;
	marioParticles.update(deltaTime);
	    if (timeLeftLeafPowerup < 0) {
	         // disable power-up
		     timeLeftLeafPowerup = 0;
		     setLeafPowerup(false);
		     marioParticles.allowCompletion();
	         }
	  }	
   }
	
	/*
	 * (non-Javadoc)
	 * @see com.packtpub.mygdx.retromario.game.objects.AbstractGameObject#updateMotionY(float)
	 * update motion y 
	 */
	@Override
	protected void updateMotionY (float deltaTime) {
		switch (jumpState) {
		case GROUNDED:
			jumpState = JUMP_STATE.FALLING;
			break;
	case JUMP_RISING:
		// Keep track of jump time
		timeJumping += deltaTime;
		// Jump time left?
		if (timeJumping <= JUMP_TIME_MAX) {
			// Still jumping
			velocity.y = terminalVelocity.y;
		}
	
		break;
	
	case FALLING:
	
	break;
	
	case JUMP_FALLING:
		// Add delta times to track jump time
		timeJumping += deltaTime;
		// Jump to minimal height if jump key was pressed too short
		if (timeJumping > 0 && timeJumping <= JUMP_TIME_MIN) {
			// Still jumping
			velocity.y = terminalVelocity.y;
		}
	}
	
		if (jumpState != JUMP_STATE.GROUNDED)
	super.updateMotionY(deltaTime);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.packtpub.mygdx.retromario.game.objects.AbstractGameObject#render(com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	@Override
	public void render (SpriteBatch batch) {
		TextureRegion reg = null;
		
		// Apply Skin Color
		batch.setColor(CharacterSkin.values()[GamePreferences.instance.charSkin].getColor());
		// Set special color when game object has a leaf power-up
		if (hasLeafPowerup) {
			batch.setColor(1.0f, 0.8f, 0.0f, 1.0f);
			marioParticles.draw(batch);
		}
	
		// Draw image
		reg = animation.getKeyFrame(stateTime,true); //set reg var
		batch.draw(reg.getTexture(), position.x - 0.4f, position.y - 0.4f, origin.x,origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation,
				reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(),reg.getRegionHeight(), viewDirection == VIEW_DIRECTION.LEFT,false);
	
		// Reset color to white
		batch.setColor(1, 1, 1, 1);
	}
}