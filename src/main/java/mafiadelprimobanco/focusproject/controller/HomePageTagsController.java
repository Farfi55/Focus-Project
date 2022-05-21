package mafiadelprimobanco.focusproject.controller;

import io.github.palexdev.materialfx.controls.MFXScrollPane;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import mafiadelprimobanco.focusproject.SceneHandler;
import mafiadelprimobanco.focusproject.TagHandler;
import mafiadelprimobanco.focusproject.model.Tag;
import mafiadelprimobanco.focusproject.model.TagsObserver;

import java.io.IOException;

public class HomePageTagsController implements TagsObserver
{

	@FXML private VBox tagsList;

	private ToggleGroup toggleGroup;

	@FXML private MFXScrollPane tagsSidebar;


	@FXML
	void initialize()
	{
		TagHandler.getInstance().addListener(this);
		toggleGroup = new ToggleGroup();
		populateTagsList();
	}

	@Override
	public void onTagAdded(Tag tag)
	{
		Platform.runLater(() ->
		{
			try
			{
				createTagView(tag);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		});
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

	private void createTagView(Tag tag) throws IOException
	{
		// -1 because we want to keep the addTag button at the bottom
		int index = tagsList.getChildren().size() - 1;
		Node tagView = SceneHandler.getInstance().createTagView(tag, this.toggleGroup);
		tagsList.getChildren().add(index, tagView);

//		if(tagView instanceof Toggle toggle)
//		{
//			toggle.setToggleGroup(this.toggleGroup);
//			System.out.println(toggle.getToggleGroup());
//		}
//		else System.err.println("Unable to inject toggle group to tag " + tag.getName());
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
				createTagView(tag);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

}
