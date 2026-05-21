package cn.tedu.xjqxz;

public class MovementController {
    private int roleX;
    private int roleY;
    private int roleWidth;
    private int roleHeight;
    private int speed;
    private WalkableMap map;

    public MovementController(int startX, int startY, int roleWidth, int roleHeight, int speed) {
        this.roleX = startX;
        this.roleY = startY;
        this.roleWidth = roleWidth;
        this.roleHeight = roleHeight;
        this.speed = speed;
    }

    public void setMap(WalkableMap map) {
        this.map = map;
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

    public int getRoleWidth() {
        return roleWidth;
    }

    public int getRoleHeight() {
        return roleHeight;
    }

    public void moveUp() {
        if (map == null) return;
        roleY -= speed;
        // 边界限制：不能越过上方边界
        if (roleY < 0) {
            roleY = 0;
        }
        int x = roleX + roleWidth / 2;
        int y = roleY + roleHeight;
        if (!map.isWalkable(x, y)) {
            roleY += speed;
        }
    }

    public void moveDown() {
        if (map == null) return;
        roleY += speed;
        int x = roleX + roleWidth / 2;
        int y = roleY + roleHeight;
        
        // 向下移动边界限制：不能越过地图下方边界
        if (y >= map.getHeight()) {
            roleY -= speed;
            return;
        }

        if (!map.isWalkable(x, y)) {
            roleY -= speed;
        }
    }

    public void moveLeft() {
        if (map == null) return;
        // 向左移动边界限制（原逻辑中的 > -16）
        if (roleX > -16) {
            roleX -= speed;
            int x = roleX + roleWidth / 2;
            int y = roleY + roleHeight;
            if (!map.isWalkable(x, y)) {
                roleX += speed;
            }
        }
    }

    public void moveRight() {
        if (map == null) return;
        roleX += speed;
        int x = roleX + roleWidth / 2;
        int y = roleY + roleHeight;

        // 向右移动边界限制
        if (x >= map.getWidth()) {
            roleX -= speed;
            return;
        }

        if (!map.isWalkable(x, y)) {
            roleX -= speed;
        }
    }

    public void handleKeyCode(int keyCode) {
        // 38 = UP, 40 = DOWN, 37 = LEFT, 39 = RIGHT
        if (keyCode == 38) {
            moveUp();
        } else if (keyCode == 40) {
            moveDown();
        } else if (keyCode == 37) {
            moveLeft();
        } else if (keyCode == 39) {
            moveRight();
        }
    }
}
