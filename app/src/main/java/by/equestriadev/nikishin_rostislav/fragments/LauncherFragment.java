package by.equestriadev.nikishin_rostislav.fragments;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import by.equestriadev.nikishin_rostislav.R;
import by.equestriadev.nikishin_rostislav.adapters.AppGridAdapter;
import by.equestriadev.nikishin_rostislav.adapters.decorators.AppGridDecorator;
import by.equestriadev.nikishin_rostislav.adapters.holders.ItemClickListener;

/**
 * Created by Rostislav on 30.01.2018.
 */

public class LauncherFragment extends Fragment{

    @BindView(R.id.app_grid)
    RecyclerView appGrid;

    @BindView(R.id.plus_fab)
    FloatingActionButton plusFloatingActionButton;

    final Random rnd = new Random();
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
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        final int spanCount;
        if(prefs.getString(getString(R.string.layout_key), getString(R.string.default_layout)).equals(getResources().getStringArray(R.array.layout_options_values)[0])){
            spanCount = getResources().getInteger(R.integer.compact);
        }
        else {
            spanCount = getResources().getInteger(R.integer.standart);
        }
        final int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.launcher_offset);
        appGrid.setLayoutManager(new GridLayoutManager(this.getContext(), spanCount));
        appGrid.addItemDecoration(new AppGridDecorator(spacingInPixels));

        appGrid.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && plusFloatingActionButton.getVisibility() == View.VISIBLE) {
                    plusFloatingActionButton.hide();
                } else if (dy < 0 && plusFloatingActionButton.getVisibility() != View.VISIBLE) {
                    plusFloatingActionButton.show();
                }
            }
        });

        for(int  i = 0; i < 1000; i++){
            colorList.add(generateColor());
        }
        appGridAdapter = new AppGridAdapter(this.getContext(),  colorList);
        appGridAdapter.setOnLongItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                appGridAdapter.removeAt(position);
            }
        });
        appGrid.setAdapter(appGridAdapter);

    }

    private Integer generateColor(){
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }
    @OnClick(R.id.plus_fab)
    public void OnFABClick(View view){
        appGridAdapter.AddItem(generateColor(), 0);
        appGrid.scrollTo(0, 0);
    }
}