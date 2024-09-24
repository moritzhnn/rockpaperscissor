package de.moritz.rps06;

import com.badlogic.gdx.math.Vector2;


import javax.swing.*;
import javax.swing.Timer;
import java.util.List;
import java.util.Random;
import java.util.*;


public class Game {
    private static final Random RANDOM = new Random();
    public List<FlyingObjects> flyingObjects;
    private GameGraphics ui;
    int numberOfFlyingObjects = 10;
    int radius = 10;
    int paperCount = 0;
    int rockCount = 0;
    int scissorsCount = 0;
    int collisionCount = 0;

    public void setPaperCount(int paperCount) {
        this.paperCount = paperCount;
    }

    public void setRockCount(int rockCount) {
        this.rockCount = rockCount;
    }

    public void setScissorsCount(int scissorsCount) {
        this.scissorsCount = scissorsCount;
    }

    public Game() {
        flyingObjects = new ArrayList<>(numberOfFlyingObjects);
        ui = new GameGraphics(this);
    }

    public String getHand() {
        return flyingObjects.get(0).getHand().toString();
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.ui.start();
        game.generate();

        while (true) {
            game.ui.initializedGrid();
            game.nextIteration(game.ui.getWidthMainPanel(), game.ui.getHeightMainPanel());
            game.ui.updateFlyingObj(game.flyingObjects);
            System.out.println(game.ui.getWidthMainPanel() + " " + game.ui.getHeightMainPanel());
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private void nextIteration(int width, int height) {
        long start = System.currentTimeMillis();

        // Move objects and update grid
        for (FlyingObjects obj : flyingObjects) {
            ui.removeFromGrid(obj);
            obj.move(width, height);
            obj.boxBegrenzung(width, height);
            ui.addToGrid(obj);
        }

        // Check collisions
        for (FlyingObjects obj1 : flyingObjects) {
            for (FlyingObjects obj2 : ui.getNearbyObjects(obj1)) {
                if (obj1 != obj2 && obj1.kollision(obj2)) {
                    obj1.resolveCollision(obj2);
                    collisionCount++;
                }
            }
        }

        long end = System.currentTimeMillis();
        System.out.println((end - start) + "ms for calculation of collision");
    }


    void generate() {
        for (int j = 0; j < numberOfFlyingObjects; j++) {
            boolean positionValid;
            ui.updateCounts(paperCount, rockCount, scissorsCount, collisionCount);
            do {
                positionValid = true;
                Hand hand = Hand.values()[RANDOM.nextInt(3)];
                double x = RANDOM.nextDouble(50, ui.getWidthMainPanel() - 100 + 0.01);          // ui.getSize().height
                double y = RANDOM.nextDouble(50, ui.getHeightMainPanel() - 101 + 0.01);
                int r = radius;
                FlyingObjects newObject = new FlyingObjects(x, y, r, hand);
                //System.out.println(newObject);

                for (FlyingObjects element : flyingObjects) {
                    if (newObject.kollision(element)) {
                        positionValid = false;
                        break;
                    }
                }
                if (positionValid) {
                    flyingObjects.add(newObject);
                    switch (hand) {
                        case PAPER -> paperCount++;
                        case ROCK -> rockCount++;
                        case SCISSOR -> scissorsCount++;
                    }
                }
            } while (!positionValid);
        }
    }


    void generateOne(Hand hand) {
        boolean positionValid;
        do {
            positionValid = true;
            int r = radius;
            double y = ui.getMouseY() - 100 - r;
            double x = ui.getMouseX() - 100 + r;
            System.out.println(ui.getMouseX() + " " + ui.getMouseY());
            FlyingObjects newObject = new FlyingObjects(x, y, r, hand);
            System.out.println(newObject);

            for (FlyingObjects element : flyingObjects) {
                if (newObject.kollision(element)) {
                    positionValid = false;
                }
            }

            if (positionValid) {
                flyingObjects.add(newObject);
            }
        } while (!positionValid);
    }
}