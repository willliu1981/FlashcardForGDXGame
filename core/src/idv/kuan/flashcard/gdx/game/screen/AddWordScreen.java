package idv.kuan.flashcard.gdx.game.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.sql.SQLException;
import java.util.Date;

import idv.kuan.flashcard.gdx.game.database.dao.WordDao;
import idv.kuan.flashcard.gdx.game.database.entity.Word;
import idv.kuan.flashcard.gdx.game.util.StyleUtil;
import idv.kuan.libs.databases.models.MetadataEntityUtil;
import idv.kuan.testlib.test.TestMetadata;

public class AddWordScreen implements Screen {
    Game game;
    private Stage stage;
    Viewport viewport;

    SpriteBatch batch;
    Texture img;

    TextField txtfTerm;
    TextField txtfTranslation;
    StyleUtil.DynamicCharacters dynamicCharacters;

    public AddWordScreen(Game game) {
        this.game = game;

        batch = new SpriteBatch();
        dynamicCharacters = new StyleUtil.DynamicCharacters();
        BitmapFont font = null;
        TextField.TextFieldStyle textFieldStyle;
        Button.ButtonStyle buttonStyle;

        viewport = new StretchViewport(800, 400);
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        dynamicCharacters.add("新增...");
        font = StyleUtil.generateDefaultDynamicFont(this.dynamicCharacters.getCharacters());
        TextButton.TextButtonStyle textButtonStyle = StyleUtil.generateDefaultButtonStyle(font);

        TextButton btnAdd = new TextButton("新增", textButtonStyle);
        btnAdd.setPosition(150, 100);
        btnAdd.setSize(200, 50);
        btnAdd.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                WordDao dao = new WordDao();
                Word word = new Word();

                Date date = new Date();
                word.setTerm(txtfTerm.getText());
                word.setTranslation(txtfTranslation.getText());
                word.setVersion(1);

                //MetadataEntityUtil.DefaultMetadata metadata = word.getMetadata();
                //metadata.setData("1234-" + date);
                MetadataEntityUtil.DefaultMetadata metadata1 = new TestMetadata();
                metadata1.addMetadataObject("msg", new MetadataEntityUtil.MetadataObject("v1"));


                try {
                    dao.create(word);
                } catch (SQLException e) {
                    e.printStackTrace();
                }


            }
        });


        TextButton btnReturn = new TextButton("Return", MainScreen.skin);
        btnReturn.setPosition(400, 100);
        btnReturn.setSize(200, 50);
        btnReturn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainScreen(game));

            }
        });


        dynamicCharacters.add("請輸入英文單字...");
        font = StyleUtil.generateDefaultDynamicFont(this.dynamicCharacters.getCharacters());
        textFieldStyle = StyleUtil.generateDefaultTextFieldStyle(font);

        txtfTerm = new TextField("", textFieldStyle);
        txtfTerm.setMessageText("請輸入英文單字...");
        txtfTerm.setPosition(50, 300);
        txtfTerm.setSize(700, 50);
        txtfTerm.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                AddWordScreen.this.dynamicCharacters.add(c);
                BitmapFont font1 = StyleUtil.generateDefaultDynamicFont(dynamicCharacters.getCharacters());
                TextField.TextFieldStyle textFieldStyle = StyleUtil.generateDefaultTextFieldStyle(font1);
                textField.setStyle(textFieldStyle);

            }
        });

        dynamicCharacters.add("請輸入中文翻譯...");
        font = StyleUtil.generateDefaultDynamicFont(this.dynamicCharacters.getCharacters());
        textFieldStyle = StyleUtil.generateDefaultTextFieldStyle(font);

        txtfTranslation = new TextField("", textFieldStyle);
        txtfTranslation.setMessageText("請輸入中文翻譯...");
        txtfTranslation.setPosition(50, 200);
        txtfTranslation.setSize(700, 50);
        txtfTranslation.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                AddWordScreen.this.dynamicCharacters.add(c);
                BitmapFont font1 = StyleUtil.generateDefaultDynamicFont(dynamicCharacters.getCharacters());
                TextField.TextFieldStyle textFieldStyle = StyleUtil.generateDefaultTextFieldStyle(font1);
                textField.setStyle(textFieldStyle);

            }
        });


        stage.addActor(btnAdd);
        stage.addActor(txtfTerm);
        stage.addActor(txtfTranslation);
        stage.addActor(btnReturn);


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
