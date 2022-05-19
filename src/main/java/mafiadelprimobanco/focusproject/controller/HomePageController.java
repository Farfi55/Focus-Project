package mafiadelprimobanco.focusproject.controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import mafiadelprimobanco.focusproject.ActivityHandler;
import mafiadelprimobanco.focusproject.SceneHandler;
import mafiadelprimobanco.focusproject.TagHandler;
import mafiadelprimobanco.focusproject.model.ActivityObserver;
import mafiadelprimobanco.focusproject.model.ActivityType;
import mafiadelprimobanco.focusproject.model.Tag;
import mafiadelprimobanco.focusproject.model.TagsObserver;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;

public class HomePageController implements TagsObserver, ActivityObserver
{
	@FXML private SplitPane splitPane;
	@FXML private VBox tagsSidebar;

	@FXML private MFXButton fullScreenButton;
	@FXML private FontIcon fullScreenIcon;

	@FXML private MFXComboBox<String> activitySelectorComboBox;

	@FXML private MFXTextField activityTimeTextField;
	@FXML private MFXProgressSpinner progressBarTime;

	@FXML private ImageView treeImageViewer;

	@FXML private MFXButton activityButton;


	@FXML
	void initialize()
	{
		populateTagsList();

		TagHandler.getInstance().addListener(this);
		ActivityHandler.getInstance().addListener(this);

		activitySelectorComboBox.getItems().addAll("Cronometro", "Timer", "Timer Pomodoro");

		activitySelectorComboBox.selectFirst();

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
				SceneHandler.getInstance().showErrorMessage("Errore",
						"Errore nella formattazione dei secondi.\n" + "Prova ad inserire un valore inferiore a 59");
				return;
			}

			ActivityHandler.getInstance().setExecutionTime(minutes * 60 + seconds);
		});

	}

	private void populateTagsList()
	{
		try
		{
			for (Tag tag : TagHandler.getInstance().getTags())
				tagsSidebar.getChildren().add(SceneHandler.getInstance().createTagView(tag));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void onTagAdded(Tag tag)
	{
		try
		{
			tagsSidebar.getChildren().add(SceneHandler.getInstance().createTagView(tag));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void onTagRemoving(Tag tag)
	{
		for (Node child : tagsSidebar.getChildren())
		{
			if (child.getProperties().containsKey("tag-uuid"))
				if (child.getProperties().get("tag-uuid").equals(tag.getUuid()))
				{
					tagsSidebar.getChildren().remove(child);
					return;
				}
		}
	}

	@Override
	public void onTagChanged(Tag tag) { }


	@Override
	public void onActivityStart()
	{
		activityButton.setText("Interrompi");
		activityTimeTextField.editableProperty().setValue(false);

		splitPane.setDividerPositions(1.0);

		activitySelectorComboBox.setVisible(false);

		tagsSidebar.setMinWidth(0);
		tagsSidebar.setVisible(false);

		switch (ActivityHandler.getInstance().getCurrActivityType())
		{
			case CHRONOMETER -> progressBarTime.setProgress(0.0);
			case TIMER -> progressBarTime.setProgress(1.0);
		}
	}

	@Override
	public void onActivityUpdate()
	{
		switch (ActivityHandler.getInstance().getCurrActivityType())
		{
			case CHRONOMETER -> onChronometerUpdateTick();
			case TIMER -> onTimerUpdateTick();
		}
	}

	@Override
	public void onActivityEnd() { onStopActivityEvent(); }

	public void onStopActivityEvent()
	{
		activityButton.setText("Avvia");
		activityTimeTextField.editableProperty().setValue(true);
		activityTimeTextField.setText("00:00");
		progressBarTime.setProgress(0.0);
		tagsSidebar.setMinWidth(Region.USE_COMPUTED_SIZE);
		tagsSidebar.setVisible(true);
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
		if (!ActivityHandler.getInstance().isActivityStarted())
		{
			ActivityHandler.getInstance().startActivity();
		}
		else
		{
			ActivityHandler.getInstance().stopCurrActivity();
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

