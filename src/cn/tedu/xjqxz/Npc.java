package cn.tedu.xjqxz;

import java.awt.*;

public class Npc {
    private String name;
    private boolean chatOver = false;

    private int x;
    private int y;

    private int index = 0;
    int chatIndex = 0;
    private CachedImage[] image;
    private String[] words;

    public Npc(String[] words, CachedImage[] image, int x, int y, String name) {
        this.words = words;
        this.image = image;
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
        return image[0].get().getWidth(null);
    }

    public int getHeight() {
        return image[0].get().getHeight(null);
    }

    public Image getImage() {
        return image[index].get();
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
}