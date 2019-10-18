package com.games.maubinh;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.common.G;
import com.common.X;
import com.common.XGame;

public class MauBinh extends XGame {
  private static int valueMask = Integer.parseInt("00001111111111111", 2);
  private static int typeMask = Integer.parseInt( "11110000000000000", 2);
  private Stage stage;
  private float cw, ch;
  private Array<Integer> cards;
  private Image btn;

  @Override
  public void _create() {
    cards = MV2.makeCards();
    cw = X.getTextureRegion("1").getRegionWidth()/2f;
    ch = X.getTextureRegion("1").getRegionHeight()/2f;
    OrthographicCamera camera = new OrthographicCamera();
    Viewport viewport = new FitViewport(1280f, 720f, camera);
    camera.position.set(1280f/2, 720f/2, 0.0f);
    stage = new Stage(viewport, batch);

    btn = (Image)G.c(Image.class).k("tiledown").add(stage).p((1280 + ch)/2, (720 - cw/2)/2).s(0.5f).r(90).clk((e,x,y) -> {
      stage.clear();stage.addActor(btn);
      move();
    }).ub();
  }

  private void move() {
    cards.shuffle();
    Array<Array<Integer>> players = new Array<>();
    for (int i = 0; i < 4; i++) players.add(new Array<>());
    for (int i = 0; i < 52; i++) players.get(i%4).add(cards.get(i));
    stage.addActor(createMove(MV2.move(players.get(0)), (1280 - (280 + cw))/2, 0));
    stage.addActor(createMove(MV2.move(players.get(1)), (1280 - (280 + cw))/2, 720 - (160 + ch)));
    stage.addActor(createMove(MV2.move(players.get(2)), 0, (720 - (160 + ch))/2));
    stage.addActor(createMove(MV2.move(players.get(3)), 1280 - (280 + cw), (720 - (160 + ch))/2));
  }

  Image createCard(int key, float x, float y) {
    int color = ((key&typeMask)>>13);
    switch (color) {
      case 8: color = 2;break;
      case 4: color = 3;break;
      case 2: color = 4;break;
      case 1: color = 1;break;
    }
    int value = (key&valueMask);
    for (int i = 0; i < 13; i++)
      if (((value>>i)&1) == 1){
        color += (i)*4;
        break;
      };

    return (Image) G.c(Image.class).k(color+"").p(x,y).a(Align.center).o(Align.center).w2(0.5f).h2(0.5f).ub();
  }

  private Group createMove(Array<Array<Integer>> move, float x, float y) {
    Group res = new Group();
    for (int i = 0; i < 3; i++) res.addActor(createCard(move.get(0).get(i), i*70, 160));
    for (int i = 0; i < 5; i++) res.addActor(createCard(move.get(1).get(i), i*70, 80));
    for (int i = 0; i < 5; i++) res.addActor(createCard(move.get(2).get(i), i*70, 0));
    res.setPosition(x, y);
    return res;
  }

  @Override
  public void render() {
    Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
    stage.act();
    stage.draw();
  }

  @Override
  public void resize(int width, int height) {
    stage.getViewport().update(width, height);
  }

  @Override
  public InputMultiplexer getInput() {
    InputMultiplexer imx = new InputMultiplexer();
    imx.addProcessor(stage);
    return imx;
  }
}
