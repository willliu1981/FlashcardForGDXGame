package idv.kuan.flashcard.gdx.game;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import idv.kuan.kuanandroidlibs.databases.provider.AndroidSQLiteFactory;
import idv.kuan.kuanandroidlibs.utils.AndroidVersionHelper;
import idv.kuan.libs.databases.DBFactoryConfiguration;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		DBFactoryConfiguration.getFactory(new AndroidSQLiteFactory(this)).config("android1","fsgdx","fsgdx");

		initialize(new FlashcardGdxGame(new AndroidVersionHelper(this)), config);


	}
}

