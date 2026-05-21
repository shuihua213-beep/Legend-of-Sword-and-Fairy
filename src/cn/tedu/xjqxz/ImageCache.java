package cn.tedu.xjqxz;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

final class ImageCache {
    private static final Map<String, SoftReference<Image>> imageCache = new HashMap<String, SoftReference<Image>>();
    private static final Map<String, SoftReference<BufferedImage>> bufferedImageCache = new HashMap<String, SoftReference<BufferedImage>>();

    private ImageCache() {
    }

    static synchronized Image getImage(String path) {
        SoftReference<Image> reference = imageCache.get(path);
        Image image = reference == null ? null : reference.get();
        if (image == null) {
            try {
                image = ImageIO.read(new File(path));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            imageCache.put(path, new SoftReference<Image>(image));
        }
        return image;
    }

    static synchronized BufferedImage getBufferedImage(String path) {
        SoftReference<BufferedImage> reference = bufferedImageCache.get(path);
        BufferedImage image = reference == null ? null : reference.get();
        if (image == null) {
            try {
                image = ImageIO.read(new File(path));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            bufferedImageCache.put(path, new SoftReference<BufferedImage>(image));
        }
        return image;
    }

    static synchronized void preloadImages(String[] paths) {
        for (int i = 0; i < paths.length; i++) {
            getImage(paths[i]);
        }
    }

    static synchronized void preloadBufferedImages(String[] paths) {
        for (int i = 0; i < paths.length; i++) {
            getBufferedImage(paths[i]);
        }
    }

    static synchronized void flushImage(String path) {
        SoftReference<Image> reference = imageCache.remove(path);
        if (reference == null) {
            return;
        }
        Image image = reference.get();
        if (image != null) {
            image.flush();
        }
        reference.clear();
    }

    static synchronized void flushBufferedImage(String path) {
        SoftReference<BufferedImage> reference = bufferedImageCache.remove(path);
        if (reference == null) {
            return;
        }
        BufferedImage image = reference.get();
        if (image != null) {
            image.flush();
        }
        reference.clear();
    }

    static synchronized void flushImages(String[] paths) {
        for (int i = 0; i < paths.length; i++) {
            flushImage(paths[i]);
        }
    }

    static synchronized void flushBufferedImages(String[] paths) {
        for (int i = 0; i < paths.length; i++) {
            flushBufferedImage(paths[i]);
        }
    }
}
