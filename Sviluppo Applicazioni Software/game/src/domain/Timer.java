/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unoxtutti.domain;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author joana
 */
public class Timer extends Thread {

    private static final int INTERRUPT_TIME = 2;
    private static final int MANCHE_TIME = 10;

    private final char type; //i for interrupt, m for manche
    private final ServerMancheHelper smh;
    private boolean stop;

    public Timer(final ServerMancheHelper smh, final char type) {
        this.type = type;
        this.smh = smh;
    }

    @Override
    public void run() {
        try {
            if (type == 'i') {
                sleep(INTERRUPT_TIME);
                smh.endInterruptTimer();
            }
            else {
                sleep(MANCHE_TIME);
                smh.endMancheTimer();
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Timer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void stopTimer() {
        stop = true;
    }

}
