/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unoxtutti.domain;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import unoxtutti.connection.P2PConnection;
import unoxtutti.connection.P2PMessage;
import unoxtutti.connection.PartnerShutDownException;
import unoxtutti.game.Card;
import unoxtutti.game.Table;

/**
 *
 * @author joana
 */
public class ServerManche {

    private final HashMap<Player, P2PConnection> connections;
    private final LinkedList<Player> players; //serve per la gestione dei turni
    private int turno;
    private char versoturno;
    private final Table tavolo;
    private int action;//conto il numero di azioni
    private boolean victory;
    private final HashMap<Player, Integer> timeout;
    private final HashMap<Player, Boolean> unopenality;

    private final HashMap<Player, LinkedList<Card>> carteNelleMani;
    private boolean askingToClientBluff; //serve per rendere impossibile interrompere fino a che il giocatore dopo pesca4 non ha cliccato se bluffare o no
    private boolean askingToClientScartaPescata; //to ask the client if he wanna play the card the he draw
    private boolean actionCardInterrupted; //if the action card have been interrupted
    private boolean beforeSaidUno; //if the player that played before said UNO!
    private Player playedBefore; //the player that played before
    private boolean currentSaidUno; //if the current player said UNO!

    public ServerManche(final HashMap<Player, P2PConnection> connections, final LinkedList<Player> players) {
        //creo un ordine random per il turno
        Random r = new Random();
        int a = 0; // numero minimo
        int b = connections.size() - 1; // numero massimo
        int c = ((b - a) + 1);
        turno = r.nextInt(c) + a;
        action = 0;

        //inizializzo i campi
        this.connections = connections;
        this.players = players;
        carteNelleMani = new HashMap();
        timeout = new HashMap();
        unopenality = new HashMap();
        askingToClientBluff = false;
        beforeSaidUno = false;
        currentSaidUno = false;
        playedBefore = null;
        versoturno = 'c';
        tavolo = new Table();
        for (Player p : connections.keySet()) {
            this.giveCards(p);

            timeout.put(p, 0);
        }
        for (Player p : connections.keySet()) {
            unopenality.put(p, false);

        }
        tavolo.start();
        this.sendFirstMessage();//ciclo su tutti gli utenti

        /*
        if (tavolo.firstCard().getTipo() == 'a') {
            //applica gli effetti
            switch (tavolo.firstCard().getEffetto()) {

                case 's':
                    upgradeTurno(turno);
                    break;
                case 'c':
                    tavolo.cambiaGiro();
                    break;
                //il caso pesca viene gestito automaticamente da effects() all'inizio del turno
            }
        }*/
    }

    void removePlayer(P2PConnection conn) {
        //------------------------------------------------------------------------------------------------TODO
        this.connections.remove(conn.getPlayer());
    }

    /*
    //checks if the action arrived at the server is permitted or not
    public boolean actionIsPermitted(final String azione, final int carta, Player p) {
        //controlla che la carta da scartare sia tra quelle in mano al giocatore
        if (!isInPlayerHand(carta, p)) {
            return false;
        }
        //commenti messi in positivo
        switch (azione) {

            case "interrompi":
                //deve essere la stessa carta di quella scoperta
                if (sameCard(carta, p)) {
                    return true;
                }
                return false;

            case "scarta":
                //se il giocatore di turno deve decidere se bluffare o no. Se giocare la carta pescata o no.
                if (askingToClientBluff || askingToClientScartaPescata) {
                    return false;
                }
                //deve essere il tuo turno
                if (!isYourRound(p)) {
                    return false;
                }

                //se è giocabile
                return cardIsPlayable(carta);

            case "pesca":
                //se il giocatore di turno deve decidere se bluffare o no. Se giocare la carta pescata o no.
                if (askingToClientBluff || askingToClientScartaPescata) {
                    return false;
                }
                //deve essere il tuo turno
                if (!isYourRound(p)) {
                    return false;
                }
                return true;

            case "bluff":
                //deve essere in atto un bluff
                if (!askingToClientBluff) {
                    return false;
                }
                //deve essere il tuo turno
                if (!isYourRound(p)) {
                    return false;
                }
                return true;

            case "uno":
                //deve essere il tuo turno
                if (!isYourRound(p)) {
                    return false;
                }
                //devi avere 2 carte in mano
                if (carteInMano() == 2) {
                    return false;
                }

                return true;
            case "checkuno":
                //se il giocatore di turno deve decidere se bluffare o no. Se giocare la carta pescata o no.
                if (askingToClientBluff || askingToClientScartaPescata) {
                    return false;
                }
                //deve essere il tuo turno
                if (!isYourRound(p)) {
                    return false;
                }
                return true;
            case "next":
                //deve essere il tuo turno
                if (!isYourRound(p)) {
                    return false;
                }
                //deve essere in atto la richiesta
                return askingToClientScartaPescata;
        }
        //action non permitted
        return false;
    }*/
    public boolean isInterruptable() {
        Card c = tavolo.firstCard();
        return !(c.getTipo() == 'a' || c.equals(new Card('j', 'n', -1, '4')));
    }

