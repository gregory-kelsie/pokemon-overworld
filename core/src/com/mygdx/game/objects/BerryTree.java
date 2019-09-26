package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.TiledApp;
import com.mygdx.game.screens.NPCInteraction;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.sprites.components.PlayerStats;
import com.mygdx.game.sprites.components.PlayerSwitches;
import com.mygdx.game.sprites.components.TextBox;

import java.util.Date;

/**
 * Created by gregorykelsie on 2018-11-29.
 */

public class BerryTree {
    protected int treeId;
    protected Sprite sprite;
    protected World world;
    protected Body b2body;

    protected Animation<TextureRegion> berryAnimation;
    protected float stateTimer;

    protected int amount; //The amount of berries on the tree.
    protected int exp; //Berry Gathering Exp per berry.
    protected int requiredLevel; //The required Berry Gathering level to gather berries.
    protected int respawnTime;
    protected long harvestTime;

    public BerryTree(int treeId, TextureAtlas.AtlasRegion r, World world, float x, float y, long harvestTime) {
        this.treeId = treeId;
        this.world = world;
        sprite = new Sprite(r);
        initAnimation();
        sprite.setPosition(x, y);
        sprite.setBounds(x, y, 22 / TiledApp.PPM, 34 / TiledApp.PPM);
        sprite.setRegion(berryAnimation.getKeyFrame(0, true));
        this.harvestTime = harvestTime;
        defineBerryTree();
        respawnTime = 1;
    }

    private void initAnimation() {
        stateTimer = 0;
        Array<TextureRegion> frames = new Array<TextureRegion>();
        frames.add(new TextureRegion(sprite.getTexture(), getSpriteX(0), getSpriteY(0), 22, 34));
        frames.add(new TextureRegion(sprite.getTexture(), getSpriteX(23), getSpriteY(0), 22, 34));
        berryAnimation = new Animation<TextureRegion>(0.5f, frames);
    }

    public int getId() {
        return treeId;
    }

    public int getBerryAmount(int playerLevel) {
       return 1;
    }

    public long getHarvestTime() { return harvestTime; }

    public int getExp(int amount) {
        return 0;
    }

    public int getRequiredLevel() {
        return 1;
    }

    public boolean isSpawned() {
        if (harvestTime == 0) {
            return true;
        }
        return false;
    }

    public void interact(PlayerSwitches switches, NPCInteraction npcInteraction) { //TODO: ADD INVENTORY AND STATS
        if (isSpawned()) {
            //harvest
            Date date = new Date();
            long newHarvestTime = date.getTime();
            this.harvestTime = newHarvestTime;
            switches.setBerryTimer(treeId, newHarvestTime);
            Filter filter = new Filter();
            filter.categoryBits = PlayScreen.NOTHING_BIT;
            for (Fixture f: b2body.getFixtureList()) {
                f.setFilterData(filter);
            }
            TextBox t = new TextBox("You harvested some berries!");
            npcInteraction.openTextBox(t);
        }
    }

    private int getSpriteX(int spriteXOffset) {
        return sprite.getRegionX() + spriteXOffset;
    }
    private int getSpriteY(int spriteYOffset) {
        return sprite.getRegionY() + spriteYOffset;
    }

    public int getTileYBotSide() {
        return (int) ((b2body.getPosition().y - (12 / TiledApp.PPM)) * TiledApp.PPM / 16);
    }

    private void defineBerryTree() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(sprite.getX(), sprite.getY());
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        Vector2[] vex = {new Vector2(-6 / TiledApp.PPM, -12 / TiledApp.PPM),
                new Vector2(-6 / TiledApp.PPM, 2 / TiledApp.PPM),
                new Vector2(5 / TiledApp.PPM, -12 / TiledApp.PPM),
                new Vector2(5  / TiledApp.PPM, 2 / TiledApp.PPM)};
        shape.set(vex);
        if (isSpawned()) {
            fdef.filter.categoryBits = PlayScreen.BERRY_BIT;
        } else {
            fdef.filter.categoryBits = PlayScreen.NOTHING_BIT;
        }
        fdef.shape = shape;
        b2body.setFixedRotation(true);
        b2body.createFixture(fdef);
    }

    public void update(float dt) {
        //Play the right animation depending on whether or not it ws harvested.
        sprite.setPosition(b2body.getPosition().x - sprite.getWidth() / 2, b2body.getPosition().y - sprite.getHeight() / 2);
        if (harvestTime == 0) {
            sprite.setRegion(berryAnimation.getKeyFrame(stateTimer, true));
        } else {
            Date date = new Date();
            long elapsedHarvestTime = date.getTime() - harvestTime;
            if ((elapsedHarvestTime / 1000) / 60 >= respawnTime) {
                harvestTime = 0; //Respawn the berry tree.
                Filter filter = new Filter();
                filter.categoryBits = PlayScreen.BERRY_BIT;
                for (Fixture f: b2body.getFixtureList()) {
                    f.setFilterData(filter);
                }
            }
        }
        stateTimer += dt;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public Body getB2body() {
        return b2body;
    }

}
