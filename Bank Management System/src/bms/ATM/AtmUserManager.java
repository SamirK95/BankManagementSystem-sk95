package bms.ATM;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JPasswordField;

import bms.dbLoaderPackage.DbLoader;



/**
 * Klasa UserManager predstavlja upravljanje korisničkim podacima na bankomatu.
 * 
 * Ova klasa omogućuje provjeru unesenog PIN-a, prikazivanje stanja računa, te izvršavanje
 * uplata i isplata s računa korisnika. Radi sa bazom podataka za pristup i ažuriranje korisničkih podataka.
 */

public class AtmUserManager {
	
	
	// Varijabla koja označava je li korisnik prijavljen (true) ili nije (false).
	private boolean userLoggedIn = false;

	// JLabel komponenta koja se koristi za prikazivanje poruka korisniku.
	private JLabel lblBotIspis;

	// kreiramo instancu klase da bi ucitali url baze podataka
	static DbLoader dbLoader = new DbLoader();
	// Pozivamo metod za dobijanje url-a baze podataka i spremamo u string
	static String sqlUrl = dbLoader.getDatabaseUrl();

	// String koji čuva trenutno uneseni PIN od strane korisnika.
	private String enteredPin = "";
	
	//Polje za unos PIN-a na korisničkom interfejsu bankomata.
	private JPasswordField pinField;

	// Funkcija koja vraća trenutno uneseni PIN od strane korisnika.
	public String getEnteredPin() {
	    return enteredPin;
	}

	// Metoda za postavljanje JLabel komponente koja će se koristiti za ispisivanje poruka korisniku.
	public void setLblBotIspis(JLabel lblBotIspis) {
	    this.lblBotIspis = lblBotIspis;
	}
	
