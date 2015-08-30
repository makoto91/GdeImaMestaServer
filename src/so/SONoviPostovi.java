/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package so;

import db.DataBaseBroker;
import domen.Post;
import java.util.List;

/**
 *
 * @author Matija1
 */
public class SONoviPostovi extends SOOpsta{
    boolean rezultat = false;
    @Override
    protected void proveriPreduslov(Object obj) throws Exception {
        
    }

    @Override
    protected void izvrsiKonkretnuOperaciju(Object obj) throws Exception {
        List<Post> listaLokala = (List<Post>) obj;
        for (int i = 0; i < listaLokala.size(); i++) {
            DataBaseBroker.getINSTANCE().kreirajNovi(listaLokala.get(i));
        }
//        DataBaseBroker.getINSTANCE().kreirajNovi(listaLokala);
        rezultat = true;
    }
    public boolean getRezultat(){
        return rezultat;
    }
}
