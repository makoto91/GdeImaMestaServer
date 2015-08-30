package thread;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;

/**
 *
 * @author Matija Veljkovic
 */
public class NitSat implements Runnable {

    JLabel jlbl;

    public NitSat(JLabel jlblVreme) {
        jlbl = jlblVreme;
    }

    @Override
    public void run() {
        SimpleDateFormat sdf = new SimpleDateFormat("H:mm:ss");
        while (true) {
            jlbl.setText(sdf.format(new Date()));
            try {
                Thread.sleep(999);
            } catch (InterruptedException ex) {
            }
        }
    }

}
