package mafiadelprimobanco.focusproject.model.activity;

import java.time.LocalDateTime;

public class ChronometerActivity extends AbstractActivity
{
	public ChronometerActivity() { super(); }

	public ChronometerActivity(Integer tagUuid)
	{
		super(tagUuid);
	}

	public ChronometerActivity(Integer tagUuid, LocalDateTime startTime)
	{
		super(tagUuid, startTime);
	}

	public ChronometerActivity(Integer tagUuid, LocalDateTime startTime, LocalDateTime endTime)
	{
		super(tagUuid, startTime, endTime);
	}
}
