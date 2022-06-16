package mafiadelprimobanco.focusproject.handler;

import mafiadelprimobanco.focusproject.model.Settings;
import mafiadelprimobanco.focusproject.utils.Language;
import mafiadelprimobanco.focusproject.utils.Theme;

public class SettingsHandler
{
	private static final SettingsHandler instance = new SettingsHandler();
	private Settings settings;

	public static SettingsHandler getInstance()
	{
		return instance;
	}



	private SettingsHandler()
	{
		this.settings = new Settings();
		loadSettingsFromDatabase(this.settings);
	}

	private void loadSettingsFromDatabase(Settings settings)
	{

		settings.setMusicVolume(30.0);
		settings.setAdvancedOptionsShowing(true);
		settings.setConfirmBeforeExit(true);
		settings.setCurrentLanguage(Language.ENGLISH);
		settings.setCurrentTheme(Theme.LIGHT);
		settings.setHideTutorial(true);
		settings.setSoundVolume(70.0);
		settings.setNavigationBlock(false);
		settings.setAdvancedOptionsShowing(false);
		settings.setStopChronometerAfter(20);
		settings.setPomodoroFocusTime(25);
		settings.setPomodoroPauseTime(5);
		settings.setMinimumTimerTime(10);
		settings.setResetTutorial(false);
	}

	public Settings getSettings() {return settings;}

}