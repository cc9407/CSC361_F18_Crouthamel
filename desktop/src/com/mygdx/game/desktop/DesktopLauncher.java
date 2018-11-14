package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.retromario.CSC361_F18_Crouthamel;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;

public class DesktopLauncher {
	
	//private static boolean for atlas and outline
	private static boolean rebuildAtlas = false;
	private static boolean drawDebugOutline = true;
	
public static void main (String[] arg) {
		
		
		if (rebuildAtlas) {
			Settings settings = new Settings();
			settings.maxWidth = 1024;
			settings.maxHeight = 1024;
			settings.duplicatePadding = false;
			settings.debug = drawDebugOutline;

			TexturePacker.process(settings, "assets/images", "../core/assets/images","mario.atlas");
			
			}
			
			LwjglApplicationConfiguration cfg = new
			LwjglApplicationConfiguration();
			cfg.title = "Mario";
			cfg.width = 800;
			cfg.height = 480;
			new LwjglApplication(new CSC361_F18_Crouthamel(), cfg);
			}
}