package cn.tedu.xjqxz;

public class MovementController {

    public static final int DIR_DOWN = 0;
    public static final int DIR_LEFT = 1;
    public static final int DIR_RIGHT = 2;
    public static final int DIR_UP = 3;

    public static final int DEFAULT_SPEED = 4;

    private int roleX;
    private int roleY;
    private final int roleW;
    private final int roleH;
    private int roleDir;
    private int speed;
    private int mapID;
    private WalkableMap walkableMap;

    public MovementController(int x, int y, int w, int h) {
        this.roleX = x;
        this.roleY = y;
        this.roleW = w;
        this.roleH = h;
        this.roleDir = DIR_DOWN;
        this.speed = DEFAULT_SPEED;
        this.mapID = 1;
    }

    public void setWalkableMap(WalkableMap walkableMap) {
        this.walkableMap = walkableMap;
    }

    public WalkableMap getWalkableMap() {
        return walkableMap;
    }

    public void setMapID(int mapID) {
        this.mapID = mapID;
    }

    public int getMapID() {
        return mapID;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getSpeed() {
        return speed;
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

    public int getRoleW() {
        return roleW;
    }

    public int getRoleH() {
        return roleH;
    }

    public int getRoleDir() {
        return roleDir;
    }

    public void setRoleDir(int roleDir) {
        this.roleDir = roleDir;
    }

    private int calcFootX() {
        return roleX + roleW / 2;
    }

    private int calcFootY() {
        return roleY + roleH;
    }

    private boolean isFootWalkable() {
        if (walkableMap == null) {
            return true;
        }
        return walkableMap.isWalkable(calcFootX(), calcFootY(), mapID);
    }

    public MovementResult moveUp() {
        roleDir = DIR_UP;
        roleY -= speed;
        boolean blocked = false;
        if (!isFootWalkable()) {
            roleY += speed;
            blocked = true;
        }
        return new MovementResult(roleX, roleY, roleDir, blocked);
    }

    public MovementResult moveDown() {
        roleDir = DIR_DOWN;
        roleY += speed;
        boolean blocked = false;
        if (!isFootWalkable()) {
            roleY -= speed;
            blocked = true;
        }
        return new MovementResult(roleX, roleY, roleDir, blocked);
    }

    public MovementResult moveLeft() {
        roleDir = DIR_LEFT;
        if (roleX > -16) {
            roleX -= speed;
            if (!isFootWalkable()) {
                roleX += speed;
                return new MovementResult(roleX, roleY, roleDir, true);
            }
        } else {
            return new MovementResult(roleX, roleY, roleDir, true);
        }
        return new MovementResult(roleX, roleY, roleDir, false);
    }

    public MovementResult moveRight() {
        roleDir = DIR_RIGHT;
        roleX += speed;
        boolean blocked = false;
        if (!isFootWalkable()) {
            roleX -= speed;
            blocked = true;
        }
        return new MovementResult(roleX, roleY, roleDir, blocked);
    }

    public MovementResult move(int direction) {
        switch (direction) {
            case DIR_UP:
                return moveUp();
            case DIR_DOWN:
                return moveDown();
            case DIR_LEFT:
                return moveLeft();
            case DIR_RIGHT:
                return moveRight();
            default:
                return new MovementResult(roleX, roleY, roleDir, false);
        }
    }
}