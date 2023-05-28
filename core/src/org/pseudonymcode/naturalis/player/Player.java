package org.pseudonymcode.naturalis.player;

import org.pseudonymcode.naturalis.ItemHandler.ItemStack;
import org.pseudonymcode.naturalis.player.Inventory.SlotType;

import java.util.LinkedHashMap;
import java.util.List;

public class Player {
    private static final int MAX_HEALTH = 100;
    private static final int MAX_FUEL = 100;
    private static final int MAX_BATTERY = 100;
    private static final int FUEL_BURN = 1;
    private static final int BATTERY_DRAIN = 1;

    private static final int MAX_STORAGE_STACKS = 20; // # of unique items player storage supports (probably doesn't change much)
    private static final int MAX_STORAGE_MASS = 50; // max mass player storage supports (likely upgradable)


    private BodyHandler bodyHandler;
    private Inventory openInventory; // if this is not null, an inventory is open! (only one can be open at a time)
    private List<ItemStack> storage;
    private Inventory storageInventory; // defines what the player sees when they open their inventory (could add functions to change this if player unlocks new things)

    // Player vitals
    private float health;
    private float fuel;
    private float battery;

    public Player() {
        bodyHandler = new BodyHandler();
        health = MAX_HEALTH;
        fuel = MAX_FUEL;
        battery = MAX_BATTERY;

        // Create the inventory that appears when the player accesses their storage
        storageInventory = generateStorageInventory();
    }

    public BodyHandler getBodyHandler() { return bodyHandler; }
    public boolean isInventoryOpen() { return openInventory == null; }

    public float getHealth() { return health; }
    public float getFuel() { return fuel; }
    public float getBattery() { return battery; }

    // Generate player's inventory (including any alterations, the below is just the default for now)
    public Inventory generateStorageInventory() {
        LinkedHashMap<SlotType, Object> slots = new LinkedHashMap<>();
        for (int i = 0; i < MAX_STORAGE_STACKS; i++) {
            slots.put(SlotType.PlayerStorage, null);
        }
        return new Inventory(slots, "playerInventoryDefault");
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
