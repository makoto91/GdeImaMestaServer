/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import domen.Korisnik;
import domen.Mesto;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Matija1
 */
public class DBKonekcija {
    Connection konekcija;
    
    private void ucitajDrajver(String subp) throws ClassNotFoundException{
        Class.forName(subp);
    }
    
    public void otvoriBazu(String subp){
        try {
            ucitajDrajver(subp);
            konekcija = DriverManager.getConnection("jdbc:mysql://localhost:3306/projekatPS", "root", "");
            konekcija.setAutoCommit(false);
        } catch (ClassNotFoundException ex) {
            System.out.println("Nije ucitan drajver");
        } catch (SQLException ex) {
            System.out.println("Nije uspostavljena konekcija");
        }
    }
    
    public List<Korisnik> vratiKorisnike(){
        List<Korisnik> listaKorisnika = new ArrayList<>();
        
        
        String upit = "Select * from korisnici left join mesto on(korisnici.mesto = mesto.ptt)";
        try {
            Statement st = konekcija.createStatement();
            
            ResultSet rs = st.executeQuery(upit);
            while(rs.next()){
                String imePrezime = rs.getString("imePrezime");
                String email = rs.getString("email");
                String adresa = rs.getString("adresa");
                String sifra = rs.getString("sifra");
                int sifraMesta = rs.getInt("mesto");
                long ptt = rs.getLong("ptt");
                String nazivMesta = rs.getString("mesto.naziv");
                int uloga = rs.getInt("uloga");
//                Mesto m = vratiMesto(sifraMesta);
                Mesto m = new Mesto(ptt, nazivMesta);
                Korisnik k = new Korisnik(imePrezime, email, sifra, m, adresa, uloga);
                listaKorisnika.add(k);
//                System.out.println("ime: "+imePrezime+" email: "+email+" adresa: "+adresa);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBKonekcija.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return listaKorisnika;
    }

    private Mesto vratiMesto(int ptt) {
        String upit = "Select naziv from mesto where ptt = "+ptt;
        Statement st;
        Mesto m = new Mesto();
        
        try {
            st = konekcija.createStatement();
            ResultSet rs = st.executeQuery(upit);
            while(rs.next()){
                String naziv = rs.getString("naziv");
                m.setNaziv(naziv);
                m.setPtt(ptt);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBKonekcija.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return m;
    }
    
    
}
