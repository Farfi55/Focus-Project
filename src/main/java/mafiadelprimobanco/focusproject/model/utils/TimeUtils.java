package mafiadelprimobanco.focusproject.model.utils;

public class TimeUtils
{
	/**
	 * provides a String representation of the given time
	 * @return {@code millis} in hh:mm:ss format
	 */
	public static String formatTime(int seconds) {
		int hours = (seconds / 3600);
		int minutes = (seconds % 3600) / 60;
		seconds = seconds % 60;
		return String.format("%02d:%02d:%02d", hours, minutes, seconds);
	}
}
