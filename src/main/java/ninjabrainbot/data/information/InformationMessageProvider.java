package ninjabrainbot.data.information;

import ninjabrainbot.event.IDisposable;
import ninjabrainbot.event.ObservableField;
import ninjabrainbot.event.DisposeHandler;
import ninjabrainbot.io.preferences.BooleanPreference;

public abstract class InformationMessageProvider extends ObservableField<InformationMessage> implements IDisposable {

	DisposeHandler disposeHandler = new DisposeHandler();

	BooleanPreference enabledPreference;

	InformationMessageProvider() {
	}

	InformationMessageProvider(BooleanPreference enabledPreference) {
		this.enabledPreference = enabledPreference;
		disposeHandler.add(enabledPreference.whenModified().subscribe(this::raiseInformationMessageChanged));
	}

	protected abstract boolean shouldShowInformationMessage();

	protected abstract InformationMessage getInformationMessage();

	protected void raiseInformationMessageChanged() {
		boolean disabledByPreference = enabledPreference != null && !enabledPreference.get();
		InformationMessage informationMessageToShow = shouldShowInformationMessage() && !disabledByPreference ? getInformationMessage() : null;
		set(informationMessageToShow);
	}

	@Override
	public void dispose() {
		disposeHandler.dispose();
	}

}
