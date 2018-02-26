package by.equestriadev.nikishin_rostislav;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import by.equestriadev.nikishin_rostislav.service.ImageLoader;
import by.equestriadev.nikishin_rostislav.service.imageloader.DerpiBooImageLoader;
import by.equestriadev.nikishin_rostislav.service.imageloader.FotkiImageLoader;
import by.equestriadev.nikishin_rostislav.service.imageloader.MLWallpaperImageLoader;
import by.equestriadev.nikishin_rostislav.service.imageloader.Rule34ImageLoader;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Bronydell on 2/23/18.
 */

@RunWith(RobolectricTestRunner.class)
public class ImageUnitTest {
    @Test
    public void DerpibooTest() {
        ImageLoader loader = new DerpiBooImageLoader();
        loader.setMock(readFile("testData/derpi.resp"));
        System.out.println(loader.getImageUrl());
        assertEquals(loader.getImageUrl() != null, true);
    }

    @Test
    public void MLWTest() {
        ImageLoader loader = new MLWallpaperImageLoader();
        loader.setMock(readFile("testData/mlw.resp"));
        System.out.println(loader.getImageUrl());
        assertEquals(loader.getImageUrl() != null, true);
    }

    @Test
    public void Rule34Test() {
        ImageLoader loader = new Rule34ImageLoader();
        loader.setMock(readFile("testData/rule34.resp"));
        System.out.println(loader.getImageUrl());

        assertEquals(loader.getImageUrl() != null, true);
    }

    @Test
    public void YandexFotkiTest() {
        ImageLoader loader = new FotkiImageLoader();
        loader.setMock(readFile("testData/fotki.resp"));
        System.out.println(loader.getImageUrl());
        assertEquals(loader.getImageUrl() != null, true);
    }

    public String readFile(String filename) {
        String content = null;
        File file = new File(filename); //for ex foo.txt
        FileReader reader = null;
        try {
            reader = new FileReader(file);
            char[] chars = new char[(int) file.length()];
            reader.read(chars);
            content = new String(chars);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return content;
    }
}
