package ru.jauseg.snowpaper;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import js.engine.BufferAllocator;
import js.jni.code.NativeCalls;
import android.app.Application;
import android.content.Context;
import android.hardware.SensorManager;

public class app extends Application
{
	private static int indexFramesSkip;
	private static int indexMotionBlur;
	private static int indexTouchSensitivity;
	private static int indexTurbulence;
	private static int indexParalax;
	private static int indexSnowCount;
	private static int indexSnowSpeed;
	private static boolean isBackgroundStatic;

	public static SnowSettings cfg = null;
	public static SnowSystemNative ss = null;

	private static int maxShowCount = 4096;

	public static Accelerometer accelerometer;

	@Override
	public void onCreate()
	{
		super.onCreate();

		accelerometer = new Accelerometer(getBaseContext());

		cfg = new SnowSettings(getBaseContext(), "snow");

		indexFramesSkip = Integer.parseInt(cfg.get(SnowSettings.FRAMES_SKIP));
		indexMotionBlur = Integer.parseInt(cfg.get(SnowSettings.MOTION_BLUR));
		indexTouchSensitivity = Integer.parseInt(cfg.get(SnowSettings.TOUCH_SENSITIVITY));
		indexTurbulence = Integer.parseInt(cfg.get(SnowSettings.TURBULENCE));
		indexParalax = Integer.parseInt(cfg.get(SnowSettings.PARALAX));
		indexSnowCount = Integer.parseInt(cfg.get(SnowSettings.SNOW_COUNT));
		indexSnowSpeed = Integer.parseInt(cfg.get(SnowSettings.SNOW_SPEED));
		isBackgroundStatic = cfg.get(SnowSettings.BACKGROUND).equals("static");

	}

	public static FloatBuffer vertexCoords = BufferAllocator.createFloatBuffer(maxShowCount * 12);
	public static FloatBuffer textureCoords = BufferAllocator.createFloatBuffer(maxShowCount * 12);
	public static ByteBuffer vertexColors = BufferAllocator.createBuffer(maxShowCount * 4 * 6);
	public static FloatBuffer texBase = BufferAllocator.createFloatBuffer(12);

	public static final float[] tableMotionBlur = new float[] { 1.00f, 0.81f, 0.64f, 0.49f, 0.36f, 0.25f, 0.16f, 0.09f,
			0.04f };
	public static final float[] tableTouchSensitivity = new float[] { 0.10f, 0.25f, 0.33f, 0.50f, 0.66f, 0.75f, 1.00f };
	public static final float[] tableTurbulence = { 0.10f, 0.25f, 0.33f, 0.5f, 0.66f, 0.75f, 1.00f };

	public static final float[] tableParallax = { 0.00f, 0.05f, 0.10f, 0.15f, 0.20f, 0.25f, 0.30f, 0.35f, 0.40f };

	public static final int[] tableSnowCount = { 32, 16, 8, 4, 2 };
	public static final float[] tableSnowSpeed = { 1.0f / 64.0f, 1.0f / 48.0f, 1.0f / 32.0f, 1.0f / 24.0f, 1.0f / 16.0f };

	public static int indexFramesSkip()
	{
		return indexFramesSkip;
	}

	public static int indexMotionBlur()
	{
		return indexMotionBlur;
	}

	public static int indexTouchSensitivity()
	{
		return indexTouchSensitivity;
	}

	public static int indexTurbulence()
	{
		return indexTurbulence;
	}

	public static int indexParalax()
	{
		return indexParalax;
	}

	public static int indexSnowCount()
	{
		return indexSnowCount;
	}

	public static int indexSnowSpeed()
	{
		return indexSnowSpeed;
	}

	public static boolean isBackgroundStatic()
	{
		return isBackgroundStatic;
	}

	public static void isBackgroundStatic(boolean value)
	{
		isBackgroundStatic = value;
		cfg.set(SnowSettings.BACKGROUND, value ? "static" : "noise");
	}

	public static void indexSnowCount(int value)
	{
		indexSnowCount = value;
		cfg.set(SnowSettings.SNOW_COUNT, Integer.toString(value));
		NativeCalls.ssSetSnowCount(tableSnowCount[value]);
	}

	public static void indexSnowSpeed(int value)
	{
		indexSnowSpeed = value;
		cfg.set(SnowSettings.SNOW_SPEED, Integer.toString(value));
		NativeCalls.ssSetSnowSpeed(tableSnowSpeed[value]);
	}

	public static void indexFramesSkip(int value)
	{
		indexFramesSkip = value;
		cfg.set(SnowSettings.FRAMES_SKIP, Integer.toString(value));
	}

	public static void indexMotionBlur(int value)
	{
		indexMotionBlur = value;
		cfg.set(SnowSettings.MOTION_BLUR, Integer.toString(value));
	}

	public static void indexTouchSensitivity(int value)
	{
		indexTouchSensitivity = value;
		cfg.set(SnowSettings.TOUCH_SENSITIVITY, Integer.toString(value));
	}

	public static void indexTurbulence(int index)
	{
		indexTurbulence = index;
		cfg.set(SnowSettings.TURBULENCE, Integer.toString(index));
		NativeCalls.ssSetTurbulence(tableTurbulence[index]);
	}

	public static void indexParalax(int index)
	{
		indexParalax = index;
		cfg.set(SnowSettings.PARALAX, Integer.toString(index));
		NativeCalls.ssSetParallax(tableParallax[index]);
	}

	public static float getMotionBlur()
	{
		return tableMotionBlur[indexMotionBlur];
	}

	public static float getTouchSensitivity()
	{
		return tableTouchSensitivity[indexTouchSensitivity];
	}
}