    private void giveCards(Player p) {
        carteNelleMani.put(p, tavolo.giveCards());
    }

    private void sendFirstMessage() {
        Card first = tavolo.firstCard();
        // System.out.println("mio turno1" + this.getPlayerTurno().getName());
        if (first.getTipo() == 'a') {
            handleActionFirst(first, this.getPlayerTurno());
        } else if (first.getTipo() == 'j') {
            handleJolly(first, tavolo.getRandomColor());

        }

        //System.out.println("mio turno" + this.getPlayerTurno().getName());
        for (Player p : connections.keySet()) {

            P2PMessage msg = new P2PMessage(Room.mancheUpdateMsg);
            //{azione da eseguire,la mano del giocatore p,numero di carte per ciascun giocatore,la prima carte,turno del giocatore , l'id dell'azione }
            Object[] params = new Object[]{"reciveCard", carteNelleMani.get(p), countCards(), first, this.getPlayerTurno().getName(), action};
            msg.setParameters(params);
            try {
                System.out.println("giocatore scelto" + p.getName());
                connections.get(p).sendMessage(msg);
            } catch (PartnerShutDownException ex) {
                Logger.getLogger(ServerRoom.class.getName()).log(Level.SEVERE, null, ex);

            }
        }

    }

    //methods to check/compare properties of the upcard on the table
    private boolean sameColor(int carta) {
        return tavolo.firstCard().getColore() == carteNelleMani.get(players.get(turno)).get(carta).getColore();
    }

    private void nextTurno() {

        if (versoturno == 'c') {
            turno = (turno + 1) % players.size();

        } else if (versoturno == 'a') {
            if (turno == 0) {
                turno = players.size() - 1;
            } else {
                turno = (turno - 1) % players.size();
            }

        }

    }

    private boolean sameNumber(int carta) {
        return tavolo.firstCard().getNumero() == carteNelleMani.get(players.get(turno)).get(carta).getNumero();
    }

    private boolean sameCard(int carta, Player p) {
        return tavolo.firstCard().equals(carteNelleMani.get(players.get(players.indexOf(p))).get(carta));
    }

    //to check if the player that sent a request is the one that have the round
    private boolean isYourRound(Player p) {
        return p.equals(players.get(turno));
    }

    private boolean isJolly(int carta) {
        return carteNelleMani.get(players.get(turno)).get(carta).getTipo() == 'j';
    }

    private boolean isBase(int carta) {
        return carteNelleMani.get(players.get(turno)).get(carta).getTipo() == 'b';
    }

    //controlla che la carta da scartare sia tra quelle in mano al giocatore
    private boolean isInPlayerHand(int carta, Player p) {
        return carta >= 1 && carta <= carteNelleMani.get(players.get(players.indexOf(p))).size();
    }

    //numero di carte nella mano del giocatore di turno
    private int carteInMano() {
        return carteNelleMani.get(players.get(turno)).size();
    }

