package by.equestriadev.nikishin_rostislav.comporators;

import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.Comparator;

/**
 * Created by Rostislav on 04.02.2018.
 */

public class AlphabetComparator implements Comparator<ResolveInfo> {

    private PackageManager mPackageManager;

    public AlphabetComparator(PackageManager packageManager){
        this.mPackageManager = packageManager;
    }
    @Override
    public int compare(ResolveInfo o1, ResolveInfo o2) {
        return o1.loadLabel(mPackageManager).toString().compareTo(o2.loadLabel(mPackageManager).toString());
    }
}
