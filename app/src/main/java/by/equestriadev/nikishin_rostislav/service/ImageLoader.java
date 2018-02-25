package by.equestriadev.nikishin_rostislav.service;

/**
 * Created by Rostislav on 13.02.2018.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public abstract class ImageLoader {

    private String mImageUrl;
    private String mMock;

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    @Nullable
    protected abstract String parse(InputStream input);

    @Nullable
    Bitmap loadBitmap(String srcUrl) {
        try {
            URL url = new URL(srcUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inDither = false;                     //Disable Dithering mode
            opts.inPurgeable = true;                   //Tell to gc that whether it needs free memory, the Bitmap can be cleared
            opts.inInputShareable = true;              //Which kind of reference will be used to recover the Bitmap data after being clear, when it will be used in the future
            opts.inTempStorage = new byte[128 * 1024];
            opts.inSampleSize = 2;
            return BitmapFactory.decodeStream(input, null, opts);
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    @Nullable
    public String getImageUrl() {
        mImageUrl = fetchURL();
        if (mImageUrl != null) {
            return mImageUrl;
        } else {
            return null;
        }
    }

    @Nullable
    private InputStream fetchURL(URL url){
        try {
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(5000);
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                return connection.getInputStream();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected abstract String buildURL();

    @Nullable
    private String fetchURL() {
            try {
                final URL url = new URL(buildURL());
                final InputStream inputStream;
                if (mMock == null)
                    inputStream = fetchURL(url);
                else {
                    inputStream = new ByteArrayInputStream(mMock.getBytes("UTF-8"));
                }
                if(inputStream != null){
                    String imageURL = parse(inputStream);
                    if (imageURL != null)
                        this.mImageUrl = imageURL;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        return mImageUrl;
    }

    public void setMock(String mock) {
        this.mMock = mock;
    }
}