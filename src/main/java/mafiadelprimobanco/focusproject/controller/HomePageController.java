package mafiadelprimobanco.focusproject.controller;

import io.github.palexdev.materialfx.beans.Alignment;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.models.spinner.IntegerSpinnerModel;
import io.github.palexdev.materialfx.utils.NumberUtils;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;
import javafx.util.StringConverter;
import mafiadelprimobanco.focusproject.Localization;
import mafiadelprimobanco.focusproject.handler.*;
import mafiadelprimobanco.focusproject.model.ActivityObserver;
import mafiadelprimobanco.focusproject.model.ActivityType;
import mafiadelprimobanco.focusproject.model.TreeChooserPopup;
import mafiadelprimobanco.focusproject.model.activity.AbstractActivity;
import mafiadelprimobanco.focusproject.model.activity.ChronometerActivity;
import mafiadelprimobanco.focusproject.model.activity.TimerActivity;
import mafiadelprimobanco.focusproject.utils.FXMLReferences;
import mafiadelprimobanco.focusproject.utils.ResourcesLoader;
import mafiadelprimobanco.focusproject.utils.TimeUtils;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.net.URL;
import java.util.List;
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

	@FXML private MFXComboBox<ActivityType> activitySelectorComboBox;

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

		initializeActivitySelectorComboBox();
		initializeSpinnersSelectors();
		initializeTreeView();

		loadTagsView();

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
		if (event.getCode().equals(KeyCode.ESCAPE) && ActivityHandler.getInstance().isActivityRunning())
		{
			stopActivityDelegate();
			event.consume();
		}
		else if (event.isControlDown())
		{
			if (event.getCode().equals(KeyCode.ENTER))
			{
				toggleActivityState();
				event.consume();
			}
			else if (hoursSpinnerSelector.isVisible())
			{
				if (event.getCode().equals(KeyCode.DIGIT1))
				{
					focusSpinnerTextField(hoursSpinnerSelector);
					event.consume();
				}
				else if (event.getCode().equals(KeyCode.DIGIT2))
				{
					focusSpinnerTextField(minutesSpinnerSelector);
					event.consume();
				}
				else if (event.getCode().equals(KeyCode.DIGIT3))
				{
					focusSpinnerTextField(secondsSpinnerSelector);
					event.consume();
				}
			}
		}
		else if (event.isAltDown() && !ActivityHandler.getInstance().isActivityRunning())
		{
			if (event.getCode().equals(KeyCode.C))
			{
				ActivityHandler.getInstance().setCurrentActivityType(ActivityType.CHRONOMETER);
				event.consume();
			}
			else if (event.getCode().equals(KeyCode.T))
			{
				ActivityHandler.getInstance().setCurrentActivityType(ActivityType.TIMER);
				event.consume();
			}
		}

	}

	private void subscribeListeners()
	{
		KeyPressManager.getInstance().addHandler(this);
		ActivityHandler.getInstance().addListener(this);
		ActivityHandler.getInstance().currentActivityTypeProperty().addListener(onActivityTypeChanged());
		TreeHandler.getInstance().selectedActivityTreeProperty().addListener(observable -> updateTreeImageView());
	}

	private void unsubscribeListeners()
	{
		KeyPressManager.getInstance().removeHandler(this);
		ActivityHandler.getInstance().removeListener(this);
		ActivityHandler.getInstance().currentActivityTypeProperty().removeListener(onActivityTypeChanged());
		TreeHandler.getInstance().selectedActivityTreeProperty().removeListener(observable -> updateTreeImageView());
	}

	private void initializeTreeView()
	{
		updateTreeImageView();
		treeChooserPopup = new TreeChooserPopup();
		treeButton.setOnAction(event ->
		{
			if (!ActivityHandler.getInstance().isActivityRunning()) treeChooserPopup.show(treeButton,
					Alignment.of(HPos.RIGHT, VPos.CENTER), 200, 0);
		});
	}

	private void initializeSpinnersSelectors()
	{
		SimpleBooleanProperty focusNextSpinnerOnCommit = new SimpleBooleanProperty(true);

		List<MFXSpinner<Integer>> spinners = List.of(hoursSpinnerSelector, minutesSpinnerSelector,
				secondsSpinnerSelector);

		for (int i = 0; i < spinners.size(); i++)
		{
			MFXSpinner<Integer> spinner = spinners.get(i);

			var model = new IntegerSpinnerModel(0);

			if (spinner != hoursSpinnerSelector)
			{
				model.setMax(59);
				model.setWrapAround(true);
			}
			spinner.setSpinnerModel(model);

			MFXSpinner<Integer> nextSpinner = spinners.get((i + 1) % spinners.size());
			spinner.setOnCommit(e ->
			{
				model.setValue(filterInput(model, e));
				if (focusNextSpinnerOnCommit.get()) focusSpinnerTextField(nextSpinner);
			});
			spinner.setOnKeyPressed(event ->
			{
				if (event.getCode().equals(KeyCode.UP))
				{
					model.next();
					event.consume();
				}
				else if (event.getCode().equals(KeyCode.DOWN))
				{
					model.previous();
					event.consume();
				}
			});
		}
	}

	private void initializeActivitySelectorComboBox()
	{
		activitySelectorComboBox.setConverter(new StringConverter<>()
		{
			@Override
			public String toString(ActivityType object)
			{
				if (object != null) return Localization.get(object.key);
				else return "";
			}

			@Override
			public ActivityType fromString(String string)
			{
				if (string == null || string.isEmpty()) return null;
				for (ActivityType activityType : ActivityType.values())
					if (Localization.get(activityType.key).equals(string)) return activityType;
				return null;
			}
		});

		Localization.localeProperty().addListener(observable -> updateActivityComboBoxStrings());

		activitySelectorComboBox.floatingTextProperty().bind(Localization.createStringBinding("activity.type"));

		updateActivityComboBoxStrings();
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


	@FXML
	void toggleActivityState()
	{
		if (!ActivityHandler.getInstance().isActivityRunning())
		{
			if (canStartActivity()) startActivityDelegate();
		}
		else
		{
			stopActivityDelegate();
		}
	}

	private void startActivityDelegate()
	{
		ActivityHandler.getInstance().startActivity();
	}

	private void stopActivityDelegate()
	{
		switch (ActivityHandler.getInstance().getCurrentActivityType())
		{
			case TIMER, POMODORO ->
			{
				if (!SettingsHandler.getInstance().getSettings().confirmInterruptTimerActivity.get()
						|| Feedback.getInstance().askYesNoConfirmation(
						Localization.get("warning.activity.stopActivity.header"),
						Localization.get("warning.activity.stopActivity.message")))
					ActivityHandler.getInstance().stopCurrentActivity();
			}
			case CHRONOMETER ->
			{
				if (!SettingsHandler.getInstance().getSettings().confirmInterruptChronometerActivity.get()
						|| Feedback.getInstance().askYesNoConfirmation(
						Localization.get("warning.activity.stopActivity.header"),
						Localization.get("warning.activity.stopActivity.message")))
					ActivityHandler.getInstance().stopCurrentActivity();
			}
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
		int minTimerDuration = SettingsHandler.getInstance().getSettings().minimumTimerDuration.get();
		if (getInputTimerDuration() < minTimerDuration)
		{
			Feedback.getInstance().showNotification(Localization.get("error.time.invalidTime.header"),
					Localization.get("error.time.invalidTime.message", TimeUtils.formatTime(minTimerDuration)));
			return false;
		}

		return true;
	}

	private ChangeListener<ActivityType> onActivityTypeChanged()
	{
		return (observable, oldValue, newValue) ->
		{
			updateActivityComboBoxSelectedItem();
			System.out.println("Activity type changed from " + oldValue + " to " + newValue);

			if (newValue == ActivityType.TIMER)
			{
				showSpinners();
				setSpinnersToMinimumTimerDuration();
			}
			else hideSpinners();
		};
	}

	private void setSpinnersToMinimumTimerDuration()
	{
		int totalSeconds = SettingsHandler.getInstance().getSettings().minimumTimerDuration.get();
		int hours = totalSeconds / 3600;
		int minutes = (totalSeconds % 3600) / 60;
		int seconds = totalSeconds % 60;

		hoursSpinnerSelector.setValue(hours);
		minutesSpinnerSelector.setValue(minutes);
		secondsSpinnerSelector.setValue(seconds);
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


	// -------------------------------------------------
	// UTILITY METHODS
	// -------------------------------------------------

	private BoundTextField getSpinnerTextField(MFXSpinner<Integer> spinnerSelector)
	{
		try
		{
			return (BoundTextField)((Parent)(((Parent)spinnerSelector.getChildrenUnmodifiable().get(
					0)).getChildrenUnmodifiable().get(2))).getChildrenUnmodifiable().get(1);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.err.println("SpinnerSelector hierarchy probably changed");
			return null;
		}
	}

	Integer filterInput(IntegerSpinnerModel spinnerModel, String newValue)
	{
		try
		{
			return NumberUtils.clamp(Integer.parseInt(newValue), spinnerModel.getMin(), spinnerModel.getMax());
		}
		catch (NumberFormatException ignored)
		{
			return spinnerModel.getValue();
		}
	}


	// -------------------------------------------------
	// INTERFACE UPDATE METHODS
	// -------------------------------------------------

	private void focusSpinnerTextField(MFXSpinner<Integer> spinnerSelector)
	{
		BoundTextField textField = getSpinnerTextField(spinnerSelector);
		if (textField != null) textField.requestFocus();
	}

	private void updateTreeImageView()
	{
		if (TreeHandler.getInstance().getSelectedActivityTree() == null) setTreeImage((Image)null);
		else setTreeImage(TreeHandler.getInstance().getSelectedActivityTree().getMatureTreeSprite());
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

		updateActivityComboBoxSelectedItem();
		if (ActivityHandler.getInstance().getCurrentActivityType() == ActivityType.TIMER) showSpinners();
		else hideSpinners();
	}


	private void updateActivityComboBoxStrings()
	{
		activitySelectorComboBox.getItems().clear();
		activitySelectorComboBox.getItems().addAll(ActivityType.CHRONOMETER, ActivityType.TIMER);
		updateActivityComboBoxSelectedItem();
	}

	private void updateActivityComboBoxSelectedItem()
	{
		activitySelectorComboBox.selectItem(ActivityHandler.getInstance().getCurrentActivityType());
		activitySelectorComboBox.setText(Localization.get(ActivityHandler.getInstance().getCurrentActivityType().key));
	}



	@FXML
	void toggleFullScreen()
	{
		SceneHandler.getInstance().toggleFullScreen();
	}

	private void showSpinners() { setSpinnersVisible(true); }

	private void hideSpinners() { setSpinnersVisible(false); }

	void showNode(Node node) { setNodeVisible(node, true); }

	void hideNode(Node node) { setNodeVisible(node, false); }


	// -------------------------------------------------
	// GETTER - SETTER METHODS
	// -------------------------------------------------

	private void setNodeVisible(Node node, boolean visible)
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

	@FXML
	private void setActivityType(ActionEvent event)
	{
		ActivityType selectedActivityType = activitySelectorComboBox.getSelectedItem();
		if (selectedActivityType != null) ActivityHandler.getInstance().setCurrentActivityType(selectedActivityType);
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


}

