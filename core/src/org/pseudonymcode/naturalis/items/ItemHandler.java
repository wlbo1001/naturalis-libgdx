package org.pseudonymcode.naturalis.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemHandler {
    public enum ItemProperties {

    }
    public enum ItemData {

    }

    public HashMap<String, Item> items;

    public ItemHandler() {
        items = new HashMap<>();

        // Should load these from a CSV/JSON file, but they're created right here in code for now!
        items.put("rocks", new Item("rocks", "Rocks\nThis item doesn't do anything right now!", 5, true, null, null));
        items.put("coal", new Item("coal", "Coal\nThis item is a test :)", 2, true, null, null));
    }

    public Item getItem(String name) {
        Item item = items.get(name);
        if (item == null) {
            throw new RuntimeException("Item not found: Item \"" + name + "\" does not exist.");
        }
        return items.get(name);
    }
    public List<Item> getItemsAsList() {
        List<Item> itemsList = new ArrayList<>();
        for (Map.Entry<String, Item> item : items.entrySet()) {
            itemsList.add(item.getValue());
        }
        return itemsList;
    }

    // Helper functions
    public static boolean areStacksCombinable(ItemStack stack1, ItemStack stack2) {
        if (stack1 == null || stack2 == null) return false;
        return stack1.item.isStackable && stack1.item.name.equals(stack2.item.name);
    }
    public static ItemStack combineStacks(ItemStack stack1, ItemStack stack2) {
        if (areStacksCombinable(stack1, stack2)) {
            stack1.count += stack2.count;
            return stack1;
        }
        else return null;
    }
}
