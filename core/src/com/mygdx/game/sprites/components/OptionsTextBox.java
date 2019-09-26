package com.mygdx.game.sprites.components;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * Created by gregorykelsie on 2018-11-25.
 */

public class OptionsTextBox extends TextBox {
    private String firstOption;
    private String secondOption;
    private TextBox firstOptionTextBox;
    private TextBox secondOptionTextBox;

    public OptionsTextBox(String text, String firstOption, String secondOption) {
        super(text);
        this.firstOption = firstOption;
        this.secondOption = secondOption;

        firstOptionTextBox = null;
        secondOptionTextBox = null;
    }

    public String getFirstOption() {
        return firstOption;
    }

    public String getSecondOption() {
        return secondOption;
    }

    @Override
    public boolean hasOptions() {
        return true;
    }

    public void selectOption(int selectedOption) {
        if (selectedOption == 1) {
            nextTextBox = firstOptionTextBox;
        } else {
            nextTextBox = secondOptionTextBox;
        }
    }

    public void setFirstOptionTextBox(TextBox firstOptionTextBox) {
        this.firstOptionTextBox = firstOptionTextBox;
    }

    public void setSecondOptionTextBox(TextBox secondOptionTextBox) {
        this.secondOptionTextBox = secondOptionTextBox;
    }

}
