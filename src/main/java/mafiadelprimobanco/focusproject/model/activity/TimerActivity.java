package mafiadelprimobanco.focusproject.model.activity;

import java.time.LocalDateTime;

public class TimerActivity extends AbstractActivity
{
	/**
	 * seconds the user originally decided to spend on this activity
	 */
	private final Integer chosenDuration;

	public TimerActivity(Integer tagUuid, Integer chosenDuration)
	{
		super(tagUuid);
		this.chosenDuration = chosenDuration;
	}

	public TimerActivity(Integer tagUuid, LocalDateTime startTime, Integer chosenDuration)
	{
		super(tagUuid, startTime);
		this.chosenDuration = chosenDuration;
	}

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

	public LocalDateTime getExpectedEndTime()
	{
		if (!hasStarted()) return null;
		else return startTime.plusSeconds(chosenDuration);
	}

	public Integer getChosenDuration()
	{
		return chosenDuration;
	}
}
