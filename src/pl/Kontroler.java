package pl;

import so.*;
import db.DataBaseBroker;
import db.Util;
import domen.*;
import domen.AbstractDomainObject;
import domen.Mesto;
import forme.GlavnaServer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import thread.NitKlijent;

/**
 *
 * @author Matija Veljkovic
 */
public class Kontroler {

    private static Kontroler INSTANCE;

    private final Map<String, Object> mapa;
    private final List<NitKlijent> onlineKlijenti;

    public static Kontroler getINSTANCE() {
        if (INSTANCE == null) {
            try {
                INSTANCE = new Kontroler();
            } catch (Exception ex) {

            }
        }
        return INSTANCE;
    }

    private Kontroler() throws Exception {
        mapa = new HashMap<>();
        onlineKlijenti = new ArrayList<>();
    }

    public Object vratiIzMape(String key) {
        return mapa.get(key);
    }

    public void staviUMapu(String key, Object value) {
        mapa.put(key, value);
    }

    public void ispisi(String zahtev) {
//        GlavnaServer gs = (GlavnaServer) mapa.get("forma");
        System.out.println(zahtev);
    }

    public void noviKorisnik(NitKlijent nk) {
        onlineKlijenti.add(nk);
    }

    public void izasaoKorisnik(NitKlijent nk) {
        onlineKlijenti.remove(nk);
    }

    public List<NitKlijent> getOnlineKlijenti() {
        return onlineKlijenti;
    }

//______________deo vezan za pomocne________________________________________________________
    public AbstractDomainObject login(String kor) throws Exception {
        SOLogin so = new SOLogin();
        so.izvrsiOperaciju(kor);
        return so.getRezultatAbs();
    }

    public boolean logout(String admin) throws Exception {
        SOLogout so = new SOLogout();
        so.izvrsiOperaciju(admin);
        return so.getRezultat();
    }

    public Map<String, Object> vratiPodatkeOBazi(Object obj) {
        String baza = (String) obj;
        return Util.getINSTANCE().vratiPodatkeOBazi(baza);
    }
    
    public String vratiIzabranuBazu(){
        return Util.getINSTANCE().vratiIzabranuBazu();
    }

    public void promeniBazu(Object baza) throws Exception {
        String b = (String) baza;
        Util.getINSTANCE().postaviNovuBazu(b);
        DataBaseBroker.getINSTANCE().postavljenaJeNovaBaza();
    }

    public void novaBaza(String url, String user, String pass, String driver) throws Exception {
        Util.getINSTANCE().postaviNovuBazu(url, user, pass, driver);
        DataBaseBroker.getINSTANCE().postavljenaJeNovaBaza();
    }

//______________deo vezan za uloge__________________________________________________________
//    public List<AbstractDomainObject> vratiUlogeZaKriterijum(Object kriterijum) throws Exception {
//        SOVratiUloge so = new SOVratiUloge();
//        so.izvrsiOperaciju(kriterijum);
//        return so.getRezultat();
//    }
//
//    public boolean novaUloga(Object kriterijum) throws Exception {
//        SONovaUloga so = new SONovaUloga();
//        so.izvrsiOperaciju(kriterijum);
//        return so.getRezultat();
//    }
//
    public boolean izmenaLokala(Object kriterijum) throws Exception {
        SOIzmenaLokala so = new SOIzmenaLokala();
        so.izvrsiOperaciju(kriterijum);
        return so.getRezultat();
    }

    public boolean brisiLokal(Lokal lokal) throws Exception {
        SOObrisiLokal so = new SOObrisiLokal();
        so.izvrsiOperaciju(lokal);
        return so.getRezultat();
    }
//
////______________deo vezan za licence__________________________________________________________
//    public boolean novaLicenca(Licenca licenca) throws Exception {
//        SONovaLicenca so = new SONovaLicenca();
//        so.izvrsiOperaciju(licenca);
//        return so.getRezultat();
//    }
//
//    public List<AbstractDomainObject> vratiLicenceZaKriterijum(Object parametar) throws Exception {
//        SOVratiLicence so = new SOVratiLicence();
//        so.izvrsiOperaciju(parametar);
//        return so.getRezultat();
//    }
//
//    public boolean izmenaLicence(Object kriterijum) throws Exception {
//        SOIzmenaLicence so = new SOIzmenaLicence();
//        so.izvrsiOperaciju(kriterijum);
//        return so.getRezultat();
//    }
//
//    public boolean brisiLicencu(Licenca l) throws Exception {
//        SOObrisiLicencu so = new SOObrisiLicencu();
//        so.izvrsiOperaciju(l);
//        return so.getRezultat();
//    }

//______________deo vezan za avione________________________________________________________
    public List<AbstractDomainObject> ucitajMesta() throws Exception {
        SOVratiSvaMesta so = new SOVratiSvaMesta();
        so.izvrsiOperaciju(new Mesto());
        return so.getLs();
    }

