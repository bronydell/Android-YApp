package by.equestriadev.nikishin_rostislav.model;

import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;

/**
 * Created by Rostislav on 14.02.2018.
 */

public class ApplicationInfo {

    private String mAppname = "";
    private String mPackageName = "";
    private String mActivityName = "";
    private Drawable mIcon;

    public ApplicationInfo() {
    }

    public ApplicationInfo(ResolveInfo info, PackageManager manager) {
        mAppname = info.loadLabel(manager).toString();
        mIcon = info.loadIcon(manager);
        mPackageName = info.activityInfo.packageName;
        mActivityName = info.activityInfo.name;
    }

    public ApplicationInfo(String appName, String packageName, String activityName, Drawable icon) {
        this.mAppname = appName;
        this.mPackageName = packageName;
        this.mActivityName = activityName;
        this.mIcon = icon;
    }


    public String getAppname() {
        return mAppname;
    }

    public void setAppname(String mAppname) {
        this.mAppname = mAppname;
    }

    public String getPackageName() {
        return mPackageName;
    }

    public void setPackageName(String mPackagename) {
        this.mPackageName = mPackagename;
    }

    public Drawable getIcon() {
        return mIcon;
    }

    public void setIcon(Drawable mIcon) {
        this.mIcon = mIcon;
    }

    public String getActivityName() {
        return mActivityName;
    }

    public void setActivityName(String activityName) {
        this.mActivityName = activityName;
    }
}
