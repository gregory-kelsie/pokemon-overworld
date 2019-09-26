package com.mygdx.game.sprites.components;

/**
 * Created by gregorykelsie on 2018-11-29.
 */

public class PlayerStats {
    private int berryLevel;
    private int berryExp;
    private int fishingLevel;
    private int fishingExp;
    private int miningLevel;
    private int miningExp;
    public PlayerStats() {
        berryLevel = 1;
        fishingLevel = 1;
        miningLevel = 1;
        berryExp = 0;
        fishingExp = 0;
        miningExp = 0;
    }

    public int getBerryLevel() {
        return berryLevel;
    }

    public int getFishingLevel() {
        return fishingLevel;
    }

    public int getMiningLevel() {
        return miningLevel;
    }

    public void addBerryExp(int exp) {
        berryExp += exp;
    }

    public void addFishingExp(int exp) {
        fishingExp += exp;
    }

    public void addMiningExp(int exp) {
        miningExp += exp;
    }

}
