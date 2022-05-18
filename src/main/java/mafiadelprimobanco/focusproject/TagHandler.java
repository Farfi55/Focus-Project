package mafiadelprimobanco.focusproject;

import javafx.scene.paint.Color;
import mafiadelprimobanco.focusproject.model.Tag;
import mafiadelprimobanco.focusproject.model.TagsObserver;

import java.util.*;

public class TagHandler
{
	private static final TagHandler instance = new TagHandler();

	public static TagHandler getInstance() { return instance; }

	private Map<Integer, Tag> tags = new HashMap<>();
	private Random rand = new Random();

	private List<TagsObserver> observers = new ArrayList<>();


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

		tags.put(uuid, new Tag(name, color, uuid));
		// todo: call onTagsChanged event
		return true;
	}

	public boolean removeTag(Integer uuid)
	{
		if (!tags.containsKey(uuid))
		{
			// todo: give feedback
			return false;
		}

		tags.remove(uuid);
		// todo: call onTagsChanged event
		return true;
	}

	public boolean updateTag(String name, Color color, Integer uuid) {
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

		tags.get(uuid).setName(name);
		tags.get(uuid).setColor(color);
		// todo: call onTagsChanged event
		return true;

	}


	void addListener(TagsObserver tagsObserver)
	{

	}

	void removeListener(TagsObserver tagsObserver)
	{

	}

	void onTagsChanged()
	{

	}

}
