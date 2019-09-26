package com.mygdx.game;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.mygdx.game.TiledApp;

public class AndroidLauncher extends AndroidApplication {
	//Android variables & LibGDX Application variable
	private int uiOptions;
	private View decorView;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initView();
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		initialize(new TiledApp(), config);
	}


	/**
	 * Initialize the view for the application. Full screen & hidden
	 * navigation.
	 */
	private void initView() {
		uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
				| View.SYSTEM_UI_FLAG_FULLSCREEN |
				View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
		decorView = getWindow().getDecorView();
		decorView.setSystemUiVisibility(uiOptions);
	}

	public void forceLandscape() {
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}

	public void forcePortrait() {
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
}
