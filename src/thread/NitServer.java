package thread;

import java.net.ServerSocket;
import java.net.Socket;
import pl.Kontroler;

/**
 *
 * @author Matija Veljkovic
 */
public class NitServer extends Thread {

    private final int port;
    private ServerSocket ss;

    public NitServer(String brojPorta) {
        port = Integer.parseInt(brojPorta);
    }

    @Override
    public void run() {
        try {
            ss = new ServerSocket(port);
            Kontroler.getINSTANCE().ispisi("Uspesno pokrenut server na portu: " + port);

            while (!isInterrupted()) {
                Socket s = ss.accept();
                NitKlijent nk = new NitKlijent(s);
                nk.start();
            }

            ss.close();
        } catch (Exception ex) {
            interrupt();
            Kontroler.getINSTANCE().ispisi("Port " + port + " je zauzet.");
        }
    }

}
