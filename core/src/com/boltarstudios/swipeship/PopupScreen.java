package com.boltarstudios.swipeship;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Created by tony on 5/6/15.
 */
public class PopupScreen implements Screen {

    private Stage stage = new Stage();
    private Table table = new Table();
    private Skin skin = new Skin();
    private Label title;

    private TextButton buttonContinue, buttonRestart;
    private SwipeShipMain game;

    public PopupScreen(final SwipeShipMain game, final String whileYouWereGone) {
        createBasicSkin();
        buttonContinue = new TextButton("OK", skin);
        title = new Label(whileYouWereGone, skin);
        title.setWrap(true);
        //title.setWidth(game.VIRTUAL_WIDTH / 2);
        this.game = game;

        buttonContinue.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("OK clicked!");
                game.setPaused(false);
                game.setScreen(game.gameScreen);
            }
        });

        table.add(title).width(game.VIRTUAL_WIDTH/2).padBottom(40).row();
        table.add(buttonContinue).size(150,60).padBottom(20).row();

        table.setFillParent(true);
        stage.addActor(table);

    }

    private void createBasicSkin() {
        //Create a font
        BitmapFont font = new BitmapFont();

        font.setColor(Color.WHITE);
        font.setScale(1);
        skin.add("default", font);

        //Create a texture
        Pixmap pixmap = new Pixmap(Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 10, Pixmap.Format.RGB888);
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
        stage.act();
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
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