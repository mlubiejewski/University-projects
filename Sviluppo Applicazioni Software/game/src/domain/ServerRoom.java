/* To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unoxtutti.domain;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import unoxtutti.connection.MessageReceiver;
import unoxtutti.connection.P2PConnection;
import unoxtutti.connection.P2PMessage;
import unoxtutti.connection.PartnerShutDownException;
import unoxtutti.connection.ServerCreationException;
import unoxtutti.domain.Player;
import unoxtutti.domain.Room;
import unoxtutti.webserver.WebServerPrevious;
import unoxtutti.game.Card;

/**
 * La classe ServerRoom rappresenta una Room (Stanza) lato Server La Room lato
 * Server &egrave; il Server stesso.
 *
 * @author picardi
 */
public class ServerRoom extends Room implements Runnable, MessageReceiver {

    private final Player owner;
    private ServerSocket serverSock;
    private boolean shouldClose;
    private boolean closed;
    private final Object closeDownMonitor;
    private final HashMap<Player, P2PConnection> connections;
    private final ArrayList<P2PConnection> waitingClients;
    private ArrayList<ServerGame> listapartite;

    /**
     * Il costruttore &egrave; privato; una ServerRoom pu&ograve; essere creata
     * solo tramite il factory method
     * <em>createServerRoom</em>
     *
     * @param p Il giocatore che crea la stanza
     * @param roomName Il nome della stanza con cui gli altri giocatori potranno
     * connettersi
     */
    private ServerRoom(Player p, String roomName) {
        super(roomName);
        owner = p;
        closed = false;
        closeDownMonitor = new Object();
        connections = new HashMap<>();
        waitingClients = new ArrayList<>();
        listapartite = new ArrayList<>();
    }

    /**
     * Permette a un thread di aspettare per un certo tempo che il server si
     * chiuda.
     *
     * @param timeout Il tempo per cui aspettare.
     * @throws InterruptedException
     */
    public void waitOnClose(long timeout) throws InterruptedException {
        synchronized (closeDownMonitor) {
            if ((closeDownMonitor != null) && (!isClosed())) {
                closeDownMonitor.wait(timeout);
            }
        }
    }

