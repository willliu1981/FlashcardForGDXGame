package idv.kuan.flashcard.gdx.game.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
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

public class EditWordScreen implements Screen {
    Game game;
    private Stage stage;
    Viewport viewport;


    TextField txtfTerm;
    TextField txtfExtraData;
    TextField txtfTranslation;
    StyleUtil.DynamicCharacters dynamicCharacters;

    TextureRegion deleteWordRegion;
    TextureRegion deleteWordClickedRegion;

    public EditWordScreen(Game game, Word word) {
        this.game = game;

        viewport = new StretchViewport(800, 400);
        stage = new Stage(viewport);


        dynamicCharacters = new StyleUtil.DynamicCharacters();
        BitmapFont font = null;

        //圖片資源載入
        deleteWordRegion = new TextureRegion(new Texture("garbage.png"));
        deleteWordClickedRegion = new TextureRegion(new Texture("garbageclicked.png"));

        //==================== Text 開始 ====================//
        TextField.TextFieldStyle textFieldStyle;

        //term
        dynamicCharacters.add("請輸入英文單字..." + word.getTerm());
        font = StyleUtil.generateFontWithAddedChars(this.dynamicCharacters.getCharacters());
        textFieldStyle = StyleUtil.generateDefaultTextFieldStyle(font);

        txtfTerm = new TextField(word.getTerm(), textFieldStyle);
        txtfTerm.setMessageText("請輸入英文單字...");
        txtfTerm.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                EditWordScreen.this.dynamicCharacters.add(c);
                BitmapFont font1 = StyleUtil.generateFontWithAddedChars(dynamicCharacters.getCharacters());
                TextField.TextFieldStyle textFieldStyle = StyleUtil.generateDefaultTextFieldStyle(font1);
                textField.setStyle(textFieldStyle);

            }
        });

        //translation
        dynamicCharacters.add("請輸入中文翻譯..." + word.getTranslation());
        font = StyleUtil.generateFontWithAddedChars(this.dynamicCharacters.getCharacters());
        textFieldStyle = StyleUtil.generateDefaultTextFieldStyle(font);

        txtfTranslation = new TextField(word.getTranslation(), textFieldStyle);
        txtfTranslation.setMessageText("請輸入中文翻譯...");
        txtfTranslation.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                EditWordScreen.this.dynamicCharacters.add(c);
                BitmapFont font1 = StyleUtil.generateFontWithAddedChars(dynamicCharacters.getCharacters());
                TextField.TextFieldStyle textFieldStyle = StyleUtil.generateDefaultTextFieldStyle(font1);
                textField.setStyle(textFieldStyle);

            }
        });

        //extra data
        dynamicCharacters.add("請輸入額外資料...");
        font = StyleUtil.generateFontWithAddedChars(this.dynamicCharacters.getCharacters());
        textFieldStyle = StyleUtil.generateDefaultTextFieldStyle(font);

        txtfExtraData = new TextField("", textFieldStyle);
        txtfExtraData.setMessageText("請輸入額外資料...");
        txtfExtraData.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                EditWordScreen.this.dynamicCharacters.add(c);
                BitmapFont font1 = StyleUtil.generateFontWithAddedChars(dynamicCharacters.getCharacters());
                TextField.TextFieldStyle textFieldStyle = StyleUtil.generateDefaultTextFieldStyle(font1);
                textField.setStyle(textFieldStyle);

            }
        });

        //==================== Text 結束 ====================//


        //--------------- Button 開始 ---------------//

        //add word
        dynamicCharacters.add("更新...");
        font = StyleUtil.generateFontWithAddedChars(this.dynamicCharacters.getCharacters());
        TextButton.TextButtonStyle textButtonStyle = StyleUtil.generateDefaultButtonStyle(font);

        TextButton btnAdd = new TextButton("更新", textButtonStyle);
        btnAdd.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                WordDao dao = new WordDao();


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
                    dao.update(word);

                    game.setScreen(new WordListScreen(game, EditWordScreen.this));
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
                game.setScreen(new WordListScreen(game));

            }
        });

        //delete
        ImageButton.ImageButtonStyle deleteStyle = new ImageButton.ImageButtonStyle();
        deleteStyle.up = new TextureRegionDrawable(deleteWordRegion);
        deleteStyle.down = new TextureRegionDrawable(deleteWordClickedRegion);
        ImageButton deleteButton = new ImageButton(deleteStyle);
        deleteButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                dynamicCharacters.add("刪除資料");
                BitmapFont font = StyleUtil.generateFontWithAddedChars(dynamicCharacters.getCharacters());
                Window.WindowStyle style = new Window.WindowStyle();
                style.titleFont = font;

                Dialog dialog = new Dialog("刪除資料", style) {
                    @Override
                    protected void result(Object object) {
                        Boolean isConfirm = (Boolean) object;
                        if (isConfirm) {
                            WordDao dao = new WordDao();
                            try {
                                dao.delete(word);
                                game.setScreen(new WordListScreen(game));
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                };

                dynamicCharacters.add("您確定要刪除這筆資料?");
                font = StyleUtil.generateFontWithAddedChars(dynamicCharacters.getCharacters());
                Label.LabelStyle labelStyle = new Label.LabelStyle();
                labelStyle.font = font;
                dialog.text("您確定要刪除這筆資料?", labelStyle);

                //button Yes
                TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
                textButtonStyle.font = font;
                textButtonStyle.fontColor = Color.RED;
                TextButton btnYes = new TextButton("Yes", textButtonStyle);

                //button No
                textButtonStyle = new TextButton.TextButtonStyle();
                textButtonStyle.font = font;
                TextButton btnNo = new TextButton("No", textButtonStyle);


                //dialog.pad(10);
                dialog.getContentTable().padTop(10);
                dialog.button(btnYes, true);
                dialog.getButtonTable().getCell(btnYes).padRight(10);
                dialog.button(btnNo, false);
                dialog.show(stage);
            }
        });

        //--------------- Button 結束 ---------------//

        Table table = new Table();
        table.add(txtfTerm).width(400).pad(10).row();
        table.add(txtfTranslation).width(400).pad(10).row();
        table.add(txtfExtraData).width(400).pad(10).row();

        Table buttonTable = new Table();

        buttonTable.add(deleteButton).size(50).padRight(10);
        buttonTable.add(btnAdd).width(150).pad(10);
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
