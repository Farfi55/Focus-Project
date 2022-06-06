package mafiadelprimobanco.focusproject.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import mafiadelprimobanco.focusproject.controller.PageController;

public record Page(
		Integer uuid,
		String FXMLPath,
		String pageNameKey,
		SimpleObjectProperty<Node> pageRoot,
		SimpleObjectProperty<PageController> controller,
		SimpleBooleanProperty isSelected,
		boolean isNavigationAlwaysEnabled,
		boolean keepInBackground)
{

	public Page(Integer uuid, String FXMLPath, String pageNameKey, boolean isNavigationAlwaysEnabled,
			boolean keepInBackground)
	{
		this(uuid, FXMLPath, pageNameKey, new SimpleObjectProperty<>(), new SimpleObjectProperty<>(),
				new SimpleBooleanProperty(), isNavigationAlwaysEnabled, keepInBackground);
	}

	public PageController getController()
	{
		return controller.get();
	}

	public void setController(PageController controller)
	{
		this.controller.set(controller);
	}
}
