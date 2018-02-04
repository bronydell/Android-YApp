package by.equestriadev.nikishin_rostislav.model;

import android.support.annotation.NonNull;

import java.util.Date;

/**
 * Created by Rostislav on 04.02.2018.
 */

public class AppStatistics {

    private String mPackage;
    private long mUsageCounter;
    private Date mLastUsage;
    private boolean isFavorite;


    public AppStatistics(String mPackage, Date mLastUsage, boolean isFavorite) {
        this.mPackage = mPackage;
        this.mLastUsage = mLastUsage;
        this.mUsageCounter = 1;
        this.isFavorite = isFavorite;
    }

    public AppStatistics(String mPackage, long mUsageCounter, Date mLastUsage, boolean isFavorite) {
        this.mPackage = mPackage;
        this.mUsageCounter = mUsageCounter;
        this.mLastUsage = mLastUsage;
        this.isFavorite = isFavorite;
    }

    public void addUsage(){
        mUsageCounter++;
    }

    public long getUsageCounter() {
        return mUsageCounter;
    }

    public Date getLastUsage() {
        return mLastUsage;
    }

    public void setLastUsage(Date mLastUsage) {
        this.mLastUsage = mLastUsage;
    }

    public String getPackage() {
        return mPackage;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
