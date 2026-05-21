package cn.tedu.xjqxz;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MovementControllerTest {
    private final MovementController movementController = new MovementController();

    @Test
    public void move_shouldRollbackWhenMovingUpIntoObstacle() {
        MovementController.WalkableMap walkableMap = mock(MovementController.WalkableMap.class);
        MovementController.KeyboardInput keyboardInput = mock(MovementController.KeyboardInput.class);
        MovementController.MovementState state = new MovementController.MovementState(50, 20, 0, 60, 108);

        when(walkableMap.getWidth()).thenReturn(200);
        when(walkableMap.getHeight()).thenReturn(200);
        when(walkableMap.isWalkable(anyInt(), anyInt())).thenReturn(true);
        when(walkableMap.isWalkable(80, 124)).thenReturn(false);
        when(keyboardInput.isUpPressed()).thenReturn(true);

        movementController.move(state, keyboardInput, walkableMap);

        assertEquals(50, state.getRoleX());
        assertEquals(20, state.getRoleY());
        assertEquals(3, state.getRoleDir());
        verify(walkableMap).isWalkable(80, 124);
    }

    @Test
    public void move_shouldRollbackWhenMovingDownBeyondBottomBoundary() {
        MovementController.WalkableMap walkableMap = mock(MovementController.WalkableMap.class);
        MovementController.KeyboardInput keyboardInput = mock(MovementController.KeyboardInput.class);
        MovementController.MovementState state = new MovementController.MovementState(50, 16, 3, 60, 108);

        when(walkableMap.getWidth()).thenReturn(200);
        when(walkableMap.getHeight()).thenReturn(120);
        when(keyboardInput.isUpPressed()).thenReturn(false);
        when(keyboardInput.isDownPressed()).thenReturn(true);

        movementController.move(state, keyboardInput, walkableMap);

        assertEquals(50, state.getRoleX());
        assertEquals(16, state.getRoleY());
        assertEquals(0, state.getRoleDir());
        verify(walkableMap, never()).isWalkable(anyInt(), anyInt());
    }

    @Test
    public void move_shouldStopAtLeftBoundary() {
        MovementController.WalkableMap walkableMap = mock(MovementController.WalkableMap.class);
        MovementController.KeyboardInput keyboardInput = mock(MovementController.KeyboardInput.class);
        MovementController.MovementState state = new MovementController.MovementState(-16, 20, 0, 60, 108);

        when(walkableMap.getWidth()).thenReturn(200);
        when(walkableMap.getHeight()).thenReturn(200);
        when(walkableMap.isWalkable(anyInt(), anyInt())).thenReturn(true);
        when(keyboardInput.isUpPressed()).thenReturn(false);
        when(keyboardInput.isDownPressed()).thenReturn(false);
        when(keyboardInput.isLeftPressed()).thenReturn(true);

        movementController.move(state, keyboardInput, walkableMap);

        assertEquals(-16, state.getRoleX());
        assertEquals(20, state.getRoleY());
        assertEquals(1, state.getRoleDir());
        verify(walkableMap).isWalkable(14, 128);
    }
}
