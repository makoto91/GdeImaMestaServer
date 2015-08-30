/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package so;

import db.DataBaseBroker;
import domen.AbstractDomainObject;
import domen.Korisnik;

/**
 *
 * @author Matija1
 */
public class SORegistracija extends SOOpsta{
    boolean rezultat = false;
    @Override
    protected void proveriPreduslov(Object obj) throws Exception {
        
    }

    @Override
    protected void izvrsiKonkretnuOperaciju(Object obj) throws Exception {
        Korisnik k = (Korisnik) obj;
        DataBaseBroker.getINSTANCE().kreirajNovi(k);
        rezultat = true;
    }
    public boolean getRezultat(){
        return rezultat;
    }
}
