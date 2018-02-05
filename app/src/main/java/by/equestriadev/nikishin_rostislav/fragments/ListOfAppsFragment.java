package by.equestriadev.nikishin_rostislav.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import by.equestriadev.nikishin_rostislav.AppUtils;
import by.equestriadev.nikishin_rostislav.Database;
import by.equestriadev.nikishin_rostislav.R;
import by.equestriadev.nikishin_rostislav.adapters.ListAppAdapter;
import by.equestriadev.nikishin_rostislav.adapters.decorators.AppGridDecorator;
import by.equestriadev.nikishin_rostislav.adapters.holders.ItemClickListener;
import by.equestriadev.nikishin_rostislav.broadcasters.AppReceiver;
import by.equestriadev.nikishin_rostislav.model.App;
import by.equestriadev.nikishin_rostislav.model.AppStatistics;

/**
 * Created by Rostislav on 05.02.2018.
 */

public class ListOfAppsFragment extends Fragment implements IUpdatable {

    @BindView(R.id.app_grid)
    RecyclerView appGrid;

    private SharedPreferences mPrefs;
    private ListAppAdapter appAdapter;
    private AppReceiver mAppReceiver;
    private Database db;
    private AppUtils mUtils;

    public static LauncherFragment newInstance() {
        LauncherFragment fragment = new LauncherFragment();

        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.app_launcher, container, false);
        ButterKnife.bind(this, v);
        db = new Database(getContext());
        mPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        mUtils = new AppUtils(getContext(), db);
        final int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.launcher_offset);
        decorateGrid(appGrid, spacingInPixels);
        return v;
    }

    private void decorateGrid(RecyclerView grid, int spacingInPixels) {
        grid.setLayoutManager(new LinearLayoutManager(this.getContext()));
        grid.addItemDecoration(new AppGridDecorator(spacingInPixels));
    }

    private void initAdapter(final ListAppAdapter adapter) {
        adapter.setOnLongItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                final ResolveInfo appInfo = appAdapter.getItemObject(position).getResolveInfo();
                final AppStatistics appStat = appAdapter.getItemObject(position).getStatistics();
                PopupMenu popup = new PopupMenu(view.getContext(), view);
                popup.inflate(R.menu.app_context_menu);
                if (!appStat.isFavorite())
                    popup.getMenu().findItem(R.id.fav).setVisible(true);
                else
                    popup.getMenu().findItem(R.id.unfav).setVisible(true);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.about:
                                mUtils.aboutAppByResolveInfo(appInfo);
                                return true;
                            case R.id.delete:
                                mUtils.deleteAppByResolveInfo(appInfo);
                                return true;
                            case R.id.unfav:
                                appStat.setFavorite(false);
                                db.CreateOrUpdate(appStat);
                                updateAppList();
                                return true;
                            case R.id.fav:
                                appStat.setFavorite(true);
                                db.CreateOrUpdate(appStat);
                                updateAppList();
                                return true;
                            case R.id.freq:
                                mUtils.frequencyInfoByResolveInfo(appInfo);
                                return true;
                            default:
                                return true;
                        }
                    }
                });
                popup.show();
            }
        });
        adapter.setOnItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                ResolveInfo appInfo = adapter.getItemObject(position).getResolveInfo();
                AppStatistics statistics = db.getApp(appInfo.activityInfo.packageName);
                statistics.setLastUsage(new Date());
                statistics.addUsage();
                db.CreateOrUpdate(statistics);
                mUtils.launchAppByResolveInfo(appInfo);
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public void onStart() {
        super.onStart();
        InitReceiver();
        InitRecyclerView();
    }

    private void InitRecyclerView() {
        Log.d(getClass().getName(), "Unregister Receiver");
        List<App> appInfos = mUtils.getSortedApps(mPrefs.getString(getString(R.string.sort_key),
                getString(R.string.default_sort)),
                mPrefs.getBoolean(getString(R.string.hide_fav_key), false));

        appAdapter = new ListAppAdapter(this.getContext(), appInfos);
        initAdapter(appAdapter);

        appGrid.setAdapter(appAdapter);
    }


    public void InitReceiver() {
        Log.d(getClass().getName(), "Registering Receiver");
        mAppReceiver = new AppReceiver(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_REPLACED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addDataScheme("package");
        getContext().registerReceiver(mAppReceiver, intentFilter);
    }

    public void updateAppList() {
        List<App> appInfos = mUtils.getSortedApps(mPrefs.getString(getString(R.string.sort_key),
                getString(R.string.default_sort)),
                mPrefs.getBoolean(getString(R.string.hide_fav_key), false));
        appAdapter.setAppList(appInfos);
    }


    @Override
    public void onStop() {
        super.onStop();
        Log.d(getClass().getName(), "Unregister Receiver");
        getContext().unregisterReceiver(this.mAppReceiver);
    }
}