package mafiadelprimobanco.focusproject.model.activity;

import org.json.JSONObject;

import java.time.LocalDateTime;

public class TimerActivity extends AbstractActivity
{
	/**
	 * seconds the user decided to spend on this activity
	 */
	private Integer chosenDuration;

	public TimerActivity() { }

	@Override
	public void startActivity()
	{
		assert chosenDuration != null && chosenDuration > 0;
		super.startActivity();
	}

	public TimerActivity(Integer tagUuid, Integer treeUuid, LocalDateTime startTime, LocalDateTime endTime, Integer chosenDuration)
	{
		super(tagUuid, treeUuid, startTime, endTime);
		this.chosenDuration = chosenDuration;
	}

	public boolean wasInterrupted()
	{
		if (!hasStarted() || isRunning()) return false;

		return getExpectedEndTime().isBefore(endTime);
	}
	public boolean wasCompleted()
	{
		if (!hasStarted() || isRunning()) return false;

		return getExpectedEndTime().isEqual(endTime);
	}

	public double getProgress()
	{
		if (chosenDuration == null || chosenDuration == 0) return 1.0d;
		else return Math.min(1.0d, (double)getSecondsSinceStart() / chosenDuration);
	}

	public int getRemainingDuration()
	{
		return Math.max(0, chosenDuration - getSecondsSinceStart());
	}

	public LocalDateTime getExpectedEndTime()
	{
		if (!hasStarted()) return null;
		else return startTime.plusSeconds(chosenDuration);
	}

	public Integer getChosenDuration()
	{
		return chosenDuration;
	}

	public void setChosenDuration(Integer chosenDuration)
	{
		assert (this.chosenDuration == null);
		this.chosenDuration = chosenDuration;
	}

	public JSONObject toJsonObject()
	{
		return new JSONObject("{" + "type:Timer," + "tagUuid:" + tagUuid + ", treeUuid:" + treeUuid + ", startTime:" + "\"" + startTime
				+ "\"" + ", endTime:" + "\"" + endTime +  "\"" + ", chosenDuration:" + chosenDuration + '}');
	}
}
