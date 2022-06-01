package mafiadelprimobanco.focusproject.controller;

import io.github.palexdev.materialfx.controls.MFXRectangleToggleNode;
import io.github.palexdev.materialfx.controls.MFXTooltip;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import mafiadelprimobanco.focusproject.*;
import mafiadelprimobanco.focusproject.model.ActivityObserver;
import mafiadelprimobanco.focusproject.model.activity.AbstractActivity;
import mafiadelprimobanco.focusproject.model.utils.FXMLReferences;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BaseController implements ActivityObserver, EventHandler<KeyEvent>
{
	List<MFXRectangleToggleNode> navButtons = new ArrayList<>();
	@FXML private AnchorPane root;
	@FXML private StackPane contentRoot;
	@FXML private MFXRectangleToggleNode homeButton;
	@FXML private MFXRectangleToggleNode progressButton;
	@FXML private MFXRectangleToggleNode statisticsButton;
//	@FXML private MFXRectangleToggleNode tagButton;
	@FXML private MFXRectangleToggleNode infoButton;
	@FXML private MFXRectangleToggleNode accountButton;
	@FXML private MFXRectangleToggleNode settingsButton;
	@FXML private ToggleGroup nav_toggle_group;
	private boolean canNavigate = true;

	@FXML
	void initialize()
	{
		SceneHandler.getInstance().setRoot(root);
		MFXTooltip.of(homeButton, "Home page (ctrl+H)").install();
		MFXTooltip.of(progressButton, "Progress page (ctrl+P)").install();
		MFXTooltip.of(statisticsButton, "Statistics page (ctrl+S)").install();
//		MFXTooltip.of(tagButton, "Tag page (ctrl+T)").install();
		MFXTooltip.of(infoButton, "Help (ctrl+I)").install();
		MFXTooltip.of(accountButton, "Account settings page (ctrl+U)").install();
		MFXTooltip.of(settingsButton, "Settings page (ctrl+,)").install();
		//
		//		navButtons = nav_toggle_group.getToggles();
		navButtons.add(homeButton);
		navButtons.add(progressButton);
		navButtons.add(statisticsButton);
//		navButtons.add(tagButton);
		navButtons.add(infoButton);
		navButtons.add(accountButton);
		navButtons.add(settingsButton);
		setNavigationEnabled(true);
		ActivityHandler.getInstance().addListener(this);
		KeyPressManager.getInstance().addHandler(this);
		SceneHandler.getInstance().setContentPane(contentRoot);

		onHomeClick();
	}

	@Override
	public void onActivityStartingSafe(AbstractActivity currentActivity)
	{
		switch (ActivityHandler.getInstance().getCurrentActivityType())
		{
			case TIMER, TOMATO_TIMER -> setNavigationEnabled(false);
		}
	}

	@Override
	public void onActivityEndSafe(AbstractActivity currentActivity)
	{
		setNavigationEnabled(true);
	}

	@Override
	public void handle(KeyEvent keyEvent)
	{
		System.out.println("key pressed: " + keyEvent.getCode());
		if (keyEvent.isControlDown() || keyEvent.isShortcutDown())
		{
			switch (keyEvent.getCode())
			{
				case DIGIT1, H -> onHomeClick();
				case DIGIT2, P -> onProgressClick();
				case DIGIT3, S -> onStatisticsClick();
				case DIGIT4, I -> onInfoClick();
				case DIGIT5, U -> onAccountClick();
				case DIGIT6, COMMA -> onSettingsClick();
			}
		}
		else if (keyEvent.getCode() == KeyCode.F1) onInfoClick();
	}

	@FXML
	void onHomeClick()
	{
		navigateTo(FXMLReferences.HOME, homeButton, "Home");
	}

	@FXML
	void onProgressClick()
	{
		navigateTo(FXMLReferences.PROGRESS, progressButton, "Progressi");
	}

	@FXML
	void onStatisticsClick()
	{
		navigateTo(FXMLReferences.STATISTICS, statisticsButton, "Statistiche");
	}

//	@FXML
//	void onTagClick()
//	{
//		navigateTo(FXMLReferences.TAGS, tagButton, "Tag");
//	}

	@FXML
	void onInfoClick()
	{
		navigateTo(FXMLReferences.INFO, infoButton, "Informazioni");
	}

	@FXML
	void onAccountClick() {
		try
		{
			SceneHandler.getInstance().showLoginPopup();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@FXML
	void onSettingsClick()
	{
		navigateTo(FXMLReferences.SETTINGS, settingsButton, "Impostazioni");
	}

	private void navigateTo(String pagePathRef, MFXRectangleToggleNode button, String pageName)
	{
		if (!canNavigate && !pagePathRef.equals(FXMLReferences.SETTINGS))
		{
			Feedback.getInstance()
					.showNotification("Navigazione disabilitata",
							"La navigazione è disabilitata una volta iniziata l'attività");
			return;
		}
		try
		{
			if (!pagePathRef.equals(""))
			{
				SceneHandler.getInstance().navTo(pagePathRef);
			}
			else System.out.println("navigation to empty page reference");
			button.setSelected(true);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			Feedback.getInstance().showError("Errore Di caricamento", "Impossibile caricare la pagina " + pageName);
		}
	}

	private void setNavigationEnabled(boolean value)
	{
		System.out.println("navigation enabled: " + value);
		canNavigate = value;
		for (MFXRectangleToggleNode button : navButtons)
		{
			button.setDisable(!value);
		}
		settingsButton.setDisable(false);
	}


}
