package mafiadelprimobanco.focusproject;

import javafx.beans.property.SimpleObjectProperty;
import mafiadelprimobanco.focusproject.model.ActivityObserver;
import mafiadelprimobanco.focusproject.model.TreeProgress;
import mafiadelprimobanco.focusproject.model.TreeTemplate;

import java.util.HashMap;
import java.util.HashSet;

public class TreeProgressHandler implements ActivityObserver
{
	private static final TreeProgressHandler instance = new TreeProgressHandler();

	public static TreeProgressHandler getInstance()
	{
		return instance;
	}

	private final SimpleObjectProperty<TreeProgress> selectedTreeToUnlock = new SimpleObjectProperty<>();
	HashMap<Integer, TreeProgress> trees = new HashMap<>();
	HashSet<Integer> treesToUnlock = new HashSet<>();
	HashSet<Integer> unlockedTrees = new HashSet<>();

	private TreeProgressHandler()
	{
		loadTrees();
		ActivityHandler.getInstance().addListener(this);
	}

	@Override
	public void onActivityEndSafe()
	{
		int duration = ActivityHandler.getInstance().getCurrentActivity().getFinalDuration();
		addProgress(duration);
	}

	private void loadTrees()
	{
		// todo: implement using database


// 		example for how we'll implement it
//		while(true)
//		{
//			Integer uuid = ...;
//			Integer requiredTime = ...;
//			Integer progressTime = ...;
//
//			var tree = new TreeProgress(uuid, requiredTime, progressTime);
//			trees.put(uuid, tree);
//			if(tree.isUnlocked()) unlockedTrees.add(tree.getTreeUuid());
//			else treesToUnlock.add(tree.getTreeUuid());
//		}


		trees.put(0, new TreeProgress(0, 0, 0));
		trees.put(1, new TreeProgress(1, 600, 235));
		trees.put(2, new TreeProgress(2, 1200, 1200));
		trees.put(3, new TreeProgress(3, 600, 0));

		for (TreeProgress tree : trees.values())
		{
			if (tree.isUnlocked()) unlockedTrees.add(tree.getTreeUuid());
			else treesToUnlock.add(tree.getTreeUuid());
		}
	}

	private void addProgress(int seconds)
	{
		assert seconds >= 0;
		if (treesToUnlock.isEmpty())
		{
			Feedback.getInstance().showNotification("Nessun albero da sbloccare",
					"Non è rimasto nessun albero da sbloccare.");
			return;
		}
		else if (selectedTreeToUnlock.get() == null)
		{
			selectedTreeToUnlock.set(getFirstTreeToUnlock());
			Feedback.getInstance().showNotification("Nessun albero da sbloccare selezionato",
					"Non è stato selezionato un albero al quale aggiungere il tempo impiegato nell'attività"
							+ "\nIl tempo verrà impiegato per l'albero " + selectedTreeToUnlock.get().getTree());

		}

		int overflow = selectedTreeToUnlock.get().addTime(seconds);

		if (selectedTreeToUnlock.get().isUnlocked())
		{
			treesToUnlock.remove(selectedTreeToUnlock.get().getTreeUuid());
			TreeProgress newSelectedTree = getFirstTreeToUnlock();

			String feedbackMessage =
					"Complimenti!\nHai sbloccato l'albero '" + selectedTreeToUnlock.get().getTree() + "'";
			if (newSelectedTree != null)
			{
				feedbackMessage += "\nè stato automaticamente selezionato l'albero '" + newSelectedTree.getTree()
						+ "'.\nPuoi sceglierne uno nuovo in qualsiasi momento dalla pagina progressi.";
			}

			Feedback.getInstance().showNotification("Albero Sbloccato!", feedbackMessage);
			selectedTreeToUnlock.set(newSelectedTree);
			if (overflow > 0 && newSelectedTree != null) addProgress(overflow);
		}
		else
		{
			Feedback.getInstance().showNotification("Report progressi albero",
					"Aggiunti " + seconds + " secondi all'albero '" + selectedTreeToUnlock.get().getTree() + "'"
							+ "\nMancano solo altri " + selectedTreeToUnlock.get().getRemainingRequiredTime()
							+ " secondi per sbloccarlo!");
		}

	}

	public HashMap<Integer, TreeProgress> getTrees()
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

	public SimpleObjectProperty<TreeProgress> getSelectedTreeToUnlockProperty()
	{
		return selectedTreeToUnlock;
	}

	public TreeProgress getSelectedTreeToUnlock()
	{
		return selectedTreeToUnlock.get();
	}

	public void setSelectedTreeToUnlock(TreeProgress tree) { setSelectedTreeToUnlock(tree.getTreeUuid()); }

	public void setSelectedTreeToUnlock(TreeTemplate tree) { setSelectedTreeToUnlock(tree.uuid()); }

	public void setSelectedTreeToUnlock(Integer uuid)
	{
		if (treesToUnlock.contains(uuid) || !trees.containsKey(uuid))
		{
			System.out.println("there is no tree with uuid " + uuid + " to unlock");
			return;
		}
		else
		{
			this.selectedTreeToUnlock.set(trees.get(uuid));
		}
	}

	public TreeProgress getFirstTreeToUnlock()
	{
		return trees.get(treesToUnlock.iterator().next());
	}

	public TreeProgress getFirstUnlockedTree()
	{
		return trees.get(unlockedTrees.iterator().next());
	}

}




















