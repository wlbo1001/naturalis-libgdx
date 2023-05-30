package org.pseudonymcode.naturalis;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.pseudonymcode.naturalis.player.BodyHandler;
import org.pseudonymcode.naturalis.player.Player;

public class UIHandler {
    private static final int DEBUG_TABLE_WIDTH = 50;
    private static final int VITALS_TABLE_WIDTH = 250;
    private static final int VITALS_TABLE_HEIGHT = 130;
    private static final String UI_SKIN_PATH = "ui/tracer-ui.json";


    private Stage stage;
    private Skin skin;

    private Table debugTable;
    private Label debugLabel;
    private Label fps;
    private Label positionX;
    private Label positionY;
    private Label velocityX;
    private Label velocityY;

    private Table vitalsTable;
    private Label vitalsLabel;
    private Label health;
    private Label fuel;
    private Label battery;

    private Inventory openInventory;

    public UIHandler() {
        int gameHeight = Gdx.graphics.getHeight();
        int gameWidth = Gdx.graphics.getWidth();

        stage = new Stage(new ScreenViewport());

        skin = new Skin(Gdx.files.internal(UI_SKIN_PATH));

        // DEBUG STATISTICS
        debugTable = new Table();
        debugTable.setWidth(DEBUG_TABLE_WIDTH);
        debugTable.align(Align.topLeft);
        debugTable.setPosition(0, gameHeight);

        debugLabel = new Label("[Debug]", skin);
        fps = new Label("fps", skin);
        positionX = new Label("positionX", skin);
        positionY = new Label("positionY", skin);
        velocityX = new Label("velocityX", skin);
        velocityY = new Label("velocityY", skin);

        debugTable.add(debugLabel).left();
        debugTable.row();
        debugTable.add(fps).left();
        debugTable.row();
        debugTable.add(positionX).left();
        debugTable.row();
        debugTable.add(positionY).left();
        debugTable.row();
        debugTable.add(velocityX).left();
        debugTable.row();
        debugTable.add(velocityY).left();

        // PLAYER VITALS STATISTICS
        vitalsTable = new Table();
        vitalsTable.setWidth(VITALS_TABLE_WIDTH);
        vitalsTable.align(Align.topLeft);
        vitalsTable.setPosition(gameWidth - VITALS_TABLE_WIDTH, VITALS_TABLE_HEIGHT);

        vitalsLabel = new Label("[Vitals]", skin);
        health = new Label("health", skin);
        fuel = new Label("fuel", skin);
        battery = new Label("energy", skin);

        vitalsTable.add(vitalsLabel).left();
        vitalsTable.row();
        vitalsTable.add(health).left();
        vitalsTable.row();
        vitalsTable.add(fuel).left();
        vitalsTable.row();
        vitalsTable.add(battery).left();

        stage.addActor(debugTable);
        stage.addActor(vitalsTable);
//        stage.setDebugAll(true);
    }

    public void draw(float deltaTime) {
        Player p = Game.getPlayer();
        BodyHandler bh = p.getBodyHandler();
        fps.setText("FPS: " + Gdx.graphics.getFramesPerSecond());
        positionX.setText("position.x: " + String.format("%.2f", bh.getPosition().x));
        positionY.setText("position.y: " + String.format("%.2f", bh.getPosition().y));
        velocityX.setText("velocity.x: " + String.format("%.2f", bh.getVelocity().x));
        velocityY.setText("velocity.y: " + String.format("%.2f", bh.getVelocity().y));

        health.setText("Health: " + String.format("%.2f", p.getHealth()));
        fuel.setText("Fuel: " + String.format("%.2f", p.getFuel()));
        battery.setText("Energy: " + String.format("%.2f", p.getBattery()));

        // Update current inventory
        if (openInventory != null) {
            openInventory.update();
        }

        stage.act(deltaTime);
        stage.draw();
    }

    public Stage getStage() { return stage; }
    public boolean isInventoryOpen() { return openInventory != null; }
    public Skin getSkin() { return skin; }

    public void setOpenInventory(Inventory inventory) {
        stage.addActor(inventory.getImageTable());
        stage.addActor(inventory.getButtonTable());
        openInventory = inventory;
    }
    public void closeOpenInventory() {
        openInventory.getButtonTable().remove();
        openInventory.getImageTable().remove();
        openInventory = null;
    }
}
