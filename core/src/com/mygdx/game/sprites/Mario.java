package com.mygdx.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.TiledApp;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.tools.MarioEdge;
import com.mygdx.game.tools.MarioSensor;

/**
 * Created by gregorykelsie on 2018-07-26.
 */

public class Mario extends Sprite {

    public State currentState;
    public State previousState;
    private float stateTimer;

    private Direction direction;

    private World world; //The world mario lives in.
    private Body b2body;

    private BodyDef bdef;
    private PlayScreen screen;


    private TextureRegion standUp;
    private TextureRegion standDown;
    private TextureRegion standLeft;
    private TextureRegion standRight;
    private TextureRegion jumpingDown;

    private Animation<TextureRegion> walkingDown;
    private Animation<TextureRegion> walkingUp;
    private Animation<TextureRegion> walkingRight;
    private Animation<TextureRegion> walkingLeft;

    private int jumpState;
    private float initialJumpY;
    private float initialJumpX;

    private final float SCALE = 1f;

    public Mario(World world, PlayScreen screen) {
        super(screen.getAtlas().findRegion("red"));
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        direction = Direction.DOWN;

        initAnimations();
        initStaticSprites();

        setBounds(0, 0, 28 / TiledApp.PPM, 28 / TiledApp.PPM); //Sprite Size on the screen
        setRegion(standDown);
        this.world = world;
        definePlayer();

        jumpState = 0;
    }

    public int getTileX() {
        return (int) Math.floor(b2body.getPosition().x * TiledApp.PPM / 16);
    }

    public int getTileY() {
        return (int) Math.floor(b2body.getPosition().y * TiledApp.PPM / 16);
    }

    public int getTileXX() {
        return (int) (b2body.getPosition().x * TiledApp.PPM / 16);
    }

    public int getTileYY() {
        return (int) (b2body.getPosition().y * TiledApp.PPM / 16);
    }

    public void jumpDown() {
        currentState = State.JUMPING_DOWN;
    }

    public void jumpRight() {
        currentState = State.JUMPING_RIGHT;
    }

    public void jumpLeft() {
        currentState = State.JUMPING_LEFT;
    }

    public void endJump() {
        currentState = State.STANDING;
    }

    private void definePlayer() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(25.25f * 16 / TiledApp.PPM, 5 * 16 / TiledApp.PPM);
        //bdef.position.set(18 * 16 / TiledApp.PPM, 5 * 16 / TiledApp.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        this.b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        //shape.setAsBox(32 / TiledApp.PPM, 32 / TiledApp.PPM);
        Vector2[] vex = {new Vector2(-8 / TiledApp.PPM, -12 / TiledApp.PPM),
                new Vector2(-8 / TiledApp.PPM, 2 / TiledApp.PPM),
                new Vector2(8 / TiledApp.PPM, -12 / TiledApp.PPM),
                new Vector2(8 / TiledApp.PPM, 2 / TiledApp.PPM)};
        shape.set(vex);
        fdef.filter.categoryBits = PlayScreen.PLAYER_BIT;
        setMaskBits(fdef.filter);
        fdef.shape = shape;
        b2body.setFixedRotation(true);
        b2body.createFixture(fdef).setUserData("playerBody");

        EdgeShape feet = new EdgeShape();
        feet.set(new Vector2(-1 / TiledApp.PPM, -12 / TiledApp.PPM),
                new Vector2(1 / TiledApp.PPM, -12 / TiledApp.PPM));
        fdef.shape = feet;
        fdef.filter.maskBits = PlayScreen.DOWN_LEDGE_BIT;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(new MarioSensor(this, MarioEdge.FEET));

        EdgeShape rightSide = new EdgeShape();
        rightSide.set(new Vector2(8 / TiledApp.PPM, 0 / TiledApp.PPM),
                new Vector2(8 / TiledApp.PPM, -5 / TiledApp.PPM));
        fdef.shape = rightSide;
        fdef.filter.maskBits = PlayScreen.RIGHT_LEDGE_BIT;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(new MarioSensor(this, MarioEdge.RIGHT));

