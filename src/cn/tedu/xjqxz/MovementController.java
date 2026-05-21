package cn.tedu.xjqxz;

public class MovementController {
    private static final int SPEED = 4;
    private static final int LEFT_BOUNDARY = -16;

    public void move(MovementState state, KeyboardInput input, WalkableMap walkableMap) {
        if (state == null || input == null || walkableMap == null) {
            return;
        }

        if (input.isUpPressed()) {
            moveVertical(state, walkableMap, -SPEED, 3);
        } else if (input.isDownPressed()) {
            moveVertical(state, walkableMap, SPEED, 0);
        } else if (input.isLeftPressed()) {
            moveHorizontal(state, walkableMap, -SPEED, 1);
        } else if (input.isRightPressed()) {
            moveHorizontal(state, walkableMap, SPEED, 2);
        }
    }

    private void moveVertical(MovementState state, WalkableMap walkableMap, int deltaY, int direction) {
        state.setRoleDir(direction);
        int originalY = state.getRoleY();
        state.setRoleY(originalY + deltaY);
        if (isBlocked(state, walkableMap)) {
            state.setRoleY(originalY);
        }
    }

    private void moveHorizontal(MovementState state, WalkableMap walkableMap, int deltaX, int direction) {
        state.setRoleDir(direction);
        int originalX = state.getRoleX();
        int targetX = originalX + deltaX;
        if (targetX < LEFT_BOUNDARY) {
            targetX = LEFT_BOUNDARY;
        }
        state.setRoleX(targetX);
        if (isBlocked(state, walkableMap)) {
            state.setRoleX(originalX);
        }
    }

    private boolean isBlocked(MovementState state, WalkableMap walkableMap) {
        int detectionX = state.getRoleX() + state.getSpriteWidth() / 2;
        int detectionY = state.getRoleY() + state.getSpriteHeight();
        if (detectionX < 0 || detectionX >= walkableMap.getWidth()) {
            return true;
        }
        if (detectionY < 0 || detectionY >= walkableMap.getHeight()) {
            return true;
        }
        return !walkableMap.isWalkable(detectionX, detectionY);
    }

    public interface KeyboardInput {
        boolean isUpPressed();

        boolean isDownPressed();

        boolean isLeftPressed();

        boolean isRightPressed();
    }

    public interface WalkableMap {
        int getWidth();

        int getHeight();

        boolean isWalkable(int x, int y);
    }

    public static class MovementState {
        private int roleX;
        private int roleY;
        private int roleDir;
        private final int spriteWidth;
        private final int spriteHeight;

        public MovementState(int roleX, int roleY, int roleDir, int spriteWidth, int spriteHeight) {
            this.roleX = roleX;
            this.roleY = roleY;
            this.roleDir = roleDir;
            this.spriteWidth = spriteWidth;
            this.spriteHeight = spriteHeight;
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

        public int getRoleDir() {
            return roleDir;
        }

        public void setRoleDir(int roleDir) {
            this.roleDir = roleDir;
        }

        public int getSpriteWidth() {
            return spriteWidth;
        }

        public int getSpriteHeight() {
            return spriteHeight;
        }
    }
}