    //parametri:
    //azione can be: 'scarta', 'interrompi', 'checkBluff', 'pesca', 'uno', 'next', 'checkUno'
    //carta is the number of the card in the list
    //color is the color of the jolly in case you played it. can be r=rosso b=blu, g=giallo, v=verde, n=no color (in case is not jolly)
    //return if the player need interaction
    /*protected void action(final String azione, final int carta, final Player p, final char color, final boolean check) {
        switch (azione) {
            case "scarta":
                scarta(carta, color);
                break;
            case "interrompi":
                interrompi(p, carta, color);
                break;
            case "pesca":
                pesca(1, true, turno);
                break;
            case "bluff":
                checkBluff(check);
                break;
            case "uno":
                uno();
                break;
            case "checkuno":
                checkUno();
                break;
            case "next":
                //-----------------------------------can be optimized
                break;
        }
    }*/
    //carta is the number of the card in the list
    //color is the color of the jolly in case you played it. can be r=rosso b=blu, g=giallo, v=verde, n=no color (in case is not jolly)
    /* protected void scarta(final int carta, final char color) {
        Card c = carteNelleMani.get(players.get(turno)).remove(carta);
        if (c.getTipo() == 'j') {
            c.setColor(color);
        }
        tavolo.scarta(c);
        if (carteNelleMani.get(players.get(turno)).size() == 0) {
            //VITTORIA!
            win();
        }
    }*/
    protected void discard(Card c, String player, String color) {// color sara settato solo se è una carta jolly
        if (c != null) {

            carteNelleMani.get(this.getPlayerTurno()).remove(c);//rimouvo la carta dalla liste del giocatore
            tavolo.scarta(c);//metto la carta in cima a quelle scartate
            Player winner=checkWin();
            if (winner!=null) {
                
                 for (Player p : connections.keySet()) {

                    P2PMessage msg = new P2PMessage(Room.mancheUpdateMsg);
                    DefaultListModel<Card> handsret = new DefaultListModel();

                    for (Card cartetemp : carteNelleMani.get(p)) {

                        handsret.addElement(cartetemp);
                    }                               //{azione,nuovo turno ,la carta in cima mazzo,id azione,la lista con stringhe che hanno nomegiocatore + carte del giocatore,mano del giocatore p}
                    Object[] params = new Object[]{"winner", getPlayerTurno().getName(), tavolo.firstCard(), action, countCards(), handsret,winner.getName()};
                    msg.setParameters(params);
                    try {

                        connections.get(p).sendMessage(msg);
                    } catch (PartnerShutDownException ex) {
                        Logger.getLogger(ServerRoom.class.getName()).log(Level.SEVERE, null, ex);

                    }
                }
                
                
                
                
            } else if (c.getEffetto() == '4') {
                char colorcode = Character.toLowerCase(color.charAt(0));
                tavolo.firstCard().setColor(colorcode);

                nextTurno();

                action++;
                for (Player p : connections.keySet()) {

                    P2PMessage msg = new P2PMessage(Room.mancheUpdateMsg);
                    DefaultListModel<Card> handsret = new DefaultListModel();

                    for (Card cartetemp : carteNelleMani.get(p)) {

                        handsret.addElement(cartetemp);
                    }                               //{azione,nuovo turno ,la carta in cima mazzo,id azione,la lista con stringhe che hanno nomegiocatore + carte del giocatore,mano del giocatore p}
                    Object[] params = new Object[]{"bluffcheck", getPlayerTurno().getName(), tavolo.firstCard(), action, countCards(), handsret};
                    msg.setParameters(params);
                    try {

                        connections.get(p).sendMessage(msg);
                    } catch (PartnerShutDownException ex) {
                        Logger.getLogger(ServerRoom.class.getName()).log(Level.SEVERE, null, ex);

                    }
                }

            } else {

                if (c.getTipo() == 'a') {
                    handleAction(c, this.getPlayerTurno());

                } else if (c.getTipo() == 'j') {
                    char colorcode = Character.toLowerCase(color.charAt(0));

                    handleJolly(c, colorcode);

                }

                if (carteNelleMani.get(this.getPlayerTurno()).size() == 1) {// è possibile chiamre che non ha chiamato uno
                    unopenality.put(this.getPlayerTurno(), true);

                }
                resetUno(player);
                nextTurno();

                action++;

                for (Player p : connections.keySet()) {

                    P2PMessage msg = new P2PMessage(Room.mancheUpdateMsg);
                    DefaultListModel<Card> handsret = new DefaultListModel();

                    for (Card cartetemp : carteNelleMani.get(p)) {

                        handsret.addElement(cartetemp);
                    }                               //{azione,nuovo turno ,la carta in cima mazzo,id azione,la lista con stringhe che hanno nomegiocatore + carte del giocatore,mano del giocatore p}
                    Object[] params = new Object[]{"update", getPlayerTurno().getName(), tavolo.firstCard(), action, countCards(), handsret};
                    msg.setParameters(params);
                    try {

                        connections.get(p).sendMessage(msg);
                    } catch (PartnerShutDownException ex) {
                        Logger.getLogger(ServerRoom.class.getName()).log(Level.SEVERE, null, ex);

                    }
                }
            }

        }
    }

