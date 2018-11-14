/*
 * Author: Christian Crouthamel
 * Project: Retro Mario
 * Class: VGP 
 * Professor: Dr.Girard
 * Description: Camera helper class
 */

package com.packtpub.mygdx.retromario.util;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.packtpub.mygdx.retromario.game.objects.AbstractGameObject;

public class CameraHelper {
private static final String TAG = CameraHelper.class.getName();
	
	//instance variable from chapter8
	private final float FOLLOW_SPEED = 4.0f;
	
	// dimensions
	private final float MAX_ZOOM_IN = 0.25f;
	private final float MAX_ZOOM_OUT = 10.0f;
	
	// position, zoom, target variables
	private Vector2 position;
	private float zoom;
	private AbstractGameObject target;
	
	/**
	 * Initialize the camera helper with the vector position
	 * and the zoom magnification (dimensions).
	 */
	public CameraHelper () {
		position = new Vector2();
		zoom = 1.0f;
	}
	
	/**
	 * Update the camera for movements to keep up to date
	 * with the time and inputs.
	 * @param deltaTime current game time
	 */
	public void update (float deltaTime) {
		// return if no target
		if (!hasTarget()) return ;
		
		// updates the current position
		position.x = target.position.x + target.origin.x;
		position.y = target.position.y + target.origin.y;
		
		//allows the camera to follow
		position.lerp(target.position, FOLLOW_SPEED * deltaTime);
		
		// Prevents camera from moving down too far
		position.y = Math.max(-1f, position.y);
	}
	
	/**
	 * Sets the new current position for the camera.
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
	public void setPosition (float x, float y) {
		this.position.set(x, y);
	}
	
	/**
	 * Gets the position for Vector2.
	 * @return position current Vector2 position
	 */
	public Vector2 getPosition () { return position; }
	
	/**
	 * Adds a new zoom setting for the camera.
	 * @param amount how much to be zoomed
	 */
	public void addZoom (float amount) {setZoom(zoom + amount); }
	
	/**
	 * Sets the new zoom for the camera.
	 * @param zoom how much to be zoomed
	 */
	public void setZoom (float zoom) {
		this.zoom = MathUtils.clamp(zoom,  MAX_ZOOM_IN,  MAX_ZOOM_OUT);
	}
	
	/**
	 * Gets the zoom scale for the camera dimensions.
	 * @return zoom the zoom scale for the dimensions of camera
	 */
	public float getZoom () { return zoom; }
	
	/**
	 * Sets the target of focus for the camera.
	 * @param target the target of focus
	 */
	public void setTarget (AbstractGameObject target) { this.target = target; }
	
	/**
	 * Gets the target and builds a Sprite for it.
	 * @return target the target to build
	 */
	public AbstractGameObject getTarget () { return target; }
	
	/**
	 * Checks if the camera has a target.
	 * @return target boolean dependent on if the camera has a target
	 */
	public boolean hasTarget () { return target != null; }
	
	/**
	 * Checks if the camera's target has a sprite.
	 * @param target the target to be checked
	 * @return whether or not the sprite target matches with found target
	 */
	public boolean hasTarget (AbstractGameObject target) {
		return hasTarget() && this.target.equals(target);
	}
	
	/**
	 * Apply any changes made to the camera.
	 * @param camera camera to be changed with new settings
	 */
	public void applyTo (OrthographicCamera camera) {
		camera.position.x = position.x;
		camera.position.y = position.y;
		camera.zoom = zoom;
		camera.update();
	}}