/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unoxtutti.connection;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import unoxtutti.domain.Player;
import unoxtutti.domain.Room;

/**
 * La classe ServerRoom rappresenta una Room (Stanza) lato Server La Room lato
 * Server &egrave; il Server stesso. Dovr&agrave; essere estesa per contenere,
 * oltre che un elenco di giocatori, anche un elenco di partite disponibili.
 *
 * @author picardi
 */
public class ServerRoom extends Room implements Runnable {

	private final Player owner;
	private ServerSocket serverSock;
	private boolean shouldClose;
	private boolean closed;
	private final Object closeDownMonitor;
	protected final HashMap<Player, P2PConnection> connections;

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
				this.addPlayer(playerConnection);
			}
			System.out.println("Server is closing down");
			for (P2PConnection p2p : connections.values()) {
				p2p.closeDown();
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
			Logger.getLogger(ServerRoom.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	protected void addPlayer(P2PConnection conn) {
		this.connections.put(conn.getPlayer(), conn);
	}

	/**
	 * Restituisce il numero di giocatori presenti nella stanza.
	 *
	 * @return il numero di giocatori presenti nella stanza
	 */
	public int getPlayerCount() {
		return connections.keySet().size();
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
}
