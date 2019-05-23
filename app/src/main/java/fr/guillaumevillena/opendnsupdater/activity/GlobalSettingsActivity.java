package fr.guillaumevillena.opendnsupdater.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.RingtonePreference;
import android.text.TextUtils;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import fr.guillaumevillena.opendnsupdater.R;
import fr.guillaumevillena.opendnsupdater.utils.ConnectivityUtil;
import fr.guillaumevillena.opendnsupdater.utils.PreferenceCodes;
import fr.guillaumevillena.opendnsupdater.utils.RessourceUtil;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class GlobalSettingsActivity extends AppCompatPreferenceActivity {

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);

            } else if (preference instanceof RingtonePreference) {
                // For ringtone preferences, look up the correct display value
                // using RingtoneManager.
                if (TextUtils.isEmpty(stringValue)) {
                    // Empty values correspond to 'silent' (no ringtone).
                    preference.setSummary(R.string.pref_ringtone_silent);

                } else {
                    Ringtone ringtone = RingtoneManager.getRingtone(
                            preference.getContext(), Uri.parse(stringValue));

                    if (ringtone == null) {
                        // Clear the summary if there was a lookup error.
                        preference.setSummary(null);
                    } else {
                        // Set the summary to reflect the new ringtone display
                        // name.
                        String name = ringtone.getTitle(preference.getContext());
                        preference.setSummary(name);
                    }
                }

            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);
            }
            return true;
        }
    };

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference, getPrefValueFromKey(preference));
    }

    private static Object getPrefValueFromKey(Preference preference) {
        Map<String, ?> all = PreferenceManager.getDefaultSharedPreferences(preference.getContext()).getAll();
        return all.get(preference.getKey()).toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
        setContentView(R.layout.global_preference_activity);
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        return GeneralPreferenceFragment.class.getName().equals(fragmentName);
    }

    /**
     * This fragment shows general preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_settings);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference(PreferenceCodes.OPENDNS_NETWORK));
            bindPreferenceSummaryToValue(findPreference(PreferenceCodes.OPENDNS_USERNAME));
            bindPreferenceSummaryToValue(findPreference(PreferenceCodes.APP_BLACKLIST));
            setBlackListToCurrentValue((MultiSelectListPreference) findPreference(PreferenceCodes.APP_BLACKLIST));
            findPreference(PreferenceCodes.OPENDNS_PASSWORD).setOnPreferenceClickListener(preference -> {
                new AlertDialog.Builder(preference.getContext())
                        .setTitle(getResources().getString(R.string.alert_password_title))
                        .setMessage(getResources().getString(R.string.alert_password_message))
                        .setPositiveButton(getResources().getString(R.string.text_Ok), (dialogInterface, i) -> {
                        }).create().show();
                return false;
            });

        }

        private void setListPreferenceValues(MultiSelectListPreference preference, Set<String> values, String[] entries) {

            preference.setValues(values);
            preference.setEntries(entries);
            preference.setEntryValues(entries);
            preference.getOnPreferenceChangeListener().onPreferenceChange(preference, getPrefValueFromKey(preference));


        }

        private String[] getBlackListEntries() {
            Set<String> entries = PreferenceManager.getDefaultSharedPreferences(getActivity()).getStringSet(PreferenceCodes.APP_BLACKLIST_ENTRIES, null);
            if (entries == null) {
                entries = getDefaultBlackListValues();
                PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putStringSet(PreferenceCodes.APP_BLACKLIST_ENTRIES, entries).apply();
            }

            String[] strings = new String[entries.size()];
            entries.toArray(strings);
            return strings;
        }

        private void setBlackListToCurrentValue(MultiSelectListPreference preference) {
            Set<String> values = PreferenceManager.getDefaultSharedPreferences(preference.getContext()).getStringSet(preference.getKey(), null);
            if (values != null) {
                setListPreferenceValues(preference, values, getBlackListEntries());
            }
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            return super.onOptionsItemSelected(item);
        }

        @Override
        public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
            if (preference.getKey().equals(getString(R.string.preference_filter_add))) {
                Set<String> entries = PreferenceManager.getDefaultSharedPreferences(preference.getContext()).getStringSet(PreferenceCodes.APP_BLACKLIST_ENTRIES, null);
                Set<String> values = PreferenceManager.getDefaultSharedPreferences(preference.getContext()).getStringSet(PreferenceCodes.APP_BLACKLIST, null);
                if (entries != null && values != null) {

                    String toAdd = ConnectivityUtil.getActiveNetworkName(getActivity());
                    entries.add(toAdd);
                    values.add(toAdd);

                    PreferenceManager.getDefaultSharedPreferences(preference.getContext()).edit().putStringSet(PreferenceCodes.APP_BLACKLIST_ENTRIES, entries).apply();
                    PreferenceManager.getDefaultSharedPreferences(preference.getContext()).edit().putStringSet(PreferenceCodes.APP_BLACKLIST, values).apply();


                    MultiSelectListPreference blacklist = (MultiSelectListPreference) findPreference(PreferenceCodes.APP_BLACKLIST);
                    setBlackListToCurrentValue(blacklist);

                }

            } else if (preference.getKey().equals(getString(R.string.preference_filter_clear))) {

                Set<String> array = getDefaultBlackListValues();
                PreferenceManager.getDefaultSharedPreferences(preference.getContext()).edit().putStringSet(PreferenceCodes.APP_BLACKLIST_ENTRIES, array).apply();
                PreferenceManager.getDefaultSharedPreferences(preference.getContext()).edit().putStringSet(PreferenceCodes.APP_BLACKLIST, array).apply();

                MultiSelectListPreference blacklist = (MultiSelectListPreference) findPreference(PreferenceCodes.APP_BLACKLIST);
                setBlackListToCurrentValue(blacklist);
            } else if (preference.getKey().startsWith("app_about_licenses")) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(RessourceUtil.getId(preference.getKey(), R.string.class))));
                startActivity(browserIntent);
            }

            return super.onPreferenceTreeClick(preferenceScreen, preference);
        }

        private Set<String> getDefaultBlackListValues() {
            String[] array = getResources().getStringArray(R.array.list_preference_networks_blacklist);
            Set<String> mySet = new HashSet<>(Arrays.asList(array));
            return mySet;
        }


    }


}
