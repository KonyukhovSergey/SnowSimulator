package ru.jauseg.snowpaper;

import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;

import ru.jauseg.snowpaper.renderer.LiveWallpaperRenderer;
import ru.serjik.engine.EngineView;
import ru.serjik.wallpaper.GLWallpaperService;

public class LiveWallpaperService extends GLWallpaperService
{
	@Override
	public Renderer getRenderer(WallpaperEngine engine, EngineView view)
	{
		return new LiveWallpaperRenderer(getBaseContext(), view, engine);
	}
}
