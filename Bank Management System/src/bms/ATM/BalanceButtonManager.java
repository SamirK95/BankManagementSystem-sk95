package bms.ATM;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;


/**
 * Klasa BalanceButtonManager predstavlja upravljanje funkcionalnostima gumba "Balance" na bankomatu.
 * 
 * Ova klasa omogućuje prikaz stanja računa korisnika kroz pozivanje odgovarajuće metode iz UserManager klase.
 */
public class BalanceButtonManager extends JButton {
	
	/**
     * Konstruktor za BalanceButtonManager.
     * 
     * @param lblBotIspis Referenca na JLabel komponentu za ispis poruka korisniku.
     * @param userManager Objekt klase UserManager koji sadrži operacije za rad s računom.
     */
	
	public BalanceButtonManager(JLabel lblBotIspis, AtmUserManager userManager) {
		// Postavljanje teksta na dugmetu "Balance".
		setText("Balance");
		
		// Postavljanje pozicije i dimenzija dugmeta.
		setBounds(589, 117, 85, 45);
		
		addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	
		    	// Pozivanje metode za dohvaćanje stanja računa iz UserManager klase
		        userManager.getAccountBalance(lblBotIspis);
		        
		    }
		});
	}

}
