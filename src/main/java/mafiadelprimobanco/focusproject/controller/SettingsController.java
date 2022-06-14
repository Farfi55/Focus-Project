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
	private MFXComboBox<String> languageComboBox;

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
	private MFXComboBox<String> themeComboBox;

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


		setStringsBasedOnCurrentLanguage();
		setCurrentLanguage();

		setLanguagesAvailable();

		setNavigation();

		setAudioVolume(settingsHandler.getSettings().getMusicVolume().getValue());
		setMusicVolume(settingsHandler.getSettings().getMusicVolume().getValue());

		setTimerSettings(settingsHandler.getSettings().getMinimumTimerTime().getValue());


		setPomodoroFocusTime(settingsHandler.getSettings().getPomodoroFocusTime().getValue());
		setPomodoroPauseTime(settingsHandler.getSettings().getPomodoroPauseTime().getValue());

		setChronometerSettings(settingsHandler.getSettings().getStopChronometerAfter().getValue());

		setThemeSettings(settingsHandler.getSettings().getCurrentTheme().get());

		setTutorialReset();
		setTutorialShowing();

		setAdvancedSettingsVisible();
		setConfirmationRequest();

		feedback = Feedback.getInstance();
		inputValidation = Pattern.compile("\\d{1,3}");

		setGlobalMeasureUnit("min", 0.5);


		outOfTextFieldFocus(pomodoroFocusTimeTextField, settingsHandler.getSettings().getPomodoroFocusTime().getValue());
		outOfTextFieldFocus(minimumTimerTextField, settingsHandler.getSettings().getMinimumTimerTime().getValue());
		outOfTextFieldFocus(pomodoroPauseTimeTextField, settingsHandler.getSettings().getPomodoroPauseTime().getValue());
		outOfTextFieldFocus(stopChronometerTextField, settingsHandler.getSettings().getStopChronometerAfter().getValue());

		onSliderScrollEnd(musicSlider, settingsHandler.getSettings().getMusicVolume().getValue());
		onSliderScrollEnd(soundSlider, settingsHandler.getSettings().getSoundVolume().getValue());

	}

	private void outOfTextFieldFocus(MFXTextField textField, Integer oldValue)
	{
			textField.delegateFocusedProperty().addListener((observable, oldStatus, newStatus) -> {

			if (!newStatus)
			{
				String input = removeLeadingZeros(textField.getText());

				if (!validateInput(input))
				{
					feedback.showError(Localization.get("error.settings.invalidTime.header"), Localization.get("error.settings.invalidTime.message"));
					textField.setText(oldValue.toString());
				}
				else
				{
					textField.setText(input);
				}
			}
		});
	}

	private void onSliderScrollEnd(MFXSlider slider, Double oldValue)
	{
		slider.pressedProperty().addListener((observer, whenScrollEnd, whenScrollStarts) ->
		{
			if (!whenScrollStarts && (slider.getValue() != oldValue))
			{
				setMusicVolume(slider.getValue());
			}
		});
	}

	Boolean validateInput(String input)
	{
		Matcher matcher = inputValidation.matcher(input);

		if (matcher.matches()) { return true; }

		return false;
	}

	String removeLeadingZeros(String input)
	{
		return input.replaceFirst("^0+(?!$)", "");
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

	void setStringsBasedOnCurrentLanguage()
	{
		generalCategoryLabel.setText(Localization.get("settings.general.categoryName"));

		languageLabel.setText(Localization.get("settings.language"));

		navigationBlockLabel.setText(Localization.get("settings.navigationBlock"));

		audioCategoryLabel.setText(Localization.get("settings.audio.categoryName"));
		soundLabel.setText(Localization.get("settings.audio.sound"));
		musicLabel.setText(Localization.get("settings.audio.music"));

		timerCategoryLabel.setText(Localization.get("settings.timer.categoryName"));
		minimumTimerLabel.setText(Localization.get("settings.timer.minimumActivityTime"));
		minimumTimerLabel.setText(Localization.get("settings.chronometer.stopActivityAfter"));

		pomodoroCategoryLabel.setText(Localization.get("settings.pomodoro.categoryName"));
		pausePeriodLabel.setText(Localization.get("settings.pomodoro.focusPeriod"));
		focusTimeLabel.setText(Localization.get("settings.pomodoro.pausePeriod"));

		chronometerCategoryLabel.setText(Localization.get("setting.chronometer.categoryName"));
		stopChronometerLabel.setText(Localization.get("settings.chronometer.stopActivityAfter"));

		themeLabel.setText(Localization.get("settings.theme"));
		themeComboBox.getItems().add(Localization.get("settings.theme.light"));
		themeComboBox.getItems().add(Localization.get("settings.theme.dark"));

		tutorialResetLabel.setText(Localization.get("settings.advancedOptions.tutorialReset"));

		advancedOptionCategoryLabel.setText(Localization.get("settings.advancedOptions.categoryName"));
		advancedSettingsButton.setText(Localization.get("settings.advancedOptions.show"));

		confirmBeforeExitLabel.setText(Localization.get("settings.advancedOptions.requestConfirmationBeforExiting"));

		hideTutorialLabel.setText(Localization.get("settings.hideTutorial"));
	}

	private void setCurrentLanguage()
	{
		languageComboBox.setText(settingsHandler.getSettings().getCurrentLanguage().get());
	}

	private void onLanguageChanged()
	{
		languageComboBox.selectedItemProperty().addListener((observer, latestLanguage, newLanguage) ->
		{
			System.out.println(newLanguage);

			settingsHandler.getSettings().setCurrentLanguage(newLanguage);
			//set language
		});
	}

	private void setLanguagesAvailable()
	{
		languageComboBox.getItems().add(Localization.get("settings.language.italian"));
		languageComboBox.getItems().add(Localization.get("settings.language.english"));
	}

	private void setNavigation()
	{
		navigationToggleButton.setSelected(settingsHandler.getSettings().isNavigationBlocked().get());
	}

	private void setMusicVolume(Double volume)
	{
		settingsHandler.getSettings().setMusicVolume(volume);
		musicSlider.setValue(settingsHandler.getSettings().getMusicVolume().get());
	}

	private void setAudioVolume(Double volume)
	{
		settingsHandler.getSettings().setSoundVolume(volume);
		soundSlider.setValue(settingsHandler.getSettings().getSoundVolume().get());
	}

	private void setTimerSettings(Integer input)
	{
		settingsHandler.getSettings().setMinimumTimerTime(input);
		minimumTimerTextField.setText(input.toString());
	}

	private void setPomodoroFocusTime(Integer input)
	{
		settingsHandler.getSettings().setPomodoroFocusTime(input);
		pomodoroFocusTimeTextField.setText(input.toString());
	}

	private void setPomodoroPauseTime(Integer input)
	{
		settingsHandler.getSettings().setPomodoroPauseTime(input);
		pomodoroPauseTimeTextField.setText(input.toString());
	}

	private void setChronometerSettings(Integer input)
	{
		settingsHandler.getSettings().setStopChronometerAfter(input);
		stopChronometerTextField.setText(input.toString());
	}

	private void setThemeSettings(String theme)
	{
		themeComboBox.setText(theme);
	}

	private void setTutorialReset()
	{
		tutorialResetToggleButton.setSelected(settingsHandler.getSettings().isTutorialResetted().get());
	}

	private void setTutorialShowing()
	{
		tutorialToggleButton.setSelected(settingsHandler.getSettings().getHideTutorial().get());
	}

	private void setConfirmationRequest()
	{
		confirmationRequestToggleButton.setSelected(settingsHandler.getSettings().isRequestingConfirmationBeforeExiting().get());
	}

	private void setAdvancedSettingsVisible()
	{
		advancedSettingsVBox.setVisible(settingsHandler.getSettings().areAdvancedOptionsShowing().get());
	}

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

	@Override
	public void terminate()
	{

	}
}
