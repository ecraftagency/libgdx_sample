package com.games;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

import static com.badlogic.gdx.math.Interpolation.*;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.common.G;
import com.common.XGame;

public class CardDeal extends XGame {
  private Stage stage;

  @Override
  public void _create() {
    stage = new Stage(new ScreenViewport(), batch);
    for (int i = 1; i < 52; i++)
      G.c(Image.class).k(i + "").p(0, 0).o(Align.center).add(stage);
    move();
  }

  private void move() {
    Array<Actor> actors = stage.getActors();
    int idx = 0; final float step = 0.1f, x = 400, y = 400, r = 360, d = 0.4f;
    for (Actor actor : actors) {
      actor.addAction(
        sequence(
          delay(idx++*step),
          parallel(
            moveTo(x, y, d, fastSlow),
            rotateTo(r, d, fastSlow))
        )
      );
    }
  }

  @Override
  public void render() {
    stage.act();
    stage.draw();
  }

  @Override
  public InputMultiplexer getInput() {
    return null;
  }
}