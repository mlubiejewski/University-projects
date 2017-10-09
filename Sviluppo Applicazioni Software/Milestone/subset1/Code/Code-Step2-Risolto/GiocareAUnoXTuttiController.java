/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unoxtutti;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import javax.swing.DefaultListModel;
import javax.swing.ListModel;
import unoxtutti.domain.Player;
import unoxtutti.connection.ServerCreationException;
import unoxtutti.connection.ServerRoom;

/**
 *
 * @author picardi
 */
public class GiocareAUnoXTuttiController {
	private static GiocareAUnoXTuttiController singleInstance;
	
	private final HashMap<String,ServerRoom> serverRooms;
	private final Player player;
	
	// GUI utility objects
	// nomi stanze ordinati alfabeticamente
	private final DefaultListModel<String> serverRoomNames;
	
	private GiocareAUnoXTuttiController(Player pl){
		player = pl;
		serverRooms = new HashMap<>();
		serverRoomNames = new DefaultListModel<>();
	}
	
	public Player getPlayer() {
		return player;
	}
	/* Per creare questo controller serve un Player. Corrisponde alla precondizione
	 dell'UC che il giocatore sia autenticato.
	*/
	public static GiocareAUnoXTuttiController getInstance(Player aPlayer) {
		if (singleInstance == null || singleInstance.player != aPlayer) singleInstance = new GiocareAUnoXTuttiController(aPlayer);
		return singleInstance;
	}	
	
	public ServerRoom apriStanza(String roomName, int port) throws ServerCreationException {
		ServerRoom room = ServerRoom.createServerRoom(player, roomName, port);
		addServerRoom(room);
		return room;
	}
	
	private void addServerRoom(ServerRoom room) {
		serverRooms.put(room.getName(), room);
		ArrayList<String> names = new ArrayList<>();
		names.addAll(serverRooms.keySet());
		Collections.sort(names);
		serverRoomNames.clear();		
		for (String n: names) serverRoomNames.addElement(n);
	}
	
	private void removeServerRoom(ServerRoom room) {
		serverRooms.remove(room.getName());
		serverRoomNames.removeElement(room.getName());
	}
	
	public boolean chiudiStanza(ServerRoom aRoom) {
		aRoom.shouldClose();
		try {
			aRoom.waitOnClose(3000);
		} catch (InterruptedException ex) {
			
		} finally {
			if (!aRoom.isClosed()) aRoom.forceClose();
		}
		removeServerRoom(aRoom);
		return true;
	}
	
	public ListModel<String> getServerRoomNames() {
		return serverRoomNames;
	}

	public ServerRoom getRoom(String rname) {
		return serverRooms.get(rname);
	}
}