    protected void bluff(String player, boolean bluff) {
        if (bluff) {
            if (checkBluff()) {
                this.addCards(4, getPreviousPlayer());

            } else {
                this.addCards(4, getPlayerTurno());
            }

        } else {
            this.addCards(4, getPlayerTurno());

        }

        for (Player p : connections.keySet()) {

            P2PMessage msg = new P2PMessage(Room.mancheUpdateMsg);
            DefaultListModel<Card> handsret = new DefaultListModel();

            for (Card cartetemp : carteNelleMani.get(p)) {

                handsret.addElement(cartetemp);
            }                               //{azione,nuovo turno ,la carta in cima mazzo,id azione,la lista con stringhe che hanno nomegiocatore + carte del giocatore,mano del giocatore p}
            Object[] params = new Object[]{"update", getPlayerTurno().getName(), tavolo.firstCard(), action, countCards(), handsret};
            msg.setParameters(params);
            try {

                connections.get(p).sendMessage(msg);
            } catch (PartnerShutDownException ex) {
                Logger.getLogger(ServerRoom.class.getName()).log(Level.SEVERE, null, ex);

            }
        }

    }

    protected void uno(Card c, String player, String color) {// color sara settato solo se è una carta jolly
        if (c != null) {

            Player getP = null;
            for (Player p : players) {
                if (p.getName().equals(player)) {
                    getP = p;
                }

            }

            carteNelleMani.get(getP).remove(c);//rimouvo la carta dalla liste del giocatore
            tavolo.scarta(c);//metto la carta in cima a quelle scartate

            if (c.getTipo() == 'a') {
                handleAction(c, this.getPlayerTurno());

            } else if (c.getTipo() == 'j') {
                char colorcode = Character.toLowerCase(color.charAt(0));

                handleJolly(c, colorcode);

            }

            nextTurno();

            action++;
            String message = "Il giocatore:" + player + " ha dichiarato UNO!!!";
            for (Player p : connections.keySet()) {

                P2PMessage msg = new P2PMessage(Room.mancheUpdateMsg);
                DefaultListModel<Card> handsret = new DefaultListModel();

                for (Card cartetemp : carteNelleMani.get(p)) {

                    handsret.addElement(cartetemp);
                }                               //{azione,nuovo turno ,la carta in cima mazzo,id azione,la lista con stringhe che hanno nomegiocatore + carte del giocatore,mano del giocatore p}
                Object[] params = new Object[]{"update", getPlayerTurno().getName(), tavolo.firstCard(), action, countCards(), handsret, message};
                msg.setParameters(params);
                try {

                    connections.get(p).sendMessage(msg);
                } catch (PartnerShutDownException ex) {
                    Logger.getLogger(ServerRoom.class.getName()).log(Level.SEVERE, null, ex);

                }
            }

        }
    }

    protected void interrupt(Card c, String player, String color) {// color sara settato solo se è una carta jolly
        if (c != null) {
            Player interrupter = null;
            for (Player p : players) {
                if (p.getName().equals(player)) {
                    interrupter = p;
                }

            }
            int index = players.indexOf(interrupter);
            turno = index;

            carteNelleMani.get(interrupter).remove(c);//rimouvo la carta dalla liste del giocatore
            tavolo.scarta(c);//metto la carta in cima a quelle scartate

            Player winner=checkWin();
            if (winner!=null) {
                
                 for (Player p : connections.keySet()) {

                    P2PMessage msg = new P2PMessage(Room.mancheUpdateMsg);
                    DefaultListModel<Card> handsret = new DefaultListModel();

                    for (Card cartetemp : carteNelleMani.get(p)) {

                        handsret.addElement(cartetemp);
                    }                               //{azione,nuovo turno ,la carta in cima mazzo,id azione,la lista con stringhe che hanno nomegiocatore + carte del giocatore,mano del giocatore p}
                    Object[] params = new Object[]{"winner", getPlayerTurno().getName(), tavolo.firstCard(), action, countCards(), handsret,winner.getName()};
                    msg.setParameters(params);
                    try {

                        connections.get(p).sendMessage(msg);
                    } catch (PartnerShutDownException ex) {
                        Logger.getLogger(ServerRoom.class.getName()).log(Level.SEVERE, null, ex);

                    }
                }
                
                
                
                
            }
            
            
            else{
            
            if (c.getTipo() == 'a') {
                handleAction(c, interrupter);

            } else if (c.getTipo() == 'j') {
                char colorcode = Character.toLowerCase(color.charAt(0));

                handleJolly(c, colorcode);

            }
            if (carteNelleMani.get(this.getPlayerTurno()).size() == 1) {// è possibile chiamre che non ha chiamato uno
                unopenality.put(this.getPlayerTurno(), true);

            }
            players.indexOf(c);
            resetUno(player);
            nextTurno();

            action++;

            for (Player p : connections.keySet()) {

                P2PMessage msg = new P2PMessage(Room.mancheUpdateMsg);
                DefaultListModel<Card> handsret = new DefaultListModel();

                for (Card cartetemp : carteNelleMani.get(p)) {

                    handsret.addElement(cartetemp);
                }                               //{azione,nuovo turno ,la carta in cima mazzo,id azione,la lista con stringhe che hanno nomegiocatore + carte del giocatore,mano del giocatore p}
                Object[] params = new Object[]{"update", getPlayerTurno().getName(), tavolo.firstCard(), action, countCards(), handsret};
                msg.setParameters(params);
                try {

                    connections.get(p).sendMessage(msg);
                } catch (PartnerShutDownException ex) {
                    Logger.getLogger(ServerRoom.class.getName()).log(Level.SEVERE, null, ex);

                }
            }
        }
        }
    }

