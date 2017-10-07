
import java.net.*;
import java.util.*;
import java.rmi.*;
import java.rmi.server.*;
import javax.naming.*;

/*
 java -Djava.rmi.server.logCalls=true -Djava.rmi.server.codebase=http://127.0.0.1:8080/ -Djava.security.policy=server.policy ServerImpl
 */
class ServerImpl extends UnicastRemoteObject implements Server {

  public static final int NUM_NAVI = 5;
  private int piazzamento; //non dev'essere statico e pubblico
  Vincitore vincitore;

  private int numNaviAffondateGioc0;
  private int numNaviAffondateGioc1;
  private Client gioc0 = null;
  private Client gioc1 = null;
  private int turno;

  public ServerImpl() throws RemoteException {
    numNaviAffondateGioc0 = 0;
    numNaviAffondateGioc1 = 0;
    piazzamento = 0;
    vincitore = Vincitore.NESSUNO;
    try {
      if (System.getSecurityManager() == null) {
        System.setSecurityManager(new SecurityManager());
      }
      java.rmi.registry.LocateRegistry.createRegistry(2000); // crea registry che ascolta su una specifica porta
      Naming.rebind("rmi://127.0.0.1:2000/server", this); // il server registra l'oggetto remoto
    } catch (Exception e) {
      System.err.println("Failed to bind to RMI Registry" + e);
      System.exit(1);
    }
    System.out.println("Server attivo (compute)...");
  }

  @Override
  public synchronized void registra(String indirizzo) {
    try {
      if (gioc0 == null) {
        gioc0 = (Client) Naming.lookup(indirizzo);
        System.out.println("Giocatore 0 e' stato assengato dall'indirizzo " + indirizzo);
      } else if (gioc1 == null) {
        gioc1 = (Client) Naming.lookup(indirizzo);
        System.out.println("Giocatore 1 e' stato assengato dall'indirizzo " + indirizzo);
        new Thread(new RunnablePiazzaNavi(gioc0)).start();
        new Thread(new RunnablePiazzaNavi(gioc1)).start();
      }
    } catch (Exception e) {
      System.out.println(e);
    }
    System.out.println("Registra finished gioc0 =" + gioc0 + " and gioc1 =" + gioc1);
  }

  @Override
  public synchronized void finitoPiazzamento() {
    piazzamento++;
    System.out.println("piazzamento " + piazzamento);
    if (piazzamento == 2) {
      turno = 0;
      new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          gioc0.modificaTurno(true, Vincitore.NESSUNO);
        } catch (RemoteException err) {
          System.out.println(err);
          System.exit(1);
        }
       }
      }).start();
      try {
        gioc1.modificaTurno(false, Vincitore.NESSUNO);
      } catch (RemoteException err) {
        System.out.println(err);
        System.exit(1);
      }
    }
  }

  @Override
  public /*synchronized*/ Risultato spara(int x, int y) {
    StatoCella statoCella = null;
    Client clientModificaTurno;
    boolean turnoModificaTurno = true;

    try {
      if (turno == 0) {
        statoCella = gioc1.controllaEsitoSpara(x, y);
        if (statoCella.equals(StatoCella.AFFONDATO)) {
          numNaviAffondateGioc1++;
        }
        clientModificaTurno = gioc1;
        if (numNaviAffondateGioc1 == NUM_NAVI) {
          vincitore = Vincitore.GIOC0;
          turnoModificaTurno = false;
          //gioc1.modificaTurno(false, vincitore);
        } else {
          //gioc1.modificaTurno(true, vincitore);
        }
      } else {
        statoCella = gioc0.controllaEsitoSpara(x, y);
        if (statoCella.equals(StatoCella.AFFONDATO)) {
          numNaviAffondateGioc0++;
        }
        clientModificaTurno = gioc0;
        if (numNaviAffondateGioc0 == NUM_NAVI) {
          vincitore = Vincitore.GIOC1;
          turnoModificaTurno = false;
          //gioc0.modificaTurno(false, vincitore);
        } else {
          //gioc0.modificaTurno(true, vincitore);
        }
      }
      
      boolean pass = turnoModificaTurno;
      new Thread(new Runnable() {
        @Override
        public void run() {
          try {
            clientModificaTurno.modificaTurno(pass, vincitore);
          } catch (RemoteException err) {
            System.out.println(err);
            System.exit(1);
          }
        }
      }).start();
    } catch (RemoteException err) {
      System.out.println(err);
      System.exit(1);
    }
    turno = (turno + 1) % 2;
    System.out.println("mando statoCella a chi ha sparato: " + statoCella);
    return new Risultato(false, statoCella, vincitore);
  }

  public static void main(String[] args) {

    try {
      ServerImpl s = new ServerImpl();
      System.out.println("Server bound in registry");
    } catch (Exception e) {
      System.err.println("Failed to create ServerImpl object" + e.getMessage());
    }

  }
}

class RunnablePiazzaNavi implements Runnable {

  Client client;

  public RunnablePiazzaNavi(Client c) {
    client = c;
  }

  @Override
  public void run() {
    try {
      client.piazzaNavi();
    } catch (RemoteException err) {
      System.out.println(err);
      System.exit(1);
    }
  }
}
