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
import javafx.scene.paint.Color;
import mafiadelprimobanco.focusproject.handler.AudioHandler;
import mafiadelprimobanco.focusproject.handler.Feedback;
import mafiadelprimobanco.focusproject.handler.Localization;
import mafiadelprimobanco.focusproject.handler.TagHandler;
import mafiadelprimobanco.focusproject.model.Tag;
import mafiadelprimobanco.focusproject.model.TagsObserver;

import java.net.URL;
import java.util.ResourceBundle;

public class TagController implements TagsObserver, Controller
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
	public void initialize(URL location, ResourceBundle resources)
	{
	}

	@Override
	public void onTagRemoving(Tag tag)
	{
		if (this.tag.equals(tag))
		{
			Platform.runLater(this::terminate);
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
			AudioHandler.getInstance().playPopTagAudioClip();
		}
		event.consume();
	}

	@FXML
	void onTextFieldAction(ActionEvent event)
	{
		// if the update didn't go through, reset textField text
		if (!TagHandler.getInstance().changeTag(textField.getText(), tag.getColor(), tag.getUuid()))
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

	@Override
	public void terminate()
	{
		TagHandler.getInstance().removeListener(this);
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


