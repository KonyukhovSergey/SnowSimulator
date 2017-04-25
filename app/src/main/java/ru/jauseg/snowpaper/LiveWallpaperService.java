package ru.jauseg.snowpaper;

import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;

import ru.jauseg.snowpaper.renderer.LiveWallpaperRenderer;
import ru.serjik.engine.EngineView;
import ru.serjik.wallpaper.GLWallpaperService;

public class LiveWallpaperService extends GLWallpaperService
{
	private Renderer renderer;

	@Override
	public Renderer getRenderer(WallpaperEngine engine, EngineView view)
	{
		if (renderer == null)
		{
			renderer = new LiveWallpaperRenderer(getBaseContext(), view, engine);
		}
		return renderer;
	}
}
