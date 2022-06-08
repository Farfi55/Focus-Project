package mafiadelprimobanco.focusproject.controller;

import io.github.palexdev.materialfx.controls.MFXRectangleToggleNode;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Pair;
import mafiadelprimobanco.focusproject.handler.KeyPressManager;
import mafiadelprimobanco.focusproject.handler.PagesHandler;
import mafiadelprimobanco.focusproject.handler.SceneHandler;
import mafiadelprimobanco.focusproject.model.Page;

import java.util.ArrayList;
import java.util.List;

public class BaseController implements EventHandler<KeyEvent>
{
	List<Pair<MFXRectangleToggleNode, Page>> navButtonsPages = new ArrayList<>();
	@FXML private AnchorPane root;
	@FXML private StackPane contentRoot;
	@FXML private MFXRectangleToggleNode homeButton;
	@FXML private MFXRectangleToggleNode progressButton;
	@FXML private MFXRectangleToggleNode statisticsButton;
	//	@FXML private MFXRectangleToggleNode tagButton;
	@FXML private MFXRectangleToggleNode infoButton;
	@FXML private MFXRectangleToggleNode accountButton;
	@FXML private MFXRectangleToggleNode settingsButton;


	@FXML
	void initialize()
	{
		SceneHandler.getInstance().setRoot(root);

		navButtonsPages.add(new Pair<>(homeButton, PagesHandler.home));
		navButtonsPages.add(new Pair<>(progressButton, PagesHandler.progress));
		navButtonsPages.add(new Pair<>(statisticsButton, PagesHandler.statistics));
		navButtonsPages.add(new Pair<>(infoButton, PagesHandler.info));
		navButtonsPages.add(new Pair<>(accountButton, PagesHandler.account));
		navButtonsPages.add(new Pair<>(settingsButton, PagesHandler.settings));

		for (var buttonPage : navButtonsPages)
		{
			MFXRectangleToggleNode button = buttonPage.getKey();
			Page page = buttonPage.getValue();

			button.disableProperty().bind(
					page.isNavigationAlwaysEnabled().not().and(PagesHandler.isNavigationEnabledProperty().not()));

			button.setOnAction(event ->
			{
				PagesHandler.navigateTo(page);
				SceneHandler.getInstance().closeLoginPopup();
				button.setSelected(true);
			});
			page.isSelected().addListener(observable -> button.setSelected(page.isSelected().get()));
		}

		// override behaviour
		accountButton.setOnAction(event -> SceneHandler.getInstance().toggleLoginPopup());

		KeyPressManager.getInstance().addHandler(this);
		SceneHandler.getInstance().setContentPane(contentRoot);
	}


	@Override
	public void handle(KeyEvent keyEvent)
	{
		if (keyEvent.isControlDown() || keyEvent.isShortcutDown())
		{
			for (var buttonPage : navButtonsPages)
			{
				Page page = buttonPage.getValue();
				if (keyEvent.getCode().equals(page.shortCutKey()))
				{
					MFXRectangleToggleNode button = buttonPage.getKey();
					button.fire();
					keyEvent.consume();
					return;
				}
			}
		}
		else if (keyEvent.getCode() == KeyCode.F1)
		{
			infoButton.fire();
			keyEvent.consume();
		}
	}


}
