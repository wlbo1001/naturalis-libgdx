package org.pseudonymcode.naturalis;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.strongjoshua.console.GUIConsole;
import org.pseudonymcode.naturalis.items.ItemHandler;
import org.pseudonymcode.naturalis.player.Player;

import java.util.ArrayList;
import java.util.List;


public class Game extends ApplicationAdapter {
    public static final int CAMERA_SCALE = 2; // pixels are 4x larger in width and height (this times the tile size converts actual pixels to game pixel positions)

    public static AssetHandler assetHandler;

    private static OrthographicCamera camera;
    private static SpriteBatch batch;
    private static UIHandler uiHandler;
    private static ItemHandler itemHandler;
    private static GUIConsole console;
    private static Player player;
    private static Texture background;
    private static List<Sprite> objs = new ArrayList<>();

    @Override
    public void create () {
        // Enter fullscreen mode immediately
        Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());

        // Setup sprite batch renderer
        batch = new SpriteBatch();

        // Load Assets
        assetHandler = new AssetHandler();
        assetHandler.queueAssets();
        assetHandler.loadAllAssets();

        // Load Items
        itemHandler = new ItemHandler();

        // Create debug console
        console = new GUIConsole(false);
        console.setSizePercent(50, 50);
        console.enableSubmitButton(true);

        // Replace cursor
        Gdx.graphics.setCursor(Gdx.graphics.newCursor(new Pixmap(Gdx.files.internal("cursors/default.png")), 0, 0));
        
        // Create UI object
        uiHandler = new UIHandler();

        // Create background object
        background = assetHandler.getAssetManager().get("backgrounds/default.jpg", Texture.class);
        background.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        // Test Objects
        for (int i = 0; i < 1000; i++) {
            Sprite sprite = new Sprite(assetHandler.getAssetManager().get("player/player.png", Texture.class));
            sprite.setPosition((float)Math.random()*1000, (float)Math.random()*1000);
            objs.add(sprite);
        }

        // Create player object
        player = new Player();
        player.getBodyHandler().setPosition(0, 0);

        // Create 2D camera
        camera = new OrthographicCamera(Gdx.graphics.getWidth()/Game.CAMERA_SCALE, Gdx.graphics.getHeight()/Game.CAMERA_SCALE);
        camera.translate(camera.viewportWidth/2, camera.viewportHeight/2);

        // Input multiplexer / processor (input comes from both the game and the UI)
        InputHandler inputHandler = new InputHandler();
        InputMultiplexer inputMultiplexer = new InputMultiplexer(inputHandler, uiHandler.getStage(), console.getInputProcessor());
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void render () {
        float dt = Gdx.graphics.getDeltaTime();

        // Update game world
        player.update(dt);

        // Move the camera
        camera.position.x = player.getBodyHandler().getPosition().x;
        camera.position.y = player.getBodyHandler().getPosition().y;
        camera.update();

        // Begin drawing to the screen
        ScreenUtils.clear(1, 1, 1, 1);
        batch.begin();
        batch.setProjectionMatrix(camera.combined);

        // Background
        batch.draw(background, camera.position.x-1500, camera.position.y-1000, 3000f, 2000f);

        // Draw test objects
        for (Sprite obj : objs) {
            batch.draw(obj, obj.getX(), obj.getY());
        }

        // Draw player
        player.getBodyHandler().draw(batch);

        // End drawing the game (so the UI can be drawn overtop)
        batch.end();

        // Draw UI over everything else
        uiHandler.draw(dt);

        // Draw console
        console.draw();
    }

    @Override
    public void dispose () {
        batch.dispose();
    }

    public static OrthographicCamera getCamera() { return camera; }
    public static Player getPlayer() { return player; }
    public static GUIConsole getConsole() { return console; }
    public static AssetHandler getAssetHandler() { return assetHandler; }
    public static UIHandler getUiHandler() { return uiHandler; }
    public static ItemHandler getItemHandler() { return itemHandler; }
}
