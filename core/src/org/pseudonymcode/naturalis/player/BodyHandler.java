package org.pseudonymcode.naturalis.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import org.pseudonymcode.naturalis.Game;

public class BodyHandler {
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
    private MovementMode movementMode = MovementMode.THRUSTERS;
    private Vector2 velocity;

    public BodyHandler() {
        sprite = new Sprite(Game.getAssetHandler().getAssetManager().get("player/player.png", Texture.class));
        velocity = new Vector2(0, 0);
    }

    public Sprite getSprite() { return sprite; }
    public MovementMode getMovementMode() { return movementMode; }
    public Vector2 getVelocity() { return velocity; }

    public void toggleMovementMode() {
        if (velocity.x == 0 && velocity.y == 0) {
            movementMode = movementMode == MovementMode.BOOSTERS ? MovementMode.THRUSTERS : MovementMode.BOOSTERS;
        }
    }

    public void stepPhysics(float deltaTime) {
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
            sprite.setPosition(sprite.getX() + (velocity.x * deltaTime), sprite.getY() + (velocity.y * deltaTime));
        }
        else if (movementMode == MovementMode.BOOSTERS) {
            if (Gdx.input.isKeyPressed(Input.Keys.A) && velocity.isZero()) {
                sprite.rotate(BOOSTERS_TURN * deltaTime);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D) && velocity.isZero()) {
                sprite.rotate(-BOOSTERS_TURN * deltaTime);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                Vector2 vel = new Vector2((float)Math.cos(Math.toRadians(sprite.getRotation())), (float)Math.sin(Math.toRadians((sprite.getRotation()))));
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
            sprite.setPosition(sprite.getX() + (velocity.x * deltaTime), sprite.getY() + (velocity.y * deltaTime));
        }
    }
}
