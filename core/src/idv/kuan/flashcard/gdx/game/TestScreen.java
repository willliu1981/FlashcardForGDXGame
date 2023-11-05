package idv.kuan.flashcard.gdx.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import java.sql.Connection;

import idv.kuan.libs.utils.DBFactoryBuilder;
import idv.kuan.libs.utils.VersionHelper;
import idv.kuan.libs.utils.schema.modifier.SchemaModifierHandler;
import idv.kuan.libs.utils.schema.modifier.TableSchemaModifier;

public class TestScreen implements Screen {

    VersionHelper versionHelper;
    SpriteBatch batch;
    Texture img;

    public TestScreen(VersionHelper versionHelper) {
        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");

        Connection connection = DBFactoryBuilder.getFactory().getConnection();


        System.out.println("xxx TS: " + connection);


        SchemaModifierHandler handler = new SchemaModifierHandler(connection,versionHelper.getVersionCode() );
        SchemaModifierHandler.SchemaModifierBuilder schemaModifierBuilder = handler.getSchemaModifierBuilder();
        schemaModifierBuilder.setConstructionSql("CREATE TABLE \"word\" ( " +
                " \"id\" INTEGER NOT NULL UNIQUE, " +
                " \"term\" TEXT NOT NULL, " +
                " \"translation\" TEXT NOT NULL, " +
                " \"at_created\" TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
                " \"at_updated\" TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
                " \"metadata\" BLOB, " +
                " PRIMARY KEY(\"id\" AUTOINCREMENT) " +
                ")");

        TableSchemaModifier schemaModifier = (TableSchemaModifier) schemaModifierBuilder.createSchemaModifier(TableSchemaModifier.class);
        schemaModifier.setTableName("word");
        schemaModifier.setCurrentColumns("id,term,translation,at_created,at_updated");
        schemaModifier.setSelectedColumns("id,term,translation,at_created,at_updated");


        handler.addSchemaModifier(schemaModifier);
        handler.execute();

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
