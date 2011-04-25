package com.rj.research.uiuc.gesturesound.android;

import com.rj.research.uiuc.gesturesound.audio.Parameter;

public interface SettingsChangedListener {
	public void settingsChanged(Parameter param, float value);
	public void settingsEnabledState(Parameter param, boolean enabled);
	public void settingsEditedFeatures(Parameter param, int[] qualities);
}