    public boolean kreirajNoveLokale(List<Lokal> listaLokala) throws Exception {
        SOKreirajNoveLokale so = new SOKreirajNoveLokale();
        so.izvrsiOperaciju(listaLokala);
        return so.getRezultat();
    }

    public boolean izmeniKorisnika(Korisnik korisnik) throws Exception {
        SOIzmeniKorisnika so = new SOIzmeniKorisnika();
        so.izvrsiOperaciju(korisnik);
        return so.getRezultat();
    }
//
    public boolean obrisiKorisnika(Korisnik k) throws Exception {
        SOObrisiKorisnika so = new SOObrisiKorisnika();
        so.izvrsiOperaciju(k);
        return so.getRezultat();
    }
//
//    public List<AbstractDomainObject> vratiTipoveAviona() throws Exception {
//        SOVratiTipoveAviona so = new SOVratiTipoveAviona();
//        so.izvrsiOperaciju(new TipAviona());
//        return so.getLs();
//    }
//
////______________deo vezan za zaposlene________________________________________________________
    public List<AbstractDomainObject> ucitajTipove() throws Exception {
        SOVratiSveTipove so = new SOVratiSveTipove();
        so.izvrsiOperaciju(new TipLokala());
        return so.getLs();
    }

    public boolean noviPostovi(ArrayList<AbstractDomainObject> zap) throws Exception {
        SONoviPostovi so = new SONoviPostovi();
        so.izvrsiOperaciju(zap);
        return so.getRezultat();
    }

//    public List<Zaposleni> vratiSveZaposlene() throws Exception {
//        SOVratiSveZaposlene so = new SOVratiSveZaposlene();
//        so.izvrsiOperaciju(new Zaposleni());
//        return so.getRezultat();
//    }
//
//    public List<AbstractDomainObject> vratiPiloteZaKriterijum(Object parametar) throws Exception {
//        SOVratiPilotePoKriterijumu so = new SOVratiPilotePoKriterijumu();
//        so.izvrsiOperaciju(parametar);
//        return so.getRezultat();
//    }
//
//    public List<AbstractDomainObject> vratiMehanicareZaKriterijum(Object parametar) throws Exception {
//        SOVratiMehanicarePoKriterijumu so = new SOVratiMehanicarePoKriterijumu();
//        so.izvrsiOperaciju(parametar);
//        return so.getRezultat();
//    }
//
//    public List<AbstractDomainObject> vratiZaposleneZaKriterijum(Object parametar) throws Exception {
//        SOVratiZaposlenePoKriterijumu so = new SOVratiZaposlenePoKriterijumu();
//        so.izvrsiOperaciju(parametar);
//        return so.getRezultat();
//    }
//
//    public boolean izmenaPilota(Pilot pilot) throws Exception {
//        SOIzmenaPilota so = new SOIzmenaPilota();
//        so.izvrsiOperaciju(pilot);
//        return so.getRezultat();
//    }
//
//    public boolean izmenaZaposlenog(Zaposleni zaposleni) throws Exception {
//        SOIzmenaZaposlenog so = new SOIzmenaZaposlenog();
//        so.izvrsiOperaciju(zaposleni);
//        return so.getRezultat();
//    }
//
//    public boolean izmenaMehanicara(AvioMehanicar avioMehanicar) throws Exception {
//        SOIzmenaMehanicara so = new SOIzmenaMehanicara();
//        so.izvrsiOperaciju(avioMehanicar);
//        return so.getRezultat();
//    }
//
//    public boolean obrisiZaposlenog(Zaposleni zaposleni) throws Exception {
//        SOBrisiZaposlenog so = new SOBrisiZaposlenog();
//        so.izvrsiOperaciju(zaposleni);
//        return so.getRezultat();
//    }
//
//    public boolean obrisiPilota(Pilot pilot) throws Exception {
//        SOBrisiPilota so = new SOBrisiPilota();
//        so.izvrsiOperaciju(pilot);
//        return so.getRezultat();
//    }
//
//    public boolean obrisiMehanicara(AvioMehanicar avioMehanicar) throws Exception {
//        SOBrisiMehanicara so = new SOBrisiMehanicara();
//        so.izvrsiOperaciju(avioMehanicar);
//        return so.getRezultat();
//    }

    public boolean registracija(Korisnik k)throws Exception {
        SORegistracija so = new SORegistracija();
        so.izvrsiOperaciju(k);
        return so.getRezultat();
    }

    public List<AbstractDomainObject> ucitajPostove() throws Exception {
        SOVratiSvePostove so = new SOVratiSvePostove();
        so.izvrsiOperaciju(new Post());
        return so.getLs();
    }

    public List<AbstractDomainObject> ucitajKorisnike() throws Exception {
        SOVratiSveKorisnike so = new SOVratiSveKorisnike();
        so.izvrsiOperaciju(new Korisnik());
        return so.getLs();
    }
    
    public List<AbstractDomainObject> ucitajLokale() throws Exception {
        SOVratiSveLokale so = new SOVratiSveLokale();
        so.izvrsiOperaciju(new Lokal());
        return so.getLs();
    }

}
