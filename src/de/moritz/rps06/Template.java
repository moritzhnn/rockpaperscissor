package de.moritz.rps06;

import javax.swing.*;
import java.awt.*;

public class Template extends JFrame{
    public static void main(String[] args) {
        Template template = new Template();

    }
    Template() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600,600);
        setVisible(true);
    }
    public void paint(Graphics g){
        g.setColor(Color.GRAY);
        g.drawOval(200, 100,100,200);
        g.fillOval(200, 100, 100,200);
        g.drawLine(100, 200,100, 500);
        g.drawRoundRect(400, 400,100,100,50,50);
    }
}
