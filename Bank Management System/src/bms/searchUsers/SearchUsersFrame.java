package bms.searchUsers;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import bms.dbLoaderPackage.DbLoader;

import javax.swing.JButton;
import javax.swing.JComboBox;

public class SearchUsersFrame extends JFrame {
	
	/*
	 * Inicijalizacija tekstualnih polja za unos,
	 * tabele za prikaz rezultata, comboBox(dropDown)
	 * za odabir kriterija i SearchUsersManager klase
	 * koja je zaduzena za sve funkcionalnosti.
	 */
	private static final long serialVersionUID = 1L;
	private JTextField txtCriteria;
	private JTextField txtCriteriaStanje;
	private JTable tblResults = new JTable();
    private JComboBox<String> cmbSearchCriteria;
    private SearchUsersManager searchManager;
    
    // kreiramo instancu klase da bi ucitali url baze podataka
 	static DbLoader dbLoader = new DbLoader();
 	// Pozivamo metod za dobijanje url-a baze podataka i spremamo u string
 	static String sqlUrl = dbLoader.getDatabaseUrl();
	

	/**
	 * Kreiramo okvir
	 */
	public SearchUsersFrame() {
		setTitle("Pretraga korisnika");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        
        // Labels, tekstualna polja i dugme za pretragu
        JLabel lblCriteria = new JLabel("Unesite kriterijum pretrage:");
        txtCriteria = new JTextField(15);
        txtCriteriaStanje = new JTextField(15);
        JButton btnSearch = new JButton("Pretraži");
        
        // Padajući meni sa opcijama pretrage
        String[] searchOptions = {"All", "JMBG", "StanjeRacuna", "FirstName", "LastName", "Status"};
        cmbSearchCriteria = new JComboBox<>(searchOptions);
        
        // Labels za dodatne kriterijume pretrage kada kliknemo Stanje Racuna
        JLabel lblStanjeOd = new JLabel("Od");
        JLabel lblStanjeDo = new JLabel("Do");
        
        // Panel za unos i pretragu
        JPanel searchPanel = new JPanel();
        searchPanel.add(lblCriteria);
        searchPanel.add(cmbSearchCriteria);
        searchPanel.add(lblStanjeOd);
        searchPanel.add(txtCriteria);
        searchPanel.add(lblStanjeDo);
        searchPanel.add(txtCriteriaStanje);
        searchPanel.add(btnSearch);
        
        /*
         * Postavljamo ova polja na false da ne budu vidljiva kad otvorimo aplikaciju
         * 
         * Prvo sto se otvori je Kriterija iz drop down All
         * za ovaj kriterij ne unose se rucni kriterijumi zato je ovo polje iskljuceno
         * 
         * txtCriteriaStanje, lblStanjeOd, lblStanjeDo je iskljuceno jer ce biti
         * samo dostupno kada se odabere Stanje Racuna iz drop down-a 
         * da bi korisnik mogao unositi vrijednost OD odredjene cifre DO odredjene cifre.
         */
	    txtCriteriaStanje.setVisible(false);
	    lblStanjeOd.setVisible(false);
	    lblStanjeDo.setVisible(false);
	    txtCriteria.setEnabled(false);
        
	    // Dodavanje panela za unos u glavni kontejner
        contentPane.add(searchPanel, BorderLayout.NORTH);
        
        /*
         * Ovaj slusac dogadjaja nam omogucava da dodatno polje za kriterijum i labels
         * budu dostupni samo kada kliknemo na Stanje Racuna kao kriterijum
         * u drugim slucajevima nece biti vidljivi.
         */
        cmbSearchCriteria.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if ("StanjeRacuna".equals(cmbSearchCriteria.getSelectedItem())) {
                    txtCriteriaStanje.setVisible(true);
                    lblStanjeOd.setVisible(true);
                    lblStanjeDo.setVisible(true);
                } else {
                    txtCriteriaStanje.setVisible(false);
                    lblStanjeOd.setVisible(false);
                    lblStanjeDo.setVisible(false);
                }
            }
        });
        
        /*
         * Ovaj slusac dogadjaja smo definisali tako da 
         * u slucaju odabira kriterijuma iz dropDown "All"
         * prikazat ce sve korisnike iz baze podataka bez mogucnosti 
         * dodavanja dodatnih kriterijuma od strane korisnika.
         * u svim drugim slucajevima dodatni kriterijum ce biti dostupan.
         */
        cmbSearchCriteria.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedCriteria = (String) cmbSearchCriteria.getSelectedItem();

                // Ako je izabrana opcija "All", onemogućite polje za unos teksta
                if ("All".equals(selectedCriteria)) {
                    txtCriteria.setEnabled(false);
                } else {
                    txtCriteria.setEnabled(true);
                }
            }
        });
        
        /*
         * Inicijalizacija menadžera za pretragu 
         * 
         * SearchUsersManager je klasa u kojoj su definisane sve funkcionalnosti 
         * i ograničenja vezana za sve kriterijume pretraživanja.
         */
        searchManager = new SearchUsersManager(sqlUrl, txtCriteria, txtCriteriaStanje, tblResults, cmbSearchCriteria);
        
        /*
         * Ovaj slusac dogadjaja je glavni i koristi se za pretrazivanje po kriterijima.
         * 
         * Ovdje dohvatamo kriterij koji je korisnik odabrao @param cmbSearchCriteria.
         * Zatim dohvatamo vrijednost za dodatni kriterij koji je korisnik rucno unjeo @param criteria.
         * Zatim dohvatamo vrijednost za dodatni kriterij koji ce korisniku biti dostupan samo kada 
         * odabere Stanje Racuna kao kriterij iz drop down-a.
         */
        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Dohvatamo odabrani kriterij iz drop down-a.
                String selectedCriteria = (String) cmbSearchCriteria.getSelectedItem();

                // Dohvatamo korisnicki unos za dodatni kriterij
                String criteria = txtCriteria.getText();
                // Dohvatamo korisnicki unos za dodatni kriterij(Stanje Racuna)
                String criteriaStanje = txtCriteriaStanje.getText();
                
                // Pozivamo metodu za pretragu na osnovu izabrane opcije iz SearchUsersManager klase
                DefaultTableModel tableModel = searchManager.searchByCriteria(selectedCriteria, criteria, criteriaStanje);
                tblResults.setModel(tableModel);


            }
        });

        // Dodavanje tabele za prikaz rezultata u glavni kontejner
        JScrollPane scrollPane = new JScrollPane(tblResults);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        setContentPane(contentPane);
    }
	
	/*
	 * Pokrecemo aplikaciju.
	 */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    SearchUsersFrame frame = new SearchUsersFrame();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

