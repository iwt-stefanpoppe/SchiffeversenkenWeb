package de.immonet.shipWreck;

/**
 * Created by spoppe on 05.09.2016.
 */
public class GameField {

    private char[] setup;

    public void setUpField() {
        setup = new char[49];
        for (int i = 0; i < 49; i++) {
            setup[i] = '~';
        }
    }

    public void printField() {
        for (int i = 0; i < 49; i++) {
            System.out.print(setup[i]);
            if (i % 7 == 6) {
                System.out.print("\n");
            }
        }
        System.out.println("\n");
    }

    public void xOutHit(int i) {
        setup[i]='x';
    }

    public char[] getSetup() {
        return setup;
    }
}
