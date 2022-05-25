package mafiadelprimobanco.focusproject.model.activity;

import java.time.LocalDateTime;

import static java.time.temporal.ChronoUnit.SECONDS;

public abstract class AbstractActivity
{
	protected final Integer tagUuid;
	protected final LocalDateTime startTime;
	protected LocalDateTime endTime;

	public AbstractActivity(Integer tagUuid)
	{ this(tagUuid, LocalDateTime.now(), null); }

	public AbstractActivity(Integer tagUuid, LocalDateTime startTime)
	{ this(tagUuid, startTime, null); }

	public AbstractActivity(Integer tagUuid, LocalDateTime startTime, LocalDateTime endTime)
	{
		assert (endTime == null) || (!endTime.isBefore(startTime));
		this.tagUuid = tagUuid;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public void endActity()
	{
		if (isRunning()) this.endTime = LocalDateTime.now();
	}

	public boolean isRunning()
	{
		return endTime == null;
	}

	public boolean hasEnded()
	{
		return endTime != null;
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
		return SECONDS.between(LocalDateTime.now(), startTime);
	}

	public Long getDuration()
	{
		if (isRunning()) return 0L;
		else return SECONDS.between(endTime, startTime);
	}
}
