package mafiadelprimobanco.focusproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class BaseController
{
	@FXML
	private BorderPane baseBorderPane;


	@FXML
	void onNavHomeClick(ActionEvent ignoredEvent)
	{

	}

	@FXML
	void onNavProgressClick(ActionEvent ignoredEvent)
	{

	}

	@FXML
	void onNavStatisticsClick(ActionEvent ignoredEvent)
	{

	}

	@FXML
	void onNavTagClick(ActionEvent ignoredEvent)
	{

	}

	@FXML
	void onNavInfoClick(ActionEvent ignoredEvent)
	{

	}

	@FXML
	void onNavUserClick(ActionEvent ignoredEvent)
	{

	}

	@FXML
	void onNavSettingsClick(ActionEvent ignoredEvent)
	{

	}

	@FXML
	void initialize()
	{
		SceneHandler.getInstance().setBaseBorderPane(baseBorderPane);
	}

}
