package mafiadelprimobanco.focusproject.model;

public interface ActivityObserver
{

	/**
	 * make sure to use Platform.runLater if you need to change a view
	 */
	default void onActivityStarting() { }

	/**
	 * make sure to use Platform.runLater if you need to change a view
	 */
	default void onActivityUpdate() { }

	/**
	 * make sure to use Platform.runLater if you need to change a view
	 */
	default void onActivityEnd() { }

	/**
	 * Thread-safe version of onActivityStart()
	 */
	default void onActivityStartingSafe() { }

	/**
	 * Thread-safe version of onActivityUpdate()
	 */
	default void onActivityUpdateSafe() { }

	/**
	 * Thread-safe version of onActivityEnd()
	 */
	default void onActivityEndSafe() { }
}
