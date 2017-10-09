/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unoxtutti.domain;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.ListModel;

import unoxtutti.UnoXTutti;
import unoxtutti.connection.ClientConnectionException;
import unoxtutti.connection.CommunicationException;
import unoxtutti.connection.MessageReceiver;
import unoxtutti.connection.P2PConnection;
import unoxtutti.connection.P2PMessage;
import unoxtutti.connection.PartnerShutDownException;
import unoxtutti.domain.dialogue.DialogueHandler;
import unoxtutti.domain.dialogue.DialogueObserver;
import unoxtutti.game.Card;

/**
 * Rappresenta una stanza dal punto di vista del client (per questa ragione
 * "remote": perch&eacute; la stanza dal suo punto di vista si trova in remoto).
 * Questo oggetto riceve messaggi di tipo "roomUpdate" per essere notificato di
 * modifiche alla stanza. Inoltre partecipa nei dialoghi di tipo "RoomEntrance"
 * (di cui riceve le notifiche di stato) per gestire l'ingresso del giocatore
 * nella stanza remota da esso rappresentata.
 *
 * @author picardi
 */
public class RemoteRoom extends Room implements MessageReceiver, DialogueObserver {

    private Player myPlayer;
    private P2PConnection p2pConn;
    private DefaultListModel<Player> allPlayers;
    private RoomEntranceDialogueHandler entranceHandler;
    private DefaultListModel<String> allPartite;
    private RemotePartita currentmatch;
    private MatchCreationDialogueHandler matchCreationHandler;
    private MatchEnterDialogueHandler matchEntraceHandler;

    /**
     * Factory method per creare una RemoteRoom. Crea la connessione al server e
     * inizia il dialogo di tipo "RoomEntrance" necessario per completare
     * l'ingresso nella stanza. Registra l'oggetto come Observer dove necessario
     * (presso la connessione P2P per i messaggi "roomUpdate" e presso il
     * DialogueHandler per notifiche sull'evoluzione del dialogo).
     *
     * @param player Il giocatore che vorrebbe collegarsi alla stanza
     * @param roomName Il nome della stanza
     * @param roomAddr L'indirizzo della stanza
     * @param roomPort La porta a cui si trova la stanza
     * @return L'oggetto RemoteRoom corrispondente alla stanza richiesta
     * @throws ClientConnectionException Se la connessione non riesce
     */
    public static RemoteRoom createRemoteRoom(Player player, String roomName, String roomAddr, int roomPort)
            throws ClientConnectionException {
        InetAddress inetaddr;
        try {
            inetaddr = InetAddress.getByName(roomAddr);
            P2PConnection p2p = P2PConnection.connectToHost(player, inetaddr, roomPort);
            RemoteRoom r = new RemoteRoom(player, roomName, p2p);
            boolean ok = r.enter();
            if (ok) {
                return r;
            }
            return null;
        } catch (IOException ex) {
            throw new ClientConnectionException("Address " + roomAddr + " is incorrect.");
        }
    }

    private RemoteRoom(String name) {
        super(name);
        allPlayers = new DefaultListModel<>();
        allPartite = new DefaultListModel<>();
    }

    private RemoteRoom(Player pl, String name, P2PConnection p2p) {
        this(name);
        myPlayer = pl;
        p2pConn = p2p;
    }

    /**
     * @return fornisce un ListModel contenente i giocatori presenti nella
     * stanza, per poterli visualizzare in modo che restino aggiornati quando
     * l'elenco cambia.
     */
    public ListModel<Player> getPlayersAsList() {
        return allPlayers;
    }

    public ListModel<Player> getPlayersOfMatch() {
        if (currentmatch != null) {
            return currentmatch.getAllPlayerOfMatch();
        }
        return null;
    }

    public ListModel<String> getPartiteAsList() {
        return allPartite;
    }

    private boolean enter() {
        entranceHandler = new RoomEntranceDialogueHandler(p2pConn);
        p2pConn.addMessageReceivedObserver(this, Room.roomUpdateMsg);

        p2pConn.addMessageReceivedObserver(this, Room.roomAskPartitaMsg);
        entranceHandler.addStateChangeObserver(this);
        return entranceHandler.startDialogue(myPlayer, getName());
    }

    /**
     * @return il giocatore per cui questo oggetto &egrave; stato creato.
     */
    public Player getConnectedPlayer() {
        return myPlayer;
    }

