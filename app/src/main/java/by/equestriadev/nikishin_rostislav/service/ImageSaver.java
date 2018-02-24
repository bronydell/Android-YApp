package by.equestriadev.nikishin_rostislav.service;

/**
 * Created by Rostislav on 13.02.2018.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageSaver {


    private static final Object mLock = new Object();
    private static final String DIRECTORY_NAME = "images";
    private static volatile ImageSaver sInstance;

    private ImageSaver() {
    }

    public static ImageSaver getInstance() {
        if (null == sInstance) {
            synchronized (mLock) {
                if (null == sInstance) {
                    sInstance = new ImageSaver();
                }
            }
        }

        return sInstance;
    }

    void saveImage(final Context context, final Bitmap bitmap, final String fileName) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(createFile(context, fileName));
            Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            if (mutableBitmap.getWidth() >= mutableBitmap.getHeight()){

                mutableBitmap = Bitmap.createBitmap(
                        mutableBitmap,
                        mutableBitmap.getWidth()/2 - mutableBitmap.getHeight()/2,
                        0,
                        mutableBitmap.getHeight(),
                        mutableBitmap.getHeight()
                );

            }else{
                mutableBitmap = Bitmap.createBitmap(
                        mutableBitmap,
                        0,
                        mutableBitmap.getHeight()/2 - mutableBitmap.getWidth()/2,
                        mutableBitmap.getWidth(),
                        mutableBitmap.getWidth()
                );
            }
            Canvas canvas = new Canvas(mutableBitmap);
            Paint paint2 = new Paint();
            paint2.setColor(Color.argb(125, 255, 255, 255));
            paint2.setStyle(Paint.Style.FILL);
            canvas.drawPaint(paint2);
            mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 70, fileOutputStream);
            mutableBitmap.recycle();
            bitmap.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                    bitmap.recycle();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @NonNull
    private File createFile(final Context context, final String fileName) {
        File directory = context.getDir(DIRECTORY_NAME, Context.MODE_PRIVATE);

        return new File(directory, fileName);
    }

    public Bitmap loadImage(final Context context, final String fileName) {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(createFile(context, fileName));
            return BitmapFactory.decodeStream(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
