package idv.kuan.flashcard.gdx.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import java.sql.Connection;

import idv.kuan.libs.databases.utils.DBFactoryBuilder;

public class TestScreen implements Screen {

    SpriteBatch batch;
    Texture img;

    public TestScreen() {
        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");

        Connection connection = DBFactoryBuilder.getFactory().getConnection();


        System.out.println("xxx TS: " + connection);


    }

    @Override
    public void show() {

    }


    @Override
    public void render(float delta) {
        ScreenUtils.clear(1, 1, 1, 1f);
        batch.begin();
        batch.draw(img, 0, 0);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
    }
}
