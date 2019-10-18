package com.common;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class XGame extends Game {
  protected SpriteBatch batch;

  @Override
  final public void create() {
    batch = new SpriteBatch();
    _create();
  }

  public abstract void _create();
  public abstract InputMultiplexer getInput();

  @Override
  public void dispose() {
    batch.dispose();
  }
}
