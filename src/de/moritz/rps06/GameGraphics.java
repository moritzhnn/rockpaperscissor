package de.moritz.rps06;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
//import java.lang.classfile.MethodSignature;
import java.util.ArrayList;


public class GameGraphics extends JFrame implements ActionListener, MouseWheelListener {

    private JPanel contentPane;
    private MainPanel mainPanel;
    private RightPanel rightPanel;
    private JButton btnTest;
    private Image dbImage, tmpDbImage, paper, rock, scissors, img;
    private double speed;
    private Graphics dbg;
    private int mouseX, mouseY, rows, cols;
    private static final int gridSize = 50;
    private ArrayList<FlyingObjects>[][] grid;
    PointerInfo pointerInfo;
    Point point;
    private Game game;
    private java.util.List<FlyingObjects> flyingObjects = new ArrayList<>();
    JLabel lblPaperCount = new JLabel();
    JLabel lblRockCount = new JLabel();
    JLabel lblScissorsCount = new JLabel();
    JLabel lblCollisionCount = new JLabel();
    JLabel lblGameDuration = new JLabel();

    JLabel lblAnnounceGameLength = new JLabel();
    JLabel lblAnnounceWinner = new JLabel();
    JButton btnPlayAgain = new JButton();


    public int getMouseY() {
        return mouseY;
    }


    public int getHeightMainPanel() {
        return mainPanel.getSize().height;
    }


    public int getWidthMainPanel() {
        return mainPanel.getSize().width;
    }

