package cn.tedu.xjqxz;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 游戏项目的画板类，即界面文件
 *
 * @author fgksgf
 */
public class GamePanel extends JPanel implements Runnable, KeyListener {
    Thread t;
    int role_dir = 0;
    int role_i = 0;
    int role_x = 152;
    int role_y = 704;
    int roleW = 60;
    int roleH = 108;
    int hen_i = 0;
    int chick_i = 0;
    int littleChick_i = 0;
    int mall_i = 0;

    int changeSpeed = 0;
    int[] count = new int[4];

    int mapID = 1;

    int chatWith = 0;
    Font chatFont = new Font("黑体", Font.BOLD, 25);

    boolean hasChat = false;

    private static int ljcX = -400;
    private static int ljcY = -190;
    private static int mallX = -200;
    private static int mallY = -200;

    private static Image ljc;
    private static Image[] ljcMall = new Image[3];
    private static Image chat;
    private static Image[][] role = new Image[4][8];
    private static Image[] aws = new Image[17];
    private static Image[] azu = new Image[6];
    private static Image[] hen = new Image[6];
    private static Image[] wcs = new Image[14];
    private static Image[] children = new Image[4];
    private static Image[] chick = new Image[2];
    private static Image[] littleChick = new Image[2];
    private static MovementController.WalkableMap[] walkableMaps = new MovementController.WalkableMap[2];

    private static Npc[] npc = new Npc[4];

    private static String[] awsWords = {"只要功夫深，铁衣磨成粉。", "你是要帮我洗衣服吗？", "走你"};
    private static String[] azuWords = {"How's it going ?", "What's wrong with you ?"};
    private static String[] wcsWords = {"Hi", "I'm washing clothes."};
    private static String[] childrenWords = {"Are you ok ?", "Let's play !"};

    private final MovementController movementController = new MovementController();

    static {
        try {
            BufferedImage villageRedMap = ImageIO.read(new File("img/LiJiaCun/RedMap.png"));
            BufferedImage marketRedMap = ImageIO.read(new File("img/LiJiaCunShiChang/RedMap.png"));
            walkableMaps[0] = createWalkableMap(villageRedMap, -521461);
            walkableMaps[1] = createWalkableMap(marketRedMap, -65536);

            ljc = ImageIO.read(new File("img/LiJiaCun/0.png"));

            for (int i = 0; i < 3; i++) {
                ljcMall[i] = ImageIO.read(new File("img/LiJiaCunShiChang/" + i + ".png"));
            }

            for (int i = 0; i < 4; i++) {
                String pathname = "";
                switch (i) {
                    case 0:
                        pathname = "img/LiXiaoYao_Down/";
                        break;
                    case 1:
                        pathname = "img/LiXiaoYao_Left/";
                        break;
                    case 2:
                        pathname = "img/LiXiaoYao_Right/";
                        break;
                    case 3:
                        pathname = "img/LiXiaoYao_Up/";
                        break;
                }
                for (int j = 0; j < 8; j++) {
                    role[i][j] = ImageIO.read(new File(pathname + j + ".png"));
                }
            }

            for (int i = 0; i < 17; i++) {
                String pathname = "img/AWangShen/" + i + ".png";
                aws[i] = ImageIO.read(new File(pathname));
            }

            for (int i = 0; i < 6; i++) {
                String pathname = "img/AZhu/" + i + ".png";
                azu[i] = ImageIO.read(new File(pathname));
            }

            for (int i = 0; i < 6; i++) {
                String pathname = "img/MuJi/" + i + ".png";
                hen[i] = ImageIO.read(new File(pathname));
            }

            for (int i = 0; i < 14; i++) {
                String pathname = "img/WangCaiSao/" + i + ".png";
                wcs[i] = ImageIO.read(new File(pathname));
            }

            for (int i = 0; i < 4; i++) {
                String pathname = "img/XiaoHai/" + i + ".png";
                children[i] = ImageIO.read(new File(pathname));
            }

            for (int i = 0; i < 2; i++) {
                String pathname = "img/XiaoJi/" + i + ".png";
                chick[i] = ImageIO.read(new File(pathname));
            }

            for (int i = 0; i < 2; i++) {
                String pathname = "img/XiaoXiaoJi/" + i + ".png";
                littleChick[i] = ImageIO.read(new File(pathname));
            }

            chat = ImageIO.read(new File("img/LiaoTian/0.png"));

            npc[0] = new Npc(awsWords, aws, 750, 480, "阿旺婶");
            npc[1] = new Npc(azuWords, azu, 560, 510, "阿朱");
            npc[2] = new Npc(wcsWords, wcs, 1030, 710, "旺财嫂");
            npc[3] = new Npc(childrenWords, children, 1160, 770, "熊孩子");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static MovementController.WalkableMap createWalkableMap(BufferedImage image, int blockedColor) {
        final boolean[][] walkableMap = new boolean[image.getHeight()][image.getWidth()];
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                walkableMap[y][x] = image.getRGB(x, y) != blockedColor;
            }
        }
        return new MovementController.WalkableMap() {
            @Override
            public int getWidth() {
                return walkableMap[0].length;
            }

            @Override
            public int getHeight() {
                return walkableMap.length;
            }

            @Override
            public boolean isWalkable(int x, int y) {
                return walkableMap[y][x];
            }
        };
    }

