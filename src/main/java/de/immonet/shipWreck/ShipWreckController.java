package de.immonet.shipWreck;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;


/**
 * Created by spoppe on 31.08.2016.
 */
@RestController
@RequestMapping("/schiffeversenken")
public class ShipWreckController {


    private ShipWreckService wreckService = new ShipWreckService();



    @RequestMapping(value = "/setzen", method = RequestMethod.GET)
    public String placeShip(@RequestParam(value = "anfangspunkt", defaultValue = "X9") String startingLocation, @RequestParam(value = "endpunkt", defaultValue = "X9") String endLocation) {
        String output = printHead();
        if (wreckService.indexOfCreatedComShips < 3) {
            output = printShipSetup(startingLocation, endLocation, output);
        }
        if (wreckService.indexOfCreatedComShips == 3) {
            output = printHead();
            output = printGameField(output, wreckService.userSettingField.getSetup());

        output += "<div class=\"text success\">Sie haben ihre Schiffe erfolgreich gesetzt!" +
                "<p><form action=\"http://ws840601:9889/schiffeversenken/raten\">" +
                "                <button type=\"submit\">Hier gehts weiter</button> " +
                "                </form></p></div>\n ";
        }
        return output;
    }

    public String printShipSetup(@RequestParam(value = "anfangspunkt", defaultValue = "X9") String startingLocation, @RequestParam(value = "endpunkt", defaultValue = "X9") String endLocation, String output) {
        ArrayList<Integer> cellLocations = new ArrayList<>();

        Integer[] locations = transformLocationsfromStringToInteger(startingLocation, endLocation);

        if (locations[2] == locations[0] + 7 * 2 || locations[2] == locations[0] + 2) {
            storeInputInformation(cellLocations, locations);

            if (wreckService.indexOfCreatedComShips == 2) {
                wreckService.game.setCom(new ComputerPlayer(wreckService.shipLocations));
            }
            wreckService.indexOfCreatedComShips++;
            output +="<div>";
            output = printGameField(output, wreckService.userSettingField.getSetup());
            output += printSetShipsFormular()+"</div>";

        } else {
            output += "<div>";
            output = printGameField(output, wreckService .userSettingField.getSetup());
            output += printSetShipsFormular() + "</div>";
        }
        return output;
    }

    public void storeInputInformation(ArrayList<Integer> cellLocations, Integer[] locations) {
        transformArrayToArrayList(locations, cellLocations);
        putMarksOnGameField(locations);
        wreckService.shipLocations.add(cellLocations);
    }

    public void transformArrayToArrayList(Integer[] array, ArrayList<Integer> list) {
        for (Integer element : array) {
            list.add(element);
        }
    }

    public Integer[] transformLocationsfromStringToInteger(String startingLocation, String endLocation) {
        Integer integerStartingLocation = Helper.inputEvaluator(startingLocation);
        Integer integerEndLocation = Helper.inputEvaluator(endLocation);
        Integer medianPoint = (integerStartingLocation + integerEndLocation) / 2;
        Integer[] locations = {integerStartingLocation, medianPoint, integerEndLocation};
        return locations;
    }


    @RequestMapping(value = "/raten", method = RequestMethod.GET)
    public String shoot(@RequestParam(value = "tipp", defaultValue = "X9") String tipp) {

        String output = printHead();

        if (wreckService.indexOfCreatedComShips == 3) {
            if (!getCom().getShipList().isEmpty() && !getUser().getShipList().isEmpty()) {

                bothPlayersGuess(tipp);

                output = "<div >" + resultsOfPlayerGuessings(output) + "</div>";

            } else if (getCom().getShipList().isEmpty() && getUser().getShipList().isEmpty()) {

                output = "<div>" + itsADraw(output) + "</div>";


            } else if (getCom().getShipList().isEmpty()) {

                output = "<div>" + comWon(output) + "</div>";

            } else {

                output = "<div>" + userWon(output) + "</div>";


            }
            return output + "</body>";
        } else {
            output +="<div>";
            output = printGameField(output, wreckService.userSettingField.getSetup()) + printSetShipsFormular() + "</div>";
            return output;

        }
    }


    public void bothPlayersGuess(@RequestParam(value = "tipp", defaultValue = "X9") String tipp) {
        if (!tipp.equals("X9")) {
            try {
                int intTipp = Integer.parseInt(tipp);
                getUser().guess(intTipp);
            } catch (Exception ex) {
                getUser().guess(tipp);
            }

            getCom().guess();
        }
    }

    public String resultsOfPlayerGuessings(String output) {
        output += "<body>" + printBothGamefield();

        output += "<div class=\"results\"><p class=\"text\">" + getUser().getGameHistory() + "</p>";

        return output + "<p class=\"text\">" + getCom().getGameHistory() + "</p></div>";
    }

