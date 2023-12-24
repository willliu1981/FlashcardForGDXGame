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
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import idv.kuan.flashcard.gdx.game.database.dao.WordDao;
import idv.kuan.flashcard.gdx.game.database.entity.Word;
import idv.kuan.flashcard.gdx.game.module.CardHandle;
import idv.kuan.flashcard.gdx.game.module.GameView;
import idv.kuan.flashcard.gdx.game.util.SoundAction;

public class MemoryMatchChallengeGameView extends GameView {
    final static int CARD_WIDTH = 100, CARD_HEIGHT = 100;
    final float ANIM_DURATIONTIME = 0.75f;

    private List<DefCardHandle> cardHandles;
    private final int CARDCOUNT = 12;
    private TextureRegion frontQuestionTexReg;
    private TextureRegion frontAnswerTexReg;
    private TextureRegion backCardTexReg;
    private TextureRegion blackCardTexReg;
    private Texture backCardAminsTexture;


    private static class DefCardHandle extends CardHandle {

        private TextureRegion frontBackgroundTexReg;
        private TextureRegion backBackgroundTexReg;
        private Sound flipToFrontSound;
        private Sound flipToBackSound;
        private boolean isFrontState;


        public DefCardHandle(Image background,
                             TextureRegion frontBackgroundTexReg, TextureRegion backBackgroundTexReg,
                             String flipToFrontSoundFilePath, String flipToFBackSoundFilePath) {
            super(background);
            this.frontBackgroundTexReg = frontBackgroundTexReg;
            this.backBackgroundTexReg = backBackgroundTexReg;
            this.flipToFrontSound = Gdx.audio.newSound(Gdx.files.internal(flipToFrontSoundFilePath));
            this.flipToBackSound = Gdx.audio.newSound(Gdx.files.internal(flipToFBackSoundFilePath));
            this.isFrontState = false;
        }

        @Override
        protected void initialize() {
            // 设置卡片的原点为中心
            this.background.setOrigin(CARD_WIDTH / 2, CARD_HEIGHT * 2 / 2);

            //sound listener --begin
            this.background.addListener(new ClickListener() {
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

            //image listener ==begin
            this.background.addListener(new ClickListener() {
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
                    //image listener ==end

                }
            });


        }

        private TextureRegion getCurrentTextureRegion() {
            return this.isFrontState ? this.frontBackgroundTexReg : this.backBackgroundTexReg;
        }

        private void changeFlipedState() {
            this.isFrontState = !this.isFrontState;
        }

        private SoundAction playFlipToFrontWithSoundAction() {
            SoundAction soundAction = new SoundAction(flipToFrontSound);
            background.addAction(soundAction);
            return soundAction;
        }

        private SoundAction playFlipToBackWithSoundAction() {
            SoundAction soundAction = new SoundAction(flipToBackSound);
            background.addAction(soundAction);
            return soundAction;
        }

        public boolean isFrontState() {
            return isFrontState;
        }

        public Image getBackground() {
            return background;
        }
    }

