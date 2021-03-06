package mafiadelprimobanco.focusproject.utils;

import mafiadelprimobanco.focusproject.handler.Localization;

public enum Theme
{
	LIGHT("settings.theme.light", "light"),
	LIGHT_ALT("settings.theme.lightAlt", "light-alt"),
	EXAM("settings.theme.exam", "exam"),
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

	public static Theme parse(String key){
		for (var theme : Theme.values()){
			if (theme.key.equals(key)) return theme;
		}
		return null;
	}

	@Override
	public String toString()
	{
		return Localization.get(key);
	}
}
