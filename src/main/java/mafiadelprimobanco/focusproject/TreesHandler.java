package mafiadelprimobanco.focusproject;

import mafiadelprimobanco.focusproject.model.TreeTemplate;

import java.util.Collection;
import java.util.HashMap;

public class TreesHandler
{
	private static final TreesHandler instance = new TreesHandler();

	public static TreesHandler getInstance()
	{
		return instance;
	}

	private HashMap<Integer, TreeTemplate> trees = new HashMap<>();

	public TreesHandler()
	{
		loadTrees();
	}

	private void loadTrees()
	{
		// TESTING ONLY
		String deadTree = "trees/testingDeadTree.png";
		trees.put(0, new TreeTemplate(0, "albero 1", "trees/tile000.png", deadTree));
		trees.put(1, new TreeTemplate(1, "albero 2", "trees/tile001.png", deadTree));
		trees.put(2, new TreeTemplate(2, "albero 3", "trees/tile007.png", deadTree));
		trees.put(3, new TreeTemplate(3, "albero 4", "trees/tile008.png", deadTree));
	}

	public Collection<TreeTemplate> getTrees()
	{
		return trees.values();
	}

	public TreeTemplate getTree(int uuid)
	{
		return trees.get(uuid);
	}

}
