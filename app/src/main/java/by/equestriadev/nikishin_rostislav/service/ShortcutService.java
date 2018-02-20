package by.equestriadev.nikishin_rostislav.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Rostislav on 19.02.2018.
 */

public class ShortcutService extends Service {

    public static final String BROADCAST_ACTION_ADD_SHORTCUT =
            "by.equestriadev.nikishin_rostislav.ADD_SHORTCUT";
    public static final String BROADCAST_ACTION_REMOVE_SHORTCUT =
            "by.equestriadev.nikishin_rostislav.REMOVE_SHORTCUT";
    public static final String SERVICE_ACTION_ADD_SHORTCUT =
            "by.equestriadev.nikishin_rostislav.SERVICE_ADD_SHORTCUT";
    public static final String SERVICE_ACTION_REMOVE_SHORTCUT =
            "by.equestriadev.nikishin_rostislav.SERVICE_REMOVE_SHORTCUT";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(getClass().getName(), "OnBind");
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(final Intent intent,
                              final int flags,
                              final int startId) {
        if(intent != null && intent.getData() != null) {
            String action = intent.getAction();
            if (SERVICE_ACTION_ADD_SHORTCUT.equals(action)) {
                Log.d(getClass().getName(), action);
                final Intent broadcastIntent = new Intent(BROADCAST_ACTION_ADD_SHORTCUT);
                sendBroadcast(broadcastIntent);
            } else if (SERVICE_ACTION_REMOVE_SHORTCUT.equals(action)) {
                final Intent broadcastIntent = new Intent(BROADCAST_ACTION_REMOVE_SHORTCUT);
                sendBroadcast(broadcastIntent);
            }

        }
        return super.onStartCommand(intent, flags, startId);
    }



}
