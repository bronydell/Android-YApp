package by.equestriadev.nikishin_rostislav.fragments;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import by.equestriadev.nikishin_rostislav.Database;
import by.equestriadev.nikishin_rostislav.R;
import by.equestriadev.nikishin_rostislav.adapters.AppGridAdapter;
import by.equestriadev.nikishin_rostislav.broadcasters.AppReceiver;
import by.equestriadev.nikishin_rostislav.comporators.AlphabetComparator;
import by.equestriadev.nikishin_rostislav.comporators.DateComparator;
import by.equestriadev.nikishin_rostislav.comporators.FrequencyComparator;
import by.equestriadev.nikishin_rostislav.adapters.decorators.AppGridDecorator;
import by.equestriadev.nikishin_rostislav.adapters.holders.ItemClickListener;
import by.equestriadev.nikishin_rostislav.model.AppStatistics;

/**
 * Created by Rostislav on 04.02.2018.
 */

public class AppLauncherFragment extends Fragment {

    @BindView(R.id.app_grid)
    RecyclerView appGrid;

    private SharedPreferences mPrefs;
    private AppGridAdapter appGridAdapter;
    private AppReceiver mAppReceiver;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Database db;

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
                final ResolveInfo appInfo = appGridAdapter.getItemObject(position);
                final AppStatistics appStat = db.getApp(appInfo.activityInfo.packageName);
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
                                aboutAppByResolveInfo(appInfo);
                                return true;
                            case R.id.delete:
                                deleteAppByResolveInfo(appInfo);
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
                                frequencyInfoByResolveInfo(appInfo);
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
                ResolveInfo appInfo = adapter.getItemObject(position);
                AppStatistics statistics = db.getApp(appInfo.activityInfo.packageName);
                statistics.setLastUsage(new Date());
                statistics.addUsage();
                db.CreateOrUpdate(statistics);
                launchAppByResolveInfo(appInfo);
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
        InitGrids();
    }

    private void InitGrids(){
        List<ResolveInfo> appInfos = getSortedApps();
        // List<ResolveInfo> favAppInfos = getFavoriteApps(appInfos);

        appGridAdapter = new AppGridAdapter(this.getContext(), appInfos);
        initAdapter(appGridAdapter);

        appGrid.setAdapter(appGridAdapter);
    }

    private List<ResolveInfo> getFavoriteApps(List<ResolveInfo> allApps){
        List<ResolveInfo> favAppInfos = new ArrayList<>();
        for(int i = 0; i < allApps.size(); i++){
            if(db.getApp(allApps.get(i).activityInfo.packageName).isFavorite()) {
                favAppInfos.add(allApps.get(i));
                allApps.remove(i);
            }
        }
        return favAppInfos;
    }

    private List<ResolveInfo> getAllInstalledApps() {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        return getContext().getPackageManager().queryIntentActivities(mainIntent, 0);
    }

    public void InitReceiver(){
        Log.d(getClass().getName(), "Registering Receiver");
        mAppReceiver = new AppReceiver(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_REPLACED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addDataScheme("package");
        getContext().registerReceiver(mAppReceiver, intentFilter);
    }

    public void updateAppList(){
        List<ResolveInfo> appInfos = getSortedApps();
        // List<ResolveInfo> favAppInfos = getFavoriteApps(appInfos);
        appGridAdapter.setAppList(appInfos);
    }

    private List<ResolveInfo> getSortedApps(){
        List<ResolveInfo> installedApps = getAllInstalledApps();
        switch (mPrefs.getString(getString(R.string.sort_key), getString(R.string.default_sort))){
            case "sort_az":
                Collections.sort(installedApps, new AlphabetComparator(getContext().getPackageManager()));
                break;
            case "sort_za":
                Collections.sort(installedApps, new AlphabetComparator(getContext().getPackageManager()));
                Collections.reverse(installedApps);
                break;
            case "sort_freq":
                Collections.sort(installedApps, new FrequencyComparator(db));
                Collections.reverse(installedApps);
                break;
            case "sort_date":
                Collections.sort(installedApps, new DateComparator(getContext()));
                Collections.reverse(installedApps);
                break;
        }
        return installedApps;
    }

    private void launchAppByResolveInfo(ResolveInfo info) {
        ActivityInfo activity = info.activityInfo;
        ComponentName name = new ComponentName(activity.applicationInfo.packageName,
                activity.name);
        Intent i = new Intent(Intent.ACTION_MAIN);

        i.addCategory(Intent.CATEGORY_LAUNCHER);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        i.setComponent(name);
        startActivity(i);
    }

    private void deleteAppByResolveInfo(ResolveInfo info) {
        if(!info.isDefault) {
            Intent intent = new Intent(Intent.ACTION_DELETE);
            intent.setData(Uri.parse("package:" + info.activityInfo.packageName));
            startActivity(intent);
        }
    }

    private void frequencyInfoByResolveInfo(ResolveInfo info) {
        final AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(getContext());
        AppStatistics appStatistics = db.getApp(info.activityInfo.packageName);
        String appName = info.loadLabel(getContext().getPackageManager()).toString();
        dlgAlert.setMessage(String.format(getString(R.string.frequency_text),appName,
                appStatistics.getUsageCounter(),
                appStatistics.getLastUsage() != null ? DATE_FORMAT.format(appStatistics.getLastUsage()) : getString(R.string.never_used)));
        dlgAlert.setTitle(String.format(getString(R.string.frequency_title), appName));
        dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }

    private void aboutAppByResolveInfo(ResolveInfo info) {
        // GLORY TO GOOGLE FOR THIS LINE!
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + info.activityInfo.packageName));
        startActivity(intent);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(getClass().getName(), "Unregister Receiver");
        getContext().unregisterReceiver(this.mAppReceiver);
    }
}