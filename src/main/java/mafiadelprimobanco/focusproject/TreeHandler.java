package mafiadelprimobanco.focusproject;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import mafiadelprimobanco.focusproject.model.ActivityObserver;
import mafiadelprimobanco.focusproject.model.Tree;

import java.util.HashMap;
import java.util.HashSet;

public class TreeHandler implements ActivityObserver
{
	private static final TreeHandler instance = new TreeHandler();

	public static TreeHandler getInstance()
	{
		return instance;
	}

	private final SimpleObjectProperty<Tree> selectedTreeToUnlock = new SimpleObjectProperty<>();
	private final SimpleIntegerProperty unusedProgressTime = new SimpleIntegerProperty();
	HashMap<Integer, Tree> trees = new HashMap<>();
	HashSet<Integer> treesToUnlock = new HashSet<>();
	HashSet<Integer> unlockedTrees = new HashSet<>();



	private TreeHandler()
	{
		loadTrees();
		setSelectedTreeToUnlock(getFirstTreeToUnlock());
		ActivityHandler.getInstance().addListener(this);
	}


	@Override
	public void onActivityEnd()
	{
		int duration = ActivityHandler.getInstance().getCurrentActivity().getFinalDuration();
		addProgressTime(duration);
	}

	private void loadTrees()
	{
		// todo: implement using database


		String deadTree = "trees/testingDeadTree.png";
		trees.put(0, new Tree(0, "albero 1", "trees/tile000.png", deadTree, 0, 0));
		trees.put(1, new Tree(1, "albero 2", "trees/tile001.png", deadTree, 600, 200));
		trees.put(2, new Tree(2, "albero 3", "trees/tile007.png", deadTree, 1200, 1200));
		trees.put(3, new Tree(3, "albero 4", "trees/tile008.png", deadTree, 600, 0));

		for (Tree tree : trees.values())
		{
			if (tree.isUnlocked()) unlockedTrees.add(tree.getUuid());
			else treesToUnlock.add(tree.getUuid());
		}
	}


	private void addProgressTime(int seconds)
	{
		assert seconds >= 0;
		if (selectedTreeToUnlock.get().isUnlocked()) selectedTreeToUnlock.set(null);

		if (selectedTreeToUnlock.get() != null)
		{
			int overflow = selectedTreeToUnlock.get().addProgressTime(seconds);
			if (selectedTreeToUnlock.get().isUnlocked())
			{
				Feedback.getInstance().showNotification("Albero Sbloccato!",
						"Evviva!\nHai sbloccato l'albero '" + selectedTreeToUnlock.get().getName() + "'");

				treesToUnlock.remove(selectedTreeToUnlock.get().getUuid());
			}
			unusedProgressTime.add(overflow);
		}
		else unusedProgressTime.add(seconds);
	}


	public Tree getTree(int uuid)
	{
		return trees.get(uuid);
	}

	public SimpleIntegerProperty getUnusedProgressTimeProperty()
	{
		return unusedProgressTime;
	}

	public int getUnusedProgressTime()
	{
		return unusedProgressTime.get();
	}

	public HashMap<Integer, Tree> getTrees()
	{
		return trees;
	}

	public HashSet<Integer> getTreesToUnlock()
	{
		return treesToUnlock;
	}

	public HashSet<Integer> getUnlockedTrees()
	{
		return unlockedTrees;
	}

	public SimpleObjectProperty<Tree> getSelectedTreeToUnlockProperty()
	{
		return selectedTreeToUnlock;
	}

	public Tree getSelectedTreeToUnlock()
	{
		return selectedTreeToUnlock.get();
	}

	public void setSelectedTreeToUnlock(Integer uuid) { setSelectedTreeToUnlock(trees.get(uuid)); }

	public void setSelectedTreeToUnlock(Tree tree)
	{
		if (!treesToUnlock.contains(tree.getUuid()) || !trees.containsKey(tree.getUuid())) System.out.println(
				"there is no tree '" + tree.getName() + "' to unlock");
		else this.selectedTreeToUnlock.set(tree);
	}

	public Tree getFirstTreeToUnlock()
	{
		return trees.get(treesToUnlock.iterator().next());
	}

	public Tree getFirstUnlockedTree()
	{
		return trees.get(unlockedTrees.iterator().next());
	}

}
