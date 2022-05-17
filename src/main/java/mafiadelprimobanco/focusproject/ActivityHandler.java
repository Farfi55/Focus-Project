package mafiadelprimobanco.focusproject;

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

	private ActivityHandler() { }

	private final List<ActivityObserver> listeners = new ArrayList<>();

	private Timer activityTimer = null;
	private boolean activityStarted = false;
	private ActivityType currActivityType = ActivityType.TIMER;

	//seconds -- used inside the timer function
	private int executionTime = 0;

	private String currentTimeTick = "00:00";
	private double currentProgressBarTick = 0.0;

	public void startActivity()
	{
		activityTimer = new Timer();

		activityStarted = true;

		fireStartActivity();

		switch (currActivityType)
		{
			case CRONO -> startChronoActivity();
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

		fireEndActivity();

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
				int minutes = ticksLeft / 60;
				int seconds = ticksLeft - (minutes * 60);

				currentTimeTick = ((minutes < 10 ? ("0" + minutes) : minutes) + ":" + (seconds < 10 ? ("0" + seconds) : seconds));

				fireUpdateActivity();

				if (--ticksLeft < 0)
				{
					stopCurrActivity();
				}
			}
		}, 0, 1000);
	}

	private void startChronoActivity()
	{
		currentProgressBarTick = 1.0 / 60;

		activityTimer.scheduleAtFixedRate(new TimerTask()
		{
			int ticks = 0;

			@Override
			public void run()
			{
				int minutes = ++ticks / 60;
				int seconds = ticks - (minutes * 60);

				currentTimeTick = ((minutes < 10 ? ("0" + minutes) : minutes) + ":" + (seconds < 10 ? ("0" + seconds) : seconds));

				fireUpdateActivity();
			}
		}, 0, 1000);
	}

	//SETTERS
	public void setActivityType(ActivityType type) { currActivityType = type; }

	public void setExecutionTime(int executionTime) { this.executionTime = executionTime; }

	//GETTERS
	public ActivityType getCurrActivityType() { return currActivityType; }

	public boolean isActivityStarted() { return activityStarted; }

	public String getCurrentTimeTick() { return currentTimeTick; }

	public double getCurrentProgressBarTick() { return currentProgressBarTick; }

	public void addListener(ActivityObserver startLst) { listeners.add(startLst); }

	public void removeListener(ActivityObserver startLst) { listeners.remove(startLst); }

	public void fireStartActivity()
	{
		for (ActivityObserver lt : listeners)
			lt.onStart();
	}

	public void fireUpdateActivity()
	{
		for (ActivityObserver lt : listeners)
			lt.onUpdate();
	}

	public void fireEndActivity()
	{
		for (ActivityObserver lt : listeners)
			lt.onEnd();
	}

}
