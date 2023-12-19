package idv.kuan.flashcard.gdx.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class CardTextureUtil {


    public static CardTextureCreator getCardTextureCreator(SpriteBatch batch) {
        return new CardTextureCreator(batch);
    }

    public static class TextureCreatorModel {
        Actor drawTarget;

        //glClearColor
        float glClearColor_red = 0, glClearColor_green = 0, glClearColor_blue = 0, glClearColor_alpha = 0;
        int x, y, width, height;
        boolean flipX, flipY = true;

        float actorAlpha = 1;

        void glClearColor(float red, float green, float blue, float alpha) {
            this.glClearColor_red = red;
            this.glClearColor_green = green;
            this.glClearColor_blue = blue;
            this.glClearColor_alpha = alpha;
        }

        void flip(boolean x, boolean y) {
            this.flipX = x;
            this.flipY = y;
        }

        public void setDrawTarget(Actor drawTarget) {
            this.drawTarget = drawTarget;
        }

        public Actor getDrawTarget() {
            return drawTarget;
        }

        public float getActorDrawedAlpha() {
            return actorAlpha;
        }

        public void setActorAlpha(float actorAlpha) {
            this.actorAlpha = actorAlpha;
        }

        public void setDrawTarget(Actor drawTarget, int width, int height) {
            this.setDrawTarget(drawTarget, 0, 0, width, height);
        }

        public void setDrawTarget(Actor drawTarget, int x, int y, int width, int height) {
            this.drawTarget = drawTarget;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public TextureRegion getTextureRegion(Texture texture) {

            if (this.width == 0 || this.height == 0) {
                this.width = texture.getWidth();
                this.height = texture.getHeight();
            }

            TextureRegion textureRegion = new TextureRegion(texture, x, y, width, height);
            textureRegion.flip(flipX, flipY);
            return textureRegion;
        }


    }

    public static class CardTextureCreator {
        SpriteBatch batch;
        FrameBuffer frameBuffer;
        TextureRegion currentTextureRegion;

        private CardTextureCreator(SpriteBatch batch) {
            this.batch = batch;
            frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        }


        public interface ITextureCreator {
            void createTexture(TextureCreatorModel model);
        }


        public CardTextureCreator createTextureRegion(ITextureCreator iTextureCreator) {
            TextureCreatorModel model = new TextureCreatorModel();
            iTextureCreator.createTexture(model);


            frameBuffer.begin();
            Gdx.gl.glClearColor(model.glClearColor_red, model.glClearColor_green, model.glClearColor_blue, model.glClearColor_alpha);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            batch.begin();

            model.getDrawTarget().draw(batch, model.getActorDrawedAlpha());

            batch.end();
            frameBuffer.end();


            Texture texture = frameBuffer.getColorBufferTexture();
            currentTextureRegion = model.getTextureRegion(texture);

            return this;
        }

        public TextureRegion getTextureRegion() {
            return this.currentTextureRegion;
        }

        public void dispose() {
            frameBuffer.dispose();
        }


    }


}
