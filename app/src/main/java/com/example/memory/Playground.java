package com.example.memory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class Playground {
    private Card[][] cards;
    private int whosOnTurn;
    private int[] score;
    private boolean isInitialized = false;


    public Playground(int x, int y){    // x = row / y = column
        cards = new Card[x][y];
        int whosOnTurn = 0;
        score = new int[3];
        if(!isInitialized)
            init();
    }

    public void init() {
        if (isInitialized)
            return;

        List<Card> card = new ArrayList<>();
        ArrayList<Integer> al = new ArrayList<Integer>();
        Random r = new Random();


        for (int i = 0; i < getNrPairs(); i++) {
            card.add(new Card());
        }
        int[] picsAr = MemoryActivity.getPicsArray();
        int randomZahl;


        for (int i = 0; i < (card.size()); i += 2) { //paerchen
            while (true) {
                randomZahl = r.nextInt(picsAr.length - 0);
                if (!al.contains(picsAr[randomZahl])) {
                    card.get(i).setValue(picsAr[randomZahl]);
                    card.get(i + 1).setValue(picsAr[randomZahl]);
                    al.add(picsAr[randomZahl]);
                    break;
                }
                }
                // System.out.println(randomZahl);
            }

            Collections.shuffle(card);

            for (Card elem : card) {
                // Log.d("MEMORY", elem.toString());
                System.out.println(elem.toString());
            }

            short temp = 0;
            for (int i = 0; i < cards.length; i++) {
                for (int j = 0; j < cards[0].length; j++) {
                    cards[i][j] = card.get(temp);
                    temp++;
                }
            }
            isInitialized = true;
        }


    private Card play(Position pos){
        return cards[pos.x][pos.y];
    }

    public boolean finished(){
        for (int i = 0; i<cards.length; i++){
            for (int j = 0; j<cards[i].length; j++) {
                if (!cards[i][j].isVisible()) {
                    return false;
                }
            }
        }
        return true;
    }

    public int getNrPairs(){
        return cards.length * cards[0].length;
    }
   public boolean isPair(Position posl, Position posl2){
       return(getCard(posl).getValue() == getCard(posl2).getValue());

    }

    public Card getCard(Position pos){
        return cards[pos.x][pos.y];
    }

    @Override
    public String toString() {
        return "Playground{" +
                "cards=" + Arrays.toString(cards) +
                ", whosOnTurn=" + whosOnTurn +
                ", score=" + Arrays.toString(score) +
                '}';
    }


}
