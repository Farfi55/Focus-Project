package mafiadelprimobanco.focusproject.model.activity;

import mafiadelprimobanco.focusproject.model.ActivityType;
import org.json.JSONObject;

import java.time.LocalDateTime;

public class ChronometerActivity extends AbstractActivity
{
	public ChronometerActivity() { super(); }

	public ChronometerActivity(Integer tagUuid, Integer treeUuid, LocalDateTime startTime, LocalDateTime endTime)
	{
		super(tagUuid, treeUuid, startTime, endTime);
	}

	public JSONObject toJsonObject()
	{
		return new JSONObject("{" + "type:" + ActivityType.CHRONOMETER.key + ", tagUuid:" + tagUuid + ", treeUuid:" + treeUuid +
				", startTime:" + "\"" + startTime + "\"" + ", endTime:" + "\"" + endTime + "\"" + '}');
	}

	@Override
	public boolean wasSuccessful()
	{
		//todo: move into settings
		int minSuccessChronometerDuration = 20;
		return (getFinalDuration() >= minSuccessChronometerDuration);
	}

}
