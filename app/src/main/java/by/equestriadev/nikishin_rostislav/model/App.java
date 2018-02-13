package by.equestriadev.nikishin_rostislav.model;

import android.content.pm.ResolveInfo;

import by.equestriadev.nikishin_rostislav.persistence.entity.AppStatistics;

/**
 * Created by Rostislav on 05.02.2018.
 */

public class App {
    private ResolveInfo mResolveInfo;
    private AppStatistics mStatistics;

    public App(ResolveInfo mResolveInfo, AppStatistics mStatistics) {
        this.mResolveInfo = mResolveInfo;
        this.mStatistics = mStatistics;
    }

    public ResolveInfo getResolveInfo() {
        return mResolveInfo;
    }

    public void setResolveInfo(ResolveInfo resolveInfo) {
        this.mResolveInfo = resolveInfo;
    }

    public AppStatistics getStatistics() {
        return mStatistics;
    }

    public void setStatistics(AppStatistics statistics) {
        this.mStatistics = statistics;
    }
}
