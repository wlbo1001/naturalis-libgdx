package org.pseudonymcode.naturalis.entities.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import org.pseudonymcode.naturalis.Game;
import org.pseudonymcode.naturalis.entities.Entity;
import org.pseudonymcode.naturalis.items.Item;
import org.pseudonymcode.naturalis.items.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class Asteroid extends Entity {
    private static final int SPAWN_SQUARE_SIZE = 1000; // bounding box length/width around the player that asteroids could spawn

    private Sprite sprite;
    private long lastClickTime = 0;
    private long clickTimeWait = 3000;

    // Handles the spawning of new asteroids naturally
    public static void doConditionalSpawns(float deltaTime) {
        if (Math.random()*10000*deltaTime < 1) {
            int xModifier = (int)(Math.random()*2) == 0 ? -1 : 1;
            int yModifier = (int)(Math.random()*2) == 0 ? -1 : 1;
            float x = (float)Math.random()*SPAWN_SQUARE_SIZE;
            float y = (float)Math.random()*SPAWN_SQUARE_SIZE;
            Vector2 pPos = Game.getPlayer().getBodyHandler().getPosition();
            Game.getEntityHandler().registerEntity(new Asteroid(new Vector2(300, 300), new Vector2(pPos.x + x*xModifier, pPos.y + y*yModifier)));
        }
    }

    private static final List<String> possibleTextures = new ArrayList<>(Arrays.asList("entities/asteroids/animationTest.png"));

    public Asteroid(Vector2 bodySize, Vector2 bodyPosition) {
        super("asteroid", bodySize, bodyPosition, true, 3000);
        this.setAnimation(possibleTextures.get((int)(Math.random()*possibleTextures.size())), 16, 16, 1);
    }

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public boolean handleClick(float mouseX, float mouseY, int button, long currentUnixTime) {
        if (button == Input.Buttons.LEFT && super.handleClick(mouseX, mouseY, button, currentUnixTime)) {
            Game.getPlayer().insertIntoStorage(new ItemStack(Item.getItem("debugItem1"), 1), 0);
            return true;
        }
        return false;
    }

    @Override
    public void handleCollisions(List<Entity> entities) {
        if (Game.getPlayer().getBodyHandler().getBoundingRectangle().overlaps(this.getBoundingRectangle())) Game.getPlayer().insertIntoStorage(new ItemStack(Item.getItem("debugItem2"), 3), 0);
    }
}
