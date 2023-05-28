package org.pseudonymcode.naturalis;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

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
        assetManager.load("player/default.png", Texture.class);
        assetManager.load("player/player.png", Texture.class);
        assetManager.load("backgrounds/default.jpg", Texture.class);
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

    public AssetManager getAssetManager() { return assetManager; }
    public boolean isFinishedLoading() { return finishedLoading; }
}
