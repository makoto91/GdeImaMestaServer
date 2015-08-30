package so;

import db.DataBaseBroker;

/**
 *
 * @author Matija Veljkovic
 */
public abstract class SOOpsta {
     
    public final void izvrsiOperaciju(Object obj) throws Exception  {
        try {
            DataBaseBroker.getINSTANCE().ucitajDrajver();
            DataBaseBroker.getINSTANCE().otvoriKonekciju();
            proveriPreduslov(obj);
            izvrsiKonkretnuOperaciju(obj);
            DataBaseBroker.getINSTANCE().commitTransakcije();
        } catch (Exception ex) {
            DataBaseBroker.getINSTANCE().rollbackTransakcije();
            throw ex;
        } finally {
            DataBaseBroker.getINSTANCE().zatvoriKonekciju();
        }
    }

    protected abstract void proveriPreduslov(Object obj) throws Exception;

    protected abstract void izvrsiKonkretnuOperaciju(Object obj) throws Exception;
    
}
