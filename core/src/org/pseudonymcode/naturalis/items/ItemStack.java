package org.pseudonymcode.naturalis.items;

public class ItemStack {
    public Item item;
    public int count;

    public ItemStack(Item newItem, int newCount) {
        item = newItem;
        count = newCount;
    }
}
