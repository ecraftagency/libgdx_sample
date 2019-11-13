package com.games;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.common.XGame;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonBinary;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.esotericsoftware.spine.SkeletonRendererDebug;

public class Spine extends XGame {
  OrthographicCamera camera;
  SkeletonRenderer renderer;
  SkeletonRendererDebug debugRenderer;

  TextureAtlas atlas;
  Skeleton skeleton;
  AnimationState state;

  @Override
  public void _create() {
    camera = new OrthographicCamera();
    camera.setToOrtho(false, 1280, 720);
    renderer = new SkeletonRenderer();
    renderer.setPremultipliedAlpha(true); // PMA results in correct blending without outlines.
    debugRenderer = new SkeletonRendererDebug();
    debugRenderer.setBoundingBoxes(false);
    debugRenderer.setRegionAttachments(false);


    atlas = new TextureAtlas(Gdx.files.internal("skeletal/spineboy/spineboy-pma.atlas"));
    SkeletonBinary json = new SkeletonBinary(atlas);
    json.setScale(0.6f); // Load the skeleton at 60% the size it was in Spine.
    SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal("skeletal/spineboy/spineboy-ess.skel"));


    skeleton = new Skeleton(skeletonData); // Skeleton holds skeleton state (bone positions, slot attachments, etc).
    skeleton.setPosition(250, 20);

    AnimationStateData stateData = new AnimationStateData(skeletonData); // Defines mixing (crossfading) between animations.
    stateData.setMix("run", "jump", 0.2f);
    stateData.setMix("jump", "run", 0.2f);

    state = new AnimationState(stateData); // Holds the animation state for a skeleton (current animation, time, etc).
    state.setTimeScale(1f); // Slow all animations down to 50% speed.

    // Queue animations on track 0.
    state.setAnimation(0, "run", true);
    state.addAnimation(0, "jump", false, 2); // Jump after 2 seconds.
    state.addAnimation(0, "run", true, 0); // Run after the jump.
  }

  @Override
  public void render() {
    state.update(Gdx.graphics.getDeltaTime()); // Update the animation time.

    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    state.apply(skeleton); // Poses skeleton using current animations. This sets the bones' local SRT.
    skeleton.updateWorldTransform(); // Uses the bones' local SRT to compute their world SRT.

    // Configure the camera, SpriteBatch, and SkeletonRendererDebug.
    camera.update();
    batch.getProjectionMatrix().set(camera.combined);
    debugRenderer.getShapeRenderer().setProjectionMatrix(camera.combined);

    batch.begin();
    renderer.draw(batch, skeleton); // Draw the skeleton images.
    batch.end();

    debugRenderer.draw(skeleton); // Draw debug lines.
  }

  @Override
  public void resize(int width, int height) {
    camera.setToOrtho(false); // Update camera with new size.
  }

  @Override
  public void dispose() {
    atlas.dispose();
  }

  @Override
  public InputMultiplexer getInput() {
    return null;
  }
}
