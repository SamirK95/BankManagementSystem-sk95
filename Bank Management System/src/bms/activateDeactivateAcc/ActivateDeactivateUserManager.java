package bms.activateDeactivateAcc;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import bms.dbLoaderPackage.DbLoader;

/**
 * Ova klasa upravlja aktivacijom i deaktivacijom korisnika u sustavu ATM.
 */

public class ActivateDeactivateUserManager {
	
	// kreiramo instancu klase da bi ucitali url baze podataka
	static DbLoader dbLoader = new DbLoader();
	// Pozivamo metod za dobijanje url-a baze podataka i spremamo u string
	static String sqlUrl = dbLoader.getDatabaseUrl();
    
    /**
     * Akcijski slušatelj na dugmić za aktivaciju korisnika.
     *
     * @param btnActivate      Dugmić za aktivaciju
     * @param txtJMBG          Polje za unos JMBG-a
     * @param txtIme           Polje za ime korisnika
     * @param txtPrezime       Polje za prezime korisnika
     * @param lblIspisPoruka   Oznaka za ispis poruka
     */
    
    	public static void addActivateActionListener(
    	        JButton btnActivate, JTextField txtJMBG, JTextField txtIme, JTextField txtPrezime, JLabel lblIspisPoruka) {
    	    btnActivate.addActionListener(new ActionListener() {
    	        public void actionPerformed(ActionEvent e) {
    	            // Dohvatamo JMBG koji je administrator unio
    	            String enteredJMBG = txtJMBG.getText();

    	            // Provjeravamo dužinu JMBG-a
    	            if (enteredJMBG.length() != 13) {
    	                lblIspisPoruka.setText("<html>JMBG mora imati tačno 13 karaktera.</html>");
    	                return;
    	            }

    	            Connection dbConnection = null;
    	            try {
    	                dbConnection = DriverManager.getConnection(sqlUrl);

    	                // Provjeravamo postoji li korisnik s unesenim JMBG-om u bazi podataka
    	                String checkUserQuery = "SELECT FirstName, LastName, Status FROM Users WHERE JMBG = ?";
    	                PreparedStatement checkUserStatement = dbConnection.prepareStatement(checkUserQuery);
    	                checkUserStatement.setString(1, enteredJMBG);
    	                ResultSet userResultSet = checkUserStatement.executeQuery();

    	                if (userResultSet.next()) {
    	                    // Ako postoji, uzimamo ime, prezime i status korisnika
    	                    String firstName = userResultSet.getString("FirstName");
    	                    String lastName = userResultSet.getString("LastName");
    	                    String status = userResultSet.getString("Status");

    	                    // provjeravamo status korisnika
    	                    if (status.equals("Aktivan")) {
    	                        // Korisnik je već aktivan, ispisujemo poruku
    	                        lblIspisPoruka.setText("<html>Račun korisnika " + firstName + " " + lastName + " je već aktivan.</html>");
    	                    } else {
    	                        // Postavljamo ime i prezime u odgovarajuća polja
    	                        txtIme.setText(firstName);
    	                        txtPrezime.setText(lastName);

    	                        // Nakon toga prikazujemo dijaloški prozor za potvrdu aktivacije
    	                        int dialogResult = JOptionPane.showConfirmDialog(null,
    	                                "Da li ste sigurni da želite ponovo aktivirati račun osobe " + firstName + " " + lastName + "",
    	                                "Potvrda aktivacije", JOptionPane.YES_NO_OPTION);

    	                        if (dialogResult == JOptionPane.YES_OPTION) {
    	                            // Ovdje ažuriramo status osobe na 'Aktivan' ako je administrator potvrdio aktivaciju
    	                            String updateUserStatusQuery = "UPDATE Users SET Status = 'Aktivan' WHERE JMBG = ?";
    	                            PreparedStatement updateUserStatusStatement = dbConnection.prepareStatement(updateUserStatusQuery);
    	                            updateUserStatusStatement.setString(1, enteredJMBG);
    	                            int rowsAffected = updateUserStatusStatement.executeUpdate();
    	                            
    	                            //Ako je korisnicki racun uspješno aktiviran ispisujemo poruku i resetujemo polja.
    	                            if (rowsAffected > 0) {
    	                                lblIspisPoruka.setText("<html>Račun osobe " + firstName + " "+ lastName+ " je uspješno aktiviran.</html>");
    	                                txtIme.setText("");
    	                                txtPrezime.setText("");
    	                                txtJMBG.setText("");
    	                            } else {
    	                            	//Poruka za grešku prilikom aktivacije računa
    	                                lblIspisPoruka.setText("<html>Nastala je greška prilikom aktivacije računa.</html>");
    	                            }
    	                        }
    	                    }
    	                } else {
    	                	//Poruka ukoliko ne postoji korisnik sa unesenim JMBG-om.
    	                    lblIspisPoruka.setText("<html>Osoba sa unesenim JMBG-om ne postoji.</html>");
    	                }
    	            } catch (SQLException ex) {
    	                ex.printStackTrace();
    	                lblIspisPoruka.setText("Došlo je do problema s bazom podataka.");
    	            } finally {
    	                if (dbConnection != null) {
    	                    try {
    	                        dbConnection.close();
    	                    } catch (SQLException ex) {
    	                        ex.printStackTrace();
    	                    }
    	                }
    	            }
    	        }
    	    });
    	}

    	
    	/**
         * Akcijski slušatelj na dugmic za deaktivaciju korisnika.
         *
         * @param btnDeactivate    Dugmic za deaktivaciju
         * @param txtJMBG          Polje za unos JMBG-a
         * @param txtIme           Polje za ime korisnika
         * @param txtPrezime       Polje za prezime korisnika
         * @param lblIspisPoruka   Oznaka za ispis poruka
         */
    	
