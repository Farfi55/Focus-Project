package mafiadelprimobanco.focusproject.utils;

import mafiadelprimobanco.focusproject.handler.Localization;

import java.util.Locale;

public enum Language
{
	ITALIAN("settings.language.italian", Locale.ITALIAN), ENGLISH("settings.language.english", Locale.ENGLISH);

	public final String key;
	public final Locale language;

	Language(String key, Locale language)
	{
		this.key = key;
		this.language = language;
	}

	public static Language parse(String key)
	{
		for (var language : Language.values())
		{
			if (language.key.equals(key)) return language;
		}
		return null;
	}


	@Override
	public String toString()
	{
		return Localization.get(key);
	}
}
