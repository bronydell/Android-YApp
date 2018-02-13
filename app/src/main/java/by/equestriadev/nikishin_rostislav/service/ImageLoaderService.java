package by.equestriadev.nikishin_rostislav.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.yandex.metrica.YandexMetrica;

import by.equestriadev.nikishin_rostislav.R;
import by.equestriadev.nikishin_rostislav.service.imageloader.DerpiBooImageLoader;
import by.equestriadev.nikishin_rostislav.service.imageloader.FotkiImageLoader;
import by.equestriadev.nikishin_rostislav.service.imageloader.MLWallpaperImageLoader;
import by.equestriadev.nikishin_rostislav.service.imageloader.Rule34ImageLoader;

/**
 * Created by Rostislav on 13.02.2018.
 */

public class ImageLoaderService extends Service {

    private static final String TAG = "Shad";

    public static final String SERVICE_ACTION_LOAD_IMAGE = "by.equestriadev.nikishin_rostislav.LOAD_IMAGE";
    public static final String BROADCAST_ACTION_UPDATE_IMAGE =
            "by.equestriadev.nikishin_rostislav.UPDATE_IMAGE";
    public static final String BROADCAST_PARAM_IMAGE = "by.equestriadev.nikishin_rostislav.IMAGE";


    private ImageLoader mImageLoader;

    public ImageLoaderService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(final Intent intent,
                              final int flags,
                              final int startId) {
        String action = intent.getAction();
        if (SERVICE_ACTION_LOAD_IMAGE.equals(action)) {
            load(startId);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void load(final int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                final SharedPreferences prefs = PreferenceManager.
                        getDefaultSharedPreferences(getApplicationContext());

                final SharedPreferences.Editor edit = prefs.edit();
                final Long tsLong = System.currentTimeMillis()/1000;
                final long freq =  Long.parseLong(prefs.getString(getString(R.string.freq_key), getResources().
                        getString(R.string.default_freq)), 10);
                if(tsLong - freq > prefs.getLong("lastWallUpdate", 0) || prefs.getBoolean("forcedWallpaper", false)){
                    switch (prefs.getString(getString(R.string.wallpaper_source_key),
                            getString(R.string.default_source))){
                        case "ya":
                            mImageLoader = new FotkiImageLoader();
                            YandexMetrica.reportEvent("Downloading wallpaper from Yandex Fotki");
                            break;
                        case "derp":
                            mImageLoader = new DerpiBooImageLoader();
                            YandexMetrica.reportEvent("Downloading wallpaper from Derpibooru");
                            break;
                        case "mlw":
                            mImageLoader = new MLWallpaperImageLoader();
                            YandexMetrica.reportEvent("Downloading wallpaper from My little wallpaper");
                            break;
                        case "rule34":
                            mImageLoader = new Rule34ImageLoader();
                            YandexMetrica.reportEvent("Downloading wallpaper from Rule34");
                            break;
                    }
                    final String imageUrl = mImageLoader.getImageUrl();
                    if (!TextUtils.isEmpty(imageUrl)) {
                        final Bitmap bitmap = mImageLoader.loadBitmap(imageUrl);
                        final String imageName = "background.png";
                        ImageSaver.getInstance().saveImage(getApplicationContext(), bitmap, imageName);
                        edit.putLong("lastWallUpdate", tsLong);
                        edit.putBoolean("forcedWallpaper", false);
                        edit.apply();
                        final Intent intent = new Intent(BROADCAST_ACTION_UPDATE_IMAGE);
                        intent.putExtra(BROADCAST_PARAM_IMAGE, imageName);
                        sendBroadcast(intent);

                    }
                }
            }
        }).start();

        stopSelf(startId);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "MyService, onDestroy");
        super.onDestroy();
    }
}