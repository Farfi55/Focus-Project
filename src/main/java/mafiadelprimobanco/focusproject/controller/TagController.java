package mafiadelprimobanco.focusproject.controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.paint.Color;
import mafiadelprimobanco.focusproject.model.Tag;

public class TagController
{

	private Tag tag;

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

	private void setColor(Color color)
	{
		colorButton.setStyle("-fx-background-color: #" + color.toString().substring(2));
	}

	private void setText(String name)
	{
		textField.setText(name);
	}


	public void setTag(Tag tag)
	{
		this.tag = tag;
		updateGraphics();
	}

	private void updateGraphics()
	{
		setText(tag.getName());
		setColor(tag.getColor());
	}
}
