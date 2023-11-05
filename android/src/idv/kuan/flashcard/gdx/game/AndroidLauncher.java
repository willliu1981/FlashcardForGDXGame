package idv.kuan.flashcard.gdx.game;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import idv.kuan.kuanandroidlibs.databases.provider.AndroidDBFactory;
import idv.kuan.kuanandroidlibs.utils.AndroidVersionHelper;
import idv.kuan.libs.utils.DBFactoryBuilder;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		initialize(new FlashcardGdxGame(new AndroidVersionHelper(this)), config);


		DBFactoryBuilder.getFactory(new AndroidDBFactory(this)).config("android1","fsgdx.db","fsgdx.db");
	}
}
