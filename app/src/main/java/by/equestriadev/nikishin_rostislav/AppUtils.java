package by.equestriadev.nikishin_rostislav;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import by.equestriadev.nikishin_rostislav.comporator.AlphabetComparator;
import by.equestriadev.nikishin_rostislav.comporator.DateComparator;
import by.equestriadev.nikishin_rostislav.comporator.FrequencyComparator;
import by.equestriadev.nikishin_rostislav.model.App;
import by.equestriadev.nikishin_rostislav.model.AppShortcut;
import by.equestriadev.nikishin_rostislav.model.ApplicationInfo;
import by.equestriadev.nikishin_rostislav.model.ShortcutType;
import by.equestriadev.nikishin_rostislav.persistence.AppDatabase;
import by.equestriadev.nikishin_rostislav.persistence.entity.AppStatistics;
import by.equestriadev.nikishin_rostislav.persistence.entity.Shortcut;
import by.equestriadev.nikishin_rostislav.service.ShortcutService;

/**
 * Created by Rostislav on 05.02.2018.
 */

public class AppUtils {


    private static final SimpleDateFormat DATE_FORMAT =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Context mContext;
    private AppDatabase mDatabase;

    public AppUtils(Context mContext, AppDatabase mDatabase) {
        this.mContext = mContext.getApplicationContext();
        this.mDatabase = mDatabase;
    }

    public void callService(String action){
        Intent intent = new Intent(mContext, ShortcutService.class);
        intent.setAction(action);
        Log.d(getClass().getName(), "Attempt to call service to call update");
        mContext.startService(intent);
    }

    public boolean addShortcut(Shortcut shortcut, int limit){
        Map<Integer, Shortcut> shortcuts = getShortcuts();
        for(int i = 0; i < limit; i++){
            if(shortcuts.get(i) == null){
                shortcut.setPosition(i);
                mDatabase.ShortcutModel().insertShortcuts(shortcut);
                callService(ShortcutService.SERVICE_ACTION_ADD_SHORTCUT);
                return true;
            }
        }
        return false;
    }

    public Map<Integer, Shortcut> getShortcuts(){
        Map<Integer, Shortcut> shortcuts = new HashMap<>();
        List<App> apps = getAllInstalledApps();
        List<Shortcut> shorts = new ArrayList<>();
        if (mDatabase != null)
            shorts = mDatabase.ShortcutModel().getAllShortcuts();
        for (Shortcut shot:
            shorts) {
            if(shot.getShortcutType() == ShortcutType.APPLICATION){
                AppShortcut cut = new AppShortcut(shot);
                for (App app:
                        apps) {
                    if(app.getApplicationInfo().getActivityName().equals(shot.getUrl())) {
                        cut.setApplication(app.getApplicationInfo());
                        shortcuts.put(shot.getPosition(), cut);
                        break;
                    }
                }
            }
            else{
                shortcuts.put(shot.getPosition(), shot);
            }
        }
        return shortcuts;
    }

    private List<App> getAllInstalledApps() {
        Log.d(getClass().getName(), "Fetching every installed app with launcher activity");
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<App> installedApps = new ArrayList<>();
        for (ResolveInfo app:
                mContext.getPackageManager().queryIntentActivities(mainIntent, 0)) {
            AppStatistics appStatistics = null;
            if(mDatabase != null)
                appStatistics = mDatabase.AppStatisticsModel().get(app.activityInfo.packageName);
            if(appStatistics == null){
                appStatistics = new AppStatistics();
                appStatistics.setFavorite(false);
                appStatistics.setUsageCounter(0);
                appStatistics.setPackage(app.activityInfo.packageName);
            }
            installedApps.add(new App(new ApplicationInfo(app, mContext.getPackageManager()),
                    appStatistics));
        }
        return installedApps;
    }

    public void sortApps(List<App> sortedApps, String sort_type) {
        switch (sort_type) {
            case "sort_az":
                Log.d(getClass().getName(), "Sorting list of apps a-z");
                Collections.sort(sortedApps,
                        new AlphabetComparator());
                break;
            case "sort_za":
                Log.d(getClass().getName(), "Sorting list of apps z-a");
                Collections.sort(sortedApps,
                        new AlphabetComparator());
                Collections.reverse(sortedApps);
                break;
            case "sort_freq":
                Log.d(getClass().getName(), "Sorting list of apps by usage frequency");
                Collections.sort(sortedApps, new FrequencyComparator());
                Collections.reverse(sortedApps);
                break;
            case "sort_date":
                Log.d(getClass().getName(), "Sorting list of apps by install date");
                Collections.sort(sortedApps, new DateComparator(mContext));
                Collections.reverse(sortedApps);
                break;
        }
    }

    public List<App> getSortedApps(String sort_type, boolean applyFavorites){
        List<App> installedApps = getAllInstalledApps();
        List<App> favApps;
        sortApps(installedApps, sort_type);
        if(applyFavorites) {
            favApps = getFavoriteApps(installedApps);
            favApps.addAll(installedApps);
            return favApps;
        }
        return installedApps;
    }


    public List<App> getFavoriteApps(List<App> allApps) {
        List<App> favAppInfos = new ArrayList<>();
        for(int i = 0; i < allApps.size(); i++){
            if(allApps.get(i).getStatistics().isFavorite()) {
                favAppInfos.add(allApps.get(i));
                // allApps.remove(i);
                // i--;
            }
        }
        return favAppInfos;
    }

    public void launchAppByApplicationInfo(ApplicationInfo info) {
        ComponentName name = new ComponentName(info.getPackageName(),
                info.getActivityName());
        Intent i = new Intent(Intent.ACTION_MAIN);

        i.addCategory(Intent.CATEGORY_LAUNCHER);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        i.setComponent(name);
        mContext.startActivity(i);
    }

    public void deleteAppByApplicationInfo(ApplicationInfo info) {
            Intent intent = new Intent(Intent.ACTION_DELETE);
            intent.setData(Uri.parse("package:" + info.getPackageName()));
            mContext.startActivity(intent);
    }

    public void frequencyInfoByApplicationInfo(final ApplicationInfo info,
                                           final Fragment fragment) {
        Log.d(getClass().getName(), "Opening frequency popup");
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (info != null) {
                    AppStatistics appStatistics = mDatabase.AppStatisticsModel()
                            .get(info.getPackageName());
                    if (appStatistics == null) {
                        appStatistics = new AppStatistics();
                        appStatistics.setFavorite(false);
                        appStatistics.setUsageCounter(0);
                        appStatistics.setPackage(info.getPackageName());
                    }
                    final AlertDialog.Builder dlgAlert = new AlertDialog.Builder(fragment.getContext());
                    String appName = info.getAppname();
                    dlgAlert.setMessage(String.format(mContext.getString(R.string.frequency_text), appName,
                            appStatistics.getUsageCounter(),
                            appStatistics.getLastUsage() != null ? DATE_FORMAT.format(appStatistics.getLastUsage()) :
                                    mContext.getString(R.string.never_used)));
                    dlgAlert.setTitle(String.format(mContext.getString(R.string.frequency_title), appName));
                    dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dlgAlert.setCancelable(true);
                    fragment.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dlgAlert.create().show();
                        }
                    });
                }
            }
        }).start();

    }

    public void aboutAppByApplicationInfo(ApplicationInfo info) {
        Log.d(getClass().getName(), "Opening about activity");
        // GLORY TO GOOGLE FOR THIS LINE!
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + info.getPackageName()));
        mContext.startActivity(intent);
    }
}
