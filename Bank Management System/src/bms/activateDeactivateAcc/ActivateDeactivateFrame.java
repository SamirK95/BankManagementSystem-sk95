package bms.activateDeactivateAcc;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import javax.swing.JTextField;
import javax.swing.JButton;


/**
 * Ova klasa predstavlja prozor za aktivaciju i deaktivaciju korisnickih racuna.
 */

public class ActivateDeactivateFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtJMBG;
	private JTextField txtIme;
	private JTextField txtPrezime;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ActivateDeactivateFrame frame = new ActivateDeactivateFrame();
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
	public ActivateDeactivateFrame() {

		setTitle("Deactivate User");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 558, 375);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		// Oznaka za ispis poruka
		JLabel lblIspisPoruka = new JLabel("Unesite JMBG korisnika");
		lblIspisPoruka.setBounds(203, 24, 167, 42);
		contentPane.add(lblIspisPoruka);
		
		// Oznaka za JMBG
		JLabel lblJMBG = new JLabel("JMBG");
		lblJMBG.setBounds(125, 91, 45, 13);
		contentPane.add(lblJMBG);
		
		// Oznaka za ime
		JLabel lblIme = new JLabel("Ime");
		lblIme.setBounds(125, 143, 45, 13);
		contentPane.add(lblIme);
		
		// Oznaka za prezime
		JLabel lblPrezime = new JLabel("Prezime");
		lblPrezime.setBounds(125, 194, 68, 13);
		contentPane.add(lblPrezime);
		
		// Polje za unos JMBG-a
		txtJMBG = new JTextField();
		txtJMBG.setBounds(203, 85, 167, 26);
		contentPane.add(txtJMBG);
		txtJMBG.setColumns(10);
		
		// Polje za prikaz imena korisnika sa pronadjenim JMBG-om.
		txtIme = new JTextField();
		txtIme.setEditable(false);
		txtIme.setColumns(10);
		txtIme.setBounds(203, 137, 167, 26);
		contentPane.add(txtIme);
		
		// Polje za prikaz prezimena korisnika sa pronadjenim JMBG-om.
		txtPrezime = new JTextField();
		txtPrezime.setEditable(false);
		txtPrezime.setColumns(10);
		txtPrezime.setBounds(203, 188, 167, 26);
		contentPane.add(txtPrezime);
		
		// Dugmic za odustajanje (Brise korisnicki unos).
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(63, 253, 106, 21);
		contentPane.add(btnCancel);
		
		// Dugmic za dekativaciju racuna korisnika.
		JButton btnDeactivate = new JButton("Deactivate User");
		btnDeactivate.setBounds(330, 253, 129, 21);
		contentPane.add(btnDeactivate);
		
		// Dugmic za aktivaciju racuna korisnika.
		JButton btnActivate = new JButton("Activate User");
		btnActivate.setBounds(191, 253, 129, 21);
		contentPane.add(btnActivate);
		
		// Akcijski slusatelj za dugmic Cancel koji resetira korisnicki unos.
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				txtJMBG.setText("");
			}
		});
		
		// Dodajemo akcijski slušatelj za aktiviranje korisnickog racuna (definicija u ActivateDeactivateUserManager klasi.
		ActivateDeactivateUserManager.addActivateActionListener(btnActivate, txtJMBG, txtIme, txtPrezime, lblIspisPoruka);
		
		// Dodajemo akcijski slušatelj za deaktiviranje korisnickog racuna (definicija u ActivateDeactivateUserManager klasi.
		ActivateDeactivateUserManager.addDeactivateActionListener(btnDeactivate, txtJMBG, txtIme, txtPrezime, lblIspisPoruka);
		
	}
}
