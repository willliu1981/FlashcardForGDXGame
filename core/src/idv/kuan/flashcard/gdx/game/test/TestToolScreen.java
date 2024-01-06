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

                ReviewSession session = new ReviewSession(actual, curr);

                int nextPeriod = session.getNextPeriod();
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


    public class ReviewSession {
        private int actual;    // 实际复习时间
        private int curr;      // 当前周期
        private int nextPeriod; // 下一个复习周期

        public ReviewSession(int actual, int curr) {
            this.actual = actual;
            this.curr = curr;
            this.nextPeriod = calculateNextPeriod(actual, curr);
        }

        private int calculateNextPeriod(int actual, int curr) {
            int next = (int) (curr * 2.1);
            int midPoint = (curr + next) / 2;

            if (actual <= midPoint) {
                // 执行 planA
                return curr + (int) ((next - curr) * ((float) actual / curr));
            } else {
                // 执行 planB
                return planB(actual, curr);
            }
        }

        private int planB(int actual, int curr) {
            int next = (int) (curr * 1.75);
            int last = (int) (curr / 1.75);

            while (actual >= next) {
                curr = next;
                next = (int) (curr * 1.75);
                last = (int) (last / 1.75);
            }

            return last;
        }

        // Getter and Setter methods
        public int getActual() {
            return actual;
        }

        public void setActual(int actual) {
            this.actual = actual;
            this.nextPeriod = calculateNextPeriod(actual, this.curr);
        }

        public int getCurr() {
            return curr;
        }

        public void setCurr(int curr) {
            this.curr = curr;
            this.nextPeriod = calculateNextPeriod(this.actual, curr);
        }

        public int getNextPeriod() {
            return nextPeriod;
        }
    }


}