    /**
     * Indica al Server che dovrebbe iniziare le procedure di chiusura
     */
    public void shouldClose() {
        boolean ok = false;
        synchronized (closeDownMonitor) {
            shouldClose = true;
            ok = isClosed();
        }
        // Sveglia se stesso dall'attesa di connessioni
        // stabilendo una P2PConnection con se stesso
        if (ok) {
            return;
        }

        P2PConnection conn = null;
        try {
            conn = P2PConnection.connectToHost(owner, this.getAddress(), this.getPort());
        } catch (IOException ex) {
            Logger.getLogger(ServerRoom.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void justStarted() {
        synchronized (closeDownMonitor) {
            shouldClose = false;
        }
    }

    private boolean shouldIClose() {
        synchronized (closeDownMonitor) {
            return shouldClose;
        }
    }

    private void setClosed(boolean b) {
        synchronized (closeDownMonitor) {
            boolean prev = isClosed();
            closed = b;
            if (!prev) {
                closeDownMonitor.notifyAll();
            }
        }
    }

    /**
     * Permette di verificare se il Server si &egrave; effettivamente chiuso.
     *
     * @return true se il Server &egrave; chiuso, false altrimenti.
     */
    public boolean isClosed() {
        boolean ret = false;
        synchronized (closeDownMonitor) {
            ret = closed;
        }
        return ret;
    }

    /**
     * Forza lo shut down del Server interrompendo tutte le connessioni ai
     * client.
     */
    public void forceClose() {
        synchronized (closeDownMonitor) {
            if (isClosed()) {
                return;
            }
        }
        for (P2PConnection p2p : connections.values()) {
            if (!p2p.isClosed()) {
                p2p.forceClose();
            }
        }
        setClosed(true);
    }

    /**
     * Crea una nuova ServerRoom e fa partire il Server.
     *
     * @param p il Giocatore che crea la Room
     * @param roomName il nome della Room
     * @param port la porta su cui aprire la Room
     * @return la ServerRoom appena creata
     * @throws ServerCreationException se non riesce ad ottenere l'indirizzo di
     * "localhost"
     */
    public static ServerRoom createServerRoom(Player p, String roomName, int port) throws ServerCreationException {
        if (checkPort(port)) {//controllo che la porta non si occupata
            ServerRoom room = new ServerRoom(p, roomName);
            InetAddress localhost = null;
            try {
                localhost = InetAddress.getLocalHost();
            } catch (UnknownHostException exc) {
                throw new ServerCreationException("Cannot find localhost. Server creation impossible.");
            }
            room.setAddress(localhost);
            room.setPort(port);
            (new Thread(room)).start();
            return room;
        }
        return null;
    }

    public static boolean checkPort(int port) {//apro una socket che poi chiuderò subito dopo se c'è un errore la porta probabilmente è occupata
        boolean result = true;
        ServerSocket temp = null;
        try {
            temp = new ServerSocket(port);
        } catch (IOException ex) {
            result = false;
            return result;
        } finally {
            try {
                if (temp != null) {
                    temp.close();
                }
            } catch (IOException ex) {
                result = false;
                return result;
            }
        }
        return result;
    }

    @Override
    /**
     * L'esecuzione vera e propria del Server thread
     *
     */
    public void run() {
        if (closed) {
            return;
        }
        try {
            serverSock = new ServerSocket(getPort());
            justStarted();

            while (!shouldIClose()) {
                P2PConnection playerConnection = P2PConnection.acceptConnectionRequest(serverSock);
                synchronized (waitingClients) {
                    waitingClients.add(playerConnection);
                    playerConnection.addMessageReceivedObserver(this, Room.roomEntranceRequestMsg);
                    playerConnection.addMessageReceivedObserver(this, Room.roomCreatePartitaMsg);
                    playerConnection.addMessageReceivedObserver(this, Room.roomEntraPartitaMsg);
                    playerConnection.addMessageReceivedObserver(this, Room.roomAskPartitaReplMsg);
                    playerConnection.addMessageReceivedObserver(this, Room.roomStartMatchMsg);

                }
            }
            System.out.println("Server is closing down");
            ArrayList<P2PConnection> disc = new ArrayList<>();
            disc.addAll(connections.values());
            disc.addAll(waitingClients);
            for (P2PConnection p2p : disc) {
                p2p.disconnect();
            }
            boolean canClose = false;
            while (!canClose) {
                canClose = true;
                for (P2PConnection p2p : connections.values()) {
                    if (!p2p.isClosed()) {
                        canClose = false;
                    }
                }
            }
            System.out.println("All helpers stopped");
            setClosed(true);
            serverSock.close();
        } catch (IOException ex) {
            Logger.getLogger(WebServerPrevious.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected void addPlayer(P2PConnection conn) {
        this.connections.put(conn.getPlayer(), conn);
    }

    protected void removePlayer(P2PConnection conn) {
        this.connections.remove(conn.getPlayer());
    }

    /**
     * Restituisce il numero di giocatori presenti nella stanza.
     *
     * @return il numero di giocatori presenti nella stanza
     */
    public int getPlayerCount() {
        return connections.keySet().size();
        /**
         * Restituisce la lista delle partite
         *
         * @return listapartite
         */
    }

    public ArrayList<String> getPartite() {
        ArrayList<String> result = new ArrayList<String>();
        for (ServerGame a : listapartite) {
            result.add(a.toString());
        }

        return result;
    }

    /**
     * Restituisce una copia dell'elenco di giocatori presenti nella stanza.
     *
     * @return l'elenco (in copia) dei giocatori presenti nella stanza
     */
    public ArrayList<Player> getPlayers() {
        ArrayList<Player> ret = new ArrayList<>();
        ret.addAll(connections.keySet());
        return ret;
    }

    @Override
    public void updateMessageReceived(P2PMessage msg) {
        if (msg.getName().equals(Room.roomEntranceRequestMsg)) {
            handleEntranceRequest(msg);
        } else if (msg.getName().equals(Room.roomExitMsg)) {
            handleExit(msg);
        } else if (msg.getName().equals(Room.roomCreatePartitaMsg)) {

            handleCreate(msg);
        } else if (msg.getName().equals(Room.roomEntraPartitaMsg)) {

            handleEntraPartita(msg);
        } else if (msg.getName().equals(Room.roomAskPartitaReplMsg)) {

            handleConfermaEntrataPartita(msg);
        } else if (msg.getName().equals(roomStartMatchMsg)) {
            handleStartMatch(msg);

        } else if (msg.getName().equals(mancheActionMsg)) {
            handleAction(msg);

        }

    }

    private void handleCreate(P2PMessage msg) {
        synchronized (this) {
            String nomepartita = (String) msg.getParameter(0);
            int ngiocatori = (int) msg.getParameter(1);
            String modalita = (String) msg.getParameter(2);
            String owner = (String) msg.getParameter(3);
            int code = (int) msg.getParameter(4); //
            ServerGame nuovap = new ServerGame(nomepartita, ngiocatori, modalita, owner, code);
            if (nuovap != null) {
                listapartite.add(nuovap);
                P2PConnection sender = msg.getSenderConnection();

                P2PMessage reply = new P2PMessage(Room.roomCreatePartitaReplMsg);
                Object[] parameters = new Object[]{true};
                reply.setParameters(parameters);
                try {
                    sender.sendMessage(reply);
                } catch (PartnerShutDownException ex) {
                    Logger.getLogger(ServerRoom.class.getName()).log(Level.SEVERE, null, ex);
                }
                new Thread(nuovap).start();
                sendRoomUpdate();
            }
            waitingClients.remove(msg.getSenderConnection());
        }
    }

    private void handleEntranceRequest(P2PMessage msg) {
        boolean reqOk = true;
        Player player = null;
        if (msg.getParametersCount() != 2) {
            reqOk = false;
        } else {
            try {
                String roomName = (String) msg.getParameter(0);
                player = (Player) msg.getParameter(1);
                if (!roomName.equals(this.getName())) {
                    reqOk = false;
                }
            } catch (ClassCastException ex) {
                reqOk = false;
            }
        }
        P2PConnection sender = msg.getSenderConnection();
        P2PMessage reply = new P2PMessage(Room.roomEntranceReplyMsg);
        Object[] parameters = new Object[2]; // reply + players+partite
        reply.setParameters(parameters);
        parameters[0] = reqOk;

        synchronized (this) {
            if (reqOk && player != null) {
                sender.setPlayer(player);
                addPlayer(sender);
                waitingClients.remove(sender);
                parameters[1] = this.getPlayers();
                sender.addMessageReceivedObserver(this, Room.roomExitMsg);
                sender.removeMessageReceivedObserver(this, Room.roomEntranceRequestMsg);
                try {
                    sender.sendMessage(reply);
                    sendRoomUpdate();
                } catch (PartnerShutDownException ex) {
                    sender.disconnect();
                    removePlayer(sender);
                }
            }
            this.waitingClients.remove(sender);
        }
    }

    private void handleEntraPartita(P2PMessage msg) {

        for (ServerGame p : listapartite) {
            if (p.getCode() == (int) msg.getParameter(0)) {
                System.out.println(p.getProprietarioCode() + "  " + (int) msg.getParameter(1));

                if (p.getProprietarioCode() == (int) msg.getParameter(1)) {
                    synchronized (this) {

                        handleConfermaEntrataPartita(msg, (int) msg.getParameter(0), (int) msg.getParameter(1));//(codipartita , codice giocatore)
                        this.waitingClients.remove(msg.getSenderConnection());
                    }
                } else {

                    synchronized (this) {

                        P2PMessage reply = new P2PMessage(Room.roomAskPartitaMsg);
                        Object[] params = new Object[]{p.getNomePartita(), msg.getSenderConnection().getPlayer().getName(), msg.getSenderConnection().getPlayer().hashCode(), p.getCode()};
                        reply.setParameters(params);

                        try {
                            for (Player player : connections.keySet()) {
                                if (player.getName() == p.getProprietario()) {

                                    connections.get(player).sendMessage(reply);

                                }

                            }

                        } catch (PartnerShutDownException ex) {
                            Logger.getLogger(ServerRoom.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        this.waitingClients.remove(msg.getSenderConnection());
                    }
                }
            }

        }

    }

    private void handleConfermaEntrataPartita(P2PMessage msg, int codicepart, int codicegioc) {//solo se sono il prorpietario del stanza

        boolean risposta = true;//risposta
        int hashgiocatore = codicegioc;//hashgiocatroe
        int codicepartita = codicepart;//codice
        for (ServerGame p : listapartite) {
            if (p.getCode() == codicepartita) {

                P2PMessage reply = new P2PMessage(Room.roomEntrPartitaReplMsg);
                Object[] params = new Object[]{p.addPlayer(msg.getSenderConnection().getPlayer(), msg.getSenderConnection()), p.getNomePartita(), p.getProprietario(), p.getModalita(), p.getCode(), p.getProprietarioCode()};

                reply.setParameters(params);

                try {
                    msg.getSenderConnection().sendMessage(reply);
                } catch (PartnerShutDownException ex) {
                    Logger.getLogger(ServerRoom.class.getName()).log(Level.SEVERE, null, ex);
                }

                sendMatchUpdate(p);

            }

        }
    }

    private void handleConfermaEntrataPartita(P2PMessage msg) {

        boolean risposta = (boolean) msg.getParameter(0);//risposta
        int hashgiocatore = (int) msg.getParameter(1);//hashgiocatroe
        int codicepartita = (int) msg.getParameter(2);//codice
        for (ServerGame p : listapartite) {
            if (p.getCode() == codicepartita) {

                for (Player player : connections.keySet()) {
                    if (player.hashCode() == hashgiocatore) {

                        boolean aggiunto = risposta && p.addPlayer(player, connections.get(player));
                        P2PMessage reply = new P2PMessage(Room.roomEntrPartitaReplMsg);
                        Object[] params = new Object[]{aggiunto, p.getNomePartita(), p.getProprietario(), p.getModalita(), p.getCode(), p.getProprietarioCode()};

                        reply.setParameters(params);

                        try {
                            connections.get(player).sendMessage(reply);
                        } catch (PartnerShutDownException ex) {
                            Logger.getLogger(ServerRoom.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        System.out.println("update");
                        sendMatchUpdate(p);
                    }
                }
            }
        }
    }

    public void handleStartMatch(P2PMessage msg) {
        int code = (int) msg.getParameter(0);
        for (ServerGame partita : listapartite) {
            if (code == partita.getCode()) {
                LinkedList<Player> players = new LinkedList();
                HashMap<Player, P2PConnection> connection = partita.getConnections();
                System.out.println("startmatch");
                // java.util.Iterator iter = connections.keySet().iterator();
                P2PMessage reply = new P2PMessage(Room.mancheUpdateMsg);
                Object[] params = new Object[]{"start"};
                reply.setParameters(params);
                //ciclo sulle connessioni della manche, entry oggetto di tipo <Player, P2PConnection> 
                for (Map.Entry<Player, P2PConnection> entry : connection.entrySet()) {
                    try {

                        players.add(((Player) entry.getKey()));
                        ((P2PConnection) entry.getValue()).addMessageReceivedObserver(this, Room.mancheActionMsg);
                        System.out.println(((Player) entry.getKey()).getName());
                        ((P2PConnection) entry.getValue()).sendMessage(reply);
                    } catch (PartnerShutDownException ex) {
                        Logger.getLogger(ServerRoom.class.getName()).log(Level.SEVERE, null, ex);

                    }

                }
                partita.addManche(new ServerManche(connection, players));

            }
        }
    }

    private void handleExit(P2PMessage msg) {
        synchronized (this) {
            this.removePlayer(msg.getSenderConnection());
            sendRoomUpdate();
        }
    }

    private void handleAction(P2PMessage msg) {
        Card carta = null;
        String action = (String) msg.getParameter(0);
        String playername = (String) msg.getParameter(1);
        int actionnumber = (int) msg.getParameter(2);
        int matchnumber = (int) msg.getParameter(3);
        String color;
        boolean bluffvalue;

        for (ServerGame p : listapartite) {
            if (p.getCode() == matchnumber) {

                ServerManche temp = p.getManche();
                if (actionnumber == temp.getAction()) {//action calcola il numero di richiesta
                    System.out.println("scelgo azione");
                    if (action.equals("scarta")) {
                        if (msg.getParameter(4) != null) {// not null se l'azione è uno scarta o interrompi cioè se è pesca
                            carta = (Card) msg.getParameter(4);
                        }
                        color = (String) msg.getParameter(5);

                        temp.discard(carta, playername, color);

                    } else if (action.equals("uno")) {
                        if (msg.getParameter(4) != null) {// not null se l'azione è uno scarta o interrompi cioè se è pesca
                            carta = (Card) msg.getParameter(4);
                        }
                        color = (String) msg.getParameter(5);
                        temp.uno(carta, playername, color);

                    } else if (action.equals("draw")) {

                        temp.draw(playername);

                    } else if (action.equals("pass")) {

                        temp.pass(playername);

                    } else if (action.equals("interrupt")) {
                        color = (String) msg.getParameter(5);
                        if (msg.getParameter(4) != null) {// not null se l'azione è uno scarta o interrompi cioè se è pesca
                            carta = (Card) msg.getParameter(4);
                        }
                        temp.interrupt(carta, playername, color);

                    } else if (action.equals("notuno")) {
                        System.out.println("arrivato room");
                        temp.notuno(playername);

                    } else if (action.equals("bluff")) {
                        bluffvalue = (boolean) msg.getParameter(4);
                        temp.bluff(playername, bluffvalue);

                    }

                } else if (action.equals("update")) {

                         temp.update(playername);

                    } 

            }
        }
    }

    private void sendRoomUpdate() {
        P2PMessage upd = new P2PMessage(Room.roomUpdateMsg);
        Object[] updpar = new Object[]{this.getPlayers(), this.getPartite()};
        //aggiungo le partite cosi aggiorno le liste
        //aggiungo le partite cosi aggiorno le liste        
        upd.setParameters(updpar);
        while (upd != null) {
            ArrayList<P2PConnection> unresp = new ArrayList<>();
            for (P2PConnection client : connections.values()) {
                try {
                    client.sendMessage(upd);

                } catch (PartnerShutDownException ex) {
                    unresp.add(client);
                }
            }
            for (P2PConnection p2p : unresp) {
                p2p.disconnect();
                removePlayer(p2p);
            }
            if (unresp.size() > 0) {
                upd.setParameters(new Object[]{this.getPlayers(), this.getPartite()});
            } else {
                upd = null;
            }
        }

    }

    private void sendMatchUpdate(ServerGame p) {
        P2PMessage upd = new P2PMessage(Room.matchUpdateMsg);
        Object[] updpar = new Object[]{p.getPlayers()};
        System.out.println("eseguo" + p.getPlayers().size());
        //aggiungo le partite cosi aggiorno le liste
        //aggiungo le partite cosi aggiorno le liste        
        upd.setParameters(updpar);
        while (upd != null) {
            ArrayList<P2PConnection> unresp = new ArrayList<>();
            for (P2PConnection client : p.getConnections().values()) {
                try {
                    client.sendMessage(upd);

                } catch (PartnerShutDownException ex) {
                    unresp.add(client);
                }
            }
            for (P2PConnection p2p : unresp) {
                p2p.disconnect();
                removePlayer(p2p);
            }
            if (unresp.size() > 0) {
                upd.setParameters(new Object[]{p.getPlayers()});
            } else {
                upd = null;
            }
        }

    }

}
