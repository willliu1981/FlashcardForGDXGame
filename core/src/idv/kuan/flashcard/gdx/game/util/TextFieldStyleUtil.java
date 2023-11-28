package idv.kuan.flashcard.gdx.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import idv.kuan.flashcard.gdx.game.screen.MainScreen;

public class TextFieldStyleUtil {


    public static class DynamicCharacters {
        StringBuilder characters = new StringBuilder();


        public DynamicCharacters add(char c) {
            return add(String.valueOf(c));
        }

        public DynamicCharacters add(String string) {
            for (char c : string.toCharArray()) {
                if (characters.indexOf(String.valueOf(c)) < 0) {
                    characters.append(c);

                }
            }

            return this;
        }


        public String getCharacters() {
            return characters.toString();
        }
    }


    public static TextField.TextFieldStyle generateDefaultDynamicFontTextFieldStyle() {
        return generateDefaultDynamicFontTextFieldStyle("");
    }

    public static TextField.TextFieldStyle generateDefaultDynamicFontTextFieldStyle(String userInput) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("GenJyuuGothic-Monospace-Normal.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 16; // 字體大小
        parameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS + userInput;
        parameter.shadowOffsetX = 1; // 設置陰影的X軸偏移
        parameter.shadowOffsetY = 1; // 設置陰影的Y軸偏移
        parameter.shadowColor = new Color(0, 0, 0, 0.75f); // 設置陰影顏色和透明度
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose(); // 不要忘記釋放資源

        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();

        textFieldStyle.font = font;
        textFieldStyle.fontColor = Color.WHITE;
        textFieldStyle.background = MainScreen.skin.getDrawable("textfield");
        textFieldStyle.cursor = MainScreen.skin.getDrawable("cursor");
        textFieldStyle.selection = MainScreen.skin.getDrawable("selection");


        return textFieldStyle;
    }


}
