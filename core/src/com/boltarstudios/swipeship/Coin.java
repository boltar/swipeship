package com.boltarstudios.swipeship;

import com.badlogic.gdx.math.MathUtils;

/**
 * Created by tony on 11/30/14.
 */
public class Coin extends StellarObject {

    public Coin() {
        super();
        setSpeed();
    }


    public void setSpeed()
    {
        //super.setSpeed(this.getImage().getWidth() * this.getImage().getHeight() / 100 + // speed is a function of area
        //        MathUtils.random(-50, 50)); // give it some flavah
        super.setSpeed(500 + MathUtils.random(-50, 50));
    }

}
