package com.common.tween;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;

import aurelienribon.tweenengine.TweenAccessor;

public class A implements TweenAccessor<Actor>
{
  public static final int XY = 1;
  public static final int CXY = 2;
  public static final int SCL = 3;
  public static final int R = 4;
  public static final int O = 5;
  public static final int C = 6;

  @Override
  public int getValues(Actor target, int tweenType, float[] returnValues)
  {
    switch (tweenType)
    {
      case XY:
        returnValues[0] = target.getX();
        returnValues[1] = target.getY();
        return 2;

      case CXY:
        returnValues[0] = target.getX() + target.getWidth()*.5f;
        returnValues[1] = target.getY() + target.getHeight()*.5f;
        return 2;

      case SCL:
        returnValues[0] = target.getScaleX();
        returnValues[1] = target.getScaleY();
        return 2;

      case R: returnValues[0] = target.getRotation(); return 1;

      case O: returnValues[0] = target.getColor().a; return 1;

      case C:
        returnValues[0] = target.getColor().r;
        returnValues[1] = target.getColor().g;
        returnValues[2] = target.getColor().b;
        return 3;

      default: assert false; return -1;
    }
  }

  @Override
  public void setValues(Actor target, int tweenType, float[] newValues) {
    switch (tweenType) {

      case XY: target.setPosition(newValues[0], newValues[1]); break;

      case CXY: target.setPosition(newValues[0] - target.getWidth()*.5f, newValues[1] - target.getHeight()*.5f); break;

      case SCL: target.setScale(newValues[0], newValues[1]); break;

      case R: target.setRotation(newValues[0]); break;

      case O:
        Color c = target.getColor();
        c.set(c.r, c.g, c.b, newValues[0]);
        target.setColor(c);
        break;

      case C:
        c = target.getColor();
        c.set(newValues[0], newValues[1], newValues[2], c.a);
        target.setColor(c);
        break;

      default: assert false;
    }
  }
}
