package mafiadelprimobanco.focusproject.controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXProgressBar;
import io.github.palexdev.materialfx.factories.InsetsFactory;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import mafiadelprimobanco.focusproject.Localization;
import mafiadelprimobanco.focusproject.handler.ActivityStatsHandler;
import mafiadelprimobanco.focusproject.handler.TreeHandler;
import mafiadelprimobanco.focusproject.model.Interval;
import mafiadelprimobanco.focusproject.model.Tree;
import mafiadelprimobanco.focusproject.model.activity.AbstractActivity;
import mafiadelprimobanco.focusproject.utils.LocalizationUtils;
import mafiadelprimobanco.focusproject.utils.NodeUtils;
import mafiadelprimobanco.focusproject.utils.ResourcesLoader;
import mafiadelprimobanco.focusproject.utils.TimeUtils;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Stream;

public class ProgressPageController implements Controller
{

	/**
	 * for each side how many trees will be rendered
	 * <p>for example, with 2:</p>
	 * <pre> X X T X X </pre>
	 * <p>with 1:</p>
	 * <pre> X T X </pre>
	 */
	private final int extraTreesShownInSelectionHBox = 2;
	private final LinkedHashMap<Tree, Node> previewSelectionTreesNodes = new LinkedHashMap<>();
	private final SimpleObjectProperty<Tree> selectedPreviewTree = new SimpleObjectProperty<>(this,
			"selectedPreviewTree");
	private final SimpleIntegerProperty selectedPreviewTreeIndex = new SimpleIntegerProperty(this,
			"selectedPreviewTreeIndex");
	@FXML private HBox treeSelectionHBox;
	@FXML private MFXButton treeSelectNextButton;
	@FXML private MFXButton treeSelectPreviousButton;
	@FXML private GridPane toUnlockTreeDetailsGrid;
	@FXML private GridPane unlockedTreeDetailsGrid;
	@FXML private MFXComboBox<Interval> intervalComboBox;
	@FXML private FlowPane treesRoot;


	private Label plantedTreesLabel;
	private Label matureTreesLabel;
	private Label deadTreesLabel;
	private Label lastTreeLabel;

	private Label treeRequiredTimeLabel;
	private Label treeProgressTimeLabel;
	private MFXProgressBar treeUnlockProgressBar;
	private MFXButton selectTreeToUnlockButton;

	private final SimpleObjectProperty<Interval> treesInterval = new SimpleObjectProperty<>(this, "treeGridInterval");


	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		initializeTreeSelectionHBox();
		initializeUnlockedTreeDetailGrid();
		initializeToUnlockTreeDetailGrid();

		initializeIntervalComboBox();
		initializeTreeFlow();
		setTreesInterval(Interval.DAY);

