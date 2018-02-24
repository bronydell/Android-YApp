package by.equestriadev.nikishin_rostislav;

import android.app.Application;

import com.yandex.metrica.YandexMetrica;
import com.yandex.metrica.push.YandexMetricaPush;

/**
 * Created by Rostislav on 11.02.2018.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        YandexMetrica.activate(getApplicationContext(),
                "434bd221-69ac-461e-af4e-e7d8bb1d0945");
        YandexMetrica.enableActivityAutoTracking(this);
        YandexMetricaPush.init(getApplicationContext());

    }
}
