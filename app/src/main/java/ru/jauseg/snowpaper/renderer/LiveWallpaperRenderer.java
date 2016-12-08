package ru.jauseg.snowpaper.renderer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.content.res.AssetManager;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.view.MotionEvent;
import android.view.View;

import ru.jauseg.snowpaper.LiveWallpaperService;
import ru.serjik.engine.EngineView;
import ru.serjik.engine.gles20.ShaderProgram;
import ru.serjik.utils.TimeCounter;
import ru.serjik.wallpaper.GLWallpaperService.WallpaperEngine;
import ru.serjik.wallpaper.WallpaperOffsetsListener;

public class LiveWallpaperRenderer implements Renderer, WallpaperOffsetsListener
{
	private Context context;
	private WallpaperEngine engine;
	private EngineView view;

	private AssetManager am;

	private TimeCounter timeCounter = new TimeCounter();

	private float offsetTarget = 0;
	private float offset = 0;

	public LiveWallpaperRenderer(Context context, EngineView view, WallpaperEngine engine)
	{
		view.setFrameRate(30);
		engine.setWallpaperOffsetsListener(this);
		this.context = context;
		this.engine = engine;
		am = context.getAssets();
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config)
	{
		ShaderProgram.releaseCompiler();
		GLES20.glClearColor(255, 0, 0, 0);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height)
	{
		if(width>0)
		{
			GLES20.glViewport(0, 0, width, height);
			GLES20.glDisable(GLES20.GL_DEPTH_TEST);
			GLES20.glEnable(GLES20.GL_TEXTURE_2D);
			GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		}
	}

	@Override
	public void onDrawFrame(GL10 gl)
	{
		float deltaTime = timeCounter.deltaTimeSeconds();
		offset += (offsetTarget - offset) * 0.1f;

		GLES20.glClear(GL10.GL_COLOR_BUFFER_BIT);
	}

	@Override
	public void onOffsetChanged(float xOffset)
	{
		offsetTarget = xOffset;
	}
}
