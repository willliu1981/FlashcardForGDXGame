package idv.kuan.flashcard.gdx.game;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import idv.kuan.flashcard.gdx.game.FlashcardGdxGame;
import idv.kuan.kuanandroidlibs.databases.provider.AndroidDBFactory;
import idv.kuan.libs.databases.utils.DBFactoryBuilder;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new FlashcardGdxGame(), config);


		DBFactoryBuilder.getFactory(new AndroidDBFactory(this)).config("android1","fsgdx.db","fsgdx");
	}
}
