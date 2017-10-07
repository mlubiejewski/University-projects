
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import javax.swing.JOptionPane;
import javax.swing.*;

/*
 * java -Djava.rmi.server.codebase=http://127.0.0.1:8081/ -Djava.security.policy=client.policy Client
 */
public class Controller extends UnicastRemoteObject implements MouseListener, Client {

  Server server;
  final static int NUMERO_NAVI = 5;    // contiene il numero di navi da decidere
  static String NOME_MIO_CAMPO = "Me";
  static String NOME_AVVERSARIO_CAMPO = "Avversario";
  private int numNaviRimanentiDaPiazzare;

  Griglia grigliaMio;
  Griglia grigliaAvversario;
  JLabel vistaTurno;

  boolean turno = false;  // gestisce il turno del giocatore
  Vincitore vincitore = Vincitore.NESSUNO;    // variabile che tiene il riferimento al vincitore. Inizip è nessuno
  int numAffondateMie = 0;    // tiene conto delle navi mie che sono state affondate
  int numAffondateAvversario = 0;    // tiene conto delle navi avversario che ho affondato

  // non ho gestito add listener e remove lister ho usato una variabile per gestire il click sul pannello
    /* inizialmente saranno entrambe false quando ci sarà pizza navi il clickMio prenderà il valore true e potrò piazzare sulle navi, alla fine lo disabilito mettendo a false  */
  boolean clickMio = false;

  /* quando inizia il gioco il click avversario prenderà il valore true   */
  boolean clickAvversario = false;

  public Controller(Griglia modelloMio, Griglia modelloAvversario, String indirizzo, String porta) throws RemoteException {
    this.grigliaMio = modelloMio;
    this.grigliaAvversario = modelloAvversario;
    numNaviRimanentiDaPiazzare = NUMERO_NAVI;
    int p = Integer.parseInt(porta);
    initRemote(indirizzo, p);
  }

  @Override
  /*synchronized*/ public void mouseClicked(MouseEvent e) {
    System.out.println("mouseclicked");
    JLabel tmp = (JLabel) e.getSource();
    System.out.println(tmp.getName() + " " + tmp.getText() + " " + clickMio);
    int x = tmp.getY() / VistaGioco.DIM_CELLA;    // prendo il riferimento della riga
    int y = tmp.getX() / VistaGioco.DIM_CELLA;    // prendo il riferimento della colonna

    /* se ho cliccato sul mio campo e il server ha invocato il metodo piazzaNavi() allora posso posizionare le navi */
    if (tmp.getName().equals(NOME_MIO_CAMPO) && clickMio) {
      if (numNaviRimanentiDaPiazzare > 0 && !grigliaMio.getCella(x, y).equals(StatoCella.NAVE)) {
        grigliaMio.setCella(StatoCella.NAVE, x, y, numAffondateAvversario, numAffondateMie);//-1 -1
        System.out.println("Hai cliccato su: riga " + x + " colonna " + y + " " + tmp.getName());
        numNaviRimanentiDaPiazzare--;
        if (numNaviRimanentiDaPiazzare == 0) {
          JOptionPane.showMessageDialog(null, "Posizionate tutte le navi");
          clickMio = false;
          turno = false;
          clickAvversario = true;
          vistaTurno.setText("In attesa dell'avversario");
          new Thread(new Runnable() {
            @Override
            public void run() {
              try {
                server.finitoPiazzamento();
              } catch (RemoteException err) {
                System.out.println(err);
                System.exit(1);
              }
            }
          }).start();
        }
      } else if (grigliaMio.getCella(x, y).equals(StatoCella.NAVE)) {
        JOptionPane.showMessageDialog(null, "Già posizionato nave in questa cella");
      }
      /* se ho cliccato sul campo avversario invoco il metodo spara del server    */
    } else if (tmp.getName().equals(NOME_AVVERSARIO_CAMPO) && turno && clickAvversario) {
      if (grigliaAvversario.getCella(x, y).equals(StatoCella.MARE)) {
        turno = false;
        new Thread(new Runnable() {
          @Override
          public void run() {
            try {
              controllaRisultatoDellaMiaSpara(server.spara(x, y), x, y);
            } catch (RemoteException err) {
              System.out.println(err);
              System.exit(1);
            }
          }
        }).start();
      }
    } else if (tmp.getName().equals(NOME_AVVERSARIO_CAMPO) && !turno && clickAvversario) {
      JOptionPane.showMessageDialog(null, "Non e' il tuo turno!");
    }
  }

