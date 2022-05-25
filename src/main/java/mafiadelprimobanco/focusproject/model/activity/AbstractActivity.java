package mafiadelprimobanco.focusproject.model.activity;

import java.time.LocalDateTime;

import static java.time.temporal.ChronoUnit.SECONDS;

public abstract class AbstractActivity
{
	protected final Integer tagUuid;
	protected LocalDateTime startTime;
	protected LocalDateTime endTime;

	public AbstractActivity(Integer tagUuid)
	{
		this.tagUuid = tagUuid;
	}

	public AbstractActivity(Integer tagUuid, LocalDateTime startTime)
	{
		this.tagUuid = tagUuid;
		this.startTime = startTime;
	}

	public AbstractActivity(Integer tagUuid, LocalDateTime startTime, LocalDateTime endTime)
	{
		assert (!endTime.isBefore(startTime));
		this.tagUuid = tagUuid;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public void startActity()
	{
		if (!isRunning()) this.startTime = LocalDateTime.now();
	}

	public void endActity()
	{
		if (isRunning()) this.endTime = LocalDateTime.now();
	}

	public boolean hasStarted()
	{
		return startTime != null;
	}

	public boolean hasEnded()
	{
		return endTime != null;
	}

	public boolean isRunning()
	{
		return startTime != null && endTime == null;
	}

	public Integer getTagUuid()
	{
		return tagUuid;
	}

	public LocalDateTime getStartTime()
	{
		return startTime;
	}

	public LocalDateTime getEndTime()
	{
		return endTime;
	}

	public Long getSecondsSinceStart()
	{
		if (hasStarted()) return SECONDS.between(LocalDateTime.now(), startTime);
		else return 0L;
	}

	public Long getDuration()
	{
		if (isRunning()) return 0L;
		else return SECONDS.between(endTime, startTime);
	}
}
