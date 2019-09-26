package com.mygdx.game.sprites;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.TiledApp;
import com.mygdx.game.screens.NPCInteraction;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.sprites.components.ai.AI;
import com.mygdx.game.sprites.components.HumanStates;
import com.mygdx.game.sprites.components.HumanTextures;
import com.mygdx.game.sprites.components.ai.LookAroundAI;
import com.mygdx.game.sprites.components.TextBox;

/**
 * Created by gregorykelsie on 2018-11-14.
 */

public class TalkingNPC extends NonPlayableCharacter {


    private TextBox textBoxes;
    private AI ai;

    public TalkingNPC(TextureAtlas textureAtlas, String regionName, World world, float x, float y, TextBox textBoxes) {
        super(textureAtlas.findRegion(regionName), world, x, y);
        this.textBoxes = textBoxes;

        textures = new HumanTextures(textureAtlas, regionName);
        states = new HumanStates();
        setBounds(0, 0, 28 / TiledApp.PPM, 28 / TiledApp.PPM);
        setRegion(textures.getStandingDown());
        ai = new LookAroundAI(b2body, states);
    }

    public HumanStates getStates() {
        return states;
    }

    public HumanTextures getTextures() {
        return textures;
    }

    public void update(float dt) {
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(dt));
        b2body.setLinearVelocity(new Vector2(0, 0));
        ai.update(dt);
    }

    public void initPlayerCollision() {
        ai.handleCollision();
    }

    public void endContact() {
        ai.resume();
    }

    public TextureRegion getFrame(float dt) {
        states.setCurrentState(calculateState());
        return states.getFrame(dt, textures);
    }

    public TextBox getTextBoxes() {
        return textBoxes;
    }

    public Body getB2body() {
        return b2body;
    }

    @Override
    protected void defineNPC() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.KinematicBody;
        this.b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        Vector2[] vex = {new Vector2(-8 / TiledApp.PPM, -12 / TiledApp.PPM),
                new Vector2(-8 / TiledApp.PPM, 2 / TiledApp.PPM),
                new Vector2(7 / TiledApp.PPM, -12 / TiledApp.PPM),
                new Vector2(7 / TiledApp.PPM, 2 / TiledApp.PPM)};
        shape.set(vex);
        fdef.filter.categoryBits = PlayScreen.NPC_BIT;
        fdef.filter.maskBits = PlayScreen.DEFAULT_BIT | PlayScreen.PLAYER_BIT |
                PlayScreen.NPC_BIT | PlayScreen.WALL_BIT;
        fdef.shape = shape;
        b2body.setFixedRotation(true);
        b2body.createFixture(fdef).setUserData(this);
    }

    public void interact(Direction interactDirection, NPCInteraction npcInteraction) {
        if (interactDirection == Direction.RIGHT) {
            states.setDirection(Direction.LEFT);
        } else if (interactDirection == Direction.LEFT) {
            states.setDirection(Direction.RIGHT);
        } else if (interactDirection == Direction.UP) {
            states.setDirection(Direction.DOWN);
        } else if (interactDirection == Direction.DOWN) {
            states.setDirection(Direction.UP);
        }
        TextBox t = new TextBox("Hello World!\nHello World!");
        TextBox t2 = new TextBox("My name is John Jacob.");
        TextBox t3 = new TextBox("I am the first NPC in this game! Bazinga!!!!");
        t.setNextTextBox(t2);
        t2.setNextTextBox(t3);

        npcInteraction.openTextBox(t);
        ai.pause(true);
    }

    public State calculateState() {
        if (b2body.getLinearVelocity().y > 0) {
            return State.WALKING_UP;
        } else if (b2body.getLinearVelocity().y < 0) {
            return State.WALKING_DOWN;
        } else if (b2body.getLinearVelocity().x > 0) {
            return State.WALKING_RIGHT;
        } else if (b2body.getLinearVelocity().x < 0) {
            return State.WALKING_LEFT;
        } else {
            return State.STANDING;
        }
    }
}
