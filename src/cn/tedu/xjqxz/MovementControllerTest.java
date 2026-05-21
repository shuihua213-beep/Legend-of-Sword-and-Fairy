package cn.tedu.xjqxz;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MovementControllerTest {

    private MovementController controller;

    @Mock
    private int[][] walkableMap;

    private static final int WALKABLE_COLOR = 0;
    private static final int OBSTACLE_COLOR = 1;
    private static final int MAP_WIDTH = 2000;
    private static final int MAP_HEIGHT = 2000;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new MovementController(100, 100, 60, 108, 4);
    }

    @Test
    public void testMoveUpBlockedByObstacle() {
        when(walkableMap[204][130]).thenReturn(OBSTACLE_COLOR);
        controller.setWalkableMap(walkableMap, WALKABLE_COLOR, MAP_WIDTH, MAP_HEIGHT);

        int initialY = controller.getY();
        boolean result = controller.moveUp();

        assertFalse(result);
        assertEquals(initialY, controller.getY());
        assertEquals(MovementController.DIR_UP, controller.getDirection());
    }

    @Test
    public void testMoveUpSuccess() {
        when(walkableMap[204][130]).thenReturn(WALKABLE_COLOR);
        controller.setWalkableMap(walkableMap, WALKABLE_COLOR, MAP_WIDTH, MAP_HEIGHT);

        int initialY = controller.getY();
        boolean result = controller.moveUp();

        assertTrue(result);
        assertEquals(initialY - 4, controller.getY());
    }

    @Test
    public void testMoveDownBlockedByBoundary() {
        controller = new MovementController(100, 1896, 60, 108, 4);
        when(walkableMap[2004][130]).thenReturn(WALKABLE_COLOR);
        controller.setWalkableMap(walkableMap, WALKABLE_COLOR, MAP_WIDTH, MAP_HEIGHT);

        int initialY = controller.getY();
        boolean result = controller.moveDown();

        assertFalse(result);
        assertEquals(initialY, controller.getY());
    }

    @Test
    public void testMoveDownBlockedByObstacle() {
        when(walkableMap[212][130]).thenReturn(OBSTACLE_COLOR);
        controller.setWalkableMap(walkableMap, WALKABLE_COLOR, MAP_WIDTH, MAP_HEIGHT);

        int initialY = controller.getY();
        boolean result = controller.moveDown();

        assertFalse(result);
        assertEquals(initialY, controller.getY());
        assertEquals(MovementController.DIR_DOWN, controller.getDirection());
    }

    @Test
    public void testMoveDownSuccess() {
        when(walkableMap[212][130]).thenReturn(WALKABLE_COLOR);
        controller.setWalkableMap(walkableMap, WALKABLE_COLOR, MAP_WIDTH, MAP_HEIGHT);

        int initialY = controller.getY();
        boolean result = controller.moveDown();

        assertTrue(result);
        assertEquals(initialY + 4, controller.getY());
    }

    @Test
    public void testMoveLeftBlockedByBoundary() {
        controller = new MovementController(-20, 100, 60, 108, 4);
        when(walkableMap[208][-10]).thenReturn(WALKABLE_COLOR);
        controller.setWalkableMap(walkableMap, WALKABLE_COLOR, MAP_WIDTH, MAP_HEIGHT);

        int initialX = controller.getX();
        boolean result = controller.moveLeft();

        assertFalse(result);
        assertEquals(initialX, controller.getX());
        assertEquals(MovementController.DIR_LEFT, controller.getDirection());
    }

    @Test
    public void testMoveLeftBlockedByObstacle() {
        when(walkableMap[208][126]).thenReturn(OBSTACLE_COLOR);
        controller.setWalkableMap(walkableMap, WALKABLE_COLOR, MAP_WIDTH, MAP_HEIGHT);

        int initialX = controller.getX();
        boolean result = controller.moveLeft();

        assertFalse(result);
        assertEquals(initialX, controller.getX());
    }

    @Test
    public void testMoveLeftSuccess() {
        when(walkableMap[208][126]).thenReturn(WALKABLE_COLOR);
        controller.setWalkableMap(walkableMap, WALKABLE_COLOR, MAP_WIDTH, MAP_HEIGHT);

        int initialX = controller.getX();
        boolean result = controller.moveLeft();

        assertTrue(result);
        assertEquals(initialX - 4, controller.getX());
    }

    @Test
    public void testMoveRightBlockedByObstacle() {
        when(walkableMap[208][134]).thenReturn(OBSTACLE_COLOR);
        controller.setWalkableMap(walkableMap, WALKABLE_COLOR, MAP_WIDTH, MAP_HEIGHT);

        int initialX = controller.getX();
        boolean result = controller.moveRight();

        assertFalse(result);
        assertEquals(initialX, controller.getX());
        assertEquals(MovementController.DIR_RIGHT, controller.getDirection());
    }

    @Test
    public void testMoveRightSuccess() {
        when(walkableMap[208][134]).thenReturn(WALKABLE_COLOR);
        controller.setWalkableMap(walkableMap, WALKABLE_COLOR, MAP_WIDTH, MAP_HEIGHT);

        int initialX = controller.getX();
        boolean result = controller.moveRight();

        assertTrue(result);
        assertEquals(initialX + 4, controller.getX());
    }

    @Test
    public void testDirectionUpdateOnMove() {
        when(walkableMap[204][130]).thenReturn(WALKABLE_COLOR);
        controller.setWalkableMap(walkableMap, WALKABLE_COLOR, MAP_WIDTH, MAP_HEIGHT);

        controller.moveUp();
        assertEquals(MovementController.DIR_UP, controller.getDirection());

        controller.moveDown();
        assertEquals(MovementController.DIR_DOWN, controller.getDirection());

        controller.moveLeft();
        assertEquals(MovementController.DIR_LEFT, controller.getDirection());

        controller.moveRight();
        assertEquals(MovementController.DIR_RIGHT, controller.getDirection());
    }

    @Test
    public void testCollisionWithNullMap() {
        controller.setWalkableMap(null, WALKABLE_COLOR, MAP_WIDTH, MAP_HEIGHT);

        boolean result = controller.moveUp();
        assertTrue(result);
    }

    @Test
    public void testGettersAndSetters() {
        controller.setX(200);
        controller.setY(300);
        controller.setSpeed(8);
        controller.setDirection(MovementController.DIR_LEFT);

        assertEquals(200, controller.getX());
        assertEquals(300, controller.getY());
        assertEquals(8, controller.getSpeed());
        assertEquals(MovementController.DIR_LEFT, controller.getDirection());
    }
}