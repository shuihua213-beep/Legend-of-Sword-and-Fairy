package cn.tedu.xjqxz;

public class MovementController {
    private int x;
    private int y;
    private int width;
    private int height;
    private int speed;
    private int direction;
    private int[][] walkableMap;
    private int walkableColor;
    private int mapWidth;
    private int mapHeight;

    public static final int DIR_DOWN = 0;
    public static final int DIR_LEFT = 1;
    public static final int DIR_RIGHT = 2;
    public static final int DIR_UP = 3;

    public MovementController(int x, int y, int width, int height, int speed) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.direction = DIR_DOWN;
    }

    public void setWalkableMap(int[][] walkableMap, int walkableColor, int mapWidth, int mapHeight) {
        this.walkableMap = walkableMap;
        this.walkableColor = walkableColor;
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
    }

    public boolean moveUp() {
        direction = DIR_UP;
        int oldY = y;
        y -= speed;
        if (checkCollision()) {
            y = oldY;
            return false;
        }
        return true;
    }

    public boolean moveDown() {
        direction = DIR_DOWN;
        int oldY = y;
        y += speed;
        if (checkCollision()) {
            y = oldY;
            return false;
        }
        return true;
    }

    public boolean moveLeft() {
        direction = DIR_LEFT;
        int oldX = x;
        if (x > -16) {
            x -= speed;
            if (checkCollision()) {
                x = oldX;
                return false;
            }
        }
        return true;
    }

    public boolean moveRight() {
        direction = DIR_RIGHT;
        int oldX = x;
        x += speed;
        if (checkCollision()) {
            x = oldX;
            return false;
        }
        return true;
    }

    public boolean checkCollision() {
        if (walkableMap == null) {
            return false;
        }
        int checkX = x + width / 2;
        int checkY = y + height;
        if (checkX < 0 || checkX >= mapWidth || checkY < 0 || checkY >= mapHeight) {
            return true;
        }
        return walkableMap[checkY][checkX] != walkableColor;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int[][] getWalkableMap() {
        return walkableMap;
    }

    public int getWalkableColor() {
        return walkableColor;
    }
}