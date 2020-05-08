package edu.pja.kasia;


import java.util.ArrayList;

class Card {
    String f;
    int id;

    Card(){
        this.id = Statics.counter_id++;
        this.f = "bin/images/img-"+(this.id+1)+".jpg";
    }


    static ArrayList<Card> createCards(int amount){
        Statics.counter_id = 0;
        ArrayList<Card> cards = new ArrayList<>(amount);
        for(int i=0; i<amount; i++){
            cards.add(i, new Card());
        }
        return cards;
    }

    @Override
    public String toString(){
        return this.id+" "+this.f;
    }

}

