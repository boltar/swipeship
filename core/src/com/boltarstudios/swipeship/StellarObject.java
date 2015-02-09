package com.boltarstudios.swipeship;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Pool;

import java.awt.*;

/**
 * Created by tony on 11/13/14.
 */
public class StellarObject implements Pool.Poolable {


    private boolean isVisible;
    private int speed;
    private Texture image;
        private Sprite sprite;
        private String imageName;

    public StellarObject() {
        isVisible = false;
    }

    public void setImage(String imageName) {
        image = new Texture(imageName);
        sprite = new Sprite(image);
        this.imageName = imageName;
    }

    public Texture getImage() {
        return image;
    }

    public Sprite getSprite() { return sprite; }

    public boolean isVisible() {
        return isVisible;
    }

    public int getSpeed() {
        return speed;
    }


    public String getImageName() {
        return imageName;
    }

    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setRectPos(float x, float y) {
        sprite.setX(x);
        sprite.setY(y);
    }

    public void spawnRandomX(int widthRange, int heightLocation)
    {
        this.setVisible(true);
        //int w = this.getRect().width;
        int w = (int) this.getSprite().getWidth();
        this.setRectPos(MathUtils.random(-1 * w / 2, widthRange - w / 2), heightLocation);
    }

    public void spawn(float x, float y)
    {
        this.setVisible(true);
        this.setRectPos(x, y);
    }
    /**
     * Callback method when the object is freed. It is automatically called by Pool.free()
     * Must reset every meaningful field of this bullet.
     */
    @Override
    public void reset() {
    }
}
