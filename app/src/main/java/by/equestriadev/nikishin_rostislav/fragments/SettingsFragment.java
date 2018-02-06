package by.equestriadev.nikishin_rostislav.fragments;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import by.equestriadev.nikishin_rostislav.R;

/**
 * Created by Rostislav on 29.01.2018.
 */

public class SettingsFragment extends PreferenceFragmentCompat {


    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();

        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public void onCreatePreferences(Bundle bundle, String rootKey) {
        setPreferencesFromResource(R.xml.settings, rootKey);
    }
}