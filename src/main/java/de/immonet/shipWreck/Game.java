package de.immonet.shipWreck;

import java.util.ArrayList;

/**
 * Created by spoppe on 09.09.2016.
 */
public class Game {

    ArrayList<Player> playerList = new ArrayList<>();


    public Game() {
        Player user = new HumanPlayer();
        Player com = new ComputerPlayer();
        playerList.add(user);
        playerList.add(com);
    }

    public ArrayList<Player> getPlayerList() {
        return playerList;
    }

    public void setCom(ComputerPlayer com){
        playerList.set(1,com);
    }

}
