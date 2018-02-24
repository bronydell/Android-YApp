package by.equestriadev.nikishin_rostislav.broadcast;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.yandex.metrica.push.YandexMetricaPush;

import by.equestriadev.nikishin_rostislav.AboutActivity;
import by.equestriadev.nikishin_rostislav.R;
import by.equestriadev.nikishin_rostislav.fragment.IUpdatable;

/**
 * Created by Bronydell on 2/24/18.
 */

public class SilentPushReceiver extends BroadcastReceiver {

    private static final String CHANNEL = "123456";
    private IUpdatable aboutFragemnt;

    public SilentPushReceiver(IUpdatable fragment) {
        this.aboutFragemnt = fragment;
    }

    public SilentPushReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(context.getPackageName() + ".action.ymp.SILENT_PUSH_RECEIVE")) {
            Log.d(getClass().getName(), "Received from Yandex Push " +
                    intent.getStringExtra(YandexMetricaPush.EXTRA_PAYLOAD));
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            sharedPreferences.edit()
                    .putString("silent_message", intent.getStringExtra(YandexMetricaPush.EXTRA_PAYLOAD))
                    .apply();
            if (aboutFragemnt != null)
                aboutFragemnt.update();
            else {
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL)
                        .setSmallIcon(R.drawable.ic_stat_name)
                        .setContentTitle(context.getString(R.string.silent_push_title))
                        .setContentText(intent.getStringExtra(YandexMetricaPush.EXTRA_PAYLOAD))
                        .setPriority(NotificationCompat.PRIORITY_LOW);
                Intent notificationIntent = new Intent(context, AboutActivity.class);
                PendingIntent pendIntent = PendingIntent.getActivity(context, 0,
                        notificationIntent, 0);
                mBuilder.setContentIntent(pendIntent);
                Notification notification = mBuilder.build();
                notification.flags |= Notification.FLAG_AUTO_CANCEL;
                notificationManager.notify(0, notification);
            }
        }
    }
}