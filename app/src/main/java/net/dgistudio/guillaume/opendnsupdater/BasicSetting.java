package net.dgistudio.guillaume.opendnsupdater;

import android.os.Bundle;
import android.preference.PreferenceActivity;


public class BasicSetting extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);
    }

}