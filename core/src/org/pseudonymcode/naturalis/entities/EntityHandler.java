package org.pseudonymcode.naturalis.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;
import org.pseudonymcode.naturalis.Game;
import org.pseudonymcode.naturalis.entities.entity.Asteroid;

import java.util.ArrayList;
import java.util.List;

public class EntityHandler {
    private List<Entity> entities;
    public EntityHandler() {
        entities = new ArrayList<>();
    }

    // Runs all entity updates, checks, spawn checks, etc. (should be called in Game's render function)
    public void update(SpriteBatch batch, float deltaTime, float animationElapsedTime) {
        // Run spawning checks
        Asteroid.doConditionalSpawns(deltaTime);

        for (Entity e : entities) {
            e.update(deltaTime);
            if (e.hasCollisionCheck()) e.handleCollisions(entities); // can check for player collision thyself (Game.getPlayer().getBodyHandler().getBoundingRectangle())
            e.draw(batch, animationElapsedTime);
        }
    }

    public void registerEntity(Entity entity) {
        entities.add(entity);
    }

    public void handleClick(float worldX, float worldY, int button) {
        long currentUnixTime = TimeUtils.millis();
        for (Entity e : entities) {
            if (e.handleClick(worldX, worldY, button, currentUnixTime)) return;
        }
    }
}
