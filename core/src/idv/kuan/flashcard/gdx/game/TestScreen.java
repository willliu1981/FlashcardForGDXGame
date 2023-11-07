package idv.kuan.flashcard.gdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.sql.Connection;
import java.util.List;

import idv.kuan.libs.databases.schema.modifier.DatabaseSchemaUtils;
import idv.kuan.libs.databases.schema.modifier.SchemaModifier;
import idv.kuan.libs.utils.VersionHelper;
import idv.kuan.libs.databases.schema.modifier.SchemaModifierHandler;
import idv.kuan.libs.databases.schema.modifier.TableSchemaModifier;

public class TestScreen implements Screen {
    private Stage stage;
    Viewport viewport;

    VersionHelper versionHelper;
    SpriteBatch batch;
    Texture img;

    public TestScreen(VersionHelper versionHelper) {
        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");

        this.versionHelper = versionHelper;

        checkSchema();

        viewport = new StretchViewport(800, 100);
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);
    }

    private void checkSchema() {


        DatabaseSchemaUtils.checkAndUpdateSchema(versionHelper, new DatabaseSchemaUtils.UpdateSchemaExecutor() {
            @Override
            public <T extends SchemaModifier> void execute(SchemaModifierHandler.SchemaModifierBuilder modifierBuilder, List<T> modifiers) {
                TableSchemaModifier word = modifierBuilder.setConstructionSql("CREATE TABLE \"word\" ( " +
                        " \"id\" INTEGER NOT NULL UNIQUE, " +
                        " \"term_1\" TEXT NOT NULL, " +
                        " \"translation_1x\" TEXT NOT NULL, " +
                        " \"at_created\" TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
                        " \"at_updated\" TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
                        " \"metadata\" BLOB, " +
                        " PRIMARY KEY(\"id\" AUTOINCREMENT) " +
                        ")").setTableName("word").createSchemaModifier(TableSchemaModifier.class);
                word.setNewColumns("id,term_1,translation_x1,at_created,at_updated,metadata");
                word.setOldColumns("id,term_1,translation_1,at_created,at_updated,metadata");

                TableSchemaModifier word2 = modifierBuilder.setConstructionSql("CREATE TABLE \"word2\" ( " +
                        " \"id\" INTEGER NOT NULL UNIQUE, " +
                        " \"term_2\" TEXT NOT NULL, " +
                        " \"translation_x2\" TEXT NOT NULL, " +
                        " \"at_created\" TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
                        " \"at_updated\" TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
                        " \"metadata\" BLOB, " +
                        " PRIMARY KEY(\"id\" AUTOINCREMENT) " +
                        ")").setTableName("word2").createSchemaModifier(TableSchemaModifier.class);
                word2.setNewColumns("id,term_2,translation_x2,at_created,at_updated,metadata");
                word2.setOldColumns("id,term_2,translation_2,at_created,at_updated,metadata");

                modifiers.add((T) word);
                modifiers.add((T) word2);
            }
        });
    }


    @Override
    public void show() {

    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        ScreenUtils.clear(1, 1, 1, 1f);
        batch.begin();
        batch.draw(img, 0, 0);
        batch.end();

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
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
        stage.dispose();
    }
}
