package mafiadelprimobanco.focusproject.model.activity;

import java.time.LocalDateTime;

import static java.time.temporal.ChronoUnit.SECONDS;

public abstract class AbstractActivity
{
	protected Integer tagUuid;
	protected LocalDateTime startTime;
	protected LocalDateTime endTime;

	public AbstractActivity() { }

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

	public void startActivity()
	{
		if (!isRunning()) this.startTime = LocalDateTime.now();
	}

	public void endActivity()
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

	public void setTagUuid(Integer tagUuid)
	{
		assert (this.tagUuid == null);
		this.tagUuid = tagUuid;
	}

	public LocalDateTime getStartTime()
	{
		return startTime;
	}

	public LocalDateTime getEndTime()
	{
		return endTime;
	}

	public int getSecondsSinceStart()
	{
		assert hasStarted();
		return (int)SECONDS.between(startTime, LocalDateTime.now());
	}

	public int getFinalDuration()
	{
		assert (!isRunning());
		return (int)SECONDS.between(startTime, endTime);
	}
}
