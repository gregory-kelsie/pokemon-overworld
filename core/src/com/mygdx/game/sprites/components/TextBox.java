package com.mygdx.game.sprites.components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.tools.TextFormater;

import java.util.List;

/**
 * Created by gregorykelsie on 2018-11-25.
 */

public class TextBox {
    protected String text;
    protected TextBox nextTextBox;
    //Music to play - default is none
    //Reference to Inventory or Party or Player to effect them after a special textbox - Receive item etc.
    protected double textCounter;
    protected int textPosition;
    protected double triangleCounter;
    protected boolean displayTriangle;
    public TextBox(String text) {
        this.text = TextFormater.formatText(text);
        nextTextBox = null;
        textCounter = 0;
        textPosition = 0;
        triangleCounter = 0;
        this.displayTriangle = false;
    }

    public void refreshTextBox() {
        textCounter = 0;
        textPosition = 0;
        triangleCounter = 0;
        displayTriangle = false;
    }

    public boolean hasOptions() {
        return false;
    }

    public void setNextTextBox(TextBox nextTextBox) {
        this.nextTextBox = nextTextBox;
    }

    public TextBox getNextTextBox() {
        return nextTextBox;
    }

    public boolean hasNext() {
        if (nextTextBox != null) {
            return true;
        }
        return false;
    }

    public void update(float dt) {
        if (textPosition != text.length()) {
            textCounter += dt;
            if (textCounter >= 0.03) {
                if (textPosition < text.length()) {
                    textPosition += 1;
                    textCounter = 0;
                }
            }
        } else {
            if (hasNext()) {
                if (triangleCounter >= 0.35) {
                    triangleCounter = 0;
                    displayTriangle = !displayTriangle;
                } else {
                    triangleCounter += dt;
                }
            }
        }

    }

    public void quickUpdate() {
        textPosition = text.length();
    }

    public boolean isFinishedRendering() {
        if (textPosition == text.length()) {
            return true;
        }
        return false;
    }

    public boolean isFinished() {
        if (isFinishedRendering() && !hasNext()) {
            return true;
        }
        return false;
    }

    public void render(SpriteBatch batch, BitmapFont font, Texture triangle) {
        font.draw(batch, text.substring(0, textPosition), 100, 1130);
        if (displayTriangle && textPosition == text.length()) {
            batch.draw(triangle, 910, 1008, 50, 30);
        }
    }

}
