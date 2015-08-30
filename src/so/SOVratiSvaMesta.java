package so;

import db.DataBaseBroker;
import domen.AbstractDomainObject;
import domen.Mesto;
import java.util.List;
import so.SOOpsta;

/**
 *
 * @author Matija Veljkovic
 */
public class SOVratiSvaMesta extends SOOpsta {

    List<AbstractDomainObject> ls;

    public List<AbstractDomainObject> getLs() {
        return ls;
    }

    @Override
    protected void proveriPreduslov(Object obj) throws Exception {
        //nema preduslova
    }

    @Override
    protected void izvrsiKonkretnuOperaciju(Object obj) throws Exception {
        ls = DataBaseBroker.getINSTANCE().selectSve((Mesto) obj);
    }
}
