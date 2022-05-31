package mafiadelprimobanco.focusproject.model;

import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.utils.ScrollUtils;
import javafx.scene.Node;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class ActivityRecapDialog extends MFXGenericDialog
{
	private TextFlow textContent;
	private Text defaultContentText;

	public ActivityRecapDialog()
	{
	}

	public ActivityRecapDialog(String headerText, String contentText)
	{
		super(headerText, contentText);
	}


	/**
	 * Builds the default dialog's content.
	 */
	@Override
	protected void buildContent() {
		buildScrollableContent(true);
	}

	/**
	 * Builds the same nodes as {@link #buildContent()} but wrapped in
	 * a {@link MFXScrollPane}.
	 *
	 * @param smoothScrolling to specify whether to use smooth scrolling on the {@link MFXScrollPane}
	 */
	@Override
	protected void buildScrollableContent(boolean smoothScrolling) {
		TextFlow textContent = new TextFlow();
		textContent.getStyleClass().add("content");
		defaultContentText = new Text();
		defaultContentText.textProperty().bind(contentTextProperty());
		textContent.getChildren().add(defaultContentText);


		MFXScrollPane scrollPane = new MFXScrollPane(textContent);
		scrollPane.getStyleClass().add("content-container");
		scrollPane.setFitToWidth(true);
		if (smoothScrolling) ScrollUtils.addSmoothScrolling(scrollPane, 0.5);


		setTextContent(textContent);
		setContent(scrollPane);
	}

	public TextFlow getTextContent()
	{
		return textContent;
	}

	protected void setTextContent(TextFlow textContent)
	{
		this.textContent = textContent;
	}

	public void addToContent(Node node)
	{
		textContent.getChildren().add(node);
	}

	public void addText(String text)
	{
		addToContent(new Text(text));
	}

	/**
	 * removes all children but the default text from the textFlow
	 */
	public void clearTextContent()
	{
		textContent.getChildren().retainAll(defaultContentText);
	}
}
