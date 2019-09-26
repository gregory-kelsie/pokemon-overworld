package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by gregorykelsie on 2018-11-29.
 */

public class OranTree extends BerryTree {
    public OranTree(int treeId, TextureAtlas.AtlasRegion r, World world, float x, float y, long harvestTime) {
        super(treeId, r, world, x, y, harvestTime);
        exp = 5;
        respawnTime = 1;
    }

    public int getExp(int amount) {
        return amount * exp;
    }

    public int getBerryAmount(int playerLevel) {
        double rand = Math.random();
        if (playerLevel >= 1 && playerLevel < 5) {
            return 1;
        } else if (playerLevel >= 5 && playerLevel < 15) {
            if (rand <= .75) {
                return 1;
            }
            return 2;
        } else if (playerLevel >= 15 && playerLevel < 20) {
            if (rand <= .5) {
                return 1;
            }
            return 2;
        } else if (playerLevel >= 20 && playerLevel < 25) {
            if (rand <= .75) {
                return 2;
            }
            return 1;
        } else if (playerLevel >= 25 && playerLevel < 40) {
            return 2;
        } else if (playerLevel >= 40 && playerLevel < 50) {
            if (rand <= .75) {
                return 2;
            }
            return 3;
        } else if (playerLevel >= 50 && playerLevel < 60) {
            if (rand <= .75) {
                return 3;
            }
            return 2;
        } else if (playerLevel >= 60 && playerLevel < 75) {
            return 3;
        } else if (playerLevel >= 75 && playerLevel < 85) {
            if (rand <= .75) {
                return 3;
            }
            return 4;
        } else if (playerLevel >= 85 && playerLevel >= 95) {
            return 4;
        } else {
            if (rand <= 0.1) {
                return 6;
            } else if (rand <= 0.6) {
                return 5;
            }
            return 4;
        }
    }
}
