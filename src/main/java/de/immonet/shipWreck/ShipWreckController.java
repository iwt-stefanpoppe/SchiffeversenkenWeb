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

    @RequestMapping(value = "/raten", method = RequestMethod.PUT)
    public String shot(@RequestParam(value = "tipp", defaultValue = "X9") String tipp) {

        String output="<head><link rel=\"stylesheet\" href=\"http://localhost:8090/schiffeversenken/style\" type=\"text/css\"></head>";

        wreckService.run(tipp);

         output += "<body><h3> Was hat ihr Schuss bewirkt? </h3> ";

        return output + "<div>" + wreckService.getGameHistory() + "</div></body>";
    }

    @RequestMapping(method = RequestMethod.POST)
    public void restart() {
        wreckService = new ShipWreckService();
    }

    @RequestMapping(method = RequestMethod.GET)
    public String Zwischenstand(){

        String output="<head><link rel=\"stylesheet\" href=\"http://localhost:8090/schiffeversenken/style\" type=\"text/css\"></head>";

        output += "<h3>Welche Schiffe schwimmen noch?</h3>";

        output = printShips(output);

        output = printWracks(output);

        output += "</body>";
        return output;
    }

    @RequestMapping(value = "/spielbrett", method = RequestMethod.GET)
    public String showGamefield(){
        String output="<head><link rel=\"stylesheet\" href=\"http://localhost:8090/schiffeversenken/style\" type=\"text/css\"></head><p><tab indent=20>0123456<br>";

        char[] gameFieldSetup = wreckService.getGameField().getSetup();

        String alphabet="ABCDEFG";
        int j=0;
        for (int i = 0; i < 49; i++) {
            if(i%7 == 0){
                output += alphabet.charAt(j++) ;
            }
            output += gameFieldSetup[i] == '~'? "<span class=\"wave\">~</span>" : "<span class=\"critical\">" + gameFieldSetup[i]+"</span>" ;
            if (i % 7 == 6) {
                output += "<br>";
            }
        }
        return output;
    }

    @RequestMapping(value = "/style",method = RequestMethod.GET)
    public String getStylesheet() {
        String stylesheet =
                ".wave{color: #00D8FF;}.critical{color: #EB3900}.success{color: #1ca507;";
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

}
