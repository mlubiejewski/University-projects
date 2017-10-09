/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unoxtutti.domain;


import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;
import unoxtutti.connection.P2PConnection;

/**
 *
 * @author mateusz
 */
public class ServerGame implements Runnable {

    private final HashMap<Player, P2PConnection> connections;
    private LinkedList<ServerManche> manches;
    private ServerManche currentManche;
    private final String nomepartita;
    private final int ngiocmax;
    private final String modalita;
    private final String proprietario;
    private final int codice;
    private final int codiceproprietatrio;
    private ServerMancheHelper manche;
    private Thread t;
    
    //Flag di chiusura?
    boolean closure;

    public ServerGame(String npartita, int ngioc, String mod, String owner, int codiceprop) {
        connections = new HashMap<>();
        nomepartita = npartita;
        codiceproprietatrio = codiceprop;
        ngiocmax = ngioc;
        modalita = mod;
        proprietario = owner;
        closure = false;
        int hash = 7;
        hash = 61 * hash + Objects.hashCode(this.nomepartita + this.modalita + this.proprietario + this.modalita);
        hash = 61 * hash + (int) (new Date().getTime() / 1000);

        codice = hash;
        manches = new LinkedList<ServerManche> ();
        ///--------------------------------------------------------------------------SE LO FAMO AVVIARE DA COSTRUTTORE VA BENE COSI', VA COMUNQUE TENUTO T PER SINCRONIZZARE
        t = new Thread(this);
        t.start();
    }

    public int getCode() {
        return this.codice;

    }

    public int getProprietarioCode() {
        return this.codiceproprietatrio;

    }

    public String getNomePartita() {
        return this.nomepartita;

    }

    public String getModalita() {
        return this.modalita;

    }

    public String getProprietario() {
        return this.proprietario;

    }

    public void run() {
        while (!closure) //System.out.println("avviata da"+proprietario );
        {}
        //To imprelement: closure of the game
        
    }

    @Override
    public String toString() {//verra stampato dalla lista 
        return " Nome Partita: " + nomepartita + " max: " + ngiocmax + " modalita: " + modalita + " proprietario: " + proprietario + " codice " + codice;
    }

    public boolean addPlayer(Player p, P2PConnection playercon) {//verra stampato dalla lista 

        boolean result = false;
        if (connections.size() <= ngiocmax) {
            System.out.println("aggiunto" + p);
            connections.put(p, playercon);
            result = true;
        }
        return result;
    }

    protected void removePlayer(P2PConnection conn) {
        this.connections.remove(conn.getPlayer());
    }

    public LinkedList<Player> getPlayers() {
        LinkedList<Player> ret = new LinkedList<>();
        ret.addAll(connections.keySet());
        return ret;
    }

    public HashMap<Player, P2PConnection> getConnections() {
        return connections;
    }

    //chiamato per incominciare una nuova manche
    void playManche() {
       manche.start();
        
        
        
        
    }
    public void addManche(ServerManche m){
         currentManche=m;
    manches.add(m);
    }
    public ServerManche getManche(){
    return currentManche;
    }
    
    //chiamato da ServerManche per notificare che la manche è finita
    void fineManche() {
        manche = null;
    }
    
    //closure of the game
    void close() {
        closure = true;
    }
    
}
