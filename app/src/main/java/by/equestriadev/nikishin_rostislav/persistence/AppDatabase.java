package by.equestriadev.nikishin_rostislav.persistence;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

import by.equestriadev.nikishin_rostislav.persistence.dao.AppStatisticDao;
import by.equestriadev.nikishin_rostislav.persistence.entity.AppStatistics;

/**
 * Created by Rostislav on 12.02.2018.
 */
@Database(entities = {AppStatistics.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;
    private static final String DATABASE_NAME = "database1";
    public abstract AppStatisticDao AppStatisticsModel();

    public static AppDatabase getDatabase(Context context) {

        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class,
                            DATABASE_NAME)
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        if(INSTANCE != null && INSTANCE.isOpen())
            INSTANCE.close();
        INSTANCE = null;
    }
}
