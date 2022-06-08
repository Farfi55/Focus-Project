package mafiadelprimobanco.focusproject.model;

import io.github.palexdev.materialfx.controls.MFXSimpleNotification;
import io.github.palexdev.materialfx.factories.InsetsFactory;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class DefaultNotification extends MFXSimpleNotification
{
	private final StringProperty headerText = new SimpleStringProperty("Notification Header");
	private final StringProperty contentText = new SimpleStringProperty();
	private final BorderPane container;

	public DefaultNotification() {

//		MFXIconWrapper icon = new MFXIconWrapper(RandomUtils.randFromArray(Model.notificationsIcons).getDescription(), 16, 32);
		Label headerLabel = new Label();
		headerLabel.textProperty().bind(headerText);
		headerLabel.setPadding(InsetsFactory.of(5,15, 5,15));
		headerLabel.getStyleClass().add("header");
//		MFXIconWrapper readIcon = new MFXIconWrapper("mfx-eye", 16, 32);
//		((MFXFontIcon) readIcon.getIcon()).descriptionProperty().bind(Bindings.createStringBinding(
//				() -> (getState() == NotificationState.READ) ? "mfx-eye" : "mfx-eye-slash",
//				notificationStateProperty()
//		));
//		StackPane.setAlignment(readIcon, Pos.CENTER_RIGHT);
//		HBox header = new HBox(headerLabel);
//		header.setAlignment(Pos.CENTER_LEFT);
//		header.setPadding(InsetsFactory.of(5, 0, 5, 0));
//		header.setMaxWidth(Double.MAX_VALUE);


		Label contentLabel = new Label();
//		contentLabel.getStyleClass().add("content");
		contentLabel.textProperty().bind(contentText);
		contentLabel.setWrapText(true);
		contentLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		contentLabel.setAlignment(Pos.TOP_LEFT);
		contentLabel.setPadding(InsetsFactory.of(3,10, 3,10));
		contentLabel.getStyleClass().add("content");

//		MFXButton action1 = new MFXButton("Action 1");
//		MFXButton action2 = new MFXButton("Action 2");
//		HBox actionsBar = new HBox(15, action1, action2);
//		actionsBar.getStyleClass().add("actions-bar");
//		actionsBar.setAlignment(Pos.CENTER_RIGHT);
//		actionsBar.setPadding(InsetsFactory.all(5));

		container = new BorderPane();
		container.getStyleClass().add("notification");
		container.setTop(headerLabel);
		container.setCenter(contentLabel);
//		container.setBottom(actionsBar);
//		container.getStylesheets().add(MFXDemoResourcesLoader.load("css/ExampleNotification.css"));
		container.setMinSize(150, 80);
		container.setMaxSize(400, 200);

		setContent(container);
	}

	public ObservableList<String> getStylesheets()
	{
		return container.getStylesheets();
	}

	public String getHeaderText() {
		return headerText.get();
	}

	public StringProperty headerTextProperty() {
		return headerText;
	}

	public void setHeaderText(String headerText) {
		this.headerText.set(headerText);
	}

	public String getContentText() {
		return contentText.get();
	}

	public StringProperty contentTextProperty() {
		return contentText;
	}

	public void setContentText(String contentText) {
		this.contentText.set(contentText);
	}
}