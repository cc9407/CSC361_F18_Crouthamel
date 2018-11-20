/*
 * Author: Christian Crouthamel
 * Project: Retro Mario
 * Class: VGP 
 * Professor: Dr.Girard
 * Description: world controller class
 */

package com.packtpub.mygdx.retromario.game;


import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.math.Rectangle;
import com.packtpub.mygdx.retromario.game.objects.Leaf;
import com.packtpub.mygdx.retromario.game.objects.Mario;
import com.packtpub.mygdx.retromario.game.objects.Mario.JUMP_STATE;
import com.packtpub.mygdx.retromario.game.objects.Rock;
import com.packtpub.mygdx.retromario.screens.MenuScreen;
import com.packtpub.mygdx.retromario.util.CameraHelper;
import com.packtpub.mygdx.retromario.util.Constants;


public class WorldController extends InputAdapter {

	private static final String TAG = WorldController.class.getName();
	public LevelOne level;
	public int lives;
	public int score;
	private Game game;
	public CameraHelper cameraHelper;
	
	public float livesVisual;
	public float scoreVisual;

	private Rectangle r1 = new Rectangle();
	private Rectangle r2 = new Rectangle();
	private float timeLeftGameOverDelay;

	/**
	 * Boolean checker method for if the game has ended
	 * @return true if lives are < 0
	 */
	public boolean isGameOver() {
		return lives < 0;
	}

	/**
	 * Checks if the player fell off screen
	 * @return
	 */
	public boolean isPlayerInWater() {
		return level.mario.position.y < -5;
	}

	/**
	 * Collision based methods
	 * @param rock the rock object
	 */
	private void onCollisionMarioWithRock(Rock rock) {
		Mario mario = level.mario;
		float heightDifference = Math.abs(mario.position.y - (rock.position.y + rock.bounds.height));
		if (heightDifference > 0.25f) {
			boolean hitRightEdge = mario.position.x > (rock.position.x + rock.bounds.width / 2.0f);
			if (hitRightEdge) {
				mario.position.x = rock.position.x + rock.bounds.width;
			} else {
				mario.position.x = rock.position.x - mario.bounds.width;
			}
			return;
		}

		switch (mario.jumpState) {
		case GROUNDED:
			break;
		case FALLING:
		case JUMP_FALLING:
			mario.position.y = rock.position.y + mario.bounds.height + mario.origin.y;
			mario.jumpState = JUMP_STATE.GROUNDED;
			break;
		case JUMP_RISING:
			mario.position.y = rock.position.y + mario.bounds.height + mario.origin.y;
			break;
		}
	};

	/**
	 * Bunny collides with a gold coin. Increase score.
	 * Set that gold coin's boolean value to true so
	 * it knows it's been collected.
	 * @param goldcoin
	 *
	private void onCollisionBunnyWithGoldCoin(GoldCoin goldcoin) {
		goldcoin.collected = true;
		score += goldcoin.getScore();
		Gdx.app.log(TAG, "Gold coin collected");
	};

	/**
	 * Bunny collides with a feather. Increase score.
	 * Set the powerup for the bunny to true that it has
	 * a feather and make sure the feather knows
	 * it's been collected.
	 * @param feather
	 */
	private void onCollisionMarioWithLeaf(Leaf leaf) {
		leaf.collected = true;
		score += leaf.getScore();
		level.mario.setLeafPowerup(true);
		Gdx.app.log(TAG, "Featehr collected");
	};

	/**
	 * Method for testing the collision of the bunnyhead with objects
	 */
	private void testCollisions() {
		r1.set(level.mario.position.x, level.mario.position.y, level.mario.bounds.width,
				level.mario.bounds.height);

		//Test collison: Bunnny <-> Rocks
		for (Rock rock : level.rocks) {
			r2.set(rock.position.x, rock.position.y, rock.bounds.width, rock.bounds.height);
			if (!r1.overlaps(r2))
				continue;
			onCollisionMarioWithRock(rock);
			// IMPORTANT: must do all collisions for valid
			// edge testing on rocks
		}
		/* Test collision: BunnyHead <-> Gold Coins
		for (GoldCoin goldcoin : level.goldcoins) {
			if (goldcoin.collected)
				continue;
			r2.set(goldcoin.position.x, goldcoin.position.y, goldcoin.bounds.width, goldcoin.bounds.height);
			if (!r1.overlaps(r2))
				continue;
			onCollisionBunnyWithGoldCoin(goldcoin);
			break;
		}*/
		// test collsion: Bunny Head <-> Feathers
		for (Leaf leaf  : level.leaves) {
			if (leaf.collected)
				continue;
			r2.set(leaf.position.x, leaf.position.y, leaf.bounds.width, leaf.bounds.height);
			if (!r1.overlaps(r2))
				continue;
			onCollisionMarioWithLeaf(leaf);
			break;
		}
	}

	/**
	 * Level initialization method
	 */
	private void initLevel() {
		score = 0;
		scoreVisual = score;
		level = new LevelOne(Constants.LEVEL_01);
		cameraHelper.setTarget(level.mario);
	}

	/**
	 * constructor for world controller
	 * @param game instance of the game/BunnyMain
	 */
	public WorldController(Game game) {
		this.game = game;
		init();
	}

	/**
	 * Initialize the world controller. Set lives.
	 * Get a CameraHelper.
	 */
	private void init() {
		Gdx.input.setInputProcessor(this);
		cameraHelper = new CameraHelper();
		lives = Constants.LIVES_START;
		livesVisual = lives;
		timeLeftGameOverDelay = 0;
		initLevel();
	}

