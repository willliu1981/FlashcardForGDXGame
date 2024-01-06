package idv.kuan.flashcard.gdx.game.test;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;


import idv.kuan.flashcard.gdx.game.module.GameView;
import idv.kuan.flashcard.gdx.game.screen.MainScreen;

public class TestToolScreen extends GameView {

    TextButton button;
    TextField txtfActual;
    TextField txtfCurr;
    Label lblActual;
    Label lblCurr;
    Label lblPeriod;

    @Override
    public void initialize() {
        Table table = new Table();
        button = new TextButton("get period", MainScreen.skin);
        txtfActual = new TextField("", MainScreen.skin);
        txtfCurr = new TextField("", MainScreen.skin);
        lblActual = new Label("actual=", MainScreen.skin);
        lblCurr = new Label("curr=", MainScreen.skin);
        lblPeriod = new Label("period=", MainScreen.skin);


        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                int actual = 0, curr = 0;

                if (txtfActual.getText() != "") {
                    actual = Integer.valueOf(txtfActual.getText());
                    curr = Integer.valueOf(txtfCurr.getText());
                }

                //2 4 7 12 21 36 63 110 192 336...

                int nextPeriod = calculateNextPeriod(actual, curr);
                lblPeriod.setText("period=" + nextPeriod);

            }
        });


        table.add(lblPeriod).row();
        table.add(lblActual).getTable().add(txtfActual).row();
        table.add(lblCurr).getTable().add(txtfCurr).row();
        table.add(button);

        table.setFillParent(true);

        stage.addActor(table);

    }

    int calculateNextPeriod(int actual, int curr) {
        int next = (int) (curr * 1.75);
        int midPoint = (curr + next) / 2;

        if (actual < midPoint) {
            // 执行 planA
            return curr + (next - curr) * (actual / curr);
        } else {
            // 执行 planB
            return planB(actual, curr);
        }
    }

    int planB(int actual, int curr) {
        int next = (int) (curr * 1.75);
        int last = (int) (curr / 1.75);

        while (actual >= next) {
            curr = next;
            next = (int) (curr * 1.75);
            last = (int) (last / 1.75);
        }

        return last;
    }


}
