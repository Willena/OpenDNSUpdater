package net.dgistudio.guillaume.opendnsupdater;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;


public class BasicSetting extends PreferenceActivity implements setPasswordDialog.NoticeDialogListener, welcomeTextDialog.NoticeDialogListener{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

       if (prefs.getBoolean("firstTime",true))
        {
            DialogFragment firstime = new welcomeTextDialog();
            firstime.show(this.getFragmentManager(), "welcome");

            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", false);
            editor.commit();
       }
        addPreferencesFromResource(R.xml.pref_general);
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        if (dialog.getTag().equals("setPass"))
        {
            Log.d("d", "Dialog pass validated");
        }
        else if (dialog.getTag().equals("welcome"))
        {
            DialogFragment setPass = new setPasswordDialog();
            setPass.show(this.getFragmentManager(), "setPass");
       }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }


    //TODO : check validity on every config change
    //TODO : Force use of openDNS DNS servers
    //TODO : display a version Number
    //TODO : Add a password befor edit
}