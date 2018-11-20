package com.packtpub.mygdx.retromario.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.packtpub.mygdx.retromario.game.Assets;

public class WaterOverlay extends AbstractGameObject{

	//variables
		private TextureRegion regWaterOverlay;
		private float length;
		
		//constructor
		public WaterOverlay (float length) {
			
			this.length = length;
			init();
		}
			
		//init method
		private void init () {
			
			dimension.set(length * 10, 3);
			
			regWaterOverlay =
			Assets.instance.levelDecoration.waterOverlay;
			
			origin.x = -dimension.x / 2;
		}
			
		//render method
		@Override
		public void render (SpriteBatch batch) {
			TextureRegion reg = null;
			reg = regWaterOverlay;
			batch.draw(reg.getTexture(), position.x + origin.x, position.y
			+ origin.y, origin.x, origin.y, dimension.x, dimension.y, scale.x,
			scale.y, rotation, reg.getRegionX(), reg.getRegionY(),
			reg.getRegionWidth(), reg.getRegionHeight(), false, false);
			}
}