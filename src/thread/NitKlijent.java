package thread;

import domen.*;

import domen.AbstractDomainObject;
import java.io.IOException;
import util.Akcija;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import pl.Kontroler;
import util.TransferKlasa;

/**
 *
 * @author Matija Veljkovic
 */
public class NitKlijent extends Thread {

    private final Socket soket;
    private final StringBuilder zahtevi;
    AbstractDomainObject a;
    private Korisnik k;
    private Lokal l;

    NitKlijent(Socket s) {
        soket = s;
        zahtevi = new StringBuilder();
    }

    public StringBuilder getZahtevi() {
        return zahtevi;
    }

    @Override
    public String toString() {
        if (a != null) {
            return a.ispisi();
        } else {
            return "[ Neulogovan ]";
        }

    }

    @Override
    public void run() {
        try {
            Kontroler.getINSTANCE().noviKorisnik(this);
            Kontroler.getINSTANCE().ispisi("Klijent: [" + soket.getInetAddress() + "] se uspesno povezao.");
            obradjujKlijenta();
            Kontroler.getINSTANCE().ispisi("Klijent: [" + soket.getInetAddress() + "] se odjavio.");
            Kontroler.getINSTANCE().izasaoKorisnik(this);
            soket.close();
        } catch (Exception ex) {
            Kontroler.getINSTANCE().ispisi("Pri obradi zahteva kod klijenta: [" + soket.getInetAddress()
                    + "]\n\tdoslo je do greske: " + ex.getMessage());
        }
    }

