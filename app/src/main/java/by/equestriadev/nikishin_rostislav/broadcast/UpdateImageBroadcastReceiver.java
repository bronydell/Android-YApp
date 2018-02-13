package by.equestriadev.nikishin_rostislav.broadcast;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;

import java.io.FileNotFoundException;
import java.util.Calendar;

import by.equestriadev.nikishin_rostislav.MainActivity;
import by.equestriadev.nikishin_rostislav.service.ImageLoaderService;
import by.equestriadev.nikishin_rostislav.service.ImageSaver;

/**
 * Created by Rostislav on 13.02.2018.
 */

public class UpdateImageBroadcastReceiver extends BroadcastReceiver {

    private Activity mActivity;

    public UpdateImageBroadcastReceiver(Activity mActivity) {
        this.mActivity = mActivity;

        final Context context = mActivity.getApplicationContext();
        final Bitmap bitmap = ImageSaver.getInstance().loadImage(context,
                "background.png");
        final Drawable drawable = new BitmapDrawable(context.getResources(), bitmap);
        mActivity.findViewById(android.R.id.content).setBackground(drawable);
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {
        String action = intent.getAction();
        if (ImageLoaderService.BROADCAST_ACTION_UPDATE_IMAGE.equals(action)) {
            final String imageName = intent.getStringExtra(ImageLoaderService.BROADCAST_PARAM_IMAGE);
            if (!TextUtils.isEmpty(imageName)) {
                final Bitmap bitmap = ImageSaver.getInstance().loadImage(context.getApplicationContext(),
                        imageName);
                final Drawable drawable = new BitmapDrawable(context.getResources(), bitmap);
                mActivity.findViewById(android.R.id.content).setBackground(drawable);
            }
        }
    }
}