    public String itsADraw(String output) {
        output += "<body>" + printBothGamefield();
        output += "<div class=\"results\"><p class=\"text\"><a class= \"critical restart\" " +
                "href=\"http://ws840601:9889/schiffeversenken/neustart" +
                "\"title=\"Sie werden nicht glauben was passiert, wenn sie hier klicken!\">" +
                " Die Schlacht war zwar episch, aber leider wird niemand darüber berichten können.</a></p></div>";
        return output;
    }

    public String comWon(String output) {
        output += "<body>" + printBothGamefield();
        output += "<div class=\"results\"><p class=\"text\"><a class= \"critical restart\" " +
                "href=\"http://ws840601:9889/schiffeversenken/neustart" +
                "\"title=\"Sie werden nicht glauben was passiert, wenn sie hier klicken!\">" +
                " Alle deine Schiffe wurden versenkt! Du hast verloren!</a></p></div>";
        return output;
    }

    public String userWon(String output) {
        output += "<body>" + printBothGamefield();
        output += "<div class=\"results\"><p class=\"text\"><a class= \"success restart\" " +
                "href=\"http://ws840601:9889/schiffeversenken/neustart\"" +
                " title=\"Sie werden nicht glauben was passiert, wenn sie hier klicken!\">" +
                " Du hast alle Schiffe vesenkt. Herzlichen Glückwunsch!</a></p></div>";
        return output;
    }


    @RequestMapping(value = "/neustart", method = RequestMethod.GET)
    public String restart() {
        wreckService = new ShipWreckService();
        String output = printHead() + "<div>";
        output = printGameField(output, wreckService.userSettingField.getSetup()) + printSetShipsFormular() + "</div>";
        return output;
    }


    @RequestMapping(value="/spectator",method = RequestMethod.GET)
    public String printBothGamefield() {
        String output = printHead() + "<div>";
        for (int i = 0; i < 2; i++) {

            char[] gameFieldSetup = selectPlayer(i).getGameField().getSetup();

            if (selectPlayer(i) instanceof ComputerPlayer) {
                output = printComField(output, i, gameFieldSetup);
            } else {
                output = printUserField(output, i, gameFieldSetup);
            }
            output += "</div>";
        }


        return output;
    }

    public String printComField(String output, int i, char[] gameFieldSetup) {
        output += "<body><ul class=\"spielfeld\"><h4>Dein Spielfeld:</h4>" + printAlphabetList() + printNumberList();
        output = printGameField(output, gameFieldSetup, selectPlayer(i)) + "</ul>";
        output += "<div class=\"info com\">" + getCom().showResult() + "</div>";
        return output;
    }

    public String printUserField(String output, int i, char[] gameFieldSetup) {
        output += "<div class=\"info user\">" + getUser().showResult() + "</div>";
        output += "<body><ul class=\"spielfeld\"><h4>Gegner-Spielfeld:</h4>" + printAlphabetList() + printNumberList();
        output = printGameField(output, gameFieldSetup, selectPlayer(i)) + "</ul>";
        return output;
    }

    private Player selectPlayer(int i) {
        return wreckService.game.getPlayerList().get(i);
    }

    @RequestMapping(value = "/style", method = RequestMethod.GET)
    public String getStylesheet() {
        String stylesheet =
                ".wave{color: #00D8FF;}.critical{color: #EB3900}.success{color: #1ca507;}\n" +
                        ".nmrs{display: inline;padding-left: 25;}\n" +
                        ".normal{color: black;margin-right: 10px;}\n" +
                        ".spielbrett{color: #00D8FF;background-color:aliceblue;margin-left: 25;display: block; width: 245 }\n" +
                        ".spot{display:inline-block; width:35}\n" +
                        ".link{text-decoration: underline; color: black; font-size: 20; padding-top:200}\n" +
                        ".restart{text-decoration:underline}\n" +
                        "li{list-style: none;}\n" +
                        "ol{position: absolute;padding-left:0;padding-top:35}\n" +
                        "a{text-decoration: none;color: #00D8FF; }\n" +
                        "body{font-size: 30}\n" +
                        "a:hover.spot{text-shadow:0 0 5px orangered;color: orangered }\n" +
                        "a.restart:hover{color:27F213}\n" +
                        ".spielfeld{float:left;}\n" +
                        ".text{clear:both; font-size:25}\n" +
                        "a.restart.critical:hover{color:#ff30ef}\n" +
                        "div.info.com{display:inline-block;width:auto; margin-left:75; margin-right:15; margin-top: 110;float:left;}\n" +
                        "div.info.user{display:inline-block;width:auto; margin-left:65; margin-top: 110;float:left;}\n" +
                        "span.info{font-size:25;}\n" +
                        "h6{margin-bottom:10;}\n" +
                        ".after::after {content: \"\\A\";white-space: pre;}\n" +
                        ".results{ clear:both; display:block; margin-left:375;width:auto;}\n" +
                        ".formular{font-size:25;margin-top:75;}";
        return stylesheet;
    }

