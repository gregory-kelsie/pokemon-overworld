package com.mygdx.game.sprites.components;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.sprites.Direction;
import com.mygdx.game.sprites.State;

/**
 * Created by gregorykelsie on 2018-11-15.
 */

public class HumanTextures {

    private TextureRegion standUp;
    private TextureRegion standDown;
    private TextureRegion standLeft;
    private TextureRegion standRight;
    private TextureRegion jumpingDown;

    private Animation<TextureRegion> walkingDown;
    private Animation<TextureRegion> walkingUp;
    private Animation<TextureRegion> walkingRight;
    private Animation<TextureRegion> walkingLeft;
    
    private TextureRegion spriteTexture;
    public HumanTextures(TextureAtlas atlas, String spriteRegion) {
        spriteTexture = atlas.findRegion(spriteRegion);
        initAnimations();
    }

    private void initAnimations() {
        initStaticSprites();
        initWalkingDownAnimation();
        initWalkingRightAnimation();
        initWalkingLeftAnimation();
        initWalkingUpAnimation();
    }

    private void initStaticSprites() {
        standUp = new TextureRegion(spriteTexture.getTexture(), getSpriteX(0), getSpriteY(0), 32, 32);
        standLeft = new TextureRegion(spriteTexture.getTexture(), getSpriteX(0), getSpriteY(65), 32, 32);
        standRight = new TextureRegion(spriteTexture.getTexture(), getSpriteX(32), getSpriteY(0), 32, 32);
        standDown = new TextureRegion(spriteTexture.getTexture(), getSpriteX(65), getSpriteY(33), 32, 32);
        jumpingDown = new TextureRegion(spriteTexture.getTexture(), getSpriteX(65), getSpriteY(65), 32, 32);
    }

    private void initWalkingDownAnimation() {
        Array<TextureRegion> frames = new Array<TextureRegion>();
        frames.add(new TextureRegion(spriteTexture.getTexture(), getSpriteX(65), getSpriteY(65), 32, 32));
        frames.add(new TextureRegion(spriteTexture.getTexture(), getSpriteX(65), getSpriteY(33), 32, 32));
        frames.add(new TextureRegion(spriteTexture.getTexture(), getSpriteX(65), getSpriteY(97), 32, 32));
        frames.add(new TextureRegion(spriteTexture.getTexture(), getSpriteX(65), getSpriteY(33), 32, 32));
        walkingDown = new Animation<TextureRegion>(0.1f, frames);
    }

    private void initWalkingRightAnimation() {
        Array<TextureRegion> frames = new Array<TextureRegion>();
        frames.add(new TextureRegion(spriteTexture.getTexture(), getSpriteX(32), getSpriteY(32), 32, 32));
        frames.add(new TextureRegion(spriteTexture.getTexture(), getSpriteX(32), getSpriteY(0), 32, 32));
        frames.add(new TextureRegion(spriteTexture.getTexture(), getSpriteX(32), getSpriteY(64), 32, 32));
        frames.add(new TextureRegion(spriteTexture.getTexture(), getSpriteX(32), getSpriteY(0), 32, 32));
        walkingRight = new Animation<TextureRegion>(0.1f, frames);
    }

    private void initWalkingLeftAnimation() {
        Array<TextureRegion> frames = new Array<TextureRegion>();
        frames.add(new TextureRegion(spriteTexture.getTexture(), getSpriteX(0), getSpriteY(33), 32, 32));
        frames.add(new TextureRegion(spriteTexture.getTexture(), getSpriteX(0), getSpriteY(65), 32, 32));
        frames.add(new TextureRegion(spriteTexture.getTexture(), getSpriteX(0), getSpriteY(97), 32, 32));
        frames.add(new TextureRegion(spriteTexture.getTexture(), getSpriteX(0), getSpriteY(65), 32, 32));
        walkingLeft = new Animation<TextureRegion>(0.1f, frames);
    }

    private void initWalkingUpAnimation() {
        Array<TextureRegion> frames = new Array<TextureRegion>();
        frames.add(new TextureRegion(spriteTexture.getTexture(), getSpriteX(32), getSpriteY(96), 32, 32));
        frames.add(new TextureRegion(spriteTexture.getTexture(), getSpriteX(0), getSpriteY(0), 32, 32));
        frames.add(new TextureRegion(spriteTexture.getTexture(), getSpriteX(64), getSpriteY(0), 32, 32));
        frames.add(new TextureRegion(spriteTexture.getTexture(), getSpriteX(0), getSpriteY(0), 32, 32));
        walkingUp = new Animation<TextureRegion>(0.1f, frames);
    }

    public TextureRegion getStandingRight() {
        return standRight;
    }

    public TextureRegion getStandingLeft() {
        return standLeft;
    }

    public TextureRegion getStandingUp() {
        return standUp;
    }

    public TextureRegion getStandingDown() {
        return standDown;
    }

    public TextureRegion getJumpingDown() { return jumpingDown; }

    public Animation<TextureRegion> getWalkingDown() {
        return walkingDown;
    }

    public Animation<TextureRegion> getWalkingUp() {
        return walkingUp;
    }

    public Animation<TextureRegion> getWalkingLeft() {
        return walkingLeft;
    }

    public Animation<TextureRegion> getWalkingRight() {
        return walkingRight;
    }

    protected int getSpriteX(int spriteXOffset) {
        return spriteTexture.getRegionX() + spriteXOffset;
    }
    protected int getSpriteY(int spriteYOffset) {
        return spriteTexture.getRegionY() + spriteYOffset;
    }


}
