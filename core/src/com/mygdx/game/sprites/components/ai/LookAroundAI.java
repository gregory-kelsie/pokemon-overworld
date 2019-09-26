package com.mygdx.game.sprites.components.ai;

import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.sprites.Direction;
import com.mygdx.game.sprites.components.HumanStates;

/**
 * Created by gregorykelsie on 2018-11-16.
 */

public class LookAroundAI extends AI {
    public LookAroundAI(Body b2body, HumanStates states) {
        super(b2body, states);
    }

    public void update(float dt) {
        if (!pauseAI) {
            if (aiTimer >= 1) {
                int directionId = (int) Math.round(3 * Math.random());
                if (directionId == 0) {
                    states.setDirection(Direction.UP);
                } else if (directionId == 1) {
                    states.setDirection(Direction.DOWN);
                } else if (directionId == 2) {
                    states.setDirection(Direction.LEFT);
                } else if (directionId == 3) {
                    states.setDirection(Direction.RIGHT);
                }
                aiTimer = 0;
            }
            aiTimer += dt;
        }
    }
}
