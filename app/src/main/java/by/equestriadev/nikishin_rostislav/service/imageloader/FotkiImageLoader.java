package by.equestriadev.nikishin_rostislav.service.imageloader;

import android.support.annotation.Nullable;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import by.equestriadev.nikishin_rostislav.service.ImageLoader;

import static org.xmlpull.v1.XmlPullParser.START_TAG;

/**
 * Created by Rostislav on 13.02.2018.
 */

public class FotkiImageLoader extends ImageLoader {

    @Nullable
    @Override
    protected String parse(InputStream input) {
        List<String> linkList = new ArrayList<>();
        final XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(input, null);
            while (parser.next() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() == START_TAG  && "content".equals(parser.getName())) {
                    for (int i = 0; i < parser.getAttributeCount(); i++) {
                        if ("src".equals(parser.getAttributeName(i))) {
                            linkList.add(parser.getAttributeValue(i));
                        }
                    }
                }
            }

            input.close();
            Log.d(getClass().getName(), linkList.size() + " links in array");
            if(linkList.size() > 0){
                return linkList.get(new Random().nextInt(linkList.size()));
            }
        }
        catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected String buildURL() {
        final Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        final String formattedDate = dateFormat.format(calendar.getTime());

        return "http://api-fotki.yandex.ru/api/podhistory/poddate;" +
                formattedDate + "T12:00:00Z/";
    }
}
