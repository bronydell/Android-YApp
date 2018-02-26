package by.equestriadev.nikishin_rostislav.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import by.equestriadev.nikishin_rostislav.fragment.IUpdatable;

/**
 * Created by Rostislav on 04.02.2018.
 */

public class AppReceiver extends BroadcastReceiver {

    public static final String CHANGE_BROADCAST = "by.equestriadev.nikishin_rostislav.SOMETHING_HAS_CHANGED";
    Context context;
    private IUpdatable appGridLauncherFragment;

    public AppReceiver(IUpdatable fragment) {
        this.appGridLauncherFragment = fragment;
    }


    public AppReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(getClass().getName(), intent.getAction() + " received action");
        this.context = context;
        if (appGridLauncherFragment != null)
            appGridLauncherFragment.update();
    }
}