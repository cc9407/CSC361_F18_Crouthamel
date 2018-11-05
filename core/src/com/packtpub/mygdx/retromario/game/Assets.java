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
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;
import com.packtpub.mygdx.retromario.util.Constants;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;



public class Assets implements Disposable, AssetErrorListener {

	//instance variables
	public static final String TAG = Assets.class.getName(); 
	public static final Assets instance = new Assets();
	public AssetMario mario;
	public AssetRock rock;
	public AssetLeaf leaf;
	public AssetGoomba goomba;
	public AssetKoopa koopa;
	public AssetLevelDecoration levelDecoration;
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
		
		for(String a : assetManager.getAssetNames()) 
			Gdx.app.debug(TAG, "asset: "+a);
		
		TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS_OBJECTS);
		//enable texture filtering for pixel smoothing
		for(Texture t : atlas.getTextures()) {
			t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
				}
				//create game resource objects
				mario = new AssetMario(atlas);
				rock = new AssetRock(atlas);
				leaf = new AssetLeaf(atlas);
				goomba = new AssetGoomba(atlas);
				koopa = new AssetKoopa(atlas);
				levelDecoration = new AssetLevelDecoration(atlas);
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
	 * and will display mario
	 */
	public class AssetMario {
		public final AtlasRegion head;

		public AssetMario (TextureAtlas atlas) {
			head = atlas.findRegion("Mario");
			}
		}
	
	/*
	 * inner class to display leaf 
	 */
	public class AssetLeaf {
		public final AtlasRegion base;
		
		public AssetLeaf(TextureAtlas atlas) {
			base = atlas.findRegion("leaf");
		}
	}
	
	 /*
	 * inner class to display goomba 
	 */
	public class AssetGoomba {
		public final AtlasRegion enemy1;
		
		public AssetGoomba(TextureAtlas atlas) {
			enemy1 = atlas.findRegion("goomba");
		}
	}
	
	 /*
		 * inner class to display koopa
		 */
		public class AssetKoopa {
			public final AtlasRegion enemy2;
			
			public AssetKoopa(TextureAtlas atlas) {
				enemy2 = atlas.findRegion("koopa");
			}
		}
	
	/*
	 * asset level decorations 
	 */
	public class AssetLevelDecoration{
		public final AtlasRegion cloud;
		public final AtlasRegion mountainLeft;
		public final AtlasRegion mountainRight;
		
		public AssetLevelDecoration(TextureAtlas atlas) {
			cloud = atlas.findRegion("cloud");
			mountainLeft = atlas.findRegion("mountain_left");
			mountainRight = atlas.findRegion("mountain_right");
		}
	}
}