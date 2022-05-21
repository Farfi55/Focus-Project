package mafiadelprimobanco.focusproject.model;

public interface TagsObserver
{
	default void onTagAdded(Tag tag) { }

	default void onTagRemoving(Tag tag) { }

	default void onTagChanged(Tag tag) { }

	default void onTagSelected(Tag tag) { }
}
