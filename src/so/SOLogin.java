package so;

import db.DataBaseBroker;
import domen.AbstractDomainObject;
import so.SOOpsta;

/**
 *
 * @author Matija Veljkovic
 */
public class SOLogin extends SOOpsta{

    private boolean ulogovan = false;
    AbstractDomainObject a;
    
    @Override
    protected void proveriPreduslov(Object obj) throws Exception {
        //nema preduslova
    }

    @Override
    protected void izvrsiKonkretnuOperaciju(Object obj) throws Exception {
        String korisnik = (String) obj;
        String[] podaci = korisnik.split("/");
        a = DataBaseBroker.getINSTANCE().validacija(podaci[0], podaci[1], podaci[2]);
    }
    
    public boolean getRezultat(){
        return ulogovan;
    }
    
    public AbstractDomainObject getRezultatAbs(){
        return a;
    }
    
}