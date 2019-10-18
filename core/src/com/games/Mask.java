package com.games;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.common.G;
import com.common.XGame;

public class Mask extends XGame {
  private Stage stage;
  private ShapeRenderer sr;

  @Override
  public void _create() {
    stage = new Stage(new ScreenViewport(), batch);
    sr = new ShapeRenderer();

    Group g = new Group() {
      @Override
      public void draw(Batch batch, float parentAlpha) {
        batch.end();
        Gdx.gl20.glDepthFunc(GL20.GL_LESS); //set function to less
        Gdx.gl20.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl20.glDepthMask(true);
        Gdx.gl20.glColorMask(false, false, false, false);

        //sr.setProjectionMatrix(batch.getProjectionMatrix());
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(0.5f, 0.5f, 0.5f, 1f);
        sr.circle(100, 100, 100);
        sr.flush();
        sr.end();

        batch.begin();
        Gdx.gl.glColorMask(true, true, true, true);
        Gdx.gl20.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl20.glDepthFunc(GL20.GL_EQUAL);
        super.draw(batch, parentAlpha);
        batch.flush();
        Gdx.gl20.glDisable(GL20.GL_DEPTH_TEST);
      }
    };

    G.b(g).add(stage).p(0,0).adds(G.c(Image.class).p(0,0).k("1").ub());
  }

  @Override
  public void render() {
    Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT); //Clear Screen
    stage.act();
    stage.draw();
  }

  @Override
  public InputMultiplexer getInput() {
    InputMultiplexer imx = new InputMultiplexer();
    imx.addProcessor(stage);
    return imx;
  }
}