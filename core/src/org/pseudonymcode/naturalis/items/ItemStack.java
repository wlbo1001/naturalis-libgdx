package org.pseudonymcode.naturalis.items;

public class ItemStack {
    public Item item;
    public int count;

    public ItemStack(Item newItem, int newCount) {
        item = newItem;
        count = newCount;
    }

    public static ItemStack createItemStack(Item item, int count) {
        if (count < 1) return null;
        return new ItemStack(item, count);
    }

    // Returns false if items are not the same OR one or both of the stacks are null
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
