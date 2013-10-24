package ru.jauseg.snowpaper;

import android.content.Context;
import js.utils.ApplicationPreferencesBase;

public class SnowSettings extends ApplicationPreferencesBase
{
	public static final String FRAMES_SKIP = "frames_skip;0";
	public static final String MOTION_BLUR = "motion_blur;4";
	public static final String TOUCH_SENSITIVITY = "touch_sensitivity;3";
	public static final String TURBULENCE = "tourbulence;3";
	public static final String SNOW_SPEED = "snow_speed;2";
	public static final String SNOW_COUNT = "snow_count;3";
	public static final String BACKGROUND = "background;static";

	public SnowSettings(Context context, String name)
	{
		super(context, name);
	}
}
