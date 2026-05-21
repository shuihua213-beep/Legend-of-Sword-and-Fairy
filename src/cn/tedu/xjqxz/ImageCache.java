package cn.tedu.xjqxz;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

public class ImageCache {
    private static final Map<String, SoftReference<Image>> imageCache = new HashMap<>();
    private static final Map<String, SoftReference<BufferedImage>> bufferedImageCache = new HashMap<>();

    public static Image getImage(String path) {
        SoftReference<Image> softRef = imageCache.get(path);
        Image image = (softRef != null) ? softRef.get() : null;

        if (image == null) {
            try {
                image = ImageIO.read(new File(path));
                imageCache.put(path, new SoftReference<>(image));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return image;
    }

    public static BufferedImage getBufferedImage(String path) {
        SoftReference<BufferedImage> softRef = bufferedImageCache.get(path);
        BufferedImage image = (softRef != null) ? softRef.get() : null;

        if (image == null) {
            try {
                image = ImageIO.read(new File(path));
                bufferedImageCache.put(path, new SoftReference<>(image));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return image;
    }

    public static void flushImage(String path) {
        SoftReference<Image> softRef = imageCache.get(path);
        if (softRef != null) {
            Image image = softRef.get();
            if (image != null) {
                image.flush();
            }
            imageCache.remove(path);
        }
    }

    public static void flushBufferedImage(String path) {
        SoftReference<BufferedImage> softRef = bufferedImageCache.get(path);
        if (softRef != null) {
            BufferedImage image = softRef.get();
            if (image != null) {
                image.flush();
            }
            bufferedImageCache.remove(path);
        }
    }

    public static void flushAllImages() {
        for (Map.Entry<String, SoftReference<Image>> entry : imageCache.entrySet()) {
            Image image = entry.getValue().get();
            if (image != null) {
                image.flush();
            }
        }
        imageCache.clear();

        for (Map.Entry<String, SoftReference<BufferedImage>> entry : bufferedImageCache.entrySet()) {
            BufferedImage image = entry.getValue().get();
            if (image != null) {
                image.flush();
            }
        }
        bufferedImageCache.clear();
    }

    public static Image[] loadImageArray(String basePath, int count) {
        Image[] images = new Image[count];
        for (int i = 0; i < count; i++) {
            String path = basePath + i + ".png";
            images[i] = getImage(path);
        }
        return images;
    }

    public static void flushImageArray(String basePath, int count) {
        for (int i = 0; i < count; i++) {
            String path = basePath + i + ".png";
            flushImage(path);
        }
    }
}
