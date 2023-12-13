package idv.kuan.flashcard.gdx.game.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import idv.kuan.flashcard.gdx.game.util.StyleUtil;

public class TestScreen3 extends ScreenAdapter {
    private Stage stage;
    private FrameBuffer frameBuffer;
    private SpriteBatch spriteBatch;
    private BitmapFont bitmapFont;

    StyleUtil.DynamicCharacters dynamicCharacters;

    @Override
    public void show() {
        spriteBatch = new SpriteBatch();
        stage = new Stage(new ScreenViewport(), spriteBatch);


        dynamicCharacters = new StyleUtil.DynamicCharacters();

        dynamicCharacters.add("功能1,訊息1");
        BitmapFont font = StyleUtil.generateDefaultDynamicFont(dynamicCharacters.getCharacters(), 80);
        Table tb = new Table();
        tb.setFillParent(true);
        TextField.TextFieldStyle style = StyleUtil.generateDefaultTextFieldStyle(font);
        style.font.getData().setScale(2.0f,1.0f);



        for (int i = 1; i <= 3; i++) {


            Table table = new Table();


            TextField txtf1 = new TextField("功能1", style);
            TextField txtf2 = new TextField("訊息1", style);
            txtf1.setAlignment(Align.center);
            txtf2.setAlignment(Align.center);


            table.add(txtf1).width(1100).height(100).row();
            table.add(txtf2).width(1100).height(100);
            table.setBounds(0, 0, 1200, 300);

            // 創建FrameBuffer
            frameBuffer = new FrameBuffer(Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);

            // 開始FrameBuffer進行繪製
            frameBuffer.begin();
            Gdx.gl.glClearColor(0, 0, 0, 0);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            spriteBatch.begin();
            // 繪製文字


            table.draw(spriteBatch, 1f);


            // 如果有其它圖片也可以在這裡繪製
            spriteBatch.end();
            frameBuffer.end();

            // 獲取FrameBuffer的Texture
            Texture texture = frameBuffer.getColorBufferTexture();
            // 创建TextureRegion并翻转Y轴
            TextureRegion textureRegion = new TextureRegion(texture);
            textureRegion.flip(false, true); // 不翻转X轴，翻转Y轴

            // 創建Image物件並將其添加到舞台上
            Image image = new Image(textureRegion);


            image.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    // 点击后将FrameBuffer的Texture翻转并设置给Image
                    //TextureRegion textureRegion = new TextureRegion(texture);
                    //textureRegion.flip(false, true); // 翻轉Y軸
                    image.setDrawable(new TextureRegionDrawable(textureRegion));
                    image.addAction(Actions.sequence(
                            Actions.scaleTo(0, 1, 0.5f),
                            Actions.scaleTo(1, 1, 0.5f)
                    ));
                }
            });

            tb.add(image);
        }

        stage.addActor(tb);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        frameBuffer.dispose();
        spriteBatch.dispose();
        bitmapFont.dispose();
    }
}
