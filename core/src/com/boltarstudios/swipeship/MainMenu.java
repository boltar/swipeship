package com.boltarstudios.swipeship;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MainMenu implements Screen {


    private Stage stage = new Stage();
    private Table table = new Table();
    private Skin skin = new Skin();
    private Label title;
//    private Skin skin = new Skin(Gdx.files.internal("skins/menuSkin.json"),
//            new TextureAtlas(Gdx.files.internal("skins/menuSkin.pack")));


    private TextButton buttonContinue, buttonRestart;
    private SwipeShipMain game;

    public MainMenu(final SwipeShipMain game) {
        createBasicSkin();
        buttonRestart = new TextButton("Restart", skin);
        buttonContinue = new TextButton("Continue", skin);
        title = new Label("Swipe Ship",skin);
        this.game = game;

        buttonRestart.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("play clicked!");
                game.restart();
                game.setPaused(false);
                game.setScreen(game.gameScreen);
            }
        });
        buttonContinue.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("play clicked!");
                game.setPaused(false);
                game.setScreen(game.gameScreen);
            }
        });

        //The elements are displayed in the order you add them.
        //The first appear on top, the last at the bottom.
        table.add(title).padBottom(40).row();
        table.add(buttonContinue).size(150,60).padBottom(20).row();
        table.add(buttonRestart).size(150,60).padBottom(20).row();

        table.setFillParent(true);
        stage.addActor(table);

    }

    private void createBasicSkin() {
        //Create a font
        BitmapFont font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.setScale(2);
        skin.add("default", font);

        //Create a texture
        Pixmap pixmap = new Pixmap((int) Gdx.graphics.getWidth() / 4, (int) Gdx.graphics.getHeight() / 10, Pixmap.Format.RGB888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("background", new Texture(pixmap));

        //Create a button style
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("background", Color.GRAY);
        textButtonStyle.down = skin.newDrawable("background", Color.DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable("background", Color.DARK_GRAY);
        textButtonStyle.over = skin.newDrawable("background", Color.LIGHT_GRAY);
        textButtonStyle.font = skin.getFont("default");
        skin.add("default", textButtonStyle);

        //Create a label style
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = skin.getFont("default");
        skin.add("default", labelStyle);
    }


    @Override
    public void render(float delta) {
        //Gdx.gl.glClearColor(0, 0, 0, 1);
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {

        Gdx.app.debug("startup", "MainMenu show()");
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

}