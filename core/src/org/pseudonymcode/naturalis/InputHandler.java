package org.pseudonymcode.naturalis;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;
import com.strongjoshua.console.GUIConsole;
import org.pseudonymcode.naturalis.items.Item;
import org.pseudonymcode.naturalis.items.ItemStack;

public class InputHandler implements InputProcessor {

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.Q) {
            Game.getPlayer().getBodyHandler().toggleMovementMode();
            return true;
        }
        else if (keycode == Input.Keys.GRAVE) {
            GUIConsole console = Game.getConsole();
            if (console.isVisible()) {
                console.setVisible(false);
                console.deselect();
            }
            else {
                console.setVisible(true);
                console.select();
            }
        }
        else if (keycode == Input.Keys.ESCAPE) {
            Gdx.app.exit();
        }
        else if (keycode == Input.Keys.E) {
            UIHandler uiHandler = Game.getUiHandler();
            if (uiHandler.isInventoryOpen()) {
                uiHandler.closeOpenInventory();
            }
            else {
                uiHandler.setOpenInventory(Game.getPlayer().generateInventory());
            }
        }
        else if (keycode == Input.Keys.R) { // debug lol
            Game.getPlayer().insertIntoStorage(new ItemStack(Item.getItem("debugItem1"), 1), 0);
            Game.getPlayer().insertIntoStorage(new ItemStack(Item.getItem("debugItem2"), 202), 0);
            Game.getPlayer().insertIntoStorage(new ItemStack(Item.getItem("debugItem3"), 1), 0);
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 worldPos = Game.getCamera().unproject(new Vector3(screenX, screenY, 0));
        Game.getEntityHandler().handleClick(worldPos.x, worldPos.y, button);

        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
