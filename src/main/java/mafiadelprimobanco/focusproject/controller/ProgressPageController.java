package mafiadelprimobanco.focusproject.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import mafiadelprimobanco.focusproject.Localization;
import mafiadelprimobanco.focusproject.controller.Controller;

import java.net.URL;
import java.util.ResourceBundle;

public class ProgressPageController implements Controller
{
	@FXML private Label pageTitle;

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		Localization.setLabel(pageTitle, "progressPage.name");
	}


	@Override
	public void terminate()
	{

	}
}
