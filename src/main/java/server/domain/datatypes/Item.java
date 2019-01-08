package server.domain.datatypes;

import java.util.Arrays;

public enum Item {
    BIER, BREEZEL, COLA, FANTA, SPRITE, WASSER;


    public static boolean isValid(String itemName) {
        if( Arrays.asList(Item.values()).toString().contains(itemName))
            return true;

        return false;
    }
}
