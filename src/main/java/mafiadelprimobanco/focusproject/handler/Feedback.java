package mafiadelprimobanco.focusproject.handler;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXProgressBar;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialogBuilder;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.enums.NotificationPos;
import io.github.palexdev.materialfx.enums.ScrimPriority;
import io.github.palexdev.materialfx.font.MFXFontIcon;
import io.github.palexdev.materialfx.notifications.MFXNotificationSystem;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mafiadelprimobanco.focusproject.model.ActivityRecapDialog;
import mafiadelprimobanco.focusproject.model.DefaultNotification;
import mafiadelprimobanco.focusproject.model.Tag;
import mafiadelprimobanco.focusproject.model.Tree;
import mafiadelprimobanco.focusproject.model.activity.AbstractActivity;
import mafiadelprimobanco.focusproject.model.activity.ChronometerActivity;
import mafiadelprimobanco.focusproject.model.activity.TimerActivity;
import mafiadelprimobanco.focusproject.utils.TimeUtils;

import java.time.temporal.ChronoUnit;
import java.util.Map;

import static mafiadelprimobanco.focusproject.utils.TextFlowUtils.addBoldText;
import static mafiadelprimobanco.focusproject.utils.TextFlowUtils.addText;

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


	private ActivityRecapDialog activityRecapDialogContent;
	private MFXStageDialog activityRecapDialog;


//	private DefaultNotification notification;

	private Feedback() { }



	public void init(Stage stage)
	{
		this.stage = stage;

		// general dialog init
		dialogContent = initDialogContent();
		dialogContent.setMaxSize(400, 230);
		dialog = initDialog(dialogContent);
		addButton(dialogContent, dialog, ButtonData.OK_DONE);

		// confirmation dialog init
		confirmationDialogContent = initDialogContent();
		confirmationDialogContent.setMaxSize(400, 230);
		confirmationDialog = initDialog(confirmationDialogContent);

		// activityRecap dialog init
		activityRecapDialogContent = new ActivityRecapDialog();
		activityRecapDialogContent.setId("endOfActivityRecap");
		activityRecapDialogContent.setMaxSize(500, 500);
		activityRecapDialog = initDialog(activityRecapDialogContent);
		addButton(activityRecapDialogContent, activityRecapDialog, ButtonData.OK_DONE);

		// notifications init
		MFXNotificationSystem.instance().initOwner(stage);
//		initNotification();

		subscribeToStyleChanges();
		setStylesheets();
	}


	private void subscribeToStyleChanges()
	{
		StyleHandler.getInstance().getObservableStyles().addListener(
				(ListChangeListener<String>)change -> setStylesheets());
	}

	private void setStylesheets()
	{
		ObservableList<String> loadedStyles = StyleHandler.getInstance().getObservableStyles();
		dialogContent.getStylesheets().setAll(loadedStyles);
		confirmationDialogContent.getStylesheets().setAll(loadedStyles);
		activityRecapDialogContent.getStylesheets().setAll(loadedStyles);
//		notification.getStylesheets().setAll(loadedStyles);
	}

	private MFXGenericDialog initDialogContent()
	{
		return MFXGenericDialogBuilder.build().addStyleClasses("dialog").makeScrollable(true).get();
	}

	private MFXStageDialog initDialog(MFXGenericDialog dialogContent)
	{
		return MFXGenericDialogBuilder.build(dialogContent).toStageDialogBuilder().initOwner(this.stage).initModality(
				Modality.APPLICATION_MODAL).setDraggable(true).setOwnerNode(this.root).setScrimPriority(
				ScrimPriority.WINDOW).setOverlayClose(true).setScrimOwner(true).get();
	}


