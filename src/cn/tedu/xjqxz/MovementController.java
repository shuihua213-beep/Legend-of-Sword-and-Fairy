package cn.tedu.xjqxz;

public class MovementController {
    public static final int DIRECTION_DOWN = 0;
    public static final int DIRECTION_LEFT = 1;
    public static final int DIRECTION_RIGHT = 2;
    public static final int DIRECTION_UP = 3;

    private int roleX;
    private int roleY;
    private int roleWidth;
    private int roleHeight;
    private int direction;
    private int speed;
    private int mapID;
    private int minX;
    private int maxX;
    private int minY;
    private int maxY;

    private WalkableMap walkableMap;

    public MovementController(int startX, int startY, int roleWidth, int roleHeight, int speed, 
                            int minX, int maxX, int minY, int maxY, WalkableMap walkableMap) {
        this.roleX = startX;
        this.roleY = startY;
        this.roleWidth = roleWidth;
        this.roleHeight = roleHeight;
        this.speed = speed;
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
        this.walkableMap = walkableMap;
        this.direction = DIRECTION_DOWN;
        this.mapID = 1;
    }

    public boolean moveUp() {
        direction = DIRECTION_UP;
        int previousY = roleY;
        roleY -= speed;
        if (isBlocked() || roleY < minY) {
            roleY = previousY;
            return false;
        }
        return true;
    }

    public boolean moveDown() {
        direction = DIRECTION_DOWN;
        int previousY = roleY;
        roleY += speed;
        if (isBlocked() || roleY > maxY) {
            roleY = previousY;
            return false;
        }
        return true;
    }

    public boolean moveLeft() {
        direction = DIRECTION_LEFT;
        if (roleX <= minX) {
            return false;
        }
        int previousX = roleX;
        roleX -= speed;
        if (isBlocked()) {
            roleX = previousX;
            return false;
        }
        return true;
    }

    public boolean moveRight() {
        direction = DIRECTION_RIGHT;
        int previousX = roleX;
        roleX += speed;
        if (isBlocked()) {
            roleX = previousX;
            return false;
        }
        return true;
    }

    private boolean isBlocked() {
        int checkX = roleX + roleWidth / 2;
        int checkY = roleY + roleHeight;
        return walkableMap != null && !walkableMap.isWalkable(checkX, checkY, mapID);
    }

    public int getRoleX() {
        return roleX;
    }

    public void setRoleX(int roleX) {
        this.roleX = roleX;
    }

    public int getRoleY() {
        return roleY;
    }

    public void setRoleY(int roleY) {
        this.roleY = roleY;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getMapID() {
        return mapID;
    }

    public void setMapID(int mapID) {
        this.mapID = mapID;
    }

    public int getMinX() {
        return minX;
    }

    public void setMinX(int minX) {
        this.minX = minX;
    }

    public int getMaxX() {
        return maxX;
    }

    public void setMaxX(int maxX) {
        this.maxX = maxX;
    }

    public int getMinY() {
        return minY;
    }

    public void setMinY(int minY) {
        this.minY = minY;
    }

    public int getMaxY() {
        return maxY;
    }

    public void setMaxY(int maxY) {
        this.maxY = maxY;
    }
}
