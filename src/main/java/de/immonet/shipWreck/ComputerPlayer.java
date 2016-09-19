package de.immonet.shipWreck;

import java.util.ArrayList;

/**
 * Created by spoppe on 09.09.2016.
 */
public class ComputerPlayer extends Player {
    public ComputerPlayer(ArrayList<ArrayList<Integer>> shipLocations) {
        shipList = new ArrayList<>(setShips(shipLocations));
        gameField = new GameField(setUpField());
    }

    public ComputerPlayer() {
        gameField = new GameField(setUpField());
    }

    private ArrayList<Integer> comTipps = new ArrayList<>();
    private Integer nextTipp = 77;
    private ArrayList<Integer> guessingIndexes = createGuessingIndexes();
    private ArrayList<Boolean> shotEvaluations = new ArrayList<>();
    private ArrayList<Integer> locationsOfHits = new ArrayList<>();

    public void guess() {
        Integer guess ;

        if (!shotEvaluations.contains(true)){
            do{
            guess = defaultGuessing();}while (comTipps.contains(guess));
            comTipps.add(guess);
        } else {
            guess = nextTipp;
        }

        String result = evaluateTipp(guess);

        evaluateResult(guess, result);
    }

    public Integer defaultGuessing() {
        Integer guess;
        if(guessingIndexes.size()>0) {
            guess = createRandomMultiplierOfThree();
        }else{guess=guessRandomly();}
        return guess;
    }

    public ArrayList<Integer> createGuessingIndexes(){
        ArrayList<Integer> guessingIndexes= new ArrayList<>();
        for (int number=0;number<17;number++){
           guessingIndexes.add(number);
        }
        return guessingIndexes;
    }

    public void evaluateResult(Integer guess, String result) {
        if (result.contains("getroffen")) {
            locationsOfHits.add(guess);
            shotEvaluations.add(true);
        }

        if (result.contains("versenkt!"))

        {
            shotEvaluations.clear();
            locationsOfHits.clear();
        }

        if (locationsOfHits.size() == 1) {
            guessingSecondLocation();

        }

        if (locationsOfHits.size() > 1) {
            guessingThirdLocation();

        }


    }

    public Integer createRandomMultiplierOfThree() {
        Integer guess;
        guess = 3 * getGuessingIndex();
        return guess;
    }

    public int getGuessingIndex() {
        Integer currentGuessingIndex=(int)(Math.random()*(guessingIndexes.size()));
        currentGuessingIndex=guessingIndexes.get(currentGuessingIndex);
        guessingIndexes.remove(guessingIndexes.indexOf(currentGuessingIndex));
        return currentGuessingIndex;
    }

    public void guessingThirdLocation() {
        int firstHit = locationsOfHits.get(0);
        int secondHit = locationsOfHits.get(1);
        if (secondHit == (firstHit - 7)) {
            guessUpwardLane(firstHit, secondHit);
        } else if (secondHit == (firstHit + 1)) {
            guessRightwardLane(firstHit, secondHit);
        } else if (secondHit == (firstHit + 7)) {
            guessDownwardLane(firstHit, secondHit);
        } else if ((secondHit == (firstHit - 1))) {
            guessLeftwardLane(firstHit, secondHit);
        }
    }

    public void guessRight(int location) {
        nextTipp = location + 1;
        comTipps.add(nextTipp);
    }
    public void guessRightwardLane(int firstHit, int secondHit) {
        if (!comTipps.contains(secondHit + 1) && firstHit % 7 != 5) {
            guessRight(secondHit);
        } else if (!comTipps.contains(firstHit - 1)&&firstHit>=1) {
            guessLeft(firstHit);
        } else{escapeOverlay();}
    }



    public void guessLeftwardLane(int firstHit, int secondHit) {
        if (!comTipps.contains(firstHit - 2)){
            guessLeft(secondHit);}
        else {
            escapeFreeze();
        }
    }

    public void guessDownwardLane(int firstHit, int secondHit) {
        if (!comTipps.contains(firstHit + 14))
            guessDownwards(secondHit);
        else {
            escapeFreeze();
        }
    }


