package com.mygdx.game.sprites.components.ai;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.TiledApp;
import com.mygdx.game.sprites.components.HumanStates;

/**
 * Created by gregorykelsie on 2018-11-16.
 */

public class RunBackForthAI extends AI {
    public RunBackForthAI(Body b2body, HumanStates states) {
        super(b2body, states);
    }
    public void update(float dt) {
        if (!pauseAI) {
            if (aiState == 0) {
                //TODO:
                b2body.setLinearVelocity(new Vector2(2f, 0));
                if (b2body.getPosition().x >= 28 * 16 / TiledApp.PPM) {
                    aiState = 1;
                    aiTimer = 0;
                }
            } else if (aiState == 1) {
                b2body.setLinearVelocity(new Vector2(-2f, 0));
                if (b2body.getPosition().x <= 22 * 16 / TiledApp.PPM) {
                    aiState = 0;
                    aiTimer = 0;
                }
            }
            aiTimer += dt;

        }
    }

    public void handleCollision() {
        states.setDirection();
        pause(false);
        b2body.setLinearVelocity(new Vector2(0, 0));
    }
}
