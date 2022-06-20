package mafiadelprimobanco.focusproject.handler;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import mafiadelprimobanco.focusproject.model.ActivityObserver;
import mafiadelprimobanco.focusproject.model.Tree;
import mafiadelprimobanco.focusproject.model.activity.AbstractActivity;
import mafiadelprimobanco.focusproject.utils.ResourcesLoader;
import mafiadelprimobanco.focusproject.utils.TimeUtils;
import org.json.JSONObject;

import java.util.*;

public class TreeHandler implements ActivityObserver
{
	private static final TreeHandler instance = new TreeHandler();

	public static TreeHandler getInstance()
	{
		return instance;
	}

	private final SimpleObjectProperty<Tree> selectedTreeToUnlock = new SimpleObjectProperty<>();
	private final SimpleObjectProperty<Tree> selectedActivityTree = new SimpleObjectProperty<>();
	private final SimpleIntegerProperty unusedProgressTime = new SimpleIntegerProperty();
	private final List<Image> treePhasesImages = new ArrayList<>(5);
	LinkedHashMap<Integer, Tree> trees = new LinkedHashMap<>();
	LinkedHashSet<Integer> treesToUnlock = new LinkedHashSet<>();
	LinkedHashSet<Integer> unlockedTrees = new LinkedHashSet<>();

	private TreeHandler()
	{
		loadTrees();
		setSelectedTreeToUnlock(getFirstTreeToUnlock());
		setSelectedActivityTree(getFirstUnlockedTree());
		loadTreePhasesImages();
		ActivityHandler.getInstance().addListener(this);
	}


	@Override
	public void onActivityEnd(AbstractActivity currentActivity)
	{
		addProgressTime(currentActivity.getFinalDuration());
		addUnusedProgressTime();
	}

	public SimpleObjectProperty<Tree> selectedActivityTreeProperty()
	{
		return selectedActivityTree;
	}

	public Image getTreePhaseImage(int index)
	{
		if (index < treePhasesImages.size()) return treePhasesImages.get(index);
		else return null;
	}

	private void loadTreePhasesImages()
	{
		for (int i = 0; i < 5; i++)
		{
			treePhasesImages.add(ResourcesLoader.loadImage("trees/treePhase" + i + ".png"));
		}
	}

	private void loadTrees()
	{
		String deadTree = "trees/deadTree.png";
		trees.put(0, new Tree(0, "Quercus Alba", "trees/tile000.png", deadTree, 0, 0));
		trees.put(1, new Tree(1, "Salix Babylonica", "trees/tile001.png", deadTree, 300, 0));
		trees.put(2, new Tree(2, "Acacia Dealbata", "trees/tile002.png", deadTree, 600, 0));
		trees.put(3, new Tree(3, "Picea Abies", "trees/tile003.png", deadTree, 900, 0));
		trees.put(4, new Tree(4, "Betula Lenta", "trees/tile004.png", deadTree, 1200, 0));
		trees.put(5, new Tree(5, "Malus Domestica", "trees/tile005.png", deadTree, 1500, 0));
		trees.put(6, new Tree(6, "Silvestris Rubi", "trees/tile006.png", deadTree, 1800, 0));
		trees.put(7, new Tree(7, "Prunus Serrulata", "trees/tile007.png", deadTree, 2100, 0));
		trees.put(8, new Tree(8, "Arbor Natalis", "trees/tile008.png", deadTree, 2400, 0));

		for (Tree tree : trees.values())
		{
			if (tree.isUnlocked()) unlockedTrees.add(tree.getUuid());
			else treesToUnlock.add(tree.getUuid());
		}
	}

	public void loadFromJson(JSONObject treeJsonObj)
	{
		//selectedTreeToUnlock.set(treeJsonObj.getInt());

		var treesList  = treeJsonObj.getJSONObject("treesList");
		treesList.keys().forEachRemaining(treeUUID -> {
			treesList.getString(treeUUID)
		});

	}

	public JSONObject toJson()
	{
		JSONObject treeJsonObj = new JSONObject();

		treeJsonObj.put("selectedTreeToUnlockUuid", selectedTreeToUnlock.get().getUuid());

		JSONObject treesList = new JSONObject();
		for (Tree tree : trees.values())
		{
			treesList.put(String.valueOf(tree.getUuid()), tree.toJson());
		}

		treeJsonObj.put("treesList", treesList);

		return treeJsonObj;
	}

	private void addProgressTime(int seconds)
	{
		assert seconds >= 0;


		if (!isValidSelectedTreeToUnlock()) selectedTreeToUnlock.set(null);

		if (selectedTreeToUnlock.get() != null)
		{

			int overflow = selectedTreeToUnlock.get().addProgressTime(seconds);
			if (selectedTreeToUnlock.get().isUnlocked())
			{
				UnlockSelectedTreeToUnlock();
			}
			unusedProgressTime.setValue(unusedProgressTime.getValue() + overflow);
		}
		else unusedProgressTime.setValue(unusedProgressTime.getValue() + seconds);
	}


	public void addUnusedProgressTime()
	{
		int unusedTime = unusedProgressTime.get();
		if (unusedTime > 0 && isValidSelectedTreeToUnlock())
		{
			unusedProgressTime.set(0);
			addProgressTime(unusedTime);

			Feedback.getInstance().showNotification(Localization.get("feedback.usedUnusedProgressTime.header"),
					Localization.get("feedback.usedUnusedProgressTime.message", TimeUtils.formatTime(unusedTime),
							TimeUtils.formatTime(unusedProgressTime.get())));
		}
	}


	public boolean isValidSelectedTreeToUnlock()
	{
		return selectedTreeToUnlock.get() != null && selectedTreeToUnlock.get().isNotUnlocked();
	}

	private void UnlockSelectedTreeToUnlock()
	{
		treesToUnlock.remove(selectedTreeToUnlock.get().getUuid());
		unlockedTrees.add(selectedTreeToUnlock.get().getUuid());
		assert !isValidSelectedTreeToUnlock();

		Platform.runLater(() -> Feedback.getInstance()
				.showNotification("Albero Sbloccato!",
						"Evviva!\nHai sbloccato l'albero '" + selectedTreeToUnlock.get().getName() + "'"));
	}

	public Tree getTree(int uuid)
	{
		return trees.get(uuid);
	}

	public Tree getSelectedActivityTree()
	{
		return selectedActivityTree.get();
	}

	public void setSelectedActivityTree(Tree selectedActivityTree)
	{
		if (selectedActivityTree == null || selectedActivityTree.isUnlocked()) this.selectedActivityTree.set(
				selectedActivityTree);
	}


	public Integer getRandomUnlockedTreeUuid()
	{
		List<Integer> unlockedTreeList = unlockedTrees.stream().toList();
		return unlockedTreeList.get(new Random().nextInt(unlockedTreeList.size()));
	}

	public Tree getRandomUnlockedTree()
	{
		return trees.get(getRandomUnlockedTreeUuid());
	}

	public SimpleIntegerProperty getUnusedProgressTimeProperty()
	{
		return unusedProgressTime;
	}

	public int getUnusedProgressTime()
	{
		return unusedProgressTime.get();
	}

	public LinkedHashMap<Integer, Tree> getTreesMap()
	{
		return trees;
	}

	public Collection<Tree> getTrees()
	{
		return trees.values();
	}

	public LinkedHashSet<Integer> getTreesToUnlock()
	{
		return treesToUnlock;
	}

	public LinkedHashSet<Integer> getUnlockedTrees()
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
		if (!treesToUnlock.contains(tree.getUuid()) || !trees.containsKey(tree.getUuid())) System.err.println(
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
