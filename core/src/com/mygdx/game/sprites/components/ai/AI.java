package com.mygdx.game.sprites.components.ai;

import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.sprites.components.HumanStates;

/**
 * Created by gregorykelsie on 2018-11-16.
 */

public abstract class AI {
    protected float aiTimer;
    protected int aiState;
    protected boolean pauseAI;

    protected Body b2body;
    protected HumanStates states;

    public AI(Body b2body, HumanStates states) {
        aiState = 0;
        aiTimer = 0;
        pauseAI = false;
        this.b2body = b2body;
        this.states = states;
    }

    public void pause() {
        pauseAI = true;
    }

    public abstract void update(float dt);

    public void handleCollision() {

    }

    public void pause(boolean resetTimer) {
        pause();
        if (resetTimer) {
            aiTimer = 0;
        }
    }

    public void resume() {
        pauseAI = false;
    }
}
