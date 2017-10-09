/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unoxtutti.domain;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Observable;
import javax.swing.DefaultListModel;
import javax.swing.ListModel;
import unoxtutti.connection.P2PConnection;
import unoxtutti.game.Card;

/**
 *
 * @author mateusz
 */
public class RemoteManche extends Observable {

    private DefaultListModel<Card> myhand;
    private DefaultListModel<String> otherPlayers;
    private Card top;
    private String eventoManche;
    private String playerturn;
    private int action;//conta il numero di azioni
    private String lastaction;
    private String lastmessage;
    private boolean ended=false;

    public RemoteManche() {
        System.out.println("creata");
    }

    public void addHand(DefaultListModel<Card> cards) {
        myhand = cards;
    }

    public ListModel<Card> getHand() {
        return myhand;
    }

    public void updatePlayersHand(DefaultListModel<String> cards) {
        otherPlayers = cards;

    }

    public void addCard(Card f, int actionN) {
        setAction(actionN);

        top = f;

    }
    public boolean isEnded() {
        return ended;
    }

    public void removeCard(DefaultListModel<Card> f) {
        myhand.clear();

        for (int i = 0; i < f.getSize(); i++) {
            myhand.addElement(f.elementAt(i));

        }
        System.out.println(myhand);

    }

    public void updatevalue(String playerturnparam, Card topc, int actionN, DefaultListModel cartegiocatori, DefaultListModel<Card> mycards) {
        setAction(actionN);
        otherPlayers.removeAllElements();
        for (int i = 0; i < cartegiocatori.getSize(); i++) {
            otherPlayers.addElement((String) cartegiocatori.getElementAt(i));
        }
        top = topc;
        playerturn = playerturnparam;
        myhand.clear();
        for (int i = 0; i < mycards.getSize(); i++) {
            myhand.addElement(mycards.elementAt(i));

        }
        System.out.println(myhand);

    }

    public void setAction(int a) {
        this.action = a;
    }

    public int getAction() {
        return action;
    }

    public void setTurn(String pl) {

        playerturn = pl;

    }

    public void notifypanel(String action) {
        lastaction = action;
        setChanged();//sono statto modificato
        notifyObservers();//scorro lista osservatori e dico che sono stato  modificato
    }

    public void writeMessage(String m) {
        lastmessage = m;

    }

    public String getMex() {
        return lastmessage;

    }

    public String getTop() {
        return top.toString();

    }

    public String getPlayerTurn() {
        return playerturn;

    }

    public Card getTopCard() {
        return top;

    }

    public String getLastAction() {
        return lastaction;

    }

    public ListModel<String> getPlayersHand() {

        return otherPlayers;
    }

    public int countCard() {

        return myhand.size();
    }
    public void setEnded(String msg) {
        lastmessage =msg;
        ended=true;
        
    }
}
