package bms.ATM;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;


/**
 * Klasa EnterButtonManager predstavlja upravljanje funkcionalnostima gumba "Enter" na bankomatu.
 * 
 * Ova klasa omogućuje provjeru PIN-a, upravljanje depozitom i povlačenjem s računa te izvršava
 * odgovarajuće akcije na temelju trenutnog stanja korisničkog interfejsa.
 */
public class EnterButtonManager extends JButton {
	
	//Ovdje smo deklarisali i inicijalizovali varijablu pinEntered na false
	//Ova varijabla ce se mijenjati u ovisnosti da li je PIN unesen(true) ili nije(False).
	private boolean pinEntered = false;
	
	
	/**
     * Konstruktor za EnterButtonManager.
     * 
     * @param pinField   Referenca na JPasswordField komponentu za unos PIN-a.
     * @param userInput Referenca na JTextArea komponentu za unos korisnika.
     * @param lblBotIspis Referenca na JLabel komponentu za ispis poruka korisniku.
     * @param userManager Objekt klase UserManager koji sadrži operacije za rad s računom.
     */
	public EnterButtonManager(JPasswordField pinField, JLabel lblBotIspis, AtmUserManager userManager, JTextArea userInput ) {
		
		//Postavljamo tekst dugmica na "Enter".
		setText("Enter");
		//Postavljamo boju dugmica na zelenu.
        setBackground(new Color(0, 255, 0));
        
        //Polje za unos userInput sluzi za unos depozita ili podizanje novca
        //Za PIN koristimo pinField prema tome dok PIN nije validan userInput nije vidljiv
        //Ukoliko je PIN validan userInput ce biti vidljiv za dalje operacije.
        userInput.setVisible(false);
        
        addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	// Dohvaćanje trenutne operacije
		        String currentOperation = lblBotIspis.getText();		        
		        
		        // Ako PIN nije ispravno unesen, provjerava se ispravnost PIN-a
		        if (!pinEntered) {
		            // Poziv metode iz UserManager klase za provjeru PIN-a
		            boolean isPinValid = userManager.isPinValid(pinField, lblBotIspis);
		            
		            if (isPinValid) {
		                // PIN je ispravno unesen, postavlja se pinEntered na true
		                pinEntered = true;
		                
		                //Ako je PIN validan userInput text area za dalje operacije ce biti vidljiv
		                userInput.setVisible(true);
		                //Ako je PIN validan resetujemo polje userInput da bude prazno za dalje operacije
		                userInput.setText("");
		                //Ako je PIN validan polje za unos PIN-a stavljamo da ne bude vidljivo(false)
		                pinField.setVisible(false);
		            }
		        } else {
		            // Ako PIN je ispravno unesen, nastavlja se s ostalim operacijama

		            // Provjera trenutne operacije i izvođenje odgovarajućih akcija
		            if (currentOperation.equals("Unesite iznos koji želite položiti.")) {
		                // Trenutna operacija je depozit

		                // Dohvaćanje unesenog iznosa za depozit
		                String depositAmountStr = userInput.getText();

		                if (!depositAmountStr.isEmpty()) {
		                    try {
		                        // Pretvaranje unesenog teksta u decimalni broj (iznos depozita)
		                        BigDecimal depositAmount = new BigDecimal(depositAmountStr);

		                        // Poziv metode za depozit iz UserManager klase
		                        boolean depositSuccess = userManager.deposit(depositAmount, lblBotIspis);

		                        if (depositSuccess) {
		                            // Ako je depozit uspješan, ažurira se prikaz i briše se uneseni iznos
		                            lblBotIspis.setText("Uspješno ste položili " + depositAmount + " KM na vaš račun.");
		                            userInput.setText("");
		                        }
		                    } catch (NumberFormatException ex) {
		                        // Greška ako uneseni iznos nije ispravan decimalni broj
		                        lblBotIspis.setText("Unesite ispravan iznos.");
		                    }
		                } else {
		                    // Greška ako korisnik nije unio iznos prije pritiska na Enter
		                    lblBotIspis.setText("Unesite iznos prije nego što pritisnete Enter.");
		                }
		            }

		            // Provjera trenutne operacije za povlačenje s računa
		            if (currentOperation.equals("Unesite iznos koji želite podići sa računa.")) {
		                // Trenutna operacija je povlačenje s računa

		                // Dohvaćanje unesenog iznosa za povlačenje
		                String withdrawalAmountStr = userInput.getText();

		                if (!withdrawalAmountStr.isEmpty()) {
		                    try {
		                        // Pretvaranje unesenog teksta u decimalni broj (iznos povlačenja)
		                        BigDecimal withdrawalAmount = new BigDecimal(withdrawalAmountStr);

		                        // Poziv metode za povlačenje iz UserManager klase
		                        boolean withdrawSuccess = userManager.withdraw(withdrawalAmount, lblBotIspis);

		                        if (withdrawSuccess) {
		                            // Ako je povlačenje uspješno, ažurira se prikaz i briše se uneseni iznos
		                            lblBotIspis.setText("Uspješno ste podigli " + withdrawalAmount + " KM sa vašeg računa.");
		                            userInput.setText("");
		                        }
		                    } catch (NumberFormatException ex) {
		                        // Greška ako uneseni iznos nije ispravan decimalni broj
		                        lblBotIspis.setText("Unesite ispravan iznos.");
		                    }
		                } else {
		                    // Greška ako korisnik nije unio iznos prije pritiska na Enter
		                    lblBotIspis.setText("Unesite iznos prije nego što pritisnete Enter.");
		                }
		            }

		            // Dodatna akcija za nepokrivene slučajeve (može ostati prazna ili se dodati odgovarajuća akcija)
		            else {
		                // Ovdje se mogu dodati dodatne akcije ili rukovanje nepokrivenim slučajevima
		            }
		        }
		        
		        
            }
        });
        
	}

}
