package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.screens.PlayScreen;

public class TiledApp extends Game {
	//Virtual Width and Height (11 BY 9 TILES)
	public static final int V_WIDTH = 16 * 11;
	public static final int V_HEIGHT = 16 * 9;//16 * 9;
	public static final float PPM = 32; //32 pixels is a meter
	//public static final float PPM = 1.13636363636f;

	public static AssetManager ASSET_MANAGER;

	public SpriteBatch batch;
	Texture img;

	@Override
	public void create () {
		Gdx.app.log("widd", "" + Gdx.graphics.getWidth());
		ASSET_MANAGER = new AssetManager();
		batch = new SpriteBatch();
		setScreen(new PlayScreen(this));
		img = new Texture("badlogic.jpg");
	}


	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
		ASSET_MANAGER.dispose();
	}
}
