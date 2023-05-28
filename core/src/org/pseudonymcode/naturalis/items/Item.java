package org.pseudonymcode.naturalis.items;

import java.util.HashMap;
import java.util.List;

public class Item {
    public String name; // textures for items are the path "items/{name}"
    public String description;
    public float mass;
    public boolean isStackable;
    public List<ItemHandler.ItemProperties> properties; // should be null unless item has known properties like
    public HashMap<ItemHandler.ItemData, Object> data; // should always be null unless item has active stats like a durability, energy level, etc.

    public Item(String iName, String iDescription, float iMass, boolean iIsStackable, List<ItemHandler.ItemProperties> iProperties, HashMap<ItemHandler.ItemData, Object> iData) {
        name = iName;
        description = iDescription;
        mass = iMass;
        isStackable = iIsStackable;
        properties = iProperties;
        data = iData;
    }
}