    protected void draw(String player) {

        Card newcard = tavolo.giveCard();
        carteNelleMani.get(this.getPlayerTurno()).add(newcard);

        action++;//modifica al id id azioni

        for (Player p : connections.keySet()) {

            P2PMessage msg = new P2PMessage(Room.mancheUpdateMsg);
            DefaultListModel<Card> handsret = new DefaultListModel();

            for (Card cartetemp : carteNelleMani.get(p)) {

                handsret.addElement(cartetemp);
            }
            Object[] params = new Object[]{"update", getPlayerTurno().getName(), tavolo.firstCard(), action, countCards(), handsret};
            msg.setParameters(params);
            try {

                connections.get(p).sendMessage(msg);
            } catch (PartnerShutDownException ex) {
                Logger.getLogger(ServerRoom.class.getName()).log(Level.SEVERE, null, ex);

            }
        }

    }

    protected void pass(String player) {
        nextTurno();
        action++;//modifica al id id azioni

        for (Player p : connections.keySet()) {

            P2PMessage msg = new P2PMessage(Room.mancheUpdateMsg);
            DefaultListModel<Card> handsret = new DefaultListModel();

            for (Card cartetemp : carteNelleMani.get(p)) {

                handsret.addElement(cartetemp);
            }
            Object[] params = new Object[]{"update", getPlayerTurno().getName(), tavolo.firstCard(), action, countCards(), handsret};
            msg.setParameters(params);
            try {

                connections.get(p).sendMessage(msg);
            } catch (PartnerShutDownException ex) {
                Logger.getLogger(ServerRoom.class.getName()).log(Level.SEVERE, null, ex);

            }
        }

    }
 protected void update(String player) {
     

        for (Player p : connections.keySet()) {
if(p.getName().equals(player)){
            P2PMessage msg = new P2PMessage(Room.mancheUpdateMsg);
            DefaultListModel<Card> handsret = new DefaultListModel();

            for (Card cartetemp : carteNelleMani.get(p)) {

                handsret.addElement(cartetemp);
            }
            Object[] params = new Object[]{"update", getPlayerTurno().getName(), tavolo.firstCard(), action, countCards(), handsret};
            msg.setParameters(params);
            try {

                connections.get(p).sendMessage(msg);
            } catch (PartnerShutDownException ex) {
                Logger.getLogger(ServerRoom.class.getName()).log(Level.SEVERE, null, ex);

            }
        }
        
        
        }

    }
    protected void notuno(String player) {
        System.out.println("quiiwii");
        boolean exist = false;
        for (Map.Entry<Player, Boolean> entry : unopenality.entrySet()) {
            if (entry.getValue() == true) {
                addCards(2, entry.getKey());
                exist = true;
            }

        }
        if (!exist) {
            System.out.println("quiiwi");
            for (Player p : connections.keySet()) {
                if (p.getName().equals(player)) {
                    addCards(2, p);
                }

            }

        }

        action++;//modifica al id id azioni

        for (Player p : connections.keySet()) {

            P2PMessage msg = new P2PMessage(Room.mancheUpdateMsg);
            DefaultListModel<Card> handsret = new DefaultListModel();

            for (Card cartetemp : carteNelleMani.get(p)) {

                handsret.addElement(cartetemp);
            }
            Object[] params = new Object[]{"update", getPlayerTurno().getName(), tavolo.firstCard(), action, countCards(), handsret};
            msg.setParameters(params);
            try {

                connections.get(p).sendMessage(msg);
            } catch (PartnerShutDownException ex) {
                Logger.getLogger(ServerRoom.class.getName()).log(Level.SEVERE, null, ex);

            }
        }

    }

