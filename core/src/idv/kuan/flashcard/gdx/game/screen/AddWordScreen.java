package idv.kuan.flashcard.gdx.game.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class AddWordScreen implements Screen {
    Game game;
    private Stage stage;
    Viewport viewport;

    SpriteBatch batch;
    Texture img;
    Skin skin;

    TextField textField;
    TextField testTextField;

    public AddWordScreen(Game game) {
        this.game = game;

        batch = new SpriteBatch();


        viewport = new StretchViewport(800, 400);
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);


        skin = new Skin(Gdx.files.internal("uiskin.json"));

        TextField.TextFieldStyle textFieldStyle = generateStyleWithFont("");


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


        textField = new TextField("Add Word", textFieldStyle);
        textField.setPosition(50, 300);
        textField.setSize(700, 50);
        StringBuilder charactersToLoad = new StringBuilder();
        textField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                if (charactersToLoad.indexOf(String.valueOf(c)) < 0) { // 如果字符集中沒有該字符
                    charactersToLoad.append(c); // 添加到字符集中
                    textField.getStyle().font.dispose();
                    TextField.TextFieldStyle Style = generateStyleWithFont(charactersToLoad.toString());
                    textField.setStyle(Style);
                }
            }
        });

        testTextField = new TextField("id", skin);
        testTextField.setPosition(50, 200);
        testTextField.setSize(700, 50);


        stage.addActor(btnAdd);
        stage.addActor(textField);
        stage.addActor(testTextField);
        stage.addActor(btnList);


    }

    private TextField.TextFieldStyle generateStyleWithFont(String userInput) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("GenJyuuGothic-Monospace-Normal.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 12; // 字體大小
        parameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS + userInput;
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose(); // 不要忘記釋放資源

        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = font;
        textFieldStyle.fontColor = Color.WHITE;
        textFieldStyle.background = skin.getDrawable("textfield");
        textFieldStyle.cursor = skin.getDrawable("cursor");
        textFieldStyle.selection = skin.getDrawable("selection");

        return textFieldStyle;
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
