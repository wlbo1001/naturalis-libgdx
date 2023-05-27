package org.pseudonymcode.naturalis;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Player {
    public enum MovementMode {
        THRUSTERS, // WASD works like RPG movement
        BOOSTERS // WASD works like "Asteroids" game
    }

    public static float THRUSTERS_ACCELERATION = 1000f;
    public static float THRUSTERS_DECELERATION = 10f;
    public static float BOOSTERS_ACCELERATION = 10f;
    public static float BOOSTERS_DECELERATION = 1f;
    public static float BOOSTERS_TURN = 35;

    private Sprite sprite;

    private Vector2 velocity;
    private boolean focusOnGame = true;
    private MovementMode movementMode = MovementMode.THRUSTERS;

    public Player() {
        sprite = new Sprite(Game.getAssetHandler().getAssetManager().get("player/player.png", Texture.class));
        velocity = new Vector2(0, 0);
    }

    public Sprite getSprite() {
        return sprite;
    }
    public boolean isFocusOnGame() { return focusOnGame; }
    public MovementMode getMovementMode() { return movementMode; }
    public Vector2 getVelocity() { return velocity; }

    public void setMovementMode(MovementMode mode) { movementMode = mode; }

    public void update(float deltaTime) {
        // Movement
        if (movementMode == MovementMode.THRUSTERS) {
            Vector2 vel = new Vector2(0, 0);
            if (Gdx.input.isKeyPressed(Input.Keys.W)) vel.y = 1;
            if (Gdx.input.isKeyPressed(Input.Keys.S)) vel.y = -1;
            if (Gdx.input.isKeyPressed(Input.Keys.A)) vel.x = -1;
            if (Gdx.input.isKeyPressed(Input.Keys.D)) vel.x = 1;
            vel.nor().scl(THRUSTERS_ACCELERATION);
            velocity.x += (vel.x - velocity.x * THRUSTERS_DECELERATION) * deltaTime;
            velocity.y += (vel.y - velocity.y * THRUSTERS_DECELERATION) * deltaTime;
            if (Math.abs(velocity.x) < 1) velocity.x = 0;
            if (Math.abs(velocity.y) < 1) velocity.y = 0;
            this.sprite.setPosition(this.sprite.getX() + (velocity.x * deltaTime), this.sprite.getY() + (velocity.y * deltaTime));
        }
        else if (movementMode == MovementMode.BOOSTERS) {
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                this.sprite.rotate(BOOSTERS_TURN * deltaTime);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                this.sprite.rotate(-BOOSTERS_TURN * deltaTime);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                Vector2 vel = new Vector2((float)Math.cos(Math.toRadians(this.sprite.getRotation())), (float)Math.sin(Math.toRadians((this.sprite.getRotation()))));
                velocity.x += vel.x * BOOSTERS_ACCELERATION * deltaTime;
                velocity.y += vel.y * BOOSTERS_ACCELERATION * deltaTime;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
//                Vector2 vel = new Vector2((float)Math.cos(Math.toRadians(this.sprite.getRotation())), (float)Math.sin(Math.toRadians((this.sprite.getRotation()))));
                Vector2 vel = velocity;
                velocity.x -= vel.x * BOOSTERS_DECELERATION * deltaTime;
                velocity.y -= vel.y * BOOSTERS_DECELERATION * deltaTime;
                if (velocity.x < 1 && velocity.y < 1) {
                    velocity.setZero();
                }
            }
            this.sprite.setPosition(this.sprite.getX() + (velocity.x * deltaTime), this.sprite.getY() + (velocity.y * deltaTime));
        }

        // Check for collisions
//        if (Game.getMap().checkCollision(this.sprite)) {
//            // Undo move!
//            position.x -= vel.x;
//            position.y -= vel.y;
//            sprite.setPosition(position.x, position.y);
//        }
    }
}
