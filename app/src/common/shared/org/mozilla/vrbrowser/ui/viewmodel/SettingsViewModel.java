package org.mozilla.vrbrowser.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import org.mozilla.geckoview.ContentBlocking;
import org.mozilla.vrbrowser.browser.SettingsStore;

public class SettingsViewModel extends AndroidViewModel {

    private MutableLiveData<ObservableBoolean> isTrackingProtectionEnabled;
    private MutableLiveData<ObservableBoolean> isDRMEnabled;

    public SettingsViewModel(@NonNull Application application) {
        super(application);

        isTrackingProtectionEnabled = new MutableLiveData<>(new ObservableBoolean(false));
        isDRMEnabled = new MutableLiveData<>(new ObservableBoolean(false));
    }

    public void refresh() {
        int level = SettingsStore.getInstance(getApplication().getBaseContext()).getTrackingProtectionLevel();
        boolean isEnabled = level != ContentBlocking.EtpLevel.NONE;
        isTrackingProtectionEnabled.setValue(new ObservableBoolean(isEnabled));

        boolean drmEnabled = SettingsStore.getInstance(getApplication().getBaseContext()).isDrmContentPlaybackEnabled();
        isDRMEnabled = new MutableLiveData<>(new ObservableBoolean(drmEnabled));
    }

    public void setIsTrackingProtectionEnabled(boolean isEnabled) {
        this.isTrackingProtectionEnabled.setValue(new ObservableBoolean(isEnabled));
    }

    public MutableLiveData<ObservableBoolean> getIsTrackingProtectionEnabled() {
        return isTrackingProtectionEnabled;
    }

    public void setIsDrmEnabled(boolean isEnabled) {
        this.isDRMEnabled.setValue(new ObservableBoolean(isEnabled));
    }

    public MutableLiveData<ObservableBoolean> getIsDrmEnabled() {
        return isDRMEnabled;
    }

}
