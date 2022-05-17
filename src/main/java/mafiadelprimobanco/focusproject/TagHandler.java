package mafiadelprimobanco.focusproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import mafiadelprimobanco.focusproject.model.Tag;

import java.util.List;
import java.util.Vector;

public class TagHandler
{
	private static final TagHandler instance = new TagHandler();
	public static TagHandler getInstance(){ return instance; }

	private TagHandler() { }

	private final ObservableList<AnchorPane> tagViewList = FXCollections.observableArrayList();
	private final ObservableList<Node> tagList = FXCollections.observableArrayList();

	public void loadFromDatabase() { /* TODO */ }

	public void addTagView(TagView tagView)
	{
		tagViewList.add(tagView.getView());
		//tagList.add(tagView);
	}

	public ObservableList<AnchorPane> getTags() { return tagViewList; }
}
