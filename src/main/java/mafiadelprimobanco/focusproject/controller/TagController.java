package mafiadelprimobanco.focusproject.controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXRectangleToggleNode;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import mafiadelprimobanco.focusproject.Feedback;
import mafiadelprimobanco.focusproject.Localization;
import mafiadelprimobanco.focusproject.TagHandler;
import mafiadelprimobanco.focusproject.model.Tag;
import mafiadelprimobanco.focusproject.model.TagsObserver;

public class TagController extends AnchorPane implements TagsObserver
{
	// we have a reference to the tag, but we never modify it directly
	// only using the TagHandler class
	private Tag tag;

	@FXML private ColorPicker colorPicker;
	@FXML private MFXButton colorButton;
	@FXML private MFXTextField textField;
	@FXML private MFXButton removeButton;
	@FXML private MFXRectangleToggleNode selectionButton;

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
		if (this.tag.equals(tag)) updateGraphics();
	}

	@Override
	public void onTagSelected(Tag tag)
	{
		if (this.tag.equals(tag)) selectionButton.setSelected(true);
	}

	public void init(Tag tag)
	{
		this.tag = tag;
		updateGraphics();
		TagHandler.getInstance().addListener(this);

		// is unset tag
		if (tag.getUuid() == 0)
		{
			colorPicker.setDisable(true);
			removeButton.setDisable(true);
			textField.setEditable(false);
			selectionButton.setSelected(true);
		}
	}

	@FXML
	void onSelectedAction(ActionEvent event)
	{
		if(TagHandler.getInstance().setSelectedTag(this.tag))
			selectionButton.setSelected(true);
		event.consume();
	}

	@FXML
	void onColorPickerAction(ActionEvent event)
	{
		Color colorPicked = colorPicker.getValue();
		TagHandler.getInstance().changeTag(tag.getName(), colorPicked, tag.getUuid());
		event.consume();
	}


	@FXML
	void onRemoveAction(Event event)
	{
		if (Feedback.getInstance().askYesNoConfirmation(Localization.get("warning.tag.deleteTag.header"),
				Localization.get("warning.tag.deleteTag.message")))
		{
			TagHandler.getInstance().removeTag(tag.getUuid());
		}
		event.consume();
	}

	@FXML
	void onTextFieldAction(ActionEvent event)
	{
		// if the update didn't go through, reset textField text
		if (TagHandler.getInstance().changeTag(textField.getText(), tag.getColor(), tag.getUuid()))
		{
			setText(tag.getName());
		}
		event.consume();


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

	public void setToggleGroup(ToggleGroup toggleGroup)
	{
		selectionButton.setToggleGroup(toggleGroup);
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


