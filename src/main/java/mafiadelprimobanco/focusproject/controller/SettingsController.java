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

public class SettingsController implements Controller
{
	@FXML
	private Label advancedOptionCategoryLabel;

	@FXML
	private MFXButton advancedSettingsButton;

	@FXML
	private VBox advancedSettingsVBox;

	@FXML
	private Label audioCategoryLabel;

	@FXML
	private Label chronometerCategoryLabel;

	@FXML
	private Label confirmBeforeExitLabel;

	@FXML
	private MFXToggleButton confirmationRequestToggleButton;

	@FXML
	private Label focusTimeLabel;

	@FXML
	private Label generalCategoryLabel;

	@FXML
	private Label hideTutorialLabel;

	@FXML
	private MFXComboBox<Language> languageComboBox;

	@FXML
	private Label languageLabel;

	@FXML
	private Label minimumTimerLabel;

	@FXML
	private MFXTextField minimumTimerTextField;

	@FXML
	private MFXSlider musicSlider;

	@FXML
	private Label musicLabel;
	@FXML
	private Label navigationBlockLabel;

	@FXML
	private MFXToggleButton navigationToggleButton;

	@FXML
	private Label pageTitle;

	@FXML
	private Label pausePeriodLabel;

	@FXML
	private Label pomodoroCategoryLabel;

	@FXML
	private MFXTextField pomodoroFocusTimeTextField;

	@FXML
	private MFXTextField pomodoroPauseTimeTextField;

	@FXML
	private Label soundLabel;

	@FXML
	private MFXSlider soundSlider;

	@FXML
	private Label stopChronometerLabel;

	@FXML
	private MFXTextField stopChronometerTextField;

	@FXML
	private MFXComboBox<Theme> themeComboBox;

	@FXML
	private Label themeLabel;

	@FXML
	private Label timerCategoryLabel;

	@FXML
	private Label tutorialResetLabel;

	@FXML
	private MFXToggleButton tutorialResetToggleButton;

	@FXML
	private MFXToggleButton tutorialToggleButton;

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

		chronometerCategoryLabel.setText(Localization.get("setting.chronometer.categoryName"));
		stopChronometerLabel.setText(Localization.get("settings.chronometer.stopActivityAfter"));

		themeLabel.setText(Localization.get("settings.theme"));

		tutorialResetLabel.setText(Localization.get("settings.advancedOptions.tutorialReset"));

		advancedOptionCategoryLabel.setText(Localization.get("settings.advancedOptions.categoryName"));
		advancedSettingsButton.setText(Localization.get("settings.advancedOptions.show"));

		confirmBeforeExitLabel.setText(Localization.get("settings.advancedOptions.requestConfirmationBeforExiting"));

		hideTutorialLabel.setText(Localization.get("settings.hideTutorial"));

