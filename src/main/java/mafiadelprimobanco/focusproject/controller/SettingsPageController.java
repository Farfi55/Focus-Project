package mafiadelprimobanco.focusproject.controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXSlider;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.MFXToggleButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import mafiadelprimobanco.focusproject.Localization;
import mafiadelprimobanco.focusproject.handler.Feedback;
import mafiadelprimobanco.focusproject.handler.SettingsHandler;
import mafiadelprimobanco.focusproject.handler.StyleHandler;
import mafiadelprimobanco.focusproject.utils.Language;
import mafiadelprimobanco.focusproject.utils.Theme;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.*;

public class SettingsPageController implements Controller
{
	@FXML private Label advancedOptionCategoryLabel;

	@FXML private MFXButton advancedSettingsButton;

	@FXML private VBox advancedSettingsVBox;

	@FXML private Label audioCategoryLabel;

	@FXML private Label chronometerCategoryLabel;

	@FXML private Label confirmBeforeExitLabel;

	@FXML private MFXToggleButton confirmationRequestToggleButton;

	@FXML private Label focusTimeLabel;

	@FXML private Label generalCategoryLabel;

	@FXML private Label hideTutorialLabel;

	@FXML private MFXComboBox<Language> languageComboBox;

	@FXML private Label languageLabel;

	@FXML private Label minimumTimerLabel;

	@FXML private MFXTextField minimumTimerTextField;

	@FXML private MFXSlider musicSlider;

	@FXML private Label musicLabel;
	@FXML private Label navigationBlockLabel;

	@FXML private MFXToggleButton navigationToggleButton;

	@FXML private Label pageTitle;

	@FXML private Label pausePeriodLabel;

	@FXML private Label pomodoroCategoryLabel;

	@FXML private MFXTextField pomodoroFocusTimeTextField;

	@FXML private MFXTextField pomodoroPauseTimeTextField;

	@FXML private Label soundLabel;

	@FXML private MFXSlider soundSlider;

	@FXML private Label stopChronometerLabel;

	@FXML private MFXTextField stopChronometerTextField;

	@FXML private MFXComboBox<Theme> themeComboBox;

	@FXML private Label themeLabel;

	@FXML private Label timerCategoryLabel;

	@FXML private Label tutorialResetLabel;

	@FXML private MFXToggleButton tutorialResetToggleButton;

	@FXML private MFXToggleButton tutorialToggleButton;

	private SettingsHandler settingsHandler;

	private Feedback feedback;

	private Pattern inputValidation;


	@Override
	public void initialize(URL url, ResourceBundle resourceBundle)
	{
		Localization.setLabel(pageTitle, "settingsPage.name");
		settingsHandler = SettingsHandler.getInstance();


		// Set settings initial state from SettingsHandler instance
		updateStringsBasedOnCurrentLanguage();
		updateCurrentLanguage();

		updateAvailableLanguages();

		updateAvailableThemes();

		updateNavigationToggleButton();

		updateSoundVolume();
		updateMusicVolume();

		updateTimerTextField();


		updatePomodoroFocusTimeTextField();
		updatePomodoroPauseTimeTextField();

		updateChronometerTextField();

		updateThemeComboBox();

		updateTutorialResetToggleButton();
		updateTutorialShowingToggleButton();

		updateAdvancedSettingsVisibility();
		updateConfirmationRequestToggleBox();
		// --------------------------------


		feedback = Feedback.getInstance();


		inputValidation = Pattern.compile("[1-9]\\d{0,2}");
		setGlobalMeasureUnit("min", 0.5);


		subscribePomodoroFocusTimeTextField();
		subScribePomodoroPauseTimeTextField();

		subscribeTimerTextField();

		subscribeChronometerTextField();

		subscribeMusicSlider();
		subscribeSoundSlider();

		subscribeLanguageComboBox();
		subscribeThemesComboBox();

		subscribeNavigationToggleButton();

		subscribeShowingTutorialToggleButton();

		subscribeTutorialResetToggleButton();

		subscribeConfirmationRequestToggleButton();
	}


