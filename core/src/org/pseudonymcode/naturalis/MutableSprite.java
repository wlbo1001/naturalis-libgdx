package org.pseudonymcode.naturalis;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class MutableSprite {
    Animation<TextureRegion> animation;
    Texture still;
    Vector2 size;
    Vector2 position;
    Rectangle boundingRectangle;

    // To draw this object, must call setAnimation() or setStill() after creating it!
    public MutableSprite(Vector2 spriteSize, Vector2 spritePosition) {
        size = spriteSize;
        position = spritePosition;
        boundingRectangle = new Rectangle();
        boundingRectangle.setPosition(position.x - size.x/2f, position.y - size.y/2f);
        boundingRectangle.setSize(size.x, size.y);
    }

    public void setAnimation(String animationPath, int frameSize, float frameDuration) {
        TextureRegion[][] frames = TextureRegion.split(Game.getAssetHandler().getAssetManager().get(animationPath, Texture.class), frameSize, frameSize);
        int index = 0;
        TextureRegion[] formattedFrames = new TextureRegion[frames.length*frames[0].length];
        for (int i = 0; i < frames.length; i++) {
            for (int j = 0; j < frames[0].length; j++) {
                formattedFrames[index++] = frames[j][i];
            }
        }
        animation = new Animation<TextureRegion>(frameDuration, formattedFrames);
    }
    public void clearAnimation() { animation = null; }

    public void setStill(String spriteStillPath) { still = Game.getAssetHandler().getAssetManager().get(spriteStillPath, Texture.class); }
    public void clearStill() { still = null; }

    public void draw(SpriteBatch batch, float animationElapsedTime) {
        // images should be drawn offset by half the size of this object (the center of the image is considered the logical position of the sprite)
        float drawX = position.x - (size.x/2f);
        float drawY = position.y - (size.y/2f);

        if (animation != null) {
            batch.draw(animation.getKeyFrame(animationElapsedTime, true), drawX, drawY, size.x, size.y);
        }
        else if (still != null) {
            batch.draw(still, drawX, drawY, size.x, size.y);
        }
        else throw new RuntimeException("Attempted to draw a sprite that has neither animation keyframes nor a still image.");
    }

    public Vector2 getSize() { return this.size; }
    public Vector2 getPosition() { return this.position; }
    public Rectangle getBoundingRectangle() { return this.boundingRectangle; }
    public void setSize(Vector2 size) {
        this.size = size;
        boundingRectangle.setSize(size.x, size.y);
    }
    public void setPosition(Vector2 position) {
        this.position = position;
        boundingRectangle.setPosition(position);
    }
    public void setX(float x) {
        this.position.x = x;
        boundingRectangle.setX(x);
    }
    public void setY(float y) {
        this.position.y = y;
        boundingRectangle.setY(y);
    }
}
