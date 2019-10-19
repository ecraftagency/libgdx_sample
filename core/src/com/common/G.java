package com.common;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

public class G {
  public static class A <T extends Actor> {
    T t;
    void b(T t) { this.t = t; }
    public T ub() {return t;}
    public A p(float x, float y) { t.setPosition(x, y);return this;} //setPosition
    public A px(float x) { t.setX(x); return this;} //setX
    public A py(float y) { t.setY(y); return this;} //setY
    public A p2(float x, float y){t.setX(t.getX() + x);t.setY(t.getY() + y);return this;} //moveTo

    public A s(float xy) {t.setScale(xy);return this;} //setScale
    public A sx(float x) {t.setScaleX(x);return this;} //setScaleX
    public A sy(float y) {t.setScaleY(y);return this;} //setScaleY
    public A s2(float x, float y) {t.setScale(t.getScaleX() + x);t.setScale(t.getScaleY() + y);return this;} //scaleTo

    public A o(int align) { t.setOrigin(align);return this; } //setOrigin
    public A o(float x, float y) {t.setOrigin(x, y);return this;} //setOriginXY
    public A ox(float x) {t.setOriginX(x);return this;} //setOriginX
    public A oy(float y) {t.setOriginY(y);return this;} //setOriginY

    public A h(float h) {t.setHeight(h); return this;} //setHeight
    public A w(float w) {t.setWidth(w); return this;} //setWidth
    public A h2(float h) {t.setHeight(t.getHeight()*h); return this;} //setHeightTo
    public A w2(float w) {t.setWidth(t.getWidth()*w); return this;} //setWidthTo

    public A add(Stage stage) { stage.addActor(t);return this;} //addActor 'Stage
    public A add(Group group) { group.addActor(t);return  this;} //addActor 'Group
    public A r(float theta) {t.setRotation(theta);return this;} //setRotation

    //WIDGET extended only
    public A a(int align) {
      if (Widget.class.isAssignableFrom(t.getClass())) {
        Image i = (Image)t;
        i.setAlign(align);
      }
      return this;
    }

    //IMAGE extended only
    public A k(String key) {
      if (t instanceof Image) {
        Image i = (Image)t;
        TextureRegion tg = X.getTextureRegion(key);
        i.setDrawable(new TextureRegionDrawable(tg));
        i.setSize(tg.getRegionWidth(), tg.getRegionHeight());
      }
      return this;
    }

    public A k(TextureRegion tt) {
      if (t instanceof Image) {
        Image i = (Image)t;
        i.setDrawable(new TextureRegionDrawable(tt));
        i.setSize(tt.getRegionWidth(), tt.getRegionHeight());
      }
      return this;
    }

    //GROUP extend only
    public A adds(Actor ...actors) { ;
      if (Group.class.isAssignableFrom(t.getClass())) {
        Group g = (Group)t;
        for (Actor a : actors)
          g.addActor(a);
      }
      return this;
    }

    //LABEL extend only region
    public A la(int align) {
      if (Label.class.isAssignableFrom(t.getClass())) {
        Label i = (Label) t;
        i.setAlignment(align);
      }
      return this;
    }

    public A style(Label.LabelStyle ls) {
      if (Label.class.isAssignableFrom(t.getClass())) {
        Label l = (Label)t;
        l.setStyle(ls);
      }
      return this;
    }

    public A text(CharSequence txt) {
      if (Label.class.isAssignableFrom(t.getClass())) {
        Label l = (Label)t;
        l.setText(txt);
      }
      return this;
    }

    public A clk(ClickInterface clk){
      t.addListener(new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
          clk.click(event, x, y);
          super.clicked(event, x, y);
        }
      });
      return this;
    }
  }

  public static <V extends Actor> A<V> c(Class<V> clazz){
    V v;
    try {
      v = clazz.getDeclaredConstructor().newInstance();
    } catch (Exception e) {
      return null;
    }
    A<V> a = new A<V>();
    a.b(v);
    return a;
  }

  public static <V extends Actor> A b(V t) {
    A<V> a = new A<V>();
    a.b(t);
    return a;
  }

  @FunctionalInterface
  public interface ClickInterface {
    void click(InputEvent event, float x, float y);
  }
}