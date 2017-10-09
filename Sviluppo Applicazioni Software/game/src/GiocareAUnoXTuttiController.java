/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unoxtutti;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.ListModel;
import static unoxtutti.UnoXTutti.mainWindow;
import unoxtutti.domain.Player;
import unoxtutti.domain.RemoteRoom;
import unoxtutti.connection.ServerCreationException;
import unoxtutti.domain.ServerRoom;
import unoxtutti.game.Card;
import unoxtutti.gui.MainWindowSubPanel;
import unoxtutti.gui.UnoXTuttiGUI;

/**
 * Controller GRASP per l'UC "GiocareAUnoXTutti". E' un singleton, quindi
 * l'unica istanza di questa classe viene ottenuta tramite il metodo statico
 * getInstance * @author picardi
 */
public class GiocareAUnoXTuttiController {

    private static GiocareAUnoXTuttiController singleInstance;

    private final HashMap<String, ServerRoom> serverRooms;
    private RemoteRoom currentRoom;
    private RemoteRoom roomInLimbo;
    private final Object entranceWaiting;
    private final Player player;
    private boolean inmatch;
    private boolean goManche=false;
    private MainWindowSubPanel panel;
   

    // GUI utility objects
    // nomi stanze ordinati alfabeticamente
    private final DefaultListModel<String> serverRoomNames;

    private GiocareAUnoXTuttiController(Player pl) {
        player = pl;
        serverRooms = new HashMap<>();
        serverRoomNames = new DefaultListModel<>();
        entranceWaiting = new Object();
        //sem=false;//per sincronizzare
    }

    /**
     *
     * @return il giocatore che sta interagendo con l'applicazione. Tale
     * giocatroe deve essere definito perch&eacute; il caso d'uso possa iniziare
     * e il controller abbia dunque senso di esistere.
     */
    public Player getPlayer() {
        return player;
    }
    public void setPanel(MainWindowSubPanel p) {        
        panel = p;        
    }

    /**
     * Crea un'istanza del controller GRASP dedicata al giocatore rappresentato
     * da Player. Corrisponde alla precondizione dell'UC che il giocatore sia
     * autenticato (se non lo fosse, non esisterebbe alcun Player).
     *
     * @param aPlayer il giocatore che sta interagendo con l'applicazione.
     */
    public static GiocareAUnoXTuttiController getInstance(Player aPlayer) {
        if (singleInstance == null || singleInstance.player != aPlayer) {
            singleInstance = new GiocareAUnoXTuttiController(aPlayer);
        }
        return singleInstance;
    }

    /**
     * Operazione utente definita nei contratti
     *
     * @param roomName il nome della stanza (server) da aprire
     * @param port la porta su cui aprire il server
     * @return un oggetto ServerRoom che rappresenta il server creato
     * @throws ServerCreationException
     */
    public ServerRoom apriStanza(String roomName, int port) throws ServerCreationException {
        ServerRoom room = ServerRoom.createServerRoom(player, roomName, port);
        if (room != null) {//if aggiunto per il checkport
            addServerRoom(room);
        }
        return room;
    }
    public boolean go(){
    return goManche;}
    public void setGo(boolean go){
        goManche=go;
    }

    public void apriPartita(String nomepartita, int ngioc, String modalita) throws ServerCreationException {

        if (currentRoom != null) {
            currentRoom.createPartita(nomepartita, ngioc, modalita);
        }

    }
    public void entrainPartita(int codicepartita) throws ServerCreationException {

        if (currentRoom != null) {
            currentRoom.entraPartita(codicepartita);
        }

    }
    
 
    private void addServerRoom(ServerRoom room) {
        serverRooms.put(room.getName(), room);
        ArrayList<String> names = new ArrayList<>();
        names.addAll(serverRooms.keySet());
        Collections.sort(names);
        serverRoomNames.clear();
        for (String n : names) {
            serverRoomNames.addElement(n);
        }
    }

    private void removeServerRoom(ServerRoom room) {
        serverRooms.remove(room.getName());
        serverRoomNames.removeElement(room.getName());
    }

    /**
     * Operazione utente definita nei contratti
     *
     * @param aRoom la stanza (server) da chiudere
     * @return true se la chiusura e' andata a buon fine, false altrimenti.
     */
    public boolean chiudiStanza(ServerRoom aRoom) {
        aRoom.shouldClose();
        try {
            aRoom.waitOnClose(3000);
        } catch (InterruptedException ex) {

        } finally {
            if (!aRoom.isClosed()) {
                aRoom.forceClose();
            }
        }
        removeServerRoom(aRoom);
        return true;
    }
  

    /**
     *
     * @return fornisce un ListModel contenente i nomi delle stanze esistenti,
     * per poterli visualizzare in modo che restino aggiornati quando l'elenco
     * cambia.
     */
    public ListModel<String> getServerRoomNames() {
        return serverRoomNames;
    }
    public void startMatch(int code){
    currentRoom.startMatchRoom(code);
    
    }

