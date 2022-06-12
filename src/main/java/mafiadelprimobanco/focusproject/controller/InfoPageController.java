package mafiadelprimobanco.focusproject.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import mafiadelprimobanco.focusproject.utils.LocalizationUtils;

import java.net.URL;
import java.util.ResourceBundle;

public class InfoPageController implements Controller
{
	@FXML private Label pageTitle;

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		LocalizationUtils.bindLabelText(pageTitle, "infoPage.name");
	}

	@Override
	public void terminate()
	{
		
	}
}
