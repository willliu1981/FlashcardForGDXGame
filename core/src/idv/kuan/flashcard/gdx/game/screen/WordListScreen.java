package idv.kuan.flashcard.gdx.game.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
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

    public WordListScreen(Game game) {
        this.game = game;

        viewport = new StretchViewport(800, 400);
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);


// 創建一個 Table，用於布局項目
        Table table = new Table();

// 假設您有一個圖片的陣列和對應的文本

        String[] names = {"圖片1", "圖片2", "圖片3", "圖片4", "圖片5", "圖片6", "圖片7", "圖片8", "圖片9", "圖片10", "圖片11", "圖片12"
                , "圖片13", "圖片14", "圖片15", "圖片16", "圖片17", "圖片18", "圖片19", "圖片20", "圖片21", "圖片22", "圖片23", "圖片24"};


        // 假設你有三個圖片檔案在你的資源目錄
        String[] imagePaths = new String[]{
                "1.png",
                "2.png",
                "3.png",
                "4.png",
                "5.png",
                "6.png",
                "7.png",
                "8.png",
                "9.png",
                "10.png",
                "11.png",
                "12.png",
                "13.png",
                "14.png",
                "15.png",
                "16.png",
                "17.png",
                "18.png",
                "19.png",
                "20.png",
                "21.png",
                "22.png",
                "23.png",
                "24.png"
        };

// 創建一個 Texture 陣列來儲存圖片
        Texture[] images = new Texture[imagePaths.length];

// 循環遍歷圖片路徑並加載每個圖片為 Texture 對象
        for (int i = 0; i < imagePaths.length; i++) {
            images[i] = new Texture(Gdx.files.internal(imagePaths[i]));
        }


// 為每個項目創建一個行
        for (int i = 0; i < images.length; i++) {


            // 創建一個新的 Table 來放置圖片和文本，使其成為一個組合
            Table comboTable = new Table();

            // 創建圖片演員
            Image image = new Image(new TextureRegionDrawable(new TextureRegion(images[i])));
            // 添加圖片到組合 Table 並設定大小
            comboTable.add(image).size(100, 100).row(); // 添加圖片並在圖片下方開始新的一行

            // 添加文本標籤到組合 Table，居中對齊
            comboTable.add(new Label(names[i], MainScreen.skin)).padTop(10);

            // 將這個組合 Table 添加到主 Table 中
            table.add(comboTable).pad(10); // 為組合周圍添加間隔

            // 每三個單元格開始新的一行
            if ((i + 1) % 3 == 0) {
                table.row();
            }
        }

// 創建 ScrollPane
        ScrollPane scrollPane = new ScrollPane(table, MainScreen.skin);

// 設定 ScrollPane 的大小和位置
        scrollPane.setBounds(50, 50, 600, 300);

// 將 ScrollPane 添加到 Stage
        stage.addActor(scrollPane);

// 別忘了處理輸入
        Gdx.input.setInputProcessor(stage);
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
    }
}
