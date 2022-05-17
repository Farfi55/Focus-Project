package mafiadelprimobanco.focusproject.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import mafiadelprimobanco.focusproject.SceneHandler;
import mafiadelprimobanco.focusproject.model.utils.FXMLReferences;

public class BaseController
{
	@FXML
	private BorderPane borderPane;

	@FXML
	private StackPane contentRoot;

	@FXML
	void initialize()
	{
		SceneHandler.getInstance().setContentRoot(contentRoot);
	}


	@FXML
	void onNavHomeClick(ActionEvent event)
	{
		navTo(FXMLReferences.HOME, "Home");
		if(event.getSource() instanceof ToggleButton toggleButton)
			toggleButton.setSelected(true);
	}

	@FXML
	void onNavProgressClick(ActionEvent event)
	{
		navTo(FXMLReferences.PROGRESS, "Progressi");
		if(event.getSource() instanceof ToggleButton toggleButton)
			toggleButton.setSelected(true);
	}

	@FXML
	void onNavStatisticsClick(ActionEvent event)
	{
		navTo(FXMLReferences.STATISTICS, "Statistiche");
			if(event.getSource() instanceof ToggleButton toggleButton)
			toggleButton.setSelected(true);
	}

	@FXML
	void onNavTagClick(ActionEvent event)
	{
		navTo(FXMLReferences.TAGS, "Tag");
			if(event.getSource() instanceof ToggleButton toggleButton)
			toggleButton.setSelected(true);
	}

	@FXML
	void onNavInfoClick(ActionEvent event)
	{
		// todo: implement
			if(event.getSource() instanceof ToggleButton toggleButton)
			toggleButton.setSelected(true);
	}

	@FXML
	void onNavUserClick(ActionEvent event)
	{
		// todo: implement
			if(event.getSource() instanceof ToggleButton toggleButton)
			toggleButton.setSelected(true);
	}

	@FXML
	void onNavSettingsClick(ActionEvent event)
	{
		// todo: implement
		if(event.getSource() instanceof ToggleButton toggleButton)
			toggleButton.setSelected(true);
	}

	private void navTo(String pagePathRef, String pageName)
	{
		/* disabled for now
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
		*/
	}



}
