package com.example.player;

/* This is the inner class representing the ScoreCard.
     */
    public class ScoreCard {
        private boolean editMode;
        private int[] scores; 
        private int numYahtzeeBonuses; 
    
    /**
     * Constructor
     */
    public ScoreCard() {
        scores = new int[13];
        for (int i = 0; i < 13; i++)
		{
			scores[i] = null;
		}
        this.editMode = false; 
        this.numYahtzeeBonuses = 0; 
    }

    public void saveScore(int ind, int points) {
        if this.editMode = true{
            scores[ind] = points; 
        } 
    }

    public boolean getSectionAvailabilty(int ind){
        if scores[ind] == null{
            sectionAvailable = false;
        } 
    }

    public boolean boardFilled(){
        filled = true; 
        for (int i = 0; i < 13; i++){ 
            if scores[i] == null{ 
                filled = false;
            }
        }
        return filled; 
    }

    public int getScore(int ind) {
        return  scores[ind];
    }

    public int getUpperScore() { 
        int upperScore = 0; 
        for (int i = 0; i < 6; i++){ 
            if scores[i] != null{
                upperScore = upperScore + scores[i];
            } 
        }

        //Bonus Score 
        if upperScore >= 63 { 
            upperScore = upperScore + 35; 
        }
        return upperScore;
    }

    public void yahtzeeBonusScore(){ 
        //probably need to check if valid yahtzee first
        if numYahtzeeBonuses < 3 {
            numYahtzeeBonuses += 1; 
        }
    }

    public int getLowerScore() { 
        int lowerScore = 0; 
        for (int i = 7; i < 13; i++){ 
            if scores[i] != null{
                lowerScore = lowerScore + scores[i];
            } 
        }

        yahtzeeBonus = numYahtzeeBonuses * 100; 
        lowerScore = lowerScore + yahtzeeBonus; 
        return lowerScore;
    }

    public int getTotalScore(){ 
        return upperScore + lowerScore;
    }

    public void checkScoreValidity() { //TODO, might need one for each section of scorecard 
      
    }
}