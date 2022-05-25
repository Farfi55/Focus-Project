package mafiadelprimobanco.focusproject.model.activity;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public abstract class AbstracyActivity
{
	protected Integer tagUuid;
	protected LocalDateTime startTime;
	protected LocalDateTime endTime;

	public AbstracyActivity(Integer tagUuid)
	{ this(tagUuid, LocalDateTime.now(), null); }

	public AbstracyActivity(Integer tagUuid, LocalDateTime startTime)
	{ this(tagUuid, startTime, null); }

	public AbstracyActivity(Integer tagUuid, LocalDateTime startTime, LocalDateTime endTime)
	{
		assert !endTime.isBefore(startTime);
		this.tagUuid = tagUuid;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public void endActity()
	{
		this.endTime = LocalDateTime.now();
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

	public long getSecondsSinceStart()
	{
		return ChronoUnit.SECONDS.between(LocalDateTime.now(), startTime);
	}
}
