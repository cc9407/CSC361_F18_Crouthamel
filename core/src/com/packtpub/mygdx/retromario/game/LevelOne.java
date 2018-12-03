package com.packtpub.mygdx.retromario.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.packtpub.mygdx.retromario.game.objects.AbstractGameObject;
import com.packtpub.mygdx.retromario.game.objects.Rock;
import com.packtpub.mygdx.retromario.game.objects.Mountains;
import com.packtpub.mygdx.retromario.game.objects.Clouds;
import com.packtpub.mygdx.retromario.game.objects.GoldCoin;
import com.packtpub.mygdx.retromario.game.objects.Leaf;
import com.packtpub.mygdx.retromario.game.objects.Mario;
import com.packtpub.mygdx.retromario.game.objects.WaterOverlay;

public class LevelOne 
{
	public static final String TAG = LevelOne.class.getName();
	
	// object member variables
		public Mario mario;
		public Array<Leaf> leaves;
		public Array<GoldCoin> goldcoins;
	
		
	public enum BLOCK_TYPE {
		EMPTY(0, 0, 0), // black
		ROCK(0, 255, 0), // green
		PLAYER_SPAWNPOINT(255, 255, 255), // white
		ITEM_LEAF(255, 165, 0), //orange
		ITEM_GOLD_COIN(255, 255, 0); // yellow
		
		private int color;
	
		// checks a block type based on color
		private BLOCK_TYPE (int r, int g, int b) {
			color = r << 24 | g << 16 | b << 8 | 0xff;
		}
	
		// checks to see if two colors are exactly the same
		public boolean sameColor (int color) {
			return this.color == color;
		}
	
		// gets the color of the block
		public int getColor () {
			return color;
		}
	}
	
		// objects
		public Array<Rock> rocks;
		
		// decoration
		public Clouds clouds;
		public Mountains mountains;
		public WaterOverlay waterOverlay;
		
		
		// initiates the level through a filename
		public LevelOne (String filename) {
			init(filename);
			mario.position.y = 1.0f;
		}
		
		private void init (String filename) {
			// rocks
			rocks = new Array<Rock>();
			//player
			mario = null;
			//leaves array
			leaves = new Array<Leaf>();
			//coins
			goldcoins = new Array<GoldCoin>();
			
			
			
			// load image file that represents the level data
			Pixmap pixmap = new Pixmap(Gdx.files.internal(filename));
			// scan pixels from top-left to bottom-right
			int lastPixel = -1;
			for (int pixelY = 0; pixelY < pixmap.getHeight(); pixelY++) {
				for (int pixelX = 0; pixelX < pixmap.getWidth(); pixelX++) {
					AbstractGameObject obj = null;
					float offsetHeight = 0;
					// height grows from bottom to top
					float baseHeight = pixmap.getHeight() - pixelY;
					// get color of current pixel as 32-bit RGBA value
					int currentPixel = pixmap.getPixel(pixelX,  pixelY);
					// find matching color value to identify block type at (x,y)
					// point and create the corresponding game object if there is
					// a match
					
					// empty space
					if (BLOCK_TYPE.EMPTY.sameColor(currentPixel)) {
						// do nothing
					}
					// rock
					else if (BLOCK_TYPE.ROCK.sameColor(currentPixel)) {
						if (lastPixel != currentPixel) {
							obj = new Rock();
							float heightIncreaseFactor = 0.25f;
							offsetHeight = -2.5f;
							obj.position.set(pixelX, baseHeight * obj.dimension.y 
									* heightIncreaseFactor + offsetHeight);
							rocks.add((Rock)obj);
						} else {
							rocks.get(rocks.size - 1).increaseLength(1);
						}
					}
					// player spawn point
					else if
						(BLOCK_TYPE.PLAYER_SPAWNPOINT.sameColor(currentPixel)) {
						obj = new Mario();
						offsetHeight = -3.0f;
						obj.position.set(pixelX,baseHeight * obj.dimension.y + offsetHeight);
						mario = (Mario)obj;
					}
					// leaf
					else if
						(BLOCK_TYPE.ITEM_LEAF.sameColor(currentPixel)) {
							obj = new Leaf();
							offsetHeight = -1.5f;
							obj.position.set(pixelX,baseHeight * obj.dimension.y + offsetHeight);
							// set the leaf then add it to the leaf array
							leaves.add((Leaf)obj);
					}
					// gold coin
					else if
						(BLOCK_TYPE.ITEM_GOLD_COIN.sameColor(currentPixel)) {
						obj = new GoldCoin();
						offsetHeight = -1.5f;
						obj.position.set(pixelX,baseHeight * obj.dimension.y + offsetHeight);
						// set the gold coin then add it to the gold coins array
						goldcoins.add((GoldCoin)obj);
					}
					// unknown object
					else {
						int r = 0xff & (currentPixel >>> 24); //red color channel
						int g = 0xff & (currentPixel >>> 16); //green color channel
						int b = 0xff & (currentPixel >>> 8); //blue color channel
						int a = 0xff & currentPixel; //alpha channel
						Gdx.app.error(TAG, "Unknown object at x<" + pixelX + "> y<"
								+ pixelY + ">: r<" + r+ "> g<" + g + "> b<" + b + "> a<" + a + ">");
					}
					lastPixel = currentPixel;
					}
				}
			
			// decoration
			clouds = new Clouds(pixmap.getWidth());
			clouds.position.set(0, 2);
			mountains = new Mountains(pixmap.getWidth());
			mountains.position.set(-1, -1);
			waterOverlay = new WaterOverlay(pixmap.getWidth());
			waterOverlay.position.set(0, -3.75f);
			
			// free memory
			pixmap.dispose();
			Gdx.app.debug(TAG,  "level '" + filename + "' loaded");
		}
		
		
		public void render (SpriteBatch batch) {
			// Draw Mountains
			mountains.render(batch);
			
			// Draw Rocks
			for (Rock rock : rocks)
				rock.render(batch);
			
			// Draw Gold Coins
			for (GoldCoin goldCoin : goldcoins)
				goldCoin.render(batch);
			
			// Draw Clouds
			clouds.render(batch);
			
			// Draw Water Overlay
			waterOverlay.render(batch);
			
			// Draw Player Character
			mario.render(batch);
			
			// Draw Feathers
			for (Leaf leaf : leaves)
				leaf.render(batch);
		}
		
		public void update (float deltaTime) {
			// update the bunny head
			// aka: where am I, have I touched anything,
			// what am I doing
			mario.update(deltaTime);
			
			// update the rocks
			// aka: have I been touched, am I moving
			for(Rock rock : rocks)
				rock.update(deltaTime);
			
			// update the gold coins
			// aka: have they been picked up yet
			for(GoldCoin goldCoin : goldcoins)
				goldCoin.update(deltaTime);
			
			// update the feathers
			// aka: have they been picked up yet
			for(Leaf leaf : leaves)
				leaf.update(deltaTime);
			
			// keeps the clouds moving
			clouds.update(deltaTime);
		}
}