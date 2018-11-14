package com.mygdx.retromario;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.packtpub.mygdx.retromario.game.Assets;
import com.packtpub.mygdx.retromario.game.WorldController;
import com.packtpub.mygdx.retromario.game.WorldRenderer;
import com.packtpub.mygdx.retromario.screens.MenuScreen;

public class CSC361_F18_Crouthamel extends Game {
	
	@Override
	public void create () {
	// Set Libgdx log level
	Gdx.app.setLogLevel(Application.LOG_DEBUG);
	// Load assets
	Assets.instance.init(new AssetManager());
	// Start game at menu screen
	setScreen(new MenuScreen(this));
	}
}
