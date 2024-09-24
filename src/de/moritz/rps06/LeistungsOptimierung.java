package de.moritz.rps06;

import java.util.ArrayList;
import java.util.Arrays;

public class LeistungsOptimierung {

    private GameGraphics ui;

//    public class Grid {
//        int zeilen;
//        int spalten;
//        double vierEckGroesse;
//        private ArrayList<>[][] grid;
//
//        public Grid(int uiWidth, int uiHeight, int vierEckGroesse){
//            this.vierEckGroesse = vierEckGroesse;
//            zeilen  = uiHeight / vierEckGroesse;
//            spalten = uiWidth / vierEckGroesse;
//            this.grid = (ArrayList[][]) new ArrayList[zeilen][spalten];
//        }
//    }

    public static void main(String[] args) {

        String[][] carsArray = {{"Mercedes", "BMW", "Audi"},
                {"Volvo", "VW", "Lambo"},
                {"Bentley", "Alfa Romeo", "Ferrari"}
        };
        ArrayList<String> autoMarkenList = new ArrayList<>(Arrays.asList("Mercedes", "BMW", "Audi",
                "Volvo", "VW", "Lambo", "Bentley", "Alfa Romeo", "Ferrari"));
        for (int i = 0; i < carsArray.length; i++) {
            System.out.println();
            for (int j = 0; j < carsArray[i].length; j++) {
                System.out.print(carsArray[i][j] + " ");

            }
        }
        System.out.println("\n" + carsArray);
        System.out.println(autoMarkenList);
    }
}
