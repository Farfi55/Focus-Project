package mafiadelprimobanco.focusproject;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialogBuilder;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.enums.ScrimPriority;
import io.github.palexdev.materialfx.font.MFXFontIcon;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Map;

public class Feedback
{
	private static final Feedback instance = new Feedback();

	public static Feedback getInstance() { return instance; }

	private Alert alert;

	private AnchorPane root;
	private MFXGenericDialog dialogContent;
	private MFXStageDialog dialog;


	public Feedback()
	{

	}



	public void init(Stage stage)
	{
		// legacy
		this.alert = new Alert(Alert.AlertType.NONE);

		// new
		this.dialogContent = MFXGenericDialogBuilder.build()
				.setContentText("TESTING 1\nTESTING 2\nTESTING 3\n...\n...")
				.makeScrollable(true)
				.get();
		this.dialog = MFXGenericDialogBuilder.build(dialogContent)
				.toStageDialogBuilder()
				.initOwner(stage)
				.initModality(Modality.APPLICATION_MODAL)
				.setDraggable(true)
				.setTitle("Dialogs Test")
				.setOwnerNode(root)
				.setScrimPriority(ScrimPriority.WINDOW)
				.setScrimOwner(true)
				.get();
		dialogContent.addActions(Map.entry(new MFXButton("OK"), event ->
		{
			dialog.close();
		}));
//				Map.entry(new MFXButton("Cancel"), event -> dialog.close()));

		dialogContent.setMaxSize(400, 230);

	}


	public void showInfo(String header, String message)
	{
		MFXFontIcon infoIcon = new MFXFontIcon("mfx-info-circle-filled", 18);
		dialogContent.setHeaderIcon(infoIcon);
		dialogContent.setHeaderText(header);
		dialogContent.setContentText(message);
		convertDialogTo("mfx-info-dialog");
		dialog.showDialog();
	}


	public void showWarning(String header, String message)
	{
		MFXFontIcon warnIcon = new MFXFontIcon("mfx-do-not-enter-circle", 18);
		dialogContent.setHeaderIcon(warnIcon);
		dialogContent.setHeaderText(header);
		dialogContent.setContentText(message);
		convertDialogTo("mfx-warn-dialog");
		dialog.showDialog();
	}


	public void showError(String header, String message)
	{
		MFXFontIcon errorIcon = new MFXFontIcon("mfx-exclamation-circle-filled", 18);
		dialogContent.setHeaderIcon(errorIcon);
		dialogContent.setHeaderText(header);
		dialogContent.setContentText(message);
		convertDialogTo("mfx-error-dialog");
		dialog.showDialog();
	}


	public void showGeneric(String header, String message)
	{
		dialogContent.setHeaderIcon(null);
		dialogContent.setHeaderText(header);
		dialogContent.setContentText(message);
		convertDialogTo(null);
		dialog.showDialog();
	}

	public void askConfirmation()
	{
		// todo: implement
		ButtonType result = ButtonType.CANCEL;

		dialogContent.setHeaderIcon(null);
		dialogContent.setHeaderText("This is a generic dialog");
		convertDialogTo(null);
		dialog.showDialog();
	}

	private void convertDialogTo(String styleClass)
	{
		dialogContent.getStyleClass().removeIf(s -> s.equals("mfx-info-dialog") || s.equals("mfx-warn-dialog") || s.equals(
						"mfx-error-dialog"));

		if (styleClass != null) dialogContent.getStyleClass().add(styleClass);
	}


	// legacy

	public void showInfoMessageLegacy(String title, String header, String message)
	{
		showMessageLegacy(title, header, message, Alert.AlertType.INFORMATION);
	}

	public void showInfoMessageLegacy(String title, String message)
	{
		showMessageLegacy(title, "", message, Alert.AlertType.INFORMATION);
	}

	public void showWarningMessageLegacy(String title, String header, String message)
	{
		showMessageLegacy(title, header, message, Alert.AlertType.WARNING);
	}

	public void showWarningMessageLegacy(String title, String message)
	{
		showMessageLegacy(title, "", message, Alert.AlertType.WARNING);
	}

	public void showErrorMessageLegacy(String title, String header, String message)
	{
		showMessageLegacy(title, header, message, Alert.AlertType.ERROR);
	}

	public void showErrorMessageLegacy(String title, String message)
	{
		showMessageLegacy(title, "", message, Alert.AlertType.ERROR);
	}

	public ButtonType showConfirmationMessageLegacy(String title, String header, String message)
	{
		return showMessageLegacy(title, header, message, Alert.AlertType.CONFIRMATION);
	}

	private ButtonType showMessageLegacy(String title, String header, String message, Alert.AlertType alertType)
	{
		alert.setAlertType(alertType);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(message);
		alert.showAndWait();
		return alert.getResult();
	}

	// getters and setters

	public AnchorPane getRoot()
	{
		return root;
	}

	public void setRoot(AnchorPane root)
	{
		this.root = root;
	}
}
