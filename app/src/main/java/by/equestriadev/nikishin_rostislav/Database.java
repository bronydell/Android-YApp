package by.equestriadev.nikishin_rostislav;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import by.equestriadev.nikishin_rostislav.model.AppStatistics;

/**
 * Created by Rostislav on 04.02.2018.
 */

public class Database extends SQLiteOpenHelper {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "database";

    //Tables
    private static final String TABLE_APPS = "apps";

    // Contacts Table Columns names
    private static final String KEY_PACKAGE = "package";
    private static final String KEY_LAST_USAGE = "last_usage";
    private static final String KEY_COUNT = "usage_counter";
    private static final String KEY_FAVORITE = "favorite";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_APPS + "("
                + KEY_PACKAGE + " TEXT PRIMARY KEY," + KEY_LAST_USAGE + " ,"
                + KEY_COUNT + " INTEGER," + KEY_FAVORITE + " integer)";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_APPS);
        onCreate(db);
    }

    public void CreateOrUpdate(AppStatistics statistics){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_PACKAGE, statistics.getPackage());
        if(statistics.getLastUsage() != null)
            initialValues.put(KEY_LAST_USAGE, DATE_FORMAT.format(statistics.getLastUsage()));
        initialValues.put(KEY_COUNT, statistics.getUsageCounter());
        initialValues.put(KEY_FAVORITE, statistics.isFavorite());
        int id = (int) db.insertWithOnConflict(TABLE_APPS, null, initialValues, SQLiteDatabase.CONFLICT_IGNORE);
        if(id == -1){
            db.update(TABLE_APPS, initialValues, KEY_PACKAGE+"=?", new String[] {statistics.getPackage()});
        }
        db.close();
    }

    public void DeleteApp(String packageName){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_APPS, KEY_PACKAGE + " = ?",
                new String[] {packageName});
        db.close();
    }

    public AppStatistics getApp(String packageName) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_APPS, new String[] { KEY_PACKAGE,
                        KEY_LAST_USAGE, KEY_COUNT, KEY_FAVORITE }, KEY_PACKAGE + "=?",
                new String[] { packageName }, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {

            try {
                if(cursor.getString(1) != null)
                    return  new AppStatistics(
                            cursor.getString(0),
                            cursor.getInt(2),
                            DATE_FORMAT.parse(cursor.getString(1)),
                            cursor.getInt(3) == 1
                    );
                else
                    return  new AppStatistics(
                            cursor.getString(0),
                            cursor.getInt(2),
                            null,
                            cursor.getInt(3) == 1
                    );

            } catch (ParseException e) {
                e.printStackTrace();
                return new AppStatistics(packageName, 0, null, false);
            }
        }
        else
            return new AppStatistics(packageName, 0, null, false);
    }

    public Map<String, AppStatistics> GetAllApps(){
        Map<String, AppStatistics> appList = new HashMap<>();

        String selectQuery = "SELECT * FROM " + TABLE_APPS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                try {
                    AppStatistics statistics = new AppStatistics(
                            cursor.getString(0),
                            cursor.getInt(2),
                            DATE_FORMAT.parse(cursor.getString(1)),
                            cursor.getInt(3) == 1
                    );
                    appList.put(statistics.getPackage(), statistics);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }

        return appList;
    }
}
