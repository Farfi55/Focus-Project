package mafiadelprimobanco.focusproject;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import mafiadelprimobanco.focusproject.controller.TagController;
import mafiadelprimobanco.focusproject.model.Tag;

import java.io.IOException;

public class TagView
{
	Tag tag;
	TagController tagController;
	AnchorPane view;

	public TagView(Tag tag)
	{
		FXMLLoader tagFxml = new FXMLLoader(getClass().getResource("Tag-view.fxml"));

		this.tag = tag;

		//TagHandler.getInstance().addTag(tag.getTagName(), tag.getColor());

		try
		{
			view = tagFxml.load();
		}
		catch (IOException e)
		{
			//e.printStackTrace();
		}
		tagController = tagFxml.getController();

		tagController.setColorButton(tag.getColor());
		tagController.setTagText(tag.getTagName());
	}

	public AnchorPane getView()
	{
		return view;
	}
}
