package mafiadelprimobanco.focusproject;

import javafx.application.Platform;
import mafiadelprimobanco.focusproject.model.ActivityObserver;
import mafiadelprimobanco.focusproject.model.ActivityType;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ActivityHandler
{
	private static final ActivityHandler instance = new ActivityHandler();

	public static ActivityHandler getInstance() { return instance; }

	private final List<ActivityObserver> listeners = new ArrayList<>();
	private Timer activityTimer = null;
	private boolean activityStarted = false;
	private ActivityType currActivityType = ActivityType.TIMER;
	//seconds -- used inside the timer function
	private int executionTime = 0;
	private String currentTimeTick = "00:00";
	private double currentProgressBarTick = 0.0;

	private ActivityHandler() { }

	public void startActivity()
	{
		activityTimer = new Timer();

		activityStarted = true;

		invokeOnStartActivity();

		switch (currActivityType)
		{
			case CHRONOMETER -> startChronometerActivity();
			case TIMER -> startTimerActivity();
		}
	}

	public void stopCurrActivity()
	{
		if (activityTimer != null)
		{
			activityTimer.cancel();
			activityTimer.purge();
			activityTimer = null;
		}

		invokeOnEndActivity();

		activityStarted = false;
	}

	private void startTimerActivity()
	{
		currentProgressBarTick = 1.0 / (executionTime + 1);
		activityTimer.scheduleAtFixedRate(new TimerTask()
		{
			int ticksLeft = executionTime;

			@Override
			public void run()
			{
				int hours 	= ticksLeft / 60 / 60;
				int minutes = ticksLeft / 60 - (hours * 60);
				int seconds = ticksLeft - (minutes * 60) - (hours * 60 * 60);

				currentTimeTick = (
						(hours < 10 ? ("0" + hours) : hours) + ":" +
						(minutes < 10 ? ("0" + minutes) : minutes) + ":" +
						(seconds < 10 ? ("0" + seconds) : seconds)
				);

				invokeOnUpdateActivity();

				if (--ticksLeft < 0)
				{
					stopCurrActivity();
				}
			}
		}, 0, 1000);
	}

	private void startChronometerActivity()
	{
		currentProgressBarTick = -1;

		activityTimer.scheduleAtFixedRate(new TimerTask()
		{
			int ticks = 0;

			@Override
			public void run()
			{
				int hours 	= ++ticks / 60 / 60;
				int minutes = ticks / 60 - (hours * 60);
				int seconds = ticks - (minutes * 60) - (hours * 60 * 60);

				currentTimeTick = (
						(hours < 10 ? ("0" + hours) : hours) + ":" +
						(minutes < 10 ? ("0" + minutes) : minutes) + ":" +
						(seconds < 10 ? ("0" + seconds) : seconds)
				);

				invokeOnUpdateActivity();
			}
		}, 0, 1000);
	}

	public void addListener(ActivityObserver observer)
	{
		assert !listeners.contains(observer);
		listeners.add(observer);
	}

	public void removeListener(ActivityObserver observer)
	{
		assert listeners.contains(observer);
		listeners.remove(observer);
	}

	private void invokeOnStartActivity()
	{
		for (ActivityObserver observer : listeners)
			observer.onActivityStart();

		Platform.runLater(() -> {
			for (ActivityObserver observer : listeners)
				observer.onActivityStartSafe();
		});
	}

	private void invokeOnUpdateActivity()
	{
		for (ActivityObserver observer : listeners)
			observer.onActivityUpdate();

		Platform.runLater(() -> {
			for (ActivityObserver observer : listeners)
				observer.onActivityUpdateSafe();
		});
	}

	private void invokeOnEndActivity()
	{
		for (ActivityObserver observer : listeners)
			observer.onActivityEnd();

		Platform.runLater(() -> {
			for (ActivityObserver observer : listeners)
				observer.onActivityEndSafe();
		});
	}

	//GETTERS
	public ActivityType getCurrActivityType() { return currActivityType; }

	public boolean isActivityStarted() { return activityStarted; }

	public String getCurrentTimeTick() { return currentTimeTick; }

	public double getCurrentProgressBarTick() { return currentProgressBarTick; }

	//SETTERS
	public void setActivityType(ActivityType type) { currActivityType = type; }

	public void setExecutionTime(int executionTime) { this.executionTime = executionTime; }

}
