package ru.jauseg.snowpaper;

import net.rbgrn.android.glwallpaperservice.GLWallpaperService;
import android.view.MotionEvent;

public class SnowWallpaperService extends GLWallpaperService
{
	public SnowWallpaperService()
	{
		super();
	}

	@Override
	public Engine onCreateEngine()
	{
		return new SnowWallpaperEngine();
	}

	class SnowWallpaperEngine extends GLEngine
	{
		SnowWallpaperRenderer renderer;

		public SnowWallpaperEngine()
		{
			super();
			renderer = new SnowWallpaperRenderer(SnowWallpaperService.this);
			setRenderer(renderer);
			setRenderMode(RENDERMODE_CONTINUOUSLY);
		}

		@Override
		public void onDestroy()
		{
			super.onDestroy();
			if (renderer != null)
			{
				renderer.release();
			}
			renderer = null;
		}

		@Override
		public void onTouchEvent(MotionEvent event)
		{
			renderer.onTouch(event);
		}

		@Override
		public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep,
				int xPixelOffset, int yPixelOffset)
		{
			// Log.v("Wallpaper1Service", String.format("%f %f %f %f %d %d",
			// xOffset, yOffset, xOffsetStep, yOffsetStep,
			// xPixelOffset, yPixelOffset));
			renderer.offset(xOffset);
			super.onOffsetsChanged(xOffset, yOffset, xOffsetStep, yOffsetStep, xPixelOffset, yPixelOffset);
		}
	}
}
