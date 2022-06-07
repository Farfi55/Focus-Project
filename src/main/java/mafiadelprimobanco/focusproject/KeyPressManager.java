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
		else if (event.isControlDown() && event.getCode().equals(KeyCode.O)){
			Feedback.getInstance().showNotification("Testing Header", "Phasellus vel tellus at justo varius aliquam quis vel enim. Mauris velit diam, sollicitudin in porta id, luctus nec ipsum. Morbi tincidunt vitae purus id scelerisque. Curabitur aliquam diam at ex mattis facilisis. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Phasellus congue vitae risus vel rutrum. Sed sagittis suscipit nisl non condimentum. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Integer tristique, augue a auctor dignissim, lorem eros vulputate augue, aliquet interdum orci turpis vulputate nisi. Fusce ornare ligula turpis, at semper lorem scelerisque quis.\nFusce placerat, felis pharetra cursus pellentesque, risus orci cursus arcu, at laoreet leo justo vitae lacus. Nunc et lorem id tellus convallis accumsan. Donec sit amet orci lobortis, sagittis magna condimentum, finibus nunc. Aliquam erat volutpat. Phasellus at eleifend urna. In volutpat sem quis consequat efficitur. Phasellus elementum magna magna, ut scelerisque est posuere sed. Suspendisse eget tortor lacus.");

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