	/**
	 * Build a pixmap
	 * @param width width of the procedural pixmap
	 * @param height height of the procedural pixmap
	 * @return the built pixmap
	 */
	private Pixmap createProceduralPixmap(int width, int height) {
		Pixmap pixmap = new Pixmap(width, height, Format.RGBA8888);
		// fill square w/ red color at 50% opacity
		pixmap.setColor(1, 0, 0, 0.5f);
		pixmap.fill();
		// Draw a yellow colored x shape on square
		pixmap.setColor(1, 1, 0, 1);
		pixmap.drawLine(0, 0, width, height);
		pixmap.drawLine(width, 0, 0, height);
		// draw a cyan colored border around square
		pixmap.setColor(0, 1, 1, 1);
		pixmap.drawRectangle(0, 0, width, height);
		return pixmap;
	}

	/**
	 * Updates the level (and its objects), update the 
	 * camera, handle game inputs, handle collisions.
	 * @param deltaTime the game time
	 */
	public void update(float deltaTime) {
		handleDebugInput(deltaTime);
		if (isGameOver()) {
			timeLeftGameOverDelay -= deltaTime;
			if (timeLeftGameOverDelay < 0)
				System.out.println("No menu yet");
		} else {
			handleInputGame(deltaTime);
		}
		level.update(deltaTime);
		testCollisions();
		cameraHelper.update(deltaTime);
		if (!isGameOver() && isPlayerInWater()) {
			lives--;
			if (isGameOver())
				timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_OVER;
			else
				initLevel();
		}
		// mountains scroll at different speeds
		level.mountains.updateScrollPosition(cameraHelper.getPosition());
		// life lost animation goes as livesVisual catches up to lives
		if (livesVisual > lives)
			livesVisual = Math.max(lives,  livesVisual - 1 * deltaTime);
		// score gain animation goes as scoreVisual catches up to new score
		if (scoreVisual < score)
			scoreVisual = Math.min(score,  scoreVisual 
					+ 250 * deltaTime);
	}

	/**
	 * Add functionality behind every user input.
	 * @param deltaTime game time
	 */
	private void handleDebugInput(float deltaTime) {
		if (Gdx.app.getType() != ApplicationType.Desktop)
			return;

		if (!cameraHelper.hasTarget(level.mario)) {
			// Camera Controls(move)
			float camMoveSpeed = 5 * deltaTime;
			float camMoveSpeedAccelerationFactor = 5;
			if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT))
				camMoveSpeed *= camMoveSpeedAccelerationFactor;
			if (Gdx.input.isKeyPressed(Keys.LEFT))
				moveCamera(-camMoveSpeed, 0);
			if (Gdx.input.isKeyPressed(Keys.RIGHT))
				moveCamera(camMoveSpeed, 0);
			if (Gdx.input.isKeyPressed(Keys.UP))
				moveCamera(0, camMoveSpeed);
			if (Gdx.input.isKeyPressed(Keys.DOWN))
				moveCamera(0, -camMoveSpeed);
			if (Gdx.input.isKeyPressed(Keys.BACKSPACE))
				cameraHelper.setPosition(0, 0);
			// Camera controls(zoom)
			float camZoomSpeed = 1 * deltaTime;
			float camZoomSpeedAccelerationFactor = 5;
			if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT))
				camZoomSpeed *= camZoomSpeedAccelerationFactor;
			if (Gdx.input.isKeyPressed(Keys.COMMA))
				cameraHelper.addZoom(camZoomSpeed);
			if (Gdx.input.isKeyPressed(Keys.PERIOD))
				cameraHelper.addZoom(-camZoomSpeed);
			if (Gdx.input.isKeyPressed(Keys.SLASH))
				cameraHelper.setZoom(1);

		}
	}

	/**
	 * Controls the x and y movements of the camera.
	 * @param x horizontal position
	 * @param y vertical position
	 */
	private void moveCamera(float x, float y) {
		x += cameraHelper.getPosition().x;
		y += cameraHelper.getPosition().y;
		cameraHelper.setPosition(x, y);
	}

	/**
	 * Checks for special key inputs that restart the world
	 * and change how the camera follows the player. Also
	 * handles escaping back to the menu screen.
	 */
	@Override
	public boolean keyUp(int keycode) {
		// Reset game world
		if (keycode == Keys.R) {
			init();
			Gdx.app.debug(TAG, "Game world restarted");
		}
		// Toggle camera follow
		else if (keycode == Keys.ENTER) {
			cameraHelper.setTarget(cameraHelper.hasTarget() ? null : level.mario);
			Gdx.app.debug(TAG, "Camera follow enabled: " + cameraHelper.hasTarget());
		}
		// Back to Menu
		else if (keycode == Keys.ESCAPE || keycode == Keys.BACK) {
			//backToMenu();
		}
		return false;
	}

	/**
	 * Handles the input keys for the game. These affect
	 * player movements in-game and change the physics affecting
	 * the player.
	 * @param deltaTime game time
	 */
	private void handleInputGame(float deltaTime) {
		if (cameraHelper.hasTarget(level.mario)) {
			// player movement
			if (Gdx.input.isKeyPressed(Keys.LEFT)) {
				level.mario.velocity.x = -level.mario.terminalVelocity.x;
			} else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
				level.mario.velocity.x = level.mario.terminalVelocity.x;
			} else {
				// Execute auto-forward movement on non-desktop platform
				if (Gdx.app.getType() != ApplicationType.Desktop) {
					level.mario.velocity.x = level.mario.terminalVelocity.x;
				}
			}
			// Bunny Jump
			if (Gdx.input.isTouched() || Gdx.input.isKeyPressed(Keys.SPACE)) {
				level.mario.setJumping(true);
			} else {
				level.mario.setJumping(false);
			}
		}
	}

	/**
	 * save a reference to the game instance
	 *
	private void backToMenu() {
		// switch to menu screen
		game.setScreen(new MenuScreen(game));
	}*/
}