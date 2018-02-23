package by.equestriadev.nikishin_rostislav.persistence.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import java.util.Date;

import by.equestriadev.nikishin_rostislav.persistence.converter.DateConverter;

/**
 * Created by Rostislav on 04.02.2018.
 */

@Entity
@TypeConverters(DateConverter.class)
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

    @Ignore
    public AppStatistics(@NonNull String pack,
                         long usageCounter,
                         Date lastUsage,
                         boolean isFavorite) {
        this.mPackage = pack;
        this.mUsageCounter = usageCounter;
        this.mLastUsage = lastUsage;
        this.isFavorite = isFavorite;
    }

    public AppStatistics() {
    }

    public void addUsage(){
        this.mUsageCounter += 1;
    }

    public long getUsageCounter() {
        return mUsageCounter;
    }

    public void setUsageCounter(long mUsageCounter) {
        this.mUsageCounter = mUsageCounter;
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

    public void setPackage(String mPackage) {
        this.mPackage = mPackage;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
