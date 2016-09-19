package de.immonet.shipWreck;

import java.util.ArrayList;

/**
 * Created by spoppe on 31.08.2016.
 */
public class Ship {
    private ArrayList<Integer> cellLocation = new ArrayList<>();
    private String name;
    private int hit = 0;
    private boolean floating = true;
    private boolean horizontal = true;



    public boolean controlYourself(Integer spielerTipp) {

        boolean getroffen = false;

        if (cellLocation.contains(spielerTipp)) {
            int indexDesTreffers = cellLocation.indexOf(spielerTipp);
            cellLocation.remove(indexDesTreffers);
            hit++;
            if (hit == 3) {
                floating = false;
            }
            getroffen = true;
        }
        return getroffen;

    }

    public Ship() {
    }

    public Ship(String name){
        this.name = name;
    }

    public Ship(ArrayList<Integer> cellLocation, String name) {
        this.cellLocation = cellLocation;
        this.name = name;
    }

    public void locateShip() {
        createCellLocation(putInitial());
    }

    private Integer putInitial() {
        Integer initial;
        do{ initial = (int) (Math.random() * 47);}while (initial ==40||initial==41);
        return initial;
    }


    public boolean isFloating() {
        return floating;
    }


    public int getHit() {

        return hit;
    }

    public ArrayList<Integer> getCellLocation() {
        return cellLocation;
    }

    public String getName() {
        return name;
    }
    
    public void createCellLocation(Integer initial) {
        //random waagerech/senkrecht
            if (initial < 35) {

                int horizontalMarker = (int) (Math.random() * 2);
                if (horizontalMarker == 0) {
                    horizontal = false;
                }
            }
            for (int i = 0; i < 7; i++) {
                if (initial == (5 + 7 * i) || initial == (6 + 7 * i)) {
                    horizontal = false;
                }
            }

            for (int j = 0; j < 3; j++) {
                cellLocation.add(initial);
                initial = horizontal ? initial + 1 : initial + 7;
            }

    }

    public void setCellLocation(ArrayList<Integer> cellLocation) {
        this.cellLocation = cellLocation;
    }
}
