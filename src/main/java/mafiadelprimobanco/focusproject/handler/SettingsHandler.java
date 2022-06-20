package mafiadelprimobanco.focusproject.handler;

import javafx.application.Platform;
import mafiadelprimobanco.focusproject.model.Settings;
import mafiadelprimobanco.focusproject.utils.Language;
import mafiadelprimobanco.focusproject.utils.Theme;

import java.util.Locale;

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
		loadSettingsFromDatabase();
	}

	private void loadDefaultSettings()
	{
		setDefaultSettings(settings);
	}

	private void setDefaultSettings(Settings settings)
	{
		settings.confirmQuitApplication.setValue(true);
		settings.language.setValue(Language.ENGLISH);
		settings.theme.setValue(Theme.LIGHT);
		settings.musicVolume.setValue(30.0);
		settings.soundVolume.setValue(30.0);
		settings.navigationDisabledDuringActivity.setValue(true);
		settings.confirmInterruptChronometerActivity.setValue(false);
		settings.confirmInterruptTimerActivity.setValue(true);
		settings.minimumSuccessfulChronometerDuration.setValue(120);
		settings.minimumTimerDuration.setValue(120);
		settings.areAdvancedOptionsShown.setValue(false);

		settings.isTutorialHidden.setValue(false);
		settings.resetTutorial.setValue(false);
	}

	public void loadSettingsFromDatabase()
	{
		var settingsJson = JsonHandler.getSettings();

		if (settingsJson == null || settingsJson.isEmpty())
		{
			loadDefaultSettings();
			return;
		}

		settings.musicVolume.setValue(settingsJson.getNumber("musicVolume"));
		settings.areAdvancedOptionsShown.setValue(settingsJson.getBoolean("areAdvancedOptionsShown"));
		settings.confirmQuitApplication.setValue(settingsJson.getBoolean("confirmQuitApplication"));
		settings.language.setValue(Language.parse(settingsJson.getString("language")));
		settings.theme.setValue(Theme.parse(settingsJson.getString("theme")));
		settings.isTutorialHidden.setValue(settingsJson.getBoolean("isTutorialHidden"));
		settings.soundVolume.setValue(settingsJson.getNumber("soundVolume"));
		settings.navigationDisabledDuringActivity.setValue(settingsJson.getBoolean("navigationDisabledDuringActivity"));
		settings.areAdvancedOptionsShown.setValue(settingsJson.getBoolean("areAdvancedOptionsShown"));
		settings.minimumSuccessfulChronometerDuration.setValue(
				settingsJson.getNumber("minimumSuccessfulChronometerDuration"));
		settings.confirmInterruptChronometerActivity.setValue(
				settingsJson.getBoolean("confirmInterruptChronometerActivity"));
		settings.confirmInterruptTimerActivity.setValue(settingsJson.getBoolean("confirmInterruptTimerActivity"));
		settings.minimumTimerDuration.setValue(settingsJson.getNumber("minimumTimerDuration"));
		settings.resetTutorial.setValue(settingsJson.getBoolean("resetTutorial"));

		Platform.runLater(() -> {
			Localization.setLocale(settings.language.getValue().language);
			StyleHandler.getInstance().setTheme(settings.theme.get());
		});
	}

	public Settings getSettings() { return settings; }

}