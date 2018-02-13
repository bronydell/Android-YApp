package by.equestriadev.nikishin_rostislav.fragment;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.yandex.metrica.YandexMetrica;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import by.equestriadev.nikishin_rostislav.AppUtils;
import by.equestriadev.nikishin_rostislav.R;
import by.equestriadev.nikishin_rostislav.adapter.AppListAdapter;
import by.equestriadev.nikishin_rostislav.adapter.decorators.AppGridDecorator;
import by.equestriadev.nikishin_rostislav.adapter.holders.ItemClickListener;
import by.equestriadev.nikishin_rostislav.broadcast.AppReceiver;
import by.equestriadev.nikishin_rostislav.model.App;
import by.equestriadev.nikishin_rostislav.persistence.AppDatabase;
import by.equestriadev.nikishin_rostislav.persistence.entity.AppStatistics;

/**
 * Created by Rostislav on 13.02.2018.
 */

public abstract class AppFragment extends Fragment implements IUpdatable {

    @BindView(R.id.app_grid)
    RecyclerView appGrid;

    private AppReceiver mAppReceiver;
    private static final SimpleDateFormat DATE_FORMAT =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private AppDatabase db;
    private AppUtils mUtils;

    protected SharedPreferences mPrefs;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected AppGridDecorator mDecorator;
    protected AppListAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.app_launcher, container, false);
        ButterKnife.bind(this, v);
        // Initialization
        db = AppDatabase.getDatabase(getContext());
        mUtils = new AppUtils(getContext(), db);
        mPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());

        initDecorators();
        return v;
    }

    protected abstract void initDecorators();

    protected void decorateGrid(RecyclerView grid) {
        grid.setLayoutManager(mLayoutManager);
        grid.addItemDecoration(mDecorator);
    }

    protected void initAdapter(final AppListAdapter adapter) {
        adapter.setOnLongItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                final ResolveInfo appInfo = mAdapter.getItemObject(position).getResolveInfo();
                final AppStatistics appStat = mAdapter.getItemObject(position).getStatistics();
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
                                updateApp(appStat);
                                return true;
                            case R.id.fav:
                                YandexMetrica.reportEvent("Pressed on \"Favorite app\"");
                                appStat.setFavorite(true);
                                updateApp(appStat);
                                return true;
                            case R.id.freq:
                                YandexMetrica.reportEvent("Pressed on \"Frequency info\"");
                                mUtils.frequencyInfoByResolveInfo(appInfo, getFragment());
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
                AppStatistics statistics = adapter.getItemObject(position).getStatistics();
                statistics.addUsage();
                statistics.setLastUsage(new Date());
                updateApp(statistics);
                mUtils.launchAppByResolveInfo(appInfo);
            }
        });
    }

    public Fragment getFragment() {
        return this;
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

    protected abstract void InitRecyclerView();


    public void InitReceiver() {
        mAppReceiver = new AppReceiver(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_REPLACED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addDataScheme("package");
        getContext().registerReceiver(mAppReceiver, intentFilter);
    }

    public void updateApp(final AppStatistics appStatistics) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                db.AppStatisticsModel().insertApps(appStatistics);
                updateAppList();
            }
        }).start();
    }

    public void updateAppList() {
        if (mAdapter != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final List<App> appInfos = mUtils.getSortedApps(mPrefs.getString(getString(R.string.sort_key),
                            getString(R.string.default_sort)),
                            mPrefs.getBoolean(getString(R.string.hide_fav_key), false));
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.setAppList(appInfos);
                        }
                    });
                }
            }).start();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        getContext().unregisterReceiver(this.mAppReceiver);
    }
}