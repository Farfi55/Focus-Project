package mafiadelprimobanco.focusproject.handler;

import mafiadelprimobanco.focusproject.model.ActivityObserver;
import mafiadelprimobanco.focusproject.model.Tag;
import mafiadelprimobanco.focusproject.model.Tree;
import mafiadelprimobanco.focusproject.model.activity.AbstractActivity;
import mafiadelprimobanco.focusproject.model.activity.ChronometerActivity;
import mafiadelprimobanco.focusproject.model.activity.TimerActivity;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.HashMap;
import java.util.TreeSet;

public class ActivityStatsHandler implements ActivityObserver
{
	private static final ActivityStatsHandler instance = new ActivityStatsHandler();

	public static ActivityStatsHandler getInstance()
	{
		return instance;
	}

	/**
	 * maps tags to set of activities
	 */
	HashMap<Integer, TreeSet<AbstractActivity>> activities = new HashMap<>();


	private ActivityStatsHandler()
	{
	}

	@Override
	public void onActivityEnd(AbstractActivity currentActivity)
	{
		addActivity(currentActivity);
	}

	public void init()
	{
		ActivityHandler.getInstance().addListener(this);
	}

	private void addActivity(AbstractActivity currentActivity)
	{
		Integer tagUuid = currentActivity.getTagUuid();
		if (!activities.containsKey(tagUuid)) activities.put(tagUuid, new TreeSet<>());

		activities.get(tagUuid).add(currentActivity);
	}


	public ActivityTime getTagActivityTime(Tag tag)
	{
		return getTagActivityTime(tag.getUuid());
	}

	public ActivityTime getTagActivityTime(Integer uuid)
	{
		TreeSet<AbstractActivity> tagActivities = this.activities.get(uuid);
		if (tagActivities == null) return null;

		ActivityTime activityTime = new ActivityTime();
		for (var activity : tagActivities)
		{
			if (activity.getStartTime().isBefore(LocalDateTime.now().minusYears(1))) break;

			activityTime.addActivityDuration(activity);
		}
		return activityTime;
	}

	public TreeStats getTreesStats(Tree tree)
	{
		return getTreesStats(tree.getUuid());
	}

	public TreeStats getTreesStats(Integer treeUuid)
	{
		TreeStats stats = new TreeStats();

		for (TreeSet<AbstractActivity> activityTreeSet : activities.values())
		{
			activityTreeSet.forEach(activity ->
			{
				if (activity.getTreeUuid().equals(treeUuid))
				{
					stats.totalPlanted++;

					if (activity.getStartTime().isAfter(stats.lastPlanted))
						stats.lastPlanted = activity.getStartTime();

					if (activity instanceof TimerActivity timerActivity)
					{
						if (timerActivity.wasInterrupted()) stats.totaDead++;
						else stats.totalMature++;
					}
					if (activity instanceof ChronometerActivity chronometerActivity)
					{
						//TODO: move into settings
						int successfulChronometerMinimunTime = 10;
						if (chronometerActivity.getFinalDuration() < successfulChronometerMinimunTime) stats.totaDead++;
						else stats.totalMature++;
					}

				}
			});

		}
		return stats;

	}


	public static class TreeStats
	{
		public Integer totalPlanted = 0;
		public Integer totalMature = 0;
		public Integer totaDead = 0;
		public LocalDateTime lastPlanted = LocalDateTime.MIN;
	}


	public static class ActivityTime
	{
		public Integer dayTime = 0;
		public Integer weekTime = 0;
		public Integer monthTime = 0;
		public Integer yearTime = 0;


		public void addActivityDuration(AbstractActivity activity)
		{
			int duration = activity.getFinalDuration();
			LocalDateTime startTime = activity.getStartTime();
			if (startTime.isAfter(LocalDateTime.now().minusYears(1))) yearTime += duration;
			if (startTime.isAfter(LocalDateTime.now().minusMonths(1))) weekTime += duration;
			if (startTime.isAfter(LocalDateTime.now().minusWeeks(1))) monthTime += duration;
			if (startTime.isAfter(LocalDateTime.now().minusDays(1))) dayTime += duration;
		}
	}
}
