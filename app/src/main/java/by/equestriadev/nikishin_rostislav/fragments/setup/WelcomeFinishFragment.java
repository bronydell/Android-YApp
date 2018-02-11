package by.equestriadev.nikishin_rostislav.fragments.setup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yandex.metrica.YandexMetrica;

import butterknife.ButterKnife;
import butterknife.OnClick;
import by.equestriadev.nikishin_rostislav.MainActivity;
import by.equestriadev.nikishin_rostislav.R;

/**
 * Created by Rostislav on 06.02.2018.
 */

public class WelcomeFinishFragment extends Fragment {



    private SharedPreferences mSharedPrefs;


    public static WelcomeFinishFragment newInstance() {
        WelcomeFinishFragment fragment = new WelcomeFinishFragment();

        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.welcome_finish, container, false);
        ButterKnife.bind(this, v);
        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @OnClick(R.id.finish_button)
    public void goButton(View view) {
        YandexMetrica.reportEvent("Finished welcome setup");
        goToLauncher();
    }

    private void goToLauncher() {
        SharedPreferences.Editor edit = mSharedPrefs.edit();
        edit.putBoolean("visited", true);
        edit.putBoolean("shouldVisit", false);
        edit.apply();
        Intent launcherIntent = new Intent(getContext(), MainActivity.class);
        startActivity(launcherIntent);
        getActivity().finish();
    }
}