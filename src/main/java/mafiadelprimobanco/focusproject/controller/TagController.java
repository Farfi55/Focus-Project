package mafiadelprimobanco.focusproject.controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class TagController {

	@FXML
	private MFXButton colorButton;

	@FXML
	private ColorPicker colorPicker;

	@FXML
	private MFXButton removeButton;

	@FXML
	private MFXTextField textField;

	@FXML
	void onColorPickerAction(ActionEvent event)
	{
		Color colorPicked = colorPicker.getValue();
		colorButton.setStyle("-fx-background-color:" + toHexColor(colorPicked) + ";");
	}

	@FXML
	void onRemoveButtonClicked(MouseEvent event)
	{

	}

	public static String toHexColor( Color color )
	{
		return String.format( "#%02X%02X%02X",
						      (int)( color.getRed() * 255 ),
							  (int)( color.getGreen() * 255 ),
							  (int)( color.getBlue() * 255 ) );
	}

}


