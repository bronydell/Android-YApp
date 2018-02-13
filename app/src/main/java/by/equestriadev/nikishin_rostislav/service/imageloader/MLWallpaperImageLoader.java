package by.equestriadev.nikishin_rostislav.service.imageloader;

import android.support.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import by.equestriadev.nikishin_rostislav.service.ImageLoader;

/**
 * Created by Rostislav on 13.02.2018.
 */

public class MLWallpaperImageLoader  extends ImageLoader {

    @Nullable
    @Override
    protected String parse(InputStream input) {
        JSONObject root = getJsonObject(input);
        if(root != null){
            try {
                JSONObject imageInfo = root.getJSONArray("result").getJSONObject(0);
                String url = imageInfo.getString("downloadurl");
                String[] parts = url.split("/");
                return "https://www.mylittlewallpaper.com/images/o_" + parts[parts.length - 1] + ".png";
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    private JSONObject getJsonObject(InputStream stream){
        try {
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();

            String inputStr;
            while ((inputStr = streamReader.readLine()) != null)
                responseStrBuilder.append(inputStr);

            JSONObject jsonObject = new JSONObject(responseStrBuilder.toString());

            //returns the json object
            return jsonObject;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //if something went wrong, return null
        return null;
    }

    @Override
    protected String buildURL() {
        final String search_queue = "Rick Sanchez";
        return "https://www.mylittlewallpaper.com/c/all/api/v1/random.json?search="+search_queue;
    }
}
