package de.immonet.shipWreck;


import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Created by spoppe on 31.08.2016.
 */
@Service
public class ShipWreckService {
    private String gameHistory = "";
    ArrayList<Ship> shipList = new ArrayList<Ship>();
    ArrayList<String> shipWrecks=new ArrayList<>();

    public ShipWreckService() {
        setShips();
    }

    public static final int GAMEOVER = 3;


    public ArrayList<Ship> setShips() {

        Ship[] gameShips = createShipArray();

        overlayPrevention(gameShips);

        for (int i = 0; i < GAMEOVER; i++) {
            shipList.add(gameShips[i]);
        }
        return shipList;
    }


    public void run(String playerTippString) {


        int playerTipp = Helper.inputEvaluator(playerTippString);

        if (playerTipp < 0 || playerTipp > 49) {
            gameHistory = "Bitte geben sie nur Kombinationen von A0-G6 ein!";

        } else {
            evaluateTipp(playerTipp);
        }


    }

    public String evaluateTipp(int playerTipp) {
        int hitCount = 0;
        for (int i = 0; i < shipList.size(); i++) {

            boolean gotHit = shipList.get(i).controlYourself(playerTipp);

            if (gotHit) {
                gameHistory = ("<div><font color=#27D800>Treffer auf der " + shipList.get(i).getName() + "!</font></div>");
                hitCount++;
            }
            if (!shipList.get(i).isFloating()) {
                gameHistory = ("<div><font color=#27D800>Die " + shipList.get(i).getName() + " wurde versenkt!</font></div>");
                wreckTheShip(i);
            }

        }
        if (hitCount == 0) {
            gameHistory = shipList.isEmpty() ? "<div><font color=<div><font color=#27D800>Sie haben alle Schiffe vesenkt." +
                    " Herzlichen Glückwunsch!</font></div>" : "<div><font color=#ED1600>Ihr Schuss hat verfehlt!";
        }
        return gameHistory;
    }

    private void wreckTheShip(int i) {
        shipWrecks.add(shipList.get(i).getName());
        shipList.remove(i);
    }

    private Ship[] createShipArray() {
        Ship[] gameShips = new Ship[3];
        gameShips[0] = new Ship("Weserkrone");
        gameShips[1] = new Ship("Black Pearl");
        gameShips[2] = new Ship("Gorch Fock");
        for (int i = 0; i < 3; i++) {
            gameShips[i].locateShip();
        }
        return gameShips;
    }

    public Ship[] overlayPrevention(Ship[] gameShips) {
        boolean noOverlay;
        do {
            noOverlay = true;
            ArrayList<Boolean> evaluation = new ArrayList<Boolean>();

            for (int i = 0; i < gameShips.length; i++) {
                for (int j = 0; j < gameShips.length; j++) {
                    if(i==j){continue;}
                    boolean a = overlayCompare(gameShips[i], gameShips[j]);
                    evaluation.add(a);
                }
            }
            if (evaluation.contains(true)) {
                gameShips=createShipArray();
                noOverlay = false;
            }
        } while (!noOverlay);
        return gameShips;
    }

    public boolean overlayCompare(Ship schiff1, Ship schiff2) {
        ArrayList<Integer> cell1 = schiff1.getCellLocation();
        ArrayList<Integer> cell2 = schiff2.getCellLocation();
        boolean overlap = false;
        for (int i = 0; i < GAMEOVER; i++) {
            overlap = cell1.contains(cell2.get(i));
            if (overlap) {
                break;
            }
        }
        return overlap;
    }

    public String getGameHistory() {
        return gameHistory;
    }

    public void setShipList(ArrayList<Ship> shipList) {
        this.shipList = shipList;
    }

    public ArrayList<Ship> getShipList() {
        return shipList;
    }
    public ArrayList<String> getShipWrecks() {
        return shipWrecks;
    }
}