    @Override
    public void initialize() {
        //init texture --begin
        frontQuestionTexReg = new TextureRegion(new Texture("test/q3.png"));
        frontAnswerTexReg = new TextureRegion(new Texture("test/a3.png"));
        backCardTexReg = new TextureRegion(new Texture("test/b1.png"));
        blackCardTexReg = new TextureRegion(new Texture("test/b0.png"));
        TextureRegion cardBackTexReg = new TextureRegion(new Texture("test/b1.png"));
        TextureRegion questionTexReg = new TextureRegion(new Texture("test/q3.png"));
        TextureRegion answerTexReg = new TextureRegion(new Texture("test/a3.png"));

        backCardAminsTexture = new Texture("test/bs3.png");


        //init texture --end


        cardHandles = new ArrayList<>();

        WordDao dao = new WordDao();
        try {
            List<Word> all = dao.findAll();
            for (Word word : all.stream().distinct().limit(CARDCOUNT).collect(Collectors.toList())) {
                Image image1 = new Image(blackCardTexReg);
                DefCardHandle questionCardHandle = new DefCardHandle(image1,
                        questionTexReg, cardBackTexReg,
                        "sounds/water004.wav", "sounds/water002.wav");
                Image image2 = new Image(blackCardTexReg);
                DefCardHandle answerCardHandle = new DefCardHandle(image2,
                        answerTexReg, cardBackTexReg,
                        "sounds/water004.wav", "sounds/water002.wav");

                questionCardHandle.setWord(word);
                answerCardHandle.setWord(word);
                cardHandles.add(questionCardHandle);
                cardHandles.add(answerCardHandle);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        createCard();

    }


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
                table.add(img).size(CARD_WIDTH, CARD_HEIGHT);
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

    private void cardFlipActions(Image img, boolean ifFlipedToFront) {
        float DT_TOBACK = 0.22f;
        float DT_TOFRONT = 0.33f;

        float minMoveDst = -5;
        float maxMoveDst = 5;

        float minRotateDst = -1f;
        float maxRotateDst = 1f;

        final float originalX = img.getX();
        final float originalY = img.getY();


        img.setOrigin(CARD_WIDTH / 2, CARD_HEIGHT / 2);

        img.addAction(

                Actions.parallel(
                        //move
                        Actions.sequence(
                                Actions.moveBy(MathUtils.random(minMoveDst / 20, maxMoveDst / 20)
                                        , MathUtils.random(minMoveDst / 20, maxMoveDst / 20)
                                        , (ifFlipedToFront ? DT_TOBACK : DT_TOFRONT)
                                        , Interpolation.smooth)
                                , Actions.moveBy(MathUtils.random(minMoveDst, maxMoveDst), MathUtils.random(minMoveDst, maxMoveDst)
                                        , (ifFlipedToFront ? DT_TOBACK : DT_TOFRONT) - Math.min(DT_TOBACK, DT_TOFRONT) / 2
                                        , Interpolation.bounceIn)
                                , Actions.moveTo(originalX, originalY, Math.min(DT_TOBACK, DT_TOFRONT) / 2, Interpolation.bounceOut))

                        //rotate
                        , Actions.sequence(Actions.rotateBy(MathUtils.random(minRotateDst / 20, maxRotateDst / 20)
                                        , (ifFlipedToFront ? DT_TOBACK : DT_TOFRONT)
                                        , Interpolation.smooth)
                                , Actions.rotateBy(MathUtils.random(minRotateDst, maxRotateDst)
                                        , (ifFlipedToFront ? DT_TOBACK : DT_TOFRONT) - Math.min(DT_TOBACK, DT_TOFRONT) / 2
                                        , Interpolation.bounceIn)
                                , Actions.rotateTo(0, Math.min(DT_TOBACK, DT_TOFRONT) / 2, Interpolation.bounceOut))

                        //alpha
                        , Actions.sequence(Actions.alpha(0.0f, ifFlipedToFront ? DT_TOBACK : DT_TOFRONT)
                                , Actions.alpha(1f, ifFlipedToFront ? DT_TOBACK / 2 : DT_TOFRONT / 2))

                        //scale
                        , Actions.sequence(
                                Actions.scaleTo(0, 1, ifFlipedToFront ? DT_TOBACK : DT_TOFRONT
                                        , Interpolation.circleOut), // 縮放動畫，持續0.5秒
                                Actions.run(new Runnable() {
                                    @Override
                                    public void run() {
                                        /*
                                        if (ifFlipedToFront) {
                                            card.flipToFront(false);

                                        } else {
                                            card.flipToFront(true);

                                        }

                                        img.setDrawable(new TextureRegionDrawable(card.getCurrentTextureRegion()));


                                         */
                                    }
                                })
                                , Actions.scaleTo(1, 1, ifFlipedToFront ? DT_TOBACK : DT_TOFRONT
                                        , (ifFlipedToFront ? Interpolation.bounceOut : Interpolation.circleOut))
                        )));

    }


    private void soundAction(Image img, Sound sound, float soundDuration, float pitchStart, float pitchEnd, Interpolation interpolation) {
        // 加載音效


        long soundId = sound.play(); // 需要保存这个ID来稍后控制音效

        // 创建一个曲线插值，例如线性或加速插值

        // 创建并添加Action到某个Actor，这样它就会执行
        SoundAction soundAction = new SoundAction(sound, soundId, soundDuration, pitchStart, pitchEnd, interpolation);
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


}
