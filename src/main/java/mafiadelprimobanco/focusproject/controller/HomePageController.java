package mafiadelprimobanco.focusproject.controller;

import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.models.spinner.IntegerSpinnerModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;
import mafiadelprimobanco.focusproject.*;
import mafiadelprimobanco.focusproject.model.ActivityObserver;
import mafiadelprimobanco.focusproject.model.ActivityType;
import mafiadelprimobanco.focusproject.model.utils.FXMLReferences;
import mafiadelprimobanco.focusproject.model.utils.TimeUtils;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;

public class HomePageController implements ActivityObserver, EventHandler<KeyEvent>
{
	//
	@FXML private BorderPane homeRoot;

	// fullscreen button controls
	@FXML private MFXButton fullScreenButton;
	@FXML private FontIcon fullScreenIcon;


	// selected tag controls
	@FXML private Circle selectedTagColorCircle;
	@FXML private Label selectedTagText;

	@FXML private MFXComboBox<String> activitySelectorComboBox;

	@FXML private MFXTextField activityTimeTextField;
	@FXML private MFXProgressSpinner activityProgressSpinner;

	@FXML private MFXSpinner<Integer> hoursSpinnerSelector;
	@FXML private MFXSpinner<Integer> minutesSpinnerSelector;
	@FXML private MFXSpinner<Integer> secondsSpinnerSelector;


	@FXML private ImageView treeImageViewer;

	@FXML private MFXButton activityButton;

	@FXML
	void initialize()
	{
		ActivityHandler.getInstance().addListener(this);
		KeyPressManager.getInstance().addHandler(this);

		activitySelectorComboBox.getItems().addAll("Cronometro", "Timer");
//		activitySelectorComboBox.getItems().addAll("Cronometro", "Timer", "Timer Pomodoro");
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

		hoursSpinnerSelector.setOnCommit(e -> {
			hourSpinnerModel.setValue(filterInput(e));
			minutesSpinnerSelector.requestFocus();
		});
		minutesSpinnerSelector.setOnCommit(e -> {
			minuteSpinnerModel.setValue(Math.min(minuteSpinnerModel.getConverter().fromString(e), 59));
			secondsSpinnerSelector.requestFocus();
		});
		secondsSpinnerSelector.setOnCommit(e -> {
			secondSpinnerModel.setValue(Math.min(secondSpinnerModel.getConverter().fromString(e), 59));
			hoursSpinnerSelector.requestFocus();
		});


		// makes sure everything is looking normal at the beginning
		onActivityStop();
	}

	@Override
	public void onActivityStarting()
	{
		activityButton.setText("Interrompi");

		showNode(activityTimeTextField);

		if (ActivityHandler.getInstance().getCurrentActivityType() == ActivityType.TIMER)
		{
			ActivityHandler.getInstance().setChosenTimerDuration(getInputTimerDuration());
			hideSpinners();
		}

		hideNode(homeRoot.getRight());
		hideNode(activitySelectorComboBox);

		selectedTagText.setText(TagHandler.getInstance().getSelectedTag().getName());
		selectedTagColorCircle.setFill(TagHandler.getInstance().getSelectedTag().getColor());
		showNode(selectedTagText);
		showNode(selectedTagColorCircle);


		switch (ActivityHandler.getInstance().getCurrentActivityType())
		{
			case CHRONOMETER -> activityProgressSpinner.setProgress(-1);
			case TIMER -> activityProgressSpinner.setProgress(1.0);
		}
	}

	private int getInputTimerDuration()
	{
		int secondsFromHoursSelector = hoursSpinnerSelector.getValue() * 60 * 60;
		int secondsFromMinutesSelector = minutesSpinnerSelector.getValue() * 60;
		int secondsFromSecondsSelector = secondsSpinnerSelector.getValue();

		return secondsFromHoursSelector + secondsFromMinutesSelector + secondsFromSecondsSelector;
	}

	@Override
	public void onActivityUpdateSafe()
	{
		switch (ActivityHandler.getInstance().getCurrentActivityType())
		{
			case CHRONOMETER -> onChronometerUpdate();
			case TIMER -> onTimerUpdate();
		}
	}

	@Override
	public void onActivityEndSafe() { onActivityStop(); }

	@Override
	public void handle(KeyEvent event)
	{
		if (event.isControlDown())
		{
			if (event.getCode().equals(KeyCode.ENTER) && !ActivityHandler.getInstance().isActivityRunning())
				startActivity();
		}
	}

	private void loadTagsView()
	{
		try
		{
			homeRoot.setRight(SceneHandler.getInstance().loadFXML(FXMLReferences.HOME_TAGS));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void onActivityStop()
	{
		activityButton.setText("Avvia");

		hideNode(activityTimeTextField);
		hideNode(selectedTagText);
		hideNode(selectedTagColorCircle);

		showNode(homeRoot.getRight());
		showNode(activitySelectorComboBox);

		activityTimeTextField.setText("00:00:00");
		activityProgressSpinner.setProgress(0.0);

		if (ActivityHandler.getInstance().getCurrentActivityType() != ActivityType.CHRONOMETER) showSpinners();

	}


	public void onTimerUpdate()
	{
		activityProgressSpinner.setProgress(1.0 - ActivityHandler.getInstance().getTimerActivityProgress());

		int remainingSeconds = ActivityHandler.getInstance().getRemainingTimerDuration();
		activityTimeTextField.setText(TimeUtils.formatTime(remainingSeconds));
	}



	public void onChronometerUpdate() {
		int secondsElapsed = ActivityHandler.getInstance().getCurrentActivity().getSecondsSinceStart();
		activityTimeTextField.setText(TimeUtils.formatTime(secondsElapsed));
	}

	@FXML
	void toggleActivityState()
	{
		if (ActivityHandler.getInstance().isActivityRunning())
		{
			stopActivity();
		}
		else
		{
			startActivity();
		}
	}

	private void stopActivity()
	{
		switch (ActivityHandler.getInstance().getCurrentActivityType())
		{
			case TIMER, TOMATO_TIMER -> {
				if (Feedback.getInstance().askYesNoConfirmation("Interrompere attività",
						"Sei sicuro di voler interrompere l'attività?"))
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

	@FXML
	void updateHourValue(KeyEvent event) {
		//System.out.println(secondsSpinnerSelector);

		//secondSpinnerModel.setValue(
	}

	@FXML
	void updateMinuteValue(KeyEvent event) {

		//System.out.println(secondsSpinnerSelector.getValue().intValue());
		//secondSpinnerModel.setValue(
	}

	@FXML
	void updateSecondValue(KeyEvent event) {
		//secondSpinnerModel.setValue(
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


}

