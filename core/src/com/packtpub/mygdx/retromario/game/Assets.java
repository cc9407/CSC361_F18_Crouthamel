/*
 * Author: Christian Crouthamel
 * Project: Retro Mario
 * Class: VGP 
 * Professor: Dr.Girard
 * Description: Assets class
 */

package com.packtpub.mygdx.retromario.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;
import com.packtpub.mygdx.retromario.util.Constants;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;



public class Assets implements Disposable, AssetErrorListener {

	//instance variables
	public static final String TAG = Assets.class.getName(); 
	public static final Assets instance = new Assets();
	//asset manager object
	private AssetManager assetManager;
	
	/*
	 * singleton: prevent instantiation from other classes
	 */
	private Assets() {

		}
	
	/*
	 * initialization method 
	 */
	public void init(AssetManager assetManager) {
		
		this.assetManager = assetManager;
		//set asset manager error handler
		assetManager.setErrorListener(this);
		
		//load texture atlas
		assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);
		
		//start loading assets and wait until finished
		assetManager.finishLoading();
		Gdx.app.debug(TAG, "# of assets loaded: " + assetManager.getAssetNames().size );
		
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.badlogic.gdx.utils.Disposable#dispose()
	 * dispose method to help with background C
	 */
	@Override
	public void dispose() {
		assetManager.dispose();
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
	 *  Builds the rock platforms based on its edges around each central piece
	 */
	public class AssetRock {
		public final AtlasRegion edge;
		public final AtlasRegion middle;

		public AssetRock (TextureAtlas atlas) {
			edge = atlas.findRegion("rock_edge");
			middle = atlas.findRegion("rock_middle");
		}
	}

	/*
	 *  This inner class contains a member variable called "head"
	 * and will display the bunny head
	 */
	public class AssetMario {
		public final AtlasRegion head;

		public AssetMario (TextureAtlas atlas) {
			head = atlas.findRegion("Mario");
			}
		}
}