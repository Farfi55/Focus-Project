package mafiadelprimobanco.focusproject;

import javafx.scene.control.ToggleGroup;
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
	private Tag selectedTag;

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
			System.err.println("no tag with (name: " + name + ", uuid " + uuid + ") found");
			return false;
		}

		var tag = tags.get(uuid);

		if (!name.equals(tag.getName()))
		{
			if (isNameUsed(name))
			{
				System.err.println("tag name '" + name + "' already in use");
				Feedback.getInstance().showError("Nome già in uso", "Esiste già una tag con nome '" + name + "'.");
				return false;
			}
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

	private void invokeOnTagSelected(Tag tag)
	{
		for (TagsObserver observer : listeners)
			observer.onTagSelected(tag);
	}

	public final Tag getTag(Integer uuid)
	{
		return tags.get(uuid);
	}



	public Tag getSelectedTag()
	{
		return selectedTag;
	}

	public void setSelectedTag(Tag tag)
	{
		this.selectedTag = tag;
		invokeOnTagSelected(tag);
	}

	private String getUniqueName()
	{
		String uniqueName = "New tag #";
		int i = 1;
		while (isNameUsed(uniqueName + i)) i++;
		return uniqueName + i;

	}

	public Collection<Tag> getTags()
	{
		return Collections.unmodifiableCollection(tags.values());
	}

}
