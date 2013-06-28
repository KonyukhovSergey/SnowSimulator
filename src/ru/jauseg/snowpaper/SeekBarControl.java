package ru.jauseg.snowpaper;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class SeekBarControl
{
	private Animation buttonAnimation;
	
	private View view;
	private int min;
	private int max;

	private SeekBar seekBar;
	private int progressStep = 1;

	private OnSeekBarControlPositionChangeListener onSeekBarControlPositionChangeListener;

	public interface OnSeekBarControlPositionChangeListener
	{
		void onSeekBarControlPositionChange(int id, int pos, int min, int max);
	}

	public SeekBarControl(int pos, int min, int max, View view, int textResourceId)
	{
		this.view = view;
		
		buttonAnimation = AnimationUtils.loadAnimation(view.getContext(), R.anim.button_press);

		seekBar = (SeekBar) view.findViewById(R.id.seek_bar_position);

		view.findViewById(R.id.button_left).setOnClickListener(onClickListener);
		view.findViewById(R.id.button_right).setOnClickListener(onClickListener);

		seekBar.setMax(max - min);
		seekBar.setProgress(pos);

		((TextView) view.findViewById(R.id.text_name)).setText(textResourceId);

		seekBar.setOnSeekBarChangeListener(onSeekBarMotionBlurChangeListener);
	}

	public void setOnSeekBarControlPositionChangeListener(
			OnSeekBarControlPositionChangeListener onSeekBarControlPositionChangeListener)
	{
		this.onSeekBarControlPositionChangeListener = onSeekBarControlPositionChangeListener;
	}
	
	public void position(int value)
	{
		seekBar.setProgress(value);
	}

	private OnClickListener onClickListener = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			v.startAnimation(buttonAnimation);
			
			switch (v.getId())
			{
				case R.id.button_left:
					seekBar.setProgress(seekBar.getProgress() - progressStep);
					break;

				case R.id.button_right:
					seekBar.setProgress(seekBar.getProgress() + progressStep);
					break;
			}
		}
	};

	private OnSeekBarChangeListener onSeekBarMotionBlurChangeListener = new OnSeekBarChangeListener()
	{
		@Override
		public void onStopTrackingTouch(SeekBar seekBar)
		{
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar)
		{
		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
		{
			if (onSeekBarControlPositionChangeListener != null)
			{
				onSeekBarControlPositionChangeListener.onSeekBarControlPositionChange(view.getId(), progress + min,
						min, max);
			}
		}
	};
}
