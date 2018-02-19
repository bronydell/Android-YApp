package by.equestriadev.nikishin_rostislav.comporator;

import java.util.Comparator;

import by.equestriadev.nikishin_rostislav.model.App;
import by.equestriadev.nikishin_rostislav.persistence.entity.AppStatistics;
import by.equestriadev.nikishin_rostislav.persistence.entity.Shortcut;

/**
 * Created by Rostislav on 19.02.2018.
 */

public class ShortcutComparator  implements Comparator<Shortcut> {


    public ShortcutComparator(){
    }

    @Override
    public int compare(Shortcut o1, Shortcut o2) {
        if(o1.getPosition() > o2.getPosition())
            return 1;
        else if(o1.getPosition() < o2.getPosition())
            return -1;
        return 0;
    }

}