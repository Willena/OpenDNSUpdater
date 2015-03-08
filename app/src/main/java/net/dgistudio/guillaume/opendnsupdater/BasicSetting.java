package net.dgistudio.guillaume.opendnsupdater;

import android.os.Bundle;
import android.preference.PreferenceActivity;


public class BasicSetting extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);
    }

    //TODO : check validity on every config change
    //TODO : check for the first time -> ask to complete infos
    //TODO : Force use of openDNS DNS servers
    //TODO : display a version Number
}