
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Mateusz Lubiejewski
 * @author Mario Lacaj
 * @author Saimir Pasho
 *
 */
public class VistaGioco extends JPanel {

  static final int NUM_CELLE = 11;
  static final int DIM_CAMPO = 385;
  static final int DIM_CELLA = 35;
  static final int SPAZIO_TRA_CAMPO = 10;
  static final int SPAZIO_TRA_CELLA = 0;
  static final String NOME_MIO_MODELLO = "modelloMio";
  static final String NOME_AVVERSARIO_MODELLO = "modelloAvversario";

  Campo me;
  Campo avversario;

  JLabel infoMe1, infoMe2, infoMe3;
  JLabel infoAvversario1, infoAvversario2, infoAvversario3;
  JLabel turno;

  ImageIcon mare;
  ImageIcon nave;
  ImageIcon naveAffondata;
  ImageIcon mancato;

  Controller controllo;
  int numNavi;

  public VistaGioco(Controller controllo) {

    this.controllo = controllo;

    
    mare = new ImageIcon("mare.jpg");
    nave = new ImageIcon("nave.jpg");
    naveAffondata = new ImageIcon("affondato.jpg");
    mancato = new ImageIcon("mancato.jpg");

    /* usato un flow layout così il pannello campo ha una dimesione fissa e mi funziona getX e getY */
    FlowLayout flowlayout = new FlowLayout();
    this.setLayout(flowlayout);
    flowlayout.setHgap(SPAZIO_TRA_CAMPO);
    flowlayout.setVgap(SPAZIO_TRA_CAMPO);

    me = new Campo(Controller.NOME_MIO_CAMPO);
    avversario = new Campo(Controller.NOME_AVVERSARIO_CAMPO);

    infoMe1 = new JLabel();
	infoMe2 = new JLabel();
	infoMe3 = new JLabel();
    infoMe1.setText("Info Giocatore1:");
	infoMe2.setText("Navi rimanenti: " + Controller.NUMERO_NAVI);
	infoMe3.setText("Navi affondate: 0");
    infoMe1.setPreferredSize(new Dimension(385, 20));
    infoMe1.setHorizontalAlignment(JLabel.CENTER);
	infoMe2.setPreferredSize(new Dimension(385, 20));
    infoMe2.setHorizontalAlignment(JLabel.CENTER);
	infoMe3.setPreferredSize(new Dimension(385, 20));
    infoMe3.setHorizontalAlignment(JLabel.CENTER);

    infoAvversario1 = new JLabel();
	infoAvversario2 = new JLabel();
	infoAvversario3= new JLabel();
	infoAvversario1.setText("Info Giocatore2:");
	infoAvversario2.setText("Navi rimanenti: " + Controller.NUMERO_NAVI);
	infoAvversario3.setText("Navi affondate: 0");
    infoAvversario1.setPreferredSize(new Dimension(385, 20));
    infoAvversario1.setHorizontalAlignment(JLabel.CENTER);
	infoAvversario2.setPreferredSize(new Dimension(385, 20));
    infoAvversario2.setHorizontalAlignment(JLabel.CENTER);
	infoAvversario3.setPreferredSize(new Dimension(385, 20));
    infoAvversario3.setHorizontalAlignment(JLabel.CENTER);
    turno = new JLabel();
    turno.setText("");
    controllo.setVistaTurno(turno);

    this.add(me);
    this.add(avversario);
    this.add(infoMe1);
	this.add(infoAvversario1);
	this.add(infoMe2);
	this.add(infoAvversario2);
	this.add(infoMe3);
    this.add(infoAvversario3);
    this.add(turno);
    numNavi = Controller.NUMERO_NAVI;
  }

  public class Campo extends JPanel implements Observer {

    JLabel matrice[][];
    String campoNome;

