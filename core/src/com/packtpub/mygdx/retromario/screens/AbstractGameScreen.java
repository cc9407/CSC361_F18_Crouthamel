/*
 * Author: Christian Crouthamel
 * Project: Retro Mario
 * Class: VGP 
 * Professor: Dr.Girard
 * Description: Abstract game screen
 */

package com.packtpub.mygdx.retromario.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.packtpub.mygdx.retromario.game.Assets;

public abstract class AbstractGameScreen {

	//game object 
	protected Game game;
	
	//constructor for the Abstract Game Screen
	public AbstractGameScreen(Game game) {
		this.game = game;
	}

	/*
	 * methods abstract game screen will hold 
	 */
	public abstract void render(float deltaTime);

	public abstract void resize(int width, int height);

	public abstract void show();

	public abstract void hide();

	public abstract void pause();

	/*
	 * (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#resume()
	 * resume and dispose methods for the asset manager
	 */
	public void resume() {
		Assets.instance.init(new AssetManager());
	}


	public void dispose() {
		Assets.instance.dispose();
	}
}