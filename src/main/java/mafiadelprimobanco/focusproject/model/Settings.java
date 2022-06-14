package mafiadelprimobanco.focusproject.model;

import javafx.beans.property.*;
import mafiadelprimobanco.focusproject.utils.Language;

public class Settings
{
	private SimpleObjectProperty<Language> currentLanguage;
	private SimpleStringProperty currentTheme;
	private SimpleBooleanProperty blockNavigation;
	private SimpleBooleanProperty hideTutorial;

	private SimpleDoubleProperty musicVolume;
	private SimpleDoubleProperty soundVolume;

	private SimpleIntegerProperty pomodoroFocusTime;
	private SimpleIntegerProperty pomodoroPauseTime;

	private SimpleIntegerProperty minimumTimerTime;
	private SimpleIntegerProperty stopChronometerAfter;

	private SimpleBooleanProperty showAdvancedOptions;
	private SimpleBooleanProperty resetTutorial;
	private SimpleBooleanProperty confirmBeforeExit;



	public Settings()
	{
		this.currentLanguage = new SimpleObjectProperty<>();
		this.currentTheme = new SimpleStringProperty();
		this.blockNavigation = new SimpleBooleanProperty();
		this.hideTutorial = new SimpleBooleanProperty();
		this.musicVolume = new SimpleDoubleProperty();
		this.soundVolume = new SimpleDoubleProperty();
		this.pomodoroFocusTime = new SimpleIntegerProperty();
		this.pomodoroPauseTime = new SimpleIntegerProperty();
		this.minimumTimerTime = new SimpleIntegerProperty();
		this.stopChronometerAfter = new SimpleIntegerProperty();
		this.showAdvancedOptions = new SimpleBooleanProperty();
		this.resetTutorial = new SimpleBooleanProperty();
		this.confirmBeforeExit = new SimpleBooleanProperty();
	}


	public SimpleObjectProperty<Language> getCurrentLanguage() { return currentLanguage; }
	public void setCurrentLanguage(Language currentLanguage) { this.currentLanguage.set(currentLanguage); }

	public SimpleStringProperty getCurrentTheme() { return currentTheme; }
	public void setCurrentTheme(String currentTheme) { this.currentTheme.set(currentTheme); }

	public SimpleBooleanProperty isNavigationBlocked() { return blockNavigation; }
	public void setNavigationBlock(Boolean blockNavigation) { this.blockNavigation.set(blockNavigation); }

	public SimpleBooleanProperty getHideTutorial() { return hideTutorial; }
	public void setHideTutorial(Boolean hideTutorial) { this.hideTutorial.set(hideTutorial); }

	public SimpleDoubleProperty getMusicVolume() { return musicVolume; }
	public void setMusicVolume(Double musicVolume) { this.musicVolume.set(musicVolume); }

	public SimpleDoubleProperty getSoundVolume() { return soundVolume; }
	public void setSoundVolume(Double soundVolume) { this.soundVolume.set(soundVolume); }

	public SimpleIntegerProperty getPomodoroFocusTime() { return pomodoroFocusTime; }
	public void setPomodoroFocusTime(Integer pomodoroFocusTime) { this.pomodoroFocusTime.set(pomodoroFocusTime); }

	public SimpleIntegerProperty getPomodoroPauseTime() { return pomodoroPauseTime; }
	public void setPomodoroPauseTime(Integer pomodoroPauseTime) { this.pomodoroPauseTime.set(pomodoroPauseTime); }

	public SimpleIntegerProperty getMinimumTimerTime() { return minimumTimerTime; }
	public void setMinimumTimerTime(Integer minimumTimerTime) { this.minimumTimerTime.set(minimumTimerTime); }

	public SimpleIntegerProperty getStopChronometerAfter() { return stopChronometerAfter; }
	public void setStopChronometerAfter(Integer stopChronometerAfter) { this.stopChronometerAfter.set(stopChronometerAfter); }

	public SimpleBooleanProperty areAdvancedOptionsShowing() { return showAdvancedOptions; }
	public void setAdvancedOptionsShowing(Boolean showAdvancedOptions) { this.showAdvancedOptions.set(showAdvancedOptions); }

	public SimpleBooleanProperty isTutorialResetted() { return resetTutorial; }
	public void setResetTutorial(Boolean resetTutorial) { this.resetTutorial.set(resetTutorial); }

	public SimpleBooleanProperty isRequestingConfirmationBeforeExiting() { return confirmBeforeExit; }
	public void setConfirmBeforeExit(Boolean confirmBeforeExit) { this.confirmBeforeExit.set(confirmBeforeExit); }
}
