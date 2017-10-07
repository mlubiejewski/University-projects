
import java.rmi.RemoteException;
import javax.swing.JFrame;

/**
 *
 * @author Utente
 */
public class BattagliaNavale {

  public static void main(String args[]) throws RemoteException {
    JFrame frame = new JFrame("Battaglia Navale");
    frame.setSize(800, 700);

    Griglia modelloMio = new Griglia("modelloMio");
    Griglia modelloAvversario = new Griglia("modelloAvversario");

    Controller controllo = new Controller(modelloMio, modelloAvversario, args[0], args[1]);
    VistaGioco vista = new VistaGioco(controllo);
    modelloMio.addObserver(vista.me);
    modelloAvversario.addObserver(vista.avversario);

    frame.add(vista);
    frame.setVisible(true);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setResizable(false);
  }
}
