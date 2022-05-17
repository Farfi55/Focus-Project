package mafiadelprimobanco.focusproject.controller;

import io.github.palexdev.materialfx.controls.MFXRectangleToggleNode;
import io.github.palexdev.materialfx.controls.MFXTooltip;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import mafiadelprimobanco.focusproject.SceneHandler;
import mafiadelprimobanco.focusproject.model.utils.FXMLReferences;

import java.util.ArrayList;
import java.util.List;

public class BaseController
{
	List<MFXRectangleToggleNode> navButtons = new ArrayList<>();
	@FXML private AnchorPane root;
	@FXML private StackPane contentRoot;
	@FXML private MFXRectangleToggleNode homeButton;
	@FXML private MFXRectangleToggleNode progressButton;
	@FXML private MFXRectangleToggleNode statisticsButton;
	@FXML private MFXRectangleToggleNode tagButton;
	@FXML private MFXRectangleToggleNode infoButton;
	@FXML private MFXRectangleToggleNode userButton;
	@FXML private MFXRectangleToggleNode settingsButton;
	@FXML private ToggleGroup nav_toggle_group;
	private boolean canNavigate = true;

	@FXML
	void initialize()
	{
		SceneHandler.getInstance().setContentRoot(contentRoot);

		MFXTooltip.of(homeButton, "Home page (ctrl+H)").install();
		MFXTooltip.of(progressButton, "Progress page (ctrl+P)").install();
		MFXTooltip.of(statisticsButton, "Statistics page (ctrl+S)").install();
		MFXTooltip.of(tagButton, "Tag page (ctrl+T)").install();
		MFXTooltip.of(infoButton, "Help (ctrl+I)").install();
		MFXTooltip.of(userButton, "Account settings page (ctrl+U)").install();
		MFXTooltip.of(settingsButton, "Settings page (ctrl+,)").install();
		navButtons.add(homeButton);
		navButtons.add(progressButton);
		navButtons.add(statisticsButton);
		navButtons.add(tagButton);
		navButtons.add(infoButton);
		navButtons.add(userButton);
		navButtons.add(settingsButton);
		root.setOnKeyPressed(this::handleKeyPress);
	}

	private void handleKeyPress(KeyEvent keyEvent)
	{
		if (keyEvent.isControlDown() || keyEvent.isShortcutDown())
		{
			switch (keyEvent.getCode())
			{
				case DIGIT1, H -> onNavHomeClick();
				case DIGIT2, P -> onNavProgressClick();
				case DIGIT3, S -> onNavStatisticsClick();
				case DIGIT4, T -> onNavTagClick();
				case DIGIT5, I -> onNavInfoClick();
				case DIGIT6, U -> onNavUserClick();
				case DIGIT7, COMMA -> onNavSettingsClick();
			}
		}
		else if (keyEvent.getCode() == KeyCode.F1) onNavInfoClick();
	}

	@FXML
	void onNavHomeClick()
	{
		if (navTo(FXMLReferences.HOME, "Home")) homeButton.setSelected(true);
	}

	@FXML
	void onNavProgressClick()
	{
		if (navTo(FXMLReferences.PROGRESS, "Progressi")) progressButton.setSelected(true);
	}

	@FXML
	void onNavStatisticsClick()
	{
		if (navTo(FXMLReferences.STATISTICS, "Statistiche")) statisticsButton.setSelected(true);
	}

	@FXML
	void onNavTagClick()
	{
		if (navTo(FXMLReferences.TAGS, "Tag")) tagButton.setSelected(true);
	}

	@FXML
	void onNavInfoClick()
	{
		// todo: implement
		infoButton.setSelected(true);
	}

	@FXML
	void onNavUserClick()
	{
		// todo: implement
		userButton.setSelected(true);
	}

	@FXML
	void onNavSettingsClick()
	{
		// todo: implement
		settingsButton.setSelected(true);
		setNavigationEnabled(!canNavigate);
	}

	private boolean navTo(String pagePathRef, String pageName)
	{
		// A bit of a hack but whatever
		// todo: to be added to the if once pages refs are defined:
		// && !pagePathRef.equals(FXMLReferences.SETTINGS)
		if (!canNavigate)
		{
			SceneHandler.getInstance().showInfoMessage("Navigation disabled",
					"Navigation is disabled once the activity has started");
			return false;
		}
		/* disabled for now
		try
		{
			SceneHandler.getInstance().loadPage(pagePathRef);
			return true;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			SceneHandler.getInstance().showErrorMessage("Errore Di caricamento",
					"Impossibile caricare la pagina " + pageName);
			return false;
		}
		*/
		return true;
	}

	void setNavigationEnabled(boolean value)
	{
		canNavigate = value;
		for (var button : navButtons)
			button.setDisable(!value);
		settingsButton.setDisable(false);
	}


}
