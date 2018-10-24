/*
 * Author: Christian Crouthamel
 * Project: Retro Mario
 * Class: VGP 
 * Professor: Dr.Girard
 * Description: world renderer class
 */

package com.packtpub.mygdx.retromario.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.packtpub.mygdx.retromario.game.WorldController;

public class WorldRenderer implements Disposable {

	//instance variables and objects 
	private SpriteBatch batch;
	private WorldController worldController;
	
	/**
	 * Initializes the world renderer and creates an
	 * instance for the world controller.
	 * @param worldController
	 */
	public WorldRenderer(WorldController worldController) {
		// instance of world controller
		this.worldController = worldController; 
		// initialize
		init(); 
	}
	
	/**
	 * Initializes the world renderer and its sprite batch,
	 * cameras, and GUI.
	 */
	private void init () {
		// create a batch of sprites
		batch = new SpriteBatch(); 
	}
	
	/**
	 * Calls renderWorld to draw the game objects of the loaded level.
	 */
	public void render () {
		renderWorld(batch);
	}
	
	/**
	 * Called by render.
	 * @param batch sprite batch
	 */
	private void renderWorld (SpriteBatch batch) {
		batch.begin();
		batch.end();
	}
	
	/**
	 * Disposes of unused resources that build up in Java
	 * and C under-layer.
	 */
	@Override public void dispose () {
		batch.dispose();
	}
}