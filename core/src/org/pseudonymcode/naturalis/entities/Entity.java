package org.pseudonymcode.naturalis.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import org.pseudonymcode.naturalis.Game;

public abstract class Entity {
    public Sprite sprite;
    public float bodySize;
    public float halfBodySize;

    public long lastClickTime; // in seconds
    public long clickTimeWait; // in seconds

    public Entity(String texturePath, float bodySize, float xPos, float yPos, long clickDelayTime) {
        sprite = new Sprite(Game.getAssetHandler().getAssetManager().get(texturePath, Texture.class));
        this.bodySize = bodySize;
        this.halfBodySize = bodySize/2f;
        sprite.setSize(bodySize, bodySize);
        sprite.setOrigin(halfBodySize, halfBodySize);
        sprite.setPosition(xPos - halfBodySize, yPos - halfBodySize); // offset centers sprite body (all objects' centers are in the middle of their sprite, not the bottom left)
        clickTimeWait = clickDelayTime;
        lastClickTime = 0;
    }

    public Vector2 getPosition() {
        return new Vector2(sprite.getX() + halfBodySize, sprite.getY() + halfBodySize);
    }

    public void setPosition(int x, int y) {
        sprite.setPosition(x + halfBodySize, y + halfBodySize);
    }

    public void draw(SpriteBatch batch) {
        this.sprite.draw(batch);
    }

    // returns true if click has been handled. Return false to keep checking entities to see if any have been clicked on
    public boolean handleClick(float worldX, float worldY, int button, long currentUnixTime) {
        if (currentUnixTime >= lastClickTime + clickTimeWait && this.sprite.getBoundingRectangle().contains(worldX, worldY)) {
            lastClickTime = currentUnixTime;
            return true;
        }
        return false;
    }

    public abstract void update(float deltaTime); // When called, the entity should update anything it needs to with respect to delta time
}
