package bms.ATM;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;


/**
 * Klasa DepositButtonManager predstavlja upravljanje funkcionalnostima gumba "Deposit" na bankomatu.
 * 
 * Ova klasa omogućuje korisniku da izvrši depozit na svoj račun. Prilikom pritiska na gumb,
 * koristi se korisnički unos za iznos depozita koji se unosi kroz `userInput` komponentu.
 * Izvršava se provjera unesenog iznosa i poziva odgovarajuća metoda iz `UserManager` klase za obradu depozita.
 */

public class DepositButtonManager extends JButton {
	
	/**
     * Konstruktor za DepositButtonManager.
     * 
     * @param userInput Referenca na JTextArea komponentu koja sadrži korisnički unos iznosa depozita.
     * @param lblBotIspis Referenca na JLabel komponentu za ispis poruka korisniku.
     * @param userManager Objekt klase UserManager koji sadrži operacije za rad s računom.
     */
	
	public DepositButtonManager(JTextArea userInput, JLabel lblBotIspis, AtmUserManager userManager) {
		
		// Postavljanje teksta na dugmetu "Deposit".
		setText("Deposit");
		
		// Postavljanje pozicije i dimenzija dugmeta.
		setBounds(589, 185, 85, 45);
		
		addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	// Varijabla za uzimanje korisničkog unosa za usporedbu sa lozinkom u bazi podataka
		        String enteredPin = userManager.getEnteredPin();
		        // Provjerite je li unesen PIN
		        if (!enteredPin.isEmpty()) {
		            // Unos iznosa direktno iz userInput polja
		            String depositAmountStr = userInput.getText();
		             
		            // Provjerite je li uneseni iznos valjan
		            if (!depositAmountStr.isEmpty()) {
		                try {
		                	BigDecimal depositAmount = new BigDecimal(depositAmountStr);
		                    
		                    // Poziv metode iz UserManager klase za depozit
		                    boolean depositSuccess = userManager.deposit(depositAmount, lblBotIspis);
		                    
		                    if (depositSuccess) {
		                    	// Nakon uspješnog depozita, ispisujemo poruku
		                    	lblBotIspis.setText("Uspješno ste položili "+ depositAmount +" KM na vaš račun.");
		                    	userInput.setText("");
		                    }
		                } catch (NumberFormatException ex) {
		                    lblBotIspis.setText("Unesite ispravan iznos.");
		                }
		            } else {
		                lblBotIspis.setText("Unesite iznos koji želite položiti.");
		            }
		        } else {
		            lblBotIspis.setText("Unesite PIN prije nego što položite novac.");
		        }

		        
		    }
		});
		
		
	}

}
