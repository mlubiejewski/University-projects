/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unoxtutti.domain;

import java.util.HashMap;
import java.util.LinkedList;
import unoxtutti.connection.P2PConnection;

/**
 *
 * @author joana
 */
public class ServerMancheHelper extends Thread {

    private boolean closure;
    private final ServerManche sm;
    private final LinkedList<Action> bufferRichieste;
    private boolean interruptTimer;
    private boolean endMancheTimer;
    private Timer timerManche;
    private Timer timerInterrupt;
    private boolean newRound;

    public ServerMancheHelper(final HashMap<Player, P2PConnection> connections, final LinkedList players) {
        sm = new ServerManche(connections, players);
        bufferRichieste = new LinkedList();
    }

    //ascolta e gestisci le rischieste dei client
    @Override
    public void run() {
        boolean victory = false;
        newRound = true;
        sm.updateGui();
        while (!closure) {
            if (!sm.isVictory()) {
                if (newRound) {
                    sm.nextRound();
                    sm.effects();
                    sm.updateGui(); //in caso di pesca4 o pesca 2
                    newRound = false;
                    timerManche = new Timer(this, 'm');
                    timerManche.start();
                    interruptTimer = false;
                    bufferRichieste.clear();
                }
                /*
                //esegui le richieste se possibili
                if (!bufferRichieste.isEmpty()) {
                    Action a = bufferRichieste.pop();
                    //se è un azione permessa
                    if (interruptTimer) {
                        endMancheTimer = false;
                        if (a.getAzione().equals("interrompi")) {
                            sm.action(a.getAzione(), a.getCarta(), a.getPlayer(), a.getColor(), a.isCheck());
                            //----------------------------------------------------------------------------------COME FERMARE STO TIMER?! (fermare timer e riattivarne un altro mettendolo a 2 secondi)
                            bufferRichieste.clear();
                            sm.updateGui();
                        }
                    } else if (endMancheTimer) {
                        sm.timeOut();
                        endRound();
                        endMancheTimer = false;
                        sm.updateGui();
                    } else {
                        //se è un azione permessa
                        if (sm.actionIsPermitted(a.getAzione(), a.getCarta(), a.getPlayer())) {
                            //se non è un interrompi chiamato quando non si può
                            if (!a.getAzione().equals("interrompi") || (a.getAzione().equals("interrompi") && sm.isInterruptable())) {
                                sm.action(a.getAzione(), a.getCarta(), a.getPlayer(), a.getColor(), a.isCheck());
                                sm.updateGui();
                                //se è uno scarta, interrompi o next passa alle interruzioni
                                if (a.getAzione().equals("scarta") || a.getAzione().equals("next") || a.getAzione().equals("interrompi")) {
                                    endRound();
                                } else if (a.getAzione().equals("pesca") && !sm.isAskingToClientScartaPescata()) {
                                    endRound();
                                }
                            }
                        }
                    }
                }*/
            } else {
                victory = true;
                closure = true;
            }
        }

        //--------------------------------------------------------------------------------------------to implement: closure and victory
    }

    private void endRound() {
        timerManche.stopTimer(); // -----------------------------------------------COME FERMARE STO TIMER?! (può essere già fermo nel caso in cui il metodo è chiamato in quanto il timer è terminato)
        timerManche = null;
        timerInterrupt = new Timer(this, 'i');
        timerInterrupt.start();
        interruptTimer = true;
    }

    public void aggiungiRichiesta(final Action a) {
        bufferRichieste.add(a);
    }

    void endInterruptTimer() {
        interruptTimer = false;
        newRound = true;
    }

    void endMancheTimer() {
        endMancheTimer = true;
    }

    //closure of the game
    void close() {
        closure = true;
    }

}
