package mafiadelprimobanco.focusproject.controller;

import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class ProgressPageController implements Controller
{

	@FXML private MFXComboBox<String> intervalComboBox;

	@FXML private GridPane toUnlockTreeDetailsGrid;

	@FXML private GridPane treeGrid;

	@FXML private HBox treeSelectionHBox;

	@FXML private GridPane unlockedTreeDetailsGrid;

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{

	}

	@Override
	public void terminate()
	{

	}

	@FXML
	void onTreeSelectNextAction(ActionEvent event)
	{

	}

	@FXML
	void onTreeSelectPreviousAction(ActionEvent event)
	{

	}
}
