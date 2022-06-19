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
import mafiadelprimobanco.focusproject.handler.*;
import mafiadelprimobanco.focusproject.utils.Language;
import mafiadelprimobanco.focusproject.utils.LocalizationUtils;
import mafiadelprimobanco.focusproject.utils.Theme;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.*;

public class SettingsPageController implements Controller
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
	private Label chronometerConfirmationLabel;

	@FXML
	private MFXToggleButton chronometerConfirmationToggleButton;

	@FXML
	private Label confirmBeforeExitLabel;

	@FXML
	private MFXToggleButton confirmationRequestToggleButton;

	@FXML
	private Label generalCategoryLabel;

	@FXML
	private MFXComboBox<Language> languageComboBox;

	@FXML
	private Label languageLabel;

	@FXML
	private Label minimumTimerActivityLabel;

	@FXML
	private MFXTextField minimumTimerTextField;

	@FXML
	private Label musicLabel;

	@FXML
	private MFXSlider musicSlider;

	@FXML
	private Label navigationBlockLabel;

	@FXML
	private MFXToggleButton navigationToggleButton;
	@FXML
	private Label soundLabel;

	@FXML
	private MFXSlider soundSlider;

	@FXML
	private Label successfulActivityMinimumChronometeLabel;

	@FXML
	private MFXTextField successfulActivityMinimumChronometerTextField;

	@FXML
	private MFXComboBox<Theme> themeComboBox;

	@FXML
	private Label themeLabel;

	@FXML
	private Label timerCategoryLabel;

	@FXML
	private Label timerConfirmationLabel;

	@FXML
	private MFXToggleButton timerConfirmationToggleButton;

	private SettingsHandler settingsHandler;

	private Feedback feedback;

	private Pattern inputValidation;

	private AudioHandler audioHandler;


	@Override
	public void initialize(URL url, ResourceBundle resourceBundle)
	{
		settingsHandler = SettingsHandler.getInstance();


		// Set settings initial state from SettingsHandler instance
		updateStringsBasedOnCurrentLanguage();
		updateCurrentLanguage();

		updateAvailableLanguages();

		updateAvailableThemes();

		updateNavigationToggleButton();

		updateSoundVolume();
		updateMusicVolume();

		updateMinimumTimerTextField();
		updateTimerConfirmationToggleButton();

		updateChronometerTextField();
		updateChronometerConfirmationToggleButton();

		updateThemeComboBox();

		updateAdvancedSettingsVisibility();
		updateConfirmationRequestToggleBox();
		// --------------------------------


		feedback = Feedback.getInstance();


		inputValidation = Pattern.compile("[0-9]\\d{0,2}");
		setGlobalMeasureUnit("min", 0.5);

		subscribeMinimumTimerTextField();
		subscribeTimerConfirmationToggleButton();

		subscribeChronometerTextField();
		subscribeChronometerConfirmationToggleButton();

		subscribeMusicSlider();
		subscribeSoundSlider();

		subscribeLanguageComboBox();
		subscribeThemesComboBox();

		subscribeNavigationToggleButton();

		subscribeConfirmationRequestToggleButton();
	}


	// Language combo box behaviour
	void updateStringsBasedOnCurrentLanguage()
	{

		LocalizationUtils.bindLabelText(generalCategoryLabel, "settings.general.categoryName");

		LocalizationUtils.bindLabelText(languageLabel, "settings.language");

		LocalizationUtils.bindLabelText(navigationBlockLabel, "settings.navigationBlock");

		LocalizationUtils.bindLabelText(audioCategoryLabel, "settings.audio.categoryName");
		LocalizationUtils.bindLabelText(soundLabel, "settings.audio.sound");
		LocalizationUtils.bindLabelText(musicLabel, "settings.audio.music");

		LocalizationUtils.bindLabelText(timerCategoryLabel, "settings.timer.categoryName");
		LocalizationUtils.bindLabelText(minimumTimerActivityLabel, "settings.timer.minimumActivityTime");
		LocalizationUtils.bindLabelText(timerConfirmationLabel, "settings.timer.confirmation");

		LocalizationUtils.bindLabelText(chronometerCategoryLabel, "settings.chronometer.categoryName");
		LocalizationUtils.bindLabelText(successfulActivityMinimumChronometeLabel, "settings.chronometer.stopActivityAfter");
		LocalizationUtils.bindLabelText(chronometerConfirmationLabel, "settings.chronometer.confirmation");

		LocalizationUtils.bindLabelText(themeLabel, "settings.theme");

		LocalizationUtils.bindLabelText(advancedOptionCategoryLabel,"settings.advancedOptions.categoryName");
		LocalizationUtils.bindLabelText(advancedSettingsButton, "settings.advancedOptions.show");

		LocalizationUtils.bindLabelText(confirmBeforeExitLabel, "settings.advancedOptions.askConfirmationBeforeQuitting");

		themeComboBox.setText(settingsHandler.getSettings().theme.getValue().toString());
	}

	private void updateCurrentLanguage()
	{
		languageComboBox.setText(settingsHandler.getSettings().language.getValue().toString());
	}

	private void subscribeLanguageComboBox()
	{
		languageComboBox.selectedItemProperty().addListener((observer, latestLanguage, newLanguage) ->
		{
			if (newLanguage != null && !newLanguage.toString().equals(
					settingsHandler.getSettings().language.toString()))
			{
				settingsHandler.getSettings().language.set(newLanguage);
				updateStringsBasedOnCurrentLanguage();
			}
		});

		settingsHandler.getSettings().language.addListener(observable ->
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
				settingsHandler.getSettings().theme.set(newValue);
				updateThemeComboBox();
				updateSelectedTheme();
			}
		});

		settingsHandler.getSettings().theme.addListener(observable -> updateThemeComboBox());
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
		themeComboBox.selectItem(settingsHandler.getSettings().theme.getValue());
		themeComboBox.setText(Localization.get(settingsHandler.getSettings().theme.getValue().key));
	}

	private void updateThemeComboBox()
	{
		themeComboBox.setText(settingsHandler.getSettings().theme.getValue().toString());
	}
	// --------------------------


	// Navigation Behaviour
	private void updateNavigationToggleButton()
	{
		navigationToggleButton.setSelected(settingsHandler.getSettings().navigationDisabledDuringActivity.getValue());
	}

	private void subscribeNavigationToggleButton()
	{
		navigationToggleButton.selectedProperty().addListener((observableValue, oldValue, newValue) ->
		{
			settingsHandler.getSettings().navigationDisabledDuringActivity.set(newValue);
			AudioHandler.getInstance().playToggleButtonAudioClip();
		});

		settingsHandler.getSettings().navigationDisabledDuringActivity.addListener(observable -> updateNavigationToggleButton());
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
				AudioHandler.getInstance().getMusicPlayer().setVolume(settingsHandler.getSettings().musicVolume.getValue() / 100.0);
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
	private void updateMinimumTimerTextField()
	{
		minimumTimerTextField.setText(settingsHandler.getSettings().minimumTimerDuration.getValue().toString());
	}

	private void subscribeMinimumTimerTextField()
	{
		minimumTimerTextField.delegateFocusedProperty().addListener((observable, oldStatus, newStatus) ->
		{
			if (!newStatus) validateMinimumTimerValue();
		});

		minimumTimerTextField.setOnAction(event -> validateMinimumTimerValue());

		settingsHandler.getSettings().minimumTimerDuration.addListener(observable ->
		{
			updateMinimumTimerTextField();
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
					settingsHandler.getSettings().minimumTimerDuration.getValue().toString());
		}
		else
		{
			minimumTimerTextField.setText(input);
			settingsHandler.getSettings().minimumTimerDuration.setValue(Integer.valueOf(input));
		}
	}

	private void updateTimerConfirmationToggleButton()
	{
		timerConfirmationToggleButton.setSelected(settingsHandler.getSettings().confirmInterruptTimerActivity.getValue());
	}

	private void subscribeTimerConfirmationToggleButton()
	{
		timerConfirmationToggleButton.selectedProperty().addListener((observableValue, oldValue, newValue) ->
		{
			settingsHandler.getSettings().confirmInterruptTimerActivity.setValue(newValue);
			AudioHandler.getInstance().playToggleButtonAudioClip();
		});
	}
	// --------------------------


	// Chronometer behaviour
	private void updateChronometerTextField()
	{
		successfulActivityMinimumChronometerTextField.setText(settingsHandler.getSettings().minimumSuccessfulChronometerDuration.getValue().toString());
	}

	private void subscribeChronometerTextField()
	{
		successfulActivityMinimumChronometerTextField.delegateFocusedProperty().addListener((observable, oldStatus, newStatus) ->
		{
			if (!newStatus) validateChronometerTextFieldValue();
		});
		successfulActivityMinimumChronometerTextField.setOnAction(event -> validateChronometerTextFieldValue());

		settingsHandler.getSettings().minimumSuccessfulChronometerDuration.addListener(observable ->
		{
			updateChronometerTextField();
		});
	}

	private void validateChronometerTextFieldValue()
	{
		String input = removeLeadingZeros(successfulActivityMinimumChronometerTextField.getText());

		if (!validateInput(input))
		{
			feedback.showError(Localization.get("error.settings.invalidTime.header"),
					Localization.get("error.settings.invalidTime.message"));
			updateChronometerTextField();
		}
		else
		{
			settingsHandler.getSettings().minimumSuccessfulChronometerDuration.setValue(Integer.valueOf(input));
			updateChronometerTextField();
		}
	}

	private void updateChronometerConfirmationToggleButton()
	{
		chronometerConfirmationToggleButton.setSelected(settingsHandler.getSettings().confirmInterruptChronometerActivity.getValue());
	}
	private void subscribeChronometerConfirmationToggleButton()
	{
		chronometerConfirmationToggleButton.selectedProperty().addListener((observableValue, oldValue, newValue) ->
		{
			settingsHandler.getSettings().confirmInterruptChronometerActivity.setValue(newValue);
			AudioHandler.getInstance().playToggleButtonAudioClip();
		});
	}

	// --------------------------


	// -- Advanced Settings behavior --
	private void updateAdvancedSettingsVisibility()
	{
		advancedSettingsVBox.setVisible(settingsHandler.getSettings().areAdvancedOptionsShown.get());
	}

	private void updateConfirmationRequestToggleBox()
	{
		confirmationRequestToggleButton.setSelected(
				settingsHandler.getSettings().confirmQuitApplication.get());
	}

	private void subscribeConfirmationRequestToggleButton()
	{
		confirmationRequestToggleButton.selectedProperty().addListener((observableValue, oldValue, newValue) ->
		{
			settingsHandler.getSettings().confirmQuitApplication.setValue(newValue);
			AudioHandler.getInstance().playToggleButtonAudioClip();
		});

		settingsHandler.getSettings().confirmQuitApplication.addListener(observable ->
		{
			updateConfirmationRequestToggleBox();
		});
	}

	@FXML
	void onMouseClickedAdvancedSettingsButton(MouseEvent event)
	{
		if (!advancedSettingsVBox.isVisible())
		{
			advancedSettingsVBox.setVisible(true);
			LocalizationUtils.bindButtonText(advancedSettingsButton, "settings.advancedOptions.hide");
			advancedSettingsVBox.setManaged(true);
		}
		else
		{
			advancedSettingsVBox.setVisible(false);
			LocalizationUtils.bindButtonText(advancedSettingsButton, "settings.advancedOptions.show");
			advancedSettingsVBox.setManaged(false);
		}
	}
	// --------------------------


	// -- Text boxes behaviour --
	private void setGlobalMeasureUnit(String measureUnit, Double gap)
	{
		minimumTimerTextField.setMeasureUnit(measureUnit);
		minimumTimerTextField.setMeasureUnitGap(gap);

		successfulActivityMinimumChronometerTextField.setMeasureUnit(measureUnit);
		successfulActivityMinimumChronometerTextField.setMeasureUnitGap(gap);
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
