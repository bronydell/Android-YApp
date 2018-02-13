package by.equestriadev.nikishin_rostislav;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.crashlytics.android.Crashlytics;
import com.yandex.metrica.YandexMetrica;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import by.equestriadev.nikishin_rostislav.broadcast.UpdateImageBroadcastReceiver;
import by.equestriadev.nikishin_rostislav.fragment.AppLauncherFragment;
import by.equestriadev.nikishin_rostislav.fragment.ListOfAppsFragment;
import by.equestriadev.nikishin_rostislav.fragment.SettingsFragment;
import by.equestriadev.nikishin_rostislav.service.ImageLoaderService;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        View.OnLongClickListener{

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.navView)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawer;

    private SharedPreferences prefs;
    private UpdateImageBroadcastReceiver mImageReceiver;
    private static final int DEFAULT_NAV_STATE = R.id.nav_grid;

    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String themeName = prefs.getString(getString(R.string.theme_key),
                getString(R.string.default_theme));
        switch (themeName){
            case "light":
                this.setTheme(R.style.AppTheme);
                break;
            case "dark":
                this.setTheme(R.style.AppTheme_Dark);
                break;
        }
        setContentView(R.layout.activity_main);
        // I'm just too lazy to rewrite old code, so to make opposite statement I've just added NO to it
        // Hate me later bro
        if (!(prefs.getBoolean("visited", false) && !prefs.getBoolean("shouldVisit", false)))
            goToWelcome();
        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            YandexMetrica.reportEvent("Main activity configuration is portrait");
        } else {
            YandexMetrica.reportEvent("Main activity configuration is landscape");
        }
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        drawerToggle = setupDrawerToggle();
        mDrawer.addDrawerListener(drawerToggle);
        setupDrawer(navView);
        if(savedInstanceState == null) {
            navView.setCheckedItem(DEFAULT_NAV_STATE);
            onNavigationItemSelected(navView.getMenu().findItem(DEFAULT_NAV_STATE));
        }
    }

    public void loopIt() {
        Calendar cur_cal = Calendar.getInstance();
        Intent intent = new Intent(this, ImageLoaderService.class);
        intent.setAction(ImageLoaderService.SERVICE_ACTION_LOAD_IMAGE);
        PendingIntent pintent = PendingIntent.getService(getApplicationContext(),
                0, intent, 0);
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        cur_cal.setTimeInMillis(System.currentTimeMillis());
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cur_cal.getTimeInMillis(),
                60 * 1000, pintent);
    }

    private void setupDrawer(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(this);
        View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header);
        ImageView ivHeaderPhoto = headerLayout.findViewById(R.id.nav_header_avatar);
        ivHeaderPhoto.setOnLongClickListener(this);
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close);
    }

    private void DeselectAll(){
        for (int i = 0; i < navView.getMenu().size(); i++) {
            navView.getMenu().getItem(i).setChecked(false);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        YandexMetrica.reportEvent("Configuration has changed in Main Activity");
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        Class fragmentClass = null;
        switch(item.getItemId()) {
            case R.id.nav_grid:
                fragmentClass = AppLauncherFragment.class;
                break;
            case R.id.nav_list:
                fragmentClass = ListOfAppsFragment.class;
                break;
            case R.id.nav_settings:
                fragmentClass = SettingsFragment.class;
                break;
        }
        if(fragmentClass != null) {
            try {
                fragment = (Fragment) fragmentClass.newInstance();
                mDrawer.closeDrawers();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragmentManager, fragment).commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean onLongClick(View v) {
        YandexMetrica.reportEvent("Open about activity");
        DeselectAll();
        final Intent myIntent = new Intent(this, AboutActivity.class);
        this.startActivity(myIntent);
        return true;
    }

    private void goToWelcome() {
        YandexMetrica.reportEvent("Launched welcome activity");
        Intent launcherIntent = new Intent(getApplicationContext(), WelcomeActivity.class);
        startActivity(launcherIntent);
        finish();
    }
    public void InitReceiver() {
        loopIt();
        mImageReceiver = new UpdateImageBroadcastReceiver(this);
        registerReceiver(mImageReceiver,
                new IntentFilter(ImageLoaderService.BROADCAST_ACTION_UPDATE_IMAGE));
    }

    @Override
    protected void onResume() {
        super.onResume();
        InitReceiver();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(mImageReceiver);
        super.onPause();
    }
}
