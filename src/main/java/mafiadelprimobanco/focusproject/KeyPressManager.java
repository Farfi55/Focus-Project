package mafiadelprimobanco.focusproject;

import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;
import java.util.List;

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
		invokeHandlersEvent(event);
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
