
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Server extends Remote {

  Risultato spara(int x, int y) throws RemoteException;

  void registra(String indirizzo) throws RemoteException;

  void finitoPiazzamento() throws RemoteException;
}
