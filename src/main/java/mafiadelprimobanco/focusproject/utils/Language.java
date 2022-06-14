package mafiadelprimobanco.focusproject.utils;

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
}
