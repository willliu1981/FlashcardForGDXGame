package idv.kuan.flashcard.gdx.game;

import com.badlogic.gdx.Game;

import idv.kuan.flashcard.gdx.game.screen.MainScreen;
import idv.kuan.flashcard.gdx.game.test.TestScreen;
import idv.kuan.testlib.test.TestMetadata;
import idv.kuan.testlib.test.TestV2Metadata;
import idv.kuan.libs.databases.models.MetadataRegister;
import idv.kuan.libs.utils.VersionHelper;


public class FlashcardGdxGame extends Game {

    private VersionHelper versionHelper;


    public FlashcardGdxGame(VersionHelper versionHelper) {
        this.versionHelper = versionHelper;

        MetadataRegister.addMetadata(1, TestMetadata.class);
        MetadataRegister.addMetadata(2, TestV2Metadata.class);
    }


    @Override
    public void create() {

        MainScreen screen = new MainScreen(this, versionHelper);

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