    //player is the player that interrupted
    //carta is the number of the card in the list
    //color is the color of the jolly in case you played it. can be r=rosso b=blu, g=giallo, v=verde, n=no color (in case is not jolly)
    protected void interrompi(final Player p, final int carta, final char color) {
        turno = players.indexOf(p);
        Card c = carteNelleMani.get(p).remove(carta);
        if (c.getTipo() == 'j') {
            c.setColor(color);
        }
        //se sono due carte action uguali
        if (c.getTipo() == 'a' && c.equals(tavolo.firstCard())) {
            actionCardInterrupted = true;
        }
        tavolo.scarta(c);
        if (carteNelleMani.get(players.get(turno)).size() == 0) {
            //VITTORIA!
            win();
        }
    }

    //action is true if pesca has been invoked as an action by a player
    protected void pesca(final int n, final boolean action, final int player) {
        //pesca
        List c = tavolo.pesca(n);
        //aggiungi la carta in mano
        carteNelleMani.get(players.get(player)).addAll(c);

        if (action) {
            //can the player play the card? if yes ask him if he wanna play it
            if (cardIsPlayable(carteNelleMani.get(players.get(turno)).size() - 1)) {
                askingToClientScartaPescata = true;
            }
        }
    }

    //the player after the one that played pesca 4 decided to check if he was bluffing
    protected void checkBluff(boolean check) {
        if (check) {
            //se il giocatore precedente a quello di turno aveva una carta scartabile con quella prima di pesca 4
            Card preC = tavolo.secondCard();
            int preP = playedBefore();
            if (couldPlayAny(preC, carteNelleMani.get(players.get(preP)))) {
                pesca(4, false, preP);
            } else {
                pesca(6, false, turno);
            }
        } else {
            pesca(4, false, turno);
        }
        askingToClientBluff = false;
    }

    protected void uno() {
        currentSaidUno = true;
    }

    protected void checkUno() {
        if (carteNelleMani.get(playedBefore).size() == 1 && !beforeSaidUno) {
            pesca(2, false, players.indexOf(playedBefore));
        }
    }

    //returns if the player could play any card
    private boolean couldPlayAny(Card upcard, LinkedList<Card> hand) {
        //
        for (Card c : hand) {
            if (c.getTipo() != 'j') {
                if (c.getColore() == upcard.getColore()) {
                    return true;
                }
            }
        }
        return false;
    }

    //returns the number of the player that played before
    private int playedBefore() {
        int pb;
        if (tavolo.getRoundDirection() == 'o') {
            pb = turno - 1;
            if (pb < 0) {
                pb = players.size() - 1;
            }
        } else {
            pb = (turno + 1) % players.size();
        }
        return pb;
    }

    private boolean cardIsPlayable(final int carta) {
        //se è una carta jolly
        if (isJolly(carta)) {
            return true;
        }
        //stesso colore
        if (sameColor(carta)) {
            return true;
        }
        return isBase(carta) && sameNumber(carta);
    }

    //to apply effects of the new round
    void effects() {
        if (!actionCardInterrupted && tavolo.firstCard().getEffetto() == 'p') {
            //pesca
            List ca = tavolo.pesca(2);
            //aggiungi le carte in mano
            carteNelleMani.get(this.players.get(turno)).addAll(ca);

        } else if (tavolo.firstCard().getEffetto() == '4') {
            askingToClientBluff = true;
        }
        actionCardInterrupted = false;
    }

    private void upgradeTurno(final int player) {
        if (tavolo.getRoundDirection() == 'c') {
            turno = (player + 1) % players.size();
        } else {
            turno = player - 1;
            if (turno < 0) {
                turno = players.size() - 1;
            }
        }
    }

    public Player getPlayerTurno() {
        return players.get(turno);

    }

