package cn.tedu.xjqxz;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.ref.SoftReference;

public class CachedImage {
    private SoftReference<Image> ref;
    private final String filePath;

    public CachedImage(String filePath) {
        this.filePath = filePath;
    }

    public Image get() {
        Image img = null;
        if (ref != null) {
            img = ref.get();
        }
        if (img == null) {
            try {
                img = ImageIO.read(new File(filePath));
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            ref = new SoftReference<>(img);
        }
        return img;
    }

    public void flush() {
        Image img = null;
        if (ref != null) {
            img = ref.get();
            ref.clear();
        }
        ref = null;
        if (img != null) {
            img.flush();
        }
    }
}