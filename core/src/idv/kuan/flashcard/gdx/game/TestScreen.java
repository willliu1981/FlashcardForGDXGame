package idv.kuan.flashcard.gdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.sql.Connection;
import java.util.List;

import javax.swing.JTextField;

import idv.kuan.libs.databases.schema.modifier.DatabaseSchemaUtils;
import idv.kuan.libs.databases.schema.modifier.SchemaModifier;
import idv.kuan.libs.databases.schema.modifier.SchemaModifierImpl;
import idv.kuan.libs.utils.VersionHelper;
import idv.kuan.libs.databases.schema.modifier.SchemaModifierHandler;
import idv.kuan.libs.databases.schema.modifier.TableSchemaModifier;

public class TestScreen implements Screen {
    private Stage stage;
    Viewport viewport;

    VersionHelper versionHelper;
    SpriteBatch batch;
    Texture img;

    TextField textField;

    public TestScreen(VersionHelper versionHelper) {
        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");

        this.versionHelper = versionHelper;

        checkSchema();

        viewport = new StretchViewport(800, 400);
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);


        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

        TextButton button = new TextButton("click me", skin);
        button.setPosition(150, 200);
        button.setSize(200, 50);
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("xxx TS:Button pressed");
                ((TextButton) actor).setText(textField.getText());
            }
        });


        textField = new TextField("edit...", skin);
        textField.setPosition(150, 300);
        textField.setSize(200, 50);

        stage.addActor(button);
        stage.addActor(textField);
    }

    private void checkSchema() {


        DatabaseSchemaUtils.checkAndUpdateSchema(versionHelper, new DatabaseSchemaUtils.UpdateSchemaExecutor() {
            @Override
            public void execute(SchemaModifierHandler.SchemaModifierBuilder modifierBuilder, List<SchemaModifier> modifiers) {
                TableSchemaModifier word = modifierBuilder.setConstructionSql("CREATE TABLE \"word\" ( " +
                        " \"id\" INTEGER NOT NULL UNIQUE, " +
                        " \"term\" TEXT NOT NULL, " +
                        " \"translation\" TEXT NOT NULL, " +
                        " \"at_created\" TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
                        " \"at_updated\" TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
                        " \"metadata\" BLOB, " +
                        " PRIMARY KEY(\"id\" AUTOINCREMENT) " +
                        ")").setTableName("word").createSchemaModifier(TableSchemaModifier.class);
                word.setNewColumns("id,term,translation,at_created,at_updated,metadata");
                word.setOldColumns("id,term,translation,at_created,at_updated,metadata");

                modifiers.add(word);

            }
        });
    }


    @Override
    public void show() {

    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

        //ScreenUtils.clear(1, 1, 1, 1f);
        //batch.begin();
        //batch.draw(img, 0, 0);
        //batch.end();

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