		if(TreeHandler.getInstance().getSelectedTreeToUnlock() != null)
			setSelectedPreviewTree(TreeHandler.getInstance().getSelectedTreeToUnlock());
		else
			setSelectedPreviewTree(extraTreesShownInSelectionHBox);

	}

	@Override
	public void terminate()
	{

	}

	private void initializeTreeSelectionHBox()
	{
		Stream<Tree> treeStream = TreeHandler.getInstance().getTrees().stream().sorted(Comparator.comparing(
				Tree::getUnlockProgress).reversed().thenComparing(Tree::getUuid));

		treeSelectionHBox.setSpacing(20);
		
		for (int i = 0; i < extraTreesShownInSelectionHBox; i++)
			treeSelectionHBox.getChildren().add(buildPlaceholderThreeSelectionCard());

		treeStream.forEachOrdered(tree ->
		{
			Node node = buildTreeSelectionCard(tree);
			previewSelectionTreesNodes.put(tree, node);
			treeSelectionHBox.getChildren().add(node);
		});

		for (int i = 0; i < extraTreesShownInSelectionHBox; i++)
			treeSelectionHBox.getChildren().add(buildPlaceholderThreeSelectionCard());

		treeSelectPreviousButton.disableProperty().bind(
				selectedPreviewTreeIndex.lessThanOrEqualTo(extraTreesShownInSelectionHBox));
		treeSelectNextButton.disableProperty().bind(selectedPreviewTreeIndex.greaterThanOrEqualTo(
				treeSelectionHBox.getChildren().size() - extraTreesShownInSelectionHBox - 1));
	}

	private Node buildPlaceholderThreeSelectionCard()
	{
		Region node = new Region();
		node.setMaxSize(100, 120);
		node.setMinSize(100, 120);
		node.setPrefSize(100, 120);
		return node;
	}

	private Node buildTreeSelectionCard(Tree tree)
	{
		ImageView treeImageView = new ImageView(ResourcesLoader.loadImage(tree.getMatureTreeSprite()));
		treeImageView.setFitWidth(80);
		treeImageView.setFitHeight(80);

		Label treeNameLabel = new Label(tree.getName());
		treeNameLabel.setAlignment(Pos.CENTER);
		treeNameLabel.setTextAlignment(TextAlignment.CENTER);


		MFXProgressBar unlockProgressBar = new MFXProgressBar(tree.getUnlockProgress());
		tree.progressTimeProperty().addListener(observable ->
		{
			unlockProgressBar.setProgress(tree.getUnlockProgress());
			unlockProgressBar.setVisible(!tree.isUnlocked());
		});
		unlockProgressBar.setMinHeight(8);
		unlockProgressBar.setVisible(!tree.isUnlocked());


		VBox vBox = new VBox(6, treeImageView, treeNameLabel, unlockProgressBar);
		vBox.setFillWidth(true);
		vBox.setPadding(InsetsFactory.all(5));
		vBox.setMaxSize(-1, -1);
		vBox.setMinSize(-1, -1);
		vBox.setPrefSize(100, 120);
		vBox.setAlignment(Pos.CENTER);

		MFXButton button = new MFXButton();
		button.setGraphic(vBox);
		button.setOnAction(event ->
		{
			setSelectedPreviewTree(tree);
		});
		button.setMaxSize(100, 120);
		button.setMinSize(100, 120);
		button.setPrefSize(100, 120);
		button.getProperties().put("tree-uuid", tree.getUuid());

		return button;
	}

	private void initializeUnlockedTreeDetailGrid()
	{
		int row = 0;
		for (var key : List.of("progress.plantedTrees", "progress.matureTrees", "progress.deadTrees",
				"progress.lastTree"))
		{
			Label label = new Label();
			LocalizationUtils.bindLabelText(label, key);
			unlockedTreeDetailsGrid.add(label, 0, row++);
		}
		plantedTreesLabel = new Label();
		matureTreesLabel = new Label();
		deadTreesLabel = new Label();
		lastTreeLabel = new Label();

		unlockedTreeDetailsGrid.addColumn(1, plantedTreesLabel, matureTreesLabel, deadTreesLabel, lastTreeLabel);
//		unlockedTreeDetailsGrid.setGridLinesVisible(true);
	}

	private void initializeToUnlockTreeDetailGrid()
	{
		int row = 0;
		for (var key : List.of("progress.treeRequiredTime", "progress.treeProgressTime"))
		{
			Label label = new Label();
			label.setAlignment(Pos.CENTER);
			LocalizationUtils.bindLabelText(label, key);
			toUnlockTreeDetailsGrid.add(label, 0, row);
			row++;
		}

		treeRequiredTimeLabel = new Label();
		treeProgressTimeLabel = new Label();
		treeUnlockProgressBar = new MFXProgressBar(0);
		selectTreeToUnlockButton = new MFXButton();

		LocalizationUtils.bindButtonText(selectTreeToUnlockButton, "progress.selectTreeButton");
		selectTreeToUnlockButton.disableProperty().bind(
				TreeHandler.getInstance().getSelectedTreeToUnlockProperty().isEqualTo(selectedPreviewTree));


		toUnlockTreeDetailsGrid.addColumn(1, treeRequiredTimeLabel, treeProgressTimeLabel);
		toUnlockTreeDetailsGrid.add(treeUnlockProgressBar, 0, row++, 2, 1);
		toUnlockTreeDetailsGrid.add(selectTreeToUnlockButton, 0, row, 2, 1);
		GridPane.setHalignment(treeUnlockProgressBar, HPos.CENTER);
		GridPane.setHalignment(selectTreeToUnlockButton, HPos.CENTER);
//		toUnlockTreeDetailsGrid.setGridLinesVisible(true);
	}

	private void initializeIntervalComboBox()
	{
		intervalComboBox.setOnAction(event -> setTreesInterval(intervalComboBox.getValue()));
		Localization.localeProperty().addListener(observable -> resetIntervalComboBox());

		resetIntervalComboBox();

	}

	private void setTreesInterval(Interval value)
	{
		if (value == null || value == treesInterval.getValue()) return;

		treesInterval.set(value);
		if(!intervalComboBox.getSelectedItem().equals(value))
			intervalComboBox.selectItem(value);

		buildTrees();
	}

	private void resetIntervalComboBox()
	{
		Interval selected = intervalComboBox.getSelectedItem();

		intervalComboBox.getItems().clear();
		for (Interval interval : Interval.values())
		{
			intervalComboBox.getItems().add(interval);
		}

		if (selected == null) intervalComboBox.selectFirst();
		else intervalComboBox.selectItem(selected);
	}

	private void initializeTreeFlow()
	{
		treesRoot.setOrientation(Orientation.HORIZONTAL);
		treesRoot.setAlignment(Pos.CENTER);
		treesRoot.setColumnHalignment(HPos.CENTER);
		treesRoot.setRowValignment(VPos.CENTER);
		treesRoot.setHgap(10);
		treesRoot.setVgap(10);
	}

	private void buildTrees()
	{
		TreeSet<AbstractActivity> activities = ActivityStatsHandler.getInstance().getAllActivitiesInInterval(
				treesInterval.get());

		treesRoot.getChildren().clear();

//		int activityCount = activities.size();
//		int minCols = 4, maxCols = 12;
//		int cols = NumberUtils.clamp(activityCount / 3, minCols, maxCols);
//		int rows = Math.min((activityCount / cols) + 1, 3);

		treesRoot.prefWrapLengthProperty().bind(treesRoot.widthProperty());

		for (AbstractActivity activity : activities)
		{
			StackPane node = (StackPane)buildTree(activity);
			treesRoot.getChildren().add(node);
		}

	}

	private Node buildTree(AbstractActivity activity)
	{
		ImageView imageView = new ImageView(ResourcesLoader.loadImage(activity.getFinalTreeSpritePath()));
		StackPane treeRoot = new StackPane(imageView);
		imageView.fitWidthProperty().bind(treeRoot.widthProperty());
		imageView.fitWidthProperty().bind(treeRoot.heightProperty());
//		imageView.fitWidthProperty().setValue(120);
//		imageView.fitWidthProperty().setValue(120);
		treeRoot.setAlignment(Pos.CENTER);
		imageView.setPreserveRatio(true);
//		treeRoot.setPrefSize(120, 120);
		treeRoot.setMaxSize(100, 100);
		treeRoot.setMinSize(100, 100);
		return treeRoot;
	}

	@FXML
	void onTreeSelectNextAction(ActionEvent event)
	{
		if (selectedPreviewTreeIndex.get()
				< treeSelectionHBox.getChildren().size() - extraTreesShownInSelectionHBox - 1)
		{
			setSelectedPreviewTree(selectedPreviewTreeIndex.get() + 1);
		}
	}

	@FXML
	void onTreeSelectPreviousAction(ActionEvent event)
	{
		if (selectedPreviewTreeIndex.get() > extraTreesShownInSelectionHBox)
		{
			setSelectedPreviewTree(selectedPreviewTreeIndex.get() - 1);
		}
	}

	private void updateTreeSelectionHBoxItems()
	{
		for (int i = 0; i < treeSelectionHBox.getChildren().size(); i++)
		{
			boolean isVisible = Math.abs(selectedPreviewTreeIndex.get() - i) <= extraTreesShownInSelectionHBox;
			Node node = treeSelectionHBox.getChildren().get(i);
			NodeUtils.setNodeVisible(node, isVisible);
		}
	}

	private void updateTreePreviewDetails()
	{
		boolean isPreviewUnlocked = this.selectedPreviewTree.get().isUnlocked();
		NodeUtils.setNodeVisible(unlockedTreeDetailsGrid, isPreviewUnlocked);
		NodeUtils.setNodeVisible(toUnlockTreeDetailsGrid, !isPreviewUnlocked);

		if (isPreviewUnlocked) updateUnlockedTreeDetails();
		else updateToUnlockTreeDetails();
	}


	private void updateUnlockedTreeDetails()
	{
		ActivityStatsHandler.TreeStats stats = ActivityStatsHandler.getInstance().getTreesStats(
				selectedPreviewTree.get());
		plantedTreesLabel.setText(stats.totalPlanted.toString());
		matureTreesLabel.setText(stats.totalMature.toString());
		deadTreesLabel.setText(stats.totaDead.toString());
		if (stats.lastPlanted.equals(LocalDateTime.MIN)) LocalizationUtils.bindLabelText(lastTreeLabel,
				"progress.treeNeverPlanted");
		else
		{
			lastTreeLabel.textProperty().unbind();
			lastTreeLabel.setText(stats.lastPlanted.truncatedTo(ChronoUnit.SECONDS).toString());
		}

	}

	private void updateToUnlockTreeDetails()
	{
		treeRequiredTimeLabel.setText(TimeUtils.formatTime(selectedPreviewTree.get().getTotalRequiredTime()));
		treeProgressTimeLabel.setText(TimeUtils.formatTime(selectedPreviewTree.get().getProgressTime()));
		treeUnlockProgressBar.setProgress(selectedPreviewTree.get().getUnlockProgress());
		selectTreeToUnlockButton.setOnAction(event ->
		{
			System.out.println("before: " + TreeHandler.getInstance().getSelectedTreeToUnlock().getName());
			TreeHandler.getInstance().setSelectedTreeToUnlock(selectedPreviewTree.get());
			System.out.println("after: " + TreeHandler.getInstance().getSelectedTreeToUnlock().getName());
		});
	}

	private Tree getSelectedPreviewTree()
	{
		return selectedPreviewTree.get();
	}

	private void setSelectedPreviewTree(Integer index)
	{
		this.selectedPreviewTreeIndex.set(index);
		Node previewNode = this.treeSelectionHBox.getChildren().get(index);
		Integer treeUuid = (Integer)previewNode.getProperties().get("tree-uuid");
		Tree tree = TreeHandler.getInstance().getTree(treeUuid);
		this.selectedPreviewTree.set(tree);

		updateTreeSelectionHBoxItems();
		updateTreePreviewDetails();
	}

	private void setSelectedPreviewTree(Tree selectedPreviewTree)
	{
		this.selectedPreviewTree.set(selectedPreviewTree);

		Node previewNode = this.previewSelectionTreesNodes.get(selectedPreviewTree);
		this.selectedPreviewTreeIndex.set(treeSelectionHBox.getChildren().indexOf(previewNode));

		if (selectedPreviewTreeIndex.get() == -1) System.err.println("couldn't find selected preview tree in HBox");

		updateTreeSelectionHBoxItems();
		updateTreePreviewDetails();
	}
}
