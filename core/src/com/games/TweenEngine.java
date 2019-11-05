package com.games;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.common.G;
import com.common.XGame;
import com.common.tween.A;

import aurelienribon.tweenengine.paths.CatmullRom;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

//https://github.com/dsaltares/libgdx-cookbook
public class TweenEngine extends XGame {
  private static final float SW = 1280f;
  private static final float SH = 720f;

  private OrthographicCamera camera;
  private Viewport viewport;

  private Stage stage;
  private TweenManager manager;

  @Override
  public void _create() {
    camera = new OrthographicCamera();
    viewport = new FitViewport(SW, SH, camera);
    stage = new Stage(viewport, batch);

    //chỗ này mình đăng kí Accessor với Tween engine.
    //nghĩa là nếu muốn đại lượng (properties) nào của object no biến thiên
    //thì phải build Accessor cho tween engine access. Google: Accessor and Mutator
    Tween.registerAccessor(Actor.class, new A());
    Tween.setWaypointsLimit(10);

    manager = new TweenManager();
    Image i = (Image) G.c(Image.class).k("6").o(Align.center).add(stage).ub();

//    Timeline.createSequence().
//    push(Tween.to(i, A.XY, 0.2f).target(200,200))
//    .beginParallel()
//      .push(Tween.to(i, A.R, .4f).target(90).ease(Linear.INOUT).repeatYoyo(3, 0.1f).repeatYoyo(2,0.3f))
//      .push(Tween.to(i, A.O, .4f).target(.5f).ease(Linear.INOUT).repeatYoyo(3, 0.1f))
//      .push(Tween.to(i, A.SCL, .4f).target(0f).ease(Linear.INOUT).repeatYoyo(3, 0.1f))
//    .end().beginSequence()
//      .pushPause(1)
//      .push(Tween.to(i, A.XY, 0.2f).target(100, 100).ease(Linear.INOUT).repeatYoyo(2,0.1f))
//      .push(Tween.to(i, A.C, 0.2f).target(0).ease(Elastic.INOUT).repeatYoyo(3, 0))
//      .push(Tween.call((type, source) -> {
//        i.setPosition(900,200);
//      }))
//    .start(manager);

    Timeline tl =Timeline.createSequence()
      .push(Tween.to(i, A.XY, 1.5f).target(100,100).path(new CatmullRom()))
      .push(Tween.to(i, A.XY, 0.5f).target(200,200))
    .start(manager);

    Tween.call((type, source) -> tl.kill()).delay(1000).start(manager);
    
    /*
    Tween.to(i, A.XY, 2f).target(20, 20).ease(Elastic.INOUT)
    là tác động lên object i,
    đại lượng tác động là thuộc tính X và Y
    Accessor là A
    thời gian 0.5,
    giá trị mà đại lượng X Y tiến đến sau 0.5s là 20 - 20
    hàm biến thiên là Elastic.INOUT
    * */
  }

  @Override
  public void render() {
    super.render();
    float dt = Gdx.graphics.getDeltaTime();
    manager.update(dt);
    stage.act(dt);
    stage.draw();
  }

  @Override
  public InputMultiplexer getInput() {
    return null;
  }

  @Override
  public void dispose() {
    super.dispose();
    stage.dispose();
  }
}
