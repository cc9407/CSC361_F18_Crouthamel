package com.packtpub.mygdx.retromario.game.objects;

import com.packtpub.mygdx.retromario.game.Assets;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Rock extends AbstractGameObject
{
	//instance variables and objects 
	private TextureRegion regEdge;
	private TextureRegion regMiddle;
	private int length;

	/*
	 * rock constructor 
	 */
	public Rock() {
		init();
	}
	
	/*
	 * init method for rock
	 */
	private void init() {
//		dimension.set(1,1.5f);
		dimension.set(1.0f, 1.0f);
		regEdge = Assets.instance.rock.edge;//set edge assets
		regMiddle = Assets.instance.rock.middle; //set middle assets
//		//Start length of this rock
		setLength(1);
	}
	
	/*
	 * setter for length 
	 */
	public void setLength(int length) {
		this.length = length; //set rock length to val
		bounds.set(0,0,dimension.x, dimension.y);
	}
	
	/*
	 * method to increase the length 
	 */
	public void increaseLength(int amount) {
		setLength(length + amount); //increase rock length
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.packtpub.mygdx.retromario.game.objects.AbstractGameObject#render(com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 * rock's render method 
	 */
	@Override
	public void render(SpriteBatch batch) {
		TextureRegion reg = null;
		
		float relX = 0;
		float relY = 0;
		
		reg = regMiddle;
		batch.draw(reg.getTexture(), position.x, position.y, 
				origin.x, origin.y, dimension.x, dimension.y,
				scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(),
				reg.getRegionWidth(), reg.getRegionHeight(), false, false);
		
		//Draw left edge
//		reg = regEdge;
//		relX -=dimension.x / 4;
//		batch.draw(reg.getTexture(), position.x + relX, position.y +
//				relY, origin.x, origin.y, dimension.x / 4, dimension.y,
//				scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(),
//				reg.getRegionWidth(), reg.getRegionHeight(), false, false);
//		
//		//Draw middle
//		relX = 0;
//		reg = regMiddle;
//		for (int i = 0; i < length; i++) {
//			batch.draw(reg.getTexture(), position.x + relX, position.y
//			+ relY, origin.x, origin.y, dimension.x, dimension.y,
//			scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(),
//			reg.getRegionWidth(), reg.getRegionHeight(), false, false);
//			relX += dimension.x;
//			}
//		
//		//Draw right edge
//		reg = regEdge;
//		batch.draw(reg.getTexture(),position.x + relX, position.y +
//				relY, origin.x + dimension.x / 8, origin.y, dimension.x / 4,
//				dimension.y, scale.x, scale.y, rotation, reg.getRegionX(),
//				reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(),
//				true, false);
		}
}