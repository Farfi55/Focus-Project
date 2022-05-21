package mafiadelprimobanco.focusproject.controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;
import mafiadelprimobanco.focusproject.ActivityHandler;
import mafiadelprimobanco.focusproject.Feedback;
import mafiadelprimobanco.focusproject.SceneHandler;
import mafiadelprimobanco.focusproject.TagHandler;
import mafiadelprimobanco.focusproject.model.ActivityObserver;
import mafiadelprimobanco.focusproject.model.ActivityType;
import mafiadelprimobanco.focusproject.model.utils.FXMLReferences;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;

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

		// makes sure everything is looking normal at the beginning
		onActivityStop();

		// todo refactor:  the definition shouldn't go inside the initialize
		//  or change method completely
		activityTimeTextField.textProperty().addListener((e) ->
		{
			if (ActivityHandler.getInstance().isActivityStarted()) return;

			String text = activityTimeTextField.getText();

			int len = text.length();

			if (len == 0) return;

			int minutes = 0;
			int seconds = 0;

			/*TODO fix focus shit to make that working again */
            /*
                if (len == 3)
                    activityTimeTextField.setText(text.substring(0, len-2) + ":" + text.substring(len - 2, len));
            */

			if (len >= 4)
			{
				minutes = Integer.parseInt(text.split(":")[0]);
				seconds = Integer.parseInt(text.split(":")[1]);
			}
			else seconds = Integer.parseInt(text);

			if (seconds > 59)
			{
				Feedback.getInstance().showError("Errore",
						"Errore nella formattazione dei secondi.\n" + "Prova ad inserire un valore inferiore a 59");
				return;
			}

			ActivityHandler.getInstance().setExecutionTime(minutes * 60 + seconds);
		});

	}



	@Override
	public void onActivityStart()
	{
		activityButton.setText("Interrompi");

		activityTimeTextField.setEditable(false);


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
		activityTimeTextField.setEditable(true);
		activityTimeTextField.setText("00:00");
		progressBarTime.setProgress(0.0);

		selectedTagText.setVisible(false);
		selectedTagText.setManaged(false);
		selectedTagColorCircle.setVisible(false);
		selectedTagColorCircle.setManaged(false);

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
		ActivityHandler.getInstance().setActivityType(
				ActivityType.values()[activitySelectorComboBox.getSelectedIndex()]);
		System.out.println("Activity type set to: " + ActivityHandler.getInstance().getCurrActivityType());
	}

	@FXML
	void setTime(KeyEvent event)
	{
	}


}

