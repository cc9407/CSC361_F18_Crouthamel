package com.packtpub.mygdx.retromario.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.packtpub.mygdx.retromario.game.Assets;
import com.packtpub.mygdx.retromario.game.WorldController;

public class Goal extends AbstractGameObject {
	
	private TextureRegion regGoal; //texture info for goal
	public Fixture playerPhysicsFixture;
	public Fixture playerSensorFixture;
	
	public Goal() {
		init();
	 }

	 private void init() {
		 dimension.set(2, 2);
			regGoal = Assets.instance.levelDecoration.goal;
			// Center image on game object
			origin.set(dimension.x-1, dimension.y);
			// Bounding box for collision detection
			bounds.set(0, 0, dimension.x, dimension.y);
			
			bodyDef = new BodyDef();
			bodyDef.type = BodyType.StaticBody;
			Body box = WorldController.b2world.createBody(bodyDef);
			PolygonShape poly = new PolygonShape();
			poly.setAsBox(0.55f, -2.05f);
			playerPhysicsFixture = box.createFixture(poly, 1);
			playerSensorFixture = box.createFixture(poly, 0);
			poly.dispose();
			
			body = box;
			body.setFixedRotation(true);
			body.setUserData(this);
	 }
	  public void render(SpriteBatch batch) {
		  TextureRegion reg = regGoal;
	  
	      batch.draw(reg.getTexture(), position.x - origin.x,
	    		  position.y - origin.y, origin.x, origin.y,dimension.x,
	    		  dimension.y,scale.x,scale.y,rotation,
	    		  reg.getRegionX(), reg.getRegionY(),
	    		  reg.getRegionWidth(),reg.getRegionHeight(),
	    		  false,false);
	  }

}
