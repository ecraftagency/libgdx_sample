package com.pixmeg;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Line {
    public Vector2 a;
    public Vector2 b;
    public float thickness;

    public Line(Vector2 a, Vector2 b, float thickness)
    {
        this.a = a;
        this.b = b;
        this.thickness = thickness;
    }

    public void draw(SpriteBatch batch, TextureAtlas atlas, Color color){
        Vector2 tangent = new Vector2(b.x-a.x,b.y-a.y);
        float angle = (float)Math.atan2(tangent.y,tangent.x);
        float imageThickness = 1;
        float thicknessScale = thickness/imageThickness;

        TextureRegion cap = atlas.findRegion("lc");
        TextureRegion midSegment = atlas.findRegion("ms");

        batch.draw(midSegment,a.x,a.y,tangent.len(),midSegment.getRegionHeight()/5);


        
    }


}
