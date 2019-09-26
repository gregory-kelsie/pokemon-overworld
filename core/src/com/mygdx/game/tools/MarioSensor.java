package com.mygdx.game.tools;

import com.mygdx.game.sprites.Mario;

/**
 * Created by gregorykelsie on 2018-11-16.
 */

public class MarioSensor {

    private Mario m;
    private MarioEdge sensorEdge;

    public MarioSensor(Mario m, MarioEdge sensorEdge) {
        this.m = m;
        this.sensorEdge = sensorEdge;
    }

    public Mario getMario() {
        return m;
    }

    public MarioEdge getSensorEdge() {
        return sensorEdge;
    }
}
