package by.equestriadev.nikishin_rostislav.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import by.equestriadev.nikishin_rostislav.R;
import by.equestriadev.nikishin_rostislav.adapters.AppGridAdapter;
import by.equestriadev.nikishin_rostislav.adapters.decorators.AppGridDecorator;
import by.equestriadev.nikishin_rostislav.adapters.holders.ItemClickListener;

/**
 * Created by Rostislav on 30.01.2018.
 */

public class LauncherFragment extends Fragment {

    @BindView(R.id.app_grid)
    RecyclerView appGrid;


    private AppGridAdapter appGridAdapter;

    public static LauncherFragment newInstance() {
        LauncherFragment fragment = new LauncherFragment();

        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.launcher, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onStart() {
        super.onStart();
        setHasOptionsMenu(false);
        List<Integer> colorList = new ArrayList<>();
        appGrid.setLayoutManager(new GridLayoutManager(this.getContext(), 7));
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        Log.d("LauncherFragment","Language: " + prefs.getString("lang", "idk"));
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.launcher_offset);
        appGrid.addItemDecoration(new AppGridDecorator(spacingInPixels));
        final Random rnd = new Random();
        for(int  i = 0; i < 1000; i++){
            colorList.add(Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)));
        }
        appGridAdapter = new AppGridAdapter(this.getContext(),  colorList);
        appGrid.setAdapter(appGridAdapter);

    }
}