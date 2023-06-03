package org.pseudonymcode.naturalis;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class AssetHandler {
    private AssetManager assetManager;
    private boolean finishedLoading;
    private BitmapFont font;

    public AssetHandler() {
        assetManager = new AssetManager();
        finishedLoading = false;
        font = new BitmapFont();
    }

    // Queues all assets for the entire game for loading
    public void queueAssets() {
        // Player assets
        assetManager.load("player/default.png", Texture.class);

        // Background Assets
        assetManager.load("backgrounds/default.jpg", Texture.class);

        // UI Assets
        assetManager.load("ui/backgrounds/playerInventoryDefault.png", Texture.class);

        // Item Assets
        assetManager.load("items/null.png", Texture.class);
        assetManager.load("items/debugItem1.png", Texture.class);
        assetManager.load("items/debugItem2.png", Texture.class);
        assetManager.load("items/debugItem3.png", Texture.class);

        // Entity Assets
        assetManager.load("entities/asteroids/circle1.png", Texture.class);
        assetManager.load("animationTest.png", Texture.class);
    }

    // Loads assets asynchronously. Called over and over in the render function, game shouldn't try to load anything until this returns true.
    public void loadAssets(SpriteBatch batch) {
        assetManager.update();
        font.draw(batch, String.valueOf(assetManager.getProgress()), 100, 100);
        if (assetManager.isFinished()) finishedLoading = true;
    }

    // Loads all assets *synchronously*
    public void loadAllAssets() {
        assetManager.finishLoading();
    }

    public Drawable getDrawableFromTexturePath(String path) {
        return new TextureRegionDrawable(assetManager.get(path, Texture.class));
    }

    public AssetManager getAssetManager() { return assetManager; }
    public boolean isFinishedLoading() { return finishedLoading; }
}
