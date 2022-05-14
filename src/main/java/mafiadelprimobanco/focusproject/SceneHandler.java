package mafiadelprimobanco.focusproject;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneHandler
{
	private static final SceneHandler instance = new SceneHandler();
	private final Alert alert;
	private Stage stage;
	private Scene scene;
	private BorderPane baseBorderPane;

	private SceneHandler()
	{
		alert = new Alert(Alert.AlertType.NONE);
	}

	public void init(Stage stage) throws IOException
	{
		this.stage = stage;
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(ViewReferences.BASE));
		this.scene = new Scene(fxmlLoader.load(), 900, 600);
		stage.setTitle("Focus");
		stage.setScene(scene);
		stage.show();
	}

	public void loadPage(String pagePathRef) throws IOException
	{
		assert baseBorderPane != null;
		FXMLLoader loader = new FXMLLoader(getClass().getResource(pagePathRef));
		baseBorderPane.setCenter(loader.load());
	}

	public void showInfoMessage(String title, String header, String message)
	{
		showMessage(title, header, message, Alert.AlertType.INFORMATION);
	}

	// alert methods

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

	private ButtonType showMessage(String title, String header, String message, Alert.AlertType alertType)
	{
		alert.setAlertType(alertType);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(message);
		alert.showAndWait();
		return alert.getResult();
	}

	public static SceneHandler getInstance() { return instance; }

	public void setBaseBorderPane(BorderPane baseBorderPane)
	{
		this.baseBorderPane = baseBorderPane;
	}


}
