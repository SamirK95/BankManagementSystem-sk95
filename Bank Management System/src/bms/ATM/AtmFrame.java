package bms.ATM;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.GridLayout;
import java.awt.Panel;

import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;


public class AtmFrame extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	//JPanel komponenta koja predstavlja glavni sadržaj korisničkog interfejsa.
	private JPanel contentPane;
	
	//URL za pristup SQL bazi podataka.
	String sqlUrl = "jdbc:sqlserver://SAMIR\\SQLEXPRESS:1433;databaseName=ATMClients;user=samir;password=samir123;;encrypt=true;trustServerCertificate=true";
	
	//Deklaracija Text Area u koje ce korisnik unositi iznose.
	public JTextArea userInput;
	
	//Deklaracija Password polje u koje ce korisnik unositi PIN.
	public JPasswordField pinField;
	
	
	/**
	 * Launch the application.
	 * 
	 */

public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
	        public void run() {
	            try {
	            	// Kreiramo novu instancu glavnog okvira (MainFrame).
	            	AtmFrame frame = new AtmFrame();
	            	
	            	// Postavljamo glavni okvir na vidljiv (prikazujemo ga korisniku).
	                frame.setVisible(true);
	                
	             // U slučaju iznimke (exception), ispisujemo je na konzolu.
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
	    });

	}
	
	/**
	 * Create the frame.
	 */
	public AtmFrame() {
		
		//Postavljamo naziv aplikacije
		setTitle("ATM");
		
		
		//Postavljanje zatvaranja prozora kad korisnik zatvori glavni prozor.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		//Postavljanje dimenzija i pozicije glavnog prozora.
		setBounds(100, 100, 758, 527);
		
		
		//Postavljanje glavnog sadržaja prozora.
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		//Postavljanje glavnog panela unutar prozora.
		Panel mainPanel = new Panel();
		mainPanel.setBounds(0, 10, 744, 422);
		contentPane.add(mainPanel);
		mainPanel.setLayout(null);
		
		
		//Postavljanje panela za brze izbore.
		JPanel fastChoicePanel = new JPanel();
		fastChoicePanel.setBackground(new Color(192, 192, 192));
		fastChoicePanel.setBounds(164, 22, 411, 270);
		mainPanel.add(fastChoicePanel);
		
		//Label za ispis poruka korisniku.
		JLabel lblBotIspis = new JLabel("");
				
		
		//Objekt za upravljanje korisničkim podacima.
        AtmUserManager userManager = new AtmUserManager();
        userManager.setLblBotIspis(lblBotIspis);
		
		//Panel za prikaz gumba za akcije.
        //Brojevi od (0 - 9) i dugmic cancel se nalaze u ovom panelu.
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new GridLayout(4, 0, 2, 2));
		
		
		// Stvaranje i inicijalizacija ButtonManager objekta za upravljanje brojevnim dugmicima (0-9) i Cancel dugmicem
		AtmUIButtonManager buttonManager = new AtmUIButtonManager(this);
		
		//Polje za unos iznosa od strane korisnika za depozit i podizanje novca.
		userInput = new JTextArea();
				
		//Password polje u koje ce korisnik unositi PIN.
		pinField = new JPasswordField();

		// Dodavanje brojevnih tipki (0-9) i Cancel tipke u buttonsPanel komponentu
		buttonsPanel.add(buttonManager.getButton1());
		buttonsPanel.add(buttonManager.getButton2());
		buttonsPanel.add(buttonManager.getButton3());
		buttonsPanel.add(buttonManager.getButton4());
		buttonsPanel.add(buttonManager.getButton5());
		buttonsPanel.add(buttonManager.getButton6());
		buttonsPanel.add(buttonManager.getButton7());
		buttonsPanel.add(buttonManager.getButton8());
		buttonsPanel.add(buttonManager.getButton9());
		buttonsPanel.add(buttonManager.getButtonCancel());
		buttonsPanel.add(buttonManager.getButton0());

		
		
		/**
		 * Dodavanje dodatnih tipki (10, 20, 50, 100, 200) u mainPanel komponentu 
		 * One ce sluziti recimo za brzo podizanje ili polaganje novca
		 * brojevi (10, 20, 50, 100, 200) kada korisnik klikne dugmic taj broj se prikaze u korisnickom unosu.
		 * Ovi brojevi za brzi unos nece biti dostupni za unos PIN-a
		 * */
		mainPanel.add(buttonManager.getButton10());
		mainPanel.add(buttonManager.getButton20());
		mainPanel.add(buttonManager.getButton50());
		mainPanel.add(buttonManager.getButton100());
		mainPanel.add(buttonManager.getButton200());

		
		// Stvaranje i inicijalizacija EnterButtonManager objekta za upravljanje Enter dugmicem
		EnterButtonManager enterManager = new EnterButtonManager(pinField, lblBotIspis, userManager, userInput);
		// Dodavanje Enter dugmica u buttonsPanel komponentu
		buttonsPanel.add(enterManager);
		

		// Stvaranje i inicijalizacija BalanceButtonManager objekta za upravljanje Balance (stanje računa) dugmicem
		BalanceButtonManager balanceManager = new BalanceButtonManager(lblBotIspis, userManager);
		// Dodavanje Balance dugmica u mainPanel komponentu
		mainPanel.add(balanceManager);
		

		// Stvaranje i inicijalizacija DepositButtonManager objekta za upravljanje Deposit (polaganje) dugmice
		DepositButtonManager depositManager = new DepositButtonManager(userInput, lblBotIspis, userManager);
		// Dodavanje Deposit dugmica u mainPanel komponentu
		mainPanel.add(depositManager);
		

		// Stvaranje i inicijalizacija WithdrawButtonManager objekta za upravljanje Withdraw (podizanje) dugmicem
		WithdrawButtonManager withdrawManager = new WithdrawButtonManager(userInput, lblBotIspis, userManager);
		// Dodavanje Withdraw dugmica u mainPanel komponentu
		mainPanel.add(withdrawManager);
		
		
		//Ovo je prva poruka koja se prikazuje korisniku kada otvori aplikaciju.
		lblBotIspis.setText("Please enter your PIN.");
		
		
		//Postavljanje rasporeda komponenata u "fastChoicePanel" pomoću GroupLayout-a.
		GroupLayout gl_fastChoicePanel = new GroupLayout(fastChoicePanel);
		gl_fastChoicePanel.setHorizontalGroup(
			gl_fastChoicePanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_fastChoicePanel.createSequentialGroup()
					.addGroup(gl_fastChoicePanel.createParallelGroup(Alignment.LEADING)
						.addComponent(buttonsPanel, GroupLayout.PREFERRED_SIZE, 411, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_fastChoicePanel.createSequentialGroup()
							.addContainerGap()
							.addComponent(userInput, GroupLayout.PREFERRED_SIZE, 392, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_fastChoicePanel.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblBotIspis, GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE))
						.addGroup(gl_fastChoicePanel.createSequentialGroup()
							.addContainerGap()
							.addComponent(pinField, GroupLayout.PREFERRED_SIZE, 392, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		gl_fastChoicePanel.setVerticalGroup(
			gl_fastChoicePanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_fastChoicePanel.createSequentialGroup()
					.addGap(51)
					.addComponent(lblBotIspis)
					.addGap(18)
					.addGroup(gl_fastChoicePanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(userInput, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
						.addComponent(pinField, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 64, Short.MAX_VALUE)
					.addComponent(buttonsPanel, GroupLayout.PREFERRED_SIZE, 124, GroupLayout.PREFERRED_SIZE))
		);
		//Postavljanje rasporeda komponenata "fastChoicePanel" na sam "fastChoicePanel".
		fastChoicePanel.setLayout(gl_fastChoicePanel);
		

	}
	
	
}
