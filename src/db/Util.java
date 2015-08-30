package db;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 *
 * @author Matija Veljkovic
 */
public class Util {

    private Properties properties;
    private static Util INSTANCE;

    public static Util getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new Util();
        }
        return INSTANCE;
    }

    private Util() {
        properties = new Properties();
        try {
            properties.load(new FileInputStream("all.properties"));
        } catch (Exception ex) {
            //System.out.println("Greska u Util pri otvaranju properties fajla.\t " + ex.getMessage());
        }
    }

    public String getDBURL() {
        return properties.getProperty(properties.getProperty("current_db") + "_url");
    }

    public String getDBUser() {
        return properties.getProperty(properties.getProperty("current_db") + "_user");
    }

    public String getDBPassword() {
        return properties.getProperty(properties.getProperty("current_db") + "_password");
    }

    public String getDBDriver() {
        return properties.getProperty(properties.getProperty("current_db") + "_driver");
    }

    public Map vratiPodatkeOBazi(String baza) {
        Map<String, Object> m = new HashMap<>();
        m.put("driver", properties.getProperty(baza + "_driver"));
        m.put("pass", properties.getProperty(baza + "_password"));
        m.put("url", properties.getProperty(baza + "_url"));
        m.put("user", properties.getProperty(baza + "_user"));
        return m;
    }
    
    public String vratiIzabranuBazu(){
        return properties.getProperty("current_db");
    }

    public void postaviNovuBazu(String url, String user, String pass, String driver) {
        properties.setProperty("current_db", "nova");
        properties.setProperty("nova_url", url);
        properties.setProperty("nova_user", user);
        properties.setProperty("nova_password", pass);
        properties.setProperty("nova_driver", driver);
    }

    public void postaviNovuBazu(String postojecaBaza) {
        properties.setProperty("current_db", postojecaBaza);
    }
}
