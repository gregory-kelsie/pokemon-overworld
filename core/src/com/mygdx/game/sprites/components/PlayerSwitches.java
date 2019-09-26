package com.mygdx.game.sprites.components;

import java.util.HashMap;

/**
 * Created by gregorykelsie on 2018-11-29.
 */

public class PlayerSwitches {
    private HashMap<Integer, Long> berries;

    public PlayerSwitches() {
        berries = new HashMap<Integer, Long>();
        berries.put(0, 0l);
        berries.put(1, 0l);
    }

    public long getBerryTimer(int berryId) {
        return berries.get(berryId);
    }

    public void setBerryTimer(int treeId, long harvestTime) {
        berries.put(treeId, harvestTime);
    }
}
