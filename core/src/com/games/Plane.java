package com.games;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.common.XGame;

public class Plane extends XGame {
  private ShapeRenderer   sr;
  private Stage           stage;
  private Vector2         pA, pB;
  @Override
  public void _create() {
    pA = new Vector2(0,300);
    pB = new Vector2(MathUtils.random(400), 200 + MathUtils.random(100));

    stage = new Stage(new ScreenViewport(), batch) {
      @Override
      public void draw() {
        super.draw();
        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(Color.WHITE);
        sr.line(pA, pB);
        sr.end();
      }
    };
    sr = new ShapeRenderer();

    stage.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);
        float r = pA.dst(pB)/2f; //ban kinh
        Vector2 c = new Vector2((pA.x + pB.x)/2f, (pA.y + pB.y)/2f); //tam
        Vector2 vAB = pB.cpy().sub(pA);//vector A--->B
        float theta = vAB.angle();//goc cua vector A--->B voi truc hoanh

        Vector2 p1 = new Vector2(c.x + r*MathUtils.cosDeg(theta), c.y + r*MathUtils.sinDeg(theta));
        Vector2 p2 = new Vector2(c.x + r*MathUtils.cosDeg(theta + 180), c.y + r*MathUtils.sinDeg(theta + 180));
        float d = (x - p1.x)*(p2.y - p1.y) - (y - p1.y)*(p2.x - p1.x);
        Gdx.app.log("d: ", "" + d);
      }
    });
  }

  @Override
  public void render() {
    super.render();
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