//	private void initNotification()
//	{
//		notification = new DefaultNotification();
//		notification.setHeaderText("unset header");
//		notification.setContentText("unset content");
//	}


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
		String buttonTextKey = switch (buttonType)
				{
					case CANCEL_CLOSE -> "feedback.button.cancel";
					case OK_DONE -> "feedback.button.ok";
					case YES -> "feedback.button.yes";
					case NO -> "feedback.button.no";
					case APPLY -> "feedback.button.apply";
					case FINISH -> "feedback.button.finish";
					case NEXT_FORWARD -> "feedback.button.next";
					case BACK_PREVIOUS -> "feedback.button.back";
					case HELP -> "feedback.button.help";
					case LEFT -> "feedback.button.left";
					case RIGHT -> "feedback.button.right";
					case OTHER -> "feedback.button.other";
					default -> throw new IllegalArgumentException("Unsupported buttonType" + buttonType);
				};

		return Localization.get(buttonTextKey);
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
		AudioHandler.getInstance().playNotificationAudioClip();
		MFXFontIcon warnIcon = new MFXFontIcon("mfx-do-not-enter-circle", 18);
		dialogContent.setHeaderIcon(warnIcon);
		dialogContent.setHeaderText(header);
		dialogContent.setContentText(message);
		convertDialogTo("mfx-warn-dialog");
		dialog.showDialog();
	}


	public void showError(String header, String message)
	{
		AudioHandler.getInstance().playErrorAudioClip();
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
		AudioHandler.getInstance().playNotificationAudioClip();
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



	public void showNotification(String header, String message)
	{
		showNotification(header, message, NotificationPos.TOP_LEFT);
	}

	public void showNotification(String header, String message, NotificationPos position)
	{
		DefaultNotification notification = new DefaultNotification();
		notification.getStylesheets().setAll(StyleHandler.getInstance().getObservableStyles());
		notification.setHeaderText(header);
		notification.setContentText(message);
		MFXNotificationSystem.instance().setPosition(position).publish(notification);
	}

	public void showActivityRecap(AbstractActivity activity)
	{
		if (activity.wasSuccessful()) AudioHandler.getInstance().playSuccessfulActivityAudioClip();
		else AudioHandler.getInstance().playFailedActivityAudioClip();

		if (activity == null || !activity.hasEnded()) return;

		String header;
		activityRecapDialogContent.clearTextContent();
		TextFlow textContent = activityRecapDialogContent.getTextContent();

		baseActivityRecap(textContent, activity);

		if (activity instanceof ChronometerActivity chronometerActivity)
		{
			header = Localization.get("feedback.EoAR.header.chronometer");
			chronometerActivityRecap(textContent, chronometerActivity);
		}
		else if (activity instanceof TimerActivity timerActivity)
		{
			header = Localization.get("feedback.EoAR.header.timer");
			timerActivityRecap(textContent, timerActivity);
		}
		else throw new IllegalArgumentException();

		tagActivityRecap(textContent);
		treeProgressActivityRecap(textContent);

		activityRecapDialogContent.setHeaderText(header);

		activityRecapDialog.showDialog();
	}


	public void showSimpleActivityRecap(AbstractActivity activity)
	{
		if (activity == null || !activity.hasEnded()) return;

		AudioHandler.getInstance().playSelectedTreeAudioClip();

		String header;
		activityRecapDialogContent.clearTextContent();
		TextFlow textContent = activityRecapDialogContent.getTextContent();

		addText(textContent, Localization.get("feedback.EoAR.startDate"));
		addBoldText(textContent, activity.getStartTime().toLocalDate().toString() + "\n");
		baseActivityRecap(textContent, activity);

		if (activity instanceof ChronometerActivity chronometerActivity)
		{
			header = Localization.get("feedback.EoAR.header.chronometer");
			chronometerActivityRecap(textContent, chronometerActivity);
		}
		else if (activity instanceof TimerActivity timerActivity)
		{
			header = Localization.get("feedback.EoAR.header.timer");
			timerActivityRecap(textContent, timerActivity);
		}
		else throw new IllegalArgumentException();

		addText(textContent, Localization.get("feedback.EoAR.tag.selected"));
		Tag tag = activity.getTag();
		if (tag != null)
		{
			addText(textContent, tag.getName() + " ", FontWeight.BOLD);
			textContent.getChildren().add(new Circle(6, tag.getColor()));
		}
		else
		{
			addText(textContent, Localization.get("feedback.EoAR.tag.wasRemoved"), FontWeight.BOLD);
		}


		activityRecapDialogContent.setHeaderText(header);

		activityRecapDialog.showDialog();
	}


	private void baseActivityRecap(TextFlow textContent, AbstractActivity activity)
	{
		addText(textContent, Localization.get("feedback.EoAR.base.start"));
		addBoldText(textContent, activity.getStartTime().toLocalTime().truncatedTo(ChronoUnit.SECONDS).toString());
		addText(textContent, Localization.get("feedback.EoAR.base.end"));
		addBoldText(textContent, activity.getEndTime().toLocalTime().truncatedTo(ChronoUnit.SECONDS).toString());
		addText(textContent, Localization.get("feedback.EoAR.base.duration"));
		addBoldText(textContent, TimeUtils.formatTime(activity.getFinalDuration()));
	}

	private void chronometerActivityRecap(TextFlow textContent, ChronometerActivity chronometerActivity)
	{
	}

	private void timerActivityRecap(TextFlow textContent, TimerActivity timerActivity)
	{

		addText(textContent, Localization.get("feedback.EoAR.timer.chosenDuration"));
		addBoldText(textContent, TimeUtils.formatTime(timerActivity.getChosenDuration()));
		addText(textContent, Localization.get("feedback.EoAR.timer.progress"));
		textContent.getChildren().add(new MFXProgressBar(timerActivity.getProgress()));
		addText(textContent, " " + (int)(timerActivity.getProgress() * 100) + "%");
	}

	private void tagActivityRecap(TextFlow textContent)
	{
		addText(textContent, Localization.get("feedback.EoAR.tag.header"), FontWeight.BLACK);
		Tag tag = ActivityHandler.getInstance().getCurrentActivity().getTag();
		addText(textContent, Localization.get("feedback.EoAR.tag.timeSpentTag") + tag.getName() + " ");
		textContent.getChildren().add(new Circle(6, tag.getColor()));

		ActivityStatsHandler.ActivityTime tagActivityTime = ActivityStatsHandler.getInstance().getTagActivityTime(tag);

		addText(textContent, Localization.get("feedback.EoAR.tag.day"));
		addBoldText(textContent, TimeUtils.formatTime(tagActivityTime.dayTime));
		addText(textContent, Localization.get("feedback.EoAR.tag.week"));
		addBoldText(textContent, TimeUtils.formatTime(tagActivityTime.weekTime));
		addText(textContent, Localization.get("feedback.EoAR.tag.month"));
		addBoldText(textContent, TimeUtils.formatTime(tagActivityTime.monthTime));
		addText(textContent, Localization.get("feedback.EoAR.tag.year"));
		addBoldText(textContent, TimeUtils.formatTime(tagActivityTime.yearTime));


	}

	private void treeProgressActivityRecap(TextFlow textContent)
	{
		addText(textContent, Localization.get("feedback.EoAR.tree.header"), FontWeight.BLACK);
		Tree selectedTreeToUnlock = TreeHandler.getInstance().getSelectedTreeToUnlock();
		addText(textContent, Localization.get("feedback.EoAR.tree.selectedTree"));
		if (selectedTreeToUnlock != null)
		{
			addText(textContent, "'" + selectedTreeToUnlock.getName() + "'", FontWeight.MEDIUM);
			addText(textContent, Localization.get("feedback.EoAR.tree.totalTimeSpent",
					TimeUtils.formatTime(selectedTreeToUnlock.getProgressTime()),
					TimeUtils.formatTime(selectedTreeToUnlock.getTotalRequiredTime())));
			addText(textContent, Localization.get("feedback.EoAR.tree.progress"));
			textContent.getChildren().add(new MFXProgressBar(selectedTreeToUnlock.getUnlockProgress()));
			addText(textContent, " " + (int)(selectedTreeToUnlock.getUnlockProgress() * 100) + "%");
			addText(textContent, Localization.get("feedback.EoAR.tree.remainingTime"));
			addBoldText(textContent, TimeUtils.formatTime(selectedTreeToUnlock.getRemainingRequiredTime()));
		}
		else
		{
			addText(textContent, Localization.get("feedback.EoAR.tree.none"), FontWeight.MEDIUM);
		}
		addText(textContent, Localization.get("feedback.EoAR.tree.unusedTime"));
		addBoldText(textContent, TimeUtils.formatTime(TreeHandler.getInstance().getUnusedProgressTime()));
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
