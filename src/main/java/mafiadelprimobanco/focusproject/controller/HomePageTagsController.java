package mafiadelprimobanco.focusproject.controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import mafiadelprimobanco.focusproject.handler.Localization;
import mafiadelprimobanco.focusproject.handler.SceneHandler;
import mafiadelprimobanco.focusproject.handler.TagHandler;
import mafiadelprimobanco.focusproject.model.Tag;
import mafiadelprimobanco.focusproject.model.TagsObserver;
import mafiadelprimobanco.focusproject.utils.LocalizationUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class HomePageTagsController implements TagsObserver, Controller
{

	List<TagController> tagsControllers;
	@FXML private VBox tagsList;
	private ToggleGroup toggleGroup;
	@FXML private MFXButton newTagButton;
	@FXML private Label tagLabel;
	@FXML private MFXScrollPane tagsSidebar;

	@Override
	public void terminate()
	{
		for (var tagController : tagsControllers)
		{
			tagController.terminate();
		}
		TagHandler.getInstance().removeListener(this);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		TagHandler.getInstance().addListener(this);
		toggleGroup = new ToggleGroup();
		populateTagsList();

		LocalizationUtils.bindLabelText(tagLabel, "tag.tags");
		LocalizationUtils.bindButtonText(newTagButton, "tag.newTag");
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
			if (child.getProperties().containsKey("tag-uuid") && child.getProperties().get("tag-uuid").equals(
					tag.getUuid()))
			{
				tagsList.getChildren().remove(child);
				break;
			}
		}
	}

	private void createTagView(Tag tag) throws IOException
	{
		// -1 because we want to keep the addTag button at the bottom
		int index = tagsList.getChildren().size() - 1;
		Node tagView = SceneHandler.getInstance().createTagView(tag, this.toggleGroup, tagsControllers);
		tagsList.getChildren().add(index, tagView);
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