	//Postavlja referencu na polje za unos PIN-a.
	//@param pinField Polje za unos PIN-a koje će se postaviti.
	public void setPinField(JPasswordField pinField) {
	    this.pinField = pinField;
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
     * Provjerava ispravnost unesenog PIN-a tako da ga uspoređuje s bazom podataka i provjerava status računa korisnika.
     *
     * @param pinField     Polje za unos PIN-a koje je unio korisnik za provjeru.
     * @param lblBotIspis  JLabel komponenta za prikazivanje poruka korisniku.
     * @return             True ako je PIN ispravan, korisnik je prijavljen i račun je aktivan, inače False.
     */
	
    public boolean isPinValid(JPasswordField pinField, JLabel lblBotIspis) {
        // Dohvaćanje unesenog PIN-a iz polja za unos
        char[] enteredPinChars = pinField.getPassword();
        // Konverzija unesenog PIN-a u String
        String enteredPin = new String(enteredPinChars);

        Connection dbConnection = null;
        try {
            // Uspostavljanje veze s bazom podataka
            dbConnection = DriverManager.getConnection(sqlUrl);
            String query = "SELECT Status, PasswordHash FROM Users WHERE Lozinka = ?";
            PreparedStatement preparedStatement = dbConnection.prepareStatement(query);

            // Postavljanje PIN-a za upit
            preparedStatement.setString(1, enteredPin);
            
            //izvrsavamo upit
            ResultSet resultSet = preparedStatement.executeQuery();

            //ukoliko je pronadjen korisnik kome se podudara lozinka sa korisnicki unesenom sifrom
            if (resultSet.next()) {
            	//Dohvatamo status korisnika iz baze podataka
                String status = resultSet.getString("Status");
                byte[] storedPasswordHash = resultSet.getBytes("PasswordHash");

                // Provjerite je li korisnikov račun aktivan ili deaktiviran
                if (status.equals("Aktivan")) {
                    // PIN je ispravan, označi da je korisnik prijavljen
                    userLoggedIn = true;
                    // Postavljanje trenutno unesenog PIN-a ako je ispravan
                    this.enteredPin = enteredPin;
                    // PIN je ispravan, i korisnik može birati dalje operacije
                    lblBotIspis.setText("PIN ispravan. Možete nastaviti. Odaberite operaciju.");
                    return true;
                } else if (status.equals("Deaktiviran")) {
                    // Račun je deaktiviran, ispišite odgovarajuću poruku
                    lblBotIspis.setText("Vaš račun je deaktiviran. Molimo kontaktirajte banku.");
                } else {
                    // Nepoznat status računa
                    lblBotIspis.setText("Nepoznat status računa: " + status);
                }
            } else {
                // PIN nije ispravan
                lblBotIspis.setText("Pogriješili ste PIN, pokušajte ponovo!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            // Prikazivanje poruke o grešci ako dođe do problema s bazom podataka
            lblBotIspis.setText("Došlo je do problema s bazom podataka. 1");
        } finally {
            // Zatvaranje veze s bazom podataka nakon završetka
            if (dbConnection != null) {
                try {
                    dbConnection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    System.err.println("Greška prilikom zatvaranja veze s bazom podataka.");
                }
            }
        }
        return false;
    }
    
	
	/**
     * Metoda getAccountBalance
     * 
     * Dohvaća stanje računa korisnika iz baze podataka i prikazuje ga na interfejsu.
     *
     * @param lblBotIspis  JLabel komponenta za prikazivanje poruka korisniku.
     */
	
	public void getAccountBalance(JLabel lblBotIspis) {
	    Connection dbConnection = null;
	    
	    try {
	        // Provjera da li je korisnik logiran
	        if (userLoggedIn) {
	            // Uspostavljanje veze s bazom podataka
	            dbConnection = DriverManager.getConnection(sqlUrl);
	            String balanceQuery = "SELECT StanjeRacuna FROM Users WHERE Lozinka = ?";
	            PreparedStatement preparedStatement = dbConnection.prepareStatement(balanceQuery);
	            preparedStatement.setString(1, enteredPin);

	            ResultSet resultSet = preparedStatement.executeQuery();

	            if (resultSet.next()) {
	                // Dohvat stanja računa iz rezultata upita
	                BigDecimal balance = resultSet.getBigDecimal("StanjeRacuna");
	                // Prikaz stanja računa korisnika
	                lblBotIspis.setText("Stanje računa: " + balance + " KM.");
	            } else {
	                lblBotIspis.setText("Korisnik s unesenim PIN-om ne postoji.");
	            }
	        }
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	        lblBotIspis.setText("Došlo je do problema s bazom podataka. 2");
	    } finally {
	        // Zatvaranje veze s bazom podataka nakon završetka
	        if (dbConnection != null) {
	            try {
	                dbConnection.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	                System.err.println("Greška prilikom zatvaranja veze s bazom podataka.");
	            }
	        } else {
	            lblBotIspis.setText("Unesite PIN prije nego što provjerite stanje računa.");
	        }
	    }
	}

    
    
	/**
     * Metoda deposit
     * 
     * Položi određeni iznos na račun korisnika u bazi podataka i ažurira stanje računa.
     *
     * @param depositAmount  Iznos koji korisnik želi položiti na račun.
     * @param lblBotIspis    JLabel komponenta za prikazivanje poruka korisniku.
     * @return              True ako je operacija bila uspješna, inače False.
     */
	
	public boolean deposit(BigDecimal depositAmount, JLabel lblBotIspis) {
	    Connection dbConnection = null;

	    try {
	        // Provjerite je li unesen PIN
	        if (userLoggedIn) {
	            lblBotIspis.setText("Unesite iznos koji želite položiti na račun.");

	            // Provjerite je li depositAmount veći od BigDecimal.ZERO
	            if (depositAmount.compareTo(BigDecimal.ZERO) > 0) {
	                dbConnection = DriverManager.getConnection(sqlUrl);

	                // Provjerite saldo računa
	                String query = "SELECT StanjeRacuna FROM Users WHERE Lozinka = ?";
	                PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
	                preparedStatement.setString(1, enteredPin);
	                ResultSet resultSet = preparedStatement.executeQuery();

	                if (resultSet.next()) {
	                    BigDecimal oldBalance = resultSet.getBigDecimal("StanjeRacuna");
	                    BigDecimal newBalance = oldBalance.add(depositAmount);
	                    String updateQuery = "UPDATE Users SET StanjeRacuna = ? WHERE Lozinka = ?";
	                    PreparedStatement updateStatement = dbConnection.prepareStatement(updateQuery);
	                    updateStatement.setBigDecimal(1, newBalance);
	                    updateStatement.setString(2, enteredPin);
	                    updateStatement.executeUpdate();
	                    
	                    // Dohvatamo ime, prezime, JMBG i UserID korisnika.
	                    String userDataQuery = "SELECT FirstName, LastName, JMBG, UserID FROM Users WHERE Lozinka = ?";
	                    PreparedStatement userDataStatement = dbConnection.prepareStatement(userDataQuery);
	                    userDataStatement.setString(1, enteredPin);
	                    ResultSet userDataResultSet = userDataStatement.executeQuery();
	                    
	                    // Varijabla za spremanje ID-a korisnika
	                    int userID = 0;
	                    // Varijable za spremanje osobnih podataka korisnika
	                    String firstName = "";
	                    String lastName = "";
	                    String jmbg = "";
	                    
	                    // Ukoliko smo dohvatili podatke smjestamo ih u varijable
	                    if (userDataResultSet.next()) {
	                    	userID = userDataResultSet.getInt("UserID");
	                        firstName = userDataResultSet.getString("FirstName");
	                        lastName = userDataResultSet.getString("LastName");
	                        jmbg = userDataResultSet.getString("JMBG");
	                    }
	                    
	                    // Ispisujemo poruku korisniku bankomata.
	                    lblBotIspis.setText("<html>Uspješno ste položili " + depositAmount + " KM na račun. Novo stanje računa: " + newBalance + " KM.</html>");
	                    
	                    
	                    // Zabilježavamo transakciju u tabelu Transactions
	                    String insertTransactionQuery = "INSERT INTO Transactions (UserID, FirstNameClient, LastNameClient, JMBGClient,"
	                    		+ " TransactionType, Amount, TransactionsDateTime) VALUES (?, ?, ?, ?, ?, ?, GETDATE())";
	                    PreparedStatement insertTransactionStatement = dbConnection.prepareStatement(insertTransactionQuery);
	                    insertTransactionStatement.setInt(1, userID); 
	                    insertTransactionStatement.setString(2, firstName);
	                    insertTransactionStatement.setString(3, lastName);
	                    insertTransactionStatement.setString(4, jmbg);
	                    insertTransactionStatement.setString(5, "Deposit");
	                    insertTransactionStatement.setBigDecimal(6, depositAmount);
	                    insertTransactionStatement.executeUpdate();
	                    // Vraćamo true kako bismo označili da je operacija uspješna
	                    return true;
	                }
	            } else {
	                lblBotIspis.setText("Unesite iznos veći od 0.");
	            }
	        } else {
	            lblBotIspis.setText("Unesite PIN prije nego što položite novac.");
	        }
	    } catch (NumberFormatException ex) {
	        lblBotIspis.setText("Unesite valjan iznos.");
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	        lblBotIspis.setText("Došlo je do problema s bazom podataka. 3");
	    } finally {
	        // Zatvaranje veze s bazom podataka nakon završetka
	        if (dbConnection != null) {
	            try {
	                dbConnection.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	                System.err.println("Greška prilikom zatvaranja veze s bazom podataka.");
	            }
	        }
	    }

	    // Vraćamo false kako bismo označili da je operacija neuspješna
	    return false;
	}


    
	/**
     * Metoda withdraw
     * 
     * Podignite određeni iznos sa računa korisnika u bazi podataka i ažurirajte stanje računa.
     *
     * @param withdrawalAmount  Iznos koji korisnik želi podići s računa.
     * @param lblBotIspis      JLabel komponenta za prikazivanje poruka korisniku.
     * @return                True ako je operacija bila uspješna, inače False.
     */
	
	public boolean withdraw(BigDecimal withdrawalAmount, JLabel lblBotIspis) {
	    Connection dbConnection = null;

	    // Provjera je li iznos veći od 0
	    if (withdrawalAmount.compareTo(BigDecimal.ZERO) > 0) {
	        try {
	            // Provjera je li unesen PIN
	            if (userLoggedIn) {
	                dbConnection = DriverManager.getConnection(sqlUrl);

	                // Provjera stanja računa
	                String balanceQuery = "SELECT StanjeRacuna FROM Users WHERE Lozinka = ?";
	                PreparedStatement preparedStatement = dbConnection.prepareStatement(balanceQuery);
	                preparedStatement.setString(1, enteredPin);

	                ResultSet resultSet = preparedStatement.executeQuery();

	                if (resultSet.next()) {
	                    BigDecimal balance = resultSet.getBigDecimal("StanjeRacuna");
	                    if (withdrawalAmount.compareTo(balance) <= 0) {
	                        // Izvršavamo isplatu i ažuriramo stanje računa
	                        BigDecimal newBalance = balance.subtract(withdrawalAmount);
	                        String updateQuery = "UPDATE Users SET StanjeRacuna = ? WHERE Lozinka = ?";
	                        PreparedStatement updateStatement = dbConnection.prepareStatement(updateQuery);
	                        updateStatement.setBigDecimal(1, newBalance);
	                        updateStatement.setString(2, enteredPin);
	                        updateStatement.executeUpdate();
	                        
	                        // Dohvatamo ime, prezime, JMBG i UserID korisnika.
		                    String userDataQuery = "SELECT FirstName, LastName, JMBG, UserID FROM Users WHERE Lozinka = ?";
		                    PreparedStatement userDataStatement = dbConnection.prepareStatement(userDataQuery);
		                    userDataStatement.setString(1, enteredPin);
		                    ResultSet userDataResultSet = userDataStatement.executeQuery();
		                    
		                    // Varijabla za spremanje ID-a korisnika
		                    int userID = 0;
		                    // Varijable za spremanje osobnih podataka korisnika
		                    String firstName = "";
		                    String lastName = "";
		                    String jmbg = "";
		                    
		                    // Ukoliko nam se vrate rezultati iz upita podatke smjestamo u varijable.
		                    if (userDataResultSet.next()) {
		                    	userID = userDataResultSet.getInt("UserID");
		                        firstName = userDataResultSet.getString("FirstName");
		                        lastName = userDataResultSet.getString("LastName");
		                        jmbg = userDataResultSet.getString("JMBG");
		                    }
		                    
		                    // Ispisujemo poruku korisniku o uspjesnoj transakciji
	                        lblBotIspis.setText("Isplatili ste " + withdrawalAmount + " KM. Novo stanje računa: " + newBalance + " KM.");
	                        
	                        // Zabiljezavamo transakciju u tabelu Transactions
		                    String insertTransactionQuery = "INSERT INTO Transactions (UserID, FirstNameClient, LastNameClient, JMBGClient,"
		                    		+ " TransactionType, Amount, TransactionsDateTime) VALUES (?, ?, ?, ?, ?, ?, GETDATE())";
		                    PreparedStatement insertTransactionStatement = dbConnection.prepareStatement(insertTransactionQuery);
		                    insertTransactionStatement.setInt(1, userID);
		                    insertTransactionStatement.setString(2, firstName);
		                    insertTransactionStatement.setString(3, lastName);
		                    insertTransactionStatement.setString(4, jmbg);
		                    insertTransactionStatement.setString(5, "Withdraw");
		                    insertTransactionStatement.setBigDecimal(6, withdrawalAmount);
		                    insertTransactionStatement.executeUpdate();
		                    
		                    
	                        //Označavamo uspješnost operacije
	                        return true;
	                    } else {
	                        lblBotIspis.setText("Nemate dovoljno sredstava za ovu transakciju.");
	                    }
	                } else {
	                    lblBotIspis.setText("Korisnik s unesenim PIN-om ne postoji.");
	                }
	            } else {
	                lblBotIspis.setText("Unesite PIN prije nego što podignete novac.");
	            }
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	            lblBotIspis.setText("Došlo je do problema s bazom podataka. 4");
	        } finally {
	            // Zatvaranje veze s bazom podataka nakon završetka
	            if (dbConnection != null) {
	                try {
	                    dbConnection.close();
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                    System.err.println("Greška prilikom zatvaranja veze s bazom podataka.");
	                }
	            }
	        }
	    } else {
	        lblBotIspis.setText("Unesite iznos veći od 0.");
	    }

	    return false; // neuspješna operacija
	}
    
    
}
    

    

