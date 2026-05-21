package cn.tedu.xjqxz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

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

    // 用于调节李家村市场场景变化速度
    int changeSpeed = 0;
    int[] count = new int[4];

    // 当前背景, 1表示李家村，2表示李家村市场
    int mapID = 1;

    // 聊天对象
    int chatWith = 0;
    Font chatFont = new Font("黑体", Font.BOLD, 25);


    // 当前是否有对话窗口
    boolean hasChat = false;

    // 背景图片绘制坐标
    private static int ljcX = -400;
    private static int ljcY = -190;
    private static int mallX = -200;
    private static int mallY = -200;

    private static Image ljc;
    private static Image[] ljcMall = new Image[3];
    private static Image chat;
    private static Image[][] role = new Image[4][8];
    private static Image[] hen = new Image[6];
    private static Image[] chick = new Image[2];
    private static Image[] littleChick = new Image[2];

    private static BufferedImage[] dataMap = new BufferedImage[2];

    // 存储npc对象
    private static Npc[] npc = new Npc[4];

    private static String[] awsWords = {"只要功夫深，铁衣磨成粉。", "你是要帮我洗衣服吗？", "走你"};
    private static String[] azuWords = {"How's it going ?", "What's wrong with you ?"};
    private static String[] wcsWords = {"Hi", "I'm washing clothes."};
    private static String[] childrenWords = {"Are you ok ?", "Let's play !"};

    // 图片路径常量
    private static final String PATH_LJC_MAP = "img/LiJiaCun/RedMap.png";
    private static final String PATH_MALL_MAP = "img/LiJiaCunShiChang/RedMap.png";
    private static final String PATH_LJC = "img/LiJiaCun/0.png";
    private static final String PATH_LJC_MALL = "img/LiJiaCunShiChang/";
    private static final String[] PATH_ROLE = {
        "img/LiXiaoYao_Down/",
        "img/LiXiaoYao_Left/",
        "img/LiXiaoYao_Right/",
        "img/LiXiaoYao_Up/"
    };
    private static final String PATH_AWS = "img/AWangShen/";
    private static final String PATH_AZU = "img/AZhu/";
    private static final String PATH_HEN = "img/MuJi/";
    private static final String PATH_WCS = "img/WangCaiSao/";
    private static final String PATH_CHILDREN = "img/XiaoHai/";
    private static final String PATH_CHICK = "img/XiaoJi/";
    private static final String PATH_LITTLE_CHICK = "img/XiaoXiaoJi/";
    private static final String PATH_CHAT = "img/LiaoTian/0.png";

    /**
     * 加载素材图片
     */
    private void loadImages() {
        dataMap[0] = ImageCache.getBufferedImage(PATH_LJC_MAP);
        dataMap[1] = ImageCache.getBufferedImage(PATH_MALL_MAP);

        ljc = ImageCache.getImage(PATH_LJC);

        for (int i = 0; i < 3; i++) {
            ljcMall[i] = ImageCache.getImage(PATH_LJC_MALL + i + ".png");
        }

        // 读取李逍遥图片
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 8; j++) {
                role[i][j] = ImageCache.getImage(PATH_ROLE[i] + j + ".png");
            }
        }

        Image[] aws = ImageCache.loadImageArray(PATH_AWS, 17);
        Image[] azu = ImageCache.loadImageArray(PATH_AZU, 6);
        Image[] wcs = ImageCache.loadImageArray(PATH_WCS, 14);
        Image[] children = ImageCache.loadImageArray(PATH_CHILDREN, 4);

        for (int i = 0; i < 6; i++) {
            hen[i] = ImageCache.getImage(PATH_HEN + i + ".png");
        }

        for (int i = 0; i < 2; i++) {
            chick[i] = ImageCache.getImage(PATH_CHICK + i + ".png");
            littleChick[i] = ImageCache.getImage(PATH_LITTLE_CHICK + i + ".png");
        }

        chat = ImageCache.getImage(PATH_CHAT);

        npc[0] = new Npc(awsWords, aws, 750, 480, "阿旺婶", PATH_AWS);
        npc[1] = new Npc(azuWords, azu, 560, 510, "阿朱", PATH_AZU);
        npc[2] = new Npc(wcsWords, wcs, 1030, 710, "旺财嫂", PATH_WCS);
        npc[3] = new Npc(childrenWords, children, 1160, 770, "熊孩子", PATH_CHILDREN);
    }

    /**
     * 释放李家村场景的图片资源
     */
    private void releaseLiJiaCunImages() {
        for (int i = 0; i < 6; i++) {
            if (hen[i] != null) {
                hen[i].flush();
            }
        }
        for (int i = 0; i < 2; i++) {
            if (chick[i] != null) {
                chick[i].flush();
            }
            if (littleChick[i] != null) {
                littleChick[i].flush();
            }
        }
        for (Npc n : npc) {
            n.flushImages();
        }
        ImageCache.flushImage(PATH_LJC);
        ImageCache.flushImageArray(PATH_HEN, 6);
        ImageCache.flushImageArray(PATH_CHICK, 2);
        ImageCache.flushImageArray(PATH_LITTLE_CHICK, 2);
    }

    /**
     * 释放市场场景的图片资源
     */
    private void releaseMarketImages() {
        for (int i = 0; i < 3; i++) {
            if (ljcMall[i] != null) {
                ljcMall[i].flush();
            }
        }
        ImageCache.flushImageArray(PATH_LJC_MALL, 3);
    }

    /**
     * 切换场景时处理图片资源
     */
    private void switchScene(int newMapID) {
        if (mapID == 1 && newMapID == 2) {
            releaseLiJiaCunImages();
        } else if (mapID == 2 && newMapID == 1) {
            releaseMarketImages();
        }
        mapID = newMapID;
        loadImages();
    }

    public GamePanel() {
        loadImages();
        t = new Thread(this);
        t.start();
    }

    /**
     * 改变配角图片
     */
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

    /**
     * 改变主角图片
     */
    public void updateRoleIndex() {
        // 步伐速度
        final int speed = 1;

        role_i += speed;
        if (role_i > 7) {
            role_i = 0;
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // 场景在李家村
        if (mapID == 1) {
            // 背景绘制
            ljcX = (1024 - role[0][0].getWidth(null)) / 2 - role_x;
            ljcY = (768 - role[0][0].getHeight(null)) / 2 - role_y;

            //判断李家村图片的边界问题
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

            // 背景绘制
            g.drawImage(ljc, ljcX, ljcY, this);

            // 配角绘制
            g.drawImage(hen[hen_i], ljcX + 580, ljcY + 600, this);
            g.drawImage(chick[chick_i], ljcX + 550, ljcY + 620, this);
            g.drawImage(littleChick[littleChick_i], ljcX + 573, ljcY + 610, this);

            // npc绘制
            for (Npc aNpc : npc) {
                g.drawImage(aNpc.getImage(), aNpc.getX() + ljcX, aNpc.getY() + ljcY, this);
            }

            // 对话框绘制
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

            // 主角绘制
            g.drawImage(role[role_dir][role_i], role_x + ljcX, role_y + ljcY, this);

        } else if (mapID == 2) {
            // 场景在李家村市场

            mallX = (1024 - role[0][0].getWidth(null)) / 2 - role_x;
            mallY = (768 - role[0][0].getHeight(null)) / 2 - role_y;

            //判断李家村市场图片的边界问题
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

            // 背景绘制
            g.drawImage(ljcMall[mall_i], mallX, mallY, this);

            // 主角绘制
            g.drawImage(role[role_dir][role_i], role_x + mallX, role_y + mallY, this);
        }
    }

    /**
     * 检测主角是否靠近npc发起对话
     * 碰撞检测
     * 判断主角的四个顶点是否在某个npc的矩形之中
     * 将配角图片矩形稍微扩大d
     */
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

    /**
     * 松开空格键弹出对话框，防止一直按住空格对话框闪烁
     * 松开回车键切换场景，防止一直按住
     */
    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
//            System.out.println(role_x + "," + role_y);
            if (!hasChat && checkChat()) {
                hasChat = true;
                npc[chatWith].setChatOver(false);
            } else if (hasChat && npc[chatWith].isChatOver()) {
                // 若聊天已经结束，则关闭聊天窗口
                hasChat = false;
                npc[chatWith].chatIndex = 0;
            } else if (hasChat && !npc[chatWith].isChatOver()) {
                // 如果聊天未结束，则更新聊天内容
                npc[chatWith].updateChatContent();
                if (npc[chatWith].chatIndex == 0) {
                    hasChat = false;
                }
            }
            repaint();
        } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            // 按下回车键判断是否切换场景
            if (mapID == 1 && role_x >= 1780 && role_x <= 1855 && role_y >= 530 && role_y <= 615) {
                switchScene(2);
                role_x = 0;
                role_y = 600;
            } else if (mapID == 2 && role_x == -16 && role_y >= 552 && role_y <= 704) {
                switchScene(1);
                role_x = 1795;
                role_y = 570;
            }
            repaint();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // 主角移动速度
        final int speed = 4;

        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                role_dir = 3;
                hasChat = false;
                role_y -= speed;
                int x = role_x + role[0][0].getWidth(null) / 2;
                int y = role_y + role[0][0].getHeight(null);
                if (mapID == 1 && dataMap[0].getRGB(x, y) == -521461) {
                    role_y += speed;
                } else if (mapID == 2 && dataMap[1].getRGB(x, y) == -65536) {
                    role_y += speed;
                }

                // 调整角色步伐改变速度
                count[3]++;
                if (count[3] > 100) {
                    count[3] = 0;
                }
                if (count[3] % 2 == 0) {
                    updateRoleIndex();
                }
                repaint();
                break;

            case KeyEvent.VK_DOWN:
                role_dir = 0;
                hasChat = false;
                role_y += speed;
                x = role_x + role[0][0].getWidth(null) / 2;
                y = role_y + role[0][0].getHeight(null);

                if (mapID == 1 && dataMap[0].getRGB(x, y) == -521461) {
                    role_y -= speed;
                } else if (mapID == 2 && dataMap[1].getRGB(x, y) == -65536) {
                    role_y -= speed;
                }

                // 调整角色步伐改变速度
                count[0]++;
                if (count[0] > 100) {
                    count[0] = 0;
                }
                if (count[0] % 2 == 0) {
                    updateRoleIndex();
                }
                repaint();
                break;

            case KeyEvent.VK_LEFT:
                role_dir = 1;
                hasChat = false;

                if (role_x > -16) {
                    role_x -= speed;
                    x = role_x + role[0][0].getWidth(null) / 2;
                    y = role_y + role[0][0].getHeight(null);

                    if (mapID == 1 && dataMap[0].getRGB(x, y) == -521461) {
                        role_x += speed;
                    } else if (mapID == 2 && dataMap[1].getRGB(x, y) == -65536) {
                        role_x += speed;
                    }

                    // 调整角色步伐改变速度
                    count[1]++;
                    if (count[1] > 100) {
                        count[1] = 0;
                    }
                    if (count[1] % 2 == 0) {
                        updateRoleIndex();
                    }
                }
                repaint();
                break;

            case KeyEvent.VK_RIGHT:
                role_dir = 2;
                hasChat = false;

                role_x += speed;
                x = role_x + role[0][0].getWidth(null) / 2;
                y = role_y + role[0][0].getHeight(null);

                if (mapID == 1 && dataMap[0].getRGB(x, y) == -521461) {
                    role_x -= speed;
                } else if (mapID == 2 && dataMap[1].getRGB(x, y) == -65536) {
                    role_x -= speed;
                }

                // 调整角色步伐改变速度
                count[2]++;
                if (count[2] > 100) {
                    count[2] = 0;
                }
                if (count[2] % 2 == 0) {
                    updateRoleIndex();
                }

                repaint();
                break;

            case KeyEvent.VK_ESCAPE:
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
                break;
        }
    }
}
