package org.pseudonymcode.naturalis.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import org.pseudonymcode.naturalis.Game;
import org.pseudonymcode.naturalis.MutableSprite;

public abstract class Entity extends MutableSprite {
    public String entityType;
    public long lastClickTime; // in seconds
    public long clickTimeWait; // in seconds

    public Entity(String namedEntityType, Vector2 bodySize, Vector2 bodyPosition, boolean hasCollisionCheck, long clickDelayTime) {
        super(bodySize, bodyPosition, hasCollisionCheck);
        entityType = namedEntityType;
        clickTimeWait = clickDelayTime;
        lastClickTime = 0;
    }

    // returns true if click has been handled. Return false to keep checking entities to see if any have been clicked on
    public boolean handleClick(float worldX, float worldY, int button, long currentUnixTime) {
        if (currentUnixTime >= lastClickTime + clickTimeWait && this.getBoundingRectangle().contains(worldX, worldY)) {
            lastClickTime = currentUnixTime;
            return true;
        }
        return false;
    }

    public abstract void update(float deltaTime); // When called, the entity should update anything it needs to with respect to delta time
}
