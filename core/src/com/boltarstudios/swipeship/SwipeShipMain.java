package com.boltarstudios.swipeship;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

//package com.badlogic.drop;

import java.util.Iterator;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class SwipeShipMain implements ApplicationListener {
	private Texture dropImage;
	private Texture spaceshipImage;
	private Texture jetImage;
	private Texture blastImage;
	private Texture dustImage1;
	private Texture dustImage2;
	private Texture dustImage3;
	private Sound dropSound;
	private Sound blastSound;
	private Sound powerupSound;
	private Music bgMusic;
	private SpriteBatch batch;
	private OrthographicCamera camera;
	//private MyRect spaceship;
	//private Array<MyRect> raindrops;
	private Array<StellarObject> planets;
	private Array<StellarObject> asteroids;
	private Array<StellarObject> nebulas;
	private Array<StellarObject> projectiles;
	private Texture sunImage, exoPlanetImage, exoPlanet2Image, gasGiantImage;
	private long lastDropTime;
	private static final int VIRTUAL_WIDTH = 720;
	private static final int VIRTUAL_HEIGHT = 1280;
	private static final float ASPECT_RATIO =
			(float)VIRTUAL_HEIGHT/(float)VIRTUAL_WIDTH;
	private Rectangle viewport;
	private Viewport fitViewport;


	private long prevNanoTime;
	private long nanoTimeSum;
	private BitmapFont font;
	private Array<Sprite> powerups;

	private Sprite spaceshipSprite;
	private Sprite blastSprite;

//	private GameData gd;

	private float distanceTraveled;
	private float distanceToGo;
	private float globalSpeed;
	private float maxSpeed;
	private float maxCurrentSpeed;
	private float globalSpeedBonus;
	private int currentSector;

	public int i;


	Preferences prefs;

	private final Pool<StellarObject> nebulaPool = new Pool<StellarObject>() {
		@Override
		protected StellarObject newObject() {
			return new Nebula();
		}
	};
	private final Pool<StellarObject> asteroidPool = new Pool<StellarObject>() {
		@Override
		protected StellarObject newObject() {
			return new Asteroid();
		}
	};
	private final Pool<StellarObject> planetPool = new Pool<StellarObject>() {
		@Override
		protected StellarObject newObject() {
			return new Planet();
		}
	};
	private final Pool<StellarObject> projectilePool = new Pool<StellarObject>() {
		@Override
		protected StellarObject newObject() {
			return new Projectile();
		}
	};

	@Override
	public void create() {
		// load the images for the droplet and the spaceship, 64x64 pixels each
		dropImage = new Texture(Gdx.files.internal("asteroid2.png"));
		spaceshipImage = new Texture(Gdx.files.internal("spaceship_2.png"));
		jetImage = new Texture(Gdx.files.internal("spaceship2_jet.png"));
		dustImage1 = new Texture(Gdx.files.internal("dust1.png"));
		dustImage2 = new Texture(Gdx.files.internal("dust2.png"));
		dustImage3 = new Texture(Gdx.files.internal("dust3.png"));
		blastImage = new Texture(Gdx.files.internal("fire_green1.png"));

		spaceshipSprite = new Sprite(spaceshipImage);
		blastSprite = new Sprite(blastImage);
		// load the drop sound effect and the rain background "music"
		dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
		powerupSound = Gdx.audio.newSound(Gdx.files.internal("sd_0.wav"));
		bgMusic = Gdx.audio.newMusic(Gdx.files.internal("bg_music.wav"));

		// start the playback of the background music immediately
		//bgMusic.setLooping(true);
		//bgMusic.play();

		// create the camera and the SpriteBatch
		camera = new OrthographicCamera(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
		camera.setToOrtho(false, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
		//camera.setToOrtho(false);
		batch = new SpriteBatch();

		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.setScale(2);


		// create a Rectangle to logically represent the spaceship
		//spaceship = new MyRect();
		//spaceship.x = VIRTUAL_WIDTH / 2 - spaceshipImage.getWidth() / 2; // center the spaceship horizontally
		//spaceship.y = 20; // bottom left corner of the spaceship is 20 pixels above the bottom screen edge
		//spaceship.width = spaceshipImage.getWidth();
		//spaceship.height = spaceshipImage.getHeight();
		//spaceshipSprite.setX(spaceship.x);
		//spaceshipSprite.setY(spaceship.y);
		spaceshipSprite.setX(VIRTUAL_WIDTH / 2 - spaceshipImage.getWidth() / 2);
		spaceshipSprite.setY(20);

		// create the raindrops array and spawn the first raindrop
		//raindrops = new Array<MyRect>();
		Gdx.input.setInputProcessor(new GestureDetector(new MyGestureListener(this)));

		planets = new Array<StellarObject>();
		asteroids = new Array<StellarObject> ();
		nebulas = new Array<StellarObject> ();
		projectiles = new Array<StellarObject>();

		for (int i = 0; i < 100; i++) {
			spawnAsteroids(false);
		}
		for (int i = 0; i < 20; i++) {
			spawnPlanets(false);
		}
		for (int i = 0; i < 1; i++) {
			spawnNebulas(false);
		}

		Texture power1 = new Texture("black-hole-bolas1.png");
		Texture power2 = new Texture("rocket-flight1.png");
		Texture power3 = new Texture("forward-field1.png");
		powerups = new Array<Sprite>();
		Sprite powersprite1 = new Sprite(power1);
		Sprite powersprite2 = new Sprite(power2);
		Sprite powersprite3 = new Sprite(power3);
		powersprite1.setBounds(VIRTUAL_WIDTH - 100 - 25, VIRTUAL_HEIGHT - 125, 100, 100);
		powersprite2.setBounds(VIRTUAL_WIDTH - 100 - 25, VIRTUAL_HEIGHT - 250, 100, 100);
		powersprite3.setBounds(VIRTUAL_WIDTH - 100 - 25, VIRTUAL_HEIGHT - 375, 100, 100);
		powersprite1.setOriginCenter();
		powerups.add(powersprite1);
		powerups.add(powersprite2);
		powerups.add(powersprite3);
		fitViewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);

		distanceToGo = 100;
		currentSector = 1;

		loadSavedData();

	}

	private void loadSavedData() {
		prefs = Gdx.app.getPreferences("My Preferences");
		currentSector = prefs.getInteger("currentSector, 0");
		distanceToGo = prefs.getFloat("distanceToGo", 100);
		distanceTraveled = prefs.getFloat("distanceTraveled", 0);
		globalSpeed = prefs.getFloat("globalSpeed", 0);
		maxCurrentSpeed = prefs.getFloat("maxCurrentSpeed", 0);
		globalSpeedBonus = prefs.getFloat("globalSpeedBonus", 0);
	}
	private void spawnAsteroids(boolean atTop) {
		if (MathUtils.random(0, 3) == 0) {
			int asteroidNum = MathUtils.random(1, 10);
			//StellarObject a = new Asteroid("asteroid" + asteroidNum + ".png");
			StellarObject a = asteroidPool.obtain();
			a.setImage("asteroid" + asteroidNum + ".png");
			if (atTop) {
				a.spawnRandomX(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
			} else {
				//a.spawn(VIRTUAL_WIDTH, MathUtils.random(a.getRect().height, VIRTUAL_HEIGHT));
				a.spawnRandomX(VIRTUAL_WIDTH, (int) MathUtils.random(a.getSprite().getHeight(), VIRTUAL_HEIGHT));
			}

			Gdx.app.debug("spawnAsteroids", "spawning asteroid " + asteroidNum + ", speed " + a.getSpeed());
			asteroids.add(a);
		}
	}

	private void spawnPlanets(boolean atTop) {
		if (MathUtils.random(0, 300) == 0) {
			int planetNum = MathUtils.random(1, 7);
			StellarObject a = planetPool.obtain();
			a.setImage("planet" + planetNum + ".png");
			if (atTop) {
				a.spawnRandomX(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
			} else {
				//a.spawn(VIRTUAL_WIDTH, MathUtils.random(a.getRect().height, VIRTUAL_HEIGHT));
				a.spawnRandomX(VIRTUAL_WIDTH, (int) MathUtils.random(a.getSprite().getHeight(), VIRTUAL_HEIGHT));
			}
			Gdx.app.debug("spawnPlanets", "spawning planet " + a.getImageName() +  ", speed " + a.getSpeed());
			planets.add(a);
		}
	}

	private void spawnNebulas(boolean atTop) {
		StellarObject a = nebulaPool.obtain();
		if (MathUtils.random(0,1) == 0) {
			a.setImage("nebula1.png");
		} else {
			a.setImage("nebula2.png");
		}
		if (atTop) {
			a.spawnRandomX(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
		} else {
			a.spawnRandomX(VIRTUAL_WIDTH, (int) MathUtils.random(a.getSprite().getHeight(), VIRTUAL_HEIGHT));
		}
		Gdx.app.debug("spawnNebula", "spawning nebula " + a.getImageName() +  ", speed " + a.getSpeed());
		nebulas.add(a);
	}

	private void spawnProjectile() {
		StellarObject p = projectilePool.obtain();
		p.setImage("fire_green3.png");
		float offset = (p.getSprite().getWidth() - spaceshipSprite.getWidth()) / 2.0f;
		p.spawn((int) spaceshipSprite.getX() - offset, (int) spaceshipSprite.getY() + spaceshipSprite.getHeight() / 2);
		projectiles.add(p);
	}

	@Override
	public void render() {
		// clear the screen with a dark blue color. The
		// arguments to glClearColor are the red, green
		// blue and alpha component in the range [0,1]
		// of the color to be used to clear the screen.

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


		// set viewport
//        Gdx.gl.glViewport((int) viewport.x, (int) viewport.y,
//                (int) viewport.width, (int) viewport.height);
		//camera.viewportHeight = Gdx.graphics.getHeight();
		//camera.viewportWidth = Gdx.graphics.getWidth();

		// tell the camera to update its matrices.
		camera.update();


		// tell the SpriteBatch to render in the
		// coordinate system specified by the camera.
		batch.setProjectionMatrix(camera.combined);

		// begin a new batch and draw the spaceship and
		// all drops
		batch.begin();

		batch.draw(dustImage1, 0, 0);
		batch.draw(dustImage2, 0, VIRTUAL_HEIGHT / 2);
		batch.draw(dustImage3, 0, VIRTUAL_HEIGHT - dustImage3.getHeight());
		for (StellarObject stellarObject : nebulas) {
			if (stellarObject.isVisible()) {
				stellarObject.getSprite().draw(batch);
			}
		}
		for(StellarObject stellarObject : planets) {
			if (stellarObject.isVisible()) {
				stellarObject.getSprite().draw(batch);
			}
		}
		for(StellarObject stellarObject : asteroids) {
			if (stellarObject.isVisible()) {
				stellarObject.getSprite().draw(batch);
			}
		}
		for(StellarObject stellarObject : projectiles) {
			if (stellarObject.isVisible()) {
				stellarObject.getSprite().draw(batch);
			}
		}
		font.draw(batch, "Distance traveled: " + (int)(distanceTraveled * 1), 25, VIRTUAL_HEIGHT - 50);
		font.draw(batch, "Distance to next : " + distanceToGo, 25, VIRTUAL_HEIGHT - 100);
		font.draw(batch, "Current speed: " + (int) globalSpeed / 100, 25, VIRTUAL_HEIGHT - 150);
		font.draw(batch, "Max speed: " + (int) maxSpeed / 100, 25, VIRTUAL_HEIGHT - 200);
		font.draw(batch, "Speed bonus: " + (int) globalSpeedBonus / 100, 25, VIRTUAL_HEIGHT - 250);
		if (maxCurrentSpeed > 0) {
			font.draw(batch, "Max current speed    : " + (int) maxCurrentSpeed / 100, 25, VIRTUAL_HEIGHT - 300);
		}


		font.draw(batch, "Current Sector: " + currentSector, 25, VIRTUAL_HEIGHT - 350);


		powerups.get(0).draw(batch);
		powerups.get(1).draw(batch);
		powerups.get(2).draw(batch);

		//batch.draw(spaceshipImage, spaceship.x, spaceship.y);
		spaceshipSprite.draw(batch);
		if (globalSpeed > 500) {
			//batch.draw(jetImage, spaceship.x, spaceship.y);
			batch.draw(jetImage, spaceshipSprite.getX(), spaceshipSprite.getY());
		}
//        font.draw(batch, "Remaining thrust: " + (int)(distanceToGo * 1000000), 25, VIRTUAL_HEIGHT - 50);
		batch.end();

		float sx = spaceshipSprite.getX();
		float sy = spaceshipSprite.getY();

		// process user input

		if (Gdx.input.justTouched())
		{
			int x = Gdx.input.getX();
			int y = Gdx.input.getY();
			if (isTouched (x, y, spaceshipSprite))
			{
				Gdx.app.debug("render", "spaceship touched!");
				spawnProjectile();
			}
		}
		if(Gdx.input.isTouched()) {
			int x = Gdx.input.getX();
			int y = Gdx.input.getY();
			float r = (float) Gdx.graphics.getWidth() / (float) VIRTUAL_WIDTH; // scale X and Y will be same
			int spaceshipWidth = (int) (spaceshipSprite.getWidth() * r);
			int buffer = 5;
			Gdx.app.debug("render", "touched : " + x + ", " + y + ", ship (" + sx + ", " + sy + "), r:" + r);
			Gdx.app.debug("render", "sprite x: " + spaceshipSprite.getX() + ", sprite y: " + spaceshipSprite.getY() +
					", width: " + spaceshipSprite.getWidth() + ", textureWidth: " + spaceshipSprite.getWidth());

			Vector3 touchPos = new Vector3();
			touchPos.set(x, y, 0);
			camera.unproject(touchPos);
			if (isTouchedY(y, spaceshipSprite)) {
				if (x < (sx * r) + (spaceshipWidth / 2) - buffer) {
					sx -= (int) (200 * Gdx.graphics.getDeltaTime());
				} else if (x > (sx * r) + (spaceshipWidth / 2) + buffer) {
					sx += (int ) (200 * Gdx.graphics.getDeltaTime());
				}
			}

			if (isTouched (x, y, powerups.get(0))) {
				globalSpeedBonus += 100;
				thrustShip(0.1f);
				Gdx.app.debug("render", "power-up 0 pressed");
				powerupSound.play();
			}
			if (isTouched (x, y, powerups.get(1)))
			{
				globalSpeedBonus -= 100;
				Gdx.app.debug("render", "power-up 1 pressed");
				powerupSound.play();
			}
			if (isTouched (x, y, powerups.get(2)))
			{
				thrustShip(5);
			}

		}
		if(Gdx.input.isKeyPressed(Keys.LEFT)) sx -= 200 * Gdx.graphics.getDeltaTime();
		if (globalSpeed > 500)
		{
			spawnAsteroids(true);
		}
		if(Gdx.input.isKeyPressed(Keys.RIGHT)) sx += 200 * Gdx.graphics.getDeltaTime();

		spaceshipSprite.setX(sx);

		if(Gdx.input.isKeyJustPressed(Keys.UP)) {
			//distanceTraveled += Gdx.graphics.getDeltaTime();
		} else if (Gdx.input.isKeyJustPressed(Keys.DOWN)) {
			thrustShip(2);
		}

		// make sure the spaceship stays within the screen bounds
		if(sx < 0) spaceshipSprite.setX(0);
		if(sx > VIRTUAL_WIDTH - spaceshipImage.getWidth()) spaceshipSprite.setX( VIRTUAL_WIDTH - spaceshipImage.getWidth());

		long currNanoTime = TimeUtils.nanoTime();
		nanoTimeSum += (currNanoTime - prevNanoTime);

		//Gdx.app.debug("render", "nanoTimeSum: " + nanoTimeSum + ", currNanoTime " + currNanoTime + ", prevNanoTime " + prevNanoTime);
		if( nanoTimeSum > 1000000) // 1ms
		{
			nanoTimeSum = 0;

		}
		// check if we need to create a new Stellar Object
		// but make sure we're moving at a fairly good speed
		if (globalSpeed > 1000)
		{
			spawnPlanets(true);
		}
		if (globalSpeed > 0) {
			Iterator<StellarObject> iter2 = planets.iterator();
			while (iter2.hasNext()) {
				StellarObject planet = iter2.next();
				planet.setRectPos(planet.getSprite().getX(), planet.getSprite().getY() -
						(int) (planet.getSpeed() * (globalSpeed/10000) * Gdx.graphics.getDeltaTime()));

				if (planet.getSprite().getY() + planet.getSprite().getHeight() < 0) {
					Gdx.app.debug("render", "removing planet " + planet.getImageName());
					iter2.remove();
					planet.getImage().dispose();
					planetPool.free(planet);
				}
			}

			Iterator<StellarObject> iter3 = asteroids.iterator();
			while (iter3.hasNext()) {
				StellarObject asteroid = iter3.next();
				asteroid.setRectPos(asteroid.getSprite().getX(), asteroid.getSprite().getY() -
						(int) (asteroid.getSpeed() *
								(globalSpeed/1000) * Gdx.graphics.getDeltaTime()));
				if (asteroid.getSprite().getY() + asteroid.getSprite().getHeight() < 0) {
					Gdx.app.debug("render", "removing asteroid " + asteroid.getImageName());
					iter3.remove();
					asteroid.getImage().dispose();
					asteroidPool.free(asteroid);
				}
			}

			Iterator<StellarObject> iter4 = nebulas.iterator();
			while (iter4.hasNext()) {
				StellarObject nebula = iter4.next();
				nebula.setRectPos(nebula.getSprite().getX(), nebula.getSprite().getY() -
						(int) (nebula.getSpeed() * (globalSpeed/10000)
								* Gdx.graphics.getDeltaTime()));
				if (nebula.getSprite().getY() + nebula.getSprite().getHeight() < 0) {
					Gdx.app.debug("render", "removing nebula " + nebula.getImageName());
					iter4.remove();
					nebula.getImage().dispose();
					nebulaPool.free(nebula);
					spawnNebulas(true);
				}
			}
			//globalSpeed = (float) Math.sqrt((double) globalSpeed);
			distanceTraveled += globalSpeed/1000 * Gdx.graphics.getDeltaTime();

			globalSpeed = globalSpeed*.95f;
			if (globalSpeedBonus > 0) globalSpeedBonus--;
			if (globalSpeed <= 1.05 + globalSpeedBonus) {
				globalSpeed = globalSpeedBonus;
				maxCurrentSpeed = globalSpeedBonus;
			}
		}

		Iterator<StellarObject> projectile_iter = projectiles.iterator();
		while (projectile_iter.hasNext()) {
			StellarObject projectile = projectile_iter.next();
			projectile.setRectPos(projectile.getSprite().getX(), projectile.getSprite().getY() +
					(int) (projectile.getSpeed() *
							Gdx.graphics.getDeltaTime() *
							(1 + (float) projectile.getSprite().getY() / (float) VIRTUAL_HEIGHT)) );
			if (projectile.getSprite().getY() > VIRTUAL_HEIGHT) {
				Gdx.app.debug("render", "removing projectile " + projectile.getImageName());
				projectile_iter.remove();
				projectile.getImage().dispose();
				projectilePool.free(projectile);
			}
		}

		prevNanoTime = TimeUtils.nanoTime();

		if (distanceTraveled >= distanceToGo)
		{
			newLevel();
		}
	}

	public void newLevel() {
		distanceToGo += 100;
		distanceTraveled = 0;
		currentSector++;
		globalSpeedBonus = globalSpeed = 0;
		saveGameData();
	}

	private void saveGameData() {
		prefs.putInteger("currentSector", currentSector);
		prefs.putFloat("distanceToGo", distanceToGo);
		prefs.putFloat("distanceTraveled", distanceTraveled);
		prefs.putFloat("globalSpeed", globalSpeed);
		prefs.putFloat("distanceToGo", distanceToGo);
		prefs.putFloat("maxCurrentSpeed", maxCurrentSpeed);
		prefs.putFloat("globalSpeedBonus", globalSpeedBonus);

		prefs.flush();
	}
	public void thrustShip(float d) {
		Gdx.app.debug("thrustShip", "d:" + d);
		if (globalSpeed + d*2000 > maxCurrentSpeed) maxCurrentSpeed = (globalSpeed + d*2000);
		globalSpeed += d*2000;
		if (globalSpeed + globalSpeedBonus > maxSpeed) maxSpeed = globalSpeed + globalSpeedBonus;
	}

	/**
	 * Determine if touch inputs are within a rect
	 * note that touch inputs are y-down, so 0,0 is top left corner.
	 *
	 * @param touchX
	 * @param touchY
	 */
	public boolean isTouched(int touchX, int touchY, Sprite sprite)
	{
		int transX = touchX * VIRTUAL_WIDTH / Gdx.graphics.getWidth() ;
		int transY = Gdx.graphics.getHeight() - touchY;
		transY = transY * VIRTUAL_HEIGHT / Gdx.graphics.getHeight();

		Gdx.app.debug("isTouched", "touchX " + touchX + ", touchY " + touchY +
				", transX " + transX + ", transY " + transY);

		if ((transX >= sprite.getX() && transX <= sprite.getX() + sprite.getWidth()) &&
				(transY >= sprite.getY() && transY <= sprite.getY() + sprite.getHeight()))
		{
			Gdx.app.debug("isTouched", "touched sprite ");
			return true;
		}

		return false;
	}

	/**
	 * It's like isTouched, but only checks Y region
	 * @param touchY
	 * @param sprite
	 * @return
	 */
	public boolean isTouchedY(int touchY, Sprite sprite)
	{
		int transY = Gdx.graphics.getHeight() - touchY;
		transY = transY * VIRTUAL_HEIGHT / Gdx.graphics.getHeight();

		Gdx.app.debug("isTouched", " touchY " + touchY + ", transY " + transY);

		if (transY >= sprite.getY() && transY <= sprite.getY() + sprite.getHeight())
		{
			Gdx.app.debug("isTouched", "touched sprite ");
			return true;
		}

		return false;
	}

	@Override
	public void dispose() {
		// dispose of all the native resources
		dropImage.dispose();
		spaceshipImage.dispose();
		dropSound.dispose();
		bgMusic.dispose();
		batch.dispose();
	}

	@Override
	public void resize(int width, int height) {

		Gdx.app.debug("resize", "resize called with w,h:" + width + " " + height);
		fitViewport.update(width, height);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}