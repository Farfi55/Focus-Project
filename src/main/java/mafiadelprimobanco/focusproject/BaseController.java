package mafiadelprimobanco.focusproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class BaseController
{
	@FXML
	private AnchorPane baseBorderPane;


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
