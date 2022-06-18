package mafiadelprimobanco.focusproject.utils;

import mafiadelprimobanco.focusproject.Localization;

public enum Theme
{
	LIGHT("settings.theme.light"), DARK("settings.theme.dark");

	public final String key;

	Theme(String key)
	{
		this.key = key;
	}

	@Override
	public String toString()
	{
		return Localization.get(key);
	}
}
