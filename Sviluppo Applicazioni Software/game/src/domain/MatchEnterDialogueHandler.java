/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unoxtutti.domain;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import unoxtutti.connection.P2PConnection;
import unoxtutti.connection.P2PMessage;
import unoxtutti.connection.PartnerShutDownException;
import unoxtutti.domain.dialogue.BasicDialogueHandler;

/**
 * Questa classe gestisce un dialogo di ingresso in una stanza. Tale dialogo
 * &egrave; costituito da una richiesta di ingresso e dall'attesa di una
 * risposta (positiva o negativa). La richiesta ("roomEntranceRequestMsg") ha
 * come parametri il nome della stanza (a mo' di verifica) e il giocatore che
 * vuole entrare. La risposta ("roomEntranceReplyMsg") ha come unico parametro
 * la risposta (true o false, accettata o rifiutata) alla richiesta.
 *
 * @author picardi
 */
public class MatchEnterDialogueHandler extends BasicDialogueHandler<MatchEnterDialogueState> {

    private final P2PConnection p2pConn;

    /**
     * Crea un handler per una richiesta di ingresso (ci deve essere un handler
     * diverso per ogni richiesta). L'handler &egrave; inizialmente in stato
     * BEFORE_REQUEST.
     *
     * @param p2p La connessione su cui i messaggi viaggiano
     */
    public MatchEnterDialogueHandler(P2PConnection p2p) {
        super(MatchEnterDialogueState.BEFORE_REQUEST);
        p2pConn = p2p;
    }

    /**
     * Inizia il dialogo richiedendo l'ingresso e passando allo stato REQUESTED.
     * Se l'invio non funziona torna allo stato BEFORE_REQUEST.
     *
     * @param pl Il giocatore che vuole entrare nella stanza
     * @param roomName Il nome della stanza in cui entrare
     * @return true se la richiesta &egrave; partita correttamente, false se
     * c'&egrave; stato un problema di comunicazione.
     */
    public boolean startDialogue(int codicepartita,int codicegioc) {
        boolean ret = true;
        p2pConn.addMessageReceivedObserver(this, Room.roomEntrPartitaReplMsg);

        P2PMessage msgToSnd = new P2PMessage(Room.roomEntraPartitaMsg);
        Object[] params = new Object[]{codicepartita,codicegioc};
        
        msgToSnd.setParameters(params);
        this.setState(MatchEnterDialogueState.REQUESTED);
        try {
            p2pConn.sendMessage(msgToSnd);

        } catch (PartnerShutDownException ex) {
            this.setState(MatchEnterDialogueState.BEFORE_REQUEST);
            ret = false;
        }
        return ret;
    }

    /**
     * Dichiara il dialogo terminato. L'oggetto smette di essere registrato per
     * ricevere messaggi presso la connessione P2P
     */
    public void concludeDialogue() {
        p2pConn.removeMessageReceivedObserver(this, Room.roomEntrPartitaReplMsg);
    }

    /**
     *
     * @return Se l'ingresso nella stanza &egrave; completato correttamente,
     * restituisce l'elenco dei giocatori ivi presenti al momento dell'ingresso.
     * Altrimenti restituisce un elenco vuoto.
     */
    public Object[] getInfo() {

        Object[] ret = null;
        if (this.getState().equals(MatchEnterDialogueState.ADMITTED)) {
            ret = new Object[5];
            ret[0] = (String) this.getStateChangeTrigger().getParameter(1);
            ret[1] = (String) this.getStateChangeTrigger().getParameter(2);
            ret[2] = (String) this.getStateChangeTrigger().getParameter(3);
            ret[3] = (int) this.getStateChangeTrigger().getParameter(4);
            ret[4] = (int) this.getStateChangeTrigger().getParameter(5);
           
        }
        return ret;
    }
}
