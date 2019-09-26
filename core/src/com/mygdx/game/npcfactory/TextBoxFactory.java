package com.mygdx.game.npcfactory;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.mygdx.game.sprites.components.TextBox;

/**
 * Created by gregorykelsie on 2018-11-25.
 */

public class TextBoxFactory {
    private BitmapFont font;
    public TextBoxFactory() {

    }

    public TextBox getTextBox(int id) {
        if (id == 0) {
            return new TextBox("Welcome to Route 1.");
        }

        return new TextBox("DEFAULT TEXTBOX");
    }
}
