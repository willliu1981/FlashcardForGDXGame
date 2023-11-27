package idv.kuan.flashcard.gdx.game.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import idv.kuan.flashcard.gdx.game.util.TextFieldStyleUtil;

public class AddWordScreen implements Screen {
    Game game;
    private Stage stage;
    Viewport viewport;

    SpriteBatch batch;
    Texture img;

    TextField txtfTerm;
    TextField testTextField;

    public AddWordScreen(Game game) {
        this.game = game;

        batch = new SpriteBatch();


        viewport = new StretchViewport(800, 400);
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);


        TextButton btnAdd = new TextButton("Empty", MainScreen.skin);
        btnAdd.setPosition(150, 100);
        btnAdd.setSize(200, 50);
        btnAdd.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {


            }
        });

        TextButton btnList = new TextButton("Return", MainScreen.skin);
        btnList.setPosition(400, 100);
        btnList.setSize(200, 50);
        btnList.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainScreen(game));

            }
        });

        String placeholderText_termHint = "請輸入英文單字...";
        TextField.TextFieldStyle textFieldStyle = TextFieldStyleUtil.generateCustomFontStyle(placeholderText_termHint);
        txtfTerm = new TextField("", textFieldStyle);
        txtfTerm.setPosition(50, 300);
        txtfTerm.setSize(700, 50);
        TextFieldStyleUtil.CharacterLoader characterLoader = new TextFieldStyleUtil.CharacterLoader();
        characterLoader.add(placeholderText_termHint);
        txtfTerm.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                TextFieldStyleUtil.refreshStyleOnInput(textField, c, characterLoader);

            }
        });

        txtfTerm.setMessageText(placeholderText_termHint);


        testTextField = new TextField("id", MainScreen.skin);
        testTextField.setPosition(50, 200);
        testTextField.setSize(700, 50);


        stage.addActor(btnAdd);
        stage.addActor(txtfTerm);
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
