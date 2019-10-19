package com;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.common.G;
import com.common.X;
import com.common.XGame;
import com.games.LinearAlgebra;
import com.games.TweenEngine;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.games.Mask;
import com.games.maubinh.MauBinh;

public class Root implements ApplicationListener {
  private Stage rootUI;
  private static Array<Class<? extends XGame>> games = new Array<>();
  private XGame currentGame;
  private InputMultiplexer imx = new InputMultiplexer();

  static {
    games.add(MauBinh.class);
    games.add(Mask.class);
    games.add(LinearAlgebra.class);
    games.add(TweenEngine.class);
  }

  @SuppressWarnings("unchecked")
  @Override
  public void create() {
    X.init(X.ATLAS | X.FONT);
    VisUI.load();

    rootUI = new Stage(new ScreenViewport());
    imx.addProcessor(0, rootUI);
    Gdx.input.setInputProcessor(imx);

    VisSelectBox<VisLabel> select = new VisSelectBox<>();
    select.addListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        VisLabel entry = select.getSelected();
        try {
          Class<? extends XGame> clazz = (Class<? extends XGame>)entry.getUserObject();
          if (clazz != null)
            createGame(clazz);
        } catch (Exception ignored) {

        }
      }
    });
    initGameList(select);
    select.setSelectedIndex(3);

    Table root = (Table) G.c(Table.class).add(rootUI).ub();
    root.setFillParent(true);root.pad(10);
    root.add(select).expand().left().top();
  }

  private void createGame(Class<? extends XGame> clazz) {
    XGame temp = currentGame;
    try {
      currentGame = clazz.getDeclaredConstructor().newInstance();
      if (temp != null){
        temp.dispose();
        if (imx.getProcessors().size > 1) imx.removeProcessor(1);
      }
      currentGame.create();
      if (currentGame.getInput() != null) imx.addProcessor(1, currentGame.getInput());
    } catch (Exception e) {
      e.printStackTrace();
      currentGame = temp;
    }
  }

  private void initGameList(VisSelectBox<VisLabel> container) {
    Array<VisLabel> items = new Array<>();
    for (Class<? extends XGame> clazz : games) {
      VisLabel entry = new VisLabel(clazz.getSimpleName()) {
        @Override
        public String toString() {
          return clazz.getSimpleName();
        }
      };
      entry.setUserObject(clazz);
      items.add(entry);
    }
    container.setItems(items);
  }

  @Override
  public void resize(int width, int height) {
    rootUI.getViewport().update(width, height);
    currentGame.resize(width, height);
  }

  @Override
  public void render() {
    Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
    currentGame.render();
    rootUI.act();
    rootUI.draw();
  }

  @Override
  public void pause() {
    currentGame.pause();
  }

  @Override
  public void resume() {
    currentGame.resume();
  }

  @Override
  public void dispose() {
    rootUI.dispose();
    currentGame.dispose();
    X.dispose();
  }
}
