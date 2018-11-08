package com.packtpub.mygdx.retromario.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.packtpub.mygdx.retromario.game.Assets;

public class Leaf extends AbstractGameObject
{

	// texture region for the leaf
		private TextureRegion regLeaf;
		
		// has mario collided with the leaf
		public boolean collected;
		
		/**
		 * Initialize the feather.
		 */
		public Leaf() {
			init();
		}
		
		/**
		 * Initialize the dimension and image for the leaf.
		 * Make sure its bound box is set for when mario
		 * collides with the leaf.
		 */
		private void init () {
			// sets the size of the leaf
			dimension.set(0.5f, 0.5f);
			// grabs the image for the leaf
			regLeaf = Assets.instance.leaf.base;
			
			// Set bounding box for collision detection
			bounds.set(0, 0, dimension.x, dimension.y);
			
			// flag for item collection
			collected = false;
		}
		
		/**
		 * Renders the leaf into the game world.
		 */
		public void render (SpriteBatch batch) {
			// if it's collected, don't render
			if (collected) return;
			
			// reset the texture region
			TextureRegion reg = null;
			reg = regLeaf;
			batch.draw(reg.getTexture(), position.x, position.y,
					origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y,
					rotation, reg.getRegionX(), reg.getRegionY(),
					reg.getRegionWidth(), reg.getRegionHeight(), false, false);
		}
		
		/*
		 * gettter for score 
		 */
		public int getScore() {
			return 250;
		}
}