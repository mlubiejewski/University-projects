
public class Risultato implements java.io.Serializable {

  private final boolean turno;
  private final StatoCella stato;
  private final Vincitore vincitore;

  public Risultato(boolean t, StatoCella s, Vincitore v) {
    turno = t;
    stato = s;
    vincitore = v;
  }

  public boolean getTurno() {
    return turno;
  }

  public StatoCella getStatoCella() {
    return stato;
  }

  public Vincitore getVincitore() {
    return vincitore;
  }
}
