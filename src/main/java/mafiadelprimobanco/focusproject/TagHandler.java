package mafiadelprimobanco.focusproject;

import javafx.scene.paint.Color;
import mafiadelprimobanco.focusproject.model.Tag;
import mafiadelprimobanco.focusproject.model.TagsObserver;

import java.util.*;

public class TagHandler
{
	private static final TagHandler instance = new TagHandler();

	public static TagHandler getInstance() { return instance; }

	private final Map<Integer, Tag> tags = new HashMap<>();
	private final List<TagsObserver> observers = new ArrayList<>();
	private final Random rand = new Random();

	private TagHandler()
	{
		loadTags();
	}

	void loadTags()
	{
		// todo: get saved tags from database
		debugLoad();

	}

	void debugLoad()
	{
		addTag("Studio Fisica", Color.BROWN, 0);
		addTag("Studio Basi di Dati", Color.RED, 1);
		addTag("Esercitazione Ukulele", Color.GREEN, 2);
		addTag("Allenamento", Color.LIGHTCYAN, 3);
	}

	public boolean addTag(String name, Color color)
	{
		int uuid = rand.nextInt();
		while (tags.containsKey(uuid)) uuid = rand.nextInt();

		return addTag(name, color, uuid);
	}

	private boolean addTag(String name, Color color, Integer uuid)
	{
		for (Tag tag : tags.values())
			if (tag.getName().equals(name))
			{
				// todo: handle with an exception or
				//  give some feedback directly with SceneHandler
				return false;
			}
		var tag = new Tag(name, color, uuid);
		tags.put(uuid, tag);
		onTagAdded(tag);
		return true;
	}

	public boolean removeTag(Integer uuid)
	{
		if (!tags.containsKey(uuid))
		{
			// todo: give feedback
			return false;
		}

		Tag tag = tags.get(uuid);
		onTagRemoving(tag);

		tags.remove(uuid);
		return true;
	}

	public boolean updateTag(String name, Color color, Integer uuid)
	{
		if (!tags.containsKey(uuid))
		{
			// todo: give feedback
			return false;
		}

		for (Tag tag : tags.values())
			if (tag.getName().equals(name))
			{
				// todo: handle with an exception or
				//  give some feedback directly with SceneHandler
				return false;
			}
		var tag = tags.get(uuid);

		tag.setName(name);
		tag.setColor(color);
		onTagChanged(tag);
		return true;
	}

	public boolean addListener(TagsObserver tagsObserver)
	{
		return observers.add(tagsObserver);
	}

	public boolean removeListener(TagsObserver tagsObserver)
	{
		return observers.remove(tagsObserver);
	}

	private void onTagAdded(Tag tag)
	{
		for (TagsObserver tagsObserver : observers)
			tagsObserver.onTagAdded(tag);
	}

	private void onTagRemoving(Tag tag)
	{
		for (TagsObserver tagsObserver : observers)
			tagsObserver.onTagRemoving(tag);
	}

	private void onTagChanged(Tag tag)
	{
		for (TagsObserver tagsObserver : observers)
			tagsObserver.onTagChanged(tag);
	}

	public final Tag getTag(Integer uuid)
	{
		return tags.get(uuid);
	}

	public Collection<Tag> getTags()
	{
		return Collections.unmodifiableCollection(tags.values());
	}

}
