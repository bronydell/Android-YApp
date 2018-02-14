package by.equestriadev.nikishin_rostislav.comporator;

import android.content.Context;
import android.content.pm.PackageManager;

import java.util.Comparator;

import by.equestriadev.nikishin_rostislav.model.App;

/**
 * Created by Rostislav on 04.02.2018.
 */

public class DateComparator implements Comparator<App> {

    private Context mContext;

    public DateComparator(Context ctx){
        this.mContext = ctx;
    }

    @Override
    public int compare(App o1, App o2) {
        try {
            long installed1 = mContext
                    .getPackageManager()
                    .getPackageInfo(o1.getApplicationInfo().getPackageName(), 0)
                    .firstInstallTime;
            long installed2 = mContext
                    .getPackageManager()
                    .getPackageInfo(o2.getApplicationInfo().getPackageName(), 0)
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
