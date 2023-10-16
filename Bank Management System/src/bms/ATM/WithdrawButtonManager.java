package bms.ATM;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;


/**
 * Klasa WithdrawButtonManager predstavlja upravljanje funkcionalnostima dugmica "Withdraw" na bankomatu.
 * 
 * Ova klasa omogućuje korisniku da izvrši povlačenje s računa. Prilikom pritiska na gumb,
 * koristi se korisnički unos za iznos povlačenja koji se unosi kroz `userInput` komponentu.
 * Izvršava se provjera unesenog iznosa i poziva odgovarajuća metoda iz `UserManager` klase za obradu povlačenja.
 */

public class WithdrawButtonManager extends JButton {
	
	/**
     * Konstruktor za WithdrawButtonManager.
     * 
     * @param userInput Referenca na JTextArea komponentu koja sadrži korisnički unos iznosa povlačenja.
     * @param lblBotIspis Referenca na JLabel komponentu za ispis poruka korisniku.
     * @param userManager Objekt klase UserManager koji sadrži operacije za rad s računom.
     */
	
	public WithdrawButtonManager(JTextArea userInput, JLabel lblBotIspis, AtmUserManager userManager) {
		
		// Postavljanje teksta na dugmetu "Withdraw".
		setText("Withdraw");
		
		// Postavljanje pozicije i dimenzija dugmeta.
		setBounds(589, 247, 85, 45);
		
		addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        // Dohvatamo uneseni PIN iz klase userManager.
		        String enteredPin = userManager.getEnteredPin();
		        // Ukoliko enteredPin nije prazan nastavljamo radnju 
		        if (!enteredPin.isEmpty()) {
		            // uzimamo iznos od korisnika
		            String withdrawalAmountStr = userInput.getText();

		            // ukoliko korisnik nije ostavio prazno polje nastavljamo s operacijama
		            if (!withdrawalAmountStr.isEmpty()) {

		                try {
		                    // konvertujemo String u BigDecimal tip podatka 
		                    BigDecimal withdrawalAmount = new BigDecimal(withdrawalAmountStr);

		                    // Poziv metode za povlačenje s računa koristeći BigDecimal
		                    boolean withdrawSuccess = userManager.withdraw(withdrawalAmount, lblBotIspis);

		                    if (withdrawSuccess) {
		                        lblBotIspis.setText("Uspješno ste podigli " + withdrawalAmount + " KM sa vašeg računa.");
		                        userInput.setText("");
		                    }
		                } catch (NumberFormatException ex) {
		                    lblBotIspis.setText("Unesite ispravan iznos.");
		                }
		            } else {
		                lblBotIspis.setText("Unesite iznos koji želite podići sa računa.");
		            }
		        } else {
		            lblBotIspis.setText("Unesite PIN prije nego što podignete novac.");
		        }
		        
		    }
		});
		
	}

}