	// Language combo box behaviour
	void updateStringsBasedOnCurrentLanguage()
	{
		//todo: bind label text using LocalizationUtils
		generalCategoryLabel.setText(Localization.get("settings.general.categoryName"));

		languageLabel.setText(Localization.get("settings.language"));

		navigationBlockLabel.setText(Localization.get("settings.navigationBlock"));

		audioCategoryLabel.setText(Localization.get("settings.audio.categoryName"));
		soundLabel.setText(Localization.get("settings.audio.sound"));
		musicLabel.setText(Localization.get("settings.audio.music"));

		timerCategoryLabel.setText(Localization.get("settings.timer.categoryName"));
		minimumTimerLabel.setText(Localization.get("settings.timer.minimumActivityTime"));

		pomodoroCategoryLabel.setText(Localization.get("settings.pomodoro.categoryName"));
		pausePeriodLabel.setText(Localization.get("settings.pomodoro.focusPeriod"));
		focusTimeLabel.setText(Localization.get("settings.pomodoro.pausePeriod"));

		chronometerCategoryLabel.setText(Localization.get("settings.chronometer.categoryName"));
		stopChronometerLabel.setText(Localization.get("settings.chronometer.stopActivityAfter"));

		themeLabel.setText(Localization.get("settings.theme"));

		tutorialResetLabel.setText(Localization.get("settings.advancedOptions.tutorialReset"));

		advancedOptionCategoryLabel.setText(Localization.get("settings.advancedOptions.categoryName"));
		advancedSettingsButton.setText(Localization.get("settings.advancedOptions.show"));

		confirmBeforeExitLabel.setText(Localization.get("settings.advancedOptions.askConfirmationBeforeQuitting"));

		hideTutorialLabel.setText(Localization.get("settings.hideTutorial"));

		themeComboBox.setText(settingsHandler.getSettings().currentTheme.getValue().toString());

	}

	private void updateCurrentLanguage()
	{
		languageComboBox.setText(settingsHandler.getSettings().currentLanguage.getValue().toString());
	}

	private void subscribeLanguageComboBox()
	{
		languageComboBox.selectedItemProperty().addListener((observer, latestLanguage, newLanguage) ->
		{
			if (newLanguage != null && !newLanguage.toString().equals(
					settingsHandler.getSettings().currentLanguage.toString()))
			{
				settingsHandler.getSettings().currentLanguage.set(newLanguage);
				updateStringsBasedOnCurrentLanguage();
			}
		});

		settingsHandler.getSettings().currentLanguage.addListener(observable ->
		{
			updateCurrentLanguage();
		});

		// todo move: why is this here?
		Localization.localeProperty().addListener(observable -> updateAvailableThemes());
	}

	private void updateAvailableLanguages()
	{
		languageComboBox.getItems().clear();
		for (Language language : Language.values())
			languageComboBox.getItems().add(language);
	}
	// --------------------------


	// Theme combo box behaviour
	private void subscribeThemesComboBox()
	{
		themeComboBox.selectedItemProperty().addListener((observer, oldValue, newValue) ->
		{
			if (newValue != null)
			{
				//todo refactor: a bit unsafe, theme localized in english has to be equal to file name
				// also this should be in StyleHandler
				StyleHandler.getInstance().setTheme(Localization.get(newValue.key, Locale.ENGLISH));
				settingsHandler.getSettings().currentTheme.set(newValue);
				updateThemeComboBox();
				updateSelectedTheme();
			}
		});

		settingsHandler.getSettings().currentTheme.addListener(observable -> updateThemeComboBox());
	}

	private void updateAvailableThemes()
	{
		themeComboBox.getItems().clear();
		for (Theme theme : Theme.values())
			themeComboBox.getItems().add(theme);
		updateSelectedTheme();
	}

	private void updateSelectedTheme()
	{
		themeComboBox.selectItem(settingsHandler.getSettings().currentTheme.getValue());
		themeComboBox.setText(Localization.get(settingsHandler.getSettings().currentTheme.getValue().key));
	}

	private void updateThemeComboBox()
	{
		themeComboBox.setText(settingsHandler.getSettings().currentTheme.getValue().toString());
	}
	// --------------------------


	// Navigation Behaviour
	private void updateNavigationToggleButton()
	{
		navigationToggleButton.setSelected(settingsHandler.getSettings().blockNavigation.getValue());
	}

	private void subscribeNavigationToggleButton()
	{
		navigationToggleButton.selectedProperty().addListener((observableValue, oldValue, newValue) ->
		{
			settingsHandler.getSettings().blockNavigation.set(newValue);
		});

		settingsHandler.getSettings().blockNavigation.addListener(observable -> updateNavigationToggleButton());
	}
	// --------------------------


	// Audio sliders behaviour
	private void updateMusicVolume()
	{
		musicSlider.setValue(settingsHandler.getSettings().musicVolume.getValue());
	}

	private void updateSoundVolume()
	{
		soundSlider.setValue(settingsHandler.getSettings().soundVolume.getValue());
	}

