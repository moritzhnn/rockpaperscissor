package de.moritz.rps06;

public class HandTest {
    public static void main(String[] args) {
        Hand[] values = Hand.values();
        for(Hand hand1 : values){
            for(Hand hand2: values){
                Hand winner = hand1.battle(hand2);
                if (winner == null){
                    System.out.println("Hand 1 hat " + hand1 + " gespielt! Sie haben unentschieden gespielt");
                }else{
                    System.out.println("Hand 1 hat " + hand1 + " und Hand 2 hat " + hand2 + " gespielt. Der Gewinner ist: " + winner);
                }
            }
        }
    }
}