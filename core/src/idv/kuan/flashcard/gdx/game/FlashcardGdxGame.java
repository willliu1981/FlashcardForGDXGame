package idv.kuan.flashcard.gdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class FlashcardGdxGame extends Game {
    TestGameScreen screen;


    @Override
    public void create() {

        screen = new TestGameScreen();
        setScreen(screen);


    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        screen.dispose();
    }

    @Override
    public void resize(int width, int height) {
        screen.resize(width, height);
    }
}
