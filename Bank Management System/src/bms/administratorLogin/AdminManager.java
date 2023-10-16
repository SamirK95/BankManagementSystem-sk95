package bms.administratorLogin;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import bms.dbLoaderPackage.DbLoader;


/**
 * Klasa `AdminManager` služi za autentikaciju administratora u aplikaciji bankomata.
 * Omogućava provjeru unesenih korisničkih podataka (korisničko ime i lozinka)
 * prema podacima pohranjenim u bazi podataka te pruža funkcionalnost za izračunavanje
 * i usporedbu hash vrijednosti lozinke radi sigurnije autentikacije.
 */
public class AdminManager {

		// kreiramo instancu klase da bi ucitali url baze podataka
		static DbLoader dbLoader = new DbLoader();
		// Pozivamo metod za dobijanje url-a baze podataka i spremamo u string
		static String sqlUrl = dbLoader.getDatabaseUrl();
	
	/**
     * Funkcija `computePasswordHash` koristi algoritam SHA-256 za izračunavanje hash vrijednosti lozinke.
     *
     * @param password Lozinka koja se treba pretvoriti u hash.
     * @return Bajtni niz koji predstavlja izračunati hash lozinke.
     */
    private static byte[] computePasswordHash(String password) {
        try {
            // Kreiranje objekta MessageDigest s SHA-256 algoritmom
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Konverzija lozinke u bajt niz
            byte[] passwordBytes = password.getBytes(StandardCharsets.UTF_8);

            // Računanje hash-a
            return digest.digest(passwordBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            // Obrada greške ako algoritam nije dostupan
            return null;
        }
    }
    
    
    /**
     * Funkcija `authenticateAdmin` provjerava autentikaciju administratora prema korisničkim
     * podacima pohranjenim u bazi podataka.
     *
     * @param username Korisničko ime administratora koje se provjerava.
     * @param password Lozinka administratora koja se provjerava.
     * @return `true` ako je autentikacija uspješna, inače `false`.
     */
    public static boolean authenticateAdmin(String username, String password) {
        Connection dbConnection = null;
        try {
            dbConnection = DriverManager.getConnection(sqlUrl);
            String lozinkaQuery = "SELECT * FROM AdminATM WHERE UserName = ? AND PasswordHash = ?";
            PreparedStatement preparedStatement = dbConnection.prepareStatement(lozinkaQuery);
            preparedStatement.setString(1, username);
            preparedStatement.setBytes(2, computePasswordHash(password));

            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next();
        } catch (SQLException ex) {
            ex.printStackTrace();
            // Prikazivanje poruke o grešci ako dođe do problema s bazom podataka
            return false;
        } finally {
            // Zatvaranje veze s bazom podataka nakon završetka
            if (dbConnection != null) {
                try {
                    dbConnection.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    System.err.println("Greška prilikom zatvaranja veze s bazom podataka.");
                }
            }
        }
    }
    
    
}
