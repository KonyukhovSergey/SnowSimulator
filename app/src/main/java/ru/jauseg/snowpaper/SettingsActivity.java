package ru.jauseg.snowpaper;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;

public class SettingsActivity extends Activity
{
//	@Override
//	protected void onCreate(Bundle savedInstanceState)
//	{
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.layout_settings);
//	}

	private SeekBarControl seekBarMotionBlur;
	private SeekBarControl seekBarTouchSensitivity;
	private SeekBarControl seekBarTurbulence;
	private SeekBarControl seekBarFramesSkip;
	private SeekBarControl seekBarSnowCount;
	private SeekBarControl seekBarSnowSpeed;
	private SeekBarControl seekBarParallax;

	private boolean isControls = true;
	private ImageButton buttonToggleControls;
	private Button buttonDefaultSettings;

	private Animation animationRoateCW;
	private Animation animationRoateCCW;

	private Animation animationButtonPress;

	private Animation animationMoveToUp;
	private Animation animationMoveFromUp;

	private View layoutControls;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_settings);

		seekBarMotionBlur = new SeekBarControl(app.indexMotionBlur(), 0, app.tableMotionBlur.length - 1, findViewById(R.id.slider_motion_blur), R.string.motion_blur_effect);
		seekBarTouchSensitivity = new SeekBarControl(app.indexTouchSensitivity(), 0, app.tableTouchSensitivity.length - 1, findViewById(R.id.slider_touch_sensitivity), R.string.touch_sensitivity);
		seekBarTurbulence = new SeekBarControl(app.indexTurbulence(), 0, app.tableTurbulence.length - 1, findViewById(R.id.slider_turbulence), R.string.turbulence);
		seekBarParallax = new SeekBarControl(app.indexParallax(), 0, app.tableParallax.length - 1, findViewById(R.id.slider_parallax), R.string.parallax);
		seekBarFramesSkip = new SeekBarControl(app.indexFramesSkip(), 0, 6, findViewById(R.id.slider_frames_skip), R.string.frames_skip);
		seekBarSnowCount = new SeekBarControl(app.indexSnowCount(), 0, app.tableSnowCount.length - 1, findViewById(R.id.slider_snow_count), R.string.snow_count);
		seekBarSnowSpeed = new SeekBarControl(app.indexSnowSpeed(), 0, app.tableSnowSpeed.length - 1, findViewById(R.id.slider_snow_speed), R.string.snow_speed);

		seekBarMotionBlur.setOnSeekBarControlPositionChangeListener(onSeekBarControlPositionChangeListener);
		seekBarTouchSensitivity.setOnSeekBarControlPositionChangeListener(onSeekBarControlPositionChangeListener);
		seekBarTurbulence.setOnSeekBarControlPositionChangeListener(onSeekBarControlPositionChangeListener);
		seekBarParallax.setOnSeekBarControlPositionChangeListener(onSeekBarControlPositionChangeListener);
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

		app.isBackgroundStatic(true);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		finish();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}

	private SeekBarControl.OnSeekBarControlPositionChangeListener onSeekBarControlPositionChangeListener = new SeekBarControl.OnSeekBarControlPositionChangeListener()
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

				case R.id.slider_parallax:
					app.indexParallax(pos);
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
			buttonToggleControls.setImageResource(R.drawable.ic_arrow_down);
			layoutControls.startAnimation(animationMoveToUp);
			layoutControls.setVisibility(View.GONE);
			buttonDefaultSettings.setVisibility(View.GONE);
			isControls = false;
		} else
		{
			buttonToggleControls.startAnimation(animationRoateCCW);
			buttonToggleControls.setImageResource(R.drawable.ic_arrow_up);
			layoutControls.startAnimation(animationMoveFromUp);
			layoutControls.setVisibility(View.VISIBLE);
			buttonDefaultSettings.setVisibility(View.VISIBLE);
			isControls = true;
		}
	}

	private void buttonDefaultSettingsClick()
	{
		buttonDefaultSettings.startAnimation(animationButtonPress);
		seekBarFramesSkip.position(0);
		seekBarMotionBlur.position(4);
		seekBarSnowCount.position(3);
		seekBarSnowSpeed.position(2);
		seekBarTouchSensitivity.position(3);
		seekBarTurbulence.position(3);
		seekBarParallax.position(4);
	}

	private View.OnClickListener onClickListener = new View.OnClickListener()
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