    private String printAlphabetList() {
        String alphabet = "ABCDEFG";
        String output = "<ol>";
        for (int i = 0; i < alphabet.length(); i++) {
            output += "<li>" + alphabet.charAt(i) + "</li>";
        }
        output += "</ol>";

        return output;
    }

    private String printNumberList() {
        String numbers = "0123456";
        String output = "<div class=\"nmrs\">";
        for (int i = 0; i < numbers.length(); i++) {
            output += "<span class=\"spot\">" + numbers.charAt(i) + "</span>";
        }
        output += "</div>";

        return output;
    }

    public String printGameField(String output, char[] gameFieldSetup, Player player) {
        output += "<div class=\"spielbrett\">";
        for (int i = 0; i < 49; i++) {
            if (i % 7 == 0) {
                output += "<li>";
            }

            output = letterChooser(output, gameFieldSetup[i], i, player);


            if (i % 7 == 6) {
                output += "</li>";
            }


        }

        output += "</div>";

        return output;
    }

    public String printGameField(String output, char[] gameFieldSetup) {
        //<ul class\"spielfeld\"><h4>Setze deine Schiffe: </h4> +printAlphabet + printNumber + printGamefield</ul>
        output += "<ul class=\"spielfeld\"><h4>Setze deine Schiffe: </h4>" +printAlphabetList() + printNumberList();
        output += "<div class=\"spielbrett\">";
        for (int i = 0; i < 49; i++) {
            if (i % 7 == 0) {
                output += "<li>";
            }

            output = letterChooser(output, gameFieldSetup[i], i);


            if (i % 7 == 6) {
                output += "</li>";
            }


        }

        output += "</div></ul>";

        return output;
    }

    public void putMarksOnGameField(Integer[] locations) {
        for (Integer location : locations) {
            wreckService.userSettingField.setHit(location, '#');
        }
    }

    public String letterChooser(String output, char c, int i, Player player) {
        if (player.equals(selectPlayer(0))) {
            if (c == 'x') {
                output += "<span class=\"critical spot\">" + c + "</span>";
            } else if (c == '~') {
                output += "<a class=\"spot\"href=\"http://ws840601:9889/schiffeversenken/raten?tipp=" + i + "\">" + c + "</a>";
            } else {
                output += "<span class=\"spot\">" + c + "</span>";
            }
        } else {
            if (c == 'x') {
                output += "<span class=\"critical spot\">" + c + "</span>";
            } else if (c == '#') {
                output += "<span class=\"success spot\">" + c + "</span>";
            } else if (c == '~') {
                output += "<span class=\"spot\"href=\"http://ws840601:9889/schiffeversenken/raten?tipp=" + i + "\">" + c + "</span>";
            } else {
                output += "<span class=\"spot\">" + c + "</span>";
            }
        }
        return output;
    }

    public String letterChooser(String output, char c, int i) {


        if (c == '#') {
            output += "<span class=\"success spot\">" + c + "</span>";
        } else if (c == '~') {
            output += "<span class=\"spot\"href=\"http://ws840601:9889/schiffeversenken/raten?tipp=" + i + "\">" + c + "</span>";
        } else {
            output += "<span class=\"spot\">" + c + "</span>";
        }

        return output;
    }

    public String printHead() {
        return "<head><link rel=\"stylesheet\" href=\"http://ws840601:9889/schiffeversenken/style\" type=\"text/css\"></head>";
    }

    public Player getCom() {
        return wreckService.game.getPlayerList().get(1);
    }

    public Player getUser() {
        return wreckService.game.getPlayerList().get(0);
    }

    public String printSetShipsFormular() {
        return "<div class=\"info com\"><form class=\"formular\" action=\"http://ws840601:9889/schiffeversenken/setzen\">\n" +
                "<p>\n" +
                "            <label class=\"success info com after\" for=\"anfangspunkt\">Anfangspunkt Schiff " + (wreckService.indexOfCreatedComShips + 1) + ":</label>\n" +
                "                <input class=\"after\" id=\"anfangspunkt\" name=\"anfangspunkt\">\n" +
                "</p>\n" +
                "<p>\n" +
                "            <label class=\"success info com after\" for=\"anfangspunkt\">Endpunkt Schiff " + (wreckService.indexOfCreatedComShips + 1) + ":</label>\n" +
                "                <input class=\"after\" id=\"endpunkt\" name=\"endpunkt\">\n" +
                "</p>\n" +
                "\n" +
                "\n" +
                "\n" +
                "        <button type=\"submit\">Schiff setzen</button>\n" +
                "</form><div>\n" ;

    }

    public static char[] setUpField() {
        char[] setup = new char[49];
        for (int i = 0; i < 49; i++) {
            setup[i] = '~';
        }
        return setup;
    }
}
