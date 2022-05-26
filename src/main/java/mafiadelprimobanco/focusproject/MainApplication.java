package mafiadelprimobanco.focusproject;

import javafx.application.Application;
import javafx.stage.Stage;

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
			Feedback.getInstance().init(stage);
			SceneHandler.getInstance().init(stage);
			Feedback.getInstance().setRoot(SceneHandler.getInstance().getRoot());
			KeyPressManager.getInstance().init(SceneHandler.getInstance().getRoot());

			if (!AutentificationHandler.getInstance().doLoginFromDatabase())
			{
				System.out.println("Utente defaut non esistente");
			}else
			{
				System.out.println("Benvenuto " + AutentificationHandler.getInstance().getUser());
			}

		} catch (IOException e)
		{
			e.printStackTrace();
			//todo localization
			Feedback.getInstance().showError("Errore di caricamento",
					"Non è stato possibile caricare l'applicazione");
		}
	}
}