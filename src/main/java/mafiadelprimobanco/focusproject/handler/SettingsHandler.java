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

		settings.musicVolume.setValue(30.0);
		settings.showAdvancedOptions.setValue(true);
		settings.confirmBeforeExit.setValue(true);
		settings.currentLanguage.setValue(Language.ENGLISH);
		settings.currentTheme.setValue(Theme.LIGHT);
		settings.hideTutorial.setValue(true);
		settings.soundVolume.setValue(70.0);
		settings.blockNavigation.setValue(false);
		settings.showAdvancedOptions.setValue(false);
		settings.stopChronometerAfter.setValue(20);
		settings.pomodoroFocusTime.setValue(25);
		settings.pomodoroPauseTime.setValue(5);
		settings.minimumTimerTime.setValue(10);
		settings.resetTutorial.setValue(false);
	}

	public Settings getSettings() {return settings;}

}