  void setVistaTurno(JLabel label) {
    vistaTurno = label;
    vistaTurno.setText("In attesa di un'altro giocatore");
  }

  private /*synchronized*/ void controllaRisultatoDellaMiaSpara(Risultato risultato, int x, int y) {
    StatoCella statoRisultato = risultato.getStatoCella();
    System.out.println("got the result back " + statoRisultato + " and vincitore is: " + risultato.getVincitore()
          + " and turno is " + risultato.getTurno());
    if (statoRisultato.equals(StatoCella.AFFONDATO)) {
      numAffondateAvversario++;
    }

    turno = risultato.getTurno();
    vincitore = risultato.getVincitore();

    grigliaAvversario.setCella(statoRisultato, x, y, numAffondateAvversario, numAffondateMie);//-1 anziche' numAffondateMie

    vistaTurno.setText("Turno: avversario");

    if (vincitore != Vincitore.NESSUNO) {
      fineGioco();
    }
  }

  private void initRemote(String indirizzo, int porta) {
    if (System.getSecurityManager() == null) {
      System.setSecurityManager(new SecurityManager());
    }
    try {
      java.rmi.registry.LocateRegistry.createRegistry(porta);
      Naming.rebind("rmi://" + indirizzo + ":" + porta + "/client", this);
      System.out.println("I'm bound");
    } catch (Exception err) {
      System.out.println(err);
      System.exit(1);
    }
    try {
      System.out.println("about to connect to server");
      server = (Server) Naming.lookup("rmi://127.0.0.1:2000/server"); // 192.168.43.29
      System.out.println("Server found");
      server.registra("rmi://" + indirizzo + ":" + porta + "/client");
    } catch (Exception err) {
      System.out.println(err);
      System.exit(1);
    }
  }

  public void fineGioco() {
    clickAvversario = false;
    System.out.println("numero navi affundante avversario " + numAffondateAvversario + " e NUM_NAVI " + NUMERO_NAVI);
    if (numAffondateAvversario == NUMERO_NAVI) {
      JOptionPane.showMessageDialog(null, "Hai vinto!");
      vistaTurno.setText("Gioco finito. Hai vinto tu.");
    } else {
      JOptionPane.showMessageDialog(null, "Non hai vinto!");
      vistaTurno.setText("Gioco finito. Hai perso.");
    }
  }

  @Override
  /*synchronized*/ public void modificaTurno(boolean turno, Vincitore vincitore) {
    this.turno = turno;
    this.vincitore = vincitore;
    /*if (turno) {
     JOptionPane.showMessageDialog(null, "Puoi sparare"); //secondo me non e' necessario
     }*/
    if (vincitore != Vincitore.NESSUNO) {
      fineGioco();
    } else {
      if (turno) {
        vistaTurno.setText("Turno: mio");
      } else {
        vistaTurno.setText("Turno: avversario");
      }
    }
  }

  @Override /* finestra di poput che dice che si può avvire le navi*/

  /*synchronized*/ public void piazzaNavi() {
    System.out.println("piazzaNavi");
    clickMio = true;
    JOptionPane.showMessageDialog(null, "Puoi iniziare a piazzare " + this.NUMERO_NAVI + " navi");
    vistaTurno.setText("Piazza le navi!");
  }

  @Override
  /*synchronized*/public StatoCella controllaEsitoSpara(int x, int y) {
    StatoCella stato = grigliaMio.getCella(x, y);
    if (StatoCella.NAVE.equals(stato)) {
      numAffondateMie++;
      grigliaMio.setCella(StatoCella.AFFONDATO, x, y, numAffondateAvversario, numAffondateMie);//-1 anziche' numAffondateAvv
    } else if (StatoCella.MARE.equals(stato)) {
      grigliaMio.setCella(StatoCella.MANCATO, x, y, numAffondateAvversario, numAffondateMie);
    }
    System.out.println("controllaEsitoSpara: Mando questo al server: " + grigliaMio.getCella(x, y));
    return grigliaMio.getCella(x, y);
  }

  @Override
  public void mousePressed(MouseEvent e) {
  }

  @Override
  public void mouseReleased(MouseEvent e) {
  }

  @Override
  public void mouseEntered(MouseEvent e) {
  }

  @Override
  public void mouseExited(MouseEvent e) {
  }
}
