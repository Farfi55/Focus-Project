package mafiadelprimobanco.focusproject;

import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import javafx.application.Platform;
import javafx.scene.control.Label;
import mafiadelprimobanco.focusproject.model.ActivityType;

import java.util.Timer;
import java.util.TimerTask;

public class ActivityHandler
{
	private static final ActivityHandler instance = new ActivityHandler();
	public static ActivityHandler getInstance() { return instance; }

	private ActivityHandler() { }

	private Timer activityTimer = null;
	private boolean activityStarted = false;
	private ActivityType currActivityType = null;

	//seconds -- used inside the timer function
	private int executionTime = 61;

	public void startActivity(MFXProgressSpinner progressBarTime, Label timeLabel)
	{
		activityTimer = new Timer();

		activityStarted = true;

		//just for testing purpose
		currActivityType = ActivityType.CRONO;

		switch (currActivityType)
		{
			case CRONO -> startChronoActivity(progressBarTime, timeLabel);
			case TIMER -> startTimerActivity(progressBarTime, timeLabel);
		}
	}

	public void stopCurrActivity(MFXProgressSpinner progressBarTime, Label timeLabel)
	{
		if (activityTimer != null)
		{
			activityTimer.cancel();
			activityTimer.purge();
			activityTimer = null;
		}

		timeLabel.setText("00:00");
		progressBarTime.setProgress(0);

		activityStarted = false;
	}

	private void startTimerActivity(MFXProgressSpinner progressBarTime, Label timeLabel)
	{
		progressBarTime.setProgress(1.0);

		activityTimer.scheduleAtFixedRate(new TimerTask()
		{
			int ticksLeft = executionTime;
			final double progressTick = 1.0 / ticksLeft;

			@Override
			public void run()
			{
				int minutes = ticksLeft / 60;
				int seconds = ticksLeft - (minutes * 60);

				Platform.runLater(() ->
				{
					timeLabel.setText(
							(minutes < 10 ? ("0" + minutes) : minutes)  + ":" +
									(seconds < 10 ? ("0" + seconds) : seconds)
					);

					progressBarTime.setProgress(progressBarTime.getProgress() - progressTick);
				});

				if (--ticksLeft < 0)
				{
					activityTimer.cancel();
					activityTimer.purge();
				}
			}
		}, 0, 1000);
	}

	private void startChronoActivity(MFXProgressSpinner progressBarTime, Label timeLabel)
	{
		progressBarTime.setProgress(0.0);

		activityTimer.scheduleAtFixedRate(new TimerTask()
		{
			int ticks = 0;
			final double progressTick = 1.0 / 60;

			@Override
			public void run()
			{
				int minutes = ++ticks / 60;
				int seconds = ticks - (minutes * 60);

				Platform.runLater(() ->
				{
					timeLabel.setText(
							(minutes < 10 ? ("0" + minutes) : minutes)  + ":" +
									(seconds < 10 ? ("0" + seconds) : seconds)
					);

					final double currProgressValue = progressBarTime.getProgress() + progressTick;

					if (currProgressValue < 1.0)
						progressBarTime.setProgress(currProgressValue);
					else
						progressBarTime.setProgress(0.0);

				});
			}
		}, 0, 1000);
	}


	//SETTERS
	public void setActivityType(ActivityType type) { currActivityType = type; }
	public void setExecutionTime(int executionTime) { this.executionTime = executionTime; }

	//GETTERS
	public ActivityType getCurrActivityType() {	return currActivityType; }
	public boolean isActivityStarted() { return activityStarted; }

}
