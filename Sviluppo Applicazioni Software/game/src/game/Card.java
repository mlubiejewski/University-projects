/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unoxtutti.game;

import java.io.Serializable;

/**
 *
 * @author mateusz
 */
public class Card implements Serializable{

    //tipo can be b=carta base, a=carta azione, j = carta jolly.
    private final char tipo;
    //colore can be r=rosso b=blu, g=giallo, v=verde, n=no color
    //For jolly cards the color must be decided when the card have been thrown
    private char colore;
    //only for Base cards, -1 for others
    private final int numero;
    //If Carta Azione: effetto can be p=pesca due carte, c=cambio giro, s=salta turno
    //If carta jolly: //effetto can be j=jolly, 4=pesca 4 carte
    //If carta base: n=nessun effetto
    private final char effetto;

    public Card(final char tipo, final char colore, final int numero, final char effetto) {
        this.tipo = tipo;
        this.colore = colore;
        this.numero = numero;
        this.effetto = effetto;
    }

    //only for jolly cards
    public void setColor(final char colore) {
        this.colore = colore;
    }

    public char getEffetto() {
        return effetto;
    }

    public char getTipo() {
        return tipo;
    }

    public char getColore() {
        return colore;
    }

    public int getNumero() {
        return numero;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + this.tipo;
        hash = 89 * hash + this.colore;
        hash = 89 * hash + this.numero;
        hash = 89 * hash + this.effetto;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Card other = (Card) obj;
        if (this.tipo != other.tipo) {
            return false;
        }
        if (this.effetto != other.effetto) {
            return false;
        }
        if (tipo != 'j') {
            if (this.colore != other.colore) {
                return false;
            }
            if (this.numero != other.numero) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Carta");
        switch (tipo) {
            case 'b':
                sb.append(" base:");
                break;
            case 'a':
                sb.append(" azione:");
                break;
            case 'j':
                sb.append(" jolly:");
                break;
        }
        switch (colore) {
            case 'b':
                sb.append(" blu");
                break;
            case 'r':
                sb.append(" rossa");
                break;
            case 'v':
                sb.append(" verde");
                break;
            case 'g':
                sb.append(" gialla");
                break;
        }

        if (tipo == 'b') {
            sb.append(" ").append(numero);
        }
        if (tipo == 'a' || tipo == 'j') {
            switch (effetto) {
                case 'p':
                    sb.append(" pesca due carte");
                    break;
                case 'c':
                    sb.append(" inverti il senso del giro");
                    break;
                case 's':
                    sb.append(" salta turno");
                    break;
                case 'j':
                    sb.append(" jolly");
                    break;
                case '4':
                    sb.append(" pesca quattro carte");
                    break;
            }
        }

        return sb.toString();
    }

}
