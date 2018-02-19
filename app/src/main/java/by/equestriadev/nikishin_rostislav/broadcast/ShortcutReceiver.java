package by.equestriadev.nikishin_rostislav.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import by.equestriadev.nikishin_rostislav.fragment.IUpdatable;

/**
 * Created by Rostislav on 19.02.2018.
 */

public class ShortcutReceiver extends BroadcastReceiver {

    Context context;
    private IUpdatable shortcutFragemnt;

    public ShortcutReceiver(IUpdatable fragment) {
        this.shortcutFragemnt = fragment;
    }


    public ShortcutReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        Log.d(this.getClass().getName(), intent.getAction());
        shortcutFragemnt.update();
    }
}