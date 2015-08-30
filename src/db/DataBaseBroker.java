package db;

import domen.AbstractDomainObject;
import domen.Korisnik;
import domen.Lokal;
import domen.Mesto;
import domen.Post;
import domen.TipLokala;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Matija Veljkovic
 */
public final class DataBaseBroker {

    private static DataBaseBroker INSTANCE;
    private Connection connection;

    private String url;
    private String user;
    private String password;
    private String driver;

    private DataBaseBroker() {
        postavljenaJeNovaBaza();
    }

    public static DataBaseBroker getINSTANCE() throws Exception {
        if (INSTANCE == null) {
            INSTANCE = new DataBaseBroker();
        }
        return INSTANCE;
    }

    public void postavljenaJeNovaBaza() {
        url = Util.getINSTANCE().getDBURL();
        user = Util.getINSTANCE().getDBUser();
        password = Util.getINSTANCE().getDBPassword();
        driver = Util.getINSTANCE().getDBDriver();
    }

    public void ucitajDrajver() throws Exception {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            throw new Exception("Sistem nije uspeo da ucita drajver.");
        }
    }

    public void otvoriKonekciju() throws Exception {
        try {
            connection = DriverManager.getConnection(url, user, password);
            connection.setAutoCommit(false);
        } catch (SQLException ex) {
            throw new Exception("Sistem nije uspeo da se poveze sa bazom.");
        }
    }

    public void commitTransakcije() throws Exception {
        try {
            connection.commit();
        } catch (SQLException ex) {
            throw new Exception("Sistem nije uspeo da sacuva transakciju.");
        }
    }

    public void rollbackTransakcije() throws Exception {
        try {
            connection.rollback();
        } catch (SQLException ex) {
            throw new Exception("Sistem nije uspeo da ponisti transakciju.");
        }
    }

    public void zatvoriKonekciju() throws Exception {
        try {
            connection.close();
        } catch (SQLException ex) {
            throw new Exception("Sistem nije uspeo da zatvori konkciju ka bazi.");
        }
    }

    public List<AbstractDomainObject> selectSve(AbstractDomainObject ado) throws Exception {
        try {//mozda zeza null u dajUslovZaSelect pa vrati na ado
            String upit = "SELECT * FROM " + ado.vratiNazivTabele() + " " + ado.dajUslovZaSelect(null) + " ORDER BY " + ado.vratiNazivTabele() + "." + ado.vratiNazivPK();
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery(upit);
            return ado.napuni(rs, ado);
        } catch (Exception ex) {
            throw new Exception("Greska pri ucitavanju liste objekata!");
        }
    }

    public List<AbstractDomainObject> selectSveOrderByDate(AbstractDomainObject ado) throws Exception {
        try {//mozda zeza null u dajUslovZaSelect pa vrati na ado
            String upit = "SELECT *, TIMEDIFF(NOW(),postovi.vreme) as 'postavljenoPre' FROM " + ado.vratiNazivTabele() + " " + ado.dajUslovZaSelect(null) + " ORDER BY postavljenoPre asc";
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery(upit);
            return ado.napuni(rs, ado);
        } catch (Exception ex) {
            throw new Exception("Greska pri ucitavanju liste objekata!");
        }
    }

    public List<AbstractDomainObject> selectPoKriterijumu(AbstractDomainObject ado, AbstractDomainObject filter) throws Exception {
        try {
            String upit = "SELECT * " + ado.dajUslovZaSelect(filter) + " ORDER BY " + ado.vratiNazivTabele() + "." + ado.vratiNazivPK();
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery(upit);
            return ado.napuni(rs, ado);
        } catch (Exception ex) {
            throw new Exception("Greska pri ucitavanju liste objekata!");
        }
    }

    public int vratiSledecuSifruZa(AbstractDomainObject obj) throws Exception {
        try {
            String upit = "SELECT MAX (" + obj.vratiNazivPK() + ") AS sifra FROM " + obj.vratiNazivTabele();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(upit);
            rs.next();
            int sifra = rs.getInt(1);
            statement.close();
            return ++sifra;
        } catch (Exception ex) {
            throw new Exception("Greska pri trazenju sledece sifre!");
        }
    }

    public void kreirajNovi(AbstractDomainObject obj) throws Exception {
        try {
            String upit = "INSERT INTO " + obj.vratiNazivTabele() + " " + obj.vratiRedosledZaInsert() + " VALUES (" + obj.vratiVrednostiZaInsert() + ")";
            Statement statement = connection.createStatement();
            statement.executeUpdate(upit);
            statement.close();
        } catch (Exception ex) {
            throw new Exception("Greska pri unosu novog objekta!");
        }
    }

    public void kreirajNove(List<AbstractDomainObject> listObj) throws Exception {
        try {
            Statement statement = connection.createStatement();
            for (int i = 0; i < listObj.size(); i++) {
                AbstractDomainObject obj = listObj.get(i);
                String upit = "INSERT INTO " + obj.vratiNazivTabele() + " " + obj.vratiRedosledZaInsert() + " VALUES (" + obj.vratiVrednostiZaInsert() + ")";
                statement.executeUpdate(upit);
            }

            statement.close();
        } catch (Exception ex) {
            throw new Exception("Greska pri unosu novog objekta!");
        }
    }

    public void izmeni(AbstractDomainObject obj) throws Exception {
        try {
            String upit = "UPDATE " + obj.vratiNazivTabele() + " SET " + obj.vratiVrednostiZaUpdate()
                    + " WHERE " + obj.vratiNazivPK() + " = " + obj.vratiVrednostPK();
            Statement statement = connection.createStatement();
            statement.executeUpdate(upit);
            statement.close();
        } catch (Exception ex) {
            throw new Exception("Greska pri izmeni objekta!");
        }
    }

    public void obrisi(AbstractDomainObject obj) throws Exception {
        try {
            String upit = "DELETE FROM " + obj.vratiNazivTabele()
                    + " WHERE " + obj.vratiNazivPK() + " = " + obj.vratiVrednostPK();
            Statement statement = connection.createStatement();
            statement.executeUpdate(upit);
            statement.close();
        } catch (Exception ex) {
            throw new Exception("Greska pri brisanju objekta!");
        }
    }

    public AbstractDomainObject validacija(String kEmail, String kSifra, String tabela) throws Exception {
        try {
            AbstractDomainObject ado;
            if (tabela.equals("lokali")) {
                ado = new Lokal();
            }else{
                ado = new Korisnik();
            }

            String upit = "SELECT * FROM " + ado.vratiNazivTabele() + " " + ado.dajUslovZaSelect(null) + " WHERE email = ?";
            PreparedStatement ps = connection.prepareStatement(upit);
            ps.setString(1, kEmail);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String pass = rs.getString("sifra");
                if (pass.equals(kSifra)) {
                    ado.napuniJedanObjekat(rs);
                    rs.close();
                    ps.close();
                    ulogujIzloguj(kEmail, true, tabela);
                    return ado;

                }

            }

        } catch (Exception ex) {
            throw new Exception("Greska pri validaciji admina!");
        }
        return null;
    }

    public void ulogujIzloguj(String kEmail, boolean uloguj, String tabela) throws Exception {
        try {
            String upit = "UPDATE " + tabela + " SET ulogovan = ?, lastLogin = NOW() WHERE email = ?";
            PreparedStatement ps = connection.prepareStatement(upit);
            ps.setBoolean(1, uloguj);
//            ps.setDate(2, new Date(new java.util.Date().getTime()));
            ps.setString(2, kEmail);
            ps.executeUpdate();
            ps.close();
        } catch (Exception ex) {
            throw new Exception("Greska pri izmeni objekta!");
        }
    }

    public void obrisiNaOsnovuSpoljnogKljuca(AbstractDomainObject a, AbstractDomainObject b) throws Exception {
        try {
            String upit = "DELETE FROM " + b.vratiNazivTabele()
                    + " WHERE " + b.vratiNazivTabele() + "." + a.vratiNazivPK() + " = " + a.vratiVrednostPK();
            Statement statement = connection.createStatement();
            statement.executeUpdate(upit);
            statement.close();
        } catch (Exception ex) {
            throw new Exception("Greska pri brisanju objekta!");
        }
    }

}
