package mafiadelprimobanco.focusproject.handler;

import javafx.scene.media.*;
import mafiadelprimobanco.focusproject.utils.ResourcesLoader;



public class AudioHandler
{
	private static final AudioHandler instance = new AudioHandler();

	private MediaPlayer mediaPlayer;
	private MediaPlayer musicPlayer;
	private Media toggleButtonAudioClip;
	private Media buttonAudioClip;
	private Media errorAudioClip;
	private Media activitySuccessAudioClip;
	private Media failedActivityAudioClip;
	private Media notificationAudioClip;
	private Media backgroundMusic;
	private final SettingsHandler settingsHandler;

	private Boolean playing = true;

	public static AudioHandler getInstance()
	{
		return instance;
	}

	AudioHandler()
	{
		this.settingsHandler = SettingsHandler.getInstance();
		loadAudioClips();
	}

	private void loadAudioClips()
	{
		toggleButtonAudioClip = new Media(ResourcesLoader.load("audio/Minimalist4.wav"));
		buttonAudioClip = new Media(ResourcesLoader.load("audio/Minimalist6.wav"));
		errorAudioClip = new Media(ResourcesLoader.load("audio/Retro12.wav"));
		activitySuccessAudioClip = new Media(ResourcesLoader.load("audio/successful-activity.wav"));
		failedActivityAudioClip = new Media(ResourcesLoader.load("audio/failed-activity.wav"));
		notificationAudioClip = new Media(ResourcesLoader.load("audio/Retro9.wav"));

		backgroundMusic = new Media(ResourcesLoader.load("audio/You-ve-been-in-the-dark-for-way-too-long.wav"));
		musicPlayer = new MediaPlayer(backgroundMusic);
		musicPlayer.setAutoPlay(true);
	}

	public void playToggleButtonAudioClip()
	{
		mediaPlayer = new MediaPlayer(toggleButtonAudioClip);
		useGlobalSoundVolume();
		mediaPlayer.seek(toggleButtonAudioClip.getDuration());
		mediaPlayer.play();
	}

	public void playPopTagAudioClip()
	{
		playToggleButtonAudioClip();
	}

	public void playButtonAudioClip()
	{
		mediaPlayer = new MediaPlayer(buttonAudioClip);
		useGlobalSoundVolume();
		mediaPlayer.seek(buttonAudioClip.getDuration());
		mediaPlayer.play();
	}

	public void playErrorAudioClip()
	{
		mediaPlayer = new MediaPlayer(errorAudioClip);
		useGlobalSoundVolume();
		mediaPlayer.seek(errorAudioClip.getDuration());
		mediaPlayer.play();
	}
	public void playSuccessfulActivityAudioClip()
	{
		mediaPlayer = new MediaPlayer(activitySuccessAudioClip);
		useGlobalSoundVolume();
		mediaPlayer.seek(activitySuccessAudioClip.getDuration());
		mediaPlayer.play();
	}
	public void playFailedActivityAudioClip()
	{
		mediaPlayer = new MediaPlayer(failedActivityAudioClip);
		useGlobalSoundVolume();
		mediaPlayer.seek(failedActivityAudioClip.getDuration());
		mediaPlayer.play();
	}


	public void playNotificationAudioClip()
	{
		mediaPlayer = new MediaPlayer(notificationAudioClip);
		useGlobalSoundVolume();
		mediaPlayer.seek(notificationAudioClip.getDuration());
		mediaPlayer.play();
	}

	public void playBackgroundMusic()
	{
		musicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
		useGlobalMusicVolume();
		musicPlayer.play();
	}

	void useGlobalSoundVolume()
	{
		mediaPlayer.setVolume(settingsHandler.getSettings().soundVolume.get() / 100.0);
	}

	void useGlobalMusicVolume()
	{
		musicPlayer.setVolume(settingsHandler.getSettings().musicVolume.get() / 100.0);
	}

	public MediaPlayer getMusicPlayer() {return musicPlayer;}
}
