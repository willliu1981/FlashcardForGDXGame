package idv.kuan.flashcard.gdx.game.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.sql.SQLException;
import java.util.List;

import idv.kuan.flashcard.gdx.game.database.dao.WordDao;
import idv.kuan.flashcard.gdx.game.database.entity.Word;
import idv.kuan.flashcard.gdx.game.util.StyleUtil;
import idv.kuan.libs.utils.VersionHelper;

public class TestScreen implements Screen {
    private Stage stage;
    Viewport viewport;

    VersionHelper versionHelper;
    SpriteBatch batch;
    Texture img;

    TextField textField;
    TextField testTextField;

    public TestScreen() {
        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");

        viewport = new StretchViewport(800, 600);
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);


    }


    @Override
    public void show() {

        Table tb = new Table();
        tb.setFillParent(true); // 讓table的大小填滿父容器

// 假設你已經有一個Texture陣列，包含你要顯示的圖片
        Texture[] cardTextures = new Texture[]{new Texture("test/13.png"),
                new Texture("test/14.png"), new Texture("test/15.png"),
                new Texture("test/16.png"), new Texture("test/17.png"),
                new Texture("test/18.png"), new Texture("test/19.png"),
                new Texture("test/22.png"), new Texture("test/24.png")
        };

        Texture cardBackTexture = new Texture("test/s1.png");

        final int cardWidth = 96, cardHeight = 108, padding = 10;


        class Card {
            boolean isFlipedToFront = false;//背面
            TextureRegion frontTextureRegion;
            TextureRegion backTextureRegion;
            Word word;

            public Card(TextureRegion frontTextureRegion, TextureRegion backTextureRegion) {
                this.frontTextureRegion = frontTextureRegion;
                this.backTextureRegion = backTextureRegion;
            }

            public TextureRegion getCurrentTextureRegion() {
                return isFlipedToFront ? frontTextureRegion : backTextureRegion;
            }

            public void flipToFront(boolean flipToFront) {
                isFlipedToFront = flipToFront;
            }

            public boolean isFlipedToFront() {
                return isFlipedToFront;
            }

            public Word getWord() {
                return word;
            }

            public void setWord(Word word) {
                this.word = word;
            }

            @Override
            public String toString() {
                return "Card{" +
                        "isFlipedToFront=" + isFlipedToFront +
                        ", frontTextureRegion=" + frontTextureRegion +
                        ", backTextureRegion=" + backTextureRegion +
                        ", word=" + word +
                        '}';
            }
        }

        WordDao dao = new WordDao();
        List<Word> all = null;
        try {
            all = dao.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        StyleUtil.DynamicCharacters dynamicCharacters = new StyleUtil.DynamicCharacters();


// 設置表格為3行3列
        int size = 3;
        for (int i = 0, idx = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                // 創建一個圖像元件，並將其添加到表格中
                Word word = all.get(idx);


                if (idx > all.size()) {
                    idx = 0;
                } else {
                    idx++;
                }


                dynamicCharacters.add(word.getTranslation());
                BitmapFont font = StyleUtil.generateCustomFont(word.getTranslation(), 200);
                TextField.TextFieldStyle textFieldStyle = StyleUtil.generateDefaultTextFieldStyle(font);


                //front set
                TextureRegion textureRegion1 = null;
                {
                    Table table1 = new Table();
                    table1.setSize(cardWidth * 20, cardHeight * 10);
                    TextField textField1 = new TextField(word.getTranslation(), textFieldStyle);
                    textField1.setAlignment(Align.center);
                    int picW = cardTextures[i * size + j].getWidth();
                    int picH = cardTextures[i * size + j].getHeight();


                    Image imageFront = new Image(new TextureRegion(cardTextures[i * size + j], picW, picH));


                    table1.add(textField1).width(cardWidth * 20).height(cardHeight * 3).row();
                    table1.add(imageFront).width(cardWidth * 20).height(cardHeight * 7);


                    FrameBuffer frameBuffer1 = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
                    SpriteBatch batch1 = new SpriteBatch();

                    frameBuffer1.begin();
                    Gdx.gl.glClearColor(0, 0, 0, 0);
                    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                    batch1.begin();

                    table1.draw(batch1, 1);

                    batch1.end();
                    frameBuffer1.end();

                    Texture colorBufferTexture1 = frameBuffer1.getColorBufferTexture();
                    textureRegion1 = new TextureRegion(colorBufferTexture1);
                    textureRegion1.flip(false, true);

                }

                //back set
                TextureRegion textureRegion2 = null;
                {
                    Table table2 = new Table();
                    table2.setSize(cardWidth * 20, cardHeight * 10);
                    TextField textField2 = new TextField(word.getTerm(), textFieldStyle);
                    textField2.setAlignment(Align.center);

                    int picW = cardBackTexture.getWidth();
                    int picH = cardBackTexture.getHeight();

                    Image imageBack = new Image(new TextureRegion(cardBackTexture, picW, picH));


                    table2.add(textField2).width(cardWidth * 20).height(cardHeight * 3).row();
                    table2.add(imageBack).width(cardWidth * 20).height(cardHeight * 7);


                    FrameBuffer frameBuffer2 = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);

                    SpriteBatch batch2 = new SpriteBatch();

                    frameBuffer2.begin();
                    Gdx.gl.glClearColor(0, 0, 0, 0);
                    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                    batch2.begin();

                    table2.draw(batch2, 1);

                    batch2.end();
                    frameBuffer2.end();

                    Texture colorBufferTexture2 = frameBuffer2.getColorBufferTexture();
                    textureRegion2 = new TextureRegion(colorBufferTexture2);
                    textureRegion2.flip(false, true);
                }

                Card card = new Card(textureRegion1, textureRegion2);

                card.setWord(word);

                Image img = new Image(card.getCurrentTextureRegion());

                tb.add(img).width(cardWidth * 1.0f).height(cardHeight * 1.0f).pad(padding);

                // 设置卡片的原点为中心
                img.setOrigin(cardWidth / 2, cardHeight * 2 / 2);


                if (j == size - 1) {
                    tb.row(); // 在每列的最後添加一個新行
                }


                //*
                // 為卡片背面添加點擊監聽器，執行翻牌動畫
                img.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        float DT_TOBACK = 0.22f;
                        float DT_TOFRONT = 0.33f;

                        float minMoveDst = -5;
                        float maxMoveDst = 5;

                        float minRotateDst = -1f;
                        float maxRotateDst = 1f;

                        // 记录原始位置
                        final float originalX = img.getX();
                        final float originalY = img.getY();
                        // 首先將卡片縮小到0寬度，模擬卡片翻到邊緣的效果
                        img.addAction(

                                Actions.parallel(
                                        //move
                                        Actions.sequence(
                                                Actions.moveBy(MathUtils.random(minMoveDst / 20, maxMoveDst / 20)
                                                        , MathUtils.random(minMoveDst / 20, maxMoveDst / 20)
                                                        , (card.isFlipedToFront() ? DT_TOBACK : DT_TOFRONT)
                                                        , Interpolation.smooth)
                                                , Actions.moveBy(MathUtils.random(minMoveDst, maxMoveDst), MathUtils.random(minMoveDst, maxMoveDst)
                                                        , (card.isFlipedToFront() ? DT_TOBACK : DT_TOFRONT) - Math.min(DT_TOBACK, DT_TOFRONT) / 2
                                                        , Interpolation.bounceIn)
                                                , Actions.moveTo(originalX, originalY, Math.min(DT_TOBACK, DT_TOFRONT) / 2, Interpolation.bounceOut))

                                        //rotate
                                        , Actions.sequence(Actions.rotateBy(MathUtils.random(minRotateDst / 20, maxRotateDst / 20)
                                                        , (card.isFlipedToFront() ? DT_TOBACK : DT_TOFRONT)
                                                        , Interpolation.smooth)
                                                , Actions.rotateBy(MathUtils.random(minRotateDst, maxRotateDst)
                                                        , (card.isFlipedToFront() ? DT_TOBACK : DT_TOFRONT) - Math.min(DT_TOBACK, DT_TOFRONT) / 2
                                                        , Interpolation.bounceIn)
                                                , Actions.rotateTo(0, Math.min(DT_TOBACK, DT_TOFRONT) / 2, Interpolation.bounceOut))

                                        //alpha
                                        , Actions.sequence(Actions.alpha(0.0f, card.isFlipedToFront() ? DT_TOBACK : DT_TOFRONT)
                                                , Actions.alpha(1f, card.isFlipedToFront() ? DT_TOBACK / 2 : DT_TOFRONT / 2))

                                        //scale
                                        , Actions.sequence(
                                                Actions.scaleTo(0, 1, card.isFlipedToFront() ? DT_TOBACK : DT_TOFRONT
                                                        , Interpolation.circleOut), // 縮放動畫，持續0.5秒
                                                Actions.run(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        if (card.isFlipedToFront()) {
                                                            card.flipToFront(false);

                                                        } else {
                                                            card.flipToFront(true);

                                                        }

                                                        img.setDrawable(new TextureRegionDrawable(card.getCurrentTextureRegion()));

                                                    }
                                                })
                                                , Actions.scaleTo(1, 1, card.isFlipedToFront() ? DT_TOBACK : DT_TOFRONT
                                                        , (card.isFlipedToFront ? Interpolation.bounceOut : Interpolation.circleOut))
                                        )));


                    }
                });//*/


            }


// 將table添加到舞台上
            stage.addActor(tb);
        }
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
        viewport.update(width, height);
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
        img.dispose();
        stage.dispose();
    }
}
