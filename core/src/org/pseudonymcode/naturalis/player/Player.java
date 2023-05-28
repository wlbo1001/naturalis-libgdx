package org.pseudonymcode.naturalis.player;

public class Player {
    private static final int MAX_HEALTH = 100;
    private static final int MAX_FUEL = 100;
    private static final int MAX_BATTERY = 100;
    private static final int FUEL_BURN = 1;
    private static final int BATTERY_DRAIN = 1;

    private BodyHandler bodyHandler;
    private boolean focusOnGame = true;

    // Player vitals
    private float health;
    private float fuel;
    private float battery;

    public Player() {
        bodyHandler = new BodyHandler();
        health = MAX_HEALTH;
        fuel = MAX_FUEL;
        battery = MAX_BATTERY;
    }

    public BodyHandler getBodyHandler() { return bodyHandler; }
    public boolean isFocusOnGame() { return focusOnGame; }

    public float getHealth() { return health; }
    public float getFuel() { return fuel; }
    public float getBattery() { return battery; }

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
