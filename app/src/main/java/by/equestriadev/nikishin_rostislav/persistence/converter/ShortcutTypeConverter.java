package by.equestriadev.nikishin_rostislav.persistence.converter;

import android.arch.persistence.room.TypeConverter;

import by.equestriadev.nikishin_rostislav.model.ShortcutType;

/**
 * Created by Rostislav on 19.02.2018.
 */

public class ShortcutTypeConverter {

    @TypeConverter
    public static ShortcutType toShortcutType(int ordinal) {
        return ShortcutType.values()[ordinal];
    }

    @TypeConverter
    public static int toOrdinal(ShortcutType status) {
        return status.getNumber();
    }
}
