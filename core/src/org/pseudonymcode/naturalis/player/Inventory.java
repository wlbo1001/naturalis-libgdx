package org.pseudonymcode.naturalis.player;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.LinkedHashMap;
import java.util.List;

public class Inventory {
    public enum SlotType {
        PlayerStorage, // slot should be populated from the player's storage (need
        Selectable,
        Display,
        Movable
    }

    private Table table;

    public Inventory(LinkedHashMap<SlotType, Object> slots, String background) {

    }

    public Table getTable() {
        return table;
    }
}
