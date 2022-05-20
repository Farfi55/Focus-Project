package mafiadelprimobanco.focusproject.controller;

import io.github.palexdev.materialfx.controls.MFXScrollPane;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import mafiadelprimobanco.focusproject.SceneHandler;
import mafiadelprimobanco.focusproject.TagHandler;
import mafiadelprimobanco.focusproject.model.Tag;
import mafiadelprimobanco.focusproject.model.TagsObserver;

import java.io.IOException;

public class HomePageTagsController implements TagsObserver
{

	@FXML private VBox tagsList;

	@FXML private MFXScrollPane tagsSidebar;


	@FXML
	void initialize()
	{
		TagHandler.getInstance().addListener(this);

		populateTagsList();
	}

	@Override
	public void onTagAdded(Tag tag)
	{
		try
		{
			// -1 because we want to keep the addTag button at the bottom
			int index = tagsList.getChildren().size() - 1;
			tagsList.getChildren().add(0, SceneHandler.getInstance().createTagView(tag));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void onTagRemoving(Tag tag)
	{
		for (Node child : tagsList.getChildren())
		{
			if (child.getProperties().containsKey("tag-uuid")) if (child.getProperties().get("tag-uuid").equals(
					tag.getUuid()))
			{
				tagsList.getChildren().remove(child);
				return;
			}
		}
	}

	@FXML
	void onNewTagAction(ActionEvent event)
	{
		TagHandler.getInstance().addTag();
	}

	private void populateTagsList()
	{
		try
		{
			for (Tag tag : TagHandler.getInstance().getTags())
			{
				// -1 because we want to keep the addTag button at the bottom
				int index = tagsList.getChildren().size() - 1;
				tagsList.getChildren().add(index, SceneHandler.getInstance().createTagView(tag));

			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

}