    /**
     * Fornisce la stanza con un certo nome (se esiste).
     *
     * @param rname Il nome della stanza desiderata
     * @return La stanza desiderata, null se il nome non corrisponde ad alcuna
     * stanza.
     */
    public ServerRoom getRoom(String rname) {
        return serverRooms.get(rname);
    }
    

    /**
     * Operazione utente definita nei contratti. Il thread viene sospeso
     * sinch&eacute; l'ingresso nella stanza non &egrave; avvenuto.
     *
     * @param roomName Il nome della stanza in cui entrare
     * @param roomAddr L'indirizzo della stanza in cui entrare
     * @param roomPort La porta della stanza in cui entrare
     */
    public void entraInStanza(String roomName, String roomAddr, int roomPort) {
        synchronized (entranceWaiting) {
            roomInLimbo = RemoteRoom.createRemoteRoom(this.player, roomName, roomAddr, roomPort);
            
            if (roomInLimbo != null) {
                try {
                    entranceWaiting.wait();
                } catch (InterruptedException ex) {
                    roomInLimbo = null;
                }
            }
            if (roomInLimbo != null) {
                currentRoom = roomInLimbo;
                roomInLimbo = null;
            } else {
                currentRoom = null;
            }
        }
    }
     public  void entraInPartita(int codice){
        synchronized (entranceWaiting) {
          if( !currentRoom.inPartita()){
                currentRoom.entraPartita(codice); 
              try {
                  entranceWaiting.wait();
              } catch (InterruptedException ex) {
                  Logger.getLogger(GiocareAUnoXTuttiController.class.getName()).log(Level.SEVERE, null, ex);
              }
                
            }
            inmatch=currentRoom.inPartita();
        }
    }
    /* public void entraInPartita(int codice) {
         synchronized (entranceWaiting) {
        
        
         }
    }*/
    /**
     *
     * @return la stanza in cui il giocatore si trova attualmente, se ne esiste
     * una. null altrimenti.
     */
    public RemoteRoom getCurrentRoom() {
        return currentRoom;
    }
 public void update() {
        currentRoom.updateRoom();
    }
    /**
     * Metodo invocato dalle classi del "model" quando il server comunica che
     * l'ingresso nella stanza &egrave, completato (si tratta di un evento
     * asincrono). Sveglia il thread che era stato messo in attesa durante
     * "entraInRoom".
     *
     * @param room La stanza in cui si e' entrati.
     */
    public void roomEntranceCompleted(RemoteRoom room) {
        synchronized (entranceWaiting) {
            roomInLimbo = room;
            entranceWaiting.notifyAll();
        }
    }

    /**
     * Metodo invocato dalle classi del "model" quando il server comunica che
     * l'ingresso nella stanza &egrave, fallito (si tratta di un evento
     * asincrono). Sveglia il thread che era stato messo in attesa durante
     * "entraInRoom".
     *
     * @param room La stanza in cui non si e' riusciti ad entrare.
     */
    public void roomEntranceFailed(RemoteRoom room) {
        synchronized (entranceWaiting) {
            roomInLimbo = null;
            entranceWaiting.notifyAll();
        }
    }
     public void PartitaEntranceCompleted() {
        synchronized (entranceWaiting) {
            
            entranceWaiting.notifyAll();
        }
    }

      public void PartitaEntranceFailed() {
        synchronized (entranceWaiting) {
            
            entranceWaiting.notifyAll();
        }
    }
    /**
     * Metodo invocato dalle classi del "model" quando il server comunica che le
     * informazioni relative alla stanza sono cambiate.
     */
  

    /**
     * Operazione utente definita nei contratti.
     */
    public void esciDalGioco() {
        if (currentRoom != null) {
            this.esciDaStanza();
        }
        ArrayList<ServerRoom> toclose = new ArrayList<>();
        toclose.addAll(serverRooms.values());
        for (ServerRoom r : toclose) {
            this.chiudiStanza(r);
        }
    }

    /**
     * Operazione utente definita nei contratti.
     */
    public void esciDaStanza() {
        if (currentRoom != null) {
            currentRoom.exit();
            currentRoom = null;
        }
    }
    public  int optionPanel(String giocatore,String partita){
      
       return panel.showMessageOption(giocatore,partita);
    
   
    }

    public boolean inStanza() {
        return (currentRoom != null);
    }

    public boolean inPartita() {
        return inmatch;
    }
    public void discard(Card c,String color){
        currentRoom.discardRoom(c,color);
    }
    public void uno(Card c,String color){
        currentRoom.unoRoom(c,color);
    }
    public void drawCard(){
        currentRoom.drawCardRoom();
    }
    public void pass(){
        currentRoom.passRoom();
    }
    public void interrupt(Card c,String color){
        currentRoom.interruptRoom(c,color);
    }
     public void notSayUno(){
        currentRoom.notSayUnoRoom();
    }
      public void bluff(boolean b){
        currentRoom.bluffRoom(b);
    }
}
