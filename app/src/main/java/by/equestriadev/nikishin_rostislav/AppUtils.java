package by.equestriadev.nikishin_rostislav;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import by.equestriadev.nikishin_rostislav.comporator.AlphabetComparator;
import by.equestriadev.nikishin_rostislav.comporator.DateComparator;
import by.equestriadev.nikishin_rostislav.comporator.FrequencyComparator;
import by.equestriadev.nikishin_rostislav.model.App;
import by.equestriadev.nikishin_rostislav.persistence.AppDatabase;
import by.equestriadev.nikishin_rostislav.persistence.entity.AppStatistics;

/**
 * Created by Rostislav on 05.02.2018.
 */

public class AppUtils {

    private Context mContext;
    private AppDatabase mDatabase;
    private static final SimpleDateFormat DATE_FORMAT =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public AppUtils(Context mContext, AppDatabase mDatabase) {
        this.mContext = mContext;
        this.mDatabase = mDatabase;
    }

    private List<App> getAllInstalledApps() {
        Log.d(getClass().getName(), "Fetching every installed app with launcher activity");
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<App> installedApps = new ArrayList<>();
        for (ResolveInfo app:
                mContext.getPackageManager().queryIntentActivities(mainIntent, 0)) {
            AppStatistics appStatistics = mDatabase.AppStatisticsModel().get(app.activityInfo.packageName);
            if(appStatistics == null){
                appStatistics = new AppStatistics();
                appStatistics.setFavorite(false);
                appStatistics.setUsageCounter(0);
                appStatistics.setPackage(app.activityInfo.packageName);
            }
            installedApps.add(new App(app, appStatistics));
        }
        return installedApps;
    }

    public List<App> getSortedApps(String sort_type, boolean applyFavorites){
        List<App> installedApps = getAllInstalledApps();
        List<App> sortedApps;
        switch (sort_type){
            case "sort_az":
                Log.d(getClass().getName(), "Sorting list of apps a-z");
                Collections.sort(installedApps,
                        new AlphabetComparator(mContext.getPackageManager()));
                break;
            case "sort_za":
                Log.d(getClass().getName(), "Sorting list of apps z-a");
                Collections.sort(installedApps,
                        new AlphabetComparator(mContext.getPackageManager()));
                Collections.reverse(installedApps);
                break;
            case "sort_freq":
                Log.d(getClass().getName(), "Sorting list of apps by usage frequency");
                Collections.sort(installedApps, new FrequencyComparator());
                Collections.reverse(installedApps);
                break;
            case "sort_date":
                Log.d(getClass().getName(), "Sorting list of apps by install date");
                Collections.sort(installedApps, new DateComparator(mContext));
                Collections.reverse(installedApps);
                break;
        }
        if(applyFavorites) {
            sortedApps = getFavoriteApps(installedApps);
            sortedApps.addAll(installedApps);
            return sortedApps;
        }
        return installedApps;
    }


    private List<App> getFavoriteApps(List<App> allApps){
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

    public void launchAppByResolveInfo(ResolveInfo info) {
        ActivityInfo activity = info.activityInfo;
        ComponentName name = new ComponentName(activity.applicationInfo.packageName,
                activity.name);
        Intent i = new Intent(Intent.ACTION_MAIN);

        i.addCategory(Intent.CATEGORY_LAUNCHER);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        i.setComponent(name);
        mContext.startActivity(i);
    }

    public void deleteAppByResolveInfo(ResolveInfo info) {
            Intent intent = new Intent(Intent.ACTION_DELETE);
            intent.setData(Uri.parse("package:" + info.activityInfo.packageName));
            mContext.startActivity(intent);
    }

    public void frequencyInfoByResolveInfo(final ResolveInfo info,
                                           final Fragment fragment) {
        Log.d(getClass().getName(), "Opening frequency popup");
        new Thread(new Runnable() {
            @Override
            public void run() {
                final AppStatistics appStatistics = mDatabase.AppStatisticsModel()
                        .get(info.activityInfo.packageName);
                final AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(mContext);
                String appName = info.loadLabel(mContext.getPackageManager()).toString();
                dlgAlert.setMessage(String.format(mContext.getString(R.string.frequency_text),appName,
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
        }).start();

    }

    public void aboutAppByResolveInfo(ResolveInfo info) {
        Log.d(getClass().getName(), "Opening about activity");
        // GLORY TO GOOGLE FOR THIS LINE!
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + info.activityInfo.packageName));
        mContext.startActivity(intent);
    }
}
