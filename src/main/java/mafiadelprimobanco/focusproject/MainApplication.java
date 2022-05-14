package mafiadelprimobanco.focusproject;

import javafx.application.Application;
import javafx.stage.Stage;
import mafiadelprimobanco.focusproject.controller.handlers.SceneHandler;

import java.io.IOException;

public class MainApplication extends Application
{
	public static void main(String[] args)
	{
		launch();
	}

	@Override
	public void start(Stage stage)
	{
		try
		{
			SceneHandler.getInstance().init(stage);
		} catch (IOException e)
		{
			e.printStackTrace();
			//todo localization
			SceneHandler.getInstance().showErrorMessage("Errore di caricamento",
					"Non è stato possibile caricare l'applicazione");
		}
	}
}