package mafiadelprimobanco.focusproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class BaseController
{
	@FXML
	private BorderPane baseBorderPane;

	@FXML
	void onNavHomeClick(ActionEvent ignoredEvent)
	{
		navTo(ViewReferences.HOME, "Home");
	}

	@FXML
	void onNavProgressClick(ActionEvent ignoredEvent)
	{
		navTo(ViewReferences.PROGRESS, "Progressi");
	}

	@FXML
	void onNavStatisticsClick(ActionEvent ignoredEvent)
	{
		navTo(ViewReferences.STATISTICS, "Statistiche");
	}

	@FXML
	void onNavTagClick(ActionEvent ignoredEvent)
	{
		navTo(ViewReferences.TAGS, "Tag");
	}

	@FXML
	void onNavInfoClick(ActionEvent ignoredEvent)
	{
		// todo: implement
	}

	@FXML
	void onNavUserClick(ActionEvent ignoredEvent)
	{
		// todo: implement
	}

	@FXML
	void onNavSettingsClick(ActionEvent ignoredEvent)
	{
		// todo: implement
	}

	private void navTo(String pagePathRef, String pageName)
	{
		try
		{
			SceneHandler.getInstance().loadPage(pagePathRef);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			SceneHandler.getInstance().showErrorMessage("Errore Di caricamento",
					"Impossibile caricare la pagina " + pageName);
		}
	}

	@FXML
	void initialize()
	{
		SceneHandler.getInstance().setBaseBorderPane(baseBorderPane);
	}

}
