package by.equestriadev.nikishin_rostislav.comporators;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.Comparator;

import by.equestriadev.nikishin_rostislav.Database;
import by.equestriadev.nikishin_rostislav.model.AppStatistics;

/**
 * Created by Rostislav on 04.02.2018.
 */

public class DateComparator implements Comparator<ResolveInfo> {

    private Context mContext;

    public DateComparator(Context ctx){
        this.mContext = ctx;
    }

    @Override
    public int compare(ResolveInfo o1, ResolveInfo o2) {
        try {
            long installed1 = mContext
                    .getPackageManager()
                    .getPackageInfo(o1.activityInfo.packageName, 0)
                    .firstInstallTime;
            long installed2 = mContext
                    .getPackageManager()
                    .getPackageInfo(o2.activityInfo.packageName, 0)
                    .firstInstallTime;
            if(installed1 > installed2)
                return 1;
            else if(installed1 < installed2)
                return -1;
            return 0;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

}
