package by.equestriadev.nikishin_rostislav.comporator;

import java.util.Comparator;

import by.equestriadev.nikishin_rostislav.model.App;

/**
 * Created by Rostislav on 04.02.2018.
 */

public class AlphabetComparator implements Comparator<App> {


    public AlphabetComparator(){}
    @Override
    public int compare(App o1, App o2) {
        return o1.getApplicationInfo().getAppname()
                .compareTo(o2.getApplicationInfo().getAppname());
    }
}
