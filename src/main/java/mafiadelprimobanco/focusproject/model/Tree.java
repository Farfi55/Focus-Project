package mafiadelprimobanco.focusproject.model;

import javafx.scene.image.Image;

public class Tree
{
	private final Integer uuid;
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
	private Integer progressTime;

	public Tree(Integer uuid, String name, String matureTreeSprite, String deadTreeSprite, Integer totalRequiredTime,
			Integer progressTime)
	{
		this.uuid = uuid;
		this.name = name;
		this.matureTreeSprite = matureTreeSprite;
		this.deadTreeSprite = deadTreeSprite;
		this.totalRequiredTime = totalRequiredTime;
		this.progressTime = progressTime;
	}


	public int addTime(Integer seconds)
	{
		assert seconds >= 0;

		progressTime += seconds;
		if (progressTime > totalRequiredTime)
		{
			int overflow = progressTime - totalRequiredTime;
			progressTime = totalRequiredTime;
			return overflow;
		}
		return 0;
	}

	public Image createMatureTreeImage()
	{
		return new Image(matureTreeSprite);
	}

	public Image createDeadTreeImage()
	{
		return new Image(deadTreeSprite);
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
		return progressTime;
	}

	public Integer getRemainingRequiredTime()
	{
		return totalRequiredTime - progressTime;
	}

	public boolean isUnlocked()
	{
		return progressTime.equals(totalRequiredTime);
	}

}
