package com.boltarstudios.swipeship;
import com.badlogic.gdx.math.MathUtils;

/**
 * Created by tony on 11/30/14.
 */
public class Asteroid extends StellarObject {

    public Asteroid() {
        super();
        setSpeed();
    }

    public void setSpeed()
    {
        //super.setSpeed(this.getImage().getWidth() * this.getImage().getHeight() / 10 + // speed is a function of area
        //        MathUtils.random(-20, 20)); // give it some flavah
        super.setSpeed(500 + MathUtils.random(-50, 50));
    }
}
