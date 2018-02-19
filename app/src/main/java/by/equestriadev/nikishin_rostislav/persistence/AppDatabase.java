package by.equestriadev.nikishin_rostislav.persistence;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

import by.equestriadev.nikishin_rostislav.persistence.dao.AppStatisticDao;
import by.equestriadev.nikishin_rostislav.persistence.dao.ShortcutDAO;
import by.equestriadev.nikishin_rostislav.persistence.entity.AppStatistics;
import by.equestriadev.nikishin_rostislav.persistence.entity.Shortcut;

/**
 * Created by Rostislav on 12.02.2018.
 */
@Database(entities = {AppStatistics.class, Shortcut.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;
    private static final String DATABASE_NAME = "database1";
    public abstract AppStatisticDao AppStatisticsModel();
    public abstract ShortcutDAO ShortcutModel();

    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE `Shortcut` (`url` TEXT NOT NULL, "
                    + "`title` TEXT NOT NULL," +
                    "`position` INTEGER NOT NULL, "+
                    "`type` INTEGER NOT NULL, PRIMARY KEY(`position`));");
        }
    };

    public static AppDatabase getDatabase(Context context) {

        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class,
                            DATABASE_NAME)
                            .addMigrations(MIGRATION_2_3)
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
