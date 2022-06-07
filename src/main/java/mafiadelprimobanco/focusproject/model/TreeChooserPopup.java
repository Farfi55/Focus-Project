package mafiadelprimobanco.focusproject.model;

import io.github.palexdev.materialfx.controls.MFXPopup;
import io.github.palexdev.materialfx.controls.MFXRectangleToggleNode;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import mafiadelprimobanco.focusproject.Feedback;
import mafiadelprimobanco.focusproject.Localization;
import mafiadelprimobanco.focusproject.ResourcesLoader;
import mafiadelprimobanco.focusproject.TreeHandler;

import java.util.HashMap;
import java.util.Map;

public class TreeChooserPopup extends MFXPopup
{
	private MFXScrollPane scrollPane;
	private GridPane treesPane;

	private Map<Tree, MFXRectangleToggleNode> treeButtons = new HashMap<>();

	public TreeChooserPopup()
	{
		super();
		initialize();
	}

	private void initialize()
	{
		setMaxSize(220, 220);
		treesPane = new GridPane();
		scrollPane = new MFXScrollPane(treesPane);
		createTreeButtons();
		setContent(scrollPane);
//		setAutoHide(false);
	}

	@Override
	public void show(Node node)
	{
		super.show(node);
		updateTreeButtons();
	}

	private void createTreeButtons()
	{
		int i = 0, j = 0;
		for (Tree tree : TreeHandler.getInstance().getTrees())
		{
			MFXRectangleToggleNode button = buildTreeButton(tree);
			treeButtons.put(tree, button);
			treesPane.add(button, i++, j);
			if(i == treesPane.getColumnCount()){
				i = 0;
				j++;
			}

		}
	}

	private void updateTreeButtons()
	{
		for (Tree tree : treeButtons.keySet())
		{
			MFXRectangleToggleNode button = treeButtons.get(tree);
			button.setDisable(tree.isUnlocked());
		}
		if (TreeHandler.getInstance().getSelectedActivityTree() != null) treeButtons.get(
				TreeHandler.getInstance().getSelectedActivityTree()).setSelected(true);
	}

	private MFXRectangleToggleNode buildTreeButton(Tree tree)
	{

		ImageView image = new ImageView(ResourcesLoader.loadImage(tree.getMatureTreeSprite()));
		image.setFitWidth(80);
		image.setFitHeight(80);

		Label treeLabel = new Label(tree.getName(), image);
		treeLabel.setContentDisplay(ContentDisplay.TOP);
		treeLabel.setGraphicTextGap(8);

		MFXRectangleToggleNode button = new MFXRectangleToggleNode("", treeLabel);
		button.setContentDisplay(ContentDisplay.CENTER);
		button.setPrefSize(100, 120);
		button.setOnAction(event ->
		{
			if (tree.isUnlocked())
			{
				TreeHandler.getInstance().setSelectedActivityTree(tree);
				hide();
			}
			else Feedback.getInstance().showNotification(Localization.get("error.tree.treeNotAvailable.header"),
					Localization.get("error.tree.treeNotAvailable.message"));
		});
		return button;
	}
}
