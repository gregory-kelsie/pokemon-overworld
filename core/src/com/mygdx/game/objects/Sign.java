package com.mygdx.game.objects;

import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.TiledApp;
import com.mygdx.game.sprites.components.TextBox;

/**
 * Created by gregorykelsie on 2018-11-29.
 */

public class Sign {

    private Body b2body;
    private TextBox textBox;

    public Sign(Body b2body, TextBox textBox) {
        this.b2body = b2body;
        this.textBox = textBox;
    }

    public Body getB2body() {
        return b2body;
    }

    public TextBox getTextBox() {
        return textBox;
    }

    public int getTileYBotSide() {
        return (int) ((b2body.getPosition().y - (12 / TiledApp.PPM)) * TiledApp.PPM / 16);
    }
}
