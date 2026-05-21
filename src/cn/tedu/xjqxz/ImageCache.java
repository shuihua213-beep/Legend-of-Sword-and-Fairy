package cn.tedu.xjqxz;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

public class ImageCache {
    private static Map<String, SoftReference<Image>> cache = new HashMap<>();

    public static Image getImage(String path) {
        if (path == null) return null;
        
        SoftReference<Image> ref = cache.get(path);
        Image img = ref != null ? ref.get() : null;
        
        if (img == null) {
            try {
                img = ImageIO.read(new File(path));
                if (img != null) {
                    cache.put(path, new SoftReference<>(img));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return img;
    }

    public static void flushImage(String path) {
        if (path == null) return;
        SoftReference<Image> ref = cache.remove(path);
        if (ref != null) {
            Image img = ref.get();
            if (img != null) {
                img.flush();
            }
        }
    }
}
