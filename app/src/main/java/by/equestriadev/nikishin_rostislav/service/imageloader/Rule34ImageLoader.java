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

public class Rule34ImageLoader  extends ImageLoader {

    @Nullable
    @Override
    protected String parse(InputStream input) {
        List<String> urls = new ArrayList<>();
        final XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(input, null);
            while (parser.next() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() == START_TAG  && "post".equals(parser.getName())) {
                    for (int i = 0; i < parser.getAttributeCount(); i++) {
                        if ("file_url".equals(parser.getAttributeName(i))) {
                            urls.add(parser.getAttributeValue(i));
                        }
                    }
                }
            }
        }
        catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        if(urls.size() > 0)
            return urls.get(new Random().nextInt(urls.size()));
        return null;
    }

    @Override
    protected String buildURL() {
        final String searchQueue = "rating:safe score:>20 overwatch";
        return "https://rule34.xxx/index.php?page=dapi&s=post&q=index&tags="+searchQueue;
    }
}
