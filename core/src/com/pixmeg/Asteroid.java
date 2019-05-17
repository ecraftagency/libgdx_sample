package com.pixmeg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class Asteroid extends Sprite {
    private DemoScreen demoScreen;
    private AssetManager manager;
    private Array<Integer> a;
    private TextureRegion region;
    private World world;
    private Body body;

    private float timer;
    public boolean hit;

    public Asteroid(DemoScreen demoScreen){
        this.demoScreen = demoScreen;
        manager = demoScreen.manager;
        a = new Array<Integer>();
        a.add(0);
        a.add(1);
        a.add(2);
        a.shuffle();
        this.region = manager.get("images/texAtlas.atlas", TextureAtlas.class).findRegion("a",a.get(0));
        world = demoScreen.getWorld();

        createBody();
        setRegion(region);
        setOriginCenter();
        setRotation(body.getAngle()*MathUtils.radiansToDegrees);
        setBounds(body.getPosition().x*Constants.PPM - getWidth()/2,body.getPosition().y*Constants.PPM-getHeight()/2,120,120);

    }

    public void update(){
        if(hit){
            timer += Gdx.graphics.getDeltaTime();
        }
        setOriginCenter();
        setRotation(body.getAngle()*MathUtils.radiansToDegrees);
        if(!hit){
        setBounds(body.getPosition().x*Constants.PPM - getWidth()/2,body.getPosition().y*Constants.PPM-getHeight()/2,120,120);}
        else {
            setBounds(body.getPosition().x*Constants.PPM - getWidth()/2,body.getPosition().y*Constants.PPM-getHeight()/2,256,256);
        }
    }


    private void createBody(){
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(MathUtils.random(0,480)/Constants.PPM,850/Constants.PPM);
        body = world.createBody(bdef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(20/Constants.PPM,20/Constants.PPM);

        body.createFixture(shape,1).setUserData(this);

        shape.dispose();

        body.setTransform(body.getPosition().x,body.getPosition().y,MathUtils.random(0,MathUtils.PI2));

    }

    public Vector2 getCenter(){
        return new Vector2(body.getPosition().x*Constants.PPM,body.getPosition().y*Constants.PPM);
    }

    public void destroyBody(){
        world.destroyBody(body);
    }

    public void setHit(boolean hit) {
        this.hit = hit;
    }

    public float getTimer() {
        return timer;
    }
}
