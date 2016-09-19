package de.immonet.shipWreck;

import java.io.*;

/**
 * Created by spoppe on 31.08.2016.
 */
abstract class Helper {
    public static Integer inputEvaluator(String eingabe) {
        Integer result=77;
        if (eingabe.length() < 3) {
            char ersteStelle = eingabe.charAt(0);
            int x = Character.getNumericValue(eingabe.charAt(1));
            if (0 <= x && x < 7) {
                //Beide ueberprüfungen auslagern!
                switch (ersteStelle) {
                    case 'A':
                        result = x;
                        break;
                    case 'B':
                        result = (x + 7 * 1);
                        break;
                    case 'C':
                        result = (x + 7 * 2);
                        break;
                    case 'D':
                        result = (x + 7 * 3);
                        break;
                    case 'E':
                        result = (x + 7 * 4);
                        break;
                    case 'F':
                        result = (x + 7 * 5);
                        break;
                    case 'G':
                        result = (x + 7 * 6);
                        break;
                    default:
                        result = 77;
                }
            } else {
                result = 77;
            }
        }
        return result;
    }


}
