package mafiadelprimobanco.focusproject.controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import mafiadelprimobanco.focusproject.Feedback;
import mafiadelprimobanco.focusproject.TagHandler;
import mafiadelprimobanco.focusproject.model.Tag;
import mafiadelprimobanco.focusproject.model.TagsObserver;

public class TagController extends AnchorPane implements TagsObserver
{
	private Tag tag;
  	private ColorPicker colorPicker;

	@FXML private MFXButton colorButton;
	@FXML private MFXTextField textField;
  	@FXML private MFXButton removeButton;	
  

	@Override
	public void onTagChanged(Tag tag)
	{
		if (this.tag.getUuid().equals(tag.getUuid())) updateGraphics();
	}
	
	

	@FXML
	void onColorPickerAction(ActionEvent event)
	{
		Color colorPicked = colorPicker.getValue();
		colorButton.setStyle("-fx-background-color:" + toHexColor(colorPicked) + ";");
	}

	private void updateGraphics()
	{
		setText(tag.getName());
		setColor(tag.getColor());
	}

	public final Tag getTag()
	{
		return tag;
	}

  	@FXML
	void onColorButtonClicked(ActionEvent event) { }

	@FXML
	void onRemoveButtonClicked(ActionEvent event)
	{
		if (Feedback.getInstance().askYesNoConfirmation("Eliminazione tag",
				"Sei sicuro di voler rimuovere questa tag?"))
		{
			TagHandler.getInstance().removeTag(tag.getUuid());

		}


	public void setTag(Tag tag)
	{
		this.tag = tag;
		updateGraphics();
	}

	private void setColor(Color color)
	{
		colorButton.setStyle("-fx-background-color: #" + color.toString().substring(2));
	}

	private void setText(String name)
	{
		textField.setText(name);
	}

	public static String toHexColor( Color color )
	{
		return String.format( "#%02X%02X%02X",
						      (int)( color.getRed() * 255 ),
							  (int)( color.getGreen() * 255 ),
							  (int)( color.getBlue() * 255 ) );
	}


}