    public void guessUpwardLane(int firstHit, int secondHit) {
        if (!comTipps.contains(secondHit -7) && firstHit > 13 ) {
            guessUpwards(secondHit);
        } else if (!comTipps.contains(firstHit + 7)) {
            guessDownwards(firstHit);
        } else{escapeOverlay();}
    }

    public void guessingSecondLocation() {
        int firstHit = locationsOfHits.get(0);
        if ((firstHit > 6) && !comTipps.contains(firstHit - 7)) {
            guessUpwards(firstHit);
        } else if (firstHit % 7 != 6 && !comTipps.contains(firstHit + 1)) {
            guessRight(firstHit);
        } else if (firstHit < 35 && !comTipps.contains(firstHit + 7)) {
            guessDownwards(firstHit);
        } else if (firstHit % 7 != 0 && !comTipps.contains(firstHit - 1)) {
            guessLeft(firstHit);
        } else {escapeFreeze();}
    }

    public void escapeOverlay(){
        locationsOfHits.remove(1);
        nextTipp= guessRandomly();
    }

    public void escapeFreeze() {
        nextTipp= guessRandomly();
        shotEvaluations.clear();
        locationsOfHits.clear();
    }

    public int guessRandomly() {
            int guess ;
        do {
            guess= (int) (Math.random() * 49);
        } while (comTipps.contains(guess));
        comTipps.add(guess);
        return guess;
    }

    public void guessLeft(int location) {
        nextTipp = location - 1;
        comTipps.add(nextTipp);
    }

    public void guessDownwards(int location) {
        nextTipp = location + 7;
        comTipps.add(nextTipp);
    }


    public void guessUpwards(int location) {
        nextTipp = location - 7;
        comTipps.add(nextTipp);
    }

    public void shotMissed(int playerTipp) {
        gameHistory = "<p class=\"success text\">Dein Gegner hat verfehlt!</p>";
        gameField.setHit(playerTipp, 'o');
    }

    public int hitOnShip(int playerTipp, int hitCount, int i) {
        gameHistory = ("<p class=\"critical text\"> Dein Gegner hat das "+ shipList.get(i).getName() +" von dir getroffen!</p>");
        gameField.setHit(playerTipp, 'x');
        hitCount++;
        return hitCount;
    }

    public String evaluateTipp(int playerTipp) {
        int hitCount = 0;
        setGameHistory(playerTipp);
        gameField.printField();
        return gameHistory;

    }


    public void sunkenShip(int i) {
        gameHistory = ("<p class=\"critical text\"> Dein " + shipList.get(i).getName() + " wurde vom Gegner versenkt!</p>");
        wreckTheShip(i);
    }

    public void gameOver() {
        gameHistory= "<p class=\"text\"><a class= \"critical restart\" " +
                "href=\"http://ws840601:9889/schiffeversenken/neustart" +
                "\"title=\"Sie werden nicht glauben was passiert, wenn sie hier klicken!\">" +
                " Alle deine Schiffe wurden versenkt! Du hast verloren!</a></p></body>";
    }



    public Ship[] createShipArray(ArrayList<ArrayList<Integer>> shipLocations) {
        Ship[] gameShips = new Ship[3];

        for (int indexOfCreatedShip = 0; indexOfCreatedShip < 3; indexOfCreatedShip++) {
            gameShips[indexOfCreatedShip] = new Ship(shipLocations.get(indexOfCreatedShip), (indexOfCreatedShip+1) + ". Spielerschiff");
        }

        return gameShips;
    }

    public String showResult() {


        String output = "<h6> Deine Schiffe: </h6>";

        output = printShips(output);

        output = printWracks(output);

        return output;
    }

    public char[] setUpField() {

        char [] setup = new char[49];
        ArrayList<Integer> positions = new ArrayList<>();
        for (int i = 0; i < 49; i++) {
            locatePositions(shipList, positions);
            if(positions.contains(i)){
                setup[i]='#';
            }else{
                setup[i] = '~';}
        }
        return setup;
    }

    public void locatePositions(ArrayList<Ship> ships, ArrayList<Integer> positions) {
        for(int j = 0; j<3 ;j++){
            for (int k=0;k<3;k++){
                positions.add(ships.get(j).getCellLocation().get(k));
            }
        }
    }


}
