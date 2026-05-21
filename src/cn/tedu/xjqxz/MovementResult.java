package cn.tedu.xjqxz;

public class MovementResult {
    public static final int DIR_DOWN = 0;
    public static final int DIR_LEFT = 1;
    public static final int DIR_RIGHT = 2;
    public static final int DIR_UP = 3;

    private final int x;
    private final int y;
    private final int direction;
    private final boolean blocked;

    public MovementResult(int x, int y, int direction, boolean blocked) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.blocked = blocked;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getDirection() {
        return direction;
    }

    public boolean isBlocked() {
        return blocked;
    }

    @Override
    public String toString() {
        return "MovementResult{x=" + x + ", y=" + y + ", direction=" + direction + ", blocked=" + blocked + '}';
    }
}