package by.equestriadev.nikishin_rostislav.comporators;

import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.Comparator;

import by.equestriadev.nikishin_rostislav.model.App;

/**
 * Created by Rostislav on 04.02.2018.
 */

public class AlphabetComparator implements Comparator<App> {

    private PackageManager mPackageManager;

    public AlphabetComparator(PackageManager packageManager){
        this.mPackageManager = packageManager;
    }
    @Override
    public int compare(App o1, App o2) {
        return o1.getResolveInfo().loadLabel(mPackageManager).toString()
                .compareTo(o2.getResolveInfo().loadLabel(mPackageManager).toString());
    }
}
