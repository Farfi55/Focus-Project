package mafiadelprimobanco.focusproject.model.activity;

import mafiadelprimobanco.focusproject.JsonHandler;
import mafiadelprimobanco.focusproject.TagHandler;
import mafiadelprimobanco.focusproject.TreeHandler;
import mafiadelprimobanco.focusproject.model.Tag;
import mafiadelprimobanco.focusproject.model.Tree;
import org.json.JSONObject;

import java.time.LocalDateTime;

import static java.time.temporal.ChronoUnit.SECONDS;

public abstract class AbstractActivity implements Comparable<AbstractActivity>
{
	protected Integer tagUuid;
	protected Integer treeUuid;
	protected LocalDateTime startTime;
	protected LocalDateTime endTime;

	public AbstractActivity() { }

	public AbstractActivity(Integer tagUuid, Integer treeUuid, LocalDateTime startTime, LocalDateTime endTime)
	{
		assert (!endTime.isBefore(startTime));
		this.tagUuid = tagUuid;
		this.treeUuid = treeUuid;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	@Override
	public int compareTo(AbstractActivity o)
	{
		return startTime.compareTo(o.startTime);
	}

	public void startActivity()
	{
		if (!isRunning()) this.startTime = LocalDateTime.now();
	}

	public void endActivity()
	{
		if (isRunning()) this.endTime = LocalDateTime.now();
		JsonHandler.addFinishedActivity(this.startTime,this);
	}

	public boolean hasStarted()
	{
		return startTime != null;
	}

	public boolean hasEnded()
	{
		return endTime != null;
	}

	public abstract JSONObject toJsonObject();

	public Integer getTreeUuid()
	{
		return treeUuid;
	}

	public void setTreeUuid(Integer treeUuid)
	{
		this.treeUuid = treeUuid;
	}

	public Tree getTree()
	{
		return TreeHandler.getInstance().getTree(treeUuid);
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

	public Tag getTag()
	{
		return TagHandler.getInstance().getTag(tagUuid);
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
