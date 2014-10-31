package ru.jauseg.snowpaper;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class Accelerometer implements SensorEventListener
{
	private int runCount = 0;
	private SensorManager sensorManager;

	public volatile float ax, ay, az;

	public Accelerometer(Context context)
	{
		try
		{
			sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void start()
	{
		try
		{
			if (runCount == 0 && sensorManager != null)
			{
				sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
						SensorManager.SENSOR_DELAY_FASTEST);
				//Log.v(this.getClass().getSimpleName(), "register");
			}
			runCount++;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			runCount = 0;
		}
	}

	public void stop()
	{
		runCount--;
		if (runCount == 0)
		{
			sensorManager.unregisterListener(this);
			//Log.v(this.getClass().getSimpleName(), "unregister");
		}

		if (runCount < 0)
		{
			runCount = 0;
		}
	}

	@Override
	public void onSensorChanged(SensorEvent event)
	{
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
		{
			ax = event.values[0];
			ay = event.values[1];
			az = event.values[2];
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy)
	{
	}
}
