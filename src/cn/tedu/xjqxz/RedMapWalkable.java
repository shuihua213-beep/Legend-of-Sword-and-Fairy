package cn.tedu.xjqxz;

import java.awt.image.BufferedImage;

public class RedMapWalkable implements WalkableMap {
    private BufferedImage[] dataMap;

    public RedMapWalkable(BufferedImage[] dataMap) {
        this.dataMap = dataMap;
    }

    @Override
    public boolean isWalkable(int x, int y, int mapID) {
        int mapIndex = mapID - 1;
        if (mapIndex < 0 || mapIndex >= dataMap.length) {
            return true;
        }
        BufferedImage map = dataMap[mapIndex];
        if (x < 0 || x >= map.getWidth() || y < 0 || y >= map.getHeight()) {
            return true;
        }
        int rgb = map.getRGB(x, y);
        if (mapID == 1) {
            return rgb != -521461;
        } else if (mapID == 2) {
            return rgb != -65536;
        }
        return true;
    }
}
