package com.mygdx.game.objects;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.sprites.Direction;
import com.mygdx.game.tools.MapLoader;

/**
 * Created by gregorykelsie on 2018-11-11.
 */

public class WarpObject {
    private float x;
    private float y;
    private Direction direction;
    private String warpLocation;
    private MapLoader mapLoader;

    public WarpObject(String warpLocation, float x, float y, Direction direction, MapLoader mapLoader) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.warpLocation = warpLocation;
        this.mapLoader = mapLoader;
    }

    public String getWarpLocation() {
        return warpLocation;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Direction getDirection() {
        return direction;
    }

    public MapLoader getMapLoader() {
        return mapLoader;
    }
    public void loadMap() {
        Gdx.app.log("test2", "" + x);
        mapLoader.loadMap(warpLocation, x, y, direction);
    }

}
