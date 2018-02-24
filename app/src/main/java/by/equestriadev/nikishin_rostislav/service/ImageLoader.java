package by.equestriadev.nikishin_rostislav.service;

/**
 * Created by Rostislav on 13.02.2018.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public abstract class ImageLoader {

    private String mImageUrl;
    private String mMock;

    @Nullable
    protected abstract String parse(InputStream input);

    @Nullable
    Bitmap loadBitmap(String srcUrl) {
        try {
            URL url = new URL(srcUrl);
            URLConnection urlConnection = url.openConnection();
            InputStream is = urlConnection.getInputStream();
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            int nRead;
            byte[] data = new byte[16384];

            while ((nRead = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();
            is.close();
            byte[] bitmap = buffer.toByteArray();

            return BitmapFactory.decodeByteArray(bitmap, 0, bitmap.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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