package com.ghostflying.locationreportenabler;

import android.content.ComponentName;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.os.Bundle;
import android.preference.PreferenceGroup;
import android.util.Log;

public class SettingActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getPreferenceManager().setSharedPreferencesMode(MODE_WORLD_READABLE);

        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateSummary(getPreferenceScreen());

        SharedPreferences sharedPreferences = getPreferenceManager().getSharedPreferences();
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        updateSummary(sharedPreferences, s);

        if (getString(R.string.pref_hide_launcher_key).equals(s)) {
            boolean needHide = sharedPreferences.getBoolean(s, false);

            if (needHide) {
                PackageManager p = getPackageManager();
                ComponentName componentName = new ComponentName(this, SettingActivity.class);
                if (needHide) {
                    if (p.getComponentEnabledSetting(componentName) != PackageManager.COMPONENT_ENABLED_STATE_DISABLED) {
                        p.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
                        Log.d("PropUtil", "Hide the icon.");
                    }
                } else {
                    if (p.getComponentEnabledSetting(componentName) != PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
                        p.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
                        Log.d("PropUtil", "Show the icon.");
                    }
                }
            }
        }
    }

    private void updateSummary(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);
        if (preference instanceof EditTextPreference) {
            preference.setSummary(sharedPreferences.getString(key, ""));
        }
    }

    private void updateSummary(PreferenceGroup group) {
        for (int i = 0; i < group.getPreferenceCount(); i++) {
            Preference preference = group.getPreference(i);
            if (preference instanceof EditTextPreference) {
                EditTextPreference editTextPreference = (EditTextPreference)preference;
                editTextPreference.setSummary(editTextPreference.getText());
            }
            if (preference instanceof PreferenceGroup) {
                updateSummary((PreferenceGroup) preference);
            }
        }
    }
}
