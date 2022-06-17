package mafiadelprimobanco.focusproject.model;

import mafiadelprimobanco.focusproject.handler.Localization;

import java.time.temporal.ChronoUnit;

public enum Interval
{
//	HOUR("interval.hour", ChronoUnit.HOURS),
	DAY("interval.day", ChronoUnit.DAYS),
	WEEK("interval.week", ChronoUnit.WEEKS),
	MONTH("interval.month", ChronoUnit.MONTHS),
	YEAR("interval.year", ChronoUnit.YEARS);

	public final String key;
	public final ChronoUnit unit;

	Interval(String key, ChronoUnit unit)
	{
		this.key = key;
		this.unit = unit;
	}

	@Override
	public String toString()
	{
		return Localization.get(key);
	}
}
