package cn.tedu.xjqxz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GameFrame {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            JFrame jFrame = new JFrame();
            jFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            jFrame.setIgnoreRepaint(true);
            jFrame.setResizable(false);
            jFrame.setTitle("仙剑奇侠传 - Version 1.0");

            GamePanel gamePanel = new GamePanel();
            jFrame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    super.windowClosing(e);
                    int ch = JOptionPane.showConfirmDialog(null, "确认退出游戏吗？", "提示", JOptionPane.YES_NO_OPTION);
                    if (ch == JOptionPane.YES_OPTION) {
                        System.exit(0);
                    }
                    EventQueue.invokeLater(gamePanel::requestFocusInWindow);
                }
            });

            jFrame.addKeyListener(gamePanel);
            jFrame.add(gamePanel);
            jFrame.pack();
            jFrame.setLocationRelativeTo(null);
            jFrame.setVisible(true);
            gamePanel.requestFocusInWindow();
        });
    }
}
