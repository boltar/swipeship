package com.boltarstudios.swipeship;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by tony on 11/9/14.
 */
public class MyGestureListener implements GestureDetector.GestureListener {
    private Sound blastSound;
    private SwipeShipMain game;

    public MyGestureListener(SwipeShipMain game)
    {
        super();
        blastSound = Gdx.audio.newSound((Gdx.files.internal("space_blaster.mp3")));
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        this.game = game;

    }
    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {

        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        Gdx.app.debug("MyTag", "tap: " + x + " " + y + " " + count + " " + button);
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {

        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        Gdx.app.debug("MyTag", "fling" + velocityX + " " + velocityY + " " + button);
        game.thrustShip(velocityY * 1 / 2000);
        blastSound.play();
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {

        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {

        return false;
    }

    @Override
    public boolean zoom (float originalDistance, float currentDistance){

        return false;
    }

    @Override
    public boolean pinch (Vector2 initialFirstPointer, Vector2 initialSecondPointer, Vector2 firstPointer, Vector2 secondPointer){

        return false;
    }
}