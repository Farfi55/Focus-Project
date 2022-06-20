package mafiadelprimobanco.focusproject.utils;

import mafiadelprimobanco.focusproject.handler.Localization;

public enum Theme
{
	LIGHT("settings.theme.light", "light"),
	LIGHT_ALT("settings.theme.lightAlt", "light-alt"),
	DARK("settings.theme.dark", "dark"),
	DARK_ALT("settings.theme.darkAlt", "dark-alt"),
	LEGACY("settings.theme.legacy", "light-legacy");

	public final String key;
	public final String fileName;

	Theme(String key, String fileName)
	{
		this.key = key;
		this.fileName = fileName;
	}

	@Override
	public String toString()
	{
		return Localization.get(key);
	}
}
