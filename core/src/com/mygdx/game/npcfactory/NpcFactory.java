package com.mygdx.game.npcfactory;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.TiledApp;
import com.mygdx.game.sprites.NonPlayableCharacter;
import com.mygdx.game.sprites.TalkingNPC;
import com.mygdx.game.sprites.components.TextBox;

/**
 * Created by gregorykelsie on 2018-11-15.
 */

public class NpcFactory {
    public NonPlayableCharacter createNPC(int npcID, String spriteName, float xPos, float yPos, TextureAtlas atlas, World world) {
        if (npcID == 0) {
            TextBox t = new TextBox("Hello World!\nHello World!");
            TextBox t2 = new TextBox("My name is John Jacob.");
            TextBox t3 = new TextBox("I am the first NPC in this game! Bazinga!!!!");
            t.setNextTextBox(t2);
            t2.setNextTextBox(t3);
            return new TalkingNPC(atlas, spriteName, world, xPos, yPos, t);
        }
        return null;
    }
}
