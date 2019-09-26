package com.mygdx.game.sprites.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.sprites.Direction;
import com.mygdx.game.sprites.State;

/**
 * Created by gregorykelsie on 2018-11-15.
 */

public class HumanStates {
    private float stateTimer;
    protected State currentState;
    protected State previousState;
    protected Direction direction;

    public HumanStates() {
        stateTimer = 0;
        currentState = State.STANDING;
        previousState = State.STANDING;
        direction = Direction.DOWN;
    }

    public void setDirection(Direction direction) { this.direction = direction; }

    public void setDirection() {
        if (currentState == State.WALKING_DOWN) {
            direction = Direction.DOWN;
        } else if (currentState == State.WALKING_UP) {
            direction = Direction.UP;
        } else if (currentState == State.WALKING_LEFT) {
            direction = Direction.LEFT;
        } else if (currentState == State.WALKING_RIGHT) {
            direction = Direction.RIGHT;
        }
    }

    public Direction getDirection() { return this.direction; }
    public void setCurrentState(State newState) { this.currentState = newState; }

    public TextureRegion getFrame(float dt, HumanTextures textures) {
        TextureRegion region;
        switch(currentState) {
            case WALKING_DOWN:
                region = textures.getWalkingDown().getKeyFrame(stateTimer, true);
                break;
            case WALKING_UP:
                region = textures.getWalkingUp().getKeyFrame(stateTimer, true);
                break;
            case WALKING_RIGHT:
                region = textures.getWalkingRight().getKeyFrame(stateTimer, true);
                break;
            case WALKING_LEFT:
                region = textures.getWalkingLeft().getKeyFrame(stateTimer, true);
                break;
            case JUMPING_DOWN:
                region = textures.getJumpingDown();
                break;
            case STANDING:
            default:
                if (direction == Direction.RIGHT) {
                    region = textures.getStandingRight();
                } else if (direction == Direction.LEFT) {
                    region = textures.getStandingLeft();
                } else if (direction == Direction.UP) {
                    region = textures.getStandingUp();
                } else {
                    region = textures.getStandingDown();
                }
                break;
        }
        //Increment stateTimer if the currentState is the previous state, otherwise reset it.
        stateTimer = currentState == previousState ? stateTimer + dt: 0;
        previousState = currentState;
        return region;
    }
}
