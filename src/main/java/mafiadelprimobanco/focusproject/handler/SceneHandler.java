package mafiadelprimobanco.focusproject.handler;

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
import mafiadelprimobanco.focusproject.Localization;
import mafiadelprimobanco.focusproject.controller.TagController;
import mafiadelprimobanco.focusproject.model.Page;
import mafiadelprimobanco.focusproject.model.Tag;
import mafiadelprimobanco.focusproject.utils.FXMLReferences;
import mafiadelprimobanco.focusproject.utils.ResourcesLoader;

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
		this.scene = new Scene(loadFXML(FXMLReferences.BASE), 1150, 600);
		setTitle("Focus");
		stage.setScene(scene);
		isFullScreen = stage.fullScreenProperty();
		loginPopup = loadFXML(FXMLReferences.LOGIN_POPUP);
		loadFonts();
		setStyleSheets();
		stage.show();


		popupPane.getChildren().add(loginPopup);

		subscribeToEvents();
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

	private void subscribeToEvents()
	{
		subscribeToStyleChanges();
		PagesHandler.getCurrentPagePropriety().addListener(observable -> updateTitle());
		Localization.localeProperty().addListener(observable -> updateTitle());
	}

	private void updateTitle()
	{
		setTitle("Focus - " + Localization.get(PagesHandler.getCurrentPage().pageNameKey()));
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

	public Parent loadFXML(String fxmlPath) throws IOException
	{
		FXMLLoader loader = getFXMLLoader(fxmlPath);
		return loader.load();
	}

	public FXMLLoader getFXMLLoader(String fxmlPath) throws IOException
	{
		return new FXMLLoader(ResourcesLoader.loadURL(fxmlPath));
	}

	public Node createTagView(Tag tag) throws IOException
	{ return createTagView(tag, null, null); }

	public Node createTagView(Tag tag, ToggleGroup toggleGroup) throws IOException
	{ return createTagView(tag, toggleGroup, null); }

	public Node createTagView(Tag tag, ToggleGroup toggleGroup, List<TagController> tagControllers) throws IOException
	{
		FXMLLoader loader = getFXMLLoader(FXMLReferences.TAG);
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
		for (String font : List.of("../fonts/Roboto/Roboto-Regular.ttf", "../fonts/Roboto/Roboto-Bold.ttf"))
			Font.loadFont(String.valueOf(getClass().getResource(font)), 10);
	}

	public void toggleFullScreen()
	{
		stage.setFullScreen(!isFullScreen());
	}

	private void setStyleSheets()
	{
		scene.getStylesheets().setAll(StyleHandler.getInstance().getObservableStyles());
		loginPopup.getStylesheets().setAll(StyleHandler.getInstance().getObservableStyles());
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
		FXMLLoader loader = getFXMLLoader(page.FXMLPath());
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

	private void setTitle(String title)
	{
		stage.setTitle(title);
	}

	public void setContentPane(StackPane pane) { this.contentPane = pane; }
}
