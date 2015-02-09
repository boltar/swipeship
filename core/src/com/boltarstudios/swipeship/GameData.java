package com.boltarstudios.swipeship;

/**
 * Created by tony on 2/8/15.
 */
public class GameData {

    private float distanceTraveled;
    private float distanceToGo;
    private float globalSpeed;
    private float maxSpeed;
    private float maxCurrentSpeed;
    private float globalSpeedBonus;
    private int currentSector;

    public float getDistanceTraveled() {
        return distanceTraveled;
    }

    public void setDistanceTraveled(float distanceTraveled) {
        this.distanceTraveled = distanceTraveled;
    }

    public float getDistanceToGo() {
        return distanceToGo;
    }

    public void setDistanceToGo(float distanceToGo) {
        this.distanceToGo = distanceToGo;
    }

    public float getGlobalSpeed() {
        return globalSpeed;
    }

    public void setGlobalSpeed(float globalSpeed) {
        this.globalSpeed = globalSpeed;
    }

    public float getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public float getMaxCurrentSpeed() {
        return maxCurrentSpeed;
    }

    public void setMaxCurrentSpeed(float maxCurrentSpeed) {
        this.maxCurrentSpeed = maxCurrentSpeed;
    }

    public float getGlobalSpeedBonus() {
        return globalSpeedBonus;
    }

    public void setGlobalSpeedBonus(float globalSpeedBonus) {
        this.globalSpeedBonus = globalSpeedBonus;
    }

    public int getCurrentSector() {
        return currentSector;
    }

    public void setCurrentSector(int currentSector) {
        this.currentSector = currentSector;
    }


}
