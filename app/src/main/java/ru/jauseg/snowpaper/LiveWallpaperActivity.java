package ru.jauseg.snowpaper;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import ru.jauseg.snowpaper.LiveWallpaperService;

public class LiveWallpaperActivity extends Activity
{
	protected static final int REQUEST_SET_LIVE_WALLPAPER = 100500;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode != 0)
		{
			finish();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setWallpaper();
		finish();
	}

	private void setWallpaper()
	{
		try
		{
			String packageName = LiveWallpaperService.class.getPackage().getName();
			String className = LiveWallpaperService.class.getCanonicalName();
			ComponentName component = new ComponentName(packageName, className);
			Intent intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
			intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, component);
			startActivityForResult(intent, REQUEST_SET_LIVE_WALLPAPER);
		}
		catch (ActivityNotFoundException e3)
		{
			try
			{
				Intent intent = new Intent(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER);
				finish();
				startActivity(intent);
			}
			catch (ActivityNotFoundException e2)
			{
				try
				{
					Intent intent = new Intent();
					intent.setAction("com.bn.nook.CHANGE_WALLPAPER");
					finish();
					startActivity(intent);
				}
				catch (ActivityNotFoundException e)
				{
					Toast.makeText(getBaseContext(), R.string.app_name, Toast.LENGTH_LONG).show();
				}
			}
		}
	}
}
