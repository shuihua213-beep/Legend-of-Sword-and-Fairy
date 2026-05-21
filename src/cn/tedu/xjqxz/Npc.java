package cn.tedu.xjqxz;

import java.awt.Image;

/**
 * 配角人物Npc 类
 *
 * @author fgksgf
 */
public class Npc {
    private String name;
    private boolean chatOver = false;
    private int x;
    private int y;
    private int index = 0;
    int chatIndex = 0;
    private String[] imagePaths;
    private String[] words;

    public Npc(String[] words, String[] imagePaths, int x, int y, String name) {
        this.words = words;
        this.imagePaths = imagePaths;
        this.x = x;
        this.y = y;
        this.name = name;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getName() {
        return name;
    }

    public int getWidth() {
        return ImageCache.getImage(imagePaths[0]).getWidth(null);
    }

    public int getHeight() {
        return ImageCache.getImage(imagePaths[0]).getHeight(null);
    }

    public Image getImage() {
        return ImageCache.getImage(imagePaths[index]);
    }

    public void updateIndex() {
        index++;
        if (index > imagePaths.length - 1) {
            index = 0;
        }
    }

    public void updateChatContent() {
        chatIndex++;
        if (chatIndex > words.length - 1) {
            chatOver = true;
            chatIndex = 0;
        }
    }

    public String getWords() {
        return words[chatIndex];
    }

    public boolean isChatOver() {
        return chatOver;
    }

    public void setChatOver(boolean b) {
        chatOver = b;
    }

    public void preloadImages() {
        ImageCache.preloadImages(imagePaths);
    }

    public void flushImages() {
        ImageCache.flushImages(imagePaths);
    }
}
