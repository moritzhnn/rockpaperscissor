package de.moritz.rps06;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;


public class GameGraphics2 extends JFrame implements ActionListener, MouseWheelListener {
    private JPanel contentPane;
    private Image dbImage, tmpDbImage, paper, rock, scissors, img;
    private double speed;
    private Graphics dbg;
    private int paperCount, rockCount, scissorsCount, mouseX, mouseY, rows, cols;
    private static final int gridSize = 50;
    private ArrayList<FlyingObjects>[][] grid;
    private JPanel dashBoard;
    PointerInfo pointerInfo;
    Point point;


    public int getMouseY() {
        return mouseY;
    }

    public void setMouseX(int mouseX) {
        this.mouseX = mouseX;
    }

    public void setMouseY(int mouseY) {
        this.mouseY = mouseY;
    }

    public int getMouseX() {
        return mouseX;
    }

    private Game game;
    Timer timer;
    private java.util.List<FlyingObjects> flyingObjects = new ArrayList<>();


    public void start() {
        EventQueue.invokeLater(new Runnable() { // Neuer lauffähiger Thread wird erstellt (Thead = Teil eines Prozesses)
            @Override
            public void run() {
                try {
                    createAndShowUI();
                    initializeGrid();
                    addListeners();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public GameGraphics2(Game game) {
        this.game = game;

    }

    private void createComponents() {
        contentPane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                paintGameBoard(g);
            }
        };
        contentPane.setBackground(new Color(199, 199, 199));
        contentPane.setLayout(null);
        contentPane.setBounds(0, 0, 350, 500);

        dashBoard = new JPanel();
        dashBoard.setLayout(null);
        dashBoard.setBackground(new Color(229, 229, 229));
        dashBoard.setBounds(500, 0, 150, 500);

        JLabel paperCountLabel = new JLabel();

        contentPane.add(dashBoard);

    }

    public void createAndShowUI() {

        setTitle("Rock, Paper, Scissor");
        setBackground(Color.WHITE);
        setBounds(100, 100, 500, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(null);
        setVisible(true);

        if (contentPane == null) {
            createComponents();
        }
        if (contentPane != null) {
            setContentPane(contentPane);
        } else {
            System.err.println("contentPane is null in createAndShowUI()");
        }
    }

    private void addListeners() {
        addMouseWheelListener(this);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                pointerInfo = MouseInfo.getPointerInfo();
                point = pointerInfo.getLocation();
                mouseX = point.x;
                mouseY = point.y;
                if (e.getKeyCode() == KeyEvent.VK_S) {
                    game.generateOne(Hand.SCISSOR);
                    repaint();
                }
                if (e.getKeyCode() == KeyEvent.VK_R) {
                    game.generateOne(Hand.ROCK);
                    repaint();
                }
                if (e.getKeyCode() == KeyEvent.VK_P) {
                    game.generateOne(Hand.PAPER);
                    repaint();
                }
            }
        });
    }


    public void paintComponents(Graphics g) {
        paintComponents(g);
        if (dbImage != null) {
            g.drawImage(dbImage, 0, 0, null);
        }
    }

    public void paintGameBoard(Graphics g) {
        paperCount = 0;
        scissorsCount = 0;
        rockCount = 0;
        paper = new ImageIcon("papier.png").getImage();
        rock = new ImageIcon("stein.png").getImage();
        scissors = new ImageIcon("schere.png").getImage();

        long start = System.currentTimeMillis();
        flyingObjects.parallelStream().forEach(element -> {
            switch (element.getHand()) {
                case PAPER: {
                    img = paper;
                    paperCount++;
                    break;
                }
                case SCISSOR: {
                    img = scissors;
                    scissorsCount++;
                    break;
                }
                case ROCK: {
                    img = rock;
                    rockCount++;
                    break;
                }
            }
            int diameter = (int) (element.r * 2);
            double x = element.getX() - diameter / 2;
            double y = element.getY() - diameter / 2;
            g.drawImage(img, (int) Math.round(x - element.r), (int) Math.round(y + element.r), diameter, diameter, null, this);

        });
        long end = System.currentTimeMillis();

        System.out.println((end - start) + "ms for calculation of image");
    }

    public void updateFlyingObj(java.util.List<FlyingObjects> flyingObjects) {
        this.flyingObjects = flyingObjects;
        if (tmpDbImage == null || tmpDbImage.getWidth(null) != getWidth() || tmpDbImage.getHeight(null) != getHeight()) {
            tmpDbImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
            dbImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        }

        Graphics dbg = tmpDbImage.getGraphics();

        dbg.setColor(Color.WHITE);
        dbg.fillRect(0, 0, tmpDbImage.getWidth(null), tmpDbImage.getHeight(null));
        paintGameBoard(dbg);
        dbImage.getGraphics().drawImage(tmpDbImage, 0, 0, null);
        repaint();

    }

    public int getWidth() {
        return 500;
    }

    public int getHeight() {
        return 500;
    }


    private void initializeGrid() {
        rows = (int) getHeight() / gridSize + 1;
        cols = (int) getWidth() / gridSize + 1;
        grid = new ArrayList[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = new ArrayList<>();
            }
        }
    }

    void addToGrid(FlyingObjects obj) {
        int row = (int) (obj.getY() / gridSize);
        int col = (int) (obj.getX() / gridSize);
        grid[row][col].add(obj);
        //System.out.println("++++");
    }

    void removeFromGrid(FlyingObjects obj) {
        int row = (int) (obj.getY() / gridSize);
        int col = (int) (obj.getX() / gridSize);
        grid[row][col].remove(obj);
        //System.out.println("----");
    }

    ArrayList<FlyingObjects> getNearbyObjects(FlyingObjects obj) {
        int row = (int) (obj.getY() / gridSize);
        int col = (int) (obj.getX() / gridSize);
        ArrayList<FlyingObjects> nearbyObjects = new ArrayList<>();
        for (int i = Math.max(0, row - 1); i <= Math.min(rows - 1, row + 1); i++) {
            for (int j = Math.max(0, col - 1); j <= Math.min(cols - 1, col + 1); j++) {
                nearbyObjects.addAll(grid[i][j]);
            }
        }
        return nearbyObjects;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (FlyingObjects element : flyingObjects) {
            removeFromGrid(element);
            element.move(getWidth(), getHeight());
            addToGrid(element);
        }
        repaint();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int rotation = e.getWheelRotation();
        if (rotation < 0) {
            speed += 0.1;
        } else {
            speed -= 0.1;
        }
        for (FlyingObjects element : flyingObjects) {
            element.setDefaultSpeed(speed);
        }
    }
}
