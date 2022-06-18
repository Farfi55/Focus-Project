package mafiadelprimobanco.focusproject.handler;

import javafx.scene.media.MediaPlayer;



public class AudioHandler
{
	private static final AudioHandler instance = new AudioHandler();

	private SettingsHandler settingsHandler;

	public static AudioHandler getInstance()
	{
		return instance;
	}

	AudioHandler()
	{
		this.settingsHandler = SettingsHandler.getInstance();
	}
}
