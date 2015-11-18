package com.boltarstudios.swipeship;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SwipeShipMain extends Game {

    //TODO: move these to GameData class with getters and setters
    public float distanceTraveled; // needs to be float to add fractional distance at slow speeds
    public long distanceToGo;
    public float globalSpeed;
    public long maxSpeed;
    public long maxCurrentSpeed;
    public long globalSpeedBonus;
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
    public static long timeStampOfLastPause;
    public Screen menuScreen;
    public Screen gameScreen;
    public Screen popupScreen;

    public void create() {
        batch = new SpriteBatch();
// Use LibGDX's default Arial font.
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.setScale(2);
        loadSavedData();
        gameResumeMaintenance();

        //this.setScreen(gameScreen);
        this.setScreen(popupScreen);
        menuScreen = new MainMenu(this);
        gameScreen = new GameScreen(this);

    }


    public void thrustShip(float d) {
        Gdx.app.debug("thrustShip", "d:" + d);
        if (globalSpeed + d*2000 > maxCurrentSpeed) maxCurrentSpeed = (long)(globalSpeed + d*2000);
        globalSpeed += d*2000;
        if (globalSpeed + globalSpeedBonus > maxSpeed) maxSpeed = (long) (globalSpeed + globalSpeedBonus);
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
        gameScreen.dispose();
        Gdx.app.debug("SwipeShipMain", "SwipeShipMain dispose");

    }

    public void loadSavedData() {
        prefs = Gdx.app.getPreferences("My Preferences");
        currentSector = prefs.getInteger("currentSector", 0);
        distanceToGo = prefs.getLong("distanceToGo", 100);
        distanceTraveled = prefs.getFloat("distanceTraveled", 0);
        globalSpeed = prefs.getFloat("globalSpeed", 0);
        maxCurrentSpeed = prefs.getLong("maxCurrentSpeed", 0);
        globalSpeedBonus = prefs.getLong("globalSpeedBonus", 0);
        maxSpeed = prefs.getLong("maxSpeed", 0);
        isNewGame = prefs.getBoolean("isNewGame", true);
        timeStampOfLastPause = prefs.getLong("timeStamp");
        Gdx.app.debug("SwipeShipMain", "SwipeShipMain loadSavedData");

    }

    public void saveGameData() {
        prefs.putInteger("currentSector", currentSector);
        prefs.putLong("distanceToGo", distanceToGo);
        prefs.putFloat("distanceTraveled", distanceTraveled);
        prefs.putFloat("globalSpeed", globalSpeed);
        prefs.putLong("maxCurrentSpeed", maxCurrentSpeed);
        prefs.putLong("globalSpeedBonus", globalSpeedBonus);
        prefs.putLong("maxSpeed", maxSpeed);
        prefs.putBoolean("isNewGame", false);
        prefs.putLong("timeStamp", System.currentTimeMillis());

        prefs.flush();
    }

    public void newLevel() {
        distanceToGo += 100;
        distanceTraveled = 0;
        currentSector++;
        globalSpeedBonus = 0;
        globalSpeed = 0;
        saveGameData();
    }

    public void restart() {
        distanceToGo = 100;
        distanceTraveled = 0;
        currentSector = 1;
        globalSpeedBonus = 0;
        globalSpeed = 0;
        maxSpeed = maxCurrentSpeed = 0;
        saveGameData();
    }

    /**
     *
     * @param timeElapsed elapsed time in seconds
     * @param speedBonus rawSpeedBonus
     * @return
     */
    public long calculateDistanceTraveled(long timeElapsed, long speedBonus)
    {
        long distance, extraDistance;
        // assume y = k - x, so figure out the area of the triangle
        speedBonus = speedBonus / 100;
        distance = (speedBonus * speedBonus / 2) * timeElapsed;
        Gdx.app.debug("", "SpeedBonus: " + speedBonus + ", timeElapsed: " + timeElapsed + ", distance: " + distance);
        // first case is when bonus < time elapsed
        if (speedBonus <= timeElapsed) {
            // do nothing - just use the base case
        } else {
            // we had too much bonus, so need to calculate distance traveled only during time elapsed
            // which is basically the first case minus the distance traveled outside of time elapsed

            //
            // d ^
            //   |\
            //   | \
            //   |  \
            //   |  |\
            //   |  | \
            //   +------------
            //   0  t  b
            // distance traveled = big triangle - small triangle (b^2/2) - (b-t)^2/2

            long leg = speedBonus - timeElapsed;
            extraDistance = (leg * leg / 2) * timeElapsed;
            distance = distance - extraDistance;
        }
        return distance;

    }

    public void gameResumeMaintenance(){
        long elapsedTime = (System.currentTimeMillis() - timeStampOfLastPause) / 1000;

        long distanceTraveledSinceLast = calculateDistanceTraveled(elapsedTime, globalSpeedBonus);

        String whileYouWereGone = "Your ship has traveled for " + distanceTraveledSinceLast + " miles in " + elapsedTime
                + " seconds while you were away.";
        popupScreen = new PopupScreen(this, whileYouWereGone);

        // there are roughly 60 cycles per second
        globalSpeedBonus = globalSpeedBonus - elapsedTime;


        if (globalSpeedBonus < 0) globalSpeedBonus = 0;
        distanceTraveled = distanceTraveled + distanceTraveledSinceLast;
        if (distanceTraveled > distanceToGo) newLevel();

        Gdx.app.debug("SwipeShipMain", elapsedTime + " seconds elapsed since last playing");

    }

}