
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Client extends Remote {

  void modificaTurno(boolean turno, Vincitore vincitore) throws RemoteException;

  void piazzaNavi() throws RemoteException;

  StatoCella controllaEsitoSpara(int x, int y) throws RemoteException;
}
