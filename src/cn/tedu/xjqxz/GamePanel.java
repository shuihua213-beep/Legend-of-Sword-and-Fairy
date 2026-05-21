package cn.tedu.xjqxz;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GamePanel extends Canvas implements Runnable, KeyListener {
    private static final int VIEWPORT_WIDTH = 1024;
    private static final int VIEWPORT_HEIGHT = 768;
    private static final long FRAME_DELAY_MS = 16L;
    private static final long ANIMATION_DELAY_MS = 200L;

    private Thread t;
    private volatile boolean running;
    private volatile boolean bufferStrategyDirty = true;

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

    public GamePanel() {
        setPreferredSize(new Dimension(VIEWPORT_WIDTH, VIEWPORT_HEIGHT));
        setSize(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        setBackground(Color.BLACK);
        setFocusable(true);
        setIgnoreRepaint(true);
        addKeyListener(this);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                bufferStrategyDirty = true;
            }

            @Override
            public void componentShown(ComponentEvent e) {
                bufferStrategyDirty = true;
            }
        });
    }

    @Override
    public void addNotify() {
        super.addNotify();
        startLoop();
        EventQueue.invokeLater(this::requestFocusInWindow);
    }

    @Override
    public void removeNotify() {
        stopLoop();
        super.removeNotify();
    }

    private synchronized void startLoop() {
        if (running) {
            return;
        }
        running = true;
        t = new Thread(this, "game-render-loop");
        t.start();
    }

    private synchronized void stopLoop() {
        running = false;
        if (t != null) {
            t.interrupt();
            t = null;
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

        for (Npc aNpc : npc) {
            aNpc.updateIndex();
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
    }

    @Override
    public void update(Graphics g) {
    }

    private int getViewportWidth() {
        int width = getWidth();
        return width > 0 ? width : VIEWPORT_WIDTH;
    }

    private int getViewportHeight() {
        int height = getHeight();
        return height > 0 ? height : VIEWPORT_HEIGHT;
    }

    private int clampMapOffset(int offset, int viewportSize, int imageSize) {
        if (viewportSize >= imageSize) {
            return (viewportSize - imageSize) / 2;
        }
        if (offset > 0) {
            return 0;
        }
        int minOffset = viewportSize - imageSize;
        if (offset < minOffset) {
            return minOffset;
        }
        return offset;
    }

    private void renderScene(Graphics2D g) {
        int viewportWidth = getViewportWidth();
        int viewportHeight = getViewportHeight();

        if (mapID == 1) {
            ljcX = (viewportWidth - role[0][0].getWidth(null)) / 2 - role_x;
            ljcY = (viewportHeight - role[0][0].getHeight(null)) / 2 - role_y;
            ljcX = clampMapOffset(ljcX, viewportWidth, ljc.getWidth(null));
            ljcY = clampMapOffset(ljcY, viewportHeight, ljc.getHeight(null));

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
            mallX = (viewportWidth - role[0][0].getWidth(null)) / 2 - role_x;
            mallY = (viewportHeight - role[0][0].getHeight(null)) / 2 - role_y;
            mallX = clampMapOffset(mallX, viewportWidth, ljcMall[0].getWidth(null));
            mallY = clampMapOffset(mallY, viewportHeight, ljcMall[0].getHeight(null));

            g.drawImage(ljcMall[mall_i], mallX, mallY, this);
            g.drawImage(role[role_dir][role_i], role_x + mallX, role_y + mallY, this);
        }
    }

    private synchronized BufferStrategy ensureBufferStrategy() {
        if (!isDisplayable() || getWidth() <= 0 || getHeight() <= 0) {
            return null;
        }

        BufferStrategy strategy = getBufferStrategy();
        if (strategy == null || bufferStrategyDirty) {
            try {
                createBufferStrategy(2);
            } catch (IllegalStateException e) {
                return null;
            }
            bufferStrategyDirty = false;
            strategy = getBufferStrategy();
        }
        return strategy;
    }

    private void renderFrame() {
        BufferStrategy strategy = ensureBufferStrategy();
        if (strategy == null) {
            return;
        }

        do {
            do {
                Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
                try {
                    g.setColor(getBackground());
                    g.fillRect(0, 0, getViewportWidth(), getViewportHeight());
                    renderScene(g);
                } finally {
                    g.dispose();
                }
            } while (strategy.contentsRestored());

            strategy.show();
            Toolkit.getDefaultToolkit().sync();
        } while (strategy.contentsLost());
    }

    private void advanceAnimation() {
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
    }

    @Override
    public void run() {
        long lastAnimationTick = System.currentTimeMillis();

        while (running) {
            long frameStart = System.nanoTime();
            long now = System.currentTimeMillis();

            while (now - lastAnimationTick >= ANIMATION_DELAY_MS) {
                advanceAnimation();
                lastAnimationTick += ANIMATION_DELAY_MS;
            }

            renderFrame();

            long elapsedMs = (System.nanoTime() - frameStart) / 1_000_000L;
            long sleepMs = FRAME_DELAY_MS - elapsedMs;
            if (sleepMs > 0) {
                try {
                    Thread.sleep(sleepMs);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    running = false;
                }
            } else {
                Thread.yield();
            }
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
                int x = role_x + role[0][0].getWidth(null) / 2;
                int y = role_y + role[0][0].getHeight(null);
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

                count[0]++;
                if (count[0] > 100) {
                    count[0] = 0;
                }
                if (count[0] % 2 == 0) {
                    updateRoleIndex();
                }
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

                    count[1]++;
                    if (count[1] > 100) {
                        count[1] = 0;
                    }
                    if (count[1] % 2 == 0) {
                        updateRoleIndex();
                    }
                }
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

                count[2]++;
                if (count[2] > 100) {
                    count[2] = 0;
                }
                if (count[2] % 2 == 0) {
                    updateRoleIndex();
                }
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
                    EventQueue.invokeLater(this::requestFocusInWindow);
                }
                break;
        }
    }
}
