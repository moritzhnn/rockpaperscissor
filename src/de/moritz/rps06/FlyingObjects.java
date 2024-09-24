package de.moritz.rps06;

import java.awt.event.KeyEvent;
import java.util.Random;


public class FlyingObjects {
    private static final Random RANDOM = new Random();
    double x; // Abgabe von X als Koordinate von dem Objekt
    double y;
    int r;
    Hand hand = Hand.values()[RANDOM.nextInt(3)];
    double defaultSpeed = 0.5;
    double movex = RANDOM.nextDouble(defaultSpeed) - defaultSpeed / 2; // Zufällige Zahl zwischen 0-2, welche auf die X Koordinate drauf gerechnet werden soll.
    double movey = RANDOM.nextDouble(defaultSpeed) - defaultSpeed / 2; // Zufällige Zahl zwischen 0-2, welche auf die Y Koordinate drauf gerechnet werden soll.

    public int getR() {
        return r;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getMovex() {
        return movex;
    }

    public double getMovey() {
        return movey;
    }

    public void setMovex(double movex) {
        this.movex = movex;
    }

    public void setMovey(double movey) {
        this.movey = movey;
    }

    public FlyingObjects(double x, double y, int r, Hand hand) {
        this.x = x; // Eingabe in Main.java als X Koordinate identifizieren!
        this.y = y; // Eingabe in Main.java als Y Koordinate identifizieren!
        this.r = r; // Eingabe in Main.java als r Radius identifizieren!
        this.hand = hand;
    }

    public Hand getHand() {
        return hand;
    }

    double distance(FlyingObjects otherFlyingObjects) {
        double a = (x - otherFlyingObjects.x); // Differenz der X Koordinanten
        double b = (y - otherFlyingObjects.y); // Differenz der Y Koordinaten
        double c = Math.sqrt(a * a + b * b); // Berechnung des Abstandes
        return c; //Distanz als Rückgabe
    }

    public boolean kollision(FlyingObjects otherFlyingObject) {

        double sum = r + otherFlyingObject.r; // Addieren von Radius
        if (Math.abs(otherFlyingObject.x - x) > sum || Math.abs(otherFlyingObject.y - y) > sum) {
            return false;
        }

        double distance = distance(otherFlyingObject); // Distance zwischen zwei Körpern berechnen
        return sum > distance; // Rückgabe r größer als die Distanz ist, treffen sich die beiden Körper, da die Ausenkanten des Kreises überschneiden.
    }

    public void resolveCollision(FlyingObjects other) {
        Hand winner = this.getHand().battle(other.getHand());
        switch (winner) {
            case PAPER:
                this.setHand(winner);
                other.setHand(winner);
                break;
            case ROCK:
                this.setHand(winner);
                other.setHand(winner);
                break;
            case SCISSOR:
                this.setHand(winner);
                other.setHand(winner);
                break;
            case EVEN:
                break;
        }

        // Reverse movement directions
        this.setMovex(-this.getMovex());
        this.setMovey(-this.getMovey());
        other.setMovex(-other.getMovex());
        other.setMovey(-other.getMovey());
    }

    void checkOverlap(FlyingObjects otherFlyingObject) {

    }
    public String toString() {
        return "FlyingObject{" +
                "x = " + x +
                ", y = " + y +
                ", r = " + r +
                ", hand = " + hand +
                '}';
        //return "(" + x + "," + y + ")" + r; // Ausgabe als String
    }

    public void move(int width, int height) {
        x = x + movex;
        y = y + movey;
        //boxBegrenzung(width, height);
        //System.out.println(x + " " + y); //Ausgabe der neuen Koordianten
    }


    void boxBegrenzung(int width, int height) {

        if (x - 2 * r < 0 || x > width) {
            movex = movex * -1;
        }
        if (y - 2 * r < 0 || y > height - 2 * r) { // + 0.2*r
            movey = movey * -1;
        }
    }

    public void setHand(Hand winner) {
        hand = winner;
    }

    public void setDefaultSpeed(double speed) {
        this.defaultSpeed = speed;
        this.movex = Math.signum(this.movex) * speed;
        this.movey = Math.signum(this.movey) * speed;
    }
}