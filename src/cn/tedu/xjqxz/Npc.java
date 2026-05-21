package cn.tedu.xjqxz;

import java.awt.*;

/**
 * 配角人物Npc 类
 *
 * @author fgksgf
 */
public class Npc {
    private String name;
    private boolean chatOver = false;

    // npc在背景图片中的坐标
    private int x;
    private int y;

    private int index = 0;
    int chatIndex = 0;
    private Image[] image;
    private String[] words;
    private String imagePath;

    public Npc(String[] words, Image[] image, int x, int y, String name, String imagePath) {
        this.words = words;
        this.image = image;
        this.x = x;
        this.y = y;
        this.name = name;
        this.imagePath = imagePath;
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
        return image[0].getWidth(null);
    }

    public int getHeight() {
        return image[0].getHeight(null);
    }

    public Image getImage() {
        return image[index];
    }

    public void updateIndex() {
        index++;
        if (index > image.length - 1) {
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

    public void flushImages() {
        if (image != null) {
            for (int i = 0; i < image.length; i++) {
                if (image[i] != null) {
                    image[i].flush();
                }
            }
            if (imagePath != null) {
                ImageCache.flushImageArray(imagePath, image.length);
            }
        }
    }
}
