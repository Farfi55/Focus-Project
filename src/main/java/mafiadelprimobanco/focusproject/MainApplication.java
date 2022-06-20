package mafiadelprimobanco.focusproject;

import javafx.application.Application;
import javafx.stage.Stage;
import mafiadelprimobanco.focusproject.handler.*;
import mafiadelprimobanco.focusproject.utils.ResourcesLoader;

import java.awt.*;
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
			JsonHandler.init();

			stage.getIcons().add(ResourcesLoader.loadImage("icon/icon.png"));

			stage.setMinWidth(690);
			stage.setMinHeight(445);

			TagHandler.getInstance().loadTags();
			SettingsHandler.getInstance().getSettings().setListeners();

			Feedback.getInstance().init(stage);
			SceneHandler.getInstance().init(stage);
			Feedback.getInstance().setRoot(SceneHandler.getInstance().getRoot());
			KeyPressManager.getInstance().init(SceneHandler.getInstance().getRoot());
			// just to call the constructor
			ActivityStatsHandler.getInstance().init();

			PagesHandler.navigateTo(PagesHandler.home);
		} catch (IOException e)
		{
			e.printStackTrace();
			Feedback.getInstance().showError(Localization.get("error.loadingApplication.header"),Localization.get("error.loadingApplication.message"));
		}
	}
}