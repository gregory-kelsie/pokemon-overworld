package com.mygdx.game.tools;

import com.mygdx.game.sprites.Direction;

/**
 * Created by gregorykelsie on 2018-11-11.
 */

public interface MapLoader {
    void loadMap(String mapName, float x, float y, Direction direction);
    void setW(String mapName, float x, float y, Direction direction);
}