	private void subscribeMusicSlider()
	{
		musicSlider.pressedProperty().addListener((observer, whenScrollEnd, whenScrollStarts) ->
		{
			if (!whenScrollStarts)
			{
				settingsHandler.getSettings().musicVolume.set(musicSlider.getValue());
			}
		});

		settingsHandler.getSettings().musicVolume.addListener(observable ->
		{
			updateMusicVolume();
		});
	}

	private void subscribeSoundSlider()
	{
		soundSlider.pressedProperty().addListener((observer, whenScrollEnd, whenScrollStarts) ->
		{
			if (!whenScrollStarts)
			{
				settingsHandler.getSettings().soundVolume.set(soundSlider.getValue());
			}
		});

		settingsHandler.getSettings().soundVolume.addListener(observable ->
		{
			updateSoundVolume();
		});
	}
	// --------------------------


	// Timer Behaviour
	private void updateTimerTextField()
	{
		minimumTimerTextField.setText(settingsHandler.getSettings().minimumTimerTime.getValue().toString());
	}

	private void subscribeTimerTextField()
	{
		minimumTimerTextField.delegateFocusedProperty().addListener((observable, oldStatus, newStatus) ->
		{
			if (!newStatus) validateMinimumTimerValue();
		});

		minimumTimerTextField.setOnAction(event -> validateMinimumTimerValue());

		settingsHandler.getSettings().minimumTimerTime.addListener(observable ->
		{
			updateTimerTextField();
		});
	}

	private void validateMinimumTimerValue()
	{
		String input = removeLeadingZeros(minimumTimerTextField.getText());

		if (!validateInput(input))
		{
			feedback.showError(Localization.get("error.settings.invalidTime.header"),
					Localization.get("error.settings.invalidTime.message"));
			minimumTimerTextField.setText(
					settingsHandler.getSettings().minimumTimerTime.getValue().toString());
		}
		else
		{
			minimumTimerTextField.setText(input);
			settingsHandler.getSettings().minimumTimerTime.setValue(Integer.valueOf(input));
		}
	}
	// --------------------------


	// Pomodoro behaviour
	private void updatePomodoroFocusTimeTextField()
	{
		pomodoroFocusTimeTextField.setText(settingsHandler.getSettings().pomodoroFocusTime.getValue().toString());
	}

	private void subscribePomodoroFocusTimeTextField()
	{
		pomodoroFocusTimeTextField.delegateFocusedProperty().addListener((observable, oldStatus, newStatus) ->
		{
			if (!newStatus) validatePomodoroFocusTimeValue();
		});

		pomodoroFocusTimeTextField.setOnAction(event -> validatePomodoroFocusTimeValue());

		settingsHandler.getSettings().pomodoroFocusTime.addListener(observable ->
		{
			updatePomodoroFocusTimeTextField();
		});
	}

	private void validatePomodoroFocusTimeValue()
	{
		String input = removeLeadingZeros(pomodoroFocusTimeTextField.getText());

		if (!validateInput(input))
		{
			feedback.showError(Localization.get("error.settings.invalidTime.header"),
					Localization.get("error.settings.invalidTime.message"));
			pomodoroFocusTimeTextField.setText(
					settingsHandler.getSettings().pomodoroFocusTime.getValue().toString());
		}
		else
		{
			pomodoroFocusTimeTextField.setText(input);
			settingsHandler.getSettings().pomodoroFocusTime.setValue(Integer.valueOf(input));
		}
	}

	private void updatePomodoroPauseTimeTextField()
	{
		pomodoroPauseTimeTextField.setText(settingsHandler.getSettings().pomodoroPauseTime.getValue().toString());
	}

	private void subScribePomodoroPauseTimeTextField()
	{
		pomodoroPauseTimeTextField.delegateFocusedProperty().addListener((observable, oldStatus, newStatus) ->
		{
			if (!newStatus) validatePomodoroPauseTimeValue();
		});
		pomodoroPauseTimeTextField.setOnAction(event -> validatePomodoroPauseTimeValue());

		settingsHandler.getSettings().pomodoroPauseTime.addListener(observable ->
		{
			updatePomodoroPauseTimeTextField();
		});
	}

	private void validatePomodoroPauseTimeValue()
	{
		String input = removeLeadingZeros(pomodoroPauseTimeTextField.getText());

		if (!validateInput(input))
		{
			feedback.showError(Localization.get("error.settings.invalidTime.header"),
					Localization.get("error.settings.invalidTime.message"));
			updatePomodoroPauseTimeTextField();
		}
		else
		{
			settingsHandler.getSettings().pomodoroPauseTime.setValue(Integer.valueOf(input));
			updatePomodoroPauseTimeTextField();
		}
	}
	// --------------------------


