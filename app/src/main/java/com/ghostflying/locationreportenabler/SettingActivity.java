package com.ghostflying.locationreportenabler;

import android.content.SharedPreferences;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.os.Bundle;

import de.robv.android.xposed.XSharedPreferences;

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

        updateSummary();

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
    }

    private void updateSummary(SharedPreferences sharedPreferences, String key) {
        findPreference(key).setSummary(sharedPreferences.getString(key, ""));
    }

    private void updateSummary() {
        for (int i = 0; i < getPreferenceScreen().getPreferenceCount(); i++) {
            Preference preference = getPreferenceScreen().getPreference(i);
            if (preference instanceof EditTextPreference) {
                EditTextPreference editTextPreference = (EditTextPreference)preference;
                editTextPreference.setSummary(editTextPreference.getText());
            }
        }
    }
}
