package by.equestriadev.nikishin_rostislav.persistence.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import by.equestriadev.nikishin_rostislav.model.ShortcutType;
import by.equestriadev.nikishin_rostislav.persistence.converter.DateConverter;
import by.equestriadev.nikishin_rostislav.persistence.converter.ShortcutTypeConverter;

/**
 * Created by Rostislav on 19.02.2018.
 */

@Entity
@TypeConverters(ShortcutTypeConverter.class)
public class Shortcut {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "position")
    protected int mPosition;

    @NonNull
    @ColumnInfo(name = "url")
    protected String mUrl;

    @NonNull
    @ColumnInfo(name = "title")
    protected String mTitle;

    @NonNull
    @ColumnInfo(name = "type")
    protected ShortcutType mShortcutType;

    public Shortcut(int position,
                    @NonNull String url,
                    @NonNull String title,
                    @NonNull ShortcutType shortcutType) {
        this.mPosition = position;
        this.mUrl = url;
        this.mTitle = title;
        this.mShortcutType = shortcutType;
    }

    public Shortcut() {
    }

    public int getPosition() {
        return mPosition;
    }

    public void setPosition(int position) {
        this.mPosition = position;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    public ShortcutType getShortcutType() {
        return mShortcutType;
    }

    public void setShortcutType(ShortcutType shortcutType) {
        this.mShortcutType = shortcutType;
    }

    @NonNull
    public String getTitle() {
        return mTitle;
    }

    public void setTitle(@NonNull String title) {
        this.mTitle = title;
    }
}
