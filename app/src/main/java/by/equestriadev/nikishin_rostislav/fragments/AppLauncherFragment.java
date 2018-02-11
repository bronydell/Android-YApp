package by.equestriadev.nikishin_rostislav.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.yandex.metrica.YandexMetrica;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import by.equestriadev.nikishin_rostislav.AppUtils;
import by.equestriadev.nikishin_rostislav.model.Database;
import by.equestriadev.nikishin_rostislav.R;
import by.equestriadev.nikishin_rostislav.adapters.AppGridAdapter;
import by.equestriadev.nikishin_rostislav.adapters.decorators.AppGridDecorator;
import by.equestriadev.nikishin_rostislav.adapters.holders.ItemClickListener;
import by.equestriadev.nikishin_rostislav.broadcasters.AppReceiver;
import by.equestriadev.nikishin_rostislav.model.App;
import by.equestriadev.nikishin_rostislav.model.AppStatistics;

/**
 * Created by Rostislav on 04.02.2018.
 */

public class AppLauncherFragment extends Fragment implements IUpdatable {

    @BindView(R.id.app_grid)
    RecyclerView appGrid;

    private SharedPreferences mPrefs;
    private AppGridAdapter appGridAdapter;
    private AppReceiver mAppReceiver;
    private static final SimpleDateFormat DATE_FORMAT =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
        mUtils = new AppUtils(getContext(), db);
        mPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        for (Map.Entry ent: mPrefs.getAll().entrySet()){
            Log.d(getClass().getName(), ent.getKey().toString() + " " + ent.getValue().toString());
        }
        final int spanCount;
        if (mPrefs.getString(getString(R.string.layout_key), getString(R.string.default_layout)).equals(getResources().getStringArray(R.array.layout_options_values)[0])) {
            spanCount = getResources().getInteger(R.integer.compact);
        } else {
            spanCount = getResources().getInteger(R.integer.standart);
        }
        final int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.launcher_offset);
        decorateGrid(appGrid, spanCount, spacingInPixels);
        return v;
    }

    private void decorateGrid(RecyclerView grid, int spanCount, int spacingInPixels){
        grid.setLayoutManager(new GridLayoutManager(this.getContext(), spanCount));
        grid.addItemDecoration(new AppGridDecorator(spacingInPixels));
    }

    private void initAdapter(final AppGridAdapter adapter){
        adapter.setOnLongItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                final ResolveInfo appInfo = appGridAdapter.getItemObject(position).getResolveInfo();
                final AppStatistics appStat = appGridAdapter.getItemObject(position).getStatistics();
                PopupMenu popup = new PopupMenu(view.getContext(), view);
                popup.inflate(R.menu.app_context_menu);
                if(!appStat.isFavorite())
                    popup.getMenu().findItem(R.id.fav).setVisible(true);
                else
                    popup.getMenu().findItem(R.id.unfav).setVisible(true);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.about:
                                YandexMetrica.reportEvent("Pressed on \"About app\"");
                                mUtils.aboutAppByResolveInfo(appInfo);
                                return true;
                            case R.id.delete:
                                YandexMetrica.reportEvent("Pressed on \"Delete app\"");
                                mUtils.deleteAppByResolveInfo(appInfo);
                                return true;
                            case R.id.unfav:
                                YandexMetrica.reportEvent("Pressed on \"Unfavorite app\"");
                                appStat.setFavorite(false);
                                db.CreateOrUpdate(appStat);
                                updateAppList();
                                return true;
                            case R.id.fav:
                                YandexMetrica.reportEvent("Pressed on \"Favorite app\"");
                                appStat.setFavorite(true);
                                db.CreateOrUpdate(appStat);
                                updateAppList();
                                return true;
                            case R.id.freq:
                                YandexMetrica.reportEvent("Pressed on \"Frequency info\"");
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
                YandexMetrica.reportEvent("Used app");
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

    private void InitRecyclerView(){
        List<App> appInfos = mUtils.getSortedApps(mPrefs.getString(getString(R.string.sort_key),
                getString(R.string.default_sort)),
                mPrefs.getBoolean(getString(R.string.hide_fav_key), false));

        appGridAdapter = new AppGridAdapter(this.getContext(), appInfos);
        initAdapter(appGridAdapter);

        appGrid.setAdapter(appGridAdapter);
    }


    public void InitReceiver(){
        mAppReceiver = new AppReceiver(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_REPLACED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addDataScheme("package");
        getContext().registerReceiver(mAppReceiver, intentFilter);
    }

    public void updateAppList(){
        List<App> appInfos = mUtils.getSortedApps(mPrefs.getString(getString(R.string.sort_key),
                getString(R.string.default_sort)),
                mPrefs.getBoolean(getString(R.string.hide_fav_key), false));
        appGridAdapter.setAppList(appInfos);
    }

    @Override
    public void onStop() {
        super.onStop();
        getContext().unregisterReceiver(this.mAppReceiver);
    }
}