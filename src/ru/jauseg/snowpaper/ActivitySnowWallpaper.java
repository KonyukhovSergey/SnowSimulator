package ru.jauseg.snowpaper;

import ru.jauseg.snowpaper.SeekBarControl.OnSeekBarControlPositionChangeListener;
import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;

public class ActivitySnowWallpaper extends Activity
{
	private static final String TAG = "ActivitySnowWallpaper";

	private GLSurfaceView engine;
	private SnowWallpaperRenderer renderer;

	private SeekBarControl seekBarMotionBlur;
	private SeekBarControl seekBarTouchSensitivy;
	private SeekBarControl seekBarTurbulence;
	private SeekBarControl seekBarFramesSkip;
	private SeekBarControl seekBarSnowCount;
	private SeekBarControl seekBarSnowSpeed;

	private boolean isControls = true;
	private ImageButton buttonToggleControls;
	private Button buttonDefaultSettings;

	private Animation animationRoateCW;
	private Animation animationRoateCCW;

	private Animation animationButtonPress;

	private Animation animationMoveToUp;
	private Animation animationMoveFromUp;

	private View layoutControls;

	private CheckBox checkBoxBackground;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		engine = new GLSurfaceView(this);
		renderer = new SnowWallpaperRenderer(this);
		engine.setRenderer(renderer);
		engine.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
		setContentView(R.layout.setup);
		((ViewGroup) findViewById(R.id.layout_container)).addView(engine);

		seekBarMotionBlur = new SeekBarControl(app.indexMotionBlur(), 0, app.tableMotionBlur.length - 1,
				findViewById(R.id.slider_motion_blur), R.string.motion_blur_effect);

		seekBarTouchSensitivy = new SeekBarControl(app.indexTouchSensitivity(), 0,
				app.tableTouchSensitivity.length - 1, findViewById(R.id.slider_touch_sensitivity),
				R.string.touch_sensitivity);

		seekBarTurbulence = new SeekBarControl(app.indexTurbulence(), 0, app.tableTurbulence.length - 1,
				findViewById(R.id.slider_turbulence), R.string.turbulence);

		seekBarFramesSkip = new SeekBarControl(app.indexFramesSkip(), 0, 6, findViewById(R.id.slider_frames_skip),
				R.string.frames_skip);

		seekBarSnowCount = new SeekBarControl(app.indexSnowCount(), 0, app.tableSnowCount.length - 1,
				findViewById(R.id.slider_snow_count), R.string.snow_count);

		seekBarSnowSpeed = new SeekBarControl(app.indexSnowSpeed(), 0, app.tableSnowSpeed.length - 1,
				findViewById(R.id.slider_snow_speed), R.string.snow_speed);

		seekBarMotionBlur.setOnSeekBarControlPositionChangeListener(onSeekBarControlPositionChangeListener);
		seekBarTouchSensitivy.setOnSeekBarControlPositionChangeListener(onSeekBarControlPositionChangeListener);
		seekBarTurbulence.setOnSeekBarControlPositionChangeListener(onSeekBarControlPositionChangeListener);
		seekBarFramesSkip.setOnSeekBarControlPositionChangeListener(onSeekBarControlPositionChangeListener);
		seekBarSnowCount.setOnSeekBarControlPositionChangeListener(onSeekBarControlPositionChangeListener);
		seekBarSnowSpeed.setOnSeekBarControlPositionChangeListener(onSeekBarControlPositionChangeListener);

		buttonToggleControls = (ImageButton) findViewById(R.id.button_toggle_controls);
		buttonToggleControls.setOnClickListener(onClickListener);

		animationButtonPress = AnimationUtils.loadAnimation(this, R.anim.button_press);
		animationRoateCW = AnimationUtils.loadAnimation(this, R.anim.rotate_cw_180);
		animationRoateCCW = AnimationUtils.loadAnimation(this, R.anim.rotate_ccw_180);

		animationMoveToUp = AnimationUtils.loadAnimation(this, R.anim.move_to_up);
		animationMoveFromUp = AnimationUtils.loadAnimation(this, R.anim.move_from_up);
		layoutControls = findViewById(R.id.layout_controls);
		buttonDefaultSettings = (Button) findViewById(R.id.button_default_settings);
		buttonDefaultSettings.setOnClickListener(onClickListener);

		checkBoxBackground = (CheckBox) findViewById(R.id.check_box_background);
		checkBoxBackground.setChecked(app.isBackgroundStatic());
		
		checkBoxBackground.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				app.isBackgroundStatic(isChecked);
			}
		});
	}

	@Override
	protected void onResume()
	{
		Log.v(TAG, "onResume");
		engine.onResume();
		super.onResume();
	}

	@Override
	protected void onPause()
	{
		Log.v(TAG, "onPause");
		engine.onPause();
		super.onPause();
		finish();
	}

	@Override
	protected void onDestroy()
	{
		Log.v(TAG, "onDestroy");
		renderer.release();
		super.onDestroy();
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev)
	{
		renderer.onTouch(ev);
		return super.dispatchTouchEvent(ev);
	}

	private OnSeekBarControlPositionChangeListener onSeekBarControlPositionChangeListener = new OnSeekBarControlPositionChangeListener()
	{
		@Override
		public void onSeekBarControlPositionChange(int id, int pos, int min, int max)
		{
			switch (id)
			{
			case R.id.slider_motion_blur:
				app.indexMotionBlur(pos);
				break;

			case R.id.slider_touch_sensitivity:
				app.indexTouchSensitivity(pos);
				break;

			case R.id.slider_turbulence:
				app.indexTurbulence(pos);
				break;

			case R.id.slider_frames_skip:
				app.indexFramesSkip(pos);
				break;

			case R.id.slider_snow_count:
				app.indexSnowCount(pos);
				break;

			case R.id.slider_snow_speed:
				app.indexSnowSpeed(pos);
				break;

			default:
				break;
			}
		}
	};

	private void buttonToggleControlsClick()
	{
		findViewById(R.id.layout_toggle_background).startAnimation(animationButtonPress);

		if (isControls)
		{
			buttonToggleControls.startAnimation(animationRoateCW);
			buttonToggleControls.setImageResource(R.drawable.arrow_down);
			layoutControls.startAnimation(animationMoveToUp);
			layoutControls.setVisibility(View.GONE);
			buttonDefaultSettings.setVisibility(View.GONE);
			isControls = false;
		}
		else
		{
			buttonToggleControls.startAnimation(animationRoateCCW);
			buttonToggleControls.setImageResource(R.drawable.arrow_up);
			layoutControls.startAnimation(animationMoveFromUp);
			layoutControls.setVisibility(View.VISIBLE);
			buttonDefaultSettings.setVisibility(View.VISIBLE);
			isControls = true;
		}
	}

	private void buttonDefaultSettingsClick()
	{
		buttonDefaultSettings.startAnimation(animationButtonPress);

		// app.indexFramesSkip(0);
		// app.indexMotionBlur(4);
		// app.indexSnowCount(3);
		// app.indexSnowSpeed(2);
		// app.indexTouchSensitivity(3);
		// app.indexTurbulence(3);
		//
		seekBarFramesSkip.position(0);
		seekBarMotionBlur.position(4);
		seekBarSnowCount.position(3);
		seekBarSnowSpeed.position(2);
		seekBarTouchSensitivy.position(3);
		seekBarTurbulence.position(3);
	}

	private OnClickListener onClickListener = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			switch (v.getId())
			{
			case R.id.button_toggle_controls:
				buttonToggleControlsClick();
				break;

			case R.id.button_default_settings:
				buttonDefaultSettingsClick();
				break;
			}
		}
	};

}
