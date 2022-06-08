package mafiadelprimobanco.focusproject.handler;

import javafx.application.Platform;
import mafiadelprimobanco.focusproject.model.ActivityObserver;
import mafiadelprimobanco.focusproject.model.ActivityType;
import mafiadelprimobanco.focusproject.model.Tree;
import mafiadelprimobanco.focusproject.model.activity.AbstractActivity;
import mafiadelprimobanco.focusproject.model.activity.ChronometerActivity;
import mafiadelprimobanco.focusproject.model.activity.TimerActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ActivityHandler
{
	private static final ActivityHandler instance = new ActivityHandler();

	public static ActivityHandler getInstance() { return instance; }

	private final List<ActivityObserver> listeners = new ArrayList<>();

	private ActivityType currentActivityType = ActivityType.CHRONOMETER;
	private AbstractActivity currentActivity = new ChronometerActivity();

	private Timer activityTimer = null;

	//seconds -- used inside the timer function
	private int chosenTimerDuration = 0;


	private ActivityHandler() { }

	public void startActivity()
	{
		activityTimer = new Timer();

		currentActivity.setTagUuid(TagHandler.getInstance().getSelectedTag().getUuid());
		currentActivity.setTreeUuid(TreeHandler.getInstance().getSelectedActivityTree().getUuid());
		currentActivity.startActivity();
		invokeOnActivityStarting();

		switch (currentActivityType)
		{
			case CHRONOMETER -> startChronometerActivity();
			case TIMER -> startTimerActivity();
		}
	}

	private AbstractActivity createActivity()
	{
		return switch (currentActivityType)
				{
					case CHRONOMETER -> new ChronometerActivity();
					case TIMER -> new TimerActivity();
					default -> throw new IllegalStateException("Unexpected value: " + currentActivityType);
				};
	}

	public void stopCurrentActivity()
	{
		System.out.println("stop");
		currentActivity.endActivity();

		if (activityTimer != null)
		{
			activityTimer.cancel();
			activityTimer.purge();
			activityTimer = null;
		}

		invokeOnActivityEnd();

		// todo: make this safer
		Platform.runLater(() -> currentActivity = createActivity());

	}

	private void startTimerActivity()
	{
		assert (currentActivity instanceof TimerActivity);
		var timerActivity = (TimerActivity)currentActivity;

		timerActivity.setChosenDuration(chosenTimerDuration);

		activityTimer.scheduleAtFixedRate(new TimerTask()
		{
			@Override
			public void run()
			{
				invokeOnActivityUpdate();

				if (timerActivity.getRemainingDuration() == 0) stopCurrentActivity();
			}
		}, 0, 1000);
	}

	private void startChronometerActivity()
	{
		activityTimer.scheduleAtFixedRate(new TimerTask()
		{
			@Override
			public void run()
			{
				invokeOnActivityUpdate();
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

	private void invokeOnActivityStarting()
	{
		for (ActivityObserver observer : listeners)
			observer.onActivityStarting(currentActivity);

		Platform.runLater(() ->
		{
			for (ActivityObserver observer : listeners)
				observer.onActivityStartingSafe(currentActivity);
		});
	}

	private void invokeOnActivityUpdate()
	{
		for (ActivityObserver observer : listeners)
			observer.onActivityUpdate(currentActivity);

		Platform.runLater(() ->
		{
			for (ActivityObserver observer : listeners)
				observer.onActivityUpdateSafe(currentActivity);
		});
	}

	private void invokeOnActivityEnd()
	{
		for (ActivityObserver observer : listeners)
			observer.onActivityEnd(currentActivity);

		Platform.runLater(() ->
		{
			for (ActivityObserver observer : listeners)
				observer.onActivityEndSafe(currentActivity);
		});
	}

	public boolean isActivityRunning()
	{
		return currentActivity.isRunning();
	}

	public AbstractActivity getCurrentActivity()
	{
		return currentActivity;
	}

	public int getRemainingTimerDuration()
	{
		assert currentActivity instanceof TimerActivity;

		return ((TimerActivity)currentActivity).getRemainingDuration();


	}

	//GETTERS
	public ActivityType getCurrentActivityType() { return currentActivityType; }

	/**
	 * @return 0.0 just started ... 1.0 completed
	 */
	public double getTimerActivityProgress()
	{
		if (currentActivity instanceof TimerActivity timerActivity)
		{
			return timerActivity.getProgress();
		}
		return -1;
	}

	public void setActivityTree(Tree chosenTree) { setActivityTree(chosenTree.getUuid()); }

	public void setActivityTree(Integer chosenTreeUuid)
	{
		currentActivity.setTreeUuid(chosenTreeUuid);
	}

	//SETTERS
	public void setActivityType(ActivityType type)
	{
		currentActivityType = type;
		currentActivity = createActivity();
	}

	public void setChosenTimerDuration(int chosenTimerDuration) { this.chosenTimerDuration = chosenTimerDuration; }

}