	// Chronometer behaviour
	private void updateChronometerTextField()
	{
		stopChronometerTextField.setText(settingsHandler.getSettings().stopChronometerAfter.getValue().toString());
	}

	private void subscribeChronometerTextField()
	{
		stopChronometerTextField.delegateFocusedProperty().addListener((observable, oldStatus, newStatus) ->
		{
			if (!newStatus) validateChronometerTextFieldValue();
		});
		stopChronometerTextField.setOnAction(event -> validateChronometerTextFieldValue());

		settingsHandler.getSettings().stopChronometerAfter.addListener(observable ->
		{
			updateChronometerTextField();
		});
	}

	private void validateChronometerTextFieldValue()
	{
		String input = removeLeadingZeros(stopChronometerTextField.getText());

		if (!validateInput(input))
		{
			feedback.showError(Localization.get("error.settings.invalidTime.header"),
					Localization.get("error.settings.invalidTime.message"));
			updateChronometerTextField();
		}
		else
		{
			settingsHandler.getSettings().stopChronometerAfter.setValue(Integer.valueOf(input));
			updateChronometerTextField();
		}
	}
	// --------------------------


	// Tutorial Behaviour
	private void updateTutorialShowingToggleButton()
	{
		tutorialToggleButton.setSelected(settingsHandler.getSettings().hideTutorial.get());
	}

	private void subscribeShowingTutorialToggleButton()
	{
		tutorialToggleButton.selectedProperty().addListener((observableValue, oldValue, newValue) ->
		{
			settingsHandler.getSettings().hideTutorial.setValue(newValue);
		});

		settingsHandler.getSettings().hideTutorial.addListener(observable ->
		{
			updateTutorialShowingToggleButton();
		});
	}
	// --------------------------


	// -- Advanced Settings behavior --
	private void updateAdvancedSettingsVisibility()
	{
		advancedSettingsVBox.setVisible(settingsHandler.getSettings().showAdvancedOptions.get());
	}

	private void updateConfirmationRequestToggleBox()
	{
		confirmationRequestToggleButton.setSelected(
				settingsHandler.getSettings().confirmBeforeExit.get());
	}

	private void subscribeConfirmationRequestToggleButton()
	{
		confirmationRequestToggleButton.selectedProperty().addListener((observableValue, oldValue, newValue) ->
		{
			settingsHandler.getSettings().confirmBeforeExit.setValue(newValue);
		});

		settingsHandler.getSettings().confirmBeforeExit.addListener(observable ->
		{
			updateConfirmationRequestToggleBox();
		});
	}

	private void updateTutorialResetToggleButton()
	{
		tutorialResetToggleButton.setSelected(settingsHandler.getSettings().resetTutorial.get());
	}

	private void subscribeTutorialResetToggleButton()
	{
		tutorialResetToggleButton.selectedProperty().addListener((observableValue, oldValue, newValue) ->
		{
			settingsHandler.getSettings().resetTutorial.setValue(newValue);
		});

		settingsHandler.getSettings().resetTutorial.addListener(observable ->
		{
			updateTutorialResetToggleButton();
		});
	}

	@FXML
	void onMouseClickedAdvancedSettingsButton(MouseEvent event)
	{
		if (!advancedSettingsVBox.isVisible())
		{
			advancedSettingsVBox.setVisible(true);
			advancedSettingsButton.setText(Localization.get("settings.advancedOptions.hide"));
			advancedSettingsVBox.setManaged(true);
		}
		else
		{
			advancedSettingsVBox.setVisible(false);
			advancedSettingsButton.setText(Localization.get("settings.advancedOptions.show"));
			advancedSettingsVBox.setManaged(false);
		}
	}
	// --------------------------


	// -- Text boxes behaviour --
	private void setGlobalMeasureUnit(String measureUnit, Double gap)
	{
		minimumTimerTextField.setMeasureUnit(measureUnit);
		minimumTimerTextField.setMeasureUnitGap(gap);

		pomodoroFocusTimeTextField.setMeasureUnit(measureUnit);
		pomodoroFocusTimeTextField.setMeasureUnitGap(gap);

		pomodoroPauseTimeTextField.setMeasureUnit(measureUnit);
		pomodoroPauseTimeTextField.setMeasureUnitGap(gap);

		stopChronometerTextField.setMeasureUnit(measureUnit);
		stopChronometerTextField.setMeasureUnitGap(gap);
	}

	Boolean validateInput(String input)
	{
		Matcher matcher = inputValidation.matcher(input);

		return matcher.matches();
	}

	String removeLeadingZeros(String input)
	{
		return input.replaceFirst("^0+(?!$)", "");
	}
	// --------------------------


	@Override
	public void terminate()
	{

	}
}
