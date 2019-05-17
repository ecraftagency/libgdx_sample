package com.pixmeg;

/*I removed the asset shown in the demo video because they are licensed. If you want the same asset you can visit
                   https://www.gamedevmarket.net/asset/space-shooter-1570/        */

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class GameClass extends Game {
	public SpriteBatch batch;
	public AssetManager manager;

	@Override
	public void create () {
		batch = new SpriteBatch();
		manager = new AssetManager();
		manager.load("images/texAtlas.atlas", TextureAtlas.class);
		manager.finishLoading();

		//setScreen(new MainScreen(this));
		setScreen(new DemoScreen(this));
	}


	@Override
	public void dispose () {
		batch.dispose();
	}
}