    public void setSizeMainPanel(int a, int b) {
        mainPanel.setSize(a, b);
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


    public void start() {
        EventQueue.invokeLater(() -> {
            try {
                setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public GameGraphics(Game game) {
        this.game = game;
        initializeUI();
    }

    public void initializeUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 700, 500);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(249, 249, 249));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));
        setResizable(false);


        mainPanel = new MainPanel();
        mainPanel.setBounds(0, 0, 500, 500);
        mainPanel.setBackground(new Color(221, 221, 221));
        mainPanel.setLayout(null);
        mainPanel.setVisible(true);
        mainPanel.addComponentListener(new myListener());
        contentPane.add(mainPanel, BorderLayout.WEST);


        rightPanel = new RightPanel();
        rightPanel.setBackground(Color.GRAY);
        rightPanel.setPreferredSize(new Dimension(184, 500));
        rightPanel.setLayout(new MigLayout());
        rightPanel.add(lblPaperCount, "cell 0 0, width 50, height 30, split 5, flowy, top");
        rightPanel.add(lblRockCount, "cell 0 1, width 50, height 30");
        rightPanel.add(lblScissorsCount, "cell 0 2, width 50, height 30");
        rightPanel.add(lblCollisionCount, "cell 0 3, width 100, height 30");
        rightPanel.add(lblGameDuration, "cell 0 4, width 50, height 30");

        rightPanel.add(lblAnnounceWinner, "cell 0 5, width 100, height 30, gaptop 100");
        lblAnnounceWinner.setVisible(false);
        rightPanel.add(lblAnnounceGameLength, "cell 0 6, width 50, height 30");
        lblAnnounceGameLength.setVisible(false);
        rightPanel.add(btnPlayAgain, "cell 0 7, width 100, height 40, gaptop 50");
        btnPlayAgain.setText("Play again!");
        btnPlayAgain.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playAgain();
            }
        });
        btnPlayAgain.setVisible(true);
        contentPane.add(rightPanel, BorderLayout.EAST);

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
        initializedGrid();
        setFocusable(true);
    }

    public void updateCounts(int countPaper, int countRock, int countScissors, int countCollisions) {
        lblPaperCount.setText("Paper: " + String.valueOf(countPaper));
        lblRockCount.setText("Rock: " + String.valueOf(countRock));
        lblScissorsCount.setText("Scissors: " + String.valueOf(countScissors));
        lblGameDuration.setText("Duration: " + "d");
        if (countCollisions % game.numberOfFlyingObjects == 0) {                         // damit nicht zuviel aktualisiert wird und es flackert
            lblCollisionCount.setText("Collisions: " + String.valueOf(countCollisions)); //aus wegen spin spin bug -> collision = 100k +
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (dbImage != null) {
            g.drawImage(dbImage, 0, 0, this);
        }
    }


    public void updateFlyingObj(java.util.List<FlyingObjects> flyingObjects) {
        this.flyingObjects = flyingObjects; // falscher fuffi ändert die werte von mainPanel
        //System.out.println("Anzahl der Flugobjekte: " + flyingObjects.size());

        mainPanel.setFlyingObjects(flyingObjects);
        mainPanel.repaint();
        updateCounts(game.paperCount, game.rockCount, game.scissorsCount, game.collisionCount);

    }

    public void initializedGrid() {        //test
        rows = 500 / (gridSize);
        cols = 500 / (gridSize);
        grid = new ArrayList[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = new ArrayList<>();
            }
        }
    }

    public void updateGrid() {
        // Entfernen Sie alle Objekte aus dem Gitter
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j].clear();
            }
        }

        // Fügen Sie alle Objekte erneut hinzu
        for (FlyingObjects obj : flyingObjects) {
            addToGrid(obj);
        }
    }


    public void addToGrid(FlyingObjects obj) {
        int row = (int) (obj.getY() / gridSize);
        int col = (int) (obj.getX() / gridSize);
        if (row >= 0 && row < rows && col >= 0 && col < cols) { // Sicherstellen, dass der Index innerhalb der Grenzen liegt
            grid[row][col].add(obj);
        }
    }

    void removeFromGrid(FlyingObjects obj) {
        int row = (int) (obj.getY() / gridSize);
        int col = (int) (obj.getX() / gridSize);
        mainPanel.setSize(500, 500);
        if (row >= 0 && row < rows && col >= 0 && col < cols) { // Sicherstellen, dass der Index innerhalb der Grenzen liegt
            grid[row][col].remove(obj);
        }
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
        updateGrid();
        repaint();
        mainPanel.repaint();
        rightPanel.repaint();
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


    class MainPanel extends JPanel {
        Image paper = new ImageIcon("papier.png").getImage();
        Image rock = new ImageIcon("stein.png").getImage();
        Image scissors = new ImageIcon("schere.png").getImage();

        private java.util.List<FlyingObjects> flyingObjects;

        public MainPanel() {
            flyingObjects = new ArrayList<>();
        }

        public void setFlyingObjects(java.util.List<FlyingObjects> flyingObjects) {
            this.flyingObjects = flyingObjects;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (FlyingObjects obj : flyingObjects) {
                switch (obj.getHand()) {
                    case PAPER:
                        img = paper;
                        break;
                    case SCISSOR:
                        img = scissors;
                        break;
                    case ROCK:
                        img = rock;
                        break;
                    default:
                        break;
                }
                int r = game.radius;
                int diameter = (int) (r * 2);
                double x = obj.getX() - diameter / 2;
                double y = obj.getY() - diameter / 2;
                g.drawImage(img, (int) Math.round(x - r), (int) Math.round(y - r), diameter, diameter, null);
                g.drawOval((int) x, (int) y, 1, 1);
                g.drawOval((int) Math.round(x - r), (int) Math.round(y - r), diameter, diameter);
            }
        }
    }

    class RightPanel extends JPanel {
        private Image image;

        public void setImage(Image image) {
            this.image = image;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (image != null) {
                int width = getWidth();
                g.drawImage(image, 0, 0, width, getHeight(), image.getWidth(null) - width,
                        0, image.getWidth(null), image.getHeight(null), this);
            }
        }
    }

    public void checkForEnd() {
        if (game.paperCount == game.numberOfFlyingObjects || game.rockCount == game.numberOfFlyingObjects ||
                game.scissorsCount == game.numberOfFlyingObjects) {

            lblAnnounceGameLength.setVisible(true);
            lblAnnounceGameLength.setText("Game Length: ");
            lblAnnounceWinner.setVisible(true);
            lblAnnounceWinner.setText("Winner: " + game.getHand());
            btnPlayAgain.setVisible(true);


        }
    }

    public void playAgain() {              //nicht besser in Game ?
        System.out.println(mainPanel.getSize());
        System.out.println(rightPanel.getSize());
    }

    class myListener implements ComponentListener {

        @Override
        public void componentResized(ComponentEvent e) {
        }

        @Override
        public void componentMoved(ComponentEvent e) {

        }

        @Override
        public void componentShown(ComponentEvent e) {

        }

        @Override
        public void componentHidden(ComponentEvent e) {

        }
    }

}
