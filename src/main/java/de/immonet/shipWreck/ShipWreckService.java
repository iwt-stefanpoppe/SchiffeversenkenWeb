package de.immonet.shipWreck;


import org.springframework.stereotype.Service;

import java.util.ArrayList;

import static de.immonet.shipWreck.ShipWreckController.setUpField;

/**
 * Created by spoppe on 31.08.2016.
 */
@Service
public class ShipWreckService{
    int indexOfCreatedComShips = 0;
    ArrayList<ArrayList<Integer>> shipLocations = new ArrayList<>();
    Game game=new Game();
    GameField userSettingField = new GameField(setUpField());
}

