package mafiadelprimobanco.focusproject;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import mafiadelprimobanco.focusproject.controller.TagController;
import mafiadelprimobanco.focusproject.model.Tag;
import mafiadelprimobanco.focusproject.model.utils.FXMLReferences;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class SceneHandler
{

	private static final SceneHandler instance = new SceneHandler();

	public static SceneHandler getInstance() { return instance; }


	private Stage stage;
	private Scene scene;
	private String currentTheme = "light";
	private AnchorPane root;

	private SceneHandler() { }

	public void init(Stage stage) throws IOException
	{
		this.stage = stage;
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(FXMLReferences.BASE));
		this.scene = new Scene(fxmlLoader.load(), 1150, 600);
		stage.setTitle("Focus");
		stage.setScene(scene);
		loadFonts();
		loadStyle();
		stage.show();
	}


	public Node loadPage(String pagePathRef) throws IOException
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource(pagePathRef));
		return loader.load();
	}

	/*public Node createTagView() throws IOException
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource(FXMLReferences.TAG));
		return loader.load();
	}*/

	public Node createTagView(Tag tag) throws IOException
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource(FXMLReferences.TAG));
		Node node = loader.load();
		node.getProperties().put("tag-uuid", tag.getUuid());
		TagController tagController = loader.getController();
		tagController.setTag(tag);
		return node;
	}

	// dialog methods


	// Style & Font methods



	private void loadFonts()
	{
		// load fonts
		for (String font : List.of("fonts/Roboto/Roboto-Regular.ttf", "fonts/Roboto/Roboto-Bold.ttf"))
			Font.loadFont(String.valueOf(getClass().getResource(font)), 10);
	}

	private void loadStyle()
	{
		scene.getStylesheets().clear();

		// load style
		for (String style : List.of(currentTheme, "fonts", "style"))
		{
			URL url = getClass().getResource("css/" + style + ".css");
			String resource = String.valueOf(url);
			scene.getStylesheets().add(resource);
		}
	}

	public void toggleLightDarkTheme()
	{
		currentTheme = currentTheme.equals("light") ? "dark" : "light";
		loadStyle();
	}

	private void changeTheme(String newTheme)
	{
		// if the user can use custom themes then sanitize 'newTheme'
		currentTheme = newTheme;
		loadStyle();
	}

	public AnchorPane getRoot() { return this.root; }

	public void setRoot(AnchorPane root) { this.root = root; }
}