    public Campo(String nome) {

      this.campoNome = nome;
      this.setPreferredSize(new Dimension(VistaGioco.DIM_CAMPO, VistaGioco.DIM_CAMPO));

      matrice = new JLabel[NUM_CELLE][NUM_CELLE];

      /* impostato il layout grid con 10 x 10 e distanza tra celle 0  */
      GridLayout gridlayout = new GridLayout(NUM_CELLE, NUM_CELLE, SPAZIO_TRA_CELLA, SPAZIO_TRA_CELLA);
      this.setLayout(gridlayout);

      for (int riga = 0; riga < NUM_CELLE; riga++) {
        for (int col = 0; col < NUM_CELLE; col++) {
		  //Per far si che nella griglia le colonne vengono stampate in lettere
		  int letter = col + 64;
		  char ascii = (char)letter;
		  String print ="";
		  print+=ascii;
		  
          if (riga == 0) {
            JLabel label = new JLabel();
            label.setPreferredSize(new Dimension(DIM_CELLA, DIM_CELLA));
            if (col != 0) {
				label.setText(print);
            }

            label.setHorizontalAlignment(JLabel.CENTER);
            this.add(label);

          } else if (col == 0) {
            JLabel label = new JLabel();
            label.setPreferredSize(new Dimension(DIM_CELLA, DIM_CELLA));
            label.setText(Integer.toString(riga));
            label.setHorizontalAlignment(JLabel.CENTER);
            this.add(label);

          } else {
            matrice[riga][col] = new JLabel();
            matrice[riga][col].setName(campoNome);
            matrice[riga][col].setPreferredSize(new Dimension(DIM_CELLA, DIM_CELLA));
            matrice[riga][col].setBorder(BorderFactory.createLineBorder(Color.black));
            matrice[riga][col].setToolTipText(riga + " " + print);
            matrice[riga][col].setIcon(mare);
            matrice[riga][col].addMouseListener(controllo);
            this.add(matrice[riga][col]);
          }
        }
      }
    }

    @Override
    public void update(Observable o, Object arg) {

      Griglia griglia = (Griglia) o;
      String nomeGriglia = griglia.getNome();

      Griglia.Dati dati = (Griglia.Dati) arg;
      int riga = dati.getRiga();
      int colonna = dati.getColonna();
	  int letter = colonna + 64;
	  char ascii = (char)letter;
	  String print ="";
	  print+=ascii;
      int numNaviAffondateAvversario = dati.numAffondateAvversario();
      int numNaviAffondateMio = dati.numAffondateMio();

      System.out.println("Sono in vista gioco " + riga + "  " + print);

      if (nomeGriglia.equals(NOME_MIO_MODELLO)) {

        if (griglia.matrice[riga][colonna].equals(StatoCella.NAVE)) {
          matrice[riga][colonna].setIcon(nave);
        } else if (griglia.matrice[riga][colonna].equals(StatoCella.MANCATO)) {
          matrice[riga][colonna].setIcon(mancato);
        } else if (griglia.matrice[riga][colonna].equals(StatoCella.AFFONDATO)) {
          matrice[riga][colonna].setIcon(naveAffondata);
          infoMe1.setText("Info Giocatore1:");
		  infoMe2.setText("Navi rimanenti: " + (numNavi - numNaviAffondateMio));
		  infoMe3.setText("Navi affondate: " + numNaviAffondateMio);
        }

      } else if (nomeGriglia.equals(NOME_AVVERSARIO_MODELLO)) {

        if (griglia.matrice[riga][colonna].equals(StatoCella.NAVE)) {
          matrice[riga][colonna].setIcon(nave);
        } else if (griglia.matrice[riga][colonna].equals(StatoCella.MANCATO)) {
          matrice[riga][colonna].setIcon(mancato);
        } else if (griglia.matrice[riga][colonna].equals(StatoCella.AFFONDATO)) {
          matrice[riga][colonna].setIcon(naveAffondata);
		  infoAvversario1.setText("Info Giocatore2:");
		  infoAvversario2.setText("Navi rimanenti: " + (numNavi - numNaviAffondateAvversario));
	      infoAvversario3.setText("Navi affondate: " + numNaviAffondateAvversario);
		}
      }
    }
  }
}
