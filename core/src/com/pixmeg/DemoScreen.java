package com.pixmeg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class DemoScreen extends ScreenAdapter {
    private GameClass gameClass;
    public AssetManager manager;

    private OrthographicCamera camera;
    private Viewport viewport;

    private ShapeRenderer renderer;
    private SpriteBatch batch;

    private Vector2 startPoint, endPoint;
    private Array<Vector2> points,primayPoints,secondaryPoints;

    private TextureRegion region;
    private Sprite sprite;

    float timer;

    private TextureRegion bg;
    private Array<TextureRegion> hitRegions;
    private Animation<TextureRegion> hitAnimation;
    private float asteroidTimer;
    private Array<Asteroid> asteroids;

    private World world;
    private Box2DDebugRenderer b2dr;
    private RayCastCallback callBack;


    private TextureRegion plane;


    public DemoScreen(GameClass gameClass){
        this.gameClass = gameClass;
        manager = gameClass.manager;

        camera = new OrthographicCamera();
        viewport = new ExtendViewport(Constants.V_WIDTH,Constants.V_HEIGHT,camera);

        renderer = new ShapeRenderer();
        batch = gameClass.batch;

        startPoint = new Vector2();
        endPoint = new Vector2();

        points = new Array<Vector2>();
        primayPoints = new Array<Vector2>();
        secondaryPoints = new Array<Vector2>();

        region = manager.get("images/texAtlas.atlas", TextureAtlas.class).findRegion("bolt1");
        sprite = new Sprite(region);
        sprite.setOrigin(0,region.getRegionHeight()/2);

        bg = manager.get("images/texAtlas.atlas", TextureAtlas.class).findRegion("bg");

        world = new World(new Vector2(0,-3f),true);
        b2dr = new Box2DDebugRenderer();

        callBack = new RayCastCallback() {
            @Override
            public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
                endPoint.set(new Vector2(point.x*Constants.PPM,point.y*Constants.PPM));

                ((Asteroid)fixture.getUserData()).setHit(true);
                fixture.getBody().setActive(false);


                return 0;
            }
        };

        plane = manager.get("images/texAtlas.atlas", TextureAtlas.class).findRegion("p1");
    }

    @Override
    public void show() {
        startPoint.set(240,100);
        endPoint.set(240,850);

        Vector3 direction = new Vector3(endPoint.x-startPoint.x,endPoint.y-startPoint.y,0);
        direction.nor();

        asteroids = new Array<Asteroid>();
        hitRegions = new Array<TextureRegion>();

        addHitRegions();

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        asteroidTimer += delta;
        if(asteroidTimer > 0.5f){
            asteroidTimer = 0;
            asteroids.add(new Asteroid(this));
        }

        for (Asteroid asteroid : asteroids) {
            setRegion(asteroid);
            asteroid.update();
            if (asteroid.getCenter().y < -20) {
                asteroids.removeValue(asteroid, true);
                asteroid.destroyBody();
            }
        }


        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            startPoint.x -= 10;
            endPoint.x -= 10;

        }
        else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            startPoint.x +=10;
            endPoint.x +=10;

        }


        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            world.rayCast(callBack, new Vector2(startPoint.x / Constants.PPM, startPoint.y / Constants.PPM), new Vector2(endPoint.x / Constants.PPM, endPoint.y / Constants.PPM));
        }
        else {
            endPoint.y = 840;
        }

        for (Asteroid asteroid : asteroids) {
            if (asteroid.hit && hitAnimation.isAnimationFinished(asteroid.getTimer())) {
                asteroids.removeValue(asteroid, true);
                asteroid.destroyBody();
                endPoint.y = 840;
            }
        }


        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) {

            timer += 1;

            if(timer > 2) {
                createBolt();
                createPrimaryBolt();
                createSecondaryBolt();
                timer = 0;

            }

        }



        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        batch.draw(bg,0,0,480,800);
        for(Asteroid asteroid:asteroids){
            asteroid.draw(batch);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) {

            for (int i = 0; i < points.size - 1; i++) {
                Vector2 sp = new Vector2(points.get(i));
                Vector2 ep = new Vector2(points.get(i + 1));

                float rotation = MathUtils.atan2(ep.y - sp.y, ep.x - sp.x);

                sprite.setOrigin(0, sprite.getHeight() / 2);
                sprite.setRotation(MathUtils.radiansToDegrees * rotation);

                float width = new Vector2(ep.x - sp.x, ep.y - sp.y).len();
                float height = 10;

                sprite.setBounds(sp.x, sp.y - sprite.getHeight() / 2, width, height);

                sprite.setColor(Constants.COLOR);
                sprite.draw(batch);
            }

            for (int i = 0; i < primayPoints.size - 1; i++) {
                Vector2 sp = new Vector2(primayPoints.get(i));
                Vector2 ep = new Vector2(primayPoints.get(i + 1));

                float rotation = MathUtils.atan2(ep.y - sp.y, ep.x - sp.x);

                sprite.setOrigin(0, sprite.getHeight() / 2);
                sprite.setRotation(MathUtils.radiansToDegrees * rotation);

                float width = new Vector2(ep.x - sp.x, ep.y - sp.y).len();
                float height = 10;

                sprite.setBounds(sp.x, sp.y - sprite.getHeight() / 2, width, height);

                sprite.setColor(Constants.COLOR);
                sprite.setAlpha(0.4f);
                sprite.draw(batch);
                sprite.setAlpha(1);

            }

            for (int i = 0; i < secondaryPoints.size - 1; i++) {
                Vector2 sp = new Vector2(secondaryPoints.get(i));
                Vector2 ep = new Vector2(secondaryPoints.get(i + 1));

                float rotation = MathUtils.atan2(ep.y - sp.y, ep.x - sp.x);

                sprite.setOrigin(0, sprite.getHeight() / 2);
                sprite.setRotation(MathUtils.radiansToDegrees * rotation);

                float width = new Vector2(ep.x - sp.x, ep.y - sp.y).len();
                float height = 10;

                sprite.setBounds(sp.x, sp.y - sprite.getHeight() / 2, width, height);

                sprite.setColor(Constants.COLOR);
                sprite.setAlpha(0.4f);
                sprite.draw(batch);
                sprite.setAlpha(1);

            }


        }
        else {
            points.clear();
            primayPoints.clear();
            secondaryPoints.clear();
        }

        batch.draw(plane,startPoint.x-plane.getRegionWidth()*1/8,startPoint.y-plane.getRegionWidth()*1/8,plane.getRegionWidth()*1/4,plane.getRegionHeight()*1/4);


        batch.end();

     //   b2dr.render(world,camera.combined.scl(Constants.PPM));
        camera.update();
        world.step(1/60f,6,2);

    }

    private void createBolt(){
        Vector2 bolt = new Vector2(endPoint.x-startPoint.x,endPoint.y-startPoint.y);
        float boltMag = bolt.len();
        bolt.nor();

        float normOffset = 5;

        int NO_OF_CIRCLES = (int) (boltMag*0.1);

        points.clear();
        points.add(startPoint);
        for(int i = 1;i<=NO_OF_CIRCLES;i +=1){
            float offset = boltMag/NO_OF_CIRCLES;
            float RADIUS = MathUtils.random(1,10);

            Vector2 v1 = new Vector2(bolt.x,bolt.y).scl(i*offset);
            Vector2 circleCenter = v1.add(startPoint);

            circleCenter.x += (normOffset)*MathUtils.sin(i);

            float theta = MathUtils.random(MathUtils.PI/4,3*MathUtils.PI/4);

            Vector2 p = new Vector2(RADIUS*MathUtils.cos(theta),RADIUS*MathUtils.sin(theta));

            Vector2 randomPoint = new Vector2(circleCenter.x+p.x,circleCenter.y+p.y);

            points.add(randomPoint);

        }
        points.add(endPoint);

    }

    private void createPrimaryBolt(){

       // endPoint.mulAdd(new Vector2(1,0),MathUtils.randomTriangular(-1,1,0));
        Vector2 bolt = new Vector2(endPoint.x-startPoint.x,endPoint.y-startPoint.y);
        bolt.x = bolt.x +MathUtils.random(-5,5);

        float boltMag = bolt.len();
        bolt.nor();

        float normOffset = 5;

        int NO_OF_CIRCLES = (int) (boltMag*0.1);

        primayPoints.clear();
        primayPoints.add(startPoint);
        for(int i = 1;i<=NO_OF_CIRCLES;i +=1){
            float offset = boltMag/NO_OF_CIRCLES;
            float RADIUS = MathUtils.random(1,10);

            Vector2 v1 = new Vector2(bolt.x,bolt.y).scl(i*offset);
            Vector2 circleCenter = v1.add(startPoint);

          //  circleCenter.x += (normOffset)*MathUtils.sin(i);

            float theta = MathUtils.random(MathUtils.PI/4,3*MathUtils.PI/4);

            Vector2 p = new Vector2(RADIUS*MathUtils.cos(theta),RADIUS*MathUtils.sin(theta));

            Vector2 randomPoint = new Vector2(circleCenter.x+p.x,circleCenter.y+p.y);

            primayPoints.add(randomPoint);

        }
        primayPoints.add(endPoint);

    }

    private void createSecondaryBolt(){

       // endPoint.mulAdd(new Vector2(1,0),MathUtils.randomTriangular(-1,1,0));
        Vector2 bolt = new Vector2(endPoint.x-startPoint.x,endPoint.y-startPoint.y);
        bolt.x = bolt.x +MathUtils.random(-5,5);
        float boltMag = bolt.len();
        bolt.nor();

        float normOffset = 5;

        int NO_OF_CIRCLES = (int) (boltMag*0.1);

        secondaryPoints.clear();
        secondaryPoints.add(startPoint);
        for(int i = 1;i<=NO_OF_CIRCLES;i +=1){
            float offset = boltMag/NO_OF_CIRCLES;
            float RADIUS = MathUtils.random(1,10);

            Vector2 v1 = new Vector2(bolt.x,bolt.y).scl(i*offset);
            Vector2 circleCenter = v1.add(startPoint);

         //   circleCenter.x += (normOffset)*MathUtils.sin(i);

            float theta = MathUtils.random(MathUtils.PI/4,3*MathUtils.PI/4);

            Vector2 p = new Vector2(RADIUS*MathUtils.cos(theta),RADIUS*MathUtils.sin(theta));

            Vector2 randomPoint = new Vector2(circleCenter.x+p.x,circleCenter.y+p.y);

            secondaryPoints.add(randomPoint);

        }
        secondaryPoints.add(endPoint);

    }


    private void addHitRegions(){
        for(int i = 2;i<64;i++){
            hitRegions.add(manager.get("images/texAtlas.atlas", TextureAtlas.class).findRegion("tile",i));
        }

        hitAnimation = new Animation<TextureRegion>(1/61f,hitRegions, Animation.PlayMode.NORMAL);
    }

    private void setRegion(Asteroid asteroid){
        if(asteroid.hit){
            asteroid.setRegion(hitAnimation.getKeyFrame(asteroid.getTimer()));
        }
    }


    public World getWorld() {
        return world;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width,height,true);
    }


    @Override
    public void dispose() {
        renderer.dispose();
    }
}
