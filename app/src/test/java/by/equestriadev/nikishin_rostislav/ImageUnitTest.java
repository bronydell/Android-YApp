package by.equestriadev.nikishin_rostislav;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

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
        DerpiBooImageLoader loader = new DerpiBooImageLoader();
        assertEquals(loader.getImageUrl() != null, true);
    }

    @Test
    public void MLWTest() {
        ImageLoader loader = new MLWallpaperImageLoader();
        System.out.println(loader.getImageUrl());
        assertEquals(loader.getImageUrl() != null, true);
    }

    @Test
    public void Rule34Test() {
        ImageLoader loader = new Rule34ImageLoader();
        assertEquals(loader.getImageUrl() != null, true);
    }

    @Test
    public void YandexFotkiTest() {
        ImageLoader loader = new FotkiImageLoader();
        assertEquals(loader.getImageUrl() != null, true);
    }
}
