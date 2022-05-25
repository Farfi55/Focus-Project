package mafiadelprimobanco.focusproject.controller;

import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.models.spinner.IntegerSpinnerModel;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.shape.Circle;
import mafiadelprimobanco.focusproject.*;
import mafiadelprimobanco.focusproject.model.ActivityObserver;
import mafiadelprimobanco.focusproject.model.ActivityType;
import mafiadelprimobanco.focusproject.model.utils.FXMLReferences;
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
	@FXML private MFXProgressSpinner progressBarTime;

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

		activitySelectorComboBox.getItems().addAll("Cronometro", "Timer", "Timer Pomodoro");
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
			hourSpinnerModel.setValue(hourSpinnerModel.getConverter().fromString(e));
			secondsSpinnerSelector.requestFocus();
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
	public void onActivityStart()
	{
		activityButton.setText("Interrompi");

		activityTimeTextField.setPrefWidth(Region.USE_COMPUTED_SIZE);

		if (ActivityHandler.getInstance().getCurrActivityType() != ActivityType.CHRONOMETER)
		{
			ActivityHandler.getInstance().setExecutionTime(
					hoursSpinnerSelector.getValue() * 60 * 60 + minutesSpinnerSelector.getValue() * 60
							+ secondsSpinnerSelector.getValue());
			hideSpinners();
		}

//		homeRoot.setRight(null);

		hideNode(homeRoot.getRight());
		hideNode(activitySelectorComboBox);

		selectedTagText.setText(TagHandler.getInstance().getSelectedTag().getName());
		selectedTagColorCircle.setFill(TagHandler.getInstance().getSelectedTag().getColor());
		showNode(selectedTagText);
		showNode(selectedTagColorCircle);


		switch (ActivityHandler.getInstance().getCurrActivityType())
		{
			case CHRONOMETER -> progressBarTime.setProgress(0.0);
			case TIMER -> progressBarTime.setProgress(1.0);
		}
	}

	@Override
	public void onActivityUpdateSafe()
	{
		switch (ActivityHandler.getInstance().getCurrActivityType())
		{
			case CHRONOMETER -> onChronometerUpdateTick();
			case TIMER -> onTimerUpdateTick();
		}
	}

	@Override
	public void onActivityEndSafe() { onActivityStop(); }

	@Override
	public void handle(KeyEvent event)
	{
		if (event.isControlDown())
		{
			if (event.getCode().equals(KeyCode.ENTER) && !ActivityHandler.getInstance().isActivityStarted())
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
		activityTimeTextField.setText("00:00");
		progressBarTime.setProgress(0.0);

		hideNode(selectedTagText);
		hideNode(selectedTagColorCircle);

		activityTimeTextField.setPrefWidth(0);

		if (ActivityHandler.getInstance().getCurrActivityType() != ActivityType.CHRONOMETER) showSpinners();

		showNode(homeRoot.getRight());
		showNode(activitySelectorComboBox);
	}


	public void onTimerUpdateTick()
	{
		progressBarTime.setProgress(
				progressBarTime.getProgress() - ActivityHandler.getInstance().getCurrentProgressBarTick());
		activityTimeTextField.setText(ActivityHandler.getInstance().getCurrentTimeTick());
	}

	public void onChronometerUpdateTick()
	{
		activityTimeTextField.setText(ActivityHandler.getInstance().getCurrentTimeTick());
	}

	@FXML
	void toggleActivityState()
	{
		if (ActivityHandler.getInstance().isActivityStarted())
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
		switch (ActivityHandler.getInstance().getCurrActivityType())
		{
			case TIMER, TOMATO_TIMER -> {
				if (Feedback.getInstance().askYesNoConfirmation("Interrompere attività",
						"Sei sicuro di voler interrompere l'attività?"))
					ActivityHandler.getInstance().stopCurrActivity();

			}
			case CHRONOMETER -> ActivityHandler.getInstance().stopCurrActivity();
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

		if (activityTypeSelected == ActivityType.CHRONOMETER) hideSpinners();
		else showSpinners();

		System.out.println("Activity type set to: " + ActivityHandler.getInstance().getCurrActivityType());
	}


}

