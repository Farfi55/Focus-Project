package mafiadelprimobanco.focusproject;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialogBuilder;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.enums.ScrimPriority;
import io.github.palexdev.materialfx.font.MFXFontIcon;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Map;

public class Feedback
{
	private static final Feedback instance = new Feedback();

	public static Feedback getInstance() { return instance; }

//	private Alert alert;

	private AnchorPane root;
	private Stage stage;

	private MFXGenericDialog dialogContent;
	private MFXStageDialog dialog;

	private ButtonData confirmationDialogResult;
	private MFXGenericDialog confirmationDialogContent;
	private MFXStageDialog confirmationDialog;

	public Feedback() { }



	public void init(Stage stage)
	{
		this.stage = stage;
		dialogContent = initDialogContent();
		dialog = initDialog(dialogContent);
		addButton(dialogContent, dialog, ButtonData.OK_DONE);
		dialogContent.setMaxSize(400, 230);

		confirmationDialogContent = initDialogContent();
		confirmationDialog = initDialog(confirmationDialogContent);

		dialogContent.setMaxSize(400, 230);

	}

	private MFXGenericDialog initDialogContent()
	{
		return MFXGenericDialogBuilder.build().makeScrollable(true).get();
	}

	private MFXStageDialog initDialog(MFXGenericDialog dialogContent)
	{
		return MFXGenericDialogBuilder.build(dialogContent).toStageDialogBuilder().initOwner(this.stage).initModality(
				Modality.APPLICATION_MODAL).setDraggable(true).setOwnerNode(this.root).setScrimPriority(
				ScrimPriority.WINDOW).setScrimOwner(true).get();
	}


	// public confirmation button interface

//	/**
//	 * <p>removes all confirmation dialog's buttons</p>
//	 * <p>use before calling {@code addButton}</p>
//	 */
//	public void clearButtons() { clearButtons(confirmationDialogContent); }

	/**
	 * <p>creates a button with text correlated to the chosen buttonType</p>
	 *
	 * @param buttonType button type, used for confirmationDialog result
	 */
	public void addButton(ButtonData buttonType)
	{
		addButton(confirmationDialogContent, confirmationDialog, buttonType);
	}

	/**
	 * <p>creates a button with {@link ButtonData}.{@code OTHER}</p>
	 *
	 * @param buttonText text displayed to the user
	 */
	public void addButton(String buttonText) { addButton(confirmationDialogContent, confirmationDialog, buttonText); }

	/**
	 * <p>creates a button with {@code buttonText} that returns {@code buttonType}</p>
	 *
	 * @param buttonType button type, used for confirmationDialog result
	 * @param buttonText text displayed to the user
	 */
	public void addButton(ButtonData buttonType, String buttonText)
	{
		addButton(confirmationDialogContent, confirmationDialog, buttonType, buttonText);
	}

//	private void clearButtons(MFXGenericDialog dialogContent) { dialogContent.clearActions(); }

	private void addButton(MFXGenericDialog dialogContent, MFXStageDialog dialog, ButtonData buttonType)
	{
		addButton(dialogContent, dialog, buttonType, getButtonTextFromType(buttonType));
	}

	private void addButton(MFXGenericDialog dialogContent, MFXStageDialog dialog, String buttonText)
	{
		dialogContent.addActions(Map.entry(new MFXButton(buttonText), event ->
		{
			this.confirmationDialogResult = ButtonData.OTHER;
			dialog.close();
		}));
	}


	private void addButton(MFXGenericDialog dialogContent, MFXStageDialog dialog, ButtonData buttonType,
			String buttonText)
	{
		dialogContent.addActions(Map.entry(new MFXButton(buttonText), event ->
		{
			this.confirmationDialogResult = buttonType;
			dialog.close();
		}));
	}


	private String getButtonTextFromType(ButtonData buttonType)
	{
		return switch (buttonType)
				{
					case CANCEL_CLOSE -> "Annulla";
					case OK_DONE -> "OK";
					case YES -> "Si";
					case NO -> "No";
					case APPLY -> "Applica";
					case FINISH -> "Finisci";
					case NEXT_FORWARD -> "Prossimo";
					case BACK_PREVIOUS -> "Precedente";
					case HELP -> "Aiuto";
					case LEFT -> "Sinistra";
					case RIGHT -> "Destra";
					case OTHER -> "Altro";
					default -> throw new IllegalArgumentException("Usupported buttonType" + buttonType);
				};
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


	/**
	 * shows a dialog with
	 *
	 * @param header  title of the dialog
	 * @param message main body of the dialog
	 * @return ButtonData specified by the Button clicked or {@code ButtonData.CANCEL_CLOSE} if none were clicked
	 */
	public ButtonData askConfirmation(String header, String message)
	{
		confirmationDialogResult = ButtonData.CANCEL_CLOSE;

		confirmationDialogContent.setHeaderIcon(null);
		confirmationDialogContent.setHeaderText(header);
		confirmationDialogContent.setContentText(message);
		confirmationDialog.showAndWait();
		confirmationDialogContent.clearActions();
		return confirmationDialogResult;
	}

	/**
	 * shows a dialog with <i>Yes</i> and <i>No</i> as buttons
	 *
	 * @param header  title of the dialog
	 * @param message main body of the dialog
	 * @return true if user pressed Yes, else false
	 */
	public Boolean askYesNoConfirmation(String header, String message)
	{
		addButton(ButtonData.YES);
		addButton(ButtonData.NO);

		confirmationDialogResult = ButtonData.CANCEL_CLOSE;
		confirmationDialogContent.setHeaderIcon(null);
		confirmationDialogContent.setHeaderText(header);
		confirmationDialogContent.setContentText(message);
		confirmationDialog.showAndWait();
		confirmationDialogContent.clearActions();
		return confirmationDialogResult.equals(ButtonData.YES);
	}

	private void convertDialogTo(String styleClass)
	{
		dialogContent.getStyleClass().removeIf(
				s -> s.equals("mfx-info-dialog") || s.equals("mfx-warn-dialog") || s.equals("mfx-error-dialog"));

		if (styleClass != null) dialogContent.getStyleClass().add(styleClass);
	}

	/*
	* legacy showMessages

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
	*/
	// getters and setters

	public AnchorPane getRoot()
	{
		return root;
	}

	public void setRoot(AnchorPane root)
	{
		this.root = root;
		confirmationDialog.setOwnerNode(root);
		dialog.setOwnerNode(root);
	}
}
