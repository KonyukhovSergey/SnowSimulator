package ru.jauseg.snowpaper;

import android.content.Context;
import android.content.SharedPreferences;

public class ApplicationPreferencesBase
{
	private SharedPreferences prefences = null;

	public ApplicationPreferencesBase(Context context, String name)
	{
		if (context == null)
		{
			throw new NullPointerException("context == null");
		}

		prefences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
	}

	private String getKey(String key)
	{
		int separatorIndex = key.indexOf(';');
		return separatorIndex >= 0 ? key.substring(0, separatorIndex) : key;
	}

	private String getDefaultValue(String key)
	{
		int separatorIndex = key.indexOf(';');
		return separatorIndex >= 0 ? key.substring(separatorIndex + 1) : "";
	}

	public String get(String key)
	{
		String value = prefences.getString(getKey(key), null);

		if (value == null)
		{
			value = getDefaultValue(key);
		}

		return value;
	}

	public void set(String key, String value)
	{
		prefences.edit().putString(getKey(key), value).apply();
	}
}
