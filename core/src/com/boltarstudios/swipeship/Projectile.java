package com.boltarstudios.swipeship;

import com.badlogic.gdx.math.MathUtils;

/**
 * Created by tony on 1/26/15.
 */
public class Projectile extends StellarObject {
    public Projectile() {
        super();
        setSpeed();
    }

    public void setSpeed()
    {
        super.setSpeed(1000);
    }

}
