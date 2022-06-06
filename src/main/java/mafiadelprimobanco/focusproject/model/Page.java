package mafiadelprimobanco.focusproject.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import mafiadelprimobanco.focusproject.controller.Controller;

public record Page(
		Integer uuid,
		String FXMLPath,
		String pageNameKey,
		SimpleObjectProperty<Node> pageRoot,
		SimpleObjectProperty<Controller> controller,
		SimpleBooleanProperty isSelected,
		SimpleBooleanProperty isNavigationAlwaysEnabled,
		SimpleBooleanProperty keepInBackground,
		KeyCode shortCutKey)
{

	public Page(Integer uuid, String FXMLPath, String pageNameKey, boolean isNavigationAlwaysEnabled,
			boolean keepInBackground,
			KeyCode shortCutKey)
	{
		this(uuid, FXMLPath, pageNameKey, new SimpleObjectProperty<>(), new SimpleObjectProperty<>(),
				new SimpleBooleanProperty(), new SimpleBooleanProperty(isNavigationAlwaysEnabled),
				new SimpleBooleanProperty(keepInBackground), shortCutKey);
	}

	public Controller getController()
	{
		return controller.get();
	}

	public void setController(Controller controller)
	{
		this.controller.set(controller);
	}
}