    private void obradjujKlijenta() throws Exception{
        while (!isInterrupted()) {
            ObjectInputStream input = null;
            TransferKlasa zahtev = null;
            TransferKlasa odgovor = null;
            try {
                input = new ObjectInputStream(soket.getInputStream());
                zahtev = (TransferKlasa) input.readObject();
                odgovor = new TransferKlasa();
                //            try {
                
                Kontroler.getINSTANCE().ispisi("Klijent: [" + soket.getInetAddress()
                        + "] poslao zahtev: " + zahtev.getOperacija());
                zahtevi.append("ZAHTEV:\t").append(zahtev.getOperacija()).append("\n");
                switch (zahtev.getOperacija()) {
                    case Akcija.KRAJ:
                        zahtevi.append("Klijent se odjavio.");
                        interrupt();
                        return;
                    case Akcija.LOGIN:
                        a = Kontroler.getINSTANCE().login(zahtev.getParametar().toString());
//                        if (a != null) {
                            odgovor.setRezultat(a);
//                        }
//                       
                        break;
                    case Akcija.REGISTRACIJA:
                        boolean rez = Kontroler.getINSTANCE().registracija((Korisnik) zahtev.getParametar());
                        odgovor.setRezultat(rez);
                        break;
                    case Akcija.LOGOUT:
                        a = null;
                        boolean izlogovan = Kontroler.getINSTANCE().logout(zahtev.getParametar().toString());
//                        odgovor.setRezultat(izlogovan);
                        break;

                    case Akcija.VRATI_SVA_MESTA:
                        List<AbstractDomainObject> mesta = Kontroler.getINSTANCE().ucitajMesta();
                        odgovor.setRezultat(mesta);
                        break;
                    case Akcija.VRATI_SVE_POSTOVE:
                        List<AbstractDomainObject> postovi = Kontroler.getINSTANCE().ucitajPostove();
                        odgovor.setRezultat(postovi);
                        break;
                    case Akcija.VRATI_SVE_KORISNIKE:
                        List<AbstractDomainObject> korisnici = Kontroler.getINSTANCE().ucitajKorisnike();
                        odgovor.setRezultat(korisnici);
                        break;
                    case Akcija.VRATI_SVE_TIPOVE:
                        List<AbstractDomainObject> tipoviLokala = Kontroler.getINSTANCE().ucitajTipove();
                        odgovor.setRezultat(tipoviLokala);
                        break;
                    case Akcija.VRATI_SVE_LOKALE:
                        List<AbstractDomainObject> lz = Kontroler.getINSTANCE().ucitajLokale();
                        odgovor.setRezultat(lz);
                        break;
//
//                    case Akcija.PRETRAZI_ULOGE_PO_KRITERIJUMU:
//                        List<AbstractDomainObject> uloge = Kontroler.getINSTANCE().vratiUlogeZaKriterijum(zahtev.getParametar());
//                        odgovor.setRezultat(uloge);
//                        break;
//                    case Akcija.PRETRAZI_LICENCE_PO_KRITERIJUMU:
//                        List<AbstractDomainObject> lic = Kontroler.getINSTANCE().vratiLicenceZaKriterijum(zahtev.getParametar());
//                        odgovor.setRezultat(lic);
//                        break;
//                    case Akcija.PRETRAZI_PILOTE_PO_KRITERIJUMU:
//                        List<AbstractDomainObject> ppk = Kontroler.getINSTANCE().vratiPiloteZaKriterijum(zahtev.getParametar());
//                        odgovor.setRezultat(ppk);
//                        break;
//                    case Akcija.PRETRAZI_MEHANICARE_PO_KRITERIJUMU:
//                        List<AbstractDomainObject> mpk = Kontroler.getINSTANCE().vratiMehanicareZaKriterijum(zahtev.getParametar());
//                        odgovor.setRezultat(mpk);
//                        break;
//                    case Akcija.PRETRAZI_ZAPOSLENE_PO_KRITERIJUMU:
//                        List<AbstractDomainObject> zpk = Kontroler.getINSTANCE().vratiZaposleneZaKriterijum(zahtev.getParametar());
//                        odgovor.setRezultat(zpk);
//                        break;
//
                    case Akcija.NOVI_LOKALI:
                        boolean ubacen = Kontroler.getINSTANCE().kreirajNoveLokale((List<Lokal>) zahtev.getParametar());
                        odgovor.setRezultat(ubacen);
                        break;
                    case Akcija.NOVI_POSTOVI:
                        boolean kreiran = Kontroler.getINSTANCE().noviPostovi((ArrayList<AbstractDomainObject>) zahtev.getParametar());
                        odgovor.setRezultat(kreiran);
                        break;
//                    case Akcija.NOVA_ULOGA:
//                        boolean ubacena = Kontroler.getINSTANCE().novaUloga((Uloga) zahtev.getParametar());
//                        odgovor.setRezultat(ubacena);
//                        break;
//                    case Akcija.NOVA_LICENCA:
//                        boolean ubacenal = Kontroler.getINSTANCE().novaLicenca((Licenca) zahtev.getParametar());
//                        odgovor.setRezultat(ubacenal);
//                        break;
//
                    case Akcija.SACUVAJ_IZMENU_KORISNIKA:
                        boolean izmenjen = Kontroler.getINSTANCE().izmeniKorisnika((Korisnik) zahtev.getParametar());
                        odgovor.setRezultat(izmenjen);
                        break;
                    case Akcija.SACUVAJ_IZMENU_LOKALA:
                        boolean izmenjena = Kontroler.getINSTANCE().izmenaLokala((Lokal) zahtev.getParametar());
                        odgovor.setRezultat(izmenjena);
                        break;
//                    case Akcija.SACUVAJ_IZMENU_LICENCE:
//                        boolean izmenjenal = Kontroler.getINSTANCE().izmenaLicence((Licenca) zahtev.getParametar());
//                        odgovor.setRezultat(izmenjenal);
//                        break;
//                    case Akcija.SACUVAJ_IZMENU_PILOTA:
//                        boolean izmenjenp = Kontroler.getINSTANCE().izmenaPilota((Pilot) zahtev.getParametar());
//                        odgovor.setRezultat(izmenjenp);
//                        break;
//                    case Akcija.SACUVAJ_IZMENU_ZAPOSLENOG:
//                        boolean izmenjenz = Kontroler.getINSTANCE().izmenaZaposlenog((Zaposleni) zahtev.getParametar());
//                        odgovor.setRezultat(izmenjenz);
//                        break;
//                    case Akcija.SACUVAJ_IZMENU_MEHANICARA:
//                        boolean izmenjenm = Kontroler.getINSTANCE().izmenaMehanicara((AvioMehanicar) zahtev.getParametar());
//                        odgovor.setRezultat(izmenjenm);
//                        break;
//
                    case Akcija.BRISI_KORISNIKA:
                        boolean obrisan = Kontroler.getINSTANCE().obrisiKorisnika((Korisnik) zahtev.getParametar());
                        odgovor.setRezultat(obrisan);
                        break;
                    case Akcija.BRISI_LOKAL:
                        boolean obrisana = Kontroler.getINSTANCE().brisiLokal((Lokal) zahtev.getParametar());
                        odgovor.setRezultat(obrisana);
                        break;
//                    case Akcija.BRISI_LICENCU:
//                        boolean obrisanal = Kontroler.getINSTANCE().brisiLicencu((Licenca) zahtev.getParametar());
//                        odgovor.setRezultat(obrisanal);
//                        break;
//                    case Akcija.BRISI_ZAPOSLENOG:
//                        boolean obrisanz = Kontroler.getINSTANCE().obrisiZaposlenog((Zaposleni) zahtev.getParametar());
//                        odgovor.setRezultat(obrisanz);
//                        break;
//                    case Akcija.BRISI_PILOTA:
//                        boolean obrisanp = Kontroler.getINSTANCE().obrisiPilota((Pilot) zahtev.getParametar());
//                        odgovor.setRezultat(obrisanp);
//                        break;
//                    case Akcija.BRISI_MEHANICARA:
//                        boolean obrisanm = Kontroler.getINSTANCE().obrisiMehanicara((AvioMehanicar) zahtev.getParametar());
//                        odgovor.setRezultat(obrisanm);
//                        break;

                    default:
                        throw new Exception("Server ne razume zahtev.");
                }
                Kontroler.getINSTANCE().ispisi("Klijentu: [" + soket.getInetAddress()
                        + "] odgovoreno na zahtev: " + zahtev.getOperacija());
                zahtevi.append("Odgovor poslat.\n");
                ObjectOutputStream output = new ObjectOutputStream(soket.getOutputStream());
                output.writeObject(odgovor);
                } catch (IOException ex) {
                interrupt();
                Logger.getLogger(NitKlijent.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                interrupt();
                Logger.getLogger(NitKlijent.class.getName()).log(Level.SEVERE, null, ex);
            }
             catch (Exception e) {
                odgovor.setIzuzetak(e);
                Kontroler.getINSTANCE().ispisi("Klijentu: [" + soket.getInetAddress()
                        + "] nije odgovoreno na zahtev: " + zahtev.getOperacija()
                        + "\n\tjer je doslo do greske: " + e.getMessage());
                zahtevi.append("Odgovor nije poslat jer je doslo do greske:\n\t").append(e.getMessage()).append("\n");
                ObjectOutputStream output = new ObjectOutputStream(soket.getOutputStream());
                output.writeObject(odgovor);
            } 
            

        }
    }

}
