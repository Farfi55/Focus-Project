package mafiadelprimobanco.focusproject.model;

import io.github.palexdev.materialfx.beans.Alignment;
import io.github.palexdev.materialfx.controls.MFXPopup;
import io.github.palexdev.materialfx.controls.MFXRectangleToggleNode;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.materialfx.factories.InsetsFactory;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import mafiadelprimobanco.focusproject.*;
import mafiadelprimobanco.focusproject.handler.Feedback;
import mafiadelprimobanco.focusproject.handler.StyleHandler;
import mafiadelprimobanco.focusproject.handler.TreeHandler;
import mafiadelprimobanco.focusproject.utils.ResourcesLoader;

import java.util.HashMap;
import java.util.Map;

public class TreeChooserPopup extends MFXPopup
{
	private GridPane treesPane;

	private ToggleGroup toggleGroup;

	private final Map<Tree, MFXRectangleToggleNode> treeButtons = new HashMap<>();

	public TreeChooserPopup()
	{
		super();
		initialize();
	}

	private void initialize()
	{
		setId("treeChooserPopup");
		toggleGroup = new ToggleGroup();
		treesPane = new GridPane();
		treesPane.setHgap(5);
		treesPane.setVgap(5);
		MFXScrollPane scrollPane = new MFXScrollPane(treesPane);
		scrollPane.setMaxSize(400, 400);
		scrollPane.setPadding(InsetsFactory.of(5, 5, 5, 5));
		scrollPane.setFitToWidth(true);
		createTreeButtons();
		setContent(scrollPane);
//		setAutoHide(false);
		StyleHandler.getInstance().getObservableStyles().addListener(
				(ListChangeListener<? super String>)c -> setStyleSheets());
		setStyleSheets();
	}

	@Override
	public void show(Node ownerNode, double anchorX, double anchorY)
	{
		super.show(ownerNode, anchorX, anchorY);
		updateTreeButtons();
	}

	@Override
	public void show(Node node)
	{
		super.show(node);
		updateTreeButtons();
	}

	@Override
	public void show(Node node, Alignment alignment)
	{
		super.show(node, alignment);
		updateTreeButtons();
	}

	@Override
	public void show(Node node, Alignment alignment, double xOffset, double yOffset)
	{
		super.show(node, alignment, xOffset, yOffset);
		updateTreeButtons();
	}

	private void setStyleSheets()
	{
		getStyleSheets().setAll(StyleHandler.getInstance().getObservableStyles());
	}

	private void createTreeButtons()
	{
		int i = 0, j = 0;
		for (Tree tree : TreeHandler.getInstance().getTrees())
		{
			MFXRectangleToggleNode button = buildTreeButton(tree);
			treeButtons.put(tree, button);
			treesPane.add(button, i++, j);
			System.out.println(treesPane.getColumnCount());
			if (i == 3)
			{
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
			button.setDisable(!tree.isUnlocked());
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
		treeLabel.setMaxSize(-1, -1);
		treeLabel.setMinSize(-1, -1);
		treeLabel.setPrefSize(100, 110);
		treeLabel.setPadding(Insets.EMPTY);
		treeLabel.setAlignment(Pos.CENTER);


		treeLabel.setContentDisplay(ContentDisplay.TOP);
		treeLabel.setGraphicTextGap(8);

		MFXRectangleToggleNode button = new MFXRectangleToggleNode();
		button.setGraphic(treeLabel);
		button.setToggleGroup(toggleGroup);
		button.setAlignment(Pos.CENTER);
		button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
		button.setMaxSize(-1, -1);
		button.setMinSize(-1, -1);
		button.setPrefSize(120, 120);
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
