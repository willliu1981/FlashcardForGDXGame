package idv.kuan.flashcard.gdx.game.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.sql.SQLException;
import java.util.List;

import idv.kuan.flashcard.gdx.game.database.dao.WordDao;
import idv.kuan.flashcard.gdx.game.database.entity.Word;
import idv.kuan.flashcard.gdx.game.util.StyleUtil;
import idv.kuan.testlib.test.TestMetadata;

public class WordListScreen implements Screen {
    Game game;
    private Stage stage;
    Viewport viewport;

    TextureRegion checkedNotCheckedRegion;
    TextureRegion checkedNotCheckedClickedRegion;
    TextureRegion checkedCheckedRegion;
    TextureRegion checkedClickedRegion;
    TextureRegion editRegion;
    TextureRegion editClickedRegion;
    TextureRegion separatorRegion;

    StyleUtil.DynamicCharacters dynamicCharacters;
    BitmapFont font;

    public WordListScreen(Game game) {
        this(game, null);
    }

    public WordListScreen(Game game, Screen lastScreen) {
        this.game = game;

        viewport = new StretchViewport(800, 400);
        stage = new Stage(viewport);

        dynamicCharacters = new StyleUtil.DynamicCharacters();


// 創建一個表格布局您的元素
        Table table = new Table();
        //table.setFillParent(true);

        // 加載圖片資源
        checkedNotCheckedRegion = new TextureRegion(new Texture("checkedboxnotchecked128.png"));
        checkedNotCheckedClickedRegion = new TextureRegion(new Texture("checkedboxnotcheckedclicked128.png"));
        checkedCheckedRegion = new TextureRegion(new Texture("checkedbox128.png"));
        checkedClickedRegion = new TextureRegion(new Texture("checkedboxclicked128.png"));
        editRegion = new TextureRegion(new Texture("edit128.png"));
        editClickedRegion = new TextureRegion(new Texture("editclicked128.png"));
        separatorRegion = new TextureRegion(new Texture("separator.png"));


        WordDao dao = new WordDao();
        try {
            List<Word> all = dao.findAll();
            int i = 0;
            for (Word w : all) {
                Table elementTable = new Table();
                Label.LabelStyle labelStyle = StyleUtil.generateDefaultLabelStyle(font);

                ImageButton.ImageButtonStyle checkedboxStyle = new ImageButton.ImageButtonStyle();
                checkedboxStyle.up = new TextureRegionDrawable(checkedNotCheckedRegion); // 未選中時的圖片
                checkedboxStyle.down = new TextureRegionDrawable(checkedNotCheckedClickedRegion); // 選中時的圖片
                final ImageButton checkedButton = new ImageButton(checkedboxStyle);

                Label idxLabel = new Label(String.valueOf(i), MainScreen.skin);

                //term
                Label termLabel = new Label(w != null ? w.getTerm() : null, MainScreen.skin);

                //translation -- begin//
                if (w != null) {
                    dynamicCharacters.add(w.getTranslation());
                }
                font = StyleUtil.generateDefaultDynamicFont(this.dynamicCharacters.getCharacters());
                labelStyle = StyleUtil.generateDefaultLabelStyle(font);

                Label translationLabel = new Label(w != null ? w.getTranslation() : null, labelStyle);
                //translation -- end//

                //extraDataLabel == begin//  for test
                if (w != null && w.getMetadata() != null && w.getMetadata().getDataObject(TestMetadata.EXTRA_DATA) != null) {
                    dynamicCharacters.add((String) w.getMetadata().getDataObject(TestMetadata.EXTRA_DATA).getData());
                }
                font = StyleUtil.generateDefaultDynamicFont(this.dynamicCharacters.getCharacters(), 12);

                labelStyle = StyleUtil.generateDefaultLabelStyle(font);

                Label extraDataLabel = new Label(w != null && w.getMetadata() != null && w.getMetadata().getDataObject(TestMetadata.EXTRA_DATA) != null ?
                        (String) w.getMetadata().getDataObject(TestMetadata.EXTRA_DATA).getData() : null, labelStyle);
                //extraDataLabel == end//

                ImageButton.ImageButtonStyle editStyle = new ImageButton.ImageButtonStyle();
                editStyle.up = new TextureRegionDrawable(editRegion); // 未點擊時的圖片
                editStyle.down = new TextureRegionDrawable(editClickedRegion); // 點擊時的圖片

                final ImageButton editButton = new ImageButton(editStyle);

                i++;

                Table textsTable = new Table();
                textsTable.add(termLabel).width(200);
                textsTable.add(extraDataLabel).width(200);
                textsTable.row();
                textsTable.add(translationLabel).align(Align.left);

                Image separator = new Image(new TextureRegionDrawable(separatorRegion));

                elementTable.add(idxLabel).width(10).padRight(10);
                elementTable.add(checkedButton).size(25, 50);
                elementTable.add(textsTable).padLeft(10);
                elementTable.add(editButton).size(50);

                table.add(elementTable);
                table.row().pad(2);
                table.add(separator).size(500, 5);
                table.row().pad(2);


            }


        } catch (SQLException e) {
            e.printStackTrace();
        }


        // 創建 ScrollPane
        ScrollPane scrollPane = new ScrollPane(table, MainScreen.skin);

// 設定 ScrollPane 的大小和位置
        scrollPane.setBounds(50, 70, 600, 300);

        TextButton returnButton = new TextButton("return", MainScreen.skin);
        returnButton.setBounds(50, 10, 200, 50);
        returnButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (lastScreen != null) {
                    if (AddWordScreen.class.isInstance(lastScreen)) {
                        game.setScreen(lastScreen);
                    }
                } else {
                    game.setScreen(new MainScreen(game));
                }
            }
        });


// 將 ScrollPane 添加到 Stage
        stage.addActor(scrollPane);
        stage.addActor(returnButton);

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
        // 释放所有纹理
        checkedNotCheckedRegion.getTexture().dispose();
        checkedNotCheckedClickedRegion.getTexture().dispose(); // 添加这行
        checkedCheckedRegion.getTexture().dispose();
        checkedClickedRegion.getTexture().dispose(); // 添加这行
        editRegion.getTexture().dispose();
        editClickedRegion.getTexture().dispose();
    }

}
