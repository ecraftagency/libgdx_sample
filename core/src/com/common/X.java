package com.common;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ParticleEffectLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import java.lang.String;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class X { //XAssetManager
  public static int ATLAS  = 1;
  public static int ANI    = 1<<1;
  public static int PAR    = 1<<2;
  public static int SHAD   = 1<<3;
  public static int SND    = 1<<4;
  public static int MUS    = 1<<5;
  public static int FONT   = 1<<6;

  private AssetManager assetManager;
  public static X inst = new X();
  private List<String> textureAtlasPaths;
  private List<String> animationPaths;
  private List<String> particlesPaths;
  private List<String> shaderPaths;
  private List<String> levelPaths;
  private List<String> bitmapFontPaths;
  private List<String> soundPaths;
  private List<String> musicPaths;

  private Map<Type, Map<String, Object>> cache;

  public static void init(int mask) {
    inst.assetManager = new AssetManager();//GAssetsManager.getAssetManager();
    inst.textureAtlasPaths = new ArrayList<>();
    inst.animationPaths = new ArrayList<>();
    inst.particlesPaths = new ArrayList<>();
    inst.shaderPaths = new ArrayList<>();
    inst.levelPaths = new ArrayList<>();
    inst.bitmapFontPaths = new ArrayList<>();
    inst.soundPaths = new ArrayList<>();
    inst.musicPaths = new ArrayList<>();
    inst.cache = new HashMap<>();
    if ((mask&ATLAS) != 0) {
      inst.initResourcePath(".*\\.atlas", "textureAtlas/", inst.textureAtlasPaths);
      inst.loadTextureAtlases();
    }
    if ((mask&ANI) != 0) {
      inst.initResourcePath(".*\\.png", "animation/", inst.animationPaths);
      inst.loadAnimations();
    }
    if ((mask&PAR) != 0) {
      inst.initResourcePath(".*\\.p", "particle/", inst.particlesPaths);
      inst.loadParticles();
    }
    if ((mask&SHAD) != 0) {
      inst.initResourcePath(".*", "shaders/", inst.shaderPaths);
      inst.loadShaderProgram();
    }
    if ((mask&SND) != 0) {
      inst.initResourcePath(".*\\.mp3", "sound/", inst.soundPaths);
      inst.loadSound();
    }
    if ((mask&MUS) != 0) {
      inst.initResourcePath(".*\\.mp3", "music/", inst.musicPaths);
      inst.loadMusic();
    }
    if ((mask&FONT) != 0) {
      inst.initResourcePath(".*\\.mp3", "font/", inst.bitmapFontPaths);
      inst.loadBitmapFont();
    }
  }

  public static int getAssetCount(Type type) {
    return inst.cache.get(type).size();
  }

  //MUSIC section////////////////////////////////////////////////////////////////////////////////////////
  private void loadMusic() {
    cache.put(Music.class, new HashMap<>());
    for (String name : musicPaths) {
      assetManager.load("music/" + name, Music.class);
    }
    assetManager.finishLoading();

    for (String name : musicPaths) {
      Music music = assetManager.get("music/" + name, Music.class);
      HashMap<String, Object> musicMap = (HashMap<String, Object>)cache.get(Music.class);
      String a[] = name.split("\\.");
      musicMap.put(a[0], music);
    }
  }

  public static Music getMusic(String key) {
    return (Music)inst.cache.get(Music.class).get(key);
  }

  //SOUND section////////////////////////////////////////////////////////////////////////////////////////
  private void loadSound() {
    cache.put(Sound.class, new HashMap<>());
    for (String name : soundPaths) {
      assetManager.load("sound/" + name, Sound.class);
    }
    assetManager.finishLoading();

    for (String name : soundPaths) {
      Sound sound = assetManager.get("sound/" + name, Sound.class);
      HashMap<String, Object> soundMap = (HashMap<String, Object>)cache.get(Sound.class);
      String a[] = name.split("\\.");
      soundMap.put(a[0], sound);
    }
  }

  public static Sound getSound(String key) {
    return (Sound)inst.cache.get(Sound.class).get(key);
  }

  //BITMAP FONT section//////////////////////////////////////////////////////////////////////////////////

  private void loadBitmapFont() {
    cache.put(BitmapFont.class, new HashMap<>());
    for (String name : bitmapFontPaths) {
      assetManager.load("font/" + name, BitmapFont.class);
    }
    assetManager.finishLoading();
    for (String name : bitmapFontPaths) {
      BitmapFont font = assetManager.get("font/" + name, BitmapFont.class);
      for (int i = 0; i < font.getRegions().size; i++) {
        font.getRegions().get(i).getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
      }
      HashMap<String, Object> fontMap = (HashMap<String, Object>)cache.get(BitmapFont.class);
      String a[] = name.split("\\.");
      fontMap.put(a[0], font);
    }
  }

  public static BitmapFont getBitmapFont(String key) {
    BitmapFont font =  new BitmapFont(Gdx.files.internal("font/" + key + ".fnt"));
    for (int i = 0; i < font.getRegions().size; i++) {
      font.getRegions().get(i).getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }
    return font;
  }

  //LEVEL region//////////////////////////////////////////////////////////////////////////////////////

  private void loadLevelsJson() {
    cache.put(String.class, new HashMap<>());
    for (String name : levelPaths) {
      String jsonStr = Gdx.files.internal("levels/" + name).readString();
      HashMap<String, Object> levelMap = (HashMap<String, Object>)cache.get(String.class);
      String a[] = name.split("\\.");
      levelMap.put(a[0], jsonStr);
    }
  }

  public static String getLevelJson(String key) {
    return (String)inst.cache.get(String.class).get(key);
  }

  //SHADER Region/////////////////////////////////////////////////////////////////////////////////////

  private void loadShaderProgram() {
    cache.put(ShaderProgram.class, new HashMap<>());
    for (String name : shaderPaths){
      String vertexShader = Gdx.files.internal("shaders/" + name + "/vertex.glsl").readString();
      String fragmentShader = Gdx.files.internal("shaders/" + name + "/fragment.glsl").readString();
      ShaderProgram shaderProgram = new ShaderProgram(vertexShader,fragmentShader);
      ShaderProgram.pedantic = false;
      HashMap<String, Object> shaderMap = (HashMap<String, Object>) cache.get(ShaderProgram.class);
      shaderMap.put(name, shaderProgram);
    }
  }

  public static ShaderProgram getShaderProgram(String key) {
    return (ShaderProgram)inst.cache.get(ShaderProgram.class).get(key);
  }

  ////////////////////////////////////////////////////////////////////////////////////////////////////

  //Particle section
  private void loadParticles() {
    cache.put(ParticleEffect.class, new HashMap<>());
    cache.put(ParticleEffectPool.class, new HashMap<>());
    ParticleEffectLoader.ParticleEffectParameter pep = new ParticleEffectLoader.ParticleEffectParameter();
    pep.atlasFile = "particle/particle.atlas";
    for (String name : particlesPaths) {
      assetManager.load("particle/" + name, ParticleEffect.class, pep);
    }
    assetManager.finishLoading();

    for (String name : particlesPaths) {
      String a[] = name.split("\\.");
      ParticleEffect pe = assetManager.get("particle/" + name, ParticleEffect.class);
      HashMap<String, Object> particleMap = (HashMap<String, Object>) cache.get(ParticleEffect.class);
      HashMap<String, Object> particlePoolMap = (HashMap<String, Object>) cache.get(ParticleEffectPool.class);
      particleMap.put(a[0], pe);
      particlePoolMap.put(a[0], new ParticleEffectPool(pe, 4, 10));
    }
  }

  public static ParticleEffectPool getParticleEffectPool(String key) {
    return (ParticleEffectPool)inst.cache.get(ParticleEffectPool.class).get(key);
  }
  /////////////////////////////////////////////////////////////////////////////////////

  //Animation section
  private void loadAnimations() {
    cache.put(Texture.class, new HashMap<>());
    for (String name : animationPaths) {
      assetManager.load("animation/" + name, Texture.class);
    }

    assetManager.finishLoading();

    for (String name : animationPaths) {
      Texture texture = assetManager.get("animation/" + name, Texture.class);
      texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
      HashMap<String, Object> textureCache = (HashMap<String, Object>) cache.get(Texture.class);
      String a[] = name.split("\\.");
      textureCache.put(a[0], texture);
    }
  }

  public static Animation<TextureRegion> getAnimation(String key, int frameCol, int frameRow, float frameDuration) {
    Texture walkSheet = (Texture)inst.cache.get(Texture.class).get(key);
    TextureRegion[][] tmp = TextureRegion.split(walkSheet,
            walkSheet.getWidth() / frameCol,
            walkSheet.getHeight() / frameRow);

    TextureRegion[] walkFrames = new TextureRegion[frameCol * frameRow];
    int index = 0;
    for (int i = 0; i < frameRow; i++) {
      for (int j = 0; j < frameCol; j++) {
        walkFrames[index++] = tmp[i][j];
      }
    }

    Animation<TextureRegion> walkAnimation = new Animation<TextureRegion>(frameDuration, walkFrames);
    return walkAnimation;
  }

  /////////////////////////////
  //texture atlas & region section
  private void loadTextureAtlases() {
    cache.put(TextureRegion.class, new HashMap<>());
    for (String name : textureAtlasPaths) {
      assetManager.load("textureAtlas/" + name, TextureAtlas.class);
    }

    assetManager.finishLoading();

    for (String name : textureAtlasPaths) {
      TextureAtlas atlas = assetManager.get("textureAtlas/" + name, TextureAtlas.class);
      Array<TextureAtlas.AtlasRegion> regions =  atlas.getRegions();
      for (TextureAtlas.AtlasRegion region : regions) {
        TextureRegion tg = atlas.findRegion(region.name);
        tg.getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        HashMap<String, Object> textureCache = (HashMap<String, Object>) cache.get(TextureRegion.class);
        textureCache.put(region.name, tg);
      }
    }
  }

  public static TextureRegion getTextureRegion(String key) {
    return (TextureRegion) inst.cache.get(TextureRegion.class).get(key);
  }
  //////////////////////////////////////////////

  private void initResourcePath(final String pattern, final String path, List<String> result) {
    FileHandle dirHandle;
    if (Gdx.app.getType() == Application.ApplicationType.Android) {
      dirHandle = Gdx.files.internal(path);
    } else {
      dirHandle = Gdx.files.internal(path);
    }
    for (FileHandle entry: dirHandle.list()) {
      if (entry.name().matches(pattern)) {
        result.add(entry.name());
      }
    }
  }

  public static void dispose() {
    for (Map.Entry<Type, Map<String, Object>> entry : inst.cache.entrySet()) {
      Type type = entry.getKey();
      HashMap<String, Object> subCategory = (HashMap<String, Object>)entry.getValue();
      for (Map.Entry<String, Object> subEntry : subCategory.entrySet()) {
        if (type.equals(TextureRegion.class)) {
          TextureRegion tg = (TextureRegion) subEntry.getValue();
          tg.getTexture().dispose();
        }
        if (type.equals(Texture.class)) {
          Texture tg = (Texture) subEntry.getValue();
          tg.dispose();
        }
        if (type.equals(ShaderProgram.class)) {
          ShaderProgram sp = (ShaderProgram)subEntry.getValue();
          sp.dispose();
        }
        if (type.equals(ParticleEffect.class)) {
          ParticleEffect pe = (ParticleEffect)subEntry.getValue();
          pe.dispose();
        }

        if (type.equals(BitmapFont.class)) {
          BitmapFont bf = (BitmapFont)subEntry.getValue();
          bf.dispose();
        }

        if (type.equals(Sound.class)) {
          Sound s = (Sound)subEntry.getValue();
          s.dispose();
        }

        if (type.equals(Music.class)) {
          Music m = (Music)subEntry.getValue();
          m.dispose();
        }
      }
    }
  }
}