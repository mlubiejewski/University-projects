/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unoxtutti.domain;

import java.util.LinkedList;
import java.util.Observable;
import javax.swing.DefaultListModel;
import javax.swing.ListModel;
import unoxtutti.connection.MessageReceiver;
import unoxtutti.game.Card;

/**
 *
 * @author joana
 */
public class RemotePartita extends Observable {

    private String nomepartita;
    private String proprietario;
    private String modalita;
    private int codice;
    private int codiceproprietario;
    private boolean closed;
    private boolean inmanche;
    private RemoteManche currentManche;
    
    private String evento;
    private DefaultListModel<Player> allPlayers;

    public RemotePartita(String nomepartita, String prop, String mod, int cod, int codprop) {
        closed = false;
        allPlayers = new DefaultListModel<>();
        this.nomepartita = nomepartita;
        this.proprietario = prop;
        this.modalita = mod;
        this.codice = cod;
        this.codiceproprietario=codprop;
        inmanche=false;
        evento="";

    }
    

    public boolean getClosed() {
        return this.closed;
    }

    public String getNomePartita() {
        return this.nomepartita;
    }
    public int getCodiceProprietario() {
        return this.codiceproprietario;
    }

    public String getModalita() {
        return this.modalita;
    }

    public int getCode() {
        return this.codice;
    }

    public ListModel<Player> getAllPlayerOfMatch() {
        return allPlayers;
    }

    public void addElement(Player p) {
        allPlayers.addElement(p);
    }
    public void startManche(){
       
    inmanche=true;   
    evento="iniziomatch";
    currentManche=new RemoteManche();
    setChanged();
    notifyObservers();
   
  
    
    }
    
    public void reciveCards(DefaultListModel<Card> cards,DefaultListModel<String> cardseachplayers,Card first,String playerturno,int action){
        
      currentManche.addHand(cards);
      currentManche.addCard(first, action);
      currentManche.updatePlayersHand(cardseachplayers);
      currentManche.setTurn(playerturno);
      currentManche.notifypanel("first");
       
    
    }
    public void updatevalue(String playerturnparam,Card topc,int actionN,DefaultListModel<String> cartegiocatori,DefaultListModel<Card> mycards)  {
        currentManche.updatevalue(playerturnparam,topc,actionN,cartegiocatori,mycards);
        currentManche.notifypanel("update");
    
    
    }
    public void updatevaluebluff(String playerturnparam,Card topc,int actionN,DefaultListModel<String> cartegiocatori,DefaultListModel<Card> mycards)  {
        currentManche.updatevalue(playerturnparam,topc,actionN,cartegiocatori,mycards);
        currentManche.notifypanel("bluff");
    
    
    }
    
     public void updatevaluewin(String playerturnparam,Card topc,int actionN,DefaultListModel<String> cartegiocatori,DefaultListModel<Card> mycards,String msg)  {
        currentManche.updatevalue(playerturnparam,topc,actionN,cartegiocatori,mycards);
        currentManche.setEnded(msg);
        currentManche.notifypanel("winner");
    
    
    }
    public void removeCard(DefaultListModel<Card>  cardremove){
    currentManche.removeCard(cardremove);
    
    }
    
    public void writeMessage(String m){
    currentManche.writeMessage(m);
    
    }
   
    public RemoteManche getManche(){
    return  currentManche; }

    public void removeAllElements() {
        allPlayers.removeAllElements();
    }
    public String getEvento(){
    return evento;}

}
