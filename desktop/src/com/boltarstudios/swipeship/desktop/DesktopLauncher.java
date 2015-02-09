package com.boltarstudios.swipeship.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.boltarstudios.swipeship.SwipeShipMain;


public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Swipe Ship";
		config.width = 450;
		config.height = 800;
		new LwjglApplication(new SwipeShipMain(), config);
	}
}
