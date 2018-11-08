/*
 * Author: Christian Crouthamel
 * Project: Retro Mario
 * Class: VGP 
 * Professor: Dr.Girard
 * Description: world controller class
 */

package com.packtpub.mygdx.retromario.game;


import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.packtpub.mygdx.retromario.util.CameraHelper;
import com.packtpub.mygdx.retromario.util.Constants;


public class WorldController extends InputAdapter {

	//instance variables and objects
	private static final String TAG = WorldController.class.getName();
	public LevelOne level;
	public int lives;
	public int score;
	
	/*
	 * init level method
	 */
	private void initLevel() {
				score = 0;
				level = new LevelOne(Constants.LEVEL_01);
			}
	
	public CameraHelper cameraHelper;
			
	/*
	 * world controller constructor
	 */
	public WorldController()
		{
			init();
		}
			
	/*
	 * init method 
	 */
	private void init(){
			Gdx.input.setInputProcessor(this);
			cameraHelper = new CameraHelper();
			lives = Constants.LIVES_START;
			initLevel();
			}

	/*
	 * pixel map method		
	 */
	private Pixmap createProceduralPixmap(int width, int height)
		{
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
	 * update method
	 */
	public void update(float deltaTime)
	{
			handleDebugInput(deltaTime);
			cameraHelper.update(deltaTime);
				
	}

	/*
	 * debug input method 		
	 */
	private void handleDebugInput(float deltaTime)
	{
				if(Gdx.app.getType()!= ApplicationType.Desktop) return;
				
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
	 * move camera method		
	 */
	private void moveCamera(float x, float y)
	{
			x += cameraHelper.getPosition().x;
			y += cameraHelper.getPosition().y;
			cameraHelper.setPosition(x,y);
			}
			
		/*
		 * (non-Javadoc)
		 * @see com.badlogic.gdx.InputAdapter#keyUp(int)
		 * key up method
		 */
		@Override
		public boolean keyUp(int keycode)
		{
				//Reset game world
				if(keycode ==Keys.R){
					init();
					Gdx.app.debug(TAG, "Game world restarted");
				}
				
				return false;
			}
}