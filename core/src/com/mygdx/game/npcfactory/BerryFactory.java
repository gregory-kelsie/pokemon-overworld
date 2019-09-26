package com.mygdx.game.npcfactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.objects.BerryTree;
import com.mygdx.game.objects.OranTree;

/**
 * Created by gregorykelsie on 2018-11-29.
 */

public class BerryFactory {
    public enum BerryType { ORAN(0), SITRUS(1), PERSIM(2), LEPPA(3), ASPEAR(4), RAWST(5), PECHA(6),
        CHESTO(7), CHERI(8), LUM(9);

        private final int value;
        private BerryType(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }
    };

    private TextureAtlas r;
    private World world;

    public BerryFactory(TextureAtlas r, World world) {
       this.r = r;
       this.world = world;
    }

    public BerryTree getBerryTree(int treeId, int berryType, float x, float y, long harvestTime) {
        if (berryType == BerryType.ORAN.getValue()) {
            Gdx.app.log("berryfactory", "oran");
            return new OranTree(treeId, r.findRegion("oran"), world, x, y, harvestTime);
        }
        return new OranTree(treeId, r.findRegion("oran"), world, x, y, harvestTime);
    }
}
