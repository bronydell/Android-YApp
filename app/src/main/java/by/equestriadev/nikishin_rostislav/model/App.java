package by.equestriadev.nikishin_rostislav.model;

import android.content.pm.ResolveInfo;

import by.equestriadev.nikishin_rostislav.persistence.entity.AppStatistics;

/**
 * Created by Rostislav on 05.02.2018.
 */

public class App {
    private ApplicationInfo mAppInfo;
    private AppStatistics mStatistics;

    public App(ApplicationInfo appInfo, AppStatistics statistics) {
        this.mAppInfo = appInfo;
        this.mStatistics = statistics;
    }

    public ApplicationInfo getApplicationInfo() {
        return mAppInfo;
    }

    public void setApplicationInfo(ApplicationInfo appInfo) {
        this.mAppInfo = appInfo;
    }

    public AppStatistics getStatistics() {
        return mStatistics;
    }

    public void setStatistics(AppStatistics statistics) {
        this.mStatistics = statistics;
    }
}
