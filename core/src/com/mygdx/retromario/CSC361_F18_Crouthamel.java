package com.mygdx.retromario;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.packtpub.mygdx.retromario.game.Assets;
import com.packtpub.mygdx.retromario.game.WorldController;
import com.packtpub.mygdx.retromario.game.WorldRenderer;

public class CSC361_F18_Crouthamel extends ApplicationAdapter {
	
		//batch and texture variables
		SpriteBatch batch;
		Texture img;
		
		//private static variable of type string
		private static final String TAG =
	    CSC361_F18_Crouthamel.class.getName();
		
		//private variable 
		private boolean paused;
		
		//private objects
		private WorldController worldController;
		private WorldRenderer worldRenderer;
				
		/*
		 * (non-Javadoc)
		 * @see com.badlogic.gdx.ApplicationAdapter#create()
		 * create method
		 */
		@Override 
		public void create () { 
		
			// Set Libgdx log level to DEBUG
			Gdx.app.setLogLevel(Application.LOG_DEBUG);
			// this allows you to see pink lines 
			
			// Load assets
			Assets.instance.init(new AssetManager());
			
			// Initialize controller and renderer
			worldController = new WorldController();
			worldRenderer = new WorldRenderer(worldController);
			
			// Game world is active on start
			paused = false;
		}
	    
		/*
		 * (non-Javadoc)
		 * @see com.badlogic.gdx.ApplicationAdapter#render()
		 * render method
		 */
		@Override 
		public void render () { 
			
			// Do not update game world when paused.
			if (!paused) {
			// Update game world by the time that has passed
			// since last rendered frame.
			worldController.update(Gdx.graphics.getDeltaTime());
			}
			
			// Sets the clear screen color to: Cornflower Blue
			Gdx.gl.glClearColor(0x64/255.0f, 0x95/255.0f, 0xed/255.0f,
			0xff/255.0f);
			
			// Clears the screen
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			
			// Render game world to screen
			worldRenderer.render();
		}
		
		/*
		 * (non-Javadoc)
		 * @see com.badlogic.gdx.ApplicationAdapter#resize(int, int)
		 * resize method 
		 */
		@Override 
		public void resize (int width, int height) { 
			worldRenderer.resize(width, height);
		}
		
		/*
		 * (non-Javadoc)
		 * @see com.badlogic.gdx.ApplicationAdapter#pause()
		 * pause method
		 */
		@Override 
		public void pause () {
			paused = true;
		}
		
		/*
		 * (non-Javadoc)
		 * @see com.badlogic.gdx.ApplicationAdapter#resume()
		 * resume method
		 */
		@Override 
		public void resume () { 
			Assets.instance.init(new AssetManager());
			paused = false;
		}
	    
		/*
		 * (non-Javadoc)
		 * @see com.badlogic.gdx.ApplicationAdapter#dispose()
		 * dispose method
		 */
		@Override 
		public void dispose () { 
			worldRenderer.dispose();
			Assets.instance.dispose();
	    }
}
