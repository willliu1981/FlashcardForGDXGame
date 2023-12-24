package idv.kuan.flashcard.gdx.game.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import idv.kuan.flashcard.gdx.game.database.dao.WordDao;
import idv.kuan.flashcard.gdx.game.database.entity.Word;
import idv.kuan.flashcard.gdx.game.util.CardTextureUtil;
import idv.kuan.flashcard.gdx.game.util.SoundAction;
import idv.kuan.flashcard.gdx.game.util.StyleUtil;


public class TestScreen5 implements Screen {
    private Stage stage;
    Viewport viewport;
    SpriteBatch batch;
    Texture img;
    ArrayList<Card> cards = new ArrayList<>();


    final int cardWidth = 96, cardHeight = 108, padding = 10;


    public TestScreen5() {

        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");

        viewport = new StretchViewport(800, 600);
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);


    }

    interface TextureCreatorEquipable {

        TextureRegion getTextureRegion(String name);

        void createTextureRegion(String name, CardTextureUtil.CardTextureCreator.ITextureCreator iTextureCreator);

        void dispose();
    }

    class Card implements TextureCreatorEquipable {
        public static final String FRONT = "front";
        public static final String BACK = "back";
        boolean isFlipedToFront = false;//背面
        TextureRegion baseFrontTextureRegion;
        TextureRegion baseBackTextureRegion;
        Word word;
        Texture pic;

        Map<String, CardTextureUtil.CardTextureCreator> creators = new HashMap();

        public Card() {

        }

        public Card(TextureRegion frontTextureRegion, TextureRegion backTextureRegion) {
            this.baseFrontTextureRegion = frontTextureRegion;
            this.baseBackTextureRegion = backTextureRegion;
        }

        public TextureRegion getCurrentTextureRegion() {
            return isFlipedToFront ? this.creators.get(FRONT).getTextureRegion() : this.creators.get(BACK).getTextureRegion();
        }


        public void setCreator(String name, CardTextureUtil.CardTextureCreator creator) {
            this.creators.put(name, creator);
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

        public Texture getPic() {
            return pic;
        }

        public void setPic(Texture pic) {
            this.pic = pic;
        }

        public TextureRegion getBaseFrontTextureRegion() {
            return this.baseFrontTextureRegion;
        }

        public TextureRegion getBaseBackTextureRegion() {
            return this.baseBackTextureRegion;
        }

        @Override
        public TextureRegion getTextureRegion(String name) {
            return this.creators.get(name).getTextureRegion();
        }

        @Override
        public void createTextureRegion(String name, CardTextureUtil.CardTextureCreator.ITextureCreator iTextureCreator) {
            this.creators.get(name).createTextureRegion(iTextureCreator);

        }

        @Override
        public void dispose() {
            this.creators.values().forEach(CardTextureUtil.CardTextureCreator::dispose);
        }

        @Override
        public String toString() {
            return "Card{" +
                    "isFlipedToFront=" + isFlipedToFront +
                    ", word=" + word +
                    ", creators=" + creators +
                    '}';
        }


    }


    @Override
    @SuppressWarnings("GDXJavaFlushInsideLoop")
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


        Texture cardBackTexture = new Texture("test/b1.png");

        Texture questionTexture = new Texture("test/q3.png");
        Texture answerTexture = new Texture("test/a3.png");


        class QuestionCard extends Card {

            public QuestionCard(TextureRegion frontTextureRegion, TextureRegion backTextureRegion) {
                super(frontTextureRegion, backTextureRegion);
            }
        }


        class AnswerCard extends Card {

            public AnswerCard(TextureRegion frontTextureRegion, TextureRegion backTextureRegion) {
                super(frontTextureRegion, backTextureRegion);
            }
        }

        WordDao dao = new WordDao();
        List<Word> all = null;
        try {
            all = dao.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }


// 設置表格為3行3列
        int cols = 6;
        int rows = 4;
        int elementCount = cols * rows / 2;

        // 假設有一個String類型的list
        List<Word> originalList = all; // ... 您的列表;
        int n = 12; // 您想取出的元素數量

// 生成一個無限循環的列表流
        Stream<Word> infiniteStream = Stream.generate(() -> originalList)
                .flatMap(List::stream);

// 打亂並限制流的大小，然後收集到新的列表中
        List<Word> randomElements = infiniteStream
                .distinct()
                .limit(n)
                .collect(Collectors.toList());

// 如果打亂順序是必要的，可以在收集到新的列表後再次打亂
        Collections.shuffle(randomElements);

        StyleUtil.DynamicCharacters dynamicCharacters = new StyleUtil.DynamicCharacters();
        List<Card> baseCardList = new ArrayList<>();
        randomElements.forEach(x -> {

            Card qc = new QuestionCard(new TextureRegion(questionTexture), new TextureRegion(cardBackTexture));
            Texture texture = Math.random() > 0.5 ? cardTextures[(int) (Math.random() * 9)] : null;
            //qc.setPic(texture);
            Card ac = new AnswerCard(new TextureRegion(answerTexture), new TextureRegion(cardBackTexture));
            //ac.setPic(texture);

            qc.setWord(x);
            ac.setWord(x);

            baseCardList.add(qc);
            baseCardList.add(ac);
            dynamicCharacters.add(x.getTerm()).add(x.getTranslation());


        });

        //parameterSize 的值越大,字體紋理越大,適當縮放後越清晰,但BitmapFont能放的元素將越少
        //parameterSize 的值越大,fontSize 應當調整要越小
        //經測試後,parameterSize = 20,fontSize = 10 時,BitmapFont大約可容納2500個字元
        int fontSize = 10;
        BitmapFont font = StyleUtil.generateFontWithAddedChars(dynamicCharacters, 20);
        TextField.TextFieldStyle textFieldStyle = StyleUtil.generateDefaultTextFieldStyle(font);

        Collections.shuffle(baseCardList);


        for (int i = 0, idx1 = 0, indx2 = 0; i < rows; i++) {

            float scaleFactor = fontSize;//cardWidth * 20/fontSize

            for (int j = 0; j < cols; j++) {
                // 創建一個圖像元件，並將其添加到表格中
                Card c1 = baseCardList.get(idx1);
                idx1++;

                String msg = null;
                if (QuestionCard.class.isInstance(c1)) {
                    msg = c1.getWord().getTerm();
                } else if (AnswerCard.class.isInstance(c1)) {
                    msg = c1.getWord().getTranslation();
                }


                String finalMsg = msg;

                Card card = new Card();
                cards.add(card);

                //front with question
                if (QuestionCard.class.isInstance(c1)) {

                    card.setCreator(Card.FRONT, CardTextureUtil.getCardTextureCreator(batch).createTextureRegion(new CardTextureUtil.CardTextureCreator.ITextureCreator() {
                        @Override
                        public void createTexture(CardTextureUtil.TextureCreatorModel model) {
                            Table table = new Table();
                            table.setSize(cardWidth * 20 / scaleFactor, cardHeight * 10 / scaleFactor);
                            table.setBackground(new TextureRegionDrawable(c1.getBaseFrontTextureRegion()));

                            textFieldStyle.fontColor = Color.WHITE;
                            TextField textField1 = new TextField(finalMsg, textFieldStyle);
                            textField1.setAlignment(Align.center);

                            // 設定背景的Drawable為半透明
                            Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
                            pixmap.setColor(new Color(0.1f, 0.2f, 1, 0.01f)); // RGBA顏色，A是透明度
                            pixmap.fill();
                            TextureRegionDrawable backgroundDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
                            pixmap.dispose();

                            textFieldStyle.background = backgroundDrawable;

                            if (c1.getPic() != null) {
                                Image imageFront = new Image(new TextureRegion(c1.getPic()));
                                table.add(imageFront).width(cardWidth * 20 / scaleFactor * 0.8f).height(cardHeight * 8 / scaleFactor * 0.8f);
                                table.row();

                            }

                            table.add(textField1).size(cardWidth * 20 / scaleFactor * 0.8f, cardHeight * 2 / scaleFactor);

                            model.setDrawTarget(table, (int) (cardWidth * 20 / scaleFactor), (int) (cardHeight * 10 / scaleFactor));

                        }
                    }));

                } else if (AnswerCard.class.isInstance(c1)) {
                    //front with answer
                    card.setCreator(Card.FRONT, CardTextureUtil.getCardTextureCreator(batch).createTextureRegion(new CardTextureUtil.CardTextureCreator.ITextureCreator() {
                        @Override
                        public void createTexture(CardTextureUtil.TextureCreatorModel model) {
                            Table table = new Table();
                            table.setSize(cardWidth * 20 / scaleFactor, cardHeight * 10 / scaleFactor);
                            table.setBackground(new TextureRegionDrawable(c1.getBaseFrontTextureRegion()));

                            textFieldStyle.fontColor = Color.BLACK;
                            TextField textField1 = new TextField(finalMsg, textFieldStyle);
                            textField1.setAlignment(Align.center);

                            // 設定背景的Drawable為半透明
                            Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
                            pixmap.setColor(new Color(1, 1, 0.9f, 0.01f)); // RGBA顏色，A是透明度
                            pixmap.fill();
                            TextureRegionDrawable backgroundDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
                            pixmap.dispose();

                            textFieldStyle.background = backgroundDrawable;


                            table.add(textField1).size(cardWidth * 20 / scaleFactor * 0.8f, cardHeight * 2 / scaleFactor);
                            if (c1.getPic() != null) {
                                Image imageFront = new Image(new TextureRegion(c1.getPic()));
                                table.row();
                                table.add(imageFront).width(cardWidth * 20 / scaleFactor * 0.8f).height(cardHeight * 8 / scaleFactor * 0.8f);

                            }

                            model.setDrawTarget(table, (int) (cardWidth * 20 / scaleFactor), (int) (cardHeight * 10 / scaleFactor));


                        }
                    }));
                }

                //back
                card.setCreator(Card.BACK, CardTextureUtil.getCardTextureCreator(batch).createTextureRegion(new CardTextureUtil.CardTextureCreator.ITextureCreator() {
                    @Override
                    public void createTexture(CardTextureUtil.TextureCreatorModel model) {
                        Table table = new Table();
                        table.setSize(cardWidth * 20, cardHeight * 10);

                        Image imageBack = new Image(c1.getBaseBackTextureRegion());

                        table.add(imageBack).width(cardWidth * 20).height(cardHeight * 10);

                        model.setDrawTarget(table);
                    }
                }));

                card.setWord(c1.getWord());

                Image img = new Image(card.getCurrentTextureRegion());


                tb.add(img).width(cardWidth * 1f).height(cardHeight * 1f).pad(padding);


                // 设置卡片的原点为中心
                img.setOrigin(cardWidth / 2, cardHeight * 2 / 2);


                if (j == cols - 1) {
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


                        // 加載音效
                        if (card.isFlipedToFront) {
                            //to front


                            Sound mySound = Gdx.audio.newSound(Gdx.files.internal("sounds/re_flip1.wav"));
                            long soundId = mySound.play(); // 需要保存这个ID来稍后控制音效
                            float soundDuration = 0.3f; // 声音应该在2秒内从无到有

// 创建一个曲线插值，例如线性或加速插值
                            Interpolation interpolation = Interpolation.circleIn;

// 创建并添加Action到某个Actor，这样它就会执行
                            SoundAction soundAction = new SoundAction(mySound, soundId, soundDuration, 0.2f, 0.7f, interpolation);
                            img.addAction(soundAction);

                        } else {
                            //to back


                            Sound mySound = Gdx.audio.newSound(Gdx.files.internal("sounds/flip1.wav"));
                            long soundId = mySound.play(); // 需要保存这个ID来稍后控制音效
                            float soundDuration = 0.3f; // 声音应该在2秒内从无到有

// 创建一个曲线插值，例如线性或加速插值
                            Interpolation interpolation = Interpolation.circleIn;

// 创建并添加Action到某个Actor，这样它就会执行
                            SoundAction soundAction = new SoundAction(mySound, soundId, soundDuration, 0.5f, 0.1f, interpolation);
                            img.addAction(soundAction);
                        }


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


        }

// 將table添加到舞台上
        stage.addActor(tb);

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
        cards.forEach(Card::dispose);
    }
}
