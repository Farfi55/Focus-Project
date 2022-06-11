package mafiadelprimobanco.focusproject;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;

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
			Localization.setLocale(Locale.ITALIAN);
			JsonHandler.init();

			Feedback.getInstance().init(stage);
			SceneHandler.getInstance().init(stage);
			Feedback.getInstance().setRoot(SceneHandler.getInstance().getRoot());
			KeyPressManager.getInstance().init(SceneHandler.getInstance().getRoot());
			// just to call the constructor
			ActivityStatsHandler.getInstance().init();

		} catch (IOException e)
		{
			e.printStackTrace();
			//todo localization
			Feedback.getInstance().showError("Errore di caricamento",
					"Non è stato possibile caricare l'applicazione");
		}
	}
}