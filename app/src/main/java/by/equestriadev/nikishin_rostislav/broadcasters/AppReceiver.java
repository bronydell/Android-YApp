package by.equestriadev.nikishin_rostislav.broadcasters;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import by.equestriadev.nikishin_rostislav.fragments.AppLauncherFragment;

/**
 * Created by Rostislav on 04.02.2018.
 */

public class AppReceiver extends BroadcastReceiver {

    Context context;
    private AppLauncherFragment appLauncherFragment;

    public AppReceiver(AppLauncherFragment fragment) {
        this.appLauncherFragment = fragment;
    }

    public AppReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;
        Log.d(getClass().getName(), "RECEIVED: " + intent.getAction());
        // This condition will be called when package removed
        if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED") ||
                intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
            if(appLauncherFragment != null)
                appLauncherFragment.updateAppList();
        }
    }
}