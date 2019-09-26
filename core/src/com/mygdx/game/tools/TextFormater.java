package com.mygdx.game.tools;

/**
 * Created by gregorykelsie on 2018-11-25.
 */

public class TextFormater {
    /**
     * Return a formatted description String that has new lines in the correct
     * places so it doesn't overflow out of the text box.
     * @param description The description before formatting.
     * @return A String of the formatted description with new lines so it doesn't overflow out
     * of the text box.
     */
    public static String formatText(String description) {
        String[] splitDescription = description.split(" ");
        String newDescription = "";
        double lineCurrentValue = 0;
        for (int i = 0; i < splitDescription.length; i++) {
            if ((lineCurrentValue + 1 + getWordLengthValue(splitDescription[i])) <= 40.0) {
                newDescription = newDescription + splitDescription[i] + " ";
                lineCurrentValue = lineCurrentValue + 1 + getWordLengthValue(splitDescription[i]);
            } else {
                newDescription = newDescription + "\n" + splitDescription[i] + " ";
                lineCurrentValue = getWordLengthValue(splitDescription[i]) + 1; //+1 for the space
            }
        }
        return newDescription;
    }

    public static double getWordLengthValue(String word) {
        double value = 0;
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == 'i' || word.charAt(i) == 'l' || word.charAt(i) == '!' ||
                    word.charAt(i) == '.') {
                value += 0.5;
            } else {
                value++;
            }
        }
        return value;
    }
}
