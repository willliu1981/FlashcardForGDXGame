package idv.kuan.flashcard.gdx.game.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import idv.kuan.flashcard.gdx.game.screen.MainScreen;


public class TestScreen6 implements Screen {
    private Stage stage;
    Viewport viewport;
    SpriteBatch batch;


    final int cardWidth = 96, cardHeight = 108, padding = 10;


    public TestScreen6() {
        batch = new SpriteBatch();

        viewport = new StretchViewport(800, 600);
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);


    }


    @Override
    @SuppressWarnings("GDXJavaFlushInsideLoop")
    public void show() {
        Gdx.graphics.setWindowedMode(1920, 1080);

        Texture t1 = new Texture("test/13.png");
        Texture t2 = new Texture("test/14.png");
        Texture t3 = new Texture("test/15.png");

        FrameBuffer frameBuffer1 = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);

        //t1 --begin
        Table table1 = new Table();
        table1.background(new TextureRegionDrawable(new TextureRegion(new Texture("editclicked128.png"))));
        table1.setSize(cardWidth, cardHeight);

        Image i1 = new Image(new TextureRegion(t1));
        TextField field1 = new TextField("P1", MainScreen.skin);
        field1.setAlignment(Align.center);
        table1.add(i1).size(cardWidth*0.8f, cardHeight*0.6f).row();
        table1.add(field1).size(cardWidth*0.8f, cardHeight *0.2f);


        frameBuffer1.begin();
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();


        table1.draw(batch, 1);

        batch.end();
        frameBuffer1.end();

        Texture texture1 = frameBuffer1.getColorBufferTexture();

        TextureRegion textureRegion1 = new TextureRegion(texture1, cardWidth, cardHeight);
        textureRegion1.flip(false, true);

        Image ii1 = new Image(textureRegion1);

        //t1 --end


        //t2 ==begin
        Table table2 = new Table();
        table2.setSize(cardWidth, cardHeight);
        table2.background(new TextureRegionDrawable(new TextureRegion(new Texture("test/b1.png"))));

        Image i2 = new Image(new TextureRegion(t2));
        TextField field2 = new TextField("P2", MainScreen.skin);
        field2.setAlignment(Align.center);
        table2.add(i2).size(cardWidth, cardHeight - cardHeight / 3).pad(10).row();
        table2.add(field2).size(cardWidth, cardHeight / 3);


        FrameBuffer frameBuffer2 = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        frameBuffer2.begin();
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();


        table2.draw(batch, 1);

        batch.end();
        frameBuffer2.end();

        Texture texture2 = frameBuffer2.getColorBufferTexture();
        TextureRegion textureRegion2 = new TextureRegion(texture2, cardWidth, cardHeight);
        textureRegion2.flip(false, true);

        Image ii2 = new Image(textureRegion2);

        //t2 ==end

        Image ii3 = new Image(new TextureRegion(t3));

        Table outTable = new Table();
        outTable.background(new TextureRegionDrawable(new TextureRegion(new Texture("test/blue.png"))));
        outTable.setBounds(100, 100, 600, 300);
        //outTable.setFillParent(true);

        outTable.add(ii1).size(cardWidth, cardHeight).pad(padding);
        outTable.add(ii3).size(cardWidth, cardHeight).pad(padding);
        outTable.add(ii2).size(cardWidth, cardHeight).pad(padding);

        //Image i2 = new Image(new TextureRegion(t2));


        //table1.add(i2).size(cardWidth, cardHeight);


        Button button = new TextButton("change", MainScreen.skin);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                frameBuffer2.begin();
                Gdx.gl.glClearColor(0, 0, 0, 0);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                batch.begin();


                Table table3 = new Table();
                table3.setSize(cardWidth, cardHeight);

                Image i3 = new Image(new TextureRegion(t3));
                TextField field3 = new TextField("P3", MainScreen.skin);
                field3.setAlignment(Align.center);
                table3.add(i3).size(cardWidth, cardHeight - cardHeight / 3).row();
                table3.add(field3).size(cardWidth, cardHeight / 3);


                table3.draw(batch, 1);

                batch.end();
                frameBuffer2.end();

            }
        });


        stage.addActor(outTable);
        stage.addActor(button);
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
        stage.dispose();

    }
}
