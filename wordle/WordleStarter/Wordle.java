/*
 * File: Wordle.java
 * -----------------
 * This module is the starter file for the Wordle assignment.
 * BE SURE TO UPDATE THIS COMMENT WHEN YOU COMPLETE THE CODE.
 */

import java.awt.Color;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import edu.willamette.cs1.wordle.WordleDictionary;
import edu.willamette.cs1.wordle.WordleGWindow;

public class Wordle {
    String word;
    int attempts = 0;
    public void run() {
        gw = new WordleGWindow();
        gw.addEnterListener((s) -> enterAction(s));
        word = WordleDictionary.FIVE_LETTER_WORDS[(int) (Math.random() * WordleDictionary.FIVE_LETTER_WORDS.length)];
        word = word.toUpperCase();
    }

/*
 * Called when the user hits the RETURN key or clicks the ENTER button,
 * passing in the string of characters on the current row.
 */

    public void endGame(){
        gw.setCurrentRow(8);
    }
    private void animateVictory() {
        Color[] colors = {
            Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, 
            Color.BLUE, Color.MAGENTA, Color.PINK
        };

        Timer timer = new Timer(100, new ActionListener() {
            int cycle = 0;
            int colorIndex = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (cycle >= 10) {
                    ((Timer) e.getSource()).stop();
                    setAllKeysToCorrectColor();
                    return;
                }

                for (char letter = 'A'; letter <= 'Z'; letter++) {
                    gw.setKeyColor(String.valueOf(letter), colors[colorIndex]);
                }

                colorIndex = (colorIndex + 1) % colors.length;
                if (colorIndex == 0) {
                    cycle++;
                }
            }
        });
        timer.start();
    }

    private void setAllKeysToCorrectColor() {
        for (char letter = 'A'; letter <= 'Z'; letter++) {
            gw.setKeyColor(String.valueOf(letter), WordleGWindow.CORRECT_COLOR);
        }
    }

    public void enterAction(String guess) {
        boolean isValidWord = false;
        for (String validWord : WordleDictionary.FIVE_LETTER_WORDS) {
            if (validWord.equalsIgnoreCase(guess)) {
                isValidWord = true;
                break;
            }
        }
    
        if (!isValidWord) {
            gw.showMessage("Not in word list");
            return;
        }
        
        String targetWord = word;
        attempts++;
        gw.showMessage(word);

        // First loop to check for missing letters
        for (int i = 0; i < 5; i++) {
            char guessChar = guess.charAt(i);
            if (targetWord.indexOf(guessChar) < 0) {
                gw.setSquareColor(gw.getCurrentRow(), i, WordleGWindow.MISSING_COLOR);
                gw.setKeyColor(String.valueOf(guessChar), WordleGWindow.MISSING_COLOR);
            }
        }
            gw.showMessage(word);

        // Second loop to check for correct letters
        for (int i = 0; i < 5; i++) {
            char guessChar = guess.charAt(i);
            if (guessChar == targetWord.charAt(i)) {
                gw.setSquareColor(gw.getCurrentRow(), i, WordleGWindow.CORRECT_COLOR);
                gw.setKeyColor(String.valueOf(guessChar), WordleGWindow.CORRECT_COLOR);
                guess = guess.substring(0, i) + "2" + guess.substring(i + 1);
                targetWord = targetWord.substring(0, i) + "1" + targetWord.substring(i + 1);
            }
        }
        
        // Third loop to check for present letters
        for (int i = 0; i < 5; i++) {
            char guessChar = guess.charAt(i);
            int index = targetWord.indexOf(guessChar);
            if (index >= 0 && guessChar != '2') {
                gw.setSquareColor(gw.getCurrentRow(), i, WordleGWindow.PRESENT_COLOR);
                if (gw.getKeyColor(String.valueOf(guessChar).toUpperCase()) != WordleGWindow.CORRECT_COLOR) {
                    gw.setKeyColor(String.valueOf(guessChar), WordleGWindow.PRESENT_COLOR);
                }
                guess = guess.substring(0, i) + "2" + guess.substring(i + 1);
                targetWord = targetWord.substring(0, index) + "1" + targetWord.substring(index + 1);
            }
        }
    
        System.out.println(guess + " " + targetWord);
        System.out.println(gw.getSquareColor(gw.getCurrentRow(), 3) + " " + gw.getSquareColor(gw.getCurrentRow(), 4));
        if (targetWord.equals("11111")) {
            gw.showMessage("Great job!!!!!");
            animateVictory();
            endGame();
            return;
        }
    
        if (attempts > 5) {
            gw.showMessage(word);
            endGame();
            return;
        }
    
        gw.setCurrentRow(gw.getCurrentRow() + 1);
    }
    
    

/* Startup code */

    public static void main(String[] args) {
        new Wordle().run();
    }

/* Private instance variables */

    private WordleGWindow gw;

}
