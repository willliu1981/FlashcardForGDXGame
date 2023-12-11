package idv.kuan.flashcard.gdx.game.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

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

        Table table = new Table();
        table.setFillParent(true); // 讓table的大小填滿父容器

// 假設你已經有一個Texture陣列，包含你要顯示的圖片
        Texture[] cardTextures = new Texture[]{new Texture("test/13.png"),
                new Texture("test/14.png"), new Texture("test/15.png"),
                new Texture("test/16.png"), new Texture("test/17.png"),
                new Texture("test/18.png"), new Texture("test/19.png"),
                new Texture("test/22.png"), new Texture("test/24.png")
        };

        Texture cardBackTexture = new Texture("test/1.png");

        final int cardWidth = 100, cardHeight = 100, padding = 10;


        class Card {
            boolean isFlipedToFront = false;//背面
            TextureRegion frontTextureRegion;
            TextureRegion backTextureRegion;

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


        }

// 設置表格為3行3列
        int size = 3;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                // 創建一個圖像元件，並將其添加到表格中


                Card card = new Card(new TextureRegion(cardTextures[i * size + j]), new TextureRegion(cardBackTexture));
                Image image = new Image(card.getCurrentTextureRegion().getTexture());

                table.add(image).width(cardWidth).height(cardHeight).pad(padding);

                // 设置卡片的原点为中心
                image.setOrigin(cardWidth / 2, cardHeight / 2);


                if (j == size - 1) {
                    table.row(); // 在每列的最後添加一個新行
                }


                //*
                // 為卡片背面添加點擊監聽器，執行翻牌動畫
                image.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        float DT_TOBACK = 0.22f;
                        float DT_TOFRONT = 0.33f;

                        float minMoveDst = -5;
                        float maxMoveDst = 5;

                        float minRotateDst = -1f;
                        float maxRotateDst = 1f;

                        // 记录原始位置
                        final float originalX = image.getX();
                        final float originalY = image.getY();
                        // 首先將卡片縮小到0寬度，模擬卡片翻到邊緣的效果
                        image.addAction(

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

                                                        image.setDrawable(new TextureRegionDrawable(card.getCurrentTextureRegion()));

                                                    }
                                                })
                                                , Actions.scaleTo(-1, 1, card.isFlipedToFront() ? DT_TOBACK : DT_TOFRONT
                                                        , (card.isFlipedToFront ? Interpolation.bounceOut : Interpolation.circleOut))
                                        )));


                    }
                });//*/


            }
            //*
            class BorderedTable extends Table {
                private ShapeRenderer shapeRenderer;

                public BorderedTable() {
                    shapeRenderer = new ShapeRenderer();
                }

                @Override
                public void draw(Batch batch, float parentAlpha) {
                    super.draw(batch, parentAlpha);
                    batch.end();

                    // 设置ShapeRenderer的投影矩阵
                    shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
                    shapeRenderer.setTransformMatrix(batch.getTransformMatrix());

                    // 绘制边框
                    shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                    shapeRenderer.setColor(com.badlogic.gdx.graphics.Color.BLACK);
                    shapeRenderer.rect(getX(), getY(), getWidth(), getHeight());
                    shapeRenderer.end();

                    // 在使用ShapeRenderer之后重新开始批处理
                    batch.begin();
                }
            }//*/


            Texture t1 = new Texture("test/1.png");
            Image i1 = new Image(t1);
            //BorderedTable tb1 = new BorderedTable();
            Table tb1 = new Table();
            tb1.setSize(200, 200);
            tb1.add(i1).width(cardWidth).height(cardHeight).pad(padding);

            NinePatch patch = new NinePatch(new Texture("test/14.png"), 0, 0, cardWidth, cardHeight);
            NinePatchDrawable background = new NinePatchDrawable(patch);
            tb1.setBackground(background);

            //tb1.setFillParent(true);

            tb1.addAction(Actions.scaleTo(20f, 20f, 3f));

            tb1.setOrigin(Align.center);


// 將table添加到舞台上
            stage.addActor(table);
            //stage.addActor(tb1);
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
