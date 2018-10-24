/*
 * Author: Christian Crouthamel
 * Project: Retro Mario
 * Class: VGP 
 * Professor: Dr.Girard
 */

package com.packtpub.mygdx.retromario.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;


public class Assets implements Disposable, AssetErrorListener {

	//instance variables
	public static final String TAG = Assets.class.getName(); 
	public static final Assets instance = new Assets();
	//asset manager object
	private AssetManager assetManager;
	public AssetFonts fonts;
	
	/*
	 * singleton: prevent instantiation from other classes
	 */
	private Assets() {

		}
	
	/*
	 * initialization method 
	 */
	public void init(AssetManager assetManager) {
		
		//set asset manager error handler
		assetManager.setErrorListener(this);
		
		//start loading assets and wait until finished
		assetManager.finishLoading();
		Gdx.app.debug(TAG, "# of assets loaded: " + assetManager.getAssetNames().size );
		
		//create game resource objects
		fonts = new AssetFonts();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.badlogic.gdx.utils.Disposable#dispose()
	 * dispose method to help with background C
	 */
	@Override
	public void dispose() {
		assetManager.dispose();
		fonts.defaultSmall.dispose();
		fonts.defaultNormal.dispose();
		fonts.defaultBig.dispose();
	}
	
	/*
	 * error method for loading assets
	 */
	public void error(String filename, Class type, Throwable throwable) {
		Gdx.app.error(TAG,  "Couldn't load asset '"+ filename + "'",(Exception)throwable);

	}
	
	/*
	 * (non-Javadoc)
	 * @see com.badlogic.gdx.assets.AssetErrorListener#error(com.badlogic.gdx.assets.AssetDescriptor, java.lang.Throwable)
	 */
	@Override
	public void error(AssetDescriptor asset, Throwable throwable) {
		Gdx.app.error(TAG, "Couldn't load asset '"+ asset.fileName + "'", (Exception)throwable);

	}
	
	/*
	 * asset fonts class
	 */
	public class AssetFonts {
		public final BitmapFont defaultSmall;
		public final BitmapFont defaultNormal;
		public final BitmapFont defaultBig;
		
		public AssetFonts () {
			// create three fonts using Libgdx's 15px bitmap font
			defaultSmall = new BitmapFont(
			Gdx.files.internal("images/arial-15.fnt"), true);
			defaultNormal = new BitmapFont(
			Gdx.files.internal("images/arial-15.fnt"), true);
			defaultBig = new BitmapFont(
			Gdx.files.internal("images/arial-15.fnt"), true);
			
			// set font sizes
			defaultSmall.getData().setScale(0.75f);
			defaultNormal.getData().setScale(1.0f);
			defaultBig.getData().setScale(2.0f);
			
			// enable linear texture filtering for smooth fonts
			defaultSmall.getRegion().getTexture().setFilter(
			TextureFilter.Linear, TextureFilter.Linear);
			defaultNormal.getRegion().getTexture().setFilter(
			TextureFilter.Linear, TextureFilter.Linear);
			defaultBig.getRegion().getTexture().setFilter(
			TextureFilter.Linear, TextureFilter.Linear);
		}
	}
}