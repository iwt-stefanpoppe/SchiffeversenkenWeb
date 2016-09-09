package de.immonet.shipWreck;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * Created by spoppe on 31.08.2016.
 */
@RestController
@RequestMapping("/schiffeversenken")
public class ShipWreckController {
    ShipWreckService wreckService = new ShipWreckService();

    @RequestMapping(value = "/raten", method = RequestMethod.GET)
    public String shot(@RequestParam(value = "tipp", defaultValue = "X9") String tipp) {
        String output = printHead();
        try {
            int intTipp = Integer.parseInt(tipp);
            wreckService.run(intTipp);
        } catch (Exception ex) {
            wreckService.run(tipp);
        }

        output += "<body>" + showGamefield();

        return output + "<div>" + wreckService.getGameHistory() + "</div></body>";


    }

    @RequestMapping(value = "/neustart", method = RequestMethod.GET)
    public String restart() {
        wreckService = new ShipWreckService();
        return showGamefield();
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public String showResult() {

        String output = printHead();

        output += "<h3>Welche Schiffe schwimmen noch?</h3>";

        output = printShips(output);

        output = printWracks(output);

        output += "</body>";
        return output;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showGamefield() {
        String output = printHead();

        char[] gameFieldSetup = wreckService.getGameField().getSetup();

        output += "<body><ul><h3>Spielbrett:</h3>" + printAlphabetList() + printNumberList();

        output = printGameField(output, gameFieldSetup);

        return output;
    }

    @RequestMapping(value = "/style", method = RequestMethod.GET)
    public String getStylesheet() {
        String stylesheet =
                ".wave{color: #00D8FF;}.critical{color: #EB3900}.success{color: #1ca507;}\n" +
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
                        "a.restart:hover{color:27F213}";
        return stylesheet;
    }


    private String printWracks(String text) {
        for (int i = 0; i < wreckService.getShipWrecks().size(); i++) {
            text += "<p class= \"critical\">Die " + wreckService.shipWrecks.get(i) + " wurde bereits versenkt.</p>";
        }
        return text;
    }

    private String printShips(String text) {
        for (int i = 0; i < wreckService.getShipList().size(); i++) {
            String name = selectedShip(i).getName();
            String hitCount = "" + selectedShip(i).getHit();
            text += "<p class= \"wave\">Die " + name + " schwimmt noch und wurde " + hitCount + "-mal getroffen.</p>";
        }
        return text;
    }

    private Ship selectedShip(int i) {
        return wreckService.getShipList().get(i);
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

    public String printGameField(String output, char[] gameFieldSetup) {
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


        output += "</div>";
        return output;
    }

    public String letterChooser(String output, char c, int i) {
        if (c == 'x') {
            output += "<span class=\"critical spot\">" + c + "</span>";
        } else if (c == '~') {
            output += "<a class=\"spot\"href=\"http://localhost:8090/schiffeversenken/raten?tipp=" + i + "\">" + c + "</a>";
        } else {
            output += "<span class=\"spot\">" + c + "</span>";
        }
        return output;
    }

    public String printHead() {
        return "<head><link rel=\"stylesheet\" href=\"http://localhost:8090/schiffeversenken/style\" type=\"text/css\"></head>";
    }
}
