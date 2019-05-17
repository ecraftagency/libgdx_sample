package com.pixmeg;

import com.badlogic.gdx.math.Vector2;

public class Segment {
    public Vector2 startPoint,endPoint;
    public boolean branch;

    public Segment(){
        startPoint = new Vector2();
        endPoint = new Vector2();
        branch = false;
    }
}
