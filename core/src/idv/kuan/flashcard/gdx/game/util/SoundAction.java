package idv.kuan.flashcard.gdx.game.util;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Interpolation;

public class SoundAction extends Action {
    private float start;
    private float end;
    private Sound sound;
    private long soundId;
    private float duration;
    private float elapsedTime;
    private Interpolation interpolation;


    public SoundAction(Sound sound, long soundId, float duration, float start, float end, Interpolation interpolation) {
        this.sound = sound;
        this.soundId = soundId;
        this.duration = duration;
        this.start = start;
        this.end = end;
        this.elapsedTime = 0;
        this.interpolation = interpolation;

    }

    @Override
    public boolean act(float delta) {
        elapsedTime += delta;
        float progress = elapsedTime / duration;
        float value = interpolation.apply(this.start, this.end, progress); // Apply interpolation curve here
        sound.setPitch(soundId, value);

        if (elapsedTime >= duration) {
            return true; // Action completed
        }
        return false; // Action not completed yet
    }
}

