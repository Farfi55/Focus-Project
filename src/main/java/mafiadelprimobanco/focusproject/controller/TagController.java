package mafiadelprimobanco.focusproject.controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXRectangleToggleNode;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import mafiadelprimobanco.focusproject.Feedback;
import mafiadelprimobanco.focusproject.TagHandler;
import mafiadelprimobanco.focusproject.model.Tag;
import mafiadelprimobanco.focusproject.model.TagsObserver;

public class TagController extends AnchorPane implements TagsObserver
{
	private Tag tag;

	@FXML private ColorPicker colorPicker;
	@FXML private MFXButton colorButton;
	@FXML private MFXTextField textField;
	@FXML private MFXRectangleToggleNode selectionButton;

//	@FXML private MFXButton removeButton;

	// todo refactor: this should not go in here
	public static String toHexColor(Color color)
	{
		// return "#" + Integer.toHexString(your_color.getRGB()).substring(2);
		return String.format("#%02X%02X%02X", (int)(color.getRed() * 255), (int)(color.getGreen() * 255),
				(int)(color.getBlue() * 255));
	}

	@Override
	public void onTagRemoving(Tag tag)
	{
		if (this.tag.equals(tag))
		{
			Platform.runLater(() -> TagHandler.getInstance().removeListener(this));
		}
	}

	@Override
	public void onTagChanged(Tag tag)
	{
//		System.out.println("tag: " + tag.getName() + " changed, I am " + this
//				.tag.getName());
		if (this.tag.equals(tag))
		{
			updateGraphics();
		}
	}

	public void init(Tag tag)
	{
		this.tag = tag;
		updateGraphics();
		TagHandler.getInstance().addListener(this);
	}

	@FXML
	void onSelectedAction(ActionEvent event) {
		TagHandler.getInstance().setSelectedTag(this.tag);
	}

	@FXML
	void onColorPickerAction(ActionEvent event)
	{
		Color colorPicked = colorPicker.getValue();
		TagHandler.getInstance().updateTag(tag.getName(), colorPicked, tag.getUuid());
	}

	@FXML
	void onRemoveAction(ActionEvent event)
	{
		if (Feedback.getInstance().askYesNoConfirmation("Eliminazione tag",
				"Sei sicuro di voler rimuovere questa tag?"))
		{
			TagHandler.getInstance().removeTag(tag.getUuid());

		}
	}

	@FXML
	void onTextFieldAction(ActionEvent event)
	{
		// if the update didn't go through, reset textField text
		if (!TagHandler.getInstance().updateTag(textField.getText(), tag.getColor(), tag.getUuid()))
		{
			setText(tag.getName());
		}

	}

	private void updateGraphics()
	{
		setText(tag.getName());
		setColor(tag.getColor());
	}

	public void setToggleGroup(ToggleGroup toggleGroup)
	{
		selectionButton.setToggleGroup(toggleGroup);
	}

	public final Tag getTag()
	{
		return tag;
	}
	
	private void setColor(Color color)
	{
		colorButton.setStyle("-fx-background-color: #" + color.toString().substring(2));
	}

	private void setText(String name)
	{
		textField.setText(name);
	}


}


