package mafiadelprimobanco.focusproject.handler;

import mafiadelprimobanco.focusproject.model.Settings;
import mafiadelprimobanco.focusproject.utils.Language;
import mafiadelprimobanco.focusproject.utils.Theme;

public class SettingsHandler
{
	private static final SettingsHandler instance = new SettingsHandler();
	private final Settings settings;

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
		settings.areAdvancedOptionsShown.setValue(true);
		settings.confirmQuitApplication.setValue(true);
		settings.language.setValue(Language.ENGLISH);
		settings.theme.setValue(Theme.LIGHT);
		settings.isTutorialHidden.setValue(true);
		settings.musicVolume.setValue(30.0);
		settings.soundVolume.setValue(70.0);
		settings.navigationDisabledDuringActivity.setValue(false);
		settings.confirmInterruptChronometerActivity.setValue(true);
		settings.confirmInterruptTimerActivity.setValue(true);
		settings.minimumSuccessfulChronometerDuration.setValue(120);
		settings.minimumTimerDuration.setValue(20);
		settings.resetTutorial.setValue(false);
	}

	public Settings getSettings() {return settings;}

}
