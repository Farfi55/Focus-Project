package mafiadelprimobanco.focusproject.model.activity;

import java.time.LocalDateTime;

public class TimerActivity extends AbstractActivity
{
	/**
	 * seconds the user decided to spend on this activity
	 */
	private Integer chosenDuration;

	public TimerActivity() { }



	public TimerActivity(Integer tagUuid, LocalDateTime startTime, LocalDateTime endTime, Integer chosenDuration)
	{
		super(tagUuid, startTime, endTime);
		this.chosenDuration = chosenDuration;
	}

	public boolean wasInterrupted()
	{
		if (!hasStarted() || isRunning()) return false;

		return getExpectedEndTime().isBefore(endTime);
	}

	public double getProgress()
	{
		return Math.min(1.0d, (double)getSecondsSinceStart() / chosenDuration);
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
}
