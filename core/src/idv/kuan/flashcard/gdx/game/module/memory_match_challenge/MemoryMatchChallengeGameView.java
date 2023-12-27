package idv.kuan.flashcard.gdx.game.module.memory_match_challenge;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import idv.kuan.flashcard.gdx.game.database.dao.WordDao;
import idv.kuan.flashcard.gdx.game.database.entity.Word;
import idv.kuan.flashcard.gdx.game.module.CardHandle;
import idv.kuan.flashcard.gdx.game.module.GameView;
import idv.kuan.flashcard.gdx.game.util.CardTextureUtil;
import idv.kuan.flashcard.gdx.game.util.SoundAction;
import idv.kuan.flashcard.gdx.game.util.StyleUtil;
import idv.kuan.libs.interfaces.observers.Observer;
import idv.kuan.libs.interfaces.observers.Subject;

public class MemoryMatchChallengeGameView extends GameView implements Subject<Action> {
    final private static int CLICKLISTENER_ID_FOR_SOUNDACTION = 1;
    final private static int CLICKLISTENER_ID_FOR_FLIPACTION = 2;
    final static int CARD_WIDTH = 100, CARD_HEIGHT = 100, PADDING = 5;
    final float ANIM_DURATIONTIME = 0.75f;

    private List<Observer<Action>> actionObservers;
    private Action observerAction;

    private List<DefCardHandle> cardHandles;
    private final int CARDCOUNT = 12;
    private TextureRegion blackCardTexReg;
    private Texture backCardAminsTexture;

    //selected card
    static private DefCardHandle firstCard = null;
    static private DefCardHandle secondCard = null;


    //observers --begin
    @Override
    public List<Observer<Action>> getActionObservers() {
        return this.actionObservers;
    }

    @Override
    public void setDataAndNotifyObservers(Action data) {
        this.observerAction = data;

        if (this.actionObservers.size() == 2) {
            this.actionObservers.forEach(o -> {
                this.onCardSelected(this.stage, ((DefCardHandle) o));

            });
            this.actionObservers.clear();
        }
        Subject.super.setDataAndNotifyObservers(data);

    }

    @Override
    public Action getData() {
        return this.observerAction;
    }
    //observers --end

    private static class IdentifiedClickListener extends ClickListener {

        int identifier;

        public IdentifiedClickListener(int identifier) {
            this.identifier = identifier;
        }

        public int getIdentifier() {
            return identifier;
        }
    }

    //DefCardHandle --begin
    protected static class DefCardHandle extends CardHandle implements Observer<Action> {
        private MemoryMatchChallengeGameView view;
        private TextureRegion frontBackgroundTexReg;
        private TextureRegion backBackgroundTexReg;
        private Sound flipToFrontSound;
        private Sound flipToBackSound;
        private boolean isFrontState;
        private boolean isMatchFinish;


        //selected card
        static private DefCardHandle firstCard = null;
        static private DefCardHandle secondCard = null;


        public DefCardHandle(MemoryMatchChallengeGameView view, Image background,
                             TextureRegion frontBackgroundTexReg, TextureRegion backBackgroundTexReg,
                             String flipToFrontSoundFilePath, String flipToFBackSoundFilePath) {
            super(background);
            this.view = view;
            this.frontBackgroundTexReg = frontBackgroundTexReg;
            this.backBackgroundTexReg = backBackgroundTexReg;
            this.flipToFrontSound = Gdx.audio.newSound(Gdx.files.internal(flipToFrontSoundFilePath));
            this.flipToBackSound = Gdx.audio.newSound(Gdx.files.internal(flipToFBackSoundFilePath));
            this.isFrontState = false;
            this.isMatchFinish = false;
        }

        //DefCardHandle initialize --begin
        @Override
        protected void initialize() {
            // 设置卡片的原点为中心
            this.background.setOrigin(CARD_WIDTH / 2, CARD_HEIGHT * 2 / 2);

            //sound listener --begin
            this.background.addListener(new IdentifiedClickListener(MemoryMatchChallengeGameView.CLICKLISTENER_ID_FOR_SOUNDACTION) {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);

                    if (DefCardHandle.this.isFrontState) {
                        DefCardHandle.this.playFlipToFrontWithSoundAction();
                    } else {
                        DefCardHandle.this.playFlipToBackWithSoundAction();
                    }
                }
            });
            //sound --end

