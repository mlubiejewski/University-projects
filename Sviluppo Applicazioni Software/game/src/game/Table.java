/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unoxtutti.game;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author joana
 */
public class Table {

    private final LinkedList<Card> card; //card del mazzo
    private final LinkedList<Card> dropped; //mazzo degli scarti
    private final static char[] colors = {'r', 'b', 'g', 'v'};

    //can be = 'c' for clockwise (by default), or ='a' for anticlockwise
    private char roundDirection;

    public Table() {

        roundDirection = 'c';
        card = new LinkedList();
        dropped = new LinkedList();

        //creo il mazzo
        //inserisco le card base
        for (int i = 0; i < 10; i++) {
            for (char c : colors) {
                card.push(new Card('b', c, i, 'n'));
            }
        }
        for (int i = 1; i < 10; i++) {
            for (char c : colors) {
                card.push(new Card('b', c, i, 'n'));
            }
        }
        //inserisco le card azione
        for (int i = 0; i < 2; i++) {
            for (char c : colors) {
                card.push(new Card('a', c, -1, 'p'));
                card.push(new Card('a', c, -1, 'c'));
                card.push(new Card('a', c, -1, 's'));
            }
        }
        //inserisco le card jolly
        for (int i = 0; i < 4; i++) {
            card.push(new Card('j', 'n', -1, 'j'));
            card.push(new Card('j', 'n', -1, '4'));

        }

        //mischio il mazzo
        Collections.shuffle(card);
    }

    public List<Card> pesca(final int n) {
        //se non ci sono abbastanza card nel mazzo rimetto dentro quelle dropped
        if (card.size() < n) {
            mix();
        }

        LinkedList<Card> l = new LinkedList();
        for (int i = 0; i < n; i++) {
            l.add(card.pop());
        }
        return Collections.unmodifiableList(l);
    }

    public void start() {
        dropped.push(card.pop());
        while (firstCard().getTipo() == 'j') {
            dropped.push(card.pop());
        }
    }

    public void scarta(final Card c) {

        dropped.push(c);
    }

    public LinkedList<Card> giveCards() {
        LinkedList<Card> l = new LinkedList();
        for (int i = 0; i < 7; i++) {
            l.add(card.pop());
        }
        return l;
    }

    @Override
    public String toString() {
        return "Tavolo{" + "card=" + card + ", dropped=" + dropped + '}';
    }

    public Card firstCard() {
        return dropped.getFirst();
    }

    public Card secondCard() {
        if (dropped.size() < 2) {
            return null;
        } else {
            return dropped.get(1);
        }
    }
       public Card giveCard() {
           return card.pop();
    }
    public void cambiaGiro() {
        if (roundDirection == 'c') {
            roundDirection = 'a';
        } else {
            roundDirection = 'c';
        }
    }

    public char getRoundDirection() {
        return roundDirection;
    }

    public int getDeckSize() {
        return card.size();
    }

    public int getDiscardedCardsSize() {
        return dropped.size();
    }

    private void mix() {
        Card temp = dropped.pop();
        while (!dropped.isEmpty()) {
            Card c = dropped.pop();
            if (c.getTipo() == 'j') {
                c.setColor('n');
            }
            card.push(c);
        }
        //mischio il mazzo
        Collections.shuffle(card);
        //rimetto la carta in cima agli scarti
        dropped.push(temp);
    }

    public char getRandomColor() {
        Random rand = new Random();
        int randInt = rand.nextInt(4);
        return colors[randInt];

    }

}
