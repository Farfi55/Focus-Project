package mafiadelprimobanco.focusproject.controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXProgressBar;
import io.github.palexdev.materialfx.factories.InsetsFactory;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import mafiadelprimobanco.focusproject.handler.TreeHandler;
import mafiadelprimobanco.focusproject.model.Tree;
import mafiadelprimobanco.focusproject.utils.ResourcesLoader;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;
import java.util.stream.Stream;

public class ProgressPageController implements Controller
{

	private final int treesShownInSelectionHBox = 3;
	@FXML private HBox treeSelectionHBox;
	@FXML private GridPane toUnlockTreeDetailsGrid;
	@FXML private GridPane unlockedTreeDetailsGrid;
	@FXML private MFXComboBox<String> intervalComboBox;
	@FXML private GridPane treeGrid;
	private SimpleObjectProperty<Tree> selectedPreviewTree = new SimpleObjectProperty<>(this, "selectedPreviewTree");

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		initializeTreeSelectionHBox();
		initializeUnlockedTreeDetailGrid();
		initializeToUnlockTreeDetailGrid();
		initializeIntervalComboBox();
		initializeTreeGrid();
	}

	@Override
	public void terminate()
	{

	}

	private void initializeTreeSelectionHBox()
	{
		Stream<Tree> treeStream = TreeHandler.getInstance().getTrees().stream().sorted(
				Comparator.comparing(Tree::getUnlockProgress).thenComparing(Tree::getUuid));

		treeStream.forEachOrdered(tree -> treeSelectionHBox.getChildren().add(buildTreeSelectionCard(tree)));
	}

	private Node buildTreeSelectionCard(Tree tree)
	{
		ImageView treeImageView = new ImageView(ResourcesLoader.loadImage(tree.getMatureTreeSprite()));
		treeImageView.setFitWidth(80);
		treeImageView.setFitHeight(80);

		Label treeNameLabel = new Label(tree.getName());
		treeNameLabel.setAlignment(Pos.CENTER);


		MFXProgressBar unlockProgressBar = new MFXProgressBar(tree.getUnlockProgress());
		tree.progressTimeProperty().addListener(observable ->
		{
			unlockProgressBar.setProgress(tree.getUnlockProgress());
		});


		VBox vBox = new VBox(6, treeImageView, treeNameLabel, unlockProgressBar);
		vBox.setFillWidth(true);

		vBox.setPadding(InsetsFactory.all(5));

		MFXButton button = new MFXButton();
		button.setGraphic(vBox);
		button.setOnAction(event ->
		{
			setSelectedPreviewTree(tree);
		});
		return button;
	}

	private void initializeUnlockedTreeDetailGrid()
	{

	}

	private void initializeToUnlockTreeDetailGrid()
	{

	}

	private void initializeIntervalComboBox()
	{

	}

	private void initializeTreeGrid()
	{

	}

	@FXML
	void onTreeSelectNextAction(ActionEvent event)
	{

	}

	@FXML
	void onTreeSelectPreviousAction(ActionEvent event)
	{

	}

	private void updateTreeSelectionHBoxItems()
	{

	}

	private Tree getSelectedPreviewTree()
	{
		return selectedPreviewTree.get();
	}

	private void setSelectedPreviewTree(Tree selectedPreviewTree)
	{
		this.selectedPreviewTree.set(selectedPreviewTree);
		updateTreeSelectionHBoxItems();

	}
}
