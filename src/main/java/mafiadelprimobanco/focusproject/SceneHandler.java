package mafiadelprimobanco.focusproject;

import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import mafiadelprimobanco.focusproject.controller.TagController;
import mafiadelprimobanco.focusproject.model.Page;
import mafiadelprimobanco.focusproject.model.Tag;
import mafiadelprimobanco.focusproject.model.utils.FXMLReferences;

import java.io.IOException;
import java.util.List;

public class SceneHandler
{

	private static final SceneHandler instance = new SceneHandler();

	public static SceneHandler getInstance() { return instance; }

	//TODO: Make that less ugly
	private final Pane popupPane = new Pane();
	private Stage stage;
	private Parent loginPopup;
	private Scene scene;
	private AnchorPane root;
	private StackPane contentPane;
	private ReadOnlyBooleanProperty isFullScreen;

	private SceneHandler() { }

	public void init(Stage stage) throws IOException
	{
		this.stage = stage;
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(FXMLReferences.BASE));
		this.scene = new Scene(fxmlLoader.load(), 1150, 600);
		stage.setTitle("Focus");
		stage.setScene(scene);
		isFullScreen = stage.fullScreenProperty();
		loadFonts();
		setStyleSheets();
		stage.show();

		loginPopup = FXMLLoader.load(getClass().getResource("Login-popup-view.fxml"));

		popupPane.getChildren().add(loginPopup);

		subscribeToStyleChanges();

		KeyPressManager.getInstance().addHandler(event ->
		{
			if (event.getCode().equals(KeyCode.F11)) toggleFullScreen();
		});

		stage.setOnCloseRequest(windowEvent ->
		{
			if (!Feedback.getInstance().askYesNoConfirmation("Chiudi applicazione Focus ",
					"Sei sicuro di voler chiudere l'applicazione?")) windowEvent.consume();
			else AutentificationHandler.getInstance().doLogout();
		});

		stage.widthProperty().addListener(e -> closeLoginPopup());
		stage.heightProperty().addListener(e -> closeLoginPopup());
		popupPane.setOnMouseClicked(e ->
		{
			//If the click is on popup pane close the login popup
			if (e.getPickResult().getIntersectedNode().equals(popupPane)) closeLoginPopup();
		});
	}

	public void showLoginPopup()
	{
		loginPopup.setLayoutX(0);
		loginPopup.setLayoutY(contentPane.getHeight() - 275);

		contentPane.getChildren().add(popupPane);
	}

	public void closeLoginPopup() { contentPane.getChildren().remove(popupPane); }

	private void subscribeToStyleChanges()
	{
		StyleHandler.getInstance().getObservableStyles().addListener(
				(ListChangeListener<String>)change -> setStyleSheets());
	}

	public Node loadFXML(String fxmlPath) throws IOException
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
		return loader.load();
	}

	public FXMLLoader getFXMLLoader(String fxmlPath) throws IOException
	{
		return new FXMLLoader(getClass().getResource(fxmlPath));
	}

	public Node createTagView(Tag tag) throws IOException
	{ return createTagView(tag, null, null); }

	public Node createTagView(Tag tag, ToggleGroup toggleGroup) throws IOException
	{ return createTagView(tag, toggleGroup, null); }

	public Node createTagView(Tag tag, ToggleGroup toggleGroup, List<TagController> tagControllers) throws IOException
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource(FXMLReferences.TAG));
		Node node = loader.load();
		node.getProperties().put("tag-uuid", tag.getUuid());
		TagController tagController = loader.getController();
		tagController.init(tag);
		tagController.setToggleGroup(toggleGroup);
		if (tagControllers != null) tagControllers.add(tagController);
		return node;
	}

	private void loadFonts()
	{
		// load fonts
		for (String font : List.of("fonts/Roboto/Roboto-Regular.ttf", "fonts/Roboto/Roboto-Bold.ttf"))
			Font.loadFont(String.valueOf(getClass().getResource(font)), 10);
	}

	public void toggleFullScreen()
	{
		stage.setFullScreen(!isFullScreen());
	}

	private void setStyleSheets()
	{
		scene.getStylesheets().setAll(StyleHandler.getInstance().getObservableStyles());
	}

	public void toggleLoginPopup()
	{
		if (popupPane.getParent() == null) showLoginPopup();
		else closeLoginPopup();
	}

	public void showPage(Page page)
	{
		contentPane.getChildren().setAll(page.pageRoot().get());
	}

	public void loadPage(Page page) throws IOException
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource(page.FXMLPath()));
		page.pageRoot().set(loader.load());
		page.controller().set(loader.getController());
		showPage(page);
	}

	public ReadOnlyBooleanProperty getIsFullScreen()
	{
		return isFullScreen;
	}

	public boolean isFullScreen()
	{
		return isFullScreen.get();
	}

	public AnchorPane getRoot() { return this.root; }

	public void setRoot(AnchorPane root) { this.root = root; }

	public void setContentPane(StackPane pane) { this.contentPane = pane; }
}
