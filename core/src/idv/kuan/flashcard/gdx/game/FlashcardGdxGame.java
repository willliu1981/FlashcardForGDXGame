package idv.kuan.flashcard.gdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import idv.kuan.libs.utils.VersionHelper;


public class FlashcardGdxGame extends Game {

    private VersionHelper versionHelper;

    public FlashcardGdxGame() {
    }

    public FlashcardGdxGame(VersionHelper versionHelper) {
        this.versionHelper = versionHelper;
    }


    @Override
    public void create() {

        TestScreen screen = new TestScreen(versionHelper);

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
