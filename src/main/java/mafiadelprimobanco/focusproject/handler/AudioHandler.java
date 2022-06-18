package mafiadelprimobanco.focusproject.handler;

import javafx.scene.media.*;
import mafiadelprimobanco.focusproject.utils.ResourcesLoader;



public class AudioHandler
{
	private static final AudioHandler instance = new AudioHandler();

	private MediaPlayer mediaPlayer;
	private Media toggleButtonAudioClip;
	private Media buttonAudioClip;
	private Media notificationAudioClip;
	private Media activitySuccessAudioClip;
	private final SettingsHandler settingsHandler;

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
		toggleButtonAudioClip = new Media(ResourcesLoader.load("audio/click1.wav"));
		buttonAudioClip = new Media(ResourcesLoader.load("audio/click5.wav"));
		notificationAudioClip = new Media(ResourcesLoader.load("audio/rollover3.wav"));
		activitySuccessAudioClip = new Media(ResourcesLoader.load("audio/activitySuccess.wav"));
	}

	public void playToggleButtonAudioClip()
	{
		mediaPlayer = new MediaPlayer(toggleButtonAudioClip);
		useGlobalVolume();
		mediaPlayer.seek(toggleButtonAudioClip.getDuration());
		mediaPlayer.play();
	}

	public void playButtonAudioClip()
	{
		mediaPlayer = new MediaPlayer(buttonAudioClip);
		useGlobalVolume();
		mediaPlayer.seek(buttonAudioClip.getDuration());
		mediaPlayer.play();
	}

	public void playNotificationAudioClip()
	{
		mediaPlayer = new MediaPlayer(notificationAudioClip);
		useGlobalVolume();
		mediaPlayer.seek(notificationAudioClip.getDuration());
		mediaPlayer.play();
	}

	public void playActivitySuccessAudioClip()
	{
		mediaPlayer = new MediaPlayer(activitySuccessAudioClip);
		useGlobalVolume();
		mediaPlayer.seek(activitySuccessAudioClip.getDuration());
		mediaPlayer.play();
	}

	void useGlobalVolume()
	{
		mediaPlayer.setVolume(settingsHandler.getSettings().soundVolume.get() / 100.0);
	}



}
