package by.equestriadev.nikishin_rostislav.persistence.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;


import java.util.Date;

import by.equestriadev.nikishin_rostislav.persistence.conventer.DateConventer;

/**
 * Created by Rostislav on 04.02.2018.
 */

@Entity
@TypeConverters(DateConventer.class)
public class AppStatistics {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "package")
    private String mPackage;
    @ColumnInfo(name = "usage_counter")
    private long mUsageCounter = 0;
    @ColumnInfo(name = "last_usage")
    private Date mLastUsage;
    @ColumnInfo(name = "favorite")
    private boolean isFavorite = false;

    public void addUsage(){
        this.mUsageCounter += 1;
    }

    public void setUsageCounter(long mUsageCounter){
        this.mUsageCounter = mUsageCounter;
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

    public void setPackage(String mPackage) {
        this.mPackage = mPackage;
    }
}
