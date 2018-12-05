/*
 * Author: Christian Crouthamel
 * Project: Retro Mario
 * Class: VGP 
 * Professor: Dr.Girard
 * Description: Abstract game object
 */

package com.packtpub.mygdx.retromario.game.objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;


public abstract class AbstractGameObject {

	
	// instance variables and objects
		public Vector2 position;
		public Vector2 dimension;
		public Vector2 origin;
		public Vector2 scale;
		public float rotation;
		public Vector2 velocity;
		public Vector2 terminalVelocity;
		public Vector2 friction;
		public Vector2 acceleration;
		public Rectangle bounds;
		public Body body;
		public BodyDef bodyDef;
		public PolygonShape poly;
		public Fixture fixture;
		public float stateTime;
		public Animation<TextureRegion> animation;
		
		/*
		 * abstract constructor
		 */
		public AbstractGameObject () {
			position = new Vector2();
			dimension = new Vector2(1, 1);
			origin = new Vector2();
			scale = new Vector2(1, 1);
			rotation = 0;
			//chapter 6
			velocity = new Vector2();
			terminalVelocity = new Vector2(1, 1);
			friction = new Vector2();
			acceleration = new Vector2();
			bounds = new Rectangle();
		}
		
		/**
		 * Change the current animation and set the state time
		 * to 0.
		 * @param animation the current animation
		 */
		public void setAnimation(Animation<TextureRegion> animation){
			this.animation = animation;
			stateTime = 0;
		}
			
		
		
		/*
		 * update method for the abstract game object class
		 */
		public void update (float deltaTime) {
			stateTime += deltaTime;
			//chapter 11 adds if statement
			if (body == null) {
			//chapter 6
			updateMotionX(deltaTime);
			updateMotionY(deltaTime);
			// Move to new position
			position.x += velocity.x * deltaTime;
			position.y += velocity.y * deltaTime;
			} else {
				position.set(body.getPosition());
				rotation = body.getAngle() * MathUtils.radiansToDegrees;
				}
		}
		
		/*
		 * abstract render method 
		 */
		public abstract void render (SpriteBatch batch);
		
		/*
		 * update motion x
		 */
		protected void updateMotionX (float deltaTime) {
			
			if (velocity.x != 0) {
			// Apply friction
			if (velocity.x > 0) {
			       velocity.x =
			       Math.max(velocity.x - friction.x * deltaTime, 0);
			} 
			else {
			      velocity.x =
			      Math.min(velocity.x + friction.x * deltaTime, 0);
			     }
			}
			
			// Apply acceleration
			velocity.x += acceleration.x * deltaTime;
			// Make sure the object's velocity does not exceed the
			// positive or negative terminal velocity
			velocity.x = MathUtils.clamp(velocity.x,
			-terminalVelocity.x, terminalVelocity.x);
			}
			
		    /*
		     * motion y method
		     */
		    protected void updateMotionY (float deltaTime) {
			
		    if (velocity.y != 0) {
			// Apply friction
			if (velocity.y > 0) {
			     velocity.y = Math.max(velocity.y - friction.y *
			     deltaTime, 0);
			} 
			else {
			        velocity.y = Math.min(velocity.y + friction.y *
			        deltaTime, 0);
			       }
			}
			
			// Apply acceleration
			velocity.y += acceleration.y * deltaTime;
			// Make sure the object's velocity does not exceed the
			// positive or negative terminal velocity
			velocity.y = MathUtils.clamp(velocity.y, -
					terminalVelocity.y, terminalVelocity.y);
			}
}