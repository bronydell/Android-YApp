package by.equestriadev.nikishin_rostislav;

import android.content.Context;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import by.equestriadev.nikishin_rostislav.model.App;
import by.equestriadev.nikishin_rostislav.model.ApplicationInfo;
import by.equestriadev.nikishin_rostislav.persistence.entity.AppStatistics;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Bronydell on 2/23/18.
 */

@RunWith(RobolectricTestRunner.class)
public class UtilsUnitTest {
    private Context context = Robolectric.buildActivity(MainActivity.class).get();

    @Test
    public void appsFetchTest() {
        AppUtils utils = new AppUtils(context, null);
        assert (utils.getSortedApps("sort_az", false).size() > 0);
    }

    @Test
    public void emptyShortcutFetchTest() {
        AppUtils utils = new AppUtils(context, null);
        assert (utils.getShortcuts().size() == 0);
    }

    @Test
    public void AZTest() {
        AppUtils utils = new AppUtils(context, null);
        App a1 = new App(new ApplicationInfo("Bapplication", "com.example.app", "com.example.app.launch", null),
                new AppStatistics("com.example.app", 0, new Date(), false));
        App a2 = new App(new ApplicationInfo("Application", "com.example.app2", "com.example.app2.launch", null),
                new AppStatistics("com.example.app2", 0, new Date(), false));
        List<App> appList = new ArrayList<>();
        appList.add(a1);
        appList.add(a2);
        utils.sortApps(appList, "sort_az");
        App expectedFirst = a2;
        assertEquals(appList.get(0) == expectedFirst, true);
    }

    @Test
    public void ZATest() {
        AppUtils utils = new AppUtils(context, null);
        App a1 = new App(new ApplicationInfo("Bapplication", "com.example.app", "com.example.app.launch", null),
                new AppStatistics("com.example.app", 0, new Date(), false));
        App a2 = new App(new ApplicationInfo("Application", "com.example.app2", "com.example.app2.launch", null),
                new AppStatistics("com.example.app2", 0, new Date(), false));
        List<App> appList = new ArrayList<>();
        appList.add(a1);
        appList.add(a2);
        utils.sortApps(appList, "sort_za");
        App expectedFirst = a1;
        assertEquals(appList.get(0) == expectedFirst, true);
    }

    @Test
    public void FrequencyTest() {
        AppUtils utils = new AppUtils(context, null);
        App a1 = new App(new ApplicationInfo("Bapplication", "com.example.app", "com.example.app.launch", null),
                new AppStatistics("com.example.app", 0, new Date(), false));
        App a2 = new App(new ApplicationInfo("Application", "com.example.app2", "com.example.app2.launch", null),
                new AppStatistics("com.example.app2", 1, new Date(), false));
        List<App> appList = new ArrayList<>();
        appList.add(a1);
        appList.add(a2);
        utils.sortApps(appList, "sort_freq");
        App expectedFirst = a2;
        assertEquals(appList.get(0) == expectedFirst, true);
    }

    @Test
    public void FavoriteTest() {
        AppUtils utils = new AppUtils(context, null);
        App a1 = new App(new ApplicationInfo("Bapplication", "com.example.app", "com.example.app.launch", null),
                new AppStatistics("com.example.app", 0, new Date(), true));
        App a2 = new App(new ApplicationInfo("Application", "com.example.app2", "com.example.app2.launch", null),
                new AppStatistics("com.example.app2", 1, new Date(), false));
        List<App> appList = new ArrayList<>();
        appList.add(a1);
        appList.add(a2);
        List<App> favList = utils.getFavoriteApps(appList);
        assertEquals(favList.get(0) == a1, true);
    }

}