    public GamePanel() {
        t = new Thread(this);
        t.start();
    }

    public void updateIndex() {
        hen_i++;
        if (hen_i > 5) {
            hen_i = 0;
        }

        chick_i++;
        if (chick_i > 1) {
            chick_i = 0;
        }

        littleChick_i++;
        if (littleChick_i > 1) {
            littleChick_i = 0;
        }

        for (int i = 0; i < npc.length; i++) {
            npc[i].updateIndex();
        }
    }

    public void updateRoleIndex() {
        final int speed = 1;

        role_i += speed;
        if (role_i > 7) {
            role_i = 0;
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        if (mapID == 1) {
            ljcX = (1024 - role[0][0].getWidth(null)) / 2 - role_x;
            ljcY = (768 - role[0][0].getHeight(null)) / 2 - role_y;

            if (ljcY > 0) {
                ljcY = 0;
            } else if (ljcY < 768 - ljc.getHeight(null)) {
                ljcY = 768 - ljc.getHeight(null);
            }
            if (ljcX > 0) {
                ljcX = 0;
            } else if (ljcX < 1024 - ljc.getWidth(null)) {
                ljcX = 1024 - ljc.getWidth(null);
            }

            g.drawImage(ljc, ljcX, ljcY, this);
            g.drawImage(hen[hen_i], ljcX + 580, ljcY + 600, this);
            g.drawImage(chick[chick_i], ljcX + 550, ljcY + 620, this);
            g.drawImage(littleChick[littleChick_i], ljcX + 573, ljcY + 610, this);

            for (Npc aNpc : npc) {
                g.drawImage(aNpc.getImage(), aNpc.getX() + ljcX, aNpc.getY() + ljcY, this);
            }

            if (hasChat && !npc[chatWith].isChatOver()) {
                final int titleX = 200;
                final int titleY = 629;
                final int contentX = 360;
                final int contentY = 670;

                g.drawImage(chat, 192, 590, this);
                g.setFont(chatFont);
                g.setColor(Color.white);

                g.drawString(npc[chatWith].getName() + ":", titleX, titleY);
                g.drawString(npc[chatWith].getWords(), contentX, contentY);
            }

            g.drawImage(role[role_dir][role_i], role_x + ljcX, role_y + ljcY, this);
        } else if (mapID == 2) {
            mallX = (1024 - role[0][0].getWidth(null)) / 2 - role_x;
            mallY = (768 - role[0][0].getHeight(null)) / 2 - role_y;

            if (mallY > 0) {
                mallY = 0;
            } else if (mallY < 768 - ljcMall[0].getHeight(null)) {
                mallY = 768 - ljcMall[0].getHeight(null);
            }
            if (mallX > 0) {
                mallX = 0;
            } else if (mallX < 1024 - ljcMall[0].getWidth(null)) {
                mallX = 1024 - ljcMall[0].getWidth(null);
            }

            g.drawImage(ljcMall[mall_i], mallX, mallY, this);
            g.drawImage(role[role_dir][role_i], role_x + mallX, role_y + mallY, this);
        }
    }

    public boolean checkChat() {
        boolean ret = false;
        for (int i = 0; i < npc.length; ++i) {
            Npc n = npc[i];
            int d = 15;
            int ax = n.getX() - d;
            int ay = n.getY() - d;
            int aw = n.getWidth() + d;
            int ah = n.getHeight() + d;

            if ((role_x >= ax && role_x <= ax + aw && role_y >= ay && role_y <= ay + ah)
                    || (role_x + roleW >= ax && role_x + roleW <= ax + aw && role_y >= ay && role_y <= ay + ah)
                    || (role_x + roleW >= ax && role_x + roleW <= ax + aw && role_y + roleH >= ay && role_y + roleH <= ay + ah)
                    || (role_x >= ax && role_x <= ax + aw && role_y >= ay && role_y <= ay + ah)) {
                ret = true;
                chatWith = i;
                break;
            }
        }
        return ret;
    }

    @Override
    public void run() {
        while (true) {
            changeSpeed++;

            if (mapID == 1) {
                updateIndex();
            } else if (mapID == 2 && changeSpeed % 5 == 0) {
                mall_i++;
                if (mall_i > 2) {
                    mall_i = 0;
                }
            }

            if (changeSpeed > 1000) {
                changeSpeed = 0;
            }

            repaint();
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (!hasChat && checkChat()) {
                hasChat = true;
                npc[chatWith].setChatOver(false);
            } else if (hasChat && npc[chatWith].isChatOver()) {
                hasChat = false;
                npc[chatWith].chatIndex = 0;
            } else if (hasChat && !npc[chatWith].isChatOver()) {
                npc[chatWith].updateChatContent();
                if (npc[chatWith].chatIndex == 0) {
                    hasChat = false;
                }
            }
            repaint();
        } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (mapID == 1 && role_x >= 1780 && role_x <= 1855 && role_y >= 530 && role_y <= 615) {
                mapID = 2;
                role_x = 0;
                role_y = 600;
            } else if (mapID == 2 && role_x == -16 && role_y >= 552 && role_y <= 704) {
                mapID = 1;
                role_x = 1795;
                role_y = 570;
            }
            repaint();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (isDirectionKey(e.getKeyCode())) {
            handleMovement(e.getKeyCode());
            return;
        }

        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (hasChat) {
                hasChat = false;
            } else {
                int ch = JOptionPane.showConfirmDialog(null, "确认退出游戏吗？", "提示",
                        JOptionPane.YES_NO_OPTION);
                if (ch == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
            repaint();
        }
    }

    private boolean isDirectionKey(int keyCode) {
        return keyCode == KeyEvent.VK_UP
                || keyCode == KeyEvent.VK_DOWN
                || keyCode == KeyEvent.VK_LEFT
                || keyCode == KeyEvent.VK_RIGHT;
    }

    private void handleMovement(int keyCode) {
        hasChat = false;
        MovementController.MovementState state = new MovementController.MovementState(
                role_x,
                role_y,
                role_dir,
                role[0][0].getWidth(null),
                role[0][0].getHeight(null)
        );
        movementController.move(state, createKeyboardInput(keyCode), getCurrentWalkableMap());
        role_x = state.getRoleX();
        role_y = state.getRoleY();
        role_dir = state.getRoleDir();
        updateRoleFrame(keyCode);
        repaint();
    }

    private MovementController.KeyboardInput createKeyboardInput(final int keyCode) {
        return new MovementController.KeyboardInput() {
            @Override
            public boolean isUpPressed() {
                return keyCode == KeyEvent.VK_UP;
            }

            @Override
            public boolean isDownPressed() {
                return keyCode == KeyEvent.VK_DOWN;
            }

            @Override
            public boolean isLeftPressed() {
                return keyCode == KeyEvent.VK_LEFT;
            }

            @Override
            public boolean isRightPressed() {
                return keyCode == KeyEvent.VK_RIGHT;
            }
        };
    }

    private MovementController.WalkableMap getCurrentWalkableMap() {
        return walkableMaps[mapID - 1];
    }

    private void updateRoleFrame(int keyCode) {
        int index = -1;
        switch (keyCode) {
            case KeyEvent.VK_DOWN:
                index = 0;
                break;
            case KeyEvent.VK_LEFT:
                index = 1;
                break;
            case KeyEvent.VK_RIGHT:
                index = 2;
                break;
            case KeyEvent.VK_UP:
                index = 3;
                break;
        }

        if (index == -1) {
            return;
        }

        count[index]++;
        if (count[index] > 100) {
            count[index] = 0;
        }
        if (count[index] % 2 == 0) {
            updateRoleIndex();
        }
    }
}
