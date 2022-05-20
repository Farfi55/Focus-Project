package mafiadelprimobanco.focusproject;

import javafx.scene.paint.Color;
import mafiadelprimobanco.focusproject.model.Tag;
import mafiadelprimobanco.focusproject.model.TagsObserver;

import java.util.*;

import static io.github.palexdev.materialfx.utils.ColorUtils.getRandomColor;

public class TagHandler
{
	private static final TagHandler instance = new TagHandler();

	public static TagHandler getInstance() { return instance; }

	private final Map<Integer, Tag> tags = new HashMap<>();
	private final HashSet<String> names = new HashSet<>();
	private final List<TagsObserver> listeners = new ArrayList<>();
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

	public void addTag()
	{
		String uniqueName = getUniqueName();
		Color randomColor = getRandomColor();
		addTag(uniqueName, randomColor);
	}

	private String getUniqueName()
	{
		String uniqueName = "New tag #";
		int i = 1;
		for (;isNameUsed(uniqueName + i); i++);

		uniqueName += Integer.toString(i);
		return uniqueName;
	}

	private boolean addTag(String name, Color color, Integer uuid)
	{
		if (isNameUsed(name)) return false;
		var tag = new Tag(name, color, uuid);
		tags.put(uuid, tag);
		names.add(name);
		invokeOnTagAdded(tag);
		return true;
	}

	private boolean isNameUsed(String name)
	{
		return names.contains(name);
	}

	public boolean removeTag(Integer uuid)
	{
		if (!tags.containsKey(uuid))
		{
			Feedback.getInstance().showError("Error", "Tried to remove a tag that doesn't exists");
			return false;
		}

		Tag tag = tags.get(uuid);
		invokeOnTagRemoving(tag);
		names.remove(tag.getName());
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

		if (isNameUsed(name)) return false;
		var tag = tags.get(uuid);

		if(!name.equals(tag.getName()))
		{
			names.remove(tag.getName());
			tag.setName(name);
			names.add(tag.getName());
		}
		tag.setColor(color);
		invokeOnTagChanged(tag);
		return true;
	}

	public boolean addListener(TagsObserver tagsObserver)
	{
		return listeners.add(tagsObserver);
	}

	public boolean removeListener(TagsObserver tagsObserver)
	{
		return listeners.remove(tagsObserver);
	}

	private void invokeOnTagAdded(Tag tag)
	{
		for (TagsObserver observer : listeners)
			observer.onTagAdded(tag);
	}

	private void invokeOnTagRemoving(Tag tag)
	{
		for (TagsObserver observer : listeners)
			observer.onTagRemoving(tag);
	}

	private void invokeOnTagChanged(Tag tag)
	{
		for (TagsObserver observer : listeners)
			observer.onTagChanged(tag);
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
