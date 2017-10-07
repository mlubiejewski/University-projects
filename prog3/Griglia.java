
import java.util.Observable;

/**
 *
 * @author Utente
 */
public class Griglia extends Observable {

  /* variabili globali */
  String nome;    // nome contiene il nome della griglia, cioè se si riferisce al mio campo o quello dell'avversario
  StatoCella[][] matrice;

  public class Dati {

    int x;
    int y;
    int numAffondateAvversario;
    int numAffondateMio;

    public Dati(int r, int c, int numAffondateAvversario, int numAffondateMie) {
      this.x = r;
      this.y = c;
      this.numAffondateAvversario = numAffondateAvversario;
      this.numAffondateMio = numAffondateMie;
    }

    public int getRiga() {
      return x;
    }

    public int getColonna() {
      return y;
    }

    /* se il campo numAffondateAvversario non serve il valore sarà - 1*/
    public int numAffondateAvversario() {
      return this.numAffondateAvversario;
    }

    /* se il campo numAffondateMio non serve il valore sarà - 1*/
    public int numAffondateMio() {
      return this.numAffondateMio;
    }
  }

  public Griglia(String nome) {
    this.nome = nome;
    matrice = new StatoCella[VistaGioco.NUM_CELLE][VistaGioco.NUM_CELLE];
    this.inizializza();
  }

  /* restituisce il nome della griglia */
  public String getNome() {
    return this.nome;
  }

  /* inizializza la matrice*/
  private void inizializza() {
    for (int riga = 0; riga < VistaGioco.NUM_CELLE; riga++) {
      for (int colonna = 0; colonna < VistaGioco.NUM_CELLE; colonna++) {
        matrice[riga][colonna] = StatoCella.MARE;
      }
    }
  }

  /* modifica lo stato della cella e notifica observer*/
  public void setCella(StatoCella stato, int r, int c, int numAffondateAvversario, int numAffondateMie) {
    System.out.println("sono in setCella");
    matrice[r][c] = stato;
    this.setChanged();
    this.notifyObservers(new Dati(r, c, numAffondateAvversario, numAffondateMie));
  }

  /* reistituisce la cella    */
  public StatoCella getCella(int r, int c) {
    return matrice[r][c];
  }
}
