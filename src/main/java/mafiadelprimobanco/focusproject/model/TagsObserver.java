package mafiadelprimobanco.focusproject.model;

public interface TagsObserver
{
	void onTagAdded(Tag tag);

	void onTagRemoving(Tag tag);

	void onTagChanged(Tag tag);
}
