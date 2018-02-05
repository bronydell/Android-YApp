package by.equestriadev.nikishin_rostislav.comporators;

import android.content.pm.ResolveInfo;

import java.util.Comparator;

import by.equestriadev.nikishin_rostislav.Database;
import by.equestriadev.nikishin_rostislav.model.App;
import by.equestriadev.nikishin_rostislav.model.AppStatistics;

/**
 * Created by Rostislav on 04.02.2018.
 */

public class FrequencyComparator  implements Comparator<App> {


    public FrequencyComparator(){
    }

    @Override
    public int compare(App o1, App o2) {
        AppStatistics stat1 = o1.getStatistics();
        AppStatistics stat2 = o2.getStatistics();
        if(stat1.getUsageCounter() > stat2.getUsageCounter())
            return 1;
        else if(stat1.getUsageCounter() < stat2.getUsageCounter())
            return -1;
        return 0;
    }

}
