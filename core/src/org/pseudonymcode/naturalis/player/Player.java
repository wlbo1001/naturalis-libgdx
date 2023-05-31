package org.pseudonymcode.naturalis.player;

import org.pseudonymcode.naturalis.Inventory;
import org.pseudonymcode.naturalis.items.ItemStack;
import org.pseudonymcode.naturalis.Inventory.SlotType;
import org.pseudonymcode.naturalis.Inventory.InventoryOwner;

import java.util.ArrayList;
import java.util.List;

public class Player implements InventoryOwner {
    private static final int MAX_HEALTH = 100;
    private static final int MAX_FUEL = 100;
    private static final int MAX_BATTERY = 100;
    private static final int FUEL_BURN = 1;
    private static final int BATTERY_DRAIN = 1;

    private static final int MAX_STORAGE_STACKS = 20; // # of unique items player storage supports (probably doesn't change much)
    private static final int MAX_STORAGE_MASS = 50; // max mass player storage supports (likely upgradable)


    private BodyHandler bodyHandler;
    private List<ItemStack> storage;
    private boolean storageChanged; // set to true when any changes are made to storage

    // Player vitals
    private float health;
    private float fuel;
    private float battery;

    public Player() {
        bodyHandler = new BodyHandler();
        health = MAX_HEALTH;
        fuel = MAX_FUEL;
        battery = MAX_BATTERY;

        storage = new ArrayList<>();
        for (int i = 0; i < MAX_STORAGE_STACKS; i++) storage.add(null); // make list MAX_STORAGE_STACKS in size but with no items yet
        storageChanged = false;
    }

    public BodyHandler getBodyHandler() { return bodyHandler; }

    public float getHealth() { return health; }
    public float getFuel() { return fuel; }
    public float getBattery() { return battery; }
    public List<ItemStack> getStorage() { return storage; }

    // Generate player's inventory (including any alterations, the below is just the default for now)
    public Inventory generateInventory() {
        List<SlotType> slots = new ArrayList<>();
        for (int i = 0; i < MAX_STORAGE_STACKS; i++) {
            slots.add(SlotType.PlayerStorage);
        }
        return new Inventory(slots, 5, 4, "playerInventoryDefault", this);
    }

    // The player's inventory is just the player's items (for now?)
    public void updateInventory(Inventory inventory) {
        inventory.setPlayerStorageSlots();
    }

    // Implementation of what the player should do when the player clicks on a slot in its defined inventory
    public void onInventorySlotClick(Inventory source, Inventory.Slot slot, int position, int mouseButtonUsed) {
        ItemStack stack = storage.get(position);
        storage.set(position, source.doDefaultSlotClick(stack, mouseButtonUsed));
        storageChanged = true;
    }

    public boolean insertIntoStorage(ItemStack itemStack, int inputNumber) {
        int firstEmptyPos = -1;
        for (int i = 0; i < storage.size(); i++) {
            ItemStack currStack = storage.get(i);
            if (ItemStack.areStacksCombinable(currStack, itemStack)) {
                storage.set(i, ItemStack.combineStacks(currStack, itemStack));
                storageChanged = true;
                return true;
            }
            if (storage.get(i) == null && firstEmptyPos == -1) firstEmptyPos = i;
        }

        // Add item if there is an open slot, else return false as there is no room
        if (firstEmptyPos != -1) {
            storage.set(firstEmptyPos, itemStack);
            storageChanged = true;
            return true;
        }
        return false;
    }

    // return true, as InventoryOwner states, means the ItemStack was successfully removed from this object's storage
    public boolean outputFromStorage(ItemStack itemStack, int outputNumber) {
        for (int i = 0; i < storage.size(); i++) {
            if (storage.get(i).item.name.equals(itemStack.item.name)) {
                storage.set(i, null);
                return true;
            }
        }
        return false;
    }

    public void update(float deltaTime) {
        // Movement
        bodyHandler.stepPhysics(deltaTime, fuel, battery);

        // Vitals
        if (!bodyHandler.getVelocity().isZero()) {
            fuel -= FUEL_BURN * deltaTime;
            if (fuel < 0) fuel = 0;
        }
        battery -= BATTERY_DRAIN * deltaTime;
        if (battery < 0) battery = 0;

        // Check for collisions
//        if (Game.getMap().checkCollision(this.sprite)) {
//            // Undo move!
//            position.x -= vel.x;
//            position.y -= vel.y;
//            sprite.setPosition(position.x, position.y);
//        }
    }
}
