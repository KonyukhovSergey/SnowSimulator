package ru.jauseg.snowpaper;

import android.util.Log;
import android.view.MotionEvent;
import net.rbgrn.android.glwallpaperservice.GLWallpaperService;

public class Wallpaper1Service extends GLWallpaperService
{
	public Wallpaper1Service()
	{
		super();
	}

	@Override
	public Engine onCreateEngine()
	{
		return new Wallpaper1Engine();
	}

	class Wallpaper1Engine extends GLEngine
	{
		Wallpaper1Renderer renderer;

		public Wallpaper1Engine()
		{
			super();
			renderer = new Wallpaper1Renderer(Wallpaper1Service.this);
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
