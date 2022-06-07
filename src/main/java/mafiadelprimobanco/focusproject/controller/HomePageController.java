package mafiadelprimobanco.focusproject.controller;

import io.github.palexdev.materialfx.beans.Alignment;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import io.github.palexdev.materialfx.controls.MFXSpinner;
import io.github.palexdev.materialfx.controls.models.spinner.IntegerSpinnerModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;
import mafiadelprimobanco.focusproject.*;
import mafiadelprimobanco.focusproject.model.ActivityObserver;
import mafiadelprimobanco.focusproject.model.ActivityType;
import mafiadelprimobanco.focusproject.model.TreeChooserPopup;
import mafiadelprimobanco.focusproject.model.activity.AbstractActivity;
import mafiadelprimobanco.focusproject.model.activity.ChronometerActivity;
import mafiadelprimobanco.focusproject.model.activity.TimerActivity;
import mafiadelprimobanco.focusproject.model.utils.FXMLReferences;
import mafiadelprimobanco.focusproject.model.utils.TimeUtils;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomePageController implements Controller, ActivityObserver, EventHandler<KeyEvent>
{
	private Controller tagsController;

	@FXML private BorderPane homeRoot;

	// fullscreen button controls
	@FXML private MFXButton fullScreenButton;
	@FXML private FontIcon fullScreenIcon;

	// selected tag controls
	@FXML private Circle selectedTagColorCircle;
	@FXML private Label selectedTagText;
	@FXML private Node selectedTagRoot;

	@FXML private MFXComboBox<String> activitySelectorComboBox;

	@FXML private Label activityTimeLabel;
	@FXML private MFXProgressSpinner activityProgressSpinner;

	@FXML private MFXSpinner<Integer> hoursSpinnerSelector;
	@FXML private MFXSpinner<Integer> minutesSpinnerSelector;
	@FXML private MFXSpinner<Integer> secondsSpinnerSelector;

	@FXML private MFXButton treeButton;
	@FXML private ImageView treeImageViewer;

	@FXML private MFXButton activityButton;

	private int treePhase = 0;

	private TreeChooserPopup treeChooserPopup;

	@FXML
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		subscribeListeners();

		activitySelectorComboBox.getItems().addAll(Localization.get("activity.chronometer"),
				Localization.get("activity.timer"));
		Localization.setMFXComboboxFloatingText(activitySelectorComboBox, "activity.mode");
		activitySelectorComboBox.selectFirst();

		loadTagsView();


		var hourSpinnerModel = new IntegerSpinnerModel(0);
		var minuteSpinnerModel = new IntegerSpinnerModel(0);
		var secondSpinnerModel = new IntegerSpinnerModel(0);

		secondSpinnerModel.setMax(59);
		minuteSpinnerModel.setMax(59);

		minutesSpinnerSelector.setSpinnerModel(minuteSpinnerModel);
		secondsSpinnerSelector.setSpinnerModel(secondSpinnerModel);
		hoursSpinnerSelector.setSpinnerModel(hourSpinnerModel);

		minuteSpinnerModel.setWrapAround(true);
		secondSpinnerModel.setWrapAround(true);

		hoursSpinnerSelector.setOnCommit(e ->
		{
			hourSpinnerModel.setValue(filterInput(e));
			minutesSpinnerSelector.requestFocus();
		});
		minutesSpinnerSelector.setOnCommit(e ->
		{
			minuteSpinnerModel.setValue(Math.min(filterInput(e), 59));
			secondsSpinnerSelector.requestFocus();
		});
		secondsSpinnerSelector.setOnCommit(e ->
		{
			secondSpinnerModel.setValue(Math.min(filterInput(e), 59));
			hoursSpinnerSelector.requestFocus();
		});

		updateTreeImageView();
		treeChooserPopup = new TreeChooserPopup();
		treeButton.setOnAction(event -> treeChooserPopup.show(treeButton, Alignment.of(HPos.RIGHT, VPos.CENTER),200,0));
		TreeHandler.getInstance().selectedActivityTreeProperty().addListener(observable -> updateTreeImageView());

		resetInterface();
	}

	@Override
	public void terminate()
	{
		tagsController.terminate();
		unsubscribeListeners();
	}

	@Override
	public void onActivityStarting(AbstractActivity currentActivity)
	{
		Localization.setButton(activityButton, "activity.stop");

		showNode(activityTimeLabel);

		if (currentActivity instanceof TimerActivity)
		{
			ActivityHandler.getInstance().setChosenTimerDuration(getInputTimerDuration());
			hideSpinners();
			activityProgressSpinner.setProgress(1.0);
		}
		else if (currentActivity instanceof ChronometerActivity)
		{
			activityProgressSpinner.setProgress(-1);
		}

		hideNode(homeRoot.getRight());
		hideNode(activitySelectorComboBox);

		selectedTagText.setText(currentActivity.getTag().getName());
		selectedTagColorCircle.setFill(currentActivity.getTag().getColor());
		showNode(selectedTagRoot);

		treePhase = 0;
		setTreeImage(TreeHandler.getInstance().getTreePhaseImage(treePhase));


	}

	@Override
	public void onActivityUpdateSafe(AbstractActivity currentActivity)
	{
		if (currentActivity instanceof ChronometerActivity chronometerActivity) onChronometerUpdate(
				chronometerActivity);
		else if (currentActivity instanceof TimerActivity timerActivity) onTimerUpdate(timerActivity);
	}

	@Override
	public void onActivityEndSafe(AbstractActivity currentActivity)
	{
		resetInterface();

		if (currentActivity instanceof TimerActivity timerActivity)
		{
			if (timerActivity.wasInterrupted()) setTreeImage(currentActivity.getTree().getDeadTreeSprite());
			else setTreeImage(currentActivity.getTree().getMatureTreeSprite());
		}
		else if (currentActivity instanceof ChronometerActivity)
		{
			if (treePhase < 5) setTreeImage(currentActivity.getTree().getDeadTreeSprite());
			else setTreeImage(currentActivity.getTree().getMatureTreeSprite());
		}

		Feedback.getInstance().showActivityRecap(currentActivity);
	}

	@Override
	public void handle(KeyEvent event)
	{
		if (event.isControlDown())
		{
			if (event.getCode().equals(KeyCode.ENTER) && !ActivityHandler.getInstance().isActivityRunning())
				startActivity();
		}
	}

	private void updateTreeImageView()
	{
		if (TreeHandler.getInstance().getSelectedActivityTree() == null) setTreeImage((Image)null);
		else setTreeImage(TreeHandler.getInstance().getSelectedActivityTree().getMatureTreeSprite());
	}

	private void subscribeListeners()
	{
		ActivityHandler.getInstance().addListener(this);
		KeyPressManager.getInstance().addHandler(this);
	}

	private void unsubscribeListeners()
	{
		ActivityHandler.getInstance().removeListener(this);
		KeyPressManager.getInstance().removeHandler(this);
	}

	@FXML
	void toggleActivityState()
	{
		if (!ActivityHandler.getInstance().isActivityRunning())
		{
			if (canStartActivity()) startActivity();
		}
		else
		{
			stopActivity();
		}
	}

	private void loadTagsView()
	{
		try
		{
			FXMLLoader fxmlLoader = SceneHandler.getInstance().getFXMLLoader(FXMLReferences.HOME_TAGS);
			homeRoot.setRight(fxmlLoader.load());

			tagsController = fxmlLoader.getController();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private void resetInterface()
	{
		Localization.setButton(activityButton, "activity.start");

		hideNode(activityTimeLabel);

		hideNode(selectedTagRoot);

		showNode(homeRoot.getRight());
		showNode(activitySelectorComboBox);

		activityTimeLabel.setText(TimeUtils.formatTime(0));
		activityProgressSpinner.setProgress(0.0);


		if (ActivityHandler.getInstance().getCurrentActivityType() == ActivityType.TIMER) showSpinners();
		else hideSpinners();
	}

	public void onTimerUpdate(TimerActivity timerActivity)
	{
		double progress = timerActivity.getProgress();
		activityProgressSpinner.setProgress(1.0 - progress);

		int remainingSeconds = timerActivity.getRemainingDuration();
		activityTimeLabel.setText(TimeUtils.formatTime(remainingSeconds));


		if (treePhase < 5 && progress >= (treePhase + 1) / 5f)
		{
			treePhase++;
			if (treePhase < 5) setTreeImage(TreeHandler.getInstance().getTreePhaseImage(treePhase));
			else setTreeImage(timerActivity.getTree().getMatureTreeSprite());
		}

	}

	public void onChronometerUpdate(ChronometerActivity chronometerActivity)
	{
		int secondsElapsed = chronometerActivity.getSecondsSinceStart();
		activityTimeLabel.setText(TimeUtils.formatTime(secondsElapsed));

		// todo: move this into settings
		int minSuccessChronometerDuration = 20; // 10 seconds

		if (treePhase < 5 && secondsElapsed >= minSuccessChronometerDuration / 5f * (treePhase + 1))
		{
			treePhase++;
			if (treePhase < 5) setTreeImage(TreeHandler.getInstance().getTreePhaseImage(treePhase));
			else setTreeImage(chronometerActivity.getTree().getMatureTreeSprite());
		}

	}

	private boolean canStartActivity()
	{
		if (TreeHandler.getInstance().getSelectedActivityTree() == null)
		{
			Feedback.getInstance().showNotification(Localization.get("error.tree.noTreeSelected.header"),
					Localization.get("error.tree.noTreeSelected.message"));
			return false;
		}

		return switch (ActivityHandler.getInstance().getCurrentActivityType())
				{
					case CHRONOMETER -> true;
					case TIMER -> canStartTimerActivity();
					default -> throw new IllegalStateException(
							"Unexpected value: " + ActivityHandler.getInstance().getCurrentActivityType());
				};
	}

	private boolean canStartTimerActivity()
	{
		// todo: move this into settings
		int minTimerDuration = 10; // 10 secondi
		if (getInputTimerDuration() < minTimerDuration)
		{
			Feedback.getInstance().showNotification(Localization.get("error.time.invalidTime.header"),
					Localization.get("error.time.invalidTime.message", TimeUtils.formatTime(minTimerDuration)));
			return false;
		}

		return true;
	}

	private void stopActivity()
	{
		switch (ActivityHandler.getInstance().getCurrentActivityType())
		{
			case TIMER, TOMATO_TIMER -> {
				if (Feedback.getInstance().askYesNoConfirmation(
						Localization.get("warning.activity.stopActivity.header"),
						Localization.get("warning.activity.stopActivity.message")))
					ActivityHandler.getInstance().stopCurrentActivity();

			}
			case CHRONOMETER -> ActivityHandler.getInstance().stopCurrentActivity();
		}
	}

	private void startActivity()
	{
		ActivityHandler.getInstance().startActivity();
	}

	@FXML
	void toggleFullScreen()
	{
		SceneHandler.getInstance().toggleFullScreen();
	}

	int filterInput(String num)
	{
		int currVal = 0;

		//System.out.println(secondsSpinnerSelector.getValue().intValue());
		//secondSpinnerModel.setValue(
		try
		{
			return Integer.parseInt(num);
		}
		catch (Exception e)
		{
		}

		return 0;
	}

	private void showSpinners() { setSpinnersVisible(true); }

	private void hideSpinners() { setSpinnersVisible(false); }

	void showNode(Node node) { setNodeVisible(node, true); }

	void hideNode(Node node) { setNodeVisible(node, false); }

	void setNodeVisible(Node node, boolean visible)
	{
		node.setVisible(visible);
		node.setManaged(visible);
	}

	private int getInputTimerDuration()
	{
		int secondsFromHoursSelector = hoursSpinnerSelector.getValue() * 60 * 60;
		int secondsFromMinutesSelector = minutesSpinnerSelector.getValue() * 60;
		int secondsFromSecondsSelector = secondsSpinnerSelector.getValue();

		return secondsFromHoursSelector + secondsFromMinutesSelector + secondsFromSecondsSelector;
	}

	private void setTreeImage(Image image)
	{
		treeImageViewer.setImage(image);
	}

	private void setTreeImage(String path)
	{
		setTreeImage(ResourcesLoader.loadImage(path));
	}

	private void setSpinnersVisible(boolean value)
	{
		setNodeVisible(hoursSpinnerSelector, value);
		setNodeVisible(minutesSpinnerSelector, value);
		setNodeVisible(secondsSpinnerSelector, value);
	}

	@FXML
	void setActivityType(ActionEvent event)
	{
		// todo refactor: this isn't safe, if you change order you dont get

		final int activityIndex = activitySelectorComboBox.getSelectedIndex();

		var activityTypeSelected = ActivityType.values()[activityIndex];

		ActivityHandler.getInstance().setActivityType(activityTypeSelected);

		if (activityTypeSelected == ActivityType.TIMER) showSpinners();
		else hideSpinners();

		System.out.println("Activity type set to: " + ActivityHandler.getInstance().getCurrentActivityType());
	}


//	public void setChosenActivityTree(Tree chosenActivityTree)
//	{
//		this.chosenActivityTree = chosenActivityTree;
//		if (chosenActivityTree != null) treeImageViewer.setImage(
//				ResourcesLoader.loadImage(chosenActivityTree.getMatureTreeSprite()));
//		else treeImageViewer.setImage(null);
//	}
}

