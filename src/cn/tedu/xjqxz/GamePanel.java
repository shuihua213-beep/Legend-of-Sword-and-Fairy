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

    private static CachedImage ljc;
    private static CachedImage[] ljcMall = new CachedImage[3];
    private static CachedImage chat;
    private static CachedImage[][] role = new CachedImage[4][8];
    private static CachedImage[] aws = new CachedImage[17];
    private static CachedImage[] azu = new CachedImage[6];
    private static CachedImage[] hen = new CachedImage[6];
    private static CachedImage[] wcs = new CachedImage[14];
    private static CachedImage[] children = new CachedImage[4];
    private static CachedImage[] chick = new CachedImage[2];
    private static CachedImage[] littleChick = new CachedImage[2];

    private static BufferedImage[] dataMap = new BufferedImage[2];

    private static Npc[] npc = new Npc[4];

    private static String[] awsWords = {"只要功夫深，铁衣磨成粉。", "你是要帮我洗衣服吗？", "走你"};
    private static String[] azuWords = {"How's it going ?", "What's wrong with you ?"};
    private static String[] wcsWords = {"Hi", "I'm washing clothes."};
    private static String[] childrenWords = {"Are you ok ?", "Let's play !"};

    static {
        try {
            dataMap[0] = ImageIO.read(new File("img/LiJiaCun/RedMap.png"));
            dataMap[1] = ImageIO.read(new File("img/LiJiaCunShiChang/RedMap.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        ljc = new CachedImage("img/LiJiaCun/0.png");

        for (int i = 0; i < 3; i++) {
            ljcMall[i] = new CachedImage("img/LiJiaCunShiChang/" + i + ".png");
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
                role[i][j] = new CachedImage(pathname + j + ".png");
            }
        }

        for (int i = 0; i < 17; i++) {
            aws[i] = new CachedImage("img/AWangShen/" + i + ".png");
        }

        for (int i = 0; i < 6; i++) {
            azu[i] = new CachedImage("img/AZhu/" + i + ".png");
        }

        for (int i = 0; i < 6; i++) {
            hen[i] = new CachedImage("img/MuJi/" + i + ".png");
        }

        for (int i = 0; i < 14; i++) {
            wcs[i] = new CachedImage("img/WangCaiSao/" + i + ".png");
        }

        for (int i = 0; i < 4; i++) {
            children[i] = new CachedImage("img/XiaoHai/" + i + ".png");
        }

        for (int i = 0; i < 2; i++) {
            chick[i] = new CachedImage("img/XiaoJi/" + i + ".png");
        }

        for (int i = 0; i < 2; i++) {
            littleChick[i] = new CachedImage("img/XiaoXiaoJi/" + i + ".png");
        }

        chat = new CachedImage("img/LiaoTian/0.png");

        npc[0] = new Npc(awsWords, aws, 750, 480, "阿旺婶");
        npc[1] = new Npc(azuWords, azu, 560, 510, "阿朱");
        npc[2] = new Npc(wcsWords, wcs, 1030, 710, "旺财嫂");
        npc[3] = new Npc(childrenWords, children, 1160, 770, "熊孩子");
    }

    public GamePanel() {
        t = new Thread(this);
        t.start();
    }

    private void flushSceneImages(int targetMapID) {
        if (targetMapID == 1) {
            for (CachedImage ci : ljcMall) {
                ci.flush();
            }
        } else if (targetMapID == 2) {
            System.runFinalization();
            System.gc();

            ljc.flush();

            for (CachedImage ci : hen) {
                ci.flush();
            }
            for (CachedImage ci : chick) {
                ci.flush();
            }
            for (CachedImage ci : littleChick) {
                ci.flush();
            }

            for (CachedImage ci : aws) {
                ci.flush();
            }
            for (CachedImage ci : azu) {
                ci.flush();
            }
            for (CachedImage ci : wcs) {
                ci.flush();
            }
            for (CachedImage ci : children) {
                ci.flush();
            }
        }
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
            Image roleImg = role[0][0].get();
            Image ljcImg = ljc.get();

            ljcX = (1024 - roleImg.getWidth(null)) / 2 - role_x;
            ljcY = (768 - roleImg.getHeight(null)) / 2 - role_y;

            if (ljcY > 0) {
                ljcY = 0;
            } else if (ljcY < 768 - ljcImg.getHeight(null)) {
                ljcY = 768 - ljcImg.getHeight(null);
            }
            if (ljcX > 0) {
                ljcX = 0;
            } else if (ljcX < 1024 - ljcImg.getWidth(null)) {
                ljcX = 1024 - ljcImg.getWidth(null);
            }

            g.drawImage(ljcImg, ljcX, ljcY, this);

            g.drawImage(hen[hen_i].get(), ljcX + 580, ljcY + 600, this);
            g.drawImage(chick[chick_i].get(), ljcX + 550, ljcY + 620, this);
            g.drawImage(littleChick[littleChick_i].get(), ljcX + 573, ljcY + 610, this);

            for (Npc aNpc : npc) {
                g.drawImage(aNpc.getImage(), aNpc.getX() + ljcX, aNpc.getY() + ljcY, this);
            }

            if (hasChat && !npc[chatWith].isChatOver()) {
                final int titleX = 200;
                final int titleY = 629;
                final int contentX = 360;
                final int contentY = 670;

                g.drawImage(chat.get(), 192, 590, this);
                g.setFont(chatFont);
                g.setColor(Color.white);

                g.drawString(npc[chatWith].getName() + ":", titleX, titleY);
                g.drawString(npc[chatWith].getWords(), contentX, contentY);
            }

            g.drawImage(role[role_dir][role_i].get(), role_x + ljcX, role_y + ljcY, this);

        } else if (mapID == 2) {

            Image roleImg = role[0][0].get();
            Image mallImg = ljcMall[0].get();

            mallX = (1024 - roleImg.getWidth(null)) / 2 - role_x;
            mallY = (768 - roleImg.getHeight(null)) / 2 - role_y;

            if (mallY > 0) {
                mallY = 0;
            } else if (mallY < 768 - mallImg.getHeight(null)) {
                mallY = 768 - mallImg.getHeight(null);
            }
            if (mallX > 0) {
                mallX = 0;
            } else if (mallX < 1024 - mallImg.getWidth(null)) {
                mallX = 1024 - mallImg.getWidth(null);
            }

            g.drawImage(ljcMall[mall_i].get(), mallX, mallY, this);

            g.drawImage(role[role_dir][role_i].get(), role_x + mallX, role_y + mallY, this);
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
                flushSceneImages(mapID);
            } else if (mapID == 2 && role_x == -16 && role_y >= 552 && role_y <= 704) {
                mapID = 1;
                role_x = 1795;
                role_y = 570;
                flushSceneImages(mapID);
            }
            repaint();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        final int speed = 4;

        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                role_dir = 3;
                hasChat = false;
                role_y -= speed;
                int x = role_x + role[0][0].get().getWidth(null) / 2;
                int y = role_y + role[0][0].get().getHeight(null);
                if (mapID == 1 && dataMap[0].getRGB(x, y) == -521461) {
                    role_y += speed;
                } else if (mapID == 2 && dataMap[1].getRGB(x, y) == -65536) {
                    role_y += speed;
                }

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
                x = role_x + role[0][0].get().getWidth(null) / 2;
                y = role_y + role[0][0].get().getHeight(null);

                if (mapID == 1 && dataMap[0].getRGB(x, y) == -521461) {
                    role_y -= speed;
                } else if (mapID == 2 && dataMap[1].getRGB(x, y) == -65536) {
                    role_y -= speed;
                }

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
                    x = role_x + role[0][0].get().getWidth(null) / 2;
                    y = role_y + role[0][0].get().getHeight(null);

                    if (mapID == 1 && dataMap[0].getRGB(x, y) == -521461) {
                        role_x += speed;
                    } else if (mapID == 2 && dataMap[1].getRGB(x, y) == -65536) {
                        role_x += speed;
                    }

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
                x = role_x + role[0][0].get().getWidth(null) / 2;
                y = role_y + role[0][0].get().getHeight(null);

                if (mapID == 1 && dataMap[0].getRGB(x, y) == -521461) {
                    role_x -= speed;
                } else if (mapID == 2 && dataMap[1].getRGB(x, y) == -65536) {
                    role_x -= speed;
                }

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