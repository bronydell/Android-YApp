package by.equestriadev.nikishin_rostislav.persistence.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import by.equestriadev.nikishin_rostislav.persistence.entity.AppStatistics;
import by.equestriadev.nikishin_rostislav.persistence.entity.Shortcut;

/**
 * Created by Rostislav on 19.02.2018.
 */

@Dao
public interface ShortcutDAO {

    @Query("Select * from Shortcut")
    List<Shortcut> getAllShortcuts();
    @Delete
    void deleteShortcut(Shortcut shortcut);
    @Query("Delete from Shortcut where position = :position")
    void deleteShortcutByPosition(int position);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertShortcuts(Shortcut... shortcuts);
    @Query("SELECT * FROM Shortcut WHERE position = :position")
    Shortcut get(int position);

}
