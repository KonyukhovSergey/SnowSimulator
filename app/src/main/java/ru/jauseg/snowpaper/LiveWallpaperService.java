package ru.jauseg.snowpaper;

import android.opengl.GLSurfaceView.Renderer;
import ru.jauseg.snowpaper.renderer.LiveWallpaperRenderer;
import ru.serjik.wallpaper.GLWallpaperService;

public class LiveWallpaperService extends GLWallpaperService
{
	@Override
	public Renderer getRenderer(GLWallpaperEngine engine)
	{
		return new LiveWallpaperRenderer(this, engine);
	}
}
