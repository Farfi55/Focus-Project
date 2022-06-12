package mafiadelprimobanco.focusproject.utils;

import javafx.scene.Node;

public class NodeUtils
{
	public static void showNode(Node node) { setNodeVisible(node, true); }

	public static void hideNode(Node node) { setNodeVisible(node, false); }

	public static void setNodeVisible(Node node, boolean visible)
	{
		node.setVisible(visible);
		node.setManaged(visible);
	}
}
