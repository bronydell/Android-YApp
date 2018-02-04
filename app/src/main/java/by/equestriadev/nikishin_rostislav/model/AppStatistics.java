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


    public AppStatistics(String mPackage, Date mLastUsage) {
        this.mPackage = mPackage;
        this.mLastUsage = mLastUsage;
        this.mUsageCounter = 1;
    }

    public AppStatistics(String mPackage, long mUsageCounter, Date mLastUsage) {
        this.mPackage = mPackage;
        this.mUsageCounter = mUsageCounter;
        this.mLastUsage = mLastUsage;
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
}
