package mafiadelprimobanco.focusproject.model;

public class TreeProgress
{

	private final TreeTemplate tree;
	/**
	 * time in seconds required to unlock this tree
	 */
	private final Integer totalRequiredTime;
	/**
	 * time put in to unlock this tree
	 */
	private Integer progressTime;

	public TreeProgress(TreeTemplate tree, Integer totalRequiredTime, Integer progressTime)
	{
		this.tree = tree;
		this.totalRequiredTime = totalRequiredTime;
		this.progressTime = progressTime;
	}

	public TreeProgress(TreeTemplate tree, Integer totalRequiredTime)
	{
		this.tree = tree;
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
		return tree;
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
