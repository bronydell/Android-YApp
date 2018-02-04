package by.equestriadev.nikishin_rostislav.comporators;

import android.content.pm.ResolveInfo;

import java.util.Comparator;

import by.equestriadev.nikishin_rostislav.Database;
import by.equestriadev.nikishin_rostislav.model.AppStatistics;

/**
 * Created by Rostislav on 04.02.2018.
 */

public class FrequencyComparator  implements Comparator<ResolveInfo> {

    private Database mDb;

    public FrequencyComparator(Database db){
        this.mDb = db;
    }
    @Override
    public int compare(ResolveInfo o1, ResolveInfo o2) {
        AppStatistics stat1 = mDb.getApp(o1.activityInfo.packageName);
        AppStatistics stat2 = mDb.getApp(o2.activityInfo.packageName);
        if(stat1.getUsageCounter() > stat2.getUsageCounter())
            return 1;
        else if(stat1.getUsageCounter() < stat2.getUsageCounter())
            return -1;
        return 0;
    }

}
