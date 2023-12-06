package idv.kuan.flashcard.gdx.game.screen;

import com.badlogic.gdx.Game;
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
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.List;

import idv.kuan.libs.databases.schema.modifier.DatabaseSchemaUtils;
import idv.kuan.libs.databases.schema.modifier.SchemaModifier;
import idv.kuan.libs.databases.schema.modifier.SchemaModifierHandler;
import idv.kuan.libs.databases.schema.modifier.TableSchemaModifier;
import idv.kuan.libs.utils.VersionHelper;

public class MainScreen implements Screen {
    Game game;
    private Stage stage;
    Viewport viewport;

    public static Skin skin;

    private static VersionHelper versionHelper;
    SpriteBatch batch;
    Texture img;

    TextField txtfTitle;
    TextField txtfCount;

    public MainScreen(Game game) {
        this(game, versionHelper);
    }

    public MainScreen(Game game, VersionHelper versionHelper) {
        if (skin == null) {
            skin = new Skin(Gdx.files.internal("uiskin.json"));

        }
        this.game = game;
        this.versionHelper = versionHelper;
        batch = new SpriteBatch();

        checkSchema();

        viewport = new StretchViewport(800, 400);
        stage = new Stage(viewport);


        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

        //
        TextButton btnAdd = new TextButton("Add Word", skin);
        btnAdd.setPosition(150, 100);
        btnAdd.setSize(200, 50);
        btnAdd.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new AddWordScreen(game));

            }
        });

        TextButton btnList = new TextButton("Word List", skin);
        btnList.setPosition(400, 100);
        btnList.setSize(200, 50);
        btnList.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new WordListScreen(game));

            }
        });


        //Label title = new Label("Word FlashCard");

        txtfTitle = new TextField("Word FlashCard", skin);
        txtfTitle.setPosition(50, 300);
        txtfTitle.setSize(700, 50);
        txtfTitle.setDisabled(true);

        txtfCount = new TextField("count:0", skin);
        txtfCount.setPosition(50, 200);
        txtfCount.setSize(700, 50);
        txtfCount.setDisabled(true);

        stage.addActor(btnAdd);
        stage.addActor(txtfTitle);
        stage.addActor(txtfCount);
        stage.addActor(btnList);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }


    private void checkSchema() {
        DatabaseSchemaUtils.checkAndUpdateSchema(versionHelper, new DatabaseSchemaUtils.UpdateSchemaExecutor() {
            @Override
            public void execute(SchemaModifierHandler.SchemaModifierBuilder modifierBuilder, List<SchemaModifier> modifiers) {
                TableSchemaModifier word = modifierBuilder.setConstructionSql("CREATE TABLE \"word\" ( " +
                        " \"id\" INTEGER NOT NULL UNIQUE, " +
                        " \"term\" TEXT NOT NULL, " +
                        " \"translation\" TEXT NOT NULL, " +
                        " \"version\" INTEGER NOT NULL, " +
                        " \"metadata\" BLOB, " +
                        " PRIMARY KEY(\"id\" AUTOINCREMENT) " +
                        ")").setTableName("word").createSchemaModifier(TableSchemaModifier.class);
                word.setNewColumns("id,term,translation,version,metadata");
                word.setOldColumns("id,term,translation,-1,metadata");

                modifiers.add(word);
            }
        });
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
        //img.dispose();
        stage.dispose();
    }


}
