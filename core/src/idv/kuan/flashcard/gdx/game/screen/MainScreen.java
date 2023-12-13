package idv.kuan.flashcard.gdx.game.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import idv.kuan.flashcard.gdx.game.test.TestScreen;
import idv.kuan.flashcard.gdx.game.test.TestScreen2;
import idv.kuan.flashcard.gdx.game.test.TestScreen3;
import idv.kuan.libs.databases.schema.modifier.DatabaseSchemaUtils;
import idv.kuan.libs.databases.schema.modifier.SchemaModifier;
import idv.kuan.libs.databases.schema.modifier.SchemaModifierHandler;
import idv.kuan.libs.databases.schema.modifier.TableSchemaModifier;
import idv.kuan.libs.utils.VersionHelper;

public class MainScreen implements Screen {
    Game game;
    private Stage stage;
    Viewport viewport;

    public static Skin skin;

    private static VersionHelper versionHelper;
    SpriteBatch batch;
    Texture img;

    TextField txtfTitle;
    TextField txtfCount;

    public MainScreen(Game game) {
        this(game, versionHelper);
    }

    public MainScreen(Game game, VersionHelper versionHelper) {
        if (skin == null) {
            skin = new Skin(Gdx.files.internal("uiskin.json"));

        }
        this.game = game;
        this.versionHelper = versionHelper;
        batch = new SpriteBatch();

        checkSchema();

        viewport = new StretchViewport(800, 400);
        stage = new Stage(viewport);


        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

        //
        TextButton btnAdd = new TextButton("Add Word", skin);
        btnAdd.setPosition(150, 100);
        btnAdd.setSize(200, 50);
        btnAdd.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new AddWordScreen(game));

            }
        });

        TextButton btnList = new TextButton("Word List", skin);
        btnList.setPosition(400, 100);
        btnList.setSize(200, 50);
        btnList.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new WordListScreen(game));

            }
        });


        //xxx 測試用
        TextButton btnTestTool = new TextButton("Tool test", skin);
        btnTestTool.setPosition(20, 20);
        btnTestTool.setSize(100, 30);
        btnTestTool.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                //testTool();
                game.setScreen(new TestScreen());

            }
        });


        txtfTitle = new TextField("Word FlashCard", skin);
        txtfTitle.setPosition(50, 300);
        txtfTitle.setSize(700, 50);
        txtfTitle.setDisabled(true);

        txtfCount = new TextField("count:0", skin);
        txtfCount.setPosition(50, 200);
        txtfCount.setSize(700, 50);
        txtfCount.setDisabled(true);

        stage.addActor(btnAdd);
        stage.addActor(txtfTitle);
        stage.addActor(txtfCount);
        stage.addActor(btnList);
        stage.addActor(btnTestTool);//xxx 測試用
    }

    //------------test tool begin--------------// xxx 測試用
    private void testTool() {
        String textWithLineNumbers =
                "1. public class StarPattern {\n" +
                        "2.     public static void main(String[] args) {\n" +
                        "3.         int rows = 5; // 定義行數\n" +
                        "4. \n" +
                        "5.         for (int i = 0; i < rows; i++) { // 外層循環控制行\n" +
                        "6.             for (int j = 0; j <= i; j++) { // 內層循環控制每行的星星數\n" +
                        "7.                 System.out.print(\"* \"); // 打印星星和一個空格\n" +
                        "8.             }\n" +
                        "9.             System.out.println(); // 每完成一行后換行\n" +
                        "10.         }\n" +
                        "11.     }\n" +
                        "12. }\n";

        String textWithoutLineNumbers = removeLineNumbers(textWithLineNumbers);

        System.out.println("Original text with line numbers:\n" + textWithLineNumbers);
        System.out.println("Text after removing line numbers:\n" + textWithoutLineNumbers);
    }

    public static String removeLineNumbers(String text) {
        // 正則表達式：僅匹配行首的數字和後綴的點，並保留後面的空白字符
        String regex = "^(\\s*)\\d+\\.\\s*";
        // 使用MULTILINE模式，使 '^' 匹配每一行的開頭
        Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(text);

        // 替換所有匹配的內容為保留的空白字符
        return matcher.replaceAll("$1");
    }
    //------------test tool end--------------//


    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }


    private void checkSchema() {
        DatabaseSchemaUtils.checkAndUpdateSchema(versionHelper, new DatabaseSchemaUtils.UpdateSchemaExecutor() {
            @Override
            public void execute(SchemaModifierHandler.SchemaModifierBuilder modifierBuilder, List<SchemaModifier> modifiers) {
                TableSchemaModifier word = modifierBuilder.setConstructionSql("CREATE TABLE \"word\" ( " +
                        " \"id\" INTEGER NOT NULL UNIQUE, " +
                        " \"term\" TEXT NOT NULL, " +
                        " \"translation\" TEXT NOT NULL, " +
                        " \"version\" INTEGER NOT NULL, " +
                        " \"metadata\" BLOB, " +
                        " PRIMARY KEY(\"id\" AUTOINCREMENT) " +
                        ")").setTableName("word").createSchemaModifier(TableSchemaModifier.class);
                word.setNewColumns("id,term,translation,version,metadata");
                word.setOldColumns("id,term,translation,-1,metadata");


                TableSchemaModifier wordReviewDetails = modifierBuilder.setConstructionSql("CREATE TABLE \"word_review_details\" (\n" +
                        "\t\"id\"\tINTEGER NOT NULL UNIQUE,\n" +
                        "\t\"initial_review_time\"\tTEXT,\n" +
                        "\t\"next_review_time\"\tTEXT,\n" +
                        "\t\"last_review_time \"\tTEXT,\n" +
                        "\t\"review_stage\"\tINTEGER NOT NULL DEFAULT -1,\n" +
                        "\t\"review_count\"\tINTEGER NOT NULL DEFAULT 0,\n" +
                        "\t\"metadata\"\tBLOB NOT NULL,\n" +
                        "\tPRIMARY KEY(\"id\" AUTOINCREMENT)\n" +
                        ")").setTableName("word_review_details").createSchemaModifier(TableSchemaModifier.class);


                modifiers.add(word);
                modifiers.add(wordReviewDetails);
            }
        });
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        //img.dispose();
        stage.dispose();
    }


}
