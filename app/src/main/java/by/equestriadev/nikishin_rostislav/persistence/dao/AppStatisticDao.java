package by.equestriadev.nikishin_rostislav.persistence.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import by.equestriadev.nikishin_rostislav.persistence.entity.AppStatistics;

/**
 * Created by Rostislav on 12.02.2018.
 */

@Dao
public interface AppStatisticDao {
    @Query("Select * from AppStatistics")
    List<AppStatistics> getAllApps();
    @Delete
    void deleteApp(AppStatistics appStatistic);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertApps(AppStatistics... appStatistics);
    @Query("SELECT * FROM AppStatistics WHERE package = :appPackage")
    AppStatistics get(String appPackage);

}
