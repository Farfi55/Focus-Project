package mafiadelprimobanco.focusproject;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import mafiadelprimobanco.focusproject.model.utils.FXMLReferences;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;

public class SceneHandler
{

	private static final SceneHandler instance = new SceneHandler();

	public static SceneHandler getInstance() { return instance; }
	private final Alert alert;
	private Stage stage;
	private Scene scene;
	private BorderPane baseBorderPane;
	private String currentTheme = "light";

	private SceneHandler()
	{
		alert = new Alert(Alert.AlertType.NONE);
	}

	public void init(Stage stage) throws IOException
	{
		this.stage = stage;
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(FXMLReferences.BASE));
		this.scene = new Scene(fxmlLoader.load(), 900, 600);
		stage.setTitle("Focus");
		stage.setScene(scene);
		loadFonts();
		loadStyle();

		stage.show();
	}

	public void loadPage(String pagePathRef) throws IOException
	{
		assert baseBorderPane != null;
		FXMLLoader loader = new FXMLLoader(getClass().getResource(pagePathRef));
		baseBorderPane.setCenter(loader.load());
	}

	// alert methods

	public void showInfoMessage(String title, String header, String message)
	{
		showMessage(title, header, message, Alert.AlertType.INFORMATION);
	}

	public void showInfoMessage(String title, String message)
	{
		showMessage(title, "", message, Alert.AlertType.INFORMATION);
	}

	public void showWariningMessage(String title, String header, String message)
	{
		showMessage(title, header, message, Alert.AlertType.WARNING);
	}

	public void showWariningMessage(String title, String message)
	{
		showMessage(title, "", message, Alert.AlertType.WARNING);
	}

	public void showErrorMessage(String title, String header, String message)
	{
		showMessage(title, header, message, Alert.AlertType.ERROR);
	}

	public void showErrorMessage(String title, String message)
	{
		showMessage(title, "", message, Alert.AlertType.ERROR);
	}

	public ButtonType showConfirmationMessage(String title, String header, String message)
	{
		return showMessage(title, header, message, Alert.AlertType.CONFIRMATION);
	}

	// Style & Font methods

	private ButtonType showMessage(String title, String header, String message, Alert.AlertType alertType)
	{
		alert.setAlertType(alertType);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(message);
		alert.showAndWait();
		return alert.getResult();
	}

	private void loadFonts()
	{
		// load fonts
		for (String font : List.of("fonts/Roboto/Roboto-Regular.ttf", "fonts/Roboto/Roboto-Bold.ttf"))
			Font.loadFont(String.valueOf(getClass().getResource(font)), 10);
	}

	private void loadStyle()
	{
		scene.getStylesheets().clear();
		alert.getDialogPane().getStylesheets().clear();

		// load style
		for (String style : List.of(currentTheme, "fonts", "style"))
		{
			URL url = getClass().getResource("css/" + style + ".css");
			String resource = String.valueOf(url);
			scene.getStylesheets().add(resource);
			alert.getDialogPane().getStylesheets().add(resource);
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

	public void setBaseBorderPane(BorderPane baseBorderPane)
	{
		this.baseBorderPane = baseBorderPane;
	}

}
