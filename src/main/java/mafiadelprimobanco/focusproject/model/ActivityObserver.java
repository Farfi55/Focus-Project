package mafiadelprimobanco.focusproject.model;

public interface ActivityObserver
{

	/**
	 * make sure to use Platform.runLater if you need to change a view
	 */
	default void onActivityStart() { }

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
	default void onActivityStartSafe() { }

	/**
	 * Thread-safe version of onActivityUpdate()
	 */
	default void onActivityUpdateSafe() { }

	/**
	 * Thread-safe version of onActivityEnd()
	 */
	default void onActivityEndSafe() { }
}