    	public static void addDeactivateActionListener(
    	        JButton btnDeactivate, JTextField txtJMBG, JTextField txtIme, JTextField txtPrezime, JLabel lblIspisPoruka) {
    		
    	    btnDeactivate.addActionListener(new ActionListener() {
    	        public void actionPerformed(ActionEvent e) {
    	        	
    	            // Dohvatamo JMBG koji je administrator unio
    	            String enteredJMBG = txtJMBG.getText();

    	            // Provjeravamo dužinu JMBG-a
    	            if (enteredJMBG.length() != 13) {
    	                lblIspisPoruka.setText("<html>JMBG mora imati tačno 13 karaktera.</html>");
    	                return;
    	            }

    	            Connection dbConnection = null;
    	            try {
    	                dbConnection = DriverManager.getConnection(sqlUrl);

    	                // Provjeravamo postoji li korisnik s unesenim JMBG-om u bazi podataka
    	                String checkUserQuery = "SELECT FirstName, LastName, Status FROM Users WHERE JMBG = ?";
    	                PreparedStatement checkUserStatement = dbConnection.prepareStatement(checkUserQuery);
    	                checkUserStatement.setString(1, enteredJMBG);
    	                ResultSet userResultSet = checkUserStatement.executeQuery();

    	                if (userResultSet.next()) {
    	                    // Ako postoji, uzimamo ime, prezime i status korisnika
    	                    String firstName = userResultSet.getString("FirstName");
    	                    String lastName = userResultSet.getString("LastName");
    	                    String status = userResultSet.getString("Status");

    	                    //Provjera statusa korisnika
    	                    if (status.equals("Deaktiviran")) {
    	                        // Korisnik je već deaktiviran, ispisujemo poruku
    	                        lblIspisPoruka.setText("<html>Račun korisnika " + firstName + " " + lastName + " je već deaktiviran.</html>");
    	                    } else {
    	                        // Postavljamo ime i prezime u odgovarajuća polja
    	                        txtIme.setText(firstName);
    	                        txtPrezime.setText(lastName);

    	                        // Nakon toga prikazujemo dijaloški prozor za potvrdu deaktivacije
    	                        int dialogResult = JOptionPane.showConfirmDialog(null,
    	                                "Da li ste sigurni da želite deaktivirati račun osobe " + firstName + " " + lastName + "",
    	                                "Potvrda deaktivacije", JOptionPane.YES_NO_OPTION);

    	                        if (dialogResult == JOptionPane.YES_OPTION) {
    	                            // Ovdje ažuriramo status osobe na 'Deaktiviran'
    	                            String updateUserStatusQuery = "UPDATE Users SET Status = 'Deaktiviran' WHERE JMBG = ?";
    	                            PreparedStatement updateUserStatusStatement = dbConnection.prepareStatement(updateUserStatusQuery);
    	                            updateUserStatusStatement.setString(1, enteredJMBG);
    	                            int rowsAffected = updateUserStatusStatement.executeUpdate();
    	                            
    	                            //ukoliko je uspješno deaktiviran ispisujemo poruku i resetujemo polja.
    	                            if (rowsAffected > 0) {
    	                                lblIspisPoruka.setText("<html>Račun osobe " + firstName + " "+ lastName+ " je uspješno deaktiviran.</html>");
    	                                txtIme.setText("");
    	                                txtPrezime.setText("");
    	                                txtJMBG.setText("");
    	                            } else {
    	                            	//Ispisujemo poruku ukoliko ne uspije deaktivacija računa.
    	                                lblIspisPoruka.setText("<html>Nastala je greška prilikom deaktivacije računa.</html>");
    	                            }
    	                        }
    	                    }
    	                } else {
    	                	//Ispisuje se poruka ukoliko se ne nadje osoba sa unesenim JMBG.
    	                    lblIspisPoruka.setText("<html>Osoba sa unesenim JMBG-om ne postoji.</html>");
    	                }
    	            } catch (SQLException ex) {
    	                ex.printStackTrace();
    	                lblIspisPoruka.setText("Došlo je do problema s bazom podataka.");
    	            } finally {
    	                if (dbConnection != null) {
    	                    try {
    	                        dbConnection.close();
    	                    } catch (SQLException ex) {
    	                        ex.printStackTrace();
    	                    }
    	                }
    	            }
    	        }
    	    });
    	}
}

