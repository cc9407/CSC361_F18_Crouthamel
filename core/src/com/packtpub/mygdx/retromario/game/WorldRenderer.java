/*
 * Author: Christian Crouthamel
 * Project: Retro Mario
 * Class: VGP 
 * Professor: Dr.Girard
 * Description: world renderer class
 */

package com.packtpub.mygdx.retromario.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.packtpub.mygdx.retromario.util.Constants;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class WorldRenderer implements Disposable {
	
	private OrthographicCamera camera;
	private OrthographicCamera cameraGUI;
	private SpriteBatch batch;
	private WorldController worldController;
	
	public WorldRenderer(WorldController worldController) {
		this.worldController = worldController; // instance of world controller
		init(); // initialize
	}
	
	/*
	 * init method
	 */
	private void init () {
		batch = new SpriteBatch(); // create a batch of sprites
		camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH,
				Constants.VIEWPORT_HEIGHT); // makes the camera
		camera.position.set(0, 0, 0); // set origin position
		camera.update(); // update to new position
		
		// creates a second camera specifically set up just to render
		// the game's GUI
		cameraGUI = new OrthographicCamera(Constants.VIEWPORT_WIDTH,
				Constants.VIEWPORT_HEIGHT); // makes the camera
		cameraGUI.position.set(0, 0, 0); // set origin position for GUI camera
		cameraGUI.setToOrtho(true); // flip y-axis
		cameraGUI.update(); // makes sure the camera's updated
	}
	
	/*
	 *  resizes the dimension of the world
	 */
	public void resize (int width, int height) {
		camera.viewportWidth = (Constants.VIEWPORT_HEIGHT / height) *
				width; // changes dimensions of camera view
		camera.update(); // updates camera
		
		// GUI camera settings
		cameraGUI.viewportHeight = Constants.VIEWPORT_GUI_HEIGHT;
		cameraGUI.viewportWidth = (Constants.VIEWPORT_GUI_HEIGHT
				/ (float)height) * (float)width; // dimensions
		cameraGUI.position.set(cameraGUI.viewportWidth / 2,
				cameraGUI.viewportHeight / 2, 0); // position of GUI camera
		cameraGUI.update(); // updates camera
	}
	
	/*
	 *  calls renderWorld to draw the game objects of the loaded level
	 */
	public void render () {
		renderWorld(batch);
		renderGui(batch);
	}
	
	/*
	 *  called by render
	 */
	private void renderWorld (SpriteBatch batch) {
		worldController.cameraHelper.applyTo(camera);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		worldController.level.render(batch);
		batch.end();
	}
	
	/*
	 *  renders all of the GUI elements to be displayed
	 */
	private void renderGui (SpriteBatch batch) {
		batch.setProjectionMatrix(cameraGUI.combined);
		batch.begin();
		// draw collected gold coins icon + text
		// (anchored to top left edge)
		//renderGuiScore(batch);
		// draw extra lives icon + text
		// (anchored to top right edge)
		renderGuiExtraLive(batch);
		// draw FPS text
		// (anchored to bottom right edge)
		renderGuiFpsCounter(batch);
		batch.end();
	}
	
	/* renders the GUI elements, specifically the overlaying score (with font)
	private void renderGuiScore (SpriteBatch batch) {
		float x = -15;
		float y = -15;
		// draw the gold coin in the top left corner, by the score
		batch.draw(Assets.instance.goldCoin.goldCoin,
				x, y, 50, 50, 100, 100, 0.35f, -0.35f, 0);
		// draws the score in the stored font
		Assets.instance.fonts.defaultBig.draw(batch,
				"" + worldController.score, x + 75, y + 37);
	}*/
	
	// renders the GUI's extra life elements, specifically the three
	// bunny heads to mark the amount of lives the player has
	// in the top right
	private void renderGuiExtraLive (SpriteBatch batch) {
		float x = cameraGUI.viewportWidth - 50 - Constants.LIVES_START * 50;
		float y = -15;
		// keeps track of the lives from the start of the game
		for (int i = 0; i < Constants.LIVES_START; i++) {
			// grays out the bunny heads when one is lost
			if (worldController.lives <= i)
				batch.setColor(0.5f, 0.5f, 0.5f, 0.5f);
			// draws the bunny heads
			batch.draw(Assets.instance.mario.head,
					x + i * 50, y, 50, 50, 120, 100, 0.35f, -0.35f, 0);
			batch.setColor(1, 1, 1, 1); // sets a color for the bunny head
		}
	}
	
	/*
	 *  FPS counter for the GUI that changes color
	 *
	 *depending on how low the FPS gets (located in the bottom right)
	 */
	private void renderGuiFpsCounter (SpriteBatch batch) {
		float x = cameraGUI.viewportWidth - 55;
		float y = cameraGUI.viewportHeight - 15;
		// grab the frames per second
		int fps = Gdx.graphics.getFramesPerSecond();
		// set the font style
		BitmapFont fpsFont = Assets.instance.fonts.defaultNormal;
		// check for how many FPS the system is getting
		if (fps >= 45) {
			// 45 or more FPS show up in green
			fpsFont.setColor(0, 1, 0, 1); // green
		} else if (fps >= 30) {
			// 30 or more FPS show up in yellow
			fpsFont.setColor(1, 1, 0, 1); // yellow
		} else {
			// less than 30 FPS show up in red
			fpsFont.setColor(1, 0, 0, 1); // red
		}
		// draw the FPS display
		fpsFont.draw(batch, "FPS: " + fps, x, y);
		fpsFont.setColor(1, 1, 1, 1); // white
	}
	
	/*
	 *  disposes of unused resources that build up
	 *  (non-Javadoc)
	 * @see com.badlogic.gdx.utils.Disposable#dispose()
	 */
	@Override public void dispose () {
		batch.dispose();
	}
}