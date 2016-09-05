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
    ShipWreckService wreckService =new ShipWreckService();

    @RequestMapping(value = "/raten",method = RequestMethod.PUT)
    public String Schuss(@RequestParam(value="tipp", defaultValue="X9") String tipp){
        wreckService.run(tipp);
        String text="<div> <h3> Was hat ihr Schuss bewirkt? </h3> </div>";

        return text + "<div>" + wreckService.getGameHistory() + "</div>";
    }

    @RequestMapping(method = RequestMethod.POST)
    public void restart(){
        wreckService = new ShipWreckService();
    }

    @RequestMapping(method = RequestMethod.GET)
    public String Zwischenstand(){
        String text = "<h3>Welche Schiffe schwimmen noch?</h3>";

        text = printShips(text);

        text = printWracks(text);

        text+="</body>";
        return text;
    }

    private String printWracks(String text) {
        for (int i = 0 ; i<wreckService.getShipWrecks().size() ; i++){
            text += "<div><font color=#EB3900>Die " + wreckService.shipWrecks.get(i) + " wurde bereits versenkt.</font><div>";
        }
        return text;
    }

    private String printShips(String text) {
        for (int i = 0; i<wreckService.getShipList().size();i++){
            boolean floating= selectedShip(i).isFloating();
            String name = selectedShip(i).getName();
            String hitCount = "" + selectedShip(i).getHit();
            text+= "<div><font color=#00D8FF>Die " + name + " schwimmt noch und wurde " + hitCount +"-mal getroffen.</font></div>";
        }
        return text;
    }

    private Ship selectedShip(int i) {
        return wreckService.getShipList().get(i);
    }

}
