package bms.addNewUser;

import java.awt.Color;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import bms.dbLoaderPackage.DbLoader;


/**
 * Klasa `AddNewUserManager` upravlja registracijom novih korisnika u sistemu banke ili ATM-a.
 * Omogućuje unos osnovnih informacija o korisniku, uključujući ime, prezime, JMBG, stanje računa
 * te sigurnu pohranu lozinke u bazu podataka.
 */
public class AddNewUserManager {

	private JTextField txtFirstName;
    private JTextField txtLastName;
    private JTextField txtAccBalance;
    private JTextField txtJMBG;
    private JPasswordField passFieldMain;
    private JPasswordField passFieldSec;
    private JLabel lblIspisPoruke;
    
    // kreiramo instancu klase da bi ucitali url baze podataka
 	static DbLoader dbLoader = new DbLoader();
 	// Pozivamo metod za dobijanje url-a baze podataka i spremamo u string
 	static String sqlUrl = dbLoader.getDatabaseUrl();
    
 	/**
     * Konstruktor klase `AddNewUserManager` koji prima instance različitih komponenti
     * za unos podataka i prikazivanje poruka.
     *
     * @param firstName   Polje za unos imena korisnika.
     * @param lastName    Polje za unos prezimena korisnika.
     * @param jmbg        Polje za unos JMBG-a korisnika.
     * @param accBalance  Polje za unos stanja računa korisnika.
     * @param passMain    Polje za unos lozinke korisnika.
     * @param passSec     Polje za potvrdu lozinke korisnika.
     * @param ispisPoruke Labela za prikazivanje poruka o rezultatu registracije.
     */
    public AddNewUserManager(JTextField firstName, JTextField lastName, JTextField jmbg, JTextField accBalance,
            JPasswordField passMain, JPasswordField passSec, JLabel ispisPoruke) {
    	
    	this.txtFirstName = firstName;
		this.txtLastName = lastName;
		this.txtJMBG = jmbg;
		this.txtAccBalance = accBalance;
		this.passFieldMain = passMain;
		this.passFieldSec = passSec;
		this.lblIspisPoruke = ispisPoruke;
			
			}
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
     * Metoda `handleUserRegistration` obavlja proces registracije korisnika.
     * Provodi različite provjere unesenih podataka prije nego što ih pohrani u bazu podataka.
     */
	public void handleUserRegistration() {
		Connection dbConnection = null;
	    // Dohvati unesene podatke iz polja
	    String firstName = txtFirstName.getText();
	    String lastName = txtLastName.getText();
	    String jmbg = txtJMBG.getText();
	    String accountBalanceStr = txtAccBalance.getText();
	    String password = new String(passFieldMain.getPassword());
	    String confirmPassword = new String(passFieldSec.getPassword());
	    BigDecimal accountBalance;
	    
	    

	    // Provjera jesu li sva polja popunjena
	    if (firstName.isEmpty() || lastName.isEmpty() || jmbg.isEmpty() || accountBalanceStr.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
	    	lblIspisPoruke.setForeground(Color.red);
	        lblIspisPoruke.setText("Sva polja su obavezna.");
	        return;
	    }
	    // Provjera duzine PIN-a da li je uneseno tacno 13 karaktera
	    if(jmbg.length() != 13) {
	    	lblIspisPoruke.setForeground(Color.red);
	        lblIspisPoruke.setText("JMBG mora sadržavati tačno 13 karaktera.");
	        return;
	    }

	    // Provjera podudaranja lozinke i potvrde lozinke
	    if (!password.equals(confirmPassword)) {
	    	lblIspisPoruke.setForeground(Color.red);
	        lblIspisPoruke.setText("Lozinke se ne podudaraju.");
	        return;
	    }
	    
	    
	    try {
	    	try {
		        accountBalance = new BigDecimal(accountBalanceStr);
		        if (accountBalance.compareTo(BigDecimal.ZERO) < 0) {
		        	lblIspisPoruke.setForeground(Color.red);
		            lblIspisPoruke.setText("Ne možete postaviti početno stanje na manje od 0.");
		            return;
		        }
		    } catch (NumberFormatException e) {
		    	lblIspisPoruke.setForeground(Color.red);
		        lblIspisPoruke.setText("Neispravan unos za stanje računa.");
		        return;
		    }
	    	
            dbConnection = DriverManager.getConnection(sqlUrl);
            
            // Provjera postoji li korisnik sa istim JMBG-om u bazi podataka
            String checkUserQuery = "SELECT COUNT(*) FROM Users WHERE JMBG = ?";
            PreparedStatement checkUserStatement = dbConnection.prepareStatement(checkUserQuery);
            checkUserStatement.setString(1, jmbg);
            ResultSet userResultSet = checkUserStatement.executeQuery();
            userResultSet.next();
            int userCount = userResultSet.getInt(1);
            
            //ako postoji korisnik sa istim JMBG-om u bazi podataka ispisati poruku
            if (userCount > 0) {
                lblIspisPoruke.setForeground(Color.red);
                lblIspisPoruke.setText("Korisnik sa unesenim JMBG-om već postoji.");
                return;
            }
            
            // Pohrana novog korisnika u bazu podataka
            String insertQuery = "INSERT INTO Users (FirstName, LastName, JMBG, StanjeRacuna, Lozinka, PasswordHash)"
            		+ "VALUES(?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = dbConnection.prepareStatement(insertQuery);
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, jmbg);
            preparedStatement.setBigDecimal(4, accountBalance);
            preparedStatement.setString(5, password);
            preparedStatement.setBytes(6, computePasswordHash(password));

            int rowsAffected = preparedStatement.executeUpdate();
            // Nakon pohrane, prikazujemo poruku o uspješnoj registraciji
            lblIspisPoruke.setForeground(Color.green);
    	    lblIspisPoruke.setText("Uspješno ste registrovali novog korisnika.");

            
        } catch (SQLException ex) {
            ex.printStackTrace();
            lblIspisPoruke.setForeground(Color.red);
            lblIspisPoruke.setText("Došlo je do problema s bazom podataka.");
            // Prikazivanje poruke o grešci ako dođe do problema s bazom podataka
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
