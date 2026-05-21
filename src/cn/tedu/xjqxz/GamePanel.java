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
    private static final String PATH_LJC_MAP = "img/LiJiaCun/RedMap.png";
    private static final String PATH_MALL_MAP = "img/LiJiaCunShiChang/RedMap.png";
    private static final String PATH_LJC = "img/LiJiaCun/0.png";
    private static final String PATH_CHAT = "img/LiaoTian/0.png";
    private static final String[] LJC_MALL_PATHS = createImagePaths("img/LiJiaCunShiChang/", 3);
    private static final String[] HEN_PATHS = createImagePaths("img/MuJi/", 6);
    private static final String[] CHICK_PATHS = createImagePaths("img/XiaoJi/", 2);
    private static final String[] LITTLE_CHICK_PATHS = createImagePaths("img/XiaoXiaoJi/", 2);
    private static final String[] AWS_PATHS = createImagePaths("img/AWangShen/", 17);
    private static final String[] AZU_PATHS = createImagePaths("img/AZhu/", 6);
    private static final String[] WCS_PATHS = createImagePaths("img/WangCaiSao/", 14);
    private static final String[] CHILDREN_PATHS = createImagePaths("img/XiaoHai/", 4);
    private static final String[][] ROLE_PATHS = new String[][]{
            createImagePaths("img/LiXiaoYao_Down/", 8),
            createImagePaths("img/LiXiaoYao_Left/", 8),
            createImagePaths("img/LiXiaoYao_Right/", 8),
            createImagePaths("img/LiXiaoYao_Up/", 8)
    };
    private static final String[] awsWords = {"只要功夫深，铁衣磨成粉。", "你是要帮我洗衣服吗？", "走你"};
    private static final String[] azuWords = {"How's it going ?", "What's wrong with you ?"};
    private static final String[] wcsWords = {"Hi", "I'm washing clothes."};
    private static final String[] childrenWords = {"Are you ok ?", "Let's play !"};
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
    private final Npc[] npc = new Npc[]{
            new Npc(awsWords, AWS_PATHS, 750, 480, "阿旺婶"),
            new Npc(azuWords, AZU_PATHS, 560, 510, "阿朱"),
            new Npc(wcsWords, WCS_PATHS, 1030, 710, "旺财嫂"),
            new Npc(childrenWords, CHILDREN_PATHS, 1160, 770, "熊孩子")
    };

    public GamePanel() {
        preloadSceneAssets(mapID);
        t = new Thread(this);
        t.start();
    }

    private static String[] createImagePaths(String basePath, int count) {
        String[] paths = new String[count];
        for (int i = 0; i < count; i++) {
            paths[i] = basePath + i + ".png";
        }
        return paths;
    }

    private void preloadSceneAssets(int targetMapId) {
        preloadRoleAssets();
        if (targetMapId == 1) {
            ImageCache.preloadBufferedImages(new String[]{PATH_LJC_MAP});
            ImageCache.preloadImages(new String[]{PATH_LJC, PATH_CHAT});
            ImageCache.preloadImages(HEN_PATHS);
            ImageCache.preloadImages(CHICK_PATHS);
            ImageCache.preloadImages(LITTLE_CHICK_PATHS);
            for (int i = 0; i < npc.length; i++) {
                npc[i].preloadImages();
            }
        } else if (targetMapId == 2) {
            ImageCache.preloadBufferedImages(new String[]{PATH_MALL_MAP});
            ImageCache.preloadImages(LJC_MALL_PATHS);
        }
    }

    private void flushSceneAssets(int sourceMapId) {
        flushRoleAssets();
        if (sourceMapId == 1) {
            ImageCache.flushBufferedImages(new String[]{PATH_LJC_MAP});
            ImageCache.flushImages(new String[]{PATH_LJC, PATH_CHAT});
            ImageCache.flushImages(HEN_PATHS);
            ImageCache.flushImages(CHICK_PATHS);
            ImageCache.flushImages(LITTLE_CHICK_PATHS);
            for (int i = 0; i < npc.length; i++) {
                npc[i].flushImages();
            }
        } else if (sourceMapId == 2) {
            ImageCache.flushBufferedImages(new String[]{PATH_MALL_MAP});
            ImageCache.flushImages(LJC_MALL_PATHS);
        }
    }

    private void preloadRoleAssets() {
        for (int i = 0; i < ROLE_PATHS.length; i++) {
            ImageCache.preloadImages(ROLE_PATHS[i]);
        }
    }

    private void flushRoleAssets() {
        for (int i = 0; i < ROLE_PATHS.length; i++) {
            ImageCache.flushImages(ROLE_PATHS[i]);
        }
    }

    private void switchScene(int targetMapId, int newRoleX, int newRoleY) {
        hasChat = false;
        flushSceneAssets(mapID);
        preloadSceneAssets(targetMapId);
        mapID = targetMapId;
        role_x = newRoleX;
        role_y = newRoleY;
        role_i = 0;
        mall_i = 0;
    }

    private Image getRoleImage(int direction, int index) {
        return ImageCache.getImage(ROLE_PATHS[direction][index]);
    }

    private Image getVillageBackground() {
        return ImageCache.getImage(PATH_LJC);
    }

    private Image getMallBackground() {
        return ImageCache.getImage(LJC_MALL_PATHS[mall_i]);
    }

    private Image getChatBackground() {
        return ImageCache.getImage(PATH_CHAT);
    }

    private BufferedImage getCurrentDataMap() {
        if (mapID == 1) {
            return ImageCache.getBufferedImage(PATH_LJC_MAP);
        }
        return ImageCache.getBufferedImage(PATH_MALL_MAP);
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

        Image roleBaseImage = getRoleImage(0, 0);
        int roleImageWidth = roleBaseImage.getWidth(null);
        int roleImageHeight = roleBaseImage.getHeight(null);

        if (mapID == 1) {
            Image villageBackground = getVillageBackground();
            ljcX = (1024 - roleImageWidth) / 2 - role_x;
            ljcY = (768 - roleImageHeight) / 2 - role_y;

            if (ljcY > 0) {
                ljcY = 0;
            } else if (ljcY < 768 - villageBackground.getHeight(null)) {
                ljcY = 768 - villageBackground.getHeight(null);
            }
            if (ljcX > 0) {
                ljcX = 0;
            } else if (ljcX < 1024 - villageBackground.getWidth(null)) {
                ljcX = 1024 - villageBackground.getWidth(null);
            }

            g.drawImage(villageBackground, ljcX, ljcY, this);
            g.drawImage(ImageCache.getImage(HEN_PATHS[hen_i]), ljcX + 580, ljcY + 600, this);
            g.drawImage(ImageCache.getImage(CHICK_PATHS[chick_i]), ljcX + 550, ljcY + 620, this);
            g.drawImage(ImageCache.getImage(LITTLE_CHICK_PATHS[littleChick_i]), ljcX + 573, ljcY + 610, this);

            for (int i = 0; i < npc.length; i++) {
                Npc aNpc = npc[i];
                g.drawImage(aNpc.getImage(), aNpc.getX() + ljcX, aNpc.getY() + ljcY, this);
            }

            if (hasChat && !npc[chatWith].isChatOver()) {
                final int titleX = 200;
                final int titleY = 629;
                final int contentX = 360;
                final int contentY = 670;

                g.drawImage(getChatBackground(), 192, 590, this);
                g.setFont(chatFont);
                g.setColor(Color.white);

                g.drawString(npc[chatWith].getName() + ":", titleX, titleY);
                g.drawString(npc[chatWith].getWords(), contentX, contentY);
            }

            g.drawImage(getRoleImage(role_dir, role_i), role_x + ljcX, role_y + ljcY, this);

        } else if (mapID == 2) {
            Image mallBackground = getMallBackground();
            mallX = (1024 - roleImageWidth) / 2 - role_x;
            mallY = (768 - roleImageHeight) / 2 - role_y;

            if (mallY > 0) {
                mallY = 0;
            } else if (mallY < 768 - mallBackground.getHeight(null)) {
                mallY = 768 - mallBackground.getHeight(null);
            }
            if (mallX > 0) {
                mallX = 0;
            } else if (mallX < 1024 - mallBackground.getWidth(null)) {
                mallX = 1024 - mallBackground.getWidth(null);
            }

            g.drawImage(mallBackground, mallX, mallY, this);
            g.drawImage(getRoleImage(role_dir, role_i), role_x + mallX, role_y + mallY, this);
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
            if (mapID == 1) {
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
            }
            repaint();
        } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (mapID == 1 && role_x >= 1780 && role_x <= 1855 && role_y >= 530 && role_y <= 615) {
                switchScene(2, 0, 600);
            } else if (mapID == 2 && role_x == -16 && role_y >= 552 && role_y <= 704) {
                switchScene(1, 1795, 570);
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
                int x = role_x + getRoleImage(0, 0).getWidth(null) / 2;
                int y = role_y + getRoleImage(0, 0).getHeight(null);
                if (mapID == 1 && getCurrentDataMap().getRGB(x, y) == -521461) {
                    role_y += speed;
                } else if (mapID == 2 && getCurrentDataMap().getRGB(x, y) == -65536) {
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
                x = role_x + getRoleImage(0, 0).getWidth(null) / 2;
                y = role_y + getRoleImage(0, 0).getHeight(null);

                if (mapID == 1 && getCurrentDataMap().getRGB(x, y) == -521461) {
                    role_y -= speed;
                } else if (mapID == 2 && getCurrentDataMap().getRGB(x, y) == -65536) {
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
                    x = role_x + getRoleImage(0, 0).getWidth(null) / 2;
                    y = role_y + getRoleImage(0, 0).getHeight(null);

                    if (mapID == 1 && getCurrentDataMap().getRGB(x, y) == -521461) {
                        role_x += speed;
                    } else if (mapID == 2 && getCurrentDataMap().getRGB(x, y) == -65536) {
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
                x = role_x + getRoleImage(0, 0).getWidth(null) / 2;
                y = role_y + getRoleImage(0, 0).getHeight(null);

                if (mapID == 1 && getCurrentDataMap().getRGB(x, y) == -521461) {
                    role_x -= speed;
                } else if (mapID == 2 && getCurrentDataMap().getRGB(x, y) == -65536) {
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
