package com.pixmeg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MainScreen extends ScreenAdapter {

    private GameClass gameClass;
    private AssetManager manager;

    private OrthographicCamera camera;
    private Viewport viewport;

    private SpriteBatch batch;

    private Vector2 startPoint, endPoint;
    private Array<Vector2> points;

    private TextureRegion region;
    private Sprite sprite;

    float timer;


    public MainScreen(GameClass gameClass){
        this.gameClass = gameClass;
        manager = gameClass.manager;

        camera = new OrthographicCamera();
        viewport = new ExtendViewport(Constants.V_WIDTH,Constants.V_HEIGHT,camera);

        batch = gameClass.batch;

        startPoint = new Vector2();
        endPoint = new Vector2();

        points = new Array<Vector2>();

        region = manager.get("images/texAtlas.atlas", TextureAtlas.class).findRegion("bolt1");
        sprite = new Sprite(region);
        sprite.setOrigin(0,region.getRegionHeight()/2);
    }

    @Override
    public void show() {
        startPoint.set(240,240);
        endPoint.set(240,700);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);



        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) {

            timer += 1;

            if (timer > 2) {
                createLaser();
                timer = 0;

            }


            batch.setProjectionMatrix(camera.combined);
            batch.begin();


            for (int i = 0; i < points.size - 1; i++) {
                Vector2 sp = new Vector2(points.get(i));
                Vector2 ep = new Vector2(points.get(i + 1));

                float rotation = MathUtils.atan2(ep.y - sp.y, ep.x - sp.x);

                sprite.setOrigin(0, sprite.getHeight() / 2);
                sprite.setRotation(MathUtils.radiansToDegrees * rotation);

                float width = new Vector2(ep.x - sp.x, ep.y - sp.y).len();
                float height = 10;

                sprite.setBounds(points.get(i).x, points.get(i).y - sprite.getHeight() / 2, width, height);

                //sprite.setColor(Constants.COLOR);
                sprite.draw(batch);
            }

            batch.end();
        }
    }

    private void createLaser(){
        Vector2 laser = new Vector2(endPoint.x-startPoint.x,endPoint.y-startPoint.y);
        float lasserMag = laser.len();
        laser.nor();

        float normOffset = 5;

        int NO_OF_CIRCLES = 40;

        points.clear();
        points.add(startPoint);
        for(int i = 1;i<=NO_OF_CIRCLES;i +=1){
            float offset = lasserMag/NO_OF_CIRCLES;
            float RADIUS = MathUtils.random(1,10);

            Vector2 v1 = new Vector2(laser.x,laser.y).scl(i*offset);
            Vector2 circleCenter = v1.add(startPoint);

            circleCenter.x += (normOffset)*MathUtils.sin(i);

            float theta = MathUtils.random(MathUtils.PI/4,3*MathUtils.PI/4);

            Vector2 p = new Vector2(RADIUS*MathUtils.cos(theta),RADIUS* MathUtils.sin(theta));

            Vector2 randomPoint = new Vector2(circleCenter.x+p.x,circleCenter.y+p.y);

            points.add(randomPoint);

        }
        points.add(endPoint);

    }


    @Override
    public void resize(int width, int height) {
        viewport.update(width,height,true);
    }


    @Override
    public void dispose() {
    }
}
