package idv.kuan.flashcard.gdx.game.module.memory_match_challenge;

import com.badlogic.gdx.scenes.scene2d.Stage;

import idv.kuan.libs.interfaces.observers.Observer;

interface DefCardHandleObservers extends Observer<DefCardHandle> {

    @Override
    default void update(DefCardHandle data) {
        this.update((Stage) getSubject().getOtherData().get("stage"), data);
    }

    void update(Stage stage, DefCardHandle data);
}