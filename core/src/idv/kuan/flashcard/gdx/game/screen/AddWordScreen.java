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

public class AddWordScreen implements Screen {
    Game game;
    private Stage stage;
    Viewport viewport;

    SpriteBatch batch;
    Texture img;

    TextField textField;
    TextField testTextField;

    public AddWordScreen(Game game) {
        this.game = game;

        batch = new SpriteBatch();


        viewport = new StretchViewport(800, 400);
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);


        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

        //
        TextButton btnAdd = new TextButton("Empty", skin);
        btnAdd.setPosition(150, 100);
        btnAdd.setSize(200, 50);
        btnAdd.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {


            }
        });

        TextButton btnList = new TextButton("Return", skin);
        btnList.setPosition(400, 100);
        btnList.setSize(200, 50);
        btnList.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainScreen(game));

            }
        });


        textField = new TextField("Add Word", skin);
        textField.setPosition(50, 300);
        textField.setSize(700, 50);

        testTextField = new TextField("id", skin);
        testTextField.setPosition(50, 200);
        testTextField.setSize(700, 50);


        stage.addActor(btnAdd);
        stage.addActor(textField);
        stage.addActor(testTextField);
        stage.addActor(btnList);
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
