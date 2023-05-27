package org.pseudonymcode.naturalis;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class UIHandler {
    private static final int DEBUG_UI_SPACING = 20;
    private static final int DEBUG_TABLE_WIDTH = 50;


    private Stage stage;
    private Skin skin;

    private Table debugTable;
    private Label debugLabel;
    private Label positionX;
    private Label positionY;
    private Label velocityX;
    private Label velocityY;

    public UIHandler() {
        int gameHeight = Gdx.graphics.getHeight();

        stage = new Stage(new ScreenViewport());

        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        debugTable = new Table();
        debugTable.setWidth(DEBUG_TABLE_WIDTH);
        debugTable.align(Align.topLeft);
        debugTable.setPosition(0, gameHeight);

        debugLabel = new Label("[Debug]", skin);
        positionX = new Label("positionX", skin);
        positionY = new Label("positionY", skin);
        velocityX = new Label("velocityX", skin);
        velocityY = new Label("velocityY", skin);

        debugTable.add(debugLabel).left();
        debugTable.row();
        debugTable.add(positionX).left();
        debugTable.row();
        debugTable.add(positionY).left();
        debugTable.row();
        debugTable.add(velocityX).left();
        debugTable.row();
        debugTable.add(velocityY).left();

        stage.addActor(debugTable);
    }

    public void draw(float deltaTime) {
        Player p = Game.getPlayer();
        Sprite s = p.getSprite();
        positionX.setText("position.x: " + String.valueOf(s.getX()));
        positionY.setText("position.y: " + String.valueOf(s.getY()));
        velocityX.setText("velocity.x: " + String.valueOf(p.getVelocity().x));
        velocityY.setText("velocity.y: " + String.valueOf(p.getVelocity().y));

        stage.act(deltaTime);
        stage.draw();
    }

    public Stage getStage() { return stage; }
}
