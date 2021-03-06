package mafiadelprimobanco.focusproject.model;

import javafx.beans.property.SimpleIntegerProperty;
import org.json.JSONObject;

public class Tree
{
	private Integer uuid;
	private final String name;
	private final String matureTreeSprite;
	private final String deadTreeSprite;

	/**
	 * time in seconds required to unlock this tree
	 */
	private final Integer totalRequiredTime;
	/**
	 * time put in to unlock this tree
	 */
	private final SimpleIntegerProperty progressTime = new SimpleIntegerProperty(this, "progressTime");

	public Tree(Integer uuid, String name, String matureTreeSprite, String deadTreeSprite, Integer totalRequiredTime,
			Integer progressTime)
	{
		this.uuid = uuid;
		this.name = name;
		this.matureTreeSprite = matureTreeSprite;
		this.deadTreeSprite = deadTreeSprite;
		this.totalRequiredTime = totalRequiredTime;
		this.progressTime.set(progressTime);
	}

	public SimpleIntegerProperty progressTimeProperty()
	{
		return progressTime;
	}

	public int addProgressTime(Integer seconds)
	{
		assert seconds >= 0;

		int overflow = seconds - getRemainingRequiredTime();
		if (overflow > 0)
		{
			progressTime.set(totalRequiredTime);
			return overflow;
		}
		else
		{
			progressTime.setValue(progressTime.getValue() + seconds);
			return 0;
		}
	}

	public Integer getUuid() { return uuid; }

	public String getName() { return name; }

	public String getMatureTreeSprite() { return matureTreeSprite; }

	public String getDeadTreeSprite() { return deadTreeSprite; }

	public Integer getTotalRequiredTime()
	{
		return totalRequiredTime;
	}

	public Integer getProgressTime()
	{
		return progressTime.get();
	}

	public Integer getRemainingRequiredTime()
	{
		return totalRequiredTime - progressTime.get();
	}

	public boolean isUnlocked()
	{
		return getProgressTime().equals(totalRequiredTime);
	}

	public boolean isNotUnlocked() { return !isUnlocked(); }

	public Float getUnlockProgress()

	{
		if (totalRequiredTime == 0) return 1.0f;
		else return (float)getProgressTime() / getTotalRequiredTime();
	}


	public String toJson()
	{
		return "{uuid:" + uuid + ", name:'" + name + '\'' + ", matureTreeSprite:'" + matureTreeSprite + '\''
				+ ", deadTreeSprite:'" + deadTreeSprite + '\'' + ", totalRequiredTime:" + totalRequiredTime
				+ ", progressTime:" + getProgressTime() + '}';
	}

	public Tree(JSONObject jsonObject)
	{
		this.uuid = jsonObject.getInt("uuid");
		this.name = jsonObject.getString("name");
		this.matureTreeSprite = jsonObject.getString("matureTreeSprite");
		this.deadTreeSprite = jsonObject.getString("deadTreeSprite");
		this.totalRequiredTime = jsonObject.getInt("totalRequiredTime");
		this.progressTime.set(jsonObject.getInt("progressTime"));
	}

	public void updateProgressFromJson(JSONObject jsonObject){
		this.progressTime.set(jsonObject.getInt("progressTime"));
	}
}
