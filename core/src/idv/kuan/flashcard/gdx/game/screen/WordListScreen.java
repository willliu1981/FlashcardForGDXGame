package idv.kuan.flashcard.gdx.game.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class WordListScreen implements Screen {
    Game game;
    private Stage stage;
    Viewport viewport;

    TextureRegion checkedNotCheckedRegion;
    TextureRegion checkedCheckedRegion;
    TextureRegion editRegion;
    TextureRegion editClickedRegion;

    public WordListScreen(Game game) {
        this.game = game;

        viewport = new StretchViewport(800, 400);
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);


// 創建一個表格布局您的元素
        Table table = new Table();
        //table.setFillParent(true);

        // 加載圖片資源
        checkedNotCheckedRegion = new TextureRegion(new Texture("checkedboxnotchecked128.png"));
        checkedCheckedRegion = new TextureRegion(new Texture("checkedbox128.png"));
        editRegion = new TextureRegion(new Texture("edit128.png"));
        editClickedRegion = new TextureRegion(new Texture("editclicked128.png"));

        for (int i = 0; i < 10; i++) {
            // ... 省略其他代碼 ...

            ImageButton.ImageButtonStyle checkedboxStyle = new ImageButton.ImageButtonStyle();
            checkedboxStyle.up = new TextureRegionDrawable(checkedNotCheckedRegion); // 未選中時的圖片
            checkedboxStyle.down = new TextureRegionDrawable(checkedCheckedRegion); // 選中時的圖片


            final ImageButton checkedButton = new ImageButton(checkedboxStyle);


            Label termLabel = new Label("term_" + i, MainScreen.skin);
            Label translationLabel = new Label("translation_" + i, MainScreen.skin);

            ImageButton.ImageButtonStyle editStyle = new ImageButton.ImageButtonStyle();
            editStyle.up = new TextureRegionDrawable(editRegion); // 未點擊時的圖片
            editStyle.down = new TextureRegionDrawable(editClickedRegion); // 點擊時的圖片

            final ImageButton editButton = new ImageButton(editStyle);


            table.add(checkedButton).size(25,50);
            table.add(termLabel).width(200);
            table.add(translationLabel).width(200);
            table.add(editButton).size(50);

            table.row();

        }


        // 創建 ScrollPane
        ScrollPane scrollPane = new ScrollPane(table, MainScreen.skin);

// 設定 ScrollPane 的大小和位置
        scrollPane.setBounds(50, 50, 600, 300);

// 將 ScrollPane 添加到 Stage
        stage.addActor(scrollPane);

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
        stage.dispose();
        checkedNotCheckedRegion.getTexture().dispose();
        checkedCheckedRegion.getTexture().dispose();
        editRegion.getTexture().dispose();
        editClickedRegion.getTexture().dispose();
    }
}
