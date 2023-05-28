package org.pseudonymcode.naturalis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ItemHandler {
    public enum ItemProperties {

    }
    public enum ItemData {

    }

    public class Item {
        public String name; // textures for items are the path "items/{name}"
        public String description;
        public float mass;
        public List<ItemProperties> properties; // should be null unless item has known properties like
        public HashMap<ItemData, Object> data; // should always be null unless item has active stats like a durability, energy level, etc.

        public Item(String iName, String iDescription, float iMass, List<ItemProperties> iProperties, HashMap<ItemData, Object> iData) {
            name = iName;
            description = iDescription;
            mass = iMass;
            properties = iProperties;
            data = iData;
        }
    }

    public class ItemStack {
        public Item item;
        public int count;

        public ItemStack(Item newItem, int newCount) {
            item = newItem;
            count = newCount;
        }
    }

    public List<Item> items;

    public ItemHandler() {
        items = new ArrayList<>();

        // Should load these from a CSV/JSON file, but they're created right here in code for now!
        items.add(new Item("rocks", "Rocks\nThis item doesn't do anything right now!", 5, null, null));
        items.add(new Item("coal", "Coal\nThis item is a test :)", 2, null, null));
    }
}
