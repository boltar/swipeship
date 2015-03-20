package com.boltarstudios.swipeship;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SwipeShipMain extends Game {

    //TODO: move these to GameData class with getters and setteres
    public float distanceTraveled;
    public float distanceToGo;
    public float globalSpeed;
    public float maxSpeed;
    public float maxCurrentSpeed;
    public float globalSpeedBonus;
    public int currentSector;
    public Preferences prefs;
    SpriteBatch batch;
    BitmapFont font;
    private boolean isPaused = false;

    public static final int VIRTUAL_WIDTH = 720;
    public static final int VIRTUAL_HEIGHT = 1280;
    public static final float ASPECT_RATIO =
            (float)VIRTUAL_HEIGHT/(float)VIRTUAL_WIDTH;
    public static boolean isNewGame = false;
    public Screen menuScreen;
    public Screen gameScreen;

    public void create() {
        batch = new SpriteBatch();
// Use LibGDX's default Arial font.
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.setScale(2);
        loadSavedData();
        menuScreen = new MainMenu(this);
        gameScreen = new GameScreen(this);

        this.setScreen(gameScreen);
    }


    public void thrustShip(float d) {
        Gdx.app.debug("thrustShip", "d:" + d);
        if (globalSpeed + d*2000 > maxCurrentSpeed) maxCurrentSpeed = (globalSpeed + d*2000);
        globalSpeed += d*2000;
        if (globalSpeed + globalSpeedBonus > maxSpeed) maxSpeed = globalSpeed + globalSpeedBonus;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean isPaused) {
        this.isPaused = isPaused;
    }

    public void render() {
        super.render(); // important!
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
    }

    public void loadSavedData() {
        prefs = Gdx.app.getPreferences("My Preferences");
        currentSector = prefs.getInteger("currentSector", 0);
        distanceToGo = prefs.getFloat("distanceToGo", 100);
        distanceTraveled = prefs.getFloat("distanceTraveled", 0);
        globalSpeed = prefs.getFloat("globalSpeed", 0);
        maxCurrentSpeed = prefs.getFloat("maxCurrentSpeed", 0);
        globalSpeedBonus = prefs.getFloat("globalSpeedBonus", 0);
        maxSpeed = prefs.getFloat("maxSpeed", 0);
        isNewGame = prefs.getBoolean("isNewGame", true);
    }

    public void saveGameData() {
        prefs.putInteger("currentSector", currentSector);
        prefs.putFloat("distanceToGo", distanceToGo);
        prefs.putFloat("distanceTraveled", distanceTraveled);
        prefs.putFloat("globalSpeed", globalSpeed);
        prefs.putFloat("distanceToGo", distanceToGo);
        prefs.putFloat("maxCurrentSpeed", maxCurrentSpeed);
        prefs.putFloat("globalSpeedBonus", globalSpeedBonus);
        prefs.putFloat("maxSpeed", maxSpeed);
        prefs.putBoolean("isNewGame", false);

        prefs.flush();
    }

    public void newLevel() {
        distanceToGo += 100;
        distanceTraveled = 0;
        currentSector++;
        globalSpeedBonus = globalSpeed = 0;
        saveGameData();
    }


}