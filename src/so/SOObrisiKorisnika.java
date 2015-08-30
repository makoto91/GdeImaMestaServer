/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package so;

import db.DataBaseBroker;
import domen.Korisnik;

/**
 *
 * @author Matija1
 */
public class SOObrisiKorisnika extends SOOpsta{
    private boolean rezultat = false;

    public boolean getRezultat() {
        return rezultat;
    }

    @Override
    protected void proveriPreduslov(Object obj) throws Exception {
        //admin ulogovan
    }

    @Override
    protected void izvrsiKonkretnuOperaciju(Object obj) throws Exception {
        try {
            DataBaseBroker.getINSTANCE().obrisi((Korisnik) obj);
            rezultat = true;
        } catch (Exception e) {
            throw e;
        }
    }
}
