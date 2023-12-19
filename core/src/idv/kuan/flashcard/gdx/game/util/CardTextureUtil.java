package idv.kuan.flashcard.gdx.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.ScreenUtils;

public class CardTextureUtil {


    public static CardTextureCreator getCardTextureCreator(SpriteBatch batch) {
        return new CardTextureCreator(batch);
    }

    public static class TextureCreatorModel {
        Actor drawTarget;

        //glClearColor
        float glClearColor_red = 0, glClearColor_green = 0, glClearColor_blue = 0, glClearColor_alpha = 0;
        int x, y, width, height;

        float actorAlpha = 1;

        void glClearColor(float red, float green, float blue, float alpha) {
            this.glClearColor_red = red;
            this.glClearColor_green = green;
            this.glClearColor_blue = blue;
            this.glClearColor_alpha = alpha;
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


            return new TextureRegion(texture, x, y, width, height);
        }


    }

    public static class CardTextureCreator {
        SpriteBatch batch;
        FrameBuffer frameBuffer;

        private CardTextureCreator(SpriteBatch batch) {
            this.batch = batch;
        }

        public interface TextureCreator {
            void createTexture(TextureCreatorModel model);
        }


        public TextureRegion createTextureRegion(TextureCreator textureCreator) {
            TextureCreatorModel model = new TextureCreatorModel();
            textureCreator.createTexture(model);

            frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
            frameBuffer.begin();
            Gdx.gl.glClearColor(model.glClearColor_red, model.glClearColor_green, model.glClearColor_blue, model.glClearColor_alpha);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            batch.begin();

            model.getDrawTarget().draw(batch, model.getActorDrawedAlpha());

            batch.end();
            frameBuffer.end();

            Texture texture = frameBuffer.getColorBufferTexture();


            return model.getTextureRegion(texture);
        }

        public void dispose() {
            frameBuffer.dispose();
        }


    }


}
