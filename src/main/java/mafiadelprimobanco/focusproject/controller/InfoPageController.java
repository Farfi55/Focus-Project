package mafiadelprimobanco.focusproject.controller;

import io.github.palexdev.materialfx.controls.MFXTooltip;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import mafiadelprimobanco.focusproject.handler.Localization;
import mafiadelprimobanco.focusproject.utils.LocalizationUtils;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class InfoPageController implements Controller
{
	@FXML private Label helpHeaderLabel;
	@FXML private Label helpLabel;

	@FXML private VBox imageContainer;

	@FXML private ImageView homeImage;

	@FXML private ImageView progressImage;

	@FXML private Label madeByLabel;

	@FXML private Hyperlink githubLink;

//	@FXML private TextFlow helpTextFlow;

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		initializeHelpSection();
		githubLink.setText(Localization.get("info.githubLink"));

//		(githubLink, "https://github.com/Farfi55/Focus-Project/");
		githubLink.setOnAction(event ->
		{
			try
			{
				Desktop.getDesktop().browse(URI.create("https://github.com/Farfi55/Focus-Project/"));
			}
			catch (IOException e)
			{
				throw new RuntimeException(e);
			}
		});

		madeByLabel.setText(Localization.get("info.madeBy") + "Alessio Farfaglia,\t Lorenzo Grillo,\t Pasquale Tudda.  ");

		homeImage.fitWidthProperty().bind(imageContainer.widthProperty());
		progressImage.fitWidthProperty().bind(imageContainer.widthProperty());
	}

	private void initializeHelpSection()
	{
		helpHeaderLabel.getStyleClass().add("info-header");
		LocalizationUtils.bindLabelText(helpHeaderLabel, "info.help.header");

		StringBuilder stringBuilder = new StringBuilder();

		for (var key : List.of("info.help.navigationBar", "info.help.activityTreeSelection", "info.help.activities",
				"info.help.tags", "info.help.trees", "info.help.forest", "info.help.login"))
			stringBuilder.append(Localization.get(key));
		helpLabel.setText(stringBuilder.toString());
	}

	@Override
	public void terminate()
	{

	}
}
