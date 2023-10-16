package bms.addNewUser;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import bms.activateDeactivateAcc.ActivateDeactivateFrame;
import bms.searchUsers.SearchUsersFrame;
import bms.transactionReports.SearchTransactionsFrame;

import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.Color;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class AddNewUserFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtFirstName;
	private JTextField txtLastName;
	private JTextField txtAccBalance;
	private JPasswordField passFieldMain;
	private JPasswordField passFieldSec;
	//private JLabel lblIspisPoruke;
	private AddNewUserManager addNewUser;
	private JTextField txtJMBG;
	
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AddNewUserFrame frame = new AddNewUserFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public AddNewUserFrame() {
		
		/***
		 *  Ovdje definišemo izgled prozora u kojoj se nalazi naša forma za registraciju
		 */
		setTitle("Add new user");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 715, 485);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		/**
		 * Ovdje definišemo sve labele, polja za unos i dugmiće kao što je izgled, dimenzije i slično.
		 */
		
		//Definišemo label za ime
		JLabel lblFirstName = new JLabel("First Name");
		lblFirstName.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblFirstName.setBounds(191, 85, 89, 28);
		contentPane.add(lblFirstName);
		
		//Definišemo label za prezime
		JLabel lblLastName = new JLabel("Last Name");
		lblLastName.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblLastName.setBounds(191, 135, 89, 28);
		contentPane.add(lblLastName);
		
		//Definišemo label za stanje racuna
		JLabel lblStanjeRacuna = new JLabel("Account Balance");
		lblStanjeRacuna.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblStanjeRacuna.setBounds(191, 232, 95, 28);
		contentPane.add(lblStanjeRacuna);
		
		//Definišemo label za lozinku 
		JLabel lblLozinka = new JLabel("Password");
		lblLozinka.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblLozinka.setBounds(191, 281, 89, 28);
		contentPane.add(lblLozinka);
		
		//Definišemo label za ponavljanje lozinke
		JLabel lblReLozinka = new JLabel("Re-Enter Password");
		lblReLozinka.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblReLozinka.setBounds(191, 335, 106, 28);
		contentPane.add(lblReLozinka);
		
		//Definišemo label za ispis poruka korisniku
		JLabel lblIspisPoruke = new JLabel("");
		lblIspisPoruke.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblIspisPoruke.setBounds(191, 28, 316, 47);
		contentPane.add(lblIspisPoruke);
		
		//Definišemo label za JMBG
		JLabel lblJMBG = new JLabel("JMBG");
		lblJMBG.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblJMBG.setBounds(191, 190, 89, 28);
		contentPane.add(lblJMBG);
		
		/**
		 * Ovdje postavljamo polja za unos, dimenzije i slicno
		 */
		
		//Definišemo polje za unos imena
		txtFirstName = new JTextField();
		txtFirstName.setBounds(307, 89, 200, 23);
		contentPane.add(txtFirstName);
		txtFirstName.setColumns(10);
		
		//Definišemo polje za unos prezimena
		txtLastName = new JTextField();
		txtLastName.setColumns(10);
		txtLastName.setBounds(307, 139, 200, 23);
		contentPane.add(txtLastName);
		
		//Definišemo polje za unos stanja racuna korisnika
		txtAccBalance = new JTextField();
		txtAccBalance.setColumns(10);
		txtAccBalance.setBounds(307, 237, 200, 23);
		contentPane.add(txtAccBalance);
		
		//Definišemo polje za unos passworda
		passFieldMain = new JPasswordField();
		passFieldMain.setBounds(307, 287, 200, 22);
		contentPane.add(passFieldMain);
		
		//Definišemo polje za unos potvrde unesenog passworda
		passFieldSec = new JPasswordField();
		passFieldSec.setBounds(307, 341, 200, 22);
		contentPane.add(passFieldSec);
		
		//Definišemo polje za unos JMBG-a
		txtJMBG = new JTextField();
		txtJMBG.setColumns(10);
		txtJMBG.setBounds(307, 194, 200, 23);
		contentPane.add(txtJMBG);
		
		// Kreiranje meni trake za izbor operacija
	    JMenuBar menuBar = new JMenuBar();
	    setJMenuBar(menuBar); // Postavljanje meni trake na prozor
		
		
		//Postavljamo početnu poruku kada se otvori prozor za registraciju korisnika
		lblIspisPoruke.setText("Unesite novog korisnika bankomata.");
		
		
		addNewUser = new AddNewUserManager(txtFirstName, txtLastName, txtJMBG, txtAccBalance, passFieldMain, passFieldSec, lblIspisPoruke);
		
		//Definišemo akcijski slušatelj na dodir Cancel dugmića
		//Kada korisnik pritisne dugme sve prethodno uneseno u polja bit će obrisano(resetirano)
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setForeground(new Color(0, 0, 0));
		btnCancel.setBackground(new Color(255, 128, 0));
		btnCancel.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	        	//Brišemo prethodni unos u polju
	            txtFirstName.setText("");
	            
	            //Brišemo prethodni unos u polju
	            txtLastName.setText("");
	            
	            //Brišemo prethodni unos u polju
	            txtJMBG.setText("");
	            
	            //Brišemo prethodni unos u polju
	            txtAccBalance.setText("");
	            
	            //Brišemo prethodni unos u polju
	            passFieldMain.setText("");
	            
	            //Brišemo prethodni unos u polju
	            passFieldSec.setText("");
	        }
	    });
		btnCancel.setBounds(287, 384, 85, 21);
		contentPane.add(btnCancel);
		
		
		//Kada korisnik klikne Submit dugmić pozivamo metodu za obradu registracije
		//Metoda je definisana u AddNewUserManager Klasi 
		JButton btnSubmit = new JButton("Register");
		btnSubmit.setBackground(new Color(0, 128, 0));
		btnSubmit.setForeground(new Color(0, 0, 0));
		btnSubmit.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	            // Ovdje pozivamo metodu za obradu registracije
	        	addNewUser.handleUserRegistration();
	        }
	    });
		btnSubmit.setBounds(422, 384, 85, 21);
		contentPane.add(btnSubmit);
		
		// Kreiranje menija za izbor različitih operacija
		JMenu switchOperationsMenu = new JMenu("Choose Operation");
		switchOperationsMenu.setBounds(10, 0, 141, 24);		
				
		// Kreiranje stavke menija za izveštaj o transakcijama
        JMenuItem transactionReportMenuItem = new JMenuItem("Transaction Reports");
        transactionReportMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	// Kreiranje instance prozora za izveštaj o transakcijama
                SearchTransactionsFrame reportFrame = new SearchTransactionsFrame();
                
                //Postavljamo zatvaranje forme za aktivaciju/deaktivaciju bez uticaja na glavnu formu.
                reportFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);    
                reportFrame.setVisible(true);
            }
        });
        
        // Kreiranje stavke menija za pretragu korisnika
        JMenuItem searchUsersMenuItem = new JMenuItem("Search Users");
        searchUsersMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SearchUsersFrame usersFrame = new SearchUsersFrame();
                //Postavljamo zatvaranje forme za aktivaciju/deaktivaciju bez uticaja na glavnu formu.
                usersFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                usersFrame.setVisible(true);
            }
        });
        
        // Kreiranje stavke menija za aktivaciju/deaktivaciju računa
        JMenuItem activateDeactivateAcc = new JMenuItem("Activate/Deactivate Account");
        activateDeactivateAcc.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ActivateDeactivateFrame accountsFrame = new ActivateDeactivateFrame();
                //Postavljamo zatvaranje forme za aktivaciju/deaktivaciju bez uticaja na glavnu formu.
                accountsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                accountsFrame.setVisible(true);
            }
        });
		
        // Dodavanje svih stavki menija u meni za izbor operacija
        switchOperationsMenu.add(transactionReportMenuItem);
        switchOperationsMenu.add(searchUsersMenuItem);
        switchOperationsMenu.add(activateDeactivateAcc);
        
        
        // Dodavanje menija za izbor operacija u glavni meni traku
        menuBar.add(switchOperationsMenu);
		
	}
}
