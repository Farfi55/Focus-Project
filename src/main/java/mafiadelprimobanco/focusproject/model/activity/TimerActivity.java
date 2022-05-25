package mafiadelprimobanco.focusproject.model.activity;

import java.time.LocalDateTime;

public class TimerActivity extends AbstractActivity
{
	/**
	 * seconds the user originally decided to spend on this activity
	 */
	private final Long chosenDuration;

	public TimerActivity(Integer tagUuid, Long chosenDuration)
	{
		super(tagUuid);
		this.chosenDuration = chosenDuration;
	}

	public TimerActivity(Integer tagUuid, LocalDateTime startTime, Long chosenDuration)
	{
		super(tagUuid, startTime);
		this.chosenDuration = chosenDuration;
	}

	public TimerActivity(Integer tagUuid, LocalDateTime startTime, LocalDateTime endTime, Long chosenDuration)
	{
		super(tagUuid, startTime, endTime);
		this.chosenDuration = chosenDuration;
	}

	public boolean wasInterrupted()
	{
		if (isRunning()) return false;

		return getExpectedEndTime().isBefore(endTime);
	}

	public LocalDateTime getExpectedEndTime()
	{
		return startTime.plusSeconds(chosenDuration);
	}

	public Long getChosenDuration()
	{
		return chosenDuration;
	}
}
