package idv.kuan.flashcard.gdx.game.module;

import com.badlogic.gdx.scenes.scene2d.ui.Image;

import idv.kuan.flashcard.gdx.game.database.entity.Word;

public abstract class CardHandle {
    protected Word word;
    protected Image background;

    protected CardHandle(Image background){
        this.background=background;
        initialize();
    }

    protected abstract void initialize();

    public Word getWord() {
        return word;
    }

    public void setWord(Word word) {
        this.word = word;
    }
}
