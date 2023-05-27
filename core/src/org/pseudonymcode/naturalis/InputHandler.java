package org.pseudonymcode.naturalis;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.strongjoshua.console.GUIConsole;

public class InputHandler implements InputProcessor {

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.Q) {
            Player p = Game.getPlayer();
            if (p.getVelocity().x == 0 && p.getVelocity().y == 0) {
                p.setMovementMode(p.getMovementMode() == Player.MovementMode.BOOSTERS ? Player.MovementMode.THRUSTERS : Player.MovementMode.BOOSTERS);
            }
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
        return false;
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
