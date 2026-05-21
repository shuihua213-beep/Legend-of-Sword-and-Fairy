package cn.tedu.xjqxz;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MovementControllerTest {

    private MovementController controller;

    private static final int INIT_X = 100;
    private static final int INIT_Y = 200;
    private static final int ROLE_W = 60;
    private static final int ROLE_H = 108;
    private static final int SPEED = 4;

    @Before
    public void setUp() {
        controller = new MovementController(INIT_X, INIT_Y, ROLE_W, ROLE_H);
        controller.setSpeed(SPEED);
    }

    private int footX(int x) {
        return x + ROLE_W / 2;
    }

    private int footY(int y) {
        return y + ROLE_H;
    }

    @Test
    public void testMoveUpNormal() {
        WalkableMap allWalkable = new WalkableMap() {
            @Override
            public boolean isWalkable(int x, int y, int mapID) {
                return true;
            }
        };
        controller.setWalkableMap(allWalkable);

        MovementResult result = controller.moveUp();

        assertEquals(MovementResult.DIR_UP, result.getDirection());
        assertEquals(INIT_X, result.getX());
        assertEquals(INIT_Y - SPEED, result.getY());
        assertFalse(result.isBlocked());
    }

    @Test
    public void testMoveDownNormal() {
        WalkableMap allWalkable = new WalkableMap() {
            @Override
            public boolean isWalkable(int x, int y, int mapID) {
                return true;
            }
        };
        controller.setWalkableMap(allWalkable);

        MovementResult result = controller.moveDown();

        assertEquals(MovementResult.DIR_DOWN, result.getDirection());
        assertEquals(INIT_X, result.getX());
        assertEquals(INIT_Y + SPEED, result.getY());
        assertFalse(result.isBlocked());
    }

    @Test
    public void testMoveLeftNormal() {
        WalkableMap allWalkable = new WalkableMap() {
            @Override
            public boolean isWalkable(int x, int y, int mapID) {
                return true;
            }
        };
        controller.setWalkableMap(allWalkable);

        MovementResult result = controller.moveLeft();

        assertEquals(MovementResult.DIR_LEFT, result.getDirection());
        assertEquals(INIT_X - SPEED, result.getX());
        assertEquals(INIT_Y, result.getY());
        assertFalse(result.isBlocked());
    }

    @Test
    public void testMoveRightNormal() {
        WalkableMap allWalkable = new WalkableMap() {
            @Override
            public boolean isWalkable(int x, int y, int mapID) {
                return true;
            }
        };
        controller.setWalkableMap(allWalkable);

        MovementResult result = controller.moveRight();

        assertEquals(MovementResult.DIR_RIGHT, result.getDirection());
        assertEquals(INIT_X + SPEED, result.getX());
        assertEquals(INIT_Y, result.getY());
        assertFalse(result.isBlocked());
    }

    @Test
    public void testMoveUpIntoObstacle() {
        final int targetY = INIT_Y - SPEED;
        final int targetFootX = footX(INIT_X);
        final int targetFootY = footY(targetY);

        WalkableMap blockedMap = new WalkableMap() {
            @Override
            public boolean isWalkable(int x, int y, int mapID) {
                if (x == targetFootX && y == targetFootY) {
                    return false;
                }
                return true;
            }
        };
        controller.setWalkableMap(blockedMap);

        MovementResult result = controller.moveUp();

        assertTrue("Movement up should be blocked by obstacle", result.isBlocked());
        assertEquals("X should be unchanged when blocked", INIT_X, result.getX());
        assertEquals("Y should revert to original when blocked", INIT_Y, result.getY());
        assertEquals("Direction should still be UP", MovementResult.DIR_UP, result.getDirection());
        assertEquals("Controller X should be unchanged", INIT_X, controller.getRoleX());
        assertEquals("Controller Y should be unchanged", INIT_Y, controller.getRoleY());
    }

    @Test
    public void testMoveDownIntoObstacle() {
        final int targetY = INIT_Y + SPEED;
        final int targetFootX = footX(INIT_X);
        final int targetFootY = footY(targetY);

        WalkableMap blockedMap = new WalkableMap() {
            @Override
            public boolean isWalkable(int x, int y, int mapID) {
                if (x == targetFootX && y == targetFootY) {
                    return false;
                }
                return true;
            }
        };
        controller.setWalkableMap(blockedMap);

        MovementResult result = controller.moveDown();

        assertTrue("Movement down should be blocked by obstacle", result.isBlocked());
        assertEquals("X should be unchanged when blocked", INIT_X, result.getX());
        assertEquals("Y should revert to original when blocked", INIT_Y, result.getY());
        assertEquals("Direction should be DOWN", MovementResult.DIR_DOWN, result.getDirection());
        assertEquals("Controller X should be unchanged", INIT_X, controller.getRoleX());
        assertEquals("Controller Y should be unchanged", INIT_Y, controller.getRoleY());
    }

    @Test
    public void testMoveLeftIntoObstacle() {
        final int targetX = INIT_X - SPEED;
        final int targetFootX = footX(targetX);
        final int targetFootY = footY(INIT_Y);

        WalkableMap blockedMap = new WalkableMap() {
            @Override
            public boolean isWalkable(int x, int y, int mapID) {
                if (x == targetFootX && y == targetFootY) {
                    return false;
                }
                return true;
            }
        };
        controller.setWalkableMap(blockedMap);

        MovementResult result = controller.moveLeft();

        assertTrue("Movement left should be blocked by obstacle", result.isBlocked());
        assertEquals("X should revert to original when blocked", INIT_X, result.getX());
        assertEquals("Y should be unchanged", INIT_Y, result.getY());
        assertEquals(MovementResult.DIR_LEFT, result.getDirection());
    }

    @Test
    public void testMoveRightIntoObstacle() {
        final int targetX = INIT_X + SPEED;
        final int targetFootX = footX(targetX);
        final int targetFootY = footY(INIT_Y);

        WalkableMap blockedMap = new WalkableMap() {
            @Override
            public boolean isWalkable(int x, int y, int mapID) {
                if (x == targetFootX && y == targetFootY) {
                    return false;
                }
                return true;
            }
        };
        controller.setWalkableMap(blockedMap);

        MovementResult result = controller.moveRight();

        assertTrue("Movement right should be blocked by obstacle", result.isBlocked());
        assertEquals("X should revert to original when blocked", INIT_X, result.getX());
        assertEquals("Y should be unchanged", INIT_Y, result.getY());
        assertEquals(MovementResult.DIR_RIGHT, result.getDirection());
    }

    @Test
    public void testMoveLeftReachesBoundary() {
        controller.setRoleX(-16);

        WalkableMap allWalkable = new WalkableMap() {
            @Override
            public boolean isWalkable(int x, int y, int mapID) {
                return true;
            }
        };
        controller.setWalkableMap(allWalkable);

        MovementResult result = controller.moveLeft();

        assertTrue("Movement should be blocked at left boundary (x == -16)", result.isBlocked());
        assertEquals("X should stay at boundary", -16, result.getX());
        assertEquals("Y should be unchanged", INIT_Y, result.getY());
        assertEquals(MovementResult.DIR_LEFT, result.getDirection());
    }

    @Test
    public void testMoveLeftOnePixelBeforeBoundary() {
        controller.setRoleX(-15);

        WalkableMap allWalkable = new WalkableMap() {
            @Override
            public boolean isWalkable(int x, int y, int mapID) {
                return true;
            }
        };
        controller.setWalkableMap(allWalkable);

        MovementResult result = controller.moveLeft();

        assertEquals("X should move when above boundary", -15 - SPEED, result.getX());
        assertFalse("Should not be blocked by boundary when x > -16", result.isBlocked());
    }

    @Test
    public void testMoveDownBoundaryLimit() {
        controller.setRoleY(2000);
        final int targetY = 2000 + SPEED;
        final int targetFootX = footX(controller.getRoleX());
        final int targetFootY = footY(targetY);

        WalkableMap boundaryMap = new WalkableMap() {
            @Override
            public boolean isWalkable(int x, int y, int mapID) {
                if (x == targetFootX && y == targetFootY) {
                    return false;
                }
                return true;
            }
        };
        controller.setWalkableMap(boundaryMap);

        MovementResult result = controller.moveDown();

        assertTrue("Movement should be blocked at down boundary", result.isBlocked());
        assertEquals("Y should revert when blocked by boundary", 2000, result.getY());
    }

    @Test
    public void testDirectionIsUpdatedAfterEachMove() {
        WalkableMap allWalkable = new WalkableMap() {
            @Override
            public boolean isWalkable(int x, int y, int mapID) {
                return true;
            }
        };
        controller.setWalkableMap(allWalkable);

        controller.moveUp();
        assertEquals(MovementResult.DIR_UP, controller.getRoleDir());

        controller.moveDown();
        assertEquals(MovementResult.DIR_DOWN, controller.getRoleDir());

        controller.moveLeft();
        assertEquals(MovementResult.DIR_LEFT, controller.getRoleDir());

        controller.moveRight();
        assertEquals(MovementResult.DIR_RIGHT, controller.getRoleDir());
    }

    @Test
    public void testMovementDoesNotAffectOtherAxis() {
        WalkableMap allWalkable = new WalkableMap() {
            @Override
            public boolean isWalkable(int x, int y, int mapID) {
                return true;
            }
        };
        controller.setWalkableMap(allWalkable);

        int origX = controller.getRoleX();
        controller.moveUp();
        assertEquals("X should not change when moving up", origX, controller.getRoleX());

        controller.moveDown();
        assertEquals("X should not change when moving down", origX, controller.getRoleX());

        int origY = controller.getRoleY();
        controller.moveLeft();
        assertEquals("Y should not change when moving left", origY, controller.getRoleY());

        controller.moveRight();
        assertEquals("Y should not change when moving right", origY, controller.getRoleY());
    }

    @Test
    public void testMoveGenericMethod() {
        WalkableMap allWalkable = new WalkableMap() {
            @Override
            public boolean isWalkable(int x, int y, int mapID) {
                return true;
            }
        };
        controller.setWalkableMap(allWalkable);

        MovementResult result = controller.move(MovementResult.DIR_UP);
        assertEquals(MovementResult.DIR_UP, result.getDirection());
        assertEquals(INIT_Y - SPEED, result.getY());

        result = controller.move(MovementResult.DIR_DOWN);
        assertEquals(MovementResult.DIR_DOWN, result.getDirection());

        result = controller.move(MovementResult.DIR_LEFT);
        assertEquals(MovementResult.DIR_LEFT, result.getDirection());

        result = controller.move(MovementResult.DIR_RIGHT);
        assertEquals(MovementResult.DIR_RIGHT, result.getDirection());
    }

    @Test
    public void testMapIDSwitchDoesNotAffectMovementCalculation() {
        WalkableMap mapAwareMock = new WalkableMap() {
            @Override
            public boolean isWalkable(int x, int y, int mapID) {
                if (mapID == 1) {
                    return true;
                } else if (mapID == 2) {
                    return false;
                }
                return true;
            }
        };
        controller.setWalkableMap(mapAwareMock);

        controller.setMapID(1);
        MovementResult result1 = controller.moveUp();
        assertFalse("Map 1 should be walkable", result1.isBlocked());

        controller.setMapID(2);
        MovementResult result2 = controller.moveUp();
        assertTrue("Map 2 should not be walkable", result2.isBlocked());
    }

    @Test
    public void testMultipleConsecutiveMovements() {
        WalkableMap allWalkable = new WalkableMap() {
            @Override
            public boolean isWalkable(int x, int y, int mapID) {
                return true;
            }
        };
        controller.setWalkableMap(allWalkable);

        controller.moveUp();
        controller.moveUp();
        controller.moveUp();

        assertEquals(INIT_Y - SPEED * 3, controller.getRoleY());
        assertEquals(INIT_X, controller.getRoleX());
    }

    @Test
    public void testResumeMovementAfterObstacle() {
        final int blockedY = INIT_Y - SPEED;
        final int blockedFootX = footX(INIT_X);
        final int blockedFootY = footY(blockedY);

        WalkableMap singleBlockMap = new WalkableMap() {
            @Override
            public boolean isWalkable(int x, int y, int mapID) {
                if (x == blockedFootX && y == blockedFootY) {
                    return false;
                }
                return true;
            }
        };
        controller.setWalkableMap(singleBlockMap);

        MovementResult blocked = controller.moveUp();
        assertTrue("First movement should be blocked", blocked.isBlocked());
        assertEquals(INIT_Y, controller.getRoleY());

        MovementResult success = controller.moveUp();
        assertFalse("Second movement should succeed (moved past obstacle)", success.isBlocked());
        assertEquals(INIT_Y - SPEED, controller.getRoleY());
    }

    @Test
    public void testConstructorSetsCorrectInitialValues() {
        MovementController c = new MovementController(50, 100, 32, 48);
        assertEquals(50, c.getRoleX());
        assertEquals(100, c.getRoleY());
        assertEquals(32, c.getRoleW());
        assertEquals(48, c.getRoleH());
        assertEquals(MovementController.DEFAULT_SPEED, c.getSpeed());
        assertEquals(1, c.getMapID());
        assertNull(c.getWalkableMap());
    }

    @Test
    public void testWalkableMapCanBeReplaced() {
        WalkableMap allWalkable = new WalkableMap() {
            @Override
            public boolean isWalkable(int x, int y, int mapID) {
                return true;
            }
        };
        controller.setWalkableMap(allWalkable);
        assertFalse(controller.moveUp().isBlocked());

        WalkableMap allBlocked = new WalkableMap() {
            @Override
            public boolean isWalkable(int x, int y, int mapID) {
                return false;
            }
        };
        controller.setWalkableMap(allBlocked);
        assertTrue(controller.moveUp().isBlocked());
    }
}