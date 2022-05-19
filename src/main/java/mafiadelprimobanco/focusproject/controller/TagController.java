package mafiadelprimobanco.focusproject.controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import mafiadelprimobanco.focusproject.TagHandler;
import mafiadelprimobanco.focusproject.model.Tag;
import mafiadelprimobanco.focusproject.model.TagsObserver;

public class TagController extends AnchorPane implements TagsObserver
{
	private Tag tag;

	@FXML private MFXButton colorButton;

	@FXML private MFXTextField textField;

	@Override
	public void onTagChanged(Tag tag)
	{
		if (this.tag.getUuid().equals(tag.getUuid())) updateGraphics();
	}


	@FXML
	void onColorButtonClicked(ActionEvent event) { }

	@FXML
	void onRemoveButtonClicked(ActionEvent event) {
		TagHandler.getInstance().removeTag(tag.getUuid());
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
}
