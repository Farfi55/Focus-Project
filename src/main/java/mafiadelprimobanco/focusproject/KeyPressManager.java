package mafiadelprimobanco.focusproject;

import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class KeyPressManager implements EventHandler<KeyEvent>
{
	private static final KeyPressManager instance = new KeyPressManager();

	public static KeyPressManager getInstance()
	{
		return instance;
	}

	List<EventHandler<KeyEvent>> handlers = new ArrayList<>();

	private KeyPressManager() { }

	@Override
	public void handle(KeyEvent event)
	{
		debug(event);
		invokeHandlersEvent(event);
	}

	private void debug(KeyEvent event)
	{
		if (event.isControlDown() && event.getCode().equals(KeyCode.K))
			StyleHandler.getInstance().toggleLightDarkTheme();
		else if (event.isControlDown() && event.getCode().equals(KeyCode.L)) StyleHandler.getInstance().setTheme(
				"light2");
		else if (event.isControlDown() && event.getCode().equals(KeyCode.J))
		{
			if (Localization.getLocale() == Locale.ENGLISH) Localization.setLocale(Locale.ITALIAN);
			else Localization.setLocale(Locale.ENGLISH);

		}

	}

	private void invokeHandlersEvent(KeyEvent event)
	{
		for (var handler : handlers)
		{
			if (event.isConsumed()) break;
			handler.handle(event);
		}
	}

	public void init(Parent root)
	{
		root.setOnKeyPressed(this);
	}


	public void addHandler(EventHandler<KeyEvent> handler)
	{
		assert !handlers.contains(handler);
		handlers.add(handler);
	}

	public void removeHandler(EventHandler<KeyEvent> handler)
	{
		assert handlers.contains(handler);
		handlers.remove(handler);
	}
}
