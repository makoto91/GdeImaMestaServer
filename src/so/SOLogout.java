package so;

import db.DataBaseBroker;
import so.SOOpsta;

/**
 *
 * @author Matija Veljkovic
 */
public class SOLogout extends SOOpsta {

    private boolean izlogovan = false;

    @Override
    protected void proveriPreduslov(Object obj) throws Exception {
        //nema preduslova
    }

    @Override
    protected void izvrsiKonkretnuOperaciju(Object obj) throws Exception {
        String korisnik = (String) obj;
        String[] podaci = korisnik.split("/");
        DataBaseBroker.getINSTANCE().ulogujIzloguj(podaci[0], false, podaci[1]);
        izlogovan = true;
    }

    public boolean getRezultat() {
        return izlogovan;
    }

}
