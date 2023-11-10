package idv.kuan.flashcard.gdx.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import idv.kuan.flashcard.gdx.game.FlashcardGdxGame;
import idv.kuan.libs.databases.DBFactoryConfiguration;
import idv.kuan.libs.databases.provider.DefaultDBFactory;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setForegroundFPS(60);
        config.setTitle("FlashcardForLibgdxGame");

        DBFactoryConfiguration.getFactory(new DefaultDBFactory()).config("desktop1", "C:/java/db/sqlite/flashcard for gdx game/fcgdx.db");

        new Lwjgl3Application(new FlashcardGdxGame(new DesktopVersionHelper()), config);

    }
}
