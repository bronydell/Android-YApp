package by.equestriadev.nikishin_rostislav.persistence.conventer;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

/**
 * Created by Rostislav on 12.02.2018.
 */

public class DateConventer {
    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