		themeComboBox.setText(settingsHandler.getSettings().getCurrentTheme().getValue().toString());

	}

	private void updateCurrentLanguage()
	{
		languageComboBox.setText(settingsHandler.getSettings().getCurrentLanguage().getValue().toString());
	}

	private void subscribeLanguageComboBox()
	{
		languageComboBox.selectedItemProperty().addListener((observer, latestLanguage, newLanguage) ->
		{
			if (!newLanguage.toString().equals(settingsHandler.getSettings().getCurrentLanguage().toString()))
			{
				settingsHandler.getSettings().setCurrentLanguage(newLanguage);
				updateStringsBasedOnCurrentLanguage();
			}
		});

		settingsHandler.getSettings().getCurrentLanguage().addListener(observable -> {updateCurrentLanguage();});

		Localization.localeProperty().addListener(observable -> updateAvailableThemes());
	}

	private void updateAvailableLanguages()
	{
		languageComboBox.getItems().addAll(Language.ITALIAN, Language.ENGLISH);
	}
	// --------------------------


	// Theme combo box behaviour
	private void subscribeThemesComboBox()
	{
		themeComboBox.selectedItemProperty().addListener((observer, oldValue, newValue) ->
		{
			if (newValue != null)
			{
				StyleHandler.getInstance().setTheme(Localization.get(newValue.key, Locale.ENGLISH));
				settingsHandler.getSettings().setCurrentTheme(newValue);
				updateThemeComboBox();
				updateSelectedTheme();
			}
		});

		settingsHandler.getSettings().getCurrentTheme().addListener(observable -> {updateThemeComboBox();});
	}

	private void updateAvailableThemes()
	{
		themeComboBox.getItems().clear();
		themeComboBox.getItems().addAll(Theme.LIGHT, Theme.DARK);
		updateSelectedTheme();
	}

	private void updateSelectedTheme()
	{
		themeComboBox.selectItem(settingsHandler.getSettings().getCurrentTheme().getValue());
		themeComboBox.setText(Localization.get(settingsHandler.getSettings().getCurrentTheme().getValue().key));
	}

	private void updateThemeComboBox()
	{
		themeComboBox.setText(settingsHandler.getSettings().getCurrentTheme().getValue().toString());
	}
	// --------------------------


	// Navigation Behaviour
	private void updateNavigationToggleButton()
	{
		navigationToggleButton.setSelected(settingsHandler.getSettings().isNavigationBlocked().getValue());
	}

	private void subscribeNavigationToggleButton()
	{
		navigationToggleButton.selectedProperty().addListener((observableValue, oldValue, newValue) ->
		{
			settingsHandler.getSettings().setNavigationBlock(newValue);
		});

		settingsHandler.getSettings().isNavigationBlocked().addListener(observable -> {updateNavigationToggleButton();});
	}
	// --------------------------


	// Audio sliders behaviour
	private void updateMusicVolume()
	{
		musicSlider.setValue(settingsHandler.getSettings().getMusicVolume().getValue());
	}

	private void updateSoundVolume()
	{
		soundSlider.setValue(settingsHandler.getSettings().getSoundVolume().getValue());
	}

	private void subscribeMusicSlider()
	{
		musicSlider.pressedProperty().addListener((observer, whenScrollEnd, whenScrollStarts) ->
		{
			if (!whenScrollStarts)
			{
				settingsHandler.getSettings().setMusicVolume(musicSlider.getValue());
			}
		});

		settingsHandler.getSettings().getMusicVolume().addListener(observable -> {updateMusicVolume();});
	}

	private void subscribeSoundSlider()
	{
		soundSlider.pressedProperty().addListener((observer, whenScrollEnd, whenScrollStarts) ->
		{
			if (!whenScrollStarts)
			{
				settingsHandler.getSettings().setSoundVolume(soundSlider.getValue());
			}
		});

		settingsHandler.getSettings().getSoundVolume().addListener(observable -> {updateSoundVolume();});
	}
	// --------------------------


	// Timer Behaviour
	private void updateTimerTextField()
	{
		minimumTimerTextField.setText(settingsHandler.getSettings().getMinimumTimerTime().getValue().toString());
	}

	private void subscribeTimerTextField()
	{
		minimumTimerTextField.delegateFocusedProperty().addListener((observable, oldStatus, newStatus) -> {

			if (!newStatus)
			{
				String input = removeLeadingZeros(minimumTimerTextField.getText());

				if (!validateInput(input))
				{
					feedback.showError(Localization.get("error.settings.invalidTime.header"), Localization.get("error.settings.invalidTime.message"));
					minimumTimerTextField.setText(settingsHandler.getSettings().getMinimumTimerTime().getValue().toString());
				}
				else
				{
					minimumTimerTextField.setText(input);
					settingsHandler.getSettings().setMinimumTimerTime(Integer.valueOf(input));
				}
			}
		});

		settingsHandler.getSettings().getMinimumTimerTime().addListener(observable -> {updateTimerTextField();});
	}
	// --------------------------


	// Pomodoro behaviour
	private void updatePomodoroFocusTimeTextField()
	{
		pomodoroFocusTimeTextField.setText(settingsHandler.getSettings().getPomodoroFocusTime().getValue().toString());
	}

	private void subscribePomodoroFocusTimeTextField()
	{
		pomodoroFocusTimeTextField.delegateFocusedProperty().addListener((observable, oldStatus, newStatus) -> {

			if (!newStatus)
			{
				String input = removeLeadingZeros(pomodoroFocusTimeTextField.getText());

				if (!validateInput(input))
				{
					feedback.showError(Localization.get("error.settings.invalidTime.header"), Localization.get("error.settings.invalidTime.message"));
					pomodoroFocusTimeTextField.setText(settingsHandler.getSettings().getPomodoroFocusTime().getValue().toString());
				}
				else
				{
					pomodoroFocusTimeTextField.setText(input);
					settingsHandler.getSettings().setPomodoroFocusTime(Integer.valueOf(input));
				}
			}
		});

		settingsHandler.getSettings().getPomodoroFocusTime().addListener(observable -> {updatePomodoroFocusTimeTextField();});
	}

	private void updatePomodoroPauseTimeTextField()
	{
		pomodoroPauseTimeTextField.setText(settingsHandler.getSettings().getPomodoroPauseTime().getValue().toString());
	}

	private void subScribePomodoroPauseTimeTextField()
	{
		pomodoroPauseTimeTextField.delegateFocusedProperty().addListener((observable, oldStatus, newStatus) -> {

			if (!newStatus)
			{
				String input = removeLeadingZeros(pomodoroPauseTimeTextField.getText());

				if (!validateInput(input))
				{
					feedback.showError(Localization.get("error.settings.invalidTime.header"), Localization.get("error.settings.invalidTime.message"));
					updatePomodoroPauseTimeTextField();
				}
				else
				{
					settingsHandler.getSettings().setPomodoroPauseTime(Integer.valueOf(input));
					updatePomodoroPauseTimeTextField();
				}
			}
		});

		settingsHandler.getSettings().getPomodoroPauseTime().addListener(observable -> {updatePomodoroPauseTimeTextField();});
	}
	// --------------------------


	// Chronometer behaviour
	private void updateChronometerTextField()
	{
		stopChronometerTextField.setText(settingsHandler.getSettings().getStopChronometerAfter().getValue().toString());
	}

	private void subscribeChronometerTextField()
	{
		stopChronometerTextField.delegateFocusedProperty().addListener((observable, oldStatus, newStatus) -> {

			if (!newStatus)
			{
				String input = removeLeadingZeros(stopChronometerTextField.getText());

				if (!validateInput(input))
				{
					feedback.showError(Localization.get("error.settings.invalidTime.header"), Localization.get("error.settings.invalidTime.message"));
					updateChronometerTextField();
				}
				else
				{
					settingsHandler.getSettings().setStopChronometerAfter(Integer.valueOf(input));
					updateChronometerTextField();
				}
			}
		});

		settingsHandler.getSettings().getStopChronometerAfter().addListener(observable -> {updateChronometerTextField();});
	}
	// --------------------------


	// Tutorial Behaviour
	private void updateTutorialShowingToggleButton()
	{
		tutorialToggleButton.setSelected(settingsHandler.getSettings().isTutorialHidden().get());
	}

	private void subscribeShowingTutorialToggleButton()
	{
		tutorialToggleButton.selectedProperty().addListener((observableValue, oldValue, newValue) ->
		{
			settingsHandler.getSettings().setHideTutorial(newValue);
		});

		settingsHandler.getSettings().isTutorialHidden().addListener(observable -> {updateTutorialShowingToggleButton();});
	}
	// --------------------------


	// -- Advanced Settings behavior --
	private void updateAdvancedSettingsVisibility()
	{
		advancedSettingsVBox.setVisible(settingsHandler.getSettings().areAdvancedOptionsShowing().get());
	}

	private void updateConfirmationRequestToggleBox()
	{
		confirmationRequestToggleButton.setSelected(settingsHandler.getSettings().isRequestingConfirmationBeforeExitingEnabled().get());
	}

	private void subscribeConfirmationRequestToggleButton()
	{
		confirmationRequestToggleButton.selectedProperty().addListener((observableValue, oldValue, newValue) ->
		{
			settingsHandler.getSettings().setConfirmBeforeExit(newValue);
		});

		settingsHandler.getSettings().isRequestingConfirmationBeforeExitingEnabled().addListener(observable -> {updateConfirmationRequestToggleBox();});
	}

	private void updateTutorialResetToggleButton()
	{
		tutorialResetToggleButton.setSelected(settingsHandler.getSettings().isTutorialResetted().get());
	}

	private void subscribeTutorialResetToggleButton()
	{
		tutorialResetToggleButton.selectedProperty().addListener((observableValue, oldValue, newValue) ->
		{
			settingsHandler.getSettings().setResetTutorial(newValue);
		});

		settingsHandler.getSettings().isTutorialResetted().addListener(observable -> { updateTutorialResetToggleButton();});
	}

	@FXML
	void onMouseClickedAdvancedSettingsButton(MouseEvent event) {

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
