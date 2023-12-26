package idv.kuan.flashcard.gdx.game.util;

import com.badlogic.gdx.Gdx;
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
    private float volume;


    public SoundAction(Sound sound) {
        this(sound, sound.play(), 1.0f, 1.0f, 1.0f, Interpolation.fade);
        this.volume = 1.0f;
    }

    public SoundAction(Sound sound, long soundId, float duration, float pitchStart, float pitchEnd, Interpolation interpolation) {
        this.sound = sound;
        this.soundId = soundId;
        this.duration = duration;
        this.start = pitchStart;
        this.end = pitchEnd;
        this.elapsedTime = 0;
        this.interpolation = interpolation;

    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    @Override
    public boolean act(float delta) {
        elapsedTime += delta;
        float progress = elapsedTime / duration;
        float value = interpolation.apply(this.start, this.end, progress); // Apply interpolation curve here
        sound.setPitch(soundId, value);
        sound.setVolume(soundId, this.volume);

        if (elapsedTime >= duration) {
            return true; // Action completed
        }
        return false; // Action not completed yet
    }
}

