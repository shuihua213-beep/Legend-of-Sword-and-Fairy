package cn.tedu.xjqxz;

public interface WalkableMap {
    /**
     * 判断指定坐标是否可通行
     * @param x 坐标 x
     * @param y 坐标 y
     * @return 是否可通行
     */
    boolean isWalkable(int x, int y);

    /**
     * 获取地图的宽度，用于边界检测
     * @return 宽度
     */
    int getWidth();

    /**
     * 获取地图的高度，用于边界检测
     * @return 高度
     */
    int getHeight();
}
