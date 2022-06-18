package mafiadelprimobanco.focusproject.utils;

import mafiadelprimobanco.focusproject.Localization;

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

	@Override
	public String toString()
	{
		return Localization.get(key);
	}
}