    public void startMatchRoom(int code) {
        try {
            P2PMessage msg = new P2PMessage(Room.roomStartMatchMsg);
            Object[] params = {code};
            msg.setParameters(params);
            p2pConn.sendMessage(msg);

        } catch (PartnerShutDownException ex) {
            Logger.getLogger(RemoteRoom.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     *
     * @return la P2P connection con cui questa RemoteRoom si collega alla
     * corrispondente ServerRoom
     */
    public P2PConnection getConnection() {
        return p2pConn;
    }

    @Override
    public int getPlayerCount() {
        return allPlayers.getSize();
    }

    @Override
    public ArrayList<Player> getPlayers() {
        ArrayList<Player> players = new ArrayList<>();
        for (int i = 0; i < getPlayerCount(); i++) {
            players.add(allPlayers.getElementAt(i));
        }
        return players;
    }

    @Override
    public synchronized void updateMessageReceived(P2PMessage msg) {

        if (msg.getName().equals(Room.roomUpdateMsg)) {
            try {

                ArrayList<Player> players = (ArrayList<Player>) msg.getParameter(0);//ottengo i num gioc e partite da messaggio di update
                ArrayList<String> partite = (ArrayList<String>) msg.getParameter(1);//ottengo la lista delle partite della stanza
                System.out.println(partite.size() + "" + players.size());
                allPlayers.removeAllElements();
                allPartite.removeAllElements();
                for (Player p : players) {

                    allPlayers.addElement(p);
                }
                for (String m : partite) {

                    allPartite.addElement(m);
                }
            } catch (ClassCastException ex) {
                throw new CommunicationException("Wrong parameter type in message " + msg.getName());
            }
        }
        if (msg.getName().equals(Room.matchUpdateMsg)) {
            System.out.println("sono in partita " + myPlayer);
            if (inPartita()) {
                LinkedList<Player> players = (LinkedList<Player>) msg.getParameter(0);//ottengo i num gioc  delle state
                System.out.println("sono in partita " + myPlayer + "  " + players.size());
                p2pConn.addMessageReceivedObserver(this, Room.mancheUpdateMsg);
                currentmatch.removeAllElements();//elimina tutti i giocatori della partita per poi riempirli di nuovo
                for (Player p : players) {

                    currentmatch.addElement(p);
                }
            }

        }

        if (msg.getName().equals(Room.mancheUpdateMsg)) {
            String action = (String) msg.getParameter(0);

            if (action.equals("start")) {//crea la remote manche
                currentmatch.startManche();

            }
            if (action.equals("reciveCard")) {  //inizializzo i valori del pannello                   
                LinkedList<Card> cards = (LinkedList<Card>) msg.getParameter(1);
                DefaultListModel<String> cartegiocatori = (DefaultListModel<String>) msg.getParameter(2);
                Card first = (Card) msg.getParameter(3);
                String nameplayert = (String) msg.getParameter(4);
                int actionmanche = (int) msg.getParameter(5);//zero perchè nessuna azione all'inizio
                DefaultListModel<Card> cardslistmodel = new DefaultListModel();//per passare una default list model la linkded list è più facile da mofidicare
                for (Card element : cards) {
                    cardslistmodel.addElement(element);
                }

                currentmatch.reciveCards(cardslistmodel, cartegiocatori, first, nameplayert, actionmanche);

            }//update",getPlayerTurno().getName(),tavolo.getTop(),action
            if (action.equals("update")) {
                String playerturn = (String) msg.getParameter(1);
                Card topcard = (Card) msg.getParameter(2);
                int newact = (int) msg.getParameter(3);
                DefaultListModel<String> cartegiocatori = (DefaultListModel<String>) msg.getParameter(4);
                DefaultListModel<Card> cardsupdate = (DefaultListModel<Card>) msg.getParameter(5);
                System.out.println("lista" + cardsupdate);
                DefaultListModel<Card> cardslistupdate = new DefaultListModel();//per passare una default list model la linkded list è più facile da mofidicare
                String message = "";
                if (msg.getParameter(6) != null) {
                    message = (String) msg.getParameter(6);
                }
                currentmatch.writeMessage(message);
                currentmatch.updatevalue(playerturn, topcard, newact, cartegiocatori, cardsupdate);

            }
            if (action.equals("scarta")) {

                LinkedList<Card> cardremove = (LinkedList<Card>) msg.getParameter(1);
                System.out.println("rooooom" + cardremove);
                DefaultListModel<Card> mycardslistmodel = new DefaultListModel();
                for (Card element : cardremove) {
                    mycardslistmodel.addElement(element);
                }

                currentmatch.removeCard(mycardslistmodel);

            }
            if (action.equals("bluffcheck")) {
                String playerturn = (String) msg.getParameter(1);
                Card topcard = (Card) msg.getParameter(2);
                int newact = (int) msg.getParameter(3);
                DefaultListModel<String> cartegiocatori = (DefaultListModel<String>) msg.getParameter(4);
                DefaultListModel<Card> cardsupdate = (DefaultListModel<Card>) msg.getParameter(5);
                System.out.println("lista" + cardsupdate);
                
                String message = "";

                currentmatch.updatevaluebluff(playerturn, topcard, newact, cartegiocatori, cardsupdate);

            }
             if (action.equals("winner")) {
                String playerturn = (String) msg.getParameter(1);
                Card topcard = (Card) msg.getParameter(2);
                int newact = (int) msg.getParameter(3);
                DefaultListModel<String> cartegiocatori = (DefaultListModel<String>) msg.getParameter(4);
                DefaultListModel<Card> cardsupdate = (DefaultListModel<Card>) msg.getParameter(5);
                System.out.println("lista" + cardsupdate);
                DefaultListModel<Card> cardslistupdate = new DefaultListModel();//per passare una default list model la linkded list è più facile da mofidicare
                String winner=(String) msg.getParameter(6);
                String message = "Il vincitore è:"+winner;
                
                currentmatch.updatevaluewin(playerturn, topcard, newact, cartegiocatori, cardsupdate,message);

            }

        }
        if (msg.getName().equals(Room.roomAskPartitaMsg)) {
            try {
                System.out.println("sonoqui");
                String nomepartita = (String) msg.getParameter(0);
                String nomegiocatore = (String) msg.getParameter(1);
                String message = "Vuoi accettare il giocatore " + nomegiocatore
                        + " nella partita " + nomepartita + " ?";
                 
                int reply =UnoXTutti.theUxtController.optionPanel(nomegiocatore,nomepartita);
                P2PMessage replymsg = new P2PMessage(Room.roomAskPartitaReplMsg);

                Object[] params = new Object[3];
                params[0] = false;
                if (reply == JOptionPane.YES_OPTION) {
                    params[0] = true;
                }

                params[1] = msg.getParameter(2);//codice del richiedente
                params[2] = msg.getParameter(3);//codice partita
                replymsg.setParameters(params);
                p2pConn.sendMessage(replymsg);
            } catch (PartnerShutDownException ex) {
                Logger.getLogger(RemoteRoom.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

       // UnoXTutti.theUxtController.roomUpdated();
    }

    public void createPartita(String nomepartita, int ngioc, String modalita) {
        matchCreationHandler = new MatchCreationDialogueHandler(p2pConn);
        matchCreationHandler.addStateChangeObserver(this);

        System.out.println(matchCreationHandler.startDialogue(nomepartita, ngioc, modalita, this.getConnectedPlayer().getName(), this.getConnectedPlayer().hashCode()));

        /* P2PMessage msgToSnd = new P2PMessage(Room.roomCreatePartitaMsg);
        Object[] params = new Object[4];
        params[0] = nomepartita;
        params[1] = ngioc;
        params[2] = modalita;
        params[3] = this.getConnectedPlayer().getName();
        msgToSnd.setParameters(params);

        try {
            this.p2pConn.sendMessage(msgToSnd);
        } catch (PartnerShutDownException ex) {
            Logger.getLogger(RemoteRoom.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }

    @Override
    public synchronized void updateDialogueStateChanged(DialogueHandler source) {
        if (source.equals(entranceHandler)) {
            RoomEntranceDialogueState state = entranceHandler.getState();
            switch (state) {
                case ADMITTED:
                    for (Player p : entranceHandler.getRemoteRoomPlayers()) {
                        allPlayers.addElement(p);
                    }
                    entranceHandler.concludeDialogue();
                    UnoXTutti.theUxtController.roomEntranceCompleted(this);
                    break;
                case REJECTED:
                    entranceHandler.concludeDialogue();
                    UnoXTutti.theUxtController.roomEntranceFailed(this);
                    break;
                default:
            }
        }
        if (source.equals(matchCreationHandler)) {

            matchCreationHandler.concludeDialogue();
        }
        if (source.equals(matchEntraceHandler)) {
            RoomEntranceDialogueState state = entranceHandler.getState();
            switch (state) {
                case ADMITTED:
                    Object[] valori = matchEntraceHandler.getInfo();
                    if (valori != null) {
                        currentmatch = new RemotePartita((String) valori[0], (String) valori[1], (String) valori[2], (int) valori[3], (int) valori[4]);//aggiungere parametri chiamremetodo di remote partita
                        System.out.println(currentmatch.getCode());
                        UnoXTutti.theUxtController.PartitaEntranceCompleted();
                    } else {
                        UnoXTutti.theUxtController.PartitaEntranceFailed();
                    }
                    matchEntraceHandler.concludeDialogue();
                case REJECTED:
                    UnoXTutti.theUxtController.PartitaEntranceFailed();
                    matchEntraceHandler.concludeDialogue();

            }

        }
    }

    /**
     * Attua l'uscita del giocatore dalla stanza, notificando il server e
     * chiudendo quindi la connessione.
     */
    public void exit() {
        P2PMessage exitMsg = new P2PMessage(Room.roomExitMsg);
        try {
            this.p2pConn.sendMessage(exitMsg);
        } catch (PartnerShutDownException ex) {
            // Non fa nulla
            // Tanto si stava chiudendo in ogni caso
        }
        this.p2pConn.disconnect();
    }

    public void entraPartita(int codicepartita) {
        System.out.println("a");
        matchEntraceHandler = new MatchEnterDialogueHandler(p2pConn);
        matchEntraceHandler.addStateChangeObserver(this);
        p2pConn.addMessageReceivedObserver(this, Room.matchUpdateMsg);
        System.out.println(matchEntraceHandler.startDialogue(codicepartita, myPlayer.hashCode()));

    }

    public boolean inPartita() {
        if (currentmatch == null) {
            return false;
        } else {
            return true;
        }
    }

    public RemotePartita getPartita() {
        return currentmatch;

    }

    public void discardRoom(Card c, String Color) {

        P2PMessage msg = new P2PMessage(Room.mancheActionMsg);
        //{azione,nome giocatore che richied azione,id azione,codice match identificativo,carta,colore selzionato solo se è jolly}
        Object[] params = {"scarta", myPlayer.getName(), currentmatch.getManche().getAction(), currentmatch.getCode(), c, Color};
        msg.setParameters(params);
        try {
            p2pConn.sendMessage(msg);
        } catch (PartnerShutDownException ex) {
            Logger.getLogger(RemoteRoom.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
   
    public void unoRoom(Card c, String Color) {

        P2PMessage msg = new P2PMessage(Room.mancheActionMsg);
        //{azione,nome giocatore che richied azione,id azione,codice match identificativo,carta,colore selzionato solo se è jolly}
        Object[] params = {"uno", myPlayer.getName(), currentmatch.getManche().getAction(), currentmatch.getCode(), c, Color};
        msg.setParameters(params);
        try {
            p2pConn.sendMessage(msg);
        } catch (PartnerShutDownException ex) {
            Logger.getLogger(RemoteRoom.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void drawCardRoom() {

        P2PMessage msg = new P2PMessage(Room.mancheActionMsg);
        //come lo scarta ma senza la carta e il colore carta 
        Object[] params = {"draw", myPlayer.getName(), currentmatch.getManche().getAction(), currentmatch.getCode()};
        msg.setParameters(params);
        try {
            p2pConn.sendMessage(msg);
        } catch (PartnerShutDownException ex) {
            Logger.getLogger(RemoteRoom.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void passRoom() {

        P2PMessage msg = new P2PMessage(Room.mancheActionMsg);
        //come lo scarta ma senza la carta e il colore carta 
        Object[] params = {"pass", myPlayer.getName(), currentmatch.getManche().getAction(), currentmatch.getCode()};
        msg.setParameters(params);
        try {
            p2pConn.sendMessage(msg);
        } catch (PartnerShutDownException ex) {
            Logger.getLogger(RemoteRoom.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void interruptRoom(Card c, String Color) {

        P2PMessage msg = new P2PMessage(Room.mancheActionMsg);
        //{azione,nome giocatore che richied azione,id azione,codice match identificativo,carta,colore selzionato solo se è jolly}
        Object[] params = {"interrupt", myPlayer.getName(), currentmatch.getManche().getAction(), currentmatch.getCode(), c, Color};
        msg.setParameters(params);
        try {
            p2pConn.sendMessage(msg);
        } catch (PartnerShutDownException ex) {
            Logger.getLogger(RemoteRoom.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void notSayUnoRoom() {

        P2PMessage msg = new P2PMessage(Room.mancheActionMsg);
        //come lo scarta ma senza la carta e il colore carta 
        Object[] params = {"notuno", myPlayer.getName(), currentmatch.getManche().getAction(), currentmatch.getCode()};
        msg.setParameters(params);
        try {
            p2pConn.sendMessage(msg);
        } catch (PartnerShutDownException ex) {
            Logger.getLogger(RemoteRoom.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    public void updateRoom() {

        P2PMessage msg = new P2PMessage(Room.mancheActionMsg);
        Object[] params = {"update", myPlayer.getName()};
        msg.setParameters(params);
        try {
            p2pConn.sendMessage(msg);
        } catch (PartnerShutDownException ex) {
            Logger.getLogger(RemoteRoom.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    public void bluffRoom(boolean b) {
        boolean valueb = b; //se chiama o non chiama il bluff
        P2PMessage msg = new P2PMessage(Room.mancheActionMsg);
        //come lo scarta ma senza la carta e il colore carta 
        Object[] params = {"bluff", myPlayer.getName(), currentmatch.getManche().getAction(), currentmatch.getCode(), valueb};
        msg.setParameters(params);
        try {
            p2pConn.sendMessage(msg);
        } catch (PartnerShutDownException ex) {
            Logger.getLogger(RemoteRoom.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
