package mafiadelprimobanco.focusproject.model;

public enum ActivityType
{
	CHRONOMETER("activity.chronometer"),
	TIMER("activity.timer"),
	POMODORO("activity.pomodoro");

	public final String key;

	ActivityType(String key) { this.key = key; }
}
