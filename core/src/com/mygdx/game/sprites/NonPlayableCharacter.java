package com.mygdx.game.sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.TiledApp;
import com.mygdx.game.screens.NPCInteraction;
import com.mygdx.game.sprites.components.HumanStates;
import com.mygdx.game.sprites.components.HumanTextures;

/**
 * Created by gregorykelsie on 2018-11-14.
 */

public abstract class NonPlayableCharacter extends Sprite {

    protected World world;
    protected Body b2body;

    protected HumanTextures textures;
    protected HumanStates states;

    public NonPlayableCharacter(AtlasRegion r, World world, float x, float y) {
        super(r);
        this.world = world;
        setPosition(x, y);
        defineNPC();
    }

    public abstract void endContact();
    public abstract State calculateState();

    public HumanStates getStates() { return states; }

    public HumanTextures getTextures() { return textures; }

    public abstract void initPlayerCollision();

    protected abstract void defineNPC();

    public abstract void interact(Direction interactDirection, NPCInteraction npcInteraction);

    public void update(float dt) { }

    public int getTileX() {
        return (int) Math.floor(b2body.getPosition().x * TiledApp.PPM / 16);
    }

    public int getTileY() {
        return (int) Math.floor(b2body.getPosition().y * TiledApp.PPM / 16);
    }

    public int getTileXX() {
        return (int) (b2body.getPosition().x * TiledApp.PPM / 16);
    }

    public int getTileXLeftSide() {
        return (int) ((b2body.getPosition().x - (8 / TiledApp.PPM)) * TiledApp.PPM / 16);
    }

    public int getTileXRightSide() {
        return (int) ((b2body.getPosition().x + (7 / TiledApp.PPM)) * TiledApp.PPM / 16);
    }

    public int getTileYTopSide() {
        return (int) ((b2body.getPosition().y + (2 / TiledApp.PPM)) * TiledApp.PPM / 16);
    }

    public int getTileYBotSide() {
        return (int) ((b2body.getPosition().y - (12 / TiledApp.PPM)) * TiledApp.PPM / 16);
    }

    public int getTileYY() {
        return (int) (b2body.getPosition().y * TiledApp.PPM / 16);
    }

    public Body getB2body() { return b2body; }

}
