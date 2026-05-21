package cn.tedu.xjqxz;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MovementControllerTest {

    @Mock
    private WalkableMap walkableMap;

    private MovementController movementController;

    private static final int START_X = 100;
    private static final int START_Y = 200;
    private static final int ROLE_WIDTH = 60;
    private static final int ROLE_HEIGHT = 108;
    private static final int SPEED = 4;
    private static final int MIN_X = -16;
    private static final int MAX_X = 2000;
    private static final int MIN_Y = 0;
    private static final int MAX_Y = 1000;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(walkableMap.isWalkable(anyInt(), anyInt(), anyInt())).thenReturn(true);
        movementController = new MovementController(
            START_X, START_Y, ROLE_WIDTH, ROLE_HEIGHT, 
            SPEED, MIN_X, MAX_X, MIN_Y, MAX_Y, walkableMap
        );
    }

    @Test
    public void testInitialization() {
        assertEquals(START_X, movementController.getRoleX());
        assertEquals(START_Y, movementController.getRoleY());
        assertEquals(MovementController.DIRECTION_DOWN, movementController.getDirection());
        assertEquals(1, movementController.getMapID());
    }

    @Test
    public void testMoveUp_Success() {
        int initialY = movementController.getRoleY();
        boolean result = movementController.moveUp();

        assertTrue(result);
        assertEquals(MovementController.DIRECTION_UP, movementController.getDirection());
        assertEquals(initialY - SPEED, movementController.getRoleY());
    }

    @Test
    public void testMoveUp_Obstacle() {
        when(walkableMap.isWalkable(anyInt(), anyInt(), anyInt())).thenReturn(false);
        int initialY = movementController.getRoleY();
        
        boolean result = movementController.moveUp();

        assertFalse(result);
        assertEquals(initialY, movementController.getRoleY());
        assertEquals(MovementController.DIRECTION_UP, movementController.getDirection());
    }

    @Test
    public void testMoveUp_Boundary() {
        movementController.setRoleY(MIN_Y);
        
        boolean result = movementController.moveUp();

        assertFalse(result);
        assertEquals(MIN_Y, movementController.getRoleY());
    }

    @Test
    public void testMoveDown_Success() {
        int initialY = movementController.getRoleY();
        boolean result = movementController.moveDown();

        assertTrue(result);
        assertEquals(MovementController.DIRECTION_DOWN, movementController.getDirection());
        assertEquals(initialY + SPEED, movementController.getRoleY());
    }

    @Test
    public void testMoveDown_Obstacle() {
        when(walkableMap.isWalkable(anyInt(), anyInt(), anyInt())).thenReturn(false);
        int initialY = movementController.getRoleY();
        
        boolean result = movementController.moveDown();

        assertFalse(result);
        assertEquals(initialY, movementController.getRoleY());
    }

    @Test
    public void testMoveDown_Boundary() {
        movementController.setRoleY(MAX_Y);
        
        boolean result = movementController.moveDown();

        assertFalse(result);
        assertEquals(MAX_Y, movementController.getRoleY());
    }

    @Test
    public void testMoveLeft_Success() {
        int initialX = movementController.getRoleX();
        boolean result = movementController.moveLeft();

        assertTrue(result);
        assertEquals(MovementController.DIRECTION_LEFT, movementController.getDirection());
        assertEquals(initialX - SPEED, movementController.getRoleX());
    }

    @Test
    public void testMoveLeft_Obstacle() {
        when(walkableMap.isWalkable(anyInt(), anyInt(), anyInt())).thenReturn(false);
        int initialX = movementController.getRoleX();
        
        boolean result = movementController.moveLeft();

        assertFalse(result);
        assertEquals(initialX, movementController.getRoleX());
    }

    @Test
    public void testMoveLeft_Boundary() {
        movementController.setRoleX(MIN_X);
        
        boolean result = movementController.moveLeft();

        assertFalse(result);
        assertEquals(MIN_X, movementController.getRoleX());
    }

    @Test
    public void testMoveRight_Success() {
        int initialX = movementController.getRoleX();
        boolean result = movementController.moveRight();

        assertTrue(result);
        assertEquals(MovementController.DIRECTION_RIGHT, movementController.getDirection());
        assertEquals(initialX + SPEED, movementController.getRoleX());
    }

    @Test
    public void testMoveRight_Obstacle() {
        when(walkableMap.isWalkable(anyInt(), anyInt(), anyInt())).thenReturn(false);
        int initialX = movementController.getRoleX();
        
        boolean result = movementController.moveRight();

        assertFalse(result);
        assertEquals(initialX, movementController.getRoleX());
    }

    @Test
    public void testSetMapID() {
        movementController.setMapID(2);
        assertEquals(2, movementController.getMapID());
    }

    @Test
    public void testWalkableCheckCalled() {
        movementController.moveUp();
        
        verify(walkableMap, atLeastOnce()).isWalkable(anyInt(), anyInt(), anyInt());
    }

    @Test
    public void testWalkableCheckUsesMapID() {
        movementController.setMapID(2);
        when(walkableMap.isWalkable(anyInt(), anyInt(), eq(2))).thenReturn(false);
        
        int initialY = movementController.getRoleY();
        boolean result = movementController.moveUp();
        
        assertFalse(result);
        assertEquals(initialY, movementController.getRoleY());
        verify(walkableMap, atLeastOnce()).isWalkable(anyInt(), anyInt(), eq(2));
    }
}
