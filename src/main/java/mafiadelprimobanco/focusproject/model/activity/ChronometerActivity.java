package mafiadelprimobanco.focusproject.model.activity;

import java.time.LocalDateTime;

public class ChronometerActivity extends AbstractActivity
{
	public ChronometerActivity() { super(); }

	public ChronometerActivity(Integer tagUuid, Integer treeUuid, LocalDateTime startTime, LocalDateTime endTime)
	{
		super(tagUuid, treeUuid, startTime, endTime);
	}

	@Override
	public boolean wasSuccessful()
	{
		//todo: move into settings
		int minSuccessChronometerDuration = 20;
		return (getFinalDuration() >= minSuccessChronometerDuration);
	}

}
