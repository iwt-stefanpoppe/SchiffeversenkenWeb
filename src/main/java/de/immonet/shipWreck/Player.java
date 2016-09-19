package de.immonet.shipWreck;

import java.util.ArrayList;

/**
 * Created by spoppe on 09.09.2016.
 */
public class Player {
    ArrayList<Ship> shipList = setShips();
    ArrayList<String> shipWrecks = new ArrayList<>();
    GameField gameField = new GameField(setUpField());
    String gameHistory = "";

    public void guess(String playerTippString) {

        int playerTipp = Helper.inputEvaluator(playerTippString);

        if (playerTipp < 0 || playerTipp > 49) {
            gameHistory = "Bitte geben sie nur Kombinationen von A0-G6 ein!";

        } else {
            evaluateTipp(playerTipp);
        }


    }

    public void guess(int playerTipp) {
        if (playerTipp < 0 || playerTipp > 49) {
            gameHistory = "Bitte geben sie nur Kombinationen von A0-G6 ein!";

        } else {
            evaluateTipp(playerTipp);
        }
    }

    public void guess() {
    }

    public String evaluateTipp(int playerTipp) {
        setGameHistory(playerTipp);
        gameField.printField();
        return gameHistory;

    }

    public void shotMissed(int playerTipp) {
        gameHistory = "<p class=\"critical text\">Ihr Schuss hat verfehlt!</p>";
        gameField.setHit(playerTipp, 'o');
    }

    public void gameOver() {
        gameHistory= "<p class=\"text\"><a class= \"success restart\" " +
                "href=\"http://ws840601:9889/schiffeversenken/neustart\"" +
                " title=\"Sie werden nicht glauben was passiert, wenn sie hier klicken!\">" +
                " Du hast alle Schiffe vesenkt. Herzlichen Glückwunsch!</a></p></body>";
    }

    public void sunkenShip(int i) {
        gameHistory = ("<p class=\"success text\">Das " + shipList.get(i).getName() + " wurde versenkt!</p>");
        wreckTheShip(i);
    }

    public int hitOnShip(int playerTipp, int hitCount, int i) {
        gameHistory = ("<p class=\"success text\">Treffer auf dem " + shipList.get(i).getName() + "!</p>");
        gameField.setHit(playerTipp, 'x');
        hitCount++;
        return hitCount;
    }

    public ArrayList<Ship> setShips() {

        Ship[] gameShips = createShipArray();

        gameShips = overlayPrevention(gameShips);

        ArrayList<Ship> shipList = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            shipList.add(gameShips[i]);
        }
        return shipList;
    }

    public ArrayList<Ship> setShips(ArrayList<ArrayList<Integer>> shipLocations) {

        Ship[] gameShips = createShipArray(shipLocations);

        gameShips = overlayPrevention(gameShips);

        ArrayList<Ship> shipList = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            shipList.add(gameShips[i]);
        }
        return shipList;
    }



    protected void wreckTheShip(int i) {
        shipWrecks.add(shipList.get(i).getName());
        shipList.remove(i);
    }

    protected Ship[] createShipArray() {
        Ship[] gameShips = new Ship[3];
        gameShips[0] = new Ship("1. Gegnerschiff");
        gameShips[1] = new Ship("2. Gegnerschiff");
        gameShips[2] = new Ship("3. Gegnerschiff");
        for (int i = 0; i < 3; i++) {
            gameShips[i].locateShip();
        }
        return gameShips;
    }

    protected Ship[] createShipArray(ArrayList<ArrayList<Integer>> shipLocations) {
        Ship[] gameShips = new Ship[3];
        return gameShips;
    }

    public Ship[] overlayPrevention(Ship[] gameShips) {
        boolean noOverlay;
        do {
            noOverlay = true;
            ArrayList<Boolean> evaluation = new ArrayList<Boolean>();

            for (int i = 0; i < gameShips.length; i++) {
                for (int j = 0; j < gameShips.length; j++) {
                    if (i == j) {
                        continue;
                    }
                    boolean a = overlayCompare(gameShips[i], gameShips[j]);
                    evaluation.add(a);
                }
            }
            if (evaluation.contains(true)) {
                gameShips = createShipArray();
                noOverlay = false;
            }
        } while (!noOverlay);
        return gameShips;
    }

    public boolean overlayCompare(Ship schiff1, Ship schiff2) {
        ArrayList<Integer> cell1 = schiff1.getCellLocation();
        ArrayList<Integer> cell2 = schiff2.getCellLocation();
        boolean overlap = false;
        for (int i = 0; i < 3; i++) {
            overlap = cell1.contains(cell2.get(i));
            if (overlap) {
                break;
            }
        }
        return overlap;
    }

    public ArrayList<Ship> getShipList() {
        return shipList;
    }

    public ArrayList<String> getShipWrecks() {
        return shipWrecks;
    }

    public GameField getGameField() {
        return gameField;
    }

    public String getGameHistory() {
        return gameHistory;
    }

    private Ship selectedShip(int i) {
        return getShipList().get(i);
    }

    protected String printShips(String text) {
        for (int i = 0; i < getShipList().size(); i++) {
            String name = selectedShip(i).getName();
            String hitCount = "" + selectedShip(i).getHit();
            text += "<span class= \"wave info after\">" + name + ": " + hitCount + " Treffer </span>";
        }
        return text;
    }

    protected String printWracks(String text) {
        for (int i = 0; i < getShipWrecks().size(); i++) {
            text += "<span class= \"critical info after\">" + shipWrecks.get(i) + ": Versenkt!</span>";
        }

        return text;
    }

    public void setGameHistory(int playerTipp) {
        int hitCount = 0;
        for (int i = 0; i < shipList.size(); i++) {
            boolean gotHit = shipList.get(i).controlYourself(playerTipp);

            if (gotHit) {
                hitCount = hitOnShip(playerTipp, hitCount, i);
            }
            if (!shipList.get(i).isFloating()) {
                sunkenShip(i);
            }
            if(shipWrecks.size()==3){
                gameOver();
            }
        }

        if (hitCount == 0 && !shipList.isEmpty()) {

            shotMissed(playerTipp);

        }
    }

    public char[] setUpField() {
        char[] setup = new char[49];
        for (int i = 0; i < 49; i++) {
            setup[i] = '~';
        }
        return setup;
    }

    public String showResult() {


        String output = "<h6> Gegner-Schiffe: </h6>";

        output = printShips(output);

        output = printWracks(output);

        return output;
    }

    public void setGameField(GameField gameField) {
        this.gameField = gameField;
    }
}
