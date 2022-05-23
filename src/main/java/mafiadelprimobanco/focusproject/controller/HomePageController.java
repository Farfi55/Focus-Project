package mafiadelprimobanco.focusproject.controller;

import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.models.spinner.IntegerSpinnerModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.shape.Circle;
import javafx.util.converter.IntegerStringConverter;
import mafiadelprimobanco.focusproject.ActivityHandler;
import mafiadelprimobanco.focusproject.Feedback;
import mafiadelprimobanco.focusproject.SceneHandler;
import mafiadelprimobanco.focusproject.TagHandler;
import mafiadelprimobanco.focusproject.model.ActivityObserver;
import mafiadelprimobanco.focusproject.model.ActivityType;
import mafiadelprimobanco.focusproject.model.utils.FXMLReferences;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.util.function.BiFunction;

public class HomePageController implements ActivityObserver
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

		activitySelectorComboBox.getItems().addAll("Cronometro", "Timer", "Timer Pomodoro");
		activitySelectorComboBox.selectFirst();

		try
		{
			homeRoot.setRight(SceneHandler.getInstance().loadFXML(FXMLReferences.HOME_TAGS));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		var hourSpinnerModel   = new IntegerSpinnerModel(0);
		var minuteSpinnerModel = new IntegerSpinnerModel(0);
		var secondSpinnerModel = new IntegerSpinnerModel(0);

		secondSpinnerModel.setMax(60);
		minuteSpinnerModel.setMax(60);

		minutesSpinnerSelector.setSpinnerModel(minuteSpinnerModel);
		secondsSpinnerSelector.setSpinnerModel(secondSpinnerModel);
		hoursSpinnerSelector.setSpinnerModel(hourSpinnerModel);

		// TODO BUG #001: if we set 3000 seconds for some unknown reason
		// the seconds text field doesn't show 0 and it keeps 3000...
		// However if we set 3001 it works fine (3001 -> 50 minutes and 1 second)

		minutesSpinnerSelector.valueProperty().addListener(e -> {
			if (minutesSpinnerSelector.getValue() > 59) minutesSpinnerSelector.setValue(0);
		});

		secondsSpinnerSelector.valueProperty().addListener(e -> {
			if (secondsSpinnerSelector.getValue() > 59) secondsSpinnerSelector.setValue(0);
		});

		minutesSpinnerSelector.setOnCommit(e -> hourSpinnerModel.setValue(filterInput(e)));

		minutesSpinnerSelector.setOnCommit(e -> {
			int currVal = filterInput(e);

			if (currVal > 59)
			{
				int minutesOverflow = currVal / 60;
				currVal -= minutesOverflow * 60;

				hourSpinnerModel.setValue(minutesOverflow);
			}

			// Very ugly fix for the TODO BUG #001 described above.
			// Seems like if we set 0 the compiler does something to skip that..

			minuteSpinnerModel.setValue(currVal == 0 ? 60 : currVal);
		});

		secondsSpinnerSelector.setOnCommit(e ->
		{
			int currVal = filterInput(e);

			if (currVal > 59)
			{
				int secondsOverflow = currVal / 60;
				int minutesOverflow = secondsOverflow / 60;

				currVal	-= secondsOverflow * 60;

				if (minutesOverflow == 0)
				{
					minuteSpinnerModel.setValue(secondsOverflow);
				}
				else
				{
					minuteSpinnerModel.setValue(secondsOverflow - minutesOverflow * 60);
					hourSpinnerModel.setValue(minutesOverflow);
				}
			}

			// Very ugly fix for the TODO BUG #001 described above.
			// Seems like if we set 0 the compiler does something to skip that..

			secondSpinnerModel.setValue(currVal == 0 ? 60 : currVal);

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
					  				  hoursSpinnerSelector.getValue() * 60 * 60
					                + minutesSpinnerSelector.getValue() * 60
									+ secondsSpinnerSelector.getValue());
			hideSpinner();
		}

//		homeRoot.setRight(null);
		homeRoot.getRight().setManaged(false);
		homeRoot.getRight().setVisible(false);
		activitySelectorComboBox.setVisible(false);
		activitySelectorComboBox.setManaged(false);


		selectedTagText.setText(TagHandler.getInstance().getSelectedTag().getName());
		selectedTagText.setVisible(true);
		selectedTagText.setManaged(true);
		selectedTagColorCircle.setFill(TagHandler.getInstance().getSelectedTag().getColor());
		selectedTagColorCircle.setVisible(true);
		selectedTagColorCircle.setManaged(true);


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



	public void onActivityStop()
	{
		activityButton.setText("Avvia");
		activityTimeTextField.setText("00:00");
		progressBarTime.setProgress(0.0);

		selectedTagText.setVisible(false);
		selectedTagText.setManaged(false);
		selectedTagColorCircle.setVisible(false);
		selectedTagColorCircle.setManaged(false);

		activityTimeTextField.setPrefWidth(0);

		if (ActivityHandler.getInstance().getCurrActivityType() != ActivityType.CHRONOMETER)
			showSpinner();

//		homeRoot.setRight(tagsRoot);

		homeRoot.getRight().setVisible(true);
		homeRoot.getRight().setManaged(true);

		activitySelectorComboBox.setVisible(true);
	}

	public void onTimerUpdateTick()
	{
		progressBarTime.setProgress(
				progressBarTime.getProgress() - ActivityHandler.getInstance().getCurrentProgressBarTick());
		activityTimeTextField.setText(ActivityHandler.getInstance().getCurrentTimeTick());
	}

	public void onChronometerUpdateTick()
	{
		final double progressbarTick = ActivityHandler.getInstance().getCurrentProgressBarTick();
		final double currProgressValue = progressBarTime.getProgress() + progressbarTick;

		if (currProgressValue < 1.0) progressBarTime.setProgress(currProgressValue);
		else progressBarTime.setProgress(0.0);

		activityTimeTextField.setText(ActivityHandler.getInstance().getCurrentTimeTick());
	}

	@FXML
	void toggleActivityState(ActionEvent event)
	{
		if (ActivityHandler.getInstance().isActivityStarted())
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
		else
		{
			ActivityHandler.getInstance().startActivity();
		}
	}

	@FXML
	void setActivityType(ActionEvent event)
	{
		// todo refactor: this isn't safe, if you change order you dont get

		final int activityIndex = activitySelectorComboBox.getSelectedIndex();

		var activityTypeSelected = ActivityType.values()[activityIndex];

		ActivityHandler.getInstance().setActivityType(activityTypeSelected);

		if (activityTypeSelected == ActivityType.CHRONOMETER)
			hideSpinner();
		else
			showSpinner();

		System.out.println("Activity type set to: " + ActivityHandler.getInstance().getCurrActivityType());
	}

	@FXML
	void toggleFullScreen()
	{
		SceneHandler.getInstance().setFullScreen();
	}

	void hideSpinner()
	{
		hoursSpinnerSelector.setPrefWidth(0);
		hoursSpinnerSelector.setMinWidth(0);
		hoursSpinnerSelector.setVisible(false);

		minutesSpinnerSelector.setPrefWidth(0);
		minutesSpinnerSelector.setMinWidth(0);
		minutesSpinnerSelector.setVisible(false);

		secondsSpinnerSelector.setPrefWidth(0);
		secondsSpinnerSelector.setMinWidth(0);
		secondsSpinnerSelector.setVisible(false);
	}

	void showSpinner()
	{
		hoursSpinnerSelector.setPrefWidth(Region.USE_COMPUTED_SIZE);
		hoursSpinnerSelector.setMinWidth(Region.USE_COMPUTED_SIZE);
		hoursSpinnerSelector.setVisible(true);
		hoursSpinnerSelector.setValue(0);

		minutesSpinnerSelector.setPrefWidth(Region.USE_COMPUTED_SIZE);
		minutesSpinnerSelector.setMinWidth(Region.USE_COMPUTED_SIZE);
		minutesSpinnerSelector.setVisible(true);
		minutesSpinnerSelector.setValue(0);

		secondsSpinnerSelector.setPrefWidth(Region.USE_COMPUTED_SIZE);
		secondsSpinnerSelector.setMinWidth(Region.USE_COMPUTED_SIZE);
		secondsSpinnerSelector.setVisible(true);
		secondsSpinnerSelector.setValue(0);
	}

	int filterInput(String num)
	{
		int currVal = 0;

		try
		{
			currVal = Integer.parseInt(num);
		}
		catch (Exception ignore)
		{
			Feedback.getInstance().showError("Errore", "Impossibile determinare il valore inserito");
		}

		return currVal;
	}


}

