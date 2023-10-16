package bms.administratorLogin;


import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import bms.addNewUser.AddNewUserFrame;

import javax.swing.JLabel;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;


//Klasa koja predstavlja prozor za prijavu administratora
public class AdministratorFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtUserName;
	private JPasswordField passInput;
	
	
	AddNewUserFrame addUser = new AddNewUserFrame();
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AdministratorFrame frame = new AdministratorFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Kreira novi prozor za prijavu administratora.
	 */
	public AdministratorFrame() {
		setTitle("Administrator Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 605, 423);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		// Tekstualna oznaka za unos korisničkog imena
		JLabel lblUserName = new JLabel("Username ");
		lblUserName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblUserName.setBounds(138, 123, 88, 24);
		contentPane.add(lblUserName);
		
		// Tekstualna oznaka za unos lozinke
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblPassword.setBounds(138, 195, 88, 24);
		contentPane.add(lblPassword);
		
		// Polje za unos korisničkog imena
		txtUserName = new JTextField();
		txtUserName.setBounds(236, 123, 214, 30);
		contentPane.add(txtUserName);
		txtUserName.setColumns(10);
		
		// Polje za unos lozinke
		passInput = new JPasswordField();
		passInput.setBounds(236, 189, 214, 30);
		contentPane.add(passInput);
		
		// Tekstualna oznaka za ispis poruka korisniku
		JLabel lblIspisPoruka = new JLabel("New label");
		lblIspisPoruka.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblIspisPoruka.setBounds(236, 65, 214, 24);
		contentPane.add(lblIspisPoruka);
		
		
		// Dugme za potvrdu unosa
		JButton btnSubmit = new JButton("Submit");
		btnSubmit.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	// Dohvati uneseno korisničko ime i lozinku
                String enteredUsername = txtUserName.getText();
                char[] enteredPasswordChars = passInput.getPassword();
                String enteredPassword = new String(enteredPasswordChars);

                // Provjeri korisničko ime i lozinku s bazom podataka
                if (AdminManager.authenticateAdmin(enteredUsername, enteredPassword)) {
                	lblIspisPoruka.setForeground(Color.GREEN);
                    lblIspisPoruka.setText("Pristup odobren! Možete nastaviti.");
                    // Ovdje otvoramo novi prozor za registraciju novog korisnika jer je prijava uspješna.
                    addUser.setVisible(true);
                    
                }
                else if(enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
                	lblIspisPoruka.setForeground(Color.RED);
                	lblIspisPoruka.setText("Oba polja su obavezna za unos!");
                }
                else {
                	lblIspisPoruka.setForeground(Color.RED);
                    lblIspisPoruka.setText("Pogrešan unos. Pokušajte ponovo!");
                }
		    }
		});
		btnSubmit.setBounds(365, 245, 85, 21);
		contentPane.add(btnSubmit);
		
		
		// Dugme za resetiranje unosa
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        // Resetiramo polja za unos korisničkog imena i lozinke nakon klika na Cancel
		        txtUserName.setText("");
		        passInput.setText("");
		    }
		});
		btnCancel.setBounds(236, 245, 85, 21);
		contentPane.add(btnCancel);
		
		// Prikazivanje početne poruke kada se otvori prozor.
		lblIspisPoruka.setText("Unesite username i password.");
	}
}
