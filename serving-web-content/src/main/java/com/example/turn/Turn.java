package com.example.turn;

/**
 * This class represents a turn taken by a {@link com.example.player.Player}
 */
public class Turn {
    /* Instantiate Fields Here
     * keeper attribute?
    */


    /**
     * Constructor
     */
    public Turn() {

    }

    public void createTurn() {

    }

    public void storeKeepers() {

    }

    /* This is the inner class representing a roll. Can be switched to be non-static as necessary */
    private static class Roll {

        /*
         * call dice methods from here
         */
        private void roll() {
            // Dice.rollDice();
        }

        private void selectKeepers() {

        }

    }

    /* This is the inner class representing the Dice. Can be switched to be non-static as necessary */
    private static class Dice {

        /*
         * get random roll results from here
         */
         private int rollDice() {
            return 0;
         }
    }

}