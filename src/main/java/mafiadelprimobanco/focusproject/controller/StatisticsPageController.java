package mafiadelprimobanco.focusproject.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import mafiadelprimobanco.focusproject.handler.Localization;

import java.net.URL;
import java.util.ResourceBundle;

public class StatisticsPageController implements Controller
{
	@FXML private Label pageTitle;

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		Localization.setLabel(pageTitle, "statisticsPage.name");
	}

	@Override
	public void terminate()
	{
		
	}
}
