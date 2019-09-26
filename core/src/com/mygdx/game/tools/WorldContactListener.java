package com.mygdx.game.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.game.objects.WarpObject;
import com.mygdx.game.sprites.Direction;
import com.mygdx.game.sprites.NonPlayableCharacter;
import com.mygdx.game.sprites.State;

/**
 * Created by gregorykelsie on 2018-07-27.
 */

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if (fixtureA.getUserData() == "playerBody" || fixtureB.getUserData() == "playerBody") {
            Gdx.app.log("collision", "player");
            Fixture player = fixtureA.getUserData() == "playerBody" ? fixtureA : fixtureB;
            Fixture object = player == fixtureA ? fixtureB : fixtureA;

            if (object.getUserData() instanceof WarpObject) {
                Gdx.app.log("collision", "warp");
                WarpObject wo = ((WarpObject) object.getUserData());

                wo.getMapLoader().setW(wo.getWarpLocation(), wo.getX(), wo.getY(), wo.getDirection());
            } else if (object.getUserData() instanceof NonPlayableCharacter) {
                ((NonPlayableCharacter)object.getUserData()).initPlayerCollision();
            }
        }
        if (fixtureA.getUserData() instanceof MarioSensor || fixtureB.getUserData() instanceof MarioSensor) {
            Fixture player = fixtureA.getUserData() instanceof MarioSensor ? fixtureA : fixtureB;
            Fixture object = player == fixtureA ? fixtureB : fixtureA;
            if (((MarioSensor)player.getUserData()).getSensorEdge() == MarioEdge.FEET &&
                    object.getUserData().equals("ledgeDown")) {
                if (!((MarioSensor)player.getUserData()).getMario().isJumping() &&
                        (((MarioSensor)player.getUserData()).getMario().getB2body().getLinearVelocity().y < 0)
                        || (((MarioSensor)player.getUserData()).getMario().getDirection() == Direction.DOWN)) {
                    Gdx.app.log("ledge", "hop it!!");
                    ((MarioSensor)player.getUserData()).getMario().setCurrentState(State.JUMPING_DOWN);
                }

            } else if (((MarioSensor)player.getUserData()).getSensorEdge() == MarioEdge.RIGHT &&
                    object.getUserData().equals("ledgeRight")) {
                Gdx.app.log("sensorRight", "right");
                if (!((MarioSensor)player.getUserData()).getMario().isJumping() &&
                        (((MarioSensor)player.getUserData()).getMario().getB2body().getLinearVelocity().x > 0)
                        || (((MarioSensor)player.getUserData()).getMario().getDirection() == Direction.RIGHT)) {
                    Gdx.app.log("ledgeRight", "hop it!!");
                    ((MarioSensor)player.getUserData()).getMario().setCurrentState(State.JUMPING_RIGHT);
                }
            } else if (((MarioSensor)player.getUserData()).getSensorEdge() == MarioEdge.LEFT &&
                    object.getUserData().equals("ledgeLeft")) {
                Gdx.app.log("sensorLeft", "left");
                if (!((MarioSensor)player.getUserData()).getMario().isJumping() &&
                        ((((MarioSensor)player.getUserData()).getMario().getB2body().getLinearVelocity().x < 0)
                        || (((MarioSensor)player.getUserData()).getMario().getDirection() == Direction.LEFT))) {
                    Gdx.app.log("ledgeRight", "hop it!!");
                    ((MarioSensor)player.getUserData()).getMario().setCurrentState(State.JUMPING_LEFT);
                }
            }
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if (fixtureA.getUserData() == "playerBody" || fixtureB.getUserData() == "playerBody") {
            Gdx.app.log("collision", "player");
            Fixture player = fixtureA.getUserData() == "playerBody" ? fixtureA : fixtureB;
            Fixture object = player == fixtureA ? fixtureB : fixtureA;

            if (object.getUserData() instanceof NonPlayableCharacter) {
                ((NonPlayableCharacter)object.getUserData()).endContact();
            }
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
