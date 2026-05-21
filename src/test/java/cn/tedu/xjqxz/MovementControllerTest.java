package cn.tedu.xjqxz;

import org.junit.Before;
import org.junit.Test;
import java.awt.event.KeyEvent;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class MovementControllerTest {

    private MovementController controller;
    private WalkableMap mockMap;

    @Before
    public void setUp() {
        mockMap = mock(WalkableMap.class);
        // 初始化控制器：startX=100, startY=100, roleWidth=60, roleHeight=108, speed=4
        controller = new MovementController(100, 100, 60, 108, 4);
        controller.setMap(mockMap);
    }

    @Test
    public void testMoveUpWithObstacle() {
        // 角色向上移动时遇到障碍物，应该回退
        // 初始位置 y=100
        // moveUp后理论上 y=96
        // 检测点 x = 100 + 30 = 130, y = 96 + 108 = 204
        when(mockMap.isWalkable(130, 204)).thenReturn(false);

        controller.moveUp();

        // 因为遇到障碍物，所以y应该恢复到100
        assertEquals(100, controller.getRoleY());
        verify(mockMap, times(1)).isWalkable(130, 204);
    }

    @Test
    public void testMoveUpWithoutObstacle() {
        // 角色向上移动没有遇到障碍物
        when(mockMap.isWalkable(130, 204)).thenReturn(true);

        controller.moveUp();

        // 成功移动，y变成96
        assertEquals(96, controller.getRoleY());
        verify(mockMap, times(1)).isWalkable(130, 204);
    }

    @Test
    public void testMoveDownBoundaryLimit() {
        // 测试向下移动边界限制
        // 假设地图高度为200
        when(mockMap.getHeight()).thenReturn(200);
        when(mockMap.isWalkable(anyInt(), anyInt())).thenReturn(true);

        // 当前y=100, moveDown后y=104, 底部y = 104 + 108 = 212
        // 212 >= 200，触发边界限制，回退
        controller.moveDown();

        assertEquals(100, controller.getRoleY());
        // 因为触发了边界限制，可能直接return，不调用isWalkable
        verify(mockMap, never()).isWalkable(anyInt(), anyInt());
    }

    @Test
    public void testMoveDownWithoutBoundaryLimit() {
        // 假设地图高度为500
        when(mockMap.getHeight()).thenReturn(500);
        when(mockMap.isWalkable(anyInt(), anyInt())).thenReturn(true);

        controller.moveDown();

        // 没有越界，成功移动，y变成104
        assertEquals(104, controller.getRoleY());
    }

    @Test
    public void testKeyboardInput() {
        // 使用 Mock 对象模拟键盘输入
        KeyEvent mockEvent = mock(KeyEvent.class);
        
        // 模拟按下 UP 键
        when(mockEvent.getKeyCode()).thenReturn(KeyEvent.VK_UP);
        when(mockMap.isWalkable(anyInt(), anyInt())).thenReturn(true);
        
        controller.handleKeyCode(mockEvent.getKeyCode());
        
        // 验证向上移动是否成功
        assertEquals(96, controller.getRoleY());
    }
}