            //flip listener ==begin
            this.background.addListener(new IdentifiedClickListener(MemoryMatchChallengeGameView.CLICKLISTENER_ID_FOR_FLIPACTION) {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);

                    float DT_TOBACK = 0.22f;
                    float DT_TOFRONT = 0.33f;

                    float minMoveDst = -5;
                    float maxMoveDst = 5;

                    float minRotateDst = -1f;
                    float maxRotateDst = 1f;

                    // 记录原始位置
                    final float originalX = DefCardHandle.this.getBackground().getX();
                    final float originalY = DefCardHandle.this.getBackground().getY();

                    view.registerObserver(DefCardHandle.this);
                    view.setDataAndNotifyObservers(new TestActon(DefCardHandle.this.getWord().getTerm()));

                    DefCardHandle.this.background.addAction(

                            Actions.parallel(
                                    //move
                                    Actions.sequence(
                                            Actions.moveBy(MathUtils.random(minMoveDst / 20, maxMoveDst / 20)
                                                    , MathUtils.random(minMoveDst / 20, maxMoveDst / 20)
                                                    , (DefCardHandle.this.isFrontState ? DT_TOBACK : DT_TOFRONT)
                                                    , Interpolation.smooth)
                                            , Actions.moveBy(MathUtils.random(minMoveDst, maxMoveDst), MathUtils.random(minMoveDst, maxMoveDst)
                                                    , (DefCardHandle.this.isFrontState ? DT_TOBACK : DT_TOFRONT) - Math.min(DT_TOBACK, DT_TOFRONT) / 2
                                                    , Interpolation.bounceIn)
                                            , Actions.moveTo(originalX, originalY, Math.min(DT_TOBACK, DT_TOFRONT) / 2, Interpolation.bounceOut))

                                    //rotate
                                    , Actions.sequence(Actions.rotateBy(MathUtils.random(minRotateDst / 20, maxRotateDst / 20)
                                                    , (DefCardHandle.this.isFrontState ? DT_TOBACK : DT_TOFRONT)
                                                    , Interpolation.smooth)
                                            , Actions.rotateBy(MathUtils.random(minRotateDst, maxRotateDst)
                                                    , (DefCardHandle.this.isFrontState ? DT_TOBACK : DT_TOFRONT) - Math.min(DT_TOBACK, DT_TOFRONT) / 2
                                                    , Interpolation.bounceIn)
                                            , Actions.rotateTo(0, Math.min(DT_TOBACK, DT_TOFRONT) / 2, Interpolation.bounceOut))

                                    //alpha
                                    , Actions.sequence(Actions.alpha(0.0f, DefCardHandle.this.isFrontState ? DT_TOBACK : DT_TOFRONT)
                                            , Actions.alpha(1f, DefCardHandle.this.isFrontState ? DT_TOBACK / 2 : DT_TOFRONT / 2))

                                    //scale
                                    , Actions.sequence(
                                            Actions.scaleTo(0, 1, DefCardHandle.this.isFrontState ? DT_TOBACK : DT_TOFRONT
                                                    , Interpolation.circleOut), // 縮放動畫，持續0.5秒
                                            Actions.run(new Runnable() {
                                                @Override
                                                public void run() {
                                                    DefCardHandle.this.changeFlipedState();

                                                    DefCardHandle.this.getBackground().setDrawable(new TextureRegionDrawable(DefCardHandle.this.getCurrentTextureRegion()));

                                                }
                                            })
                                            , Actions.scaleTo(1, 1, DefCardHandle.this.isFrontState ? DT_TOBACK : DT_TOFRONT
                                                    , (DefCardHandle.this.isFrontState ? Interpolation.bounceOut : Interpolation.circleOut))
                                    )));
                    //flip listener ==end

                }
            });


        }
        //DefCardHandle initialize --end


        private void finishMatch() {
            this.isMatchFinish = true;
        }


        private TextureRegion getCurrentTextureRegion() {
            return this.isFrontState ? this.frontBackgroundTexReg : this.backBackgroundTexReg;
        }

        private void changeFlipedState() {
            this.isFrontState = !this.isFrontState;
        }

        private SoundAction playFlipToFrontWithSoundAction() {
            SoundAction soundAction = new SoundAction(flipToFrontSound);
            soundAction.setVolume(0.33f);
            background.addAction(soundAction);
            return soundAction;
        }

        private SoundAction playFlipToBackWithSoundAction() {
            SoundAction soundAction = new SoundAction(flipToBackSound);
            soundAction.setVolume(0.33f);
            background.addAction(soundAction);
            return soundAction;
        }

        public boolean isFrontState() {
            return isFrontState;
        }

        public Image getBackground() {
            return background;
        }


        @Override
        public Subject getSubject() {
            return this.view;
        }

        @Override
        public void update(Action data) {
            // Gdx.app.log("MMCG", "update data=" + ((TestActon) data).toString());
        }
    }
    //DefCardHandle --end

    //MemoryMatchChallengeGameView initialize --begin
    @Override
    public void initialize() {
        //init texture --begin
        blackCardTexReg = new TextureRegion(new Texture("test/b0.png"));
        TextureRegion cardBackTexReg = new TextureRegion(new Texture("test/b1.png"));
        TextureRegion questionTexReg = new TextureRegion(new Texture("test/q4.png"));
        TextureRegion answerTexReg = new TextureRegion(new Texture("test/a4.png"));

        backCardAminsTexture = new Texture("test/bs3.png");


        //init texture --end

        actionObservers = new ArrayList<>();
        cardHandles = new ArrayList<>();

        float scaleFactor = 15;

        //style --begin
        StyleUtil.DynamicCharacters dynamicCharacters = new StyleUtil.DynamicCharacters();
        WordDao dao = new WordDao();
        List<Word> all = null;
        try {
            all = dao.findAll();
            all = all.stream().distinct().limit(CARDCOUNT).collect(Collectors.toList());
            all.forEach(x -> dynamicCharacters.add(x.getTranslation()));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Label.LabelStyle labelStyle = StyleUtil.generateDefaultLabelStyle(
                StyleUtil.generateFontWithAddedChars(dynamicCharacters, 20));

        //style --end

        //create cardHandle ==begin
        try {
            for (Word word : all) {

                //question --begin
                CardTextureUtil.CardTextureCreator questionCardTextureCreator = CardTextureUtil.getCardTextureCreator(batch);
                questionCardTextureCreator.createTextureRegion(new CardTextureUtil.CardTextureCreator.ITextureCreator() {
                    @Override
                    public void createTexture(CardTextureUtil.TextureCreatorModel model) {
                        Table table = new Table();
                        table.setSize(CARD_WIDTH * 20 / scaleFactor, CARD_HEIGHT * 10 / scaleFactor);
                        table.setBackground(new TextureRegionDrawable(questionTexReg));

                        //書寫文字 --begin
                        labelStyle.fontColor = Color.YELLOW;
                        Label label = new Label(word.getTerm(), labelStyle);

                        // 設定Label的一些屬性
                        label.setAlignment(Align.center);
                        label.setWrap(true);
                        table.add(label).expand().fill();

                        //書寫文字 --end


                        model.setDrawTarget(table, (int) (CARD_WIDTH * 20 / scaleFactor), (int) (CARD_HEIGHT * 10 / scaleFactor));
                    }
                });
                DefCardHandle questionCardHandle = new DefCardHandle(this, new Image(blackCardTexReg),
                        questionCardTextureCreator.getTextureRegion(), cardBackTexReg,
                        "sounds/water004.wav", "sounds/water002.wav");
                //question --end

                //answer  ==begin
                CardTextureUtil.CardTextureCreator answerCardTextureCreator = CardTextureUtil.getCardTextureCreator(batch);
                answerCardTextureCreator.createTextureRegion(new CardTextureUtil.CardTextureCreator.ITextureCreator() {
                    @Override
                    public void createTexture(CardTextureUtil.TextureCreatorModel model) {
                        Table table = new Table();
                        table.setSize(CARD_WIDTH * 20 / scaleFactor, CARD_HEIGHT * 10 / scaleFactor);
                        table.setBackground(new TextureRegionDrawable(answerTexReg));

                        //書寫文字 --begin
                        labelStyle.fontColor = Color.RED;
                        Label label = new Label(word.getTranslation(), labelStyle);

                        // 設定Label的一些屬性
                        label.setAlignment(Align.center);
                        label.setWrap(true);
                        table.add(label).expand().fill();

                        //書寫文字 --end

                        model.setDrawTarget(table, (int) (CARD_WIDTH * 20 / scaleFactor), (int) (CARD_HEIGHT * 10 / scaleFactor));
                    }
                });
                DefCardHandle answerCardHandle = new DefCardHandle(this, new Image(blackCardTexReg),
                        answerCardTextureCreator.getTextureRegion(), cardBackTexReg,
                        "sounds/water004.wav", "sounds/water002.wav");
                //answer ==end


                questionCardHandle.setWord(word);
                answerCardHandle.setWord(word);
                cardHandles.add(questionCardHandle);
                cardHandles.add(answerCardHandle);

            }

            Collections.shuffle(cardHandles);
            //create cardHandle ==end

        } catch (Exception e) {
            e.printStackTrace();
        }

        createCard();

    }
    //MemoryMatchChallengeGameView initialize --end

    private void createCard() {

        Table table = new Table();
        table.setFillParent(true);

        SequenceAction sequenceAction = new SequenceAction();
        Animation<TextureRegion> animation = buildCardCreateAmintions(backCardAminsTexture, ANIM_DURATIONTIME);


        for (int idx = 0, row = 0; row < 4; row++) {
            for (int col = 0; col < 6; col++, idx++) {
                DefCardHandle cardHandle = cardHandles.get(idx);
                Image img = cardHandle.getBackground();

                RunnableAction runnableAction = new RunnableAction();
                runnableAction.setRunnable(new Runnable() {
                    @Override
                    public void run() {
                        Sound sound = Gdx.audio.newSound(Gdx.files.internal("sounds/water001.wav"));
                        cardCreatedActions(img, animation);
                        soundAction(img, sound, 1.0f, 1.5f, 1.0f, Interpolation.smooth);

                    }
                });


                sequenceAction.addAction(runnableAction);
                table.add(img).size(CARD_WIDTH, CARD_HEIGHT).pad(PADDING);
                sequenceAction.addAction(new DelayAction(0.05f));
            }
            table.row();
        }

        stage.addAction(sequenceAction);
        stage.addActor(table);
    }

    private Animation<TextureRegion> buildCardCreateAmintions(Texture texture, float duration) {
        int FRAME_COUNT = 8;
        int FRAME_WIDTH = 80;
        int FRAME_HEIGHT = 80;
        float FRAME_DURATION = duration / FRAME_COUNT;


        // 创建一个TextureRegion数组，其中包含所有动画帧
        TextureRegion[] aminFrames = new TextureRegion[FRAME_COUNT];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 4; j++) {
                aminFrames[i * 4 + j] = new TextureRegion(texture
                        , j * FRAME_WIDTH, i * FRAME_HEIGHT, FRAME_WIDTH, FRAME_HEIGHT);
            }
        }

        // 使用这些帧创建一个Animation对象
        return new Animation<>(FRAME_DURATION, aminFrames);
    }

    private void cardCreatedActions(Image img, Animation<TextureRegion> animation) {

        AminAction aminAction = new AminAction(img, animation, ANIM_DURATIONTIME, Interpolation.fade);
        img.addAction(aminAction);
    }

    //card selected ==begin
    public void onCardSelected(Stage stage, DefCardHandle card) {
        if (firstCard == null) {
            firstCard = card;
            // 翻開卡片
        } else if (secondCard == null && card != firstCard) {
            secondCard = card;
            // 翻開第二張卡片並檢查配對
            checkForMatch(stage);
        }
    }

    private void checkForMatch(Stage stage) {
        final float DELAY_TIME = 3.0f;
        if (firstCard.getWord().getId() == secondCard.getWord().getId()) {
            // 配對成功
            firstCard = null;
            secondCard = null;
        } else {
            // 延遲一段時間後翻回卡片
            delayAndAct(stage, () -> {
                triggerClick(firstCard.isFrontState, firstCard.getBackground());
                triggerClick(secondCard.isFrontState, secondCard.getBackground());
                firstCard = null;
                secondCard = null;
            }, DELAY_TIME);
        }
    }

    // 這是一個延遲執行動作的方法
    private void delayAndAct(Stage stage, Runnable action, float delayTime) {
        SequenceAction sequence = Actions.sequence(Actions.delay(delayTime), Actions.run(action));
        stage.addAction(sequence);
    }

    //card selected ==end


    private void triggerClick(boolean isFrontState, Actor actor) {
        if (isFrontState) {
            // 遍歷actor的所有事件監聽器
            for (EventListener listener : actor.getListeners()) {
                // 檢查是否為ClickListener
                if (listener instanceof IdentifiedClickListener) {
                    // 強制轉換為ClickListener
                    IdentifiedClickListener clickListener = (IdentifiedClickListener) listener;
                    if (clickListener.getIdentifier() == MemoryMatchChallengeGameView.CLICKLISTENER_ID_FOR_SOUNDACTION
                            || clickListener.getIdentifier() == MemoryMatchChallengeGameView.CLICKLISTENER_ID_FOR_FLIPACTION) {
                        // 觸發點擊事件，這裡的參數可以根據需要調整
                        clickListener.clicked(null, actor.getX(), actor.getY());
                    }
                }
            }
        }


    }

    private void soundAction(Image img, Sound sound, float soundDuration, float pitchStart, float pitchEnd, Interpolation interpolation) {
        // 加載音效


        long soundId = sound.play(); // 需要保存这个ID来稍后控制音效

        // 创建一个曲线插值，例如线性或加速插值

        // 创建并添加Action到某个Actor，这样它就会执行
        SoundAction soundAction = new SoundAction(sound, soundId, soundDuration, pitchStart, pitchEnd, interpolation);
        soundAction.setVolume(0.33f);
        img.addAction(soundAction);

    }

    public class AnimatedActor extends Actor {
        private Animation<TextureRegion> animation;
        private float stateTime = 0;

        public AnimatedActor(Animation<TextureRegion> animation) {
            this.animation = animation;
        }

        @Override
        public void act(float delta) {
            super.act(delta);
            // 更新状态时间
            stateTime += delta;

            // 这里还可以添加条件，以触发或修改动画
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            // 获取当前应显示的帧
            TextureRegion currentFrame = animation.getKeyFrame(stateTime, false);

            // 考虑到Actor的位置和父Alpha值绘制帧
            Color color = getColor(); // 应用父Alpha值(透明度)
            batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

            if (currentFrame != null) {
                batch.draw(currentFrame, getX(), getY(), getOriginX(), getOriginY(),
                        getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
            }
        }
    }


    public class AminAction extends Action {
        private Image img;
        Animation<TextureRegion> animation;
        private float duration;
        private float elapsedTime;
        private Interpolation interpolation;


        public AminAction(Image img, Animation<TextureRegion> animation, float duration, Interpolation interpolation) {
            this.img = img;
            this.animation = animation;
            this.duration = duration;
            this.elapsedTime = 0;
            this.interpolation = interpolation;
        }

        @Override
        public boolean act(float delta) {
            elapsedTime += delta;

            if (elapsedTime >= duration) {
                elapsedTime = duration;
            }

            TextureRegion keyFrame = animation.getKeyFrame(elapsedTime);
            img.setDrawable(new TextureRegionDrawable(keyFrame));
            return elapsedTime >= duration; // Action not completed yet
        }
    }

    public static class TestActon extends Action {
        private String name;

        public TestActon(String name) {
            this.name = name;
        }

        @Override
        public boolean act(float delta) {
            return false;
        }

        @Override
        public String toString() {
            return "TestActon{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }
}

