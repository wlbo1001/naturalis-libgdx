package org.pseudonymcode.naturalis.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Item {
    public enum ItemProperties {

    }
    public enum ItemData {

    }

    public static HashMap<String, Item> items;

    public static void loadItems() {
        items = new HashMap<>();

        // Should load these from a CSV/JSON file, but they're created right here in code for now!
        items.put("rocks", new Item("rocks", "Rocks\nThis item doesn't do anything right now!", 5, true, null, null));
        items.put("coal", new Item("coal", "Coal\nThis item is a test :)", 2, true, null, null));
        items.put("cheese", new Item("cheese", "Cheese\nThis item will never do anything :D", 1, false, null, null));
    }

    public static Item getItem(String name) {
        Item item = items.get(name);
        if (item == null) {
            throw new RuntimeException("Item not found: Item \"" + name + "\" does not exist.");
        }
        return items.get(name);
    }
    public static List<Item> getItemsAsList() {
        List<Item> itemsList = new ArrayList<>();
        for (Map.Entry<String, Item> item : items.entrySet()) {
            itemsList.add(item.getValue());
        }
        return itemsList;
    }

    public String name; // textures for items are the path "items/{name}"
    public String description;
    public float mass;
    public boolean isStackable;
    public List<ItemProperties> properties; // should be null unless item has known properties like
    public HashMap<ItemData, Object> data; // should always be null unless item has active stats like a durability, energy level, etc.

    public Item(String iName, String iDescription, float iMass, boolean iIsStackable, List<ItemProperties> iProperties, HashMap<ItemData, Object> iData) {
        name = iName;
        description = iDescription;
        mass = iMass;
        isStackable = iIsStackable;
        properties = iProperties;
        data = iData;
    }
}
