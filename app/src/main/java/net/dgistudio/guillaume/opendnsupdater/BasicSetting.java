package net.dgistudio.guillaume.opendnsupdater;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;


public class BasicSetting extends PreferenceActivity implements NoticeDialogFragment.NoticeDialogListener{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showNoticeDialog();
        addPreferencesFromResource(R.xml.pref_general);

      /*  SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if (prefs.getBoolean("firstTime",true))
        {


            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", false);
            editor.commit();

        }*/




    }

    public void showNoticeDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new NoticeDialogFragment();
        dialog.show(this.getFragmentManager(), "setPass");
    }


    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        if (dialog.getTag().equals("setPass"))
        {
            Log.d("d", "Dialog pass validated");
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        if (dialog.getTag().equals("setPass"))
        {
            Log.d("d", "Dialog closed");
        }
    }


    //TODO : check validity on every config change
    //TODO : check for the first time -> ask to complete infos
    //TODO : Force use of openDNS DNS servers
    //TODO : display a version Number
    //TODO : Add a password befor edit
    //TODO : Allow to disable OpenDNSUpdate
}