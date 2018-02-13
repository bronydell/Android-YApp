package by.equestriadev.nikishin_rostislav.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;

import com.yandex.metrica.YandexMetrica;

import by.equestriadev.nikishin_rostislav.R;
import by.equestriadev.nikishin_rostislav.service.ImageLoaderService;

/**
 * Created by Rostislav on 29.01.2018.
 */

public class SettingsFragment extends PreferenceFragmentCompat
        implements SharedPreferences.OnSharedPreferenceChangeListener {


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

    @Override
    public void onResume() {
        super.onResume();
        // Set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
    private void callService(){
        Intent intent = new Intent(getActivity(), ImageLoaderService.class);
        intent.setAction(ImageLoaderService.SERVICE_ACTION_LOAD_IMAGE);
        getActivity().startService(intent);
    }
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(getString(R.string.theme_key))){
            YandexMetrica.reportEvent("Theme has changed");
            getActivity().recreate();
        }
        if(key.equals(getString(R.string.freq_key))){
            YandexMetrica.reportEvent("Changed frequency source");
            callService();
        }
        if(key.equals(getString(R.string.wallpaper_source_key))){
            YandexMetrica.reportEvent("Changed wallpaper source");
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            prefs.edit()
                    .putBoolean("forcedWallpaper", true)
                    .apply();
            callService();
        }

    }
}