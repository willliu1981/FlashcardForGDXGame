package idv.kuan.flashcard.gdx.game.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.sql.SQLException;
import java.sql.Timestamp;
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


    TextField txtfTerm;
    TextField txtfExtraData;
    TextField txtfTranslation;
    StyleUtil.DynamicCharacters dynamicCharacters;

    public AddWordScreen(Game game) {
        this.game = game;

        viewport = new StretchViewport(800, 400);
        stage = new Stage(viewport);


        dynamicCharacters = new StyleUtil.DynamicCharacters();
        BitmapFont font = null;

        //==================== Text 開始 ====================//
        TextField.TextFieldStyle textFieldStyle;

        //term
        dynamicCharacters.add("請輸入英文單字...");
        font = StyleUtil.generateDefaultDynamicFont(this.dynamicCharacters.getCharacters());
        textFieldStyle = StyleUtil.generateDefaultTextFieldStyle(font);

        txtfTerm = new TextField("", textFieldStyle);
        txtfTerm.setMessageText("請輸入英文單字...");
        txtfTerm.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                AddWordScreen.this.dynamicCharacters.add(c);
                BitmapFont font1 = StyleUtil.generateDefaultDynamicFont(dynamicCharacters.getCharacters());
                TextField.TextFieldStyle textFieldStyle = StyleUtil.generateDefaultTextFieldStyle(font1);
                textField.setStyle(textFieldStyle);

            }
        });

        //translation
        dynamicCharacters.add("請輸入中文翻譯...");
        font = StyleUtil.generateDefaultDynamicFont(this.dynamicCharacters.getCharacters());
        textFieldStyle = StyleUtil.generateDefaultTextFieldStyle(font);

        txtfTranslation = new TextField("", textFieldStyle);
        txtfTranslation.setMessageText("請輸入中文翻譯...");
        txtfTranslation.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                AddWordScreen.this.dynamicCharacters.add(c);
                BitmapFont font1 = StyleUtil.generateDefaultDynamicFont(dynamicCharacters.getCharacters());
                TextField.TextFieldStyle textFieldStyle = StyleUtil.generateDefaultTextFieldStyle(font1);
                textField.setStyle(textFieldStyle);

            }
        });

        //extra data
        dynamicCharacters.add("請輸入額外資料...");
        font = StyleUtil.generateDefaultDynamicFont(this.dynamicCharacters.getCharacters());
        textFieldStyle = StyleUtil.generateDefaultTextFieldStyle(font);

        txtfExtraData = new TextField("", textFieldStyle);
        txtfExtraData.setMessageText("請輸入額外資料...");
        txtfExtraData.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                AddWordScreen.this.dynamicCharacters.add(c);
                BitmapFont font1 = StyleUtil.generateDefaultDynamicFont(dynamicCharacters.getCharacters());
                TextField.TextFieldStyle textFieldStyle = StyleUtil.generateDefaultTextFieldStyle(font1);
                textField.setStyle(textFieldStyle);

            }
        });

        //==================== Text 結束 ====================//


        //--------------- Button 開始 ---------------//

        //add word
        dynamicCharacters.add("新增...");
        font = StyleUtil.generateDefaultDynamicFont(this.dynamicCharacters.getCharacters());
        TextButton.TextButtonStyle textButtonStyle = StyleUtil.generateDefaultButtonStyle(font);

        TextButton btnAdd = new TextButton("新增", textButtonStyle);
        btnAdd.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                WordDao dao = new WordDao();
                Word word = new Word();

                word.setTerm(txtfTerm.getText());
                word.setTranslation(txtfTranslation.getText());
                word.setVersion(1);

                MetadataEntityUtil.DefaultMetadata metadata = new TestMetadata();
                metadata.addDataObject(TestMetadata.EXTRA_DATA, new MetadataEntityUtil.DataObject
                        ("n_" + txtfExtraData.getText()).setData(txtfExtraData.getText()));
                metadata.setAtCreated(new Timestamp(new Date().getTime()));
                metadata.setAtUpdated(new Timestamp(new Date().getTime()));
                word.setMetadata(metadata);

                try {
                    dao.create(word);

                    game.setScreen(new WordListScreen(game, AddWordScreen.this));
                } catch (SQLException e) {
                    e.printStackTrace();
                }


            }
        });


        //return
        TextButton btnReturn = new TextButton("Return", MainScreen.skin);
        btnReturn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainScreen(game));

            }
        });

        //--------------- Button 結束 ---------------//

        Table table = new Table();
        table.add(txtfTerm).width(400).pad(10).row();
        table.add(txtfTranslation).width(400).pad(10).row();
        table.add(txtfExtraData).width(400).pad(10).row();

        Table buttonTable = new Table();

        buttonTable.add(btnAdd).width(150).padRight(10);
        buttonTable.add(btnReturn).width(150).padLeft(10);

        table.add(buttonTable).center().row();
        table.setBounds(50, 10, 600, 300);

        stage.addActor(table);


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
        stage.dispose();
    }


}
