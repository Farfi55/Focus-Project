package mafiadelprimobanco.focusproject.handler;

import javafx.scene.paint.Color;
import mafiadelprimobanco.focusproject.model.Tag;
import mafiadelprimobanco.focusproject.model.TagsObserver;

import java.util.*;

import static io.github.palexdev.materialfx.utils.ColorUtils.getRandomColor;

public class TagHandler
{
	private static final TagHandler instance = new TagHandler();

	public static TagHandler getInstance() { return instance; }

	private final TreeMap<Integer, Tag> tags = new TreeMap<>();
	private final HashSet<String> names = new HashSet<>();
	private final List<TagsObserver> listeners = new ArrayList<>();
	private final Random rand = new Random();
	private Tag selectedTag;
	private Tag unsetTag;

	private TagHandler() { }

	public void loadTags()
	{
		createUnsetTag();
		JsonHandler.loadTags();
	}

	private void createUnsetTag()
	{
		addTag("Unset", Color.GRAY, 0);
		this.unsetTag = tags.get(0);
		setSelectedTag(this.unsetTag);
	}

	public boolean addTag(String name, Color color)
	{
		int uuid = rand.nextInt();
		while (uuid <= 0 || tags.containsKey(uuid)) uuid = rand.nextInt();
		return addTag(name, color, uuid);
	}

	public void addTag()
	{
		String uniqueName = getUniqueName();
		Color randomColor = getRandomColor();
		addTag(uniqueName, randomColor);
	}

	public boolean addTag(String name, Color color, Integer uuid)
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
			System.err.println("Tried to remove a tag that doesn't exists");
			return false;
		}
		else if (uuid == 0)
		{
			System.err.println("cant remove the 'unset' tag");
			return false;
		}


		Tag tag = tags.get(uuid);
		if (tag.equals(selectedTag))
		{
			if (ActivityHandler.getInstance().isActivityRunning())
			{
				System.err.println("can't remove a tag while its used in an activity");
				return false;
			}
			else setSelectedTag(unsetTag);
		}
		invokeOnTagRemoving(tag);
		names.remove(tag.getName());
		tags.remove(uuid);
		return true;
	}

	public boolean changeTag(String name, Color color, Integer uuid)
	{
		if (!tags.containsKey(uuid))
		{
			System.err.println("no tag with (name: " + name + ", uuid " + uuid + ") found");
			return false;
		}
		else if (uuid == 0)
		{
			System.err.println("cant change the 'unset' tag");
			return false;
		}
		else if (ActivityHandler.getInstance().isActivityRunning() && uuid.equals(selectedTag.getUuid()))
		{
			Feedback.getInstance().showError(Localization.get("error.tags.changeDuringActivity.header"),
					Localization.get("error.tags.changeDuringActivity.message"));
			return false;
		}

		var tag = tags.get(uuid);

		if (!name.equals(tag.getName()))
		{
			if (isNameUsed(name))
			{
				Feedback.getInstance().showError(Localization.get("error.tags.nameAlreadyUsed.header"),
						Localization.get("error.tags.nameAlreadyUsed.message", name));
				return false;
			}
			else if (name.isEmpty())
			{
				Feedback.getInstance().showError(Localization.get("error.tags.emptyName.header"),
						Localization.get("error.tags.emptyName.message"));
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

	public boolean setSelectedTag(Tag tag)
	{
		if (ActivityHandler.getInstance().isActivityRunning())
		{

			Feedback.getInstance().showError("Errore di Selezione",
					"Non è possibile selezionare un tag durante un'attività");
			return false;
		}
		this.selectedTag = tag;
		invokeOnTagSelected(tag);
		return false;
	}

	public Tag getSelectedTag()
	{
		return selectedTag;
	}

	private String getUniqueName()
	{
		String uniqueName = "New tag #";
		int i = 1;
		while (isNameUsed(uniqueName + i)) i++;
		return uniqueName + i;

	}

	public Integer getRandomTagUuid()
	{
		List<Integer> tagsUuid = tags.keySet().stream().toList();
		return tagsUuid.get(rand.nextInt(tagsUuid.size()));
	}

	public Tag getRandomTag()
	{
		return tags.get(getRandomTagUuid());
	}

	public Collection<Tag> getTags()
	{
		return Collections.unmodifiableCollection(tags.values());
	}

}
