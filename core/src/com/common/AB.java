package com.common;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.ColorAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.RotateToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;

public class AB {//Action builder
  private static Actor target; //thread !safe :)
  private static ArrayMap<String, Object> props = new ArrayMap<>();

  public static AB build(Actor target) {
    AB.target = target;
    return new AB();
  }

  public AB p(float x, float y) {
    props.put("position", new Vector2(x,y));
    return this;
  }

  public AB c(Color c) {
    props.put("color", c);
    return this;
  }

  public AB r(float a) {
    props.put("angle", a);
    return this;
  }

  public AB d(float d) {
    props.put("duration", d);
    return this;
  }

  public AB in(Interpolation in) {
    props.put("interpolation", in);
    return this;
  }

  public AB cnt(int c) {
    props.put("repeat", c);
    return this;
  }

  public AB zz() {
    props.put("zozo", "yes");
    return this;
  }

  public Action done() {
    if (target == null) return Actions.delay(0);
    Action r = buildRepeat(buildDI(buildZoZo(buildAppearance())));
    props.clear();target = null;
    return r;
  }

  public Action done(Runnable r) {
    if (target == null) return Actions.delay(0);

    SequenceAction s = Actions.sequence();
    Action main = buildRepeat(buildDI(buildZoZo(buildAppearance())));
    RunnableAction ra = Actions.run(r);
    s.addAction(main);s.addAction(ra);
    props.clear();target = null;
    return s;
  }

  private static Action buildRepeat(SequenceAction seq) {
    Integer repeat = (Integer)props.get("repeat");
    return Actions.repeat(repeat == null ? 1 : repeat, seq);
  }

  private static SequenceAction buildDI(SequenceAction zozo) {
    Float d = (Float)props.get("duration");
    Interpolation in = (Interpolation)props.get("interpolation");
    for (Action a : zozo.getActions()){
      ParallelAction pal = (ParallelAction)a;
      for (Action i : pal.getActions()){
        TemporalAction temp = (TemporalAction)i;
        if (d != null) temp.setDuration(d);
        if (in != null) temp.setInterpolation(in);
      }
    }
    return zozo;
  }

  private static SequenceAction buildZoZo(Array<ParallelAction> appear) {
    SequenceAction seq = Actions.sequence();
    String zz = (String) props.get("zozo");
    if (zz != null) {
      seq.addAction(appear.get(0));
      seq.addAction(appear.get(1));
    }
    seq.addAction(appear.get(0));
    return seq;
  }

  private static Array<ParallelAction> buildAppearance() {
    ParallelAction pa = Actions.parallel();
    ParallelAction rpa = Actions.parallel();

    Vector2 p = (Vector2) props.get("position");
    Color c = (Color)props.get("color");
    Float a = (Float) props.get("angle");

    if (p != null) {
      MoveToAction m = Actions.moveTo(p.x, p.y);
      MoveToAction rm = Actions.moveTo(target.getX(), target.getY());
      pa.addAction(m);rpa.addAction(rm);
    }

    if (c != null) {
      ColorAction ca = Actions.color(c);
      ColorAction rca = Actions.color(target.getColor());
      pa.addAction(ca);rpa.addAction(rca);
    }

    if (a != null) {
      RotateToAction ra = Actions.rotateTo(a);
      RotateToAction rra = Actions.rotateTo(target.getRotation());
      pa.addAction(ra);rpa.addAction(rra);
    }

    Array<ParallelAction> r = new Array<>();
    r.add(pa);r.add(rpa);
    return r;
  }
}