        EdgeShape leftSide = new EdgeShape();
        leftSide.set(new Vector2(-8 / TiledApp.PPM, 0 / TiledApp.PPM),
                new Vector2(-8 / TiledApp.PPM, -5 / TiledApp.PPM));
        fdef.shape = leftSide;
        fdef.filter.maskBits = PlayScreen.LEFT_LEDGE_BIT;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(new MarioSensor(this, MarioEdge.LEFT));
    }

    public Body getB2body() {
        return b2body;
    }


    public World getWorld() {
        return world;
    }

    private void initAnimations() {
        initWalkingDownAnimation();
        initWalkingRightAnimation();
        initWalkingLeftAnimation();
        initWalkingUpAnimation();
    }

    private int getSpriteX(int spriteXOffset) {
        return getRegionX() + spriteXOffset;
    }
    private int getSpriteY(int spriteYOffset) {
        return getRegionY() + spriteYOffset;
    }

    private void initStaticSprites() {
        jumpingDown = new TextureRegion(getTexture(), getSpriteX(65), getSpriteY(65), 32, 32);
        standUp = new TextureRegion(getTexture(), getSpriteX(0), getSpriteY(0), 32, 32);
        standLeft = new TextureRegion(getTexture(), getSpriteX(0), getSpriteY(65), 32, 32);
        standRight = new TextureRegion(getTexture(), getSpriteX(32), getSpriteY(0), 32, 32);
        standDown = new TextureRegion(getTexture(), getSpriteX(65), getSpriteY(33), 32, 32);
    }

    private void initWalkingDownAnimation() {
        Array<TextureRegion> frames = new Array<TextureRegion>();
        frames.add(new TextureRegion(getTexture(), getSpriteX(65), getSpriteY(65), 32, 32));
        frames.add(new TextureRegion(getTexture(), getSpriteX(65), getSpriteY(33), 32, 32));
        frames.add(new TextureRegion(getTexture(), getSpriteX(65), getSpriteY(97), 32, 32));
        frames.add(new TextureRegion(getTexture(), getSpriteX(65), getSpriteY(33), 32, 32));
        walkingDown = new Animation<TextureRegion>(0.1f, frames);
    }

    private void initWalkingRightAnimation() {
        Array<TextureRegion> frames = new Array<TextureRegion>();
        frames.add(new TextureRegion(getTexture(), getSpriteX(32), getSpriteY(32), 32, 32));
        frames.add(new TextureRegion(getTexture(), getSpriteX(32), getSpriteY(0), 32, 32));
        frames.add(new TextureRegion(getTexture(), getSpriteX(32), getSpriteY(64), 32, 32));
        frames.add(new TextureRegion(getTexture(), getSpriteX(32), getSpriteY(0), 32, 32));
        walkingRight = new Animation<TextureRegion>(0.1f, frames);
    }

    private void initWalkingLeftAnimation() {
        Array<TextureRegion> frames = new Array<TextureRegion>();
        frames.add(new TextureRegion(getTexture(), getSpriteX(0), getSpriteY(33), 32, 32));
        frames.add(new TextureRegion(getTexture(), getSpriteX(0), getSpriteY(65), 32, 32));
        frames.add(new TextureRegion(getTexture(), getSpriteX(0), getSpriteY(97), 32, 32));
        frames.add(new TextureRegion(getTexture(), getSpriteX(0), getSpriteY(65), 32, 32));
        walkingLeft = new Animation<TextureRegion>(0.1f, frames);
    }

    private void initWalkingUpAnimation() {
        Array<TextureRegion> frames = new Array<TextureRegion>();
        frames.add(new TextureRegion(getTexture(), getSpriteX(32), getSpriteY(96), 32, 32));
        frames.add(new TextureRegion(getTexture(), getSpriteX(0), getSpriteY(0), 32, 32));
        frames.add(new TextureRegion(getTexture(), getSpriteX(64), getSpriteY(0), 32, 32));
        frames.add(new TextureRegion(getTexture(), getSpriteX(0), getSpriteY(0), 32, 32));
        walkingUp = new Animation<TextureRegion>(0.1f, frames);
    }

    public boolean isJumping() {
        if (currentState == State.JUMPING_DOWN ||currentState == State.JUMPING_RIGHT
                || currentState == State.JUMPING_LEFT) {
            return true;
        }
        return false;
    }

    public void update(float dt) {
        Gdx.app.log("updaz", currentState + "");
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        if (!isJumping()) {
            setRegion(getFrame(dt));
        } else {
            if (currentState == State.JUMPING_RIGHT) {
                updateJumpRight();
            } else if (currentState == State.JUMPING_DOWN) {
                updateJumpDown();
            } else if (currentState == State.JUMPING_LEFT) {
                updateJumpLeft();
            }
        }
    }

    private void updateJumpLeft() {
        if (jumpState == 0) {
            Filter filter = new Filter();
            filter.maskBits = PlayScreen.NOTHING_BIT;
            for (Fixture f: b2body.getFixtureList()) {
                if (f.getUserData().equals("playerBody")) {
                    f.setFilterData(filter);
                }
            }
            initialJumpY = b2body.getPosition().y;
            initialJumpX = b2body.getPosition().x;

            b2body.setLinearVelocity(new Vector2(-4.5f, 3f));
            jumpState = 1;
        } else if (jumpState == 1) {
            Gdx.app.log("JumpBody", "cur: " + b2body.getPosition().y + ", init: " + initialJumpY);
            if (b2body.getPosition().x <= initialJumpX - 0.45) {
                jumpState = 2;
                b2body.setLinearVelocity(new Vector2(0, 0));
                b2body.setLinearVelocity(new Vector2(-3f, -5f));
            }
        } else if (jumpState == 2) {
            Gdx.app.log("Jump", "state2");
            if (b2body.getPosition().y <= initialJumpY) {
                b2body.setLinearVelocity(new Vector2(0, 0));
                jumpState = 0;
                Filter filter = new Filter();
                setMaskBits(filter);
                for (Fixture f: b2body.getFixtureList()) {
                    if (f.getUserData().equals("playerBody")) {
                        f.setFilterData(filter);
                    }
                }
                currentState = State.STANDING;
                previousState = State.STANDING;
            }
        }
    }

    private void updateJumpRight() {
        if (jumpState == 0) {
            Gdx.app.log("JumpRight", "impulse");
            Filter filter = new Filter();
            filter.maskBits = PlayScreen.NOTHING_BIT;
            for (Fixture f: b2body.getFixtureList()) {
                if (f.getUserData().equals("playerBody")) {
                    f.setFilterData(filter);
                }
            }
            initialJumpY = b2body.getPosition().y;
            initialJumpX = b2body.getPosition().x;

            b2body.setLinearVelocity(new Vector2(4.5f, 3f));
            jumpState = 1;
        } else if (jumpState == 1) {
            Gdx.app.log("JumpBody", "cur: " + b2body.getPosition().y + ", init: " + initialJumpY);
            if (b2body.getPosition().x >= initialJumpX + 0.45) {
                jumpState = 2;
                b2body.setLinearVelocity(new Vector2(0, 0));
                b2body.setLinearVelocity(new Vector2(3f, -5f));
            }
        } else if (jumpState == 2) {
            Gdx.app.log("Jump", "state2");
            if (b2body.getPosition().y <= initialJumpY) {
                b2body.setLinearVelocity(new Vector2(0, 0));
                jumpState = 0;
                Filter filter = new Filter();
                setMaskBits(filter);
                for (Fixture f: b2body.getFixtureList()) {
                    if (f.getUserData().equals("playerBody")) {
                        f.setFilterData(filter);
                    }
                }
                currentState = State.STANDING;
                previousState = State.STANDING;
            }
        }
    }

    private void updateJumpDown() {
        if (jumpState == 0) {
            Gdx.app.log("Jump", "impulse");
            Filter filter = new Filter();
            filter.maskBits = PlayScreen.NOTHING_BIT;
            for (Fixture f: b2body.getFixtureList()) {
                if (f.getUserData().equals("playerBody")) {
                    f.setFilterData(filter);
                }
            }
            initialJumpY = b2body.getPosition().y;

            b2body.setLinearVelocity(new Vector2(0, 3f));
            jumpState = 1;
        } else if (jumpState == 1) {
            Gdx.app.log("JumpBody", "cur: " + b2body.getPosition().y + ", init: " + initialJumpY);
            if (b2body.getPosition().y >= initialJumpY + 0.15) {
                jumpState = 2;
                b2body.setLinearVelocity(new Vector2(0, 0));
                b2body.setLinearVelocity(new Vector2(0, -5f));
            }
        } else if (jumpState == 2) {
            Gdx.app.log("Jump", "state2");
            if (b2body.getPosition().y <= initialJumpY - 0.5) {
                b2body.setLinearVelocity(new Vector2(0, 0));
                jumpState = 0;
                Filter filter = new Filter();
                setMaskBits(filter);
                for (Fixture f: b2body.getFixtureList()) {
                    if (f.getUserData().equals("playerBody")) {
                        f.setFilterData(filter);
                    }
                }
                currentState = State.STANDING;
                previousState = State.STANDING;
            }
        }
    }

    private void setMaskBits(Filter filter) {
        filter.maskBits = PlayScreen.DEFAULT_BIT | PlayScreen.WARP_BIT | PlayScreen.WALL_BIT
                | PlayScreen.NPC_BIT | PlayScreen.DOWN_LEDGE_BIT | PlayScreen.RIGHT_LEDGE_BIT
                | PlayScreen.LEFT_LEDGE_BIT | PlayScreen.SIGN_BIT | PlayScreen.BERRY_BIT;
    }

    public void setDirection(Direction d) {
        this.direction = d;
    }

    public Direction getDirection() { return direction; }

    public void setCurrentState(State newState) {
        currentState = newState;
    }

    public TextureRegion getFrame(float dt) {
        currentState = getState();
        TextureRegion region;
        switch(currentState) {
            case WALKING_DOWN:
                region = walkingDown.getKeyFrame(stateTimer, true);
                break;
            case WALKING_UP:
                region = walkingUp.getKeyFrame(stateTimer, true);
                break;
            case WALKING_RIGHT:
                region = walkingRight.getKeyFrame(stateTimer, true);
                break;
            case WALKING_LEFT:
                region = walkingLeft.getKeyFrame(stateTimer, true);
                break;
            case JUMPING_DOWN:
                region = jumpingDown;
                break;
            case STANDING:
            default:
                if (direction == Direction.RIGHT) {
                    region = standRight;
                } else if (direction == Direction.LEFT) {
                    region = standLeft;
                } else if (direction == Direction.UP) {
                    region = standUp;
                } else {
                    region = standDown;
                }
                break;
        }

        //Increment stateTimer if the currentState is the previous state, otherwise reset it.
        stateTimer = currentState == previousState ? stateTimer + dt: 0;
        previousState = currentState;
        return region;
    }

    public State getState() {
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

    public State getCurrentState() {
        return currentState;
    }
}
