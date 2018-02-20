package by.equestriadev.nikishin_rostislav.model;

import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.annotation.NonNull;

import by.equestriadev.nikishin_rostislav.persistence.entity.Shortcut;

/**
 * Created by Rostislav on 19.02.2018.
 */

public class AppShortcut extends Shortcut {


    private ApplicationInfo mApplication;

    public AppShortcut(int position, @NonNull String url,
                       @NonNull String title,
                       @NonNull ShortcutType shortcutType) {
        super(position, url, title, shortcutType);
    }


    public AppShortcut(Shortcut shortcut) {
        this.setPosition(shortcut.getPosition());
        this.setShortcutType(shortcut.getShortcutType());
        this.setTitle(shortcut.getTitle());
        this.setUrl(shortcut.getUrl());
    }

    public void setApplication(ResolveInfo info, PackageManager manager){
        mApplication = new ApplicationInfo(info, manager);
    }

    public void setApplication(ApplicationInfo application) {
        this.mApplication = application;
    }

    public ApplicationInfo getApplication(){
        return mApplication;
    }

}
