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

public class DerpiBooImageLoader extends ImageLoader {

    @Nullable
    @Override
    protected String parse(InputStream input) {
        JSONObject root = getJsonObject(input);
        if(root != null){
            try {
                JSONObject imageInfo = root.getJSONArray("search").getJSONObject(0);
                return "https:" + imageInfo.getJSONObject("representations").getString("full");
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

            //returns the json object
            return new JSONObject(responseStrBuilder.toString());

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
        final String search_queue = "Twilight sparkle,-animated".toLowerCase().replace(' ', '+');
        final int min_rating = 100;
        return "https://derpibooru.org/search.json?q="+search_queue+"&min_score="+min_rating;
    }
}
