/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unoxtutti.domain;

/**
 *
 * @author mateusz
 */
public class Action {

    //azione can be: 'scarta', 'interrompi', 'checkBluff', 'pesca', 'uno', 'next'
    //carta is the number of the card in the list
    //color is the color of the jolly in case you played it. can be r=rosso b=blu, g=giallo, v=verde, n=no color (in case is not jolly)

    private final String azione;
    private final int carta;
    private final Player player;
    private final char color;
    private final boolean check;

    public Action(final String azione, final int carta, final char color, final Player player, final boolean check) {
        this.azione = azione;
        this.carta = carta;
        this.player = player;
        this.color = color;
        this.check = check;
    }

    public String getAzione() {
        return azione;
    }

    public int getCarta() {
        return carta;
    }

    public Player getPlayer() {
        return player;
    }

    public char getColor() {
        return color;
    }

    public boolean isCheck() {
        return check;
    }

}
