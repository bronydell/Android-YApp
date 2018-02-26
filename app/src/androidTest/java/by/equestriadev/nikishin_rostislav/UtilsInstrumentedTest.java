package by.equestriadev.nikishin_rostislav;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.List;
import java.util.Random;

import by.equestriadev.nikishin_rostislav.model.App;
import by.equestriadev.nikishin_rostislav.model.ShortcutType;
import by.equestriadev.nikishin_rostislav.persistence.AppDatabase;
import by.equestriadev.nikishin_rostislav.persistence.entity.Shortcut;

import static org.junit.Assert.assertEquals;

/**
 * Created by Bronydell on 2/23/18.
 */
@RunWith(AndroidJUnit4.class)
public class UtilsInstrumentedTest {

    private Context context;
    private AppDatabase db;

    @Before
    public void Setup() {
        context = InstrumentationRegistry.getTargetContext();
        db = AppDatabase.getDatabase(context);
    }

    @Test
    public void getListOfApps() throws Exception {
        AppUtils utils = new AppUtils(context, db);
        assertEquals(utils.getSortedApps("none", false).size() > 0, true);
    }

    @Test
    public void addFavorite() throws Exception {
        AppUtils utils = new AppUtils(context, db);
        List<App> appList = utils.getSortedApps("none", false);
        App app = appList.get(new Random().nextInt(appList.size()));
        app.getStatistics().setFavorite(true);
        db.AppStatisticsModel().insertApps(app.getStatistics());
        appList = utils.getSortedApps("none", true);
        boolean isFaved = false;
        for (App application :
                appList) {
            if (application.getStatistics().isFavorite() &&
                    app.getApplicationInfo().getPackageName().equals(application.getApplicationInfo().getPackageName())) {
                isFaved = true;
                break;
            }
        }
        assertEquals(isFaved, true);
    }


    @Test
    public void addUsage() throws Exception {
        AppUtils utils = new AppUtils(context, db);
        List<App> appList = utils.getSortedApps("none", false);
        App app = appList.get(new Random().nextInt(appList.size()));
        app.getStatistics().addUsage();
        db.AppStatisticsModel().insertApps(app.getStatistics());
        appList = utils.getSortedApps("none", true);
        boolean rightUsage = false;
        for (App application :
                appList) {
            if (application.getStatistics().getUsageCounter() == app.getStatistics().getUsageCounter() &&
                    app.getApplicationInfo().getPackageName().equals(application.getApplicationInfo().getPackageName())) {
                rightUsage = true;
                break;
            }
        }
        assertEquals(rightUsage, true);
    }


    @Test
    public void addDate() throws Exception {
        AppUtils utils = new AppUtils(context, db);
        List<App> appList = utils.getSortedApps("none", false);
        App app = appList.get(new Random().nextInt(appList.size()));
        app.getStatistics().setLastUsage(new Date());
        db.AppStatisticsModel().insertApps(app.getStatistics());
        appList = utils.getSortedApps("none", true);
        boolean rightDate = false;
        for (App application :
                appList) {
            if (app.getApplicationInfo().getPackageName().equals(application.getApplicationInfo().getPackageName()) &&
                    application.getStatistics().getLastUsage().equals(app.getStatistics().getLastUsage())) {
                rightDate = true;
                break;
            }
        }
        assertEquals(rightDate, true);
    }

    @Test
    public void addShortcut() throws Exception {
        AppUtils utils = new AppUtils(context, db);
        utils.addShortcut(
                new Shortcut(0, "https://bronydell.xyz", "bronydell", ShortcutType.URL), 25);
        assertEquals(utils.getShortcuts().size() > 0, true);
    }
}
