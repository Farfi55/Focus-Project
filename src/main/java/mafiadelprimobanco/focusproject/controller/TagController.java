package mafiadelprimobanco.focusproject.controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;

public class TagController
{

	@FXML
	private MFXButton colorButton;

	@FXML
	private MFXTextField textField;

	@FXML
	void onColorButtonClicked(ActionEvent event) {

	}

	@FXML
	void onRemoveButtonClicked(ActionEvent event) {

	}

	public void setColorButton(Color color)
	{
		colorButton.setStyle("-fx-background-color: " + color.toString());
		System.out.println(color.toString());
	}

	public void setTagText(String name)
	{
		textField.setText(name);
	}

}