    public Player getNextPlayer() {
        int temp = 0;

        if (versoturno == 'c') {
            temp = (turno + 1) % players.size();

        } else if (versoturno == 'a') {
            if (turno == 0) {
                temp = players.size() - 1;
            } else {
                temp = (turno - 1) % players.size();
            }

        }
        return players.get(temp);

    }

    public Player getPreviousPlayer() {
        int temp = 0;

        if (versoturno == 'c') {
            if (turno == 0) {
                temp = players.size() - 1;
            } else {
                temp = (turno - 1) % players.size();
            }

        } else if (versoturno == 'a') {

            temp = (turno + 1) % players.size();

        }
        return players.get(temp);

    }

    //player is the turno that did the action
    void nextRound() {
        //puliamo-modifichiamo le variabili
        playedBefore = players.get(turno);
        if (currentSaidUno) {
            currentSaidUno = false;
            beforeSaidUno = true;
        } else {
            beforeSaidUno = false;
        }
        askingToClientScartaPescata = false;

        //se è un cambia giro
        if (!actionCardInterrupted && tavolo.firstCard().getEffetto() == 'c') {
            tavolo.cambiaGiro();
        }

        upgradeTurno(turno);

        //se è stato giocato un salta turno
        if (!actionCardInterrupted && tavolo.firstCard().getEffetto() == 'c') {
            upgradeTurno(turno);
        }
    }



    public void win() {
        victory = true;
    }

    public boolean isVictory() {
        return victory;
    }

    public boolean isAskingToClientScartaPescata() {
        return askingToClientScartaPescata;
    }

    void timeOut() {

        //askingToClientScartaPescata finendo il tempo si chiudono le dialog (sarebbe come non scartare) quindi non va implementato
        if (askingToClientBluff) {
            checkBluff(false);
        }
        pesca(2, false, turno);

        //gestioneTimeout
        int t = timeout.get(players.get(turno)) + 1;
        timeout.replace(players.get(turno), t);

        //----------------------------------------------------------------BUTTARE FUORI IL PLAYER
    }

    private DefaultListModel<String> countCards() {
        DefaultListModel<String> r = new DefaultListModel();
        for (Map.Entry<Player, LinkedList<Card>> entry : carteNelleMani.entrySet()) {

            r.addElement(((Player) entry.getKey()).getName() + ":" + ((LinkedList<Card>) entry.getValue()).size() + " carte");
        }

        return r;
    }

    public int getAction() {
        return action;
    }

    public void addCards(int number, Player toadd) {

        List<Card> cards = tavolo.pesca(number);
        for (Card card : cards) {
            carteNelleMani.get(toadd).add(card);
        }
    }

    public void handleActionFirst(Card c, Player p) {// per gestire le azioni

        char effetto = c.getEffetto();
        switch (effetto) {
            case 'p':
                this.addCards(2, p);
                break;
            case 's':
                nextTurno();
                break;
            case 'c':
                if (versoturno == 'c') {
                    versoturno = 'a';
                } else {
                    versoturno = 'c';
                }
                break;
            default:
                break;
        }
    }

    public void handleAction(Card c, Player p) {// per gestire le azioni

        char effetto = c.getEffetto();
        switch (effetto) {
            case 'p':
                this.addCards(2, getNextPlayer());
                break;
            case 's':
                nextTurno();
                break;
            case 'c':
                if (versoturno == 'c') {
                    versoturno = 'a';
                } else {
                    versoturno = 'c';
                }
                break;
            default:
                break;
        }
    }

    public void handleJolly(Card c, char color) {
        char effetto = c.getEffetto();
        if (effetto == 'j') {
            c.setColor(color);

        } else if (effetto == '4') {
            c.setColor(color);
            this.addCards(4, getNextPlayer());

        }

    }

    public void resetUno(String player) {
        for (Player p : connections.keySet()) {
            if (!p.getName().equals(player)) {
                unopenality.put(p, false);

            }
            System.out.println("lista pen" + unopenality);

        }

    }

    public boolean checkBluff() {
        Card top = tavolo.firstCard();
        for (Card c : carteNelleMani.get(getPreviousPlayer())) {
            if (c.getColore() == top.getColore()) {
                return true;
            }

        }
        return false;

    }

    public Player checkWin() {
        Card top = tavolo.firstCard();
        for (Player p : carteNelleMani.keySet()) {
            if (carteNelleMani.get(p).isEmpty()) {
                return p;

            }

        }
        return null;

    }

}
