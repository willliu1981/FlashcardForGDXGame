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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

public class MemoryMatchChallengeGameView extends GameView implements Subject<DefCardHandle> {
    final static int CARD_WIDTH = 100, CARD_HEIGHT = 100, PADDING = 5;
    final float ANIM_DURATIONTIME = 0.75f;

    private List<Observer<DefCardHandle>> cardhandleObservers;

    private List<DefCardHandle> cardHandles;
    private final int CARDCOUNT = 12;
    private TextureRegion blackCardTexReg;
    private Texture backCardAminsTexture;


    //MemoryMatchChallengeGameView initialize --begin
    @Override
    public void initialize() {
        //init texture --begin
        blackCardTexReg = new TextureRegion(new Texture("test/b0.png"));
        TextureRegion cardBackTexReg = new TextureRegion(new Texture("test/b1.png"));
        TextureRegion questionTexReg = new TextureRegion(new Texture("test/q4.png"));
        TextureRegion answerTexReg = new TextureRegion(new Texture("test/a4.png"));
        TextureRegion matchedQuestionTexReg = new TextureRegion(new Texture("test/matched_q4.png"));
        TextureRegion matchedAnswerTexReg = new TextureRegion(new Texture("test/matched_a4.png"));
        TextureRegion darkMatchedQuestionTexReg = new TextureRegion(new Texture("test/dark_matched_q4.png"));
        TextureRegion darkMatchedAnswerTexReg = new TextureRegion(new Texture("test/dark_matched_a4.png"));

        backCardAminsTexture = new Texture("test/bs3.png");


        //init texture --end

        cardhandleObservers = new ArrayList<>();
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
                //common
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

                //matched
                CardTextureUtil.CardTextureCreator matchedQuestionCardTextureCreator = CardTextureUtil.getCardTextureCreator(batch);
                matchedQuestionCardTextureCreator.createTextureRegion(new CardTextureUtil.CardTextureCreator.ITextureCreator() {
                    @Override
                    public void createTexture(CardTextureUtil.TextureCreatorModel model) {
                        Table table = new Table();
                        table.setSize(CARD_WIDTH * 20 / scaleFactor, CARD_HEIGHT * 10 / scaleFactor);
                        table.setBackground(new TextureRegionDrawable(matchedQuestionTexReg));

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

                //dark
                CardTextureUtil.CardTextureCreator darkMatchedQuestionCardTextureCreator = CardTextureUtil.getCardTextureCreator(batch);
                darkMatchedQuestionCardTextureCreator.createTextureRegion(new CardTextureUtil.CardTextureCreator.ITextureCreator() {
                    @Override
                    public void createTexture(CardTextureUtil.TextureCreatorModel model) {
                        Table table = new Table();
                        table.setSize(CARD_WIDTH * 20 / scaleFactor, CARD_HEIGHT * 10 / scaleFactor);
                        table.setBackground(new TextureRegionDrawable(darkMatchedQuestionTexReg));

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

                //set cardhandle
                DefCardHandle questionCardHandle = new DefCardHandle(this, new Image(blackCardTexReg),
                        questionCardTextureCreator.getTextureRegion(), cardBackTexReg,
                        matchedQuestionCardTextureCreator.getTextureRegion(),
                        darkMatchedQuestionCardTextureCreator.getTextureRegion(),
                        "sounds/water004.wav", "sounds/water002.wav",
                        "sounds/single029.wav", "sounds/single039.wav");
                //question --end

                //answer  ==begin
                //common
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

                //matched
                CardTextureUtil.CardTextureCreator matchedAnswerCardTextureCreator = CardTextureUtil.getCardTextureCreator(batch);
                matchedAnswerCardTextureCreator.createTextureRegion(new CardTextureUtil.CardTextureCreator.ITextureCreator() {
                    @Override
                    public void createTexture(CardTextureUtil.TextureCreatorModel model) {
                        Table table = new Table();
                        table.setSize(CARD_WIDTH * 20 / scaleFactor, CARD_HEIGHT * 10 / scaleFactor);
                        table.setBackground(new TextureRegionDrawable(matchedAnswerTexReg));

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

                //dark
                CardTextureUtil.CardTextureCreator darkMatchedAnswerCardTextureCreator = CardTextureUtil.getCardTextureCreator(batch);
                darkMatchedAnswerCardTextureCreator.createTextureRegion(new CardTextureUtil.CardTextureCreator.ITextureCreator() {
                    @Override
                    public void createTexture(CardTextureUtil.TextureCreatorModel model) {
                        Table table = new Table();
                        table.setSize(CARD_WIDTH * 20 / scaleFactor, CARD_HEIGHT * 10 / scaleFactor);
                        table.setBackground(new TextureRegionDrawable(darkMatchedAnswerTexReg));

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

                //set cardlandle
                DefCardHandle answerCardHandle = new DefCardHandle(this, new Image(blackCardTexReg),
                        answerCardTextureCreator.getTextureRegion(), cardBackTexReg,
                        matchedAnswerCardTextureCreator.getTextureRegion(),
                        darkMatchedAnswerCardTextureCreator.getTextureRegion(),
                        "sounds/water004.wav", "sounds/water002.wav",
                        "sounds/single029.wav", "sounds/single039.wav");
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

    private void soundAction(Image img, Sound sound, float soundDuration, float pitchStart, float pitchEnd, Interpolation interpolation) {
        // 加載音效


        long soundId = sound.play(); // 需要保存这个ID来稍后控制音效

        // 创建一个曲线插值，例如线性或加速插值

        // 创建并添加Action到某个Actor，这样它就会执行
        SoundAction soundAction = new SoundAction(sound, soundId, soundDuration, pitchStart, pitchEnd, interpolation);
        soundAction.setVolume(0.33f);
        img.addAction(soundAction);

    }

    @Override
    public List<Observer<DefCardHandle>> getObservers() {
        return this.cardhandleObservers;
    }

    @Override
    public Map<String, Object> getOtherData() {
        HashMap<String, Object> objectHashMap = new HashMap<>();
        objectHashMap.put("stage", this.stage);
        return objectHashMap;
    }


    //inner class --begin

    private static class IdentifiedClickListener extends ClickListener {

        int identifier;

        public IdentifiedClickListener(int identifier) {
            this.identifier = identifier;
        }

        public int getIdentifier() {
            return identifier;
        }
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


}

