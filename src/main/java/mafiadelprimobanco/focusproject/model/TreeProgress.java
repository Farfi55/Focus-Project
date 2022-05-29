package mafiadelprimobanco.focusproject.model;

import mafiadelprimobanco.focusproject.TreesHandler;

public class TreeProgress
{

	private final Integer treeUuid;

	/**
	 * time in seconds required to unlock this tree
	 */
	private final Integer totalRequiredTime;
	/**
	 * time put in to unlock this tree
	 */
	private Integer progressTime;

	public TreeProgress(Integer treeUuid, Integer totalRequiredTime, Integer progressTime)
	{
		this.treeUuid = treeUuid;
		this.totalRequiredTime = totalRequiredTime;
		this.progressTime = progressTime;
	}

	public TreeProgress(Integer treeUuid, Integer totalRequiredTime)
	{
		this.treeUuid = treeUuid;
		this.totalRequiredTime = totalRequiredTime;
		this.progressTime = 0;
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

	public boolean isUnlocked()
	{
		return progressTime.equals(totalRequiredTime);
	}

	public TreeTemplate getTree()
	{
		return TreesHandler.getInstance().getTree(treeUuid);
	}
	public String getTreeName()
	{
		return TreesHandler.getInstance().getTree(treeUuid).name();
	}

	public Integer getTreeUuid()
	{
		return treeUuid;
	}

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
}
