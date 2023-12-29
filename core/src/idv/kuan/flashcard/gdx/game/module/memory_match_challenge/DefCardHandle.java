package idv.kuan.flashcard.gdx.game.module.memory_match_challenge;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import idv.kuan.flashcard.gdx.game.module.CardHandle;
import idv.kuan.flashcard.gdx.game.util.SoundAction;
import idv.kuan.libs.interfaces.observers.Subject;

public class DefCardHandle extends CardHandle implements DefCardHandleObservers {
    private static int currentAddId;
    private static int flipToFrontCount;
    final static int CARD_WIDTH = 100, CARD_HEIGHT = 100, PADDING = 5;

    private MemoryMatchChallengeGameView view;
    private TextureRegion frontBackgroundTexReg;
    private TextureRegion backBackgroundTexReg;
    private Sound flipToFrontSound;
    private Sound flipToBackSound;
    private boolean isFrontState;
    private boolean isMatched;
    private boolean isFinishMatch;
    private int id;
    private ClickListener soundListener;
    private ClickListener flipListener;


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
        this.isMatched = false;


        this.id = currentAddId++;
    }

    //DefCardHandle initialize --begin
    @Override
    protected void initialize() {
        // 设置卡片的原点为中心
        this.background.setOrigin(CARD_WIDTH / 2, CARD_HEIGHT * 2 / 2);

        //set sound listener --begin
        soundListener = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                if (DefCardHandle.this.isFrontState) {
                    DefCardHandle.this.playFlipToFrontWithSoundAction();
                } else {
                    DefCardHandle.this.playFlipToBackWithSoundAction();
                }
            }
        };
        //set sound listener --end

        //set flip listener ==begin
        flipListener = new ClickListener() {
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

            }
        };
        //set flip listener ==end

        this.background.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                DefCardHandle.this.soundListener.clicked(event, x, y);
                DefCardHandle.this.flipListener.clicked(event, x, y);

                view.registerObserver(DefCardHandle.this);
                DefCardHandle.flipToFrontCount++;
                DefCardHandle.this.setData(DefCardHandle.this);
            }
        });


    }
    //DefCardHandle initialize --end


    private void match(boolean match) {
        this.isMatched = match;
    }

    private void finishMatch(boolean finishMatch) {
        this.isFinishMatch = finishMatch;
    }

    protected boolean isMatched() {
        return isMatched;
    }

    protected boolean isFinishMatch() {
        return this.isFinishMatch;
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


    //observer --begin

    @Override
    public Subject getSubject() {
        return this.view;
    }

    @Override
    public void onBeforeAllUpdate(DefCardHandle data) {
        if (DefCardHandle.flipToFrontCount == 2) {
            this.finishMatch(true);
            data.finishMatch(true);

            if (this.getId() != data.getId()) {
                if (this.getWord().getId() == data.getWord().getId()) {
                    this.match(true);
                    data.match(true);
                }
            }
        }
    }

    @Override
    public void update(Stage stage, DefCardHandle data) {
        final float DELAY_TIME = 2.0f;

        if (this.isFinishMatch()) {
            if (!this.isMatched()) {
                delayAndAct(stage, () -> {
                    this.soundListener.clicked(null,
                            this.background.getX(), this.background.getY());

                    this.flipListener.clicked(null,
                            this.background.getX(), DefCardHandle.this.background.getY());

                }, DELAY_TIME);

                this.finishMatch(false);

            }

            DefCardHandle.flipToFrontCount = 0;
            this.getSubject().removeObserver(this);
        }


    }

    @Override
    public void onAfterAllUpdate(DefCardHandle data) {

    }

    // 這是一個延遲執行動作的方法
    private void delayAndAct(Stage stage, Runnable action, float delayTime) {
        SequenceAction sequence = Actions.sequence(
                Actions.delay(delayTime),
                Actions.run(action));
        stage.addAction(sequence);
    }

    //observer --end

    public boolean isFrontState() {
        return isFrontState;
    }

    public Image getBackground() {
        return background;
    }

    public int getId() {
        return this.id;
    }


}