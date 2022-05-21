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
	private Tag selectedTag;
	private Tag unsetTag;

	private TagHandler()
	{
		loadTags();
	}

	private void loadTags()
	{
		// todo: get saved tags from database
		createUnsetTag();
		debugLoad();

	}

	private void createUnsetTag()
	{
		// todo: translate
		addTag("Unset", Color.GRAY, 0);
		this.unsetTag = tags.get(0);
		setSelectedTag(this.unsetTag);
	}

	private void debugLoad()
	{
		addTag("Studio Basi di Dati", Color.RED, 1);
		addTag("Esercitazione Ukulele", Color.GREEN, 2);
		addTag("Allenamento", Color.LIGHTCYAN, 3);
		addTag("Studio Fisica", Color.BROWN, 4);
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
//			Feedback.getInstance().showError("Error", "Tried to remove a tag that doesn't exists");
			Feedback.getInstance().showError("Errore di rimozione", "Hai provato a rimuovere una tag che non esiste");
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
			// "Tag error", "Can't delete the tag that is being used in an activity"
			if (ActivityHandler.getInstance().isActivityStarted())
			{
				Feedback.getInstance().showError("Errore di rimozione",
						"Non è possibile rimuovere una tag mentre viene usata in una attività");
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
		else if (ActivityHandler.getInstance().isActivityStarted() && uuid.equals(selectedTag.getUuid()))
		{
			Feedback.getInstance().showError("Errore di modifica",
					"Non è possibile modificare una tag mentre è usata in una attività");
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
