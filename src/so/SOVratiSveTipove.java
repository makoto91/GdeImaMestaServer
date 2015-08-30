/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package so;

import db.DataBaseBroker;
import domen.AbstractDomainObject;
import domen.TipLokala;
import java.util.List;

/**
 *
 * @author Matija1
 */
public class SOVratiSveTipove extends SOOpsta{
    List<AbstractDomainObject> ls;

    public List<AbstractDomainObject> getLs() {
        return ls;
    }
    
    @Override
    protected void proveriPreduslov(Object obj) throws Exception {
        
    }

    @Override
    protected void izvrsiKonkretnuOperaciju(Object obj) throws Exception {
        ls = DataBaseBroker.getINSTANCE().selectSve((TipLokala) obj);
    }
}
