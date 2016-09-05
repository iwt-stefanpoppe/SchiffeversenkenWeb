package de.immonet.shipWreck;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by spoppe on 02.09.2016.
 */
public class ShipTest {

    @Test
    public void pruefDichGiebtTrueZurück() throws Exception {
        ArrayList<Integer> testListe = new ArrayList<Integer>();
        Ship ship = new Ship();

        testListe.add(1);
        testListe.add(2);
        testListe.add(3);

        ship.setCellLocation(new ArrayList<>(testListe));

        ship.controlYourself(1);
    }

    @Test
    public void pruefDichGiebtFalseZurück() throws Exception {

        ArrayList<Integer> testListe = new ArrayList<Integer>();
        Ship ship = new Ship();

        testListe.add(1);
        testListe.add(2);
        testListe.add(3);

        ship.setCellLocation(new ArrayList<>(testListe));

        ship.controlYourself(4);
    }


    @Test
    public void createCellLocationDeliversVerticalList() throws Exception {
        ArrayList<Integer> testListe = new ArrayList<Integer>();
        Ship ship = new Ship();

        testListe.add(5);
        testListe.add(12);
        testListe.add(19);

        ship.createCellLocation(5);
        ArrayList<Integer> shipList = ship.getCellLocation();

        assertEquals(testListe, shipList);

    }

    @Test
    public void createCellLocationDeliversHorizontalList() throws Exception {
        ArrayList<Integer> testListe = new ArrayList<Integer>();
        Ship ship = new Ship();

        testListe.add(35);
        testListe.add(36);
        testListe.add(37);

        ship.createCellLocation(35);
        ArrayList<Integer> shipList = ship.getCellLocation();

        assertEquals(testListe, shipList);
    }


    @Test
    public void createCellLocationDeliversRandomList() throws Exception {

        //Dieser Test darf failen!

        ArrayList<Integer> testListe1 = new ArrayList<Integer>();
        Ship ship = new Ship();

        testListe1.add(15);
        testListe1.add(16);
        testListe1.add(17);

        ship.createCellLocation(15);
        ArrayList<Integer> shipList = ship.getCellLocation();

        assertEquals(testListe1, shipList);
    }
}