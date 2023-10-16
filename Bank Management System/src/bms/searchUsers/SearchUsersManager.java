package bms.searchUsers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 * Klasa SearchUsersManager upravlja pretragom korisnika u bazi podataka
 * na osnovu različitih kriterijuma i prikazuje rezultate u JTable komponenti.
 */
public class SearchUsersManager {
    private String sqlUrl; 							// URL za pristup bazi podataka
    private SearchUsersFrame searchUsersFrame;  	// incijalizacija klase sa varijablama i okvirom
    private JTextField txtCriteria; 				// Osnovno polje za unos kriterija(korisnik)
	private JTextField txtCriteriaStanje; 			// dodatno polje za unos kriterijuma (Stanje Racuna)
    private JTable tblResults; 						// Tabela za prikaz rezultata pretrage
    private JComboBox<String> cmbSearchCriteria;	// Padajući meni za odabir kriterijuma pretrage
    
    /**
     * Konstruktor klase.
     * 
     * @param sqlUrl             URL za uspostavljanje veze sa bazom podataka.
     * @param txtCriteria        Textualno polje za unos kriterijuma.
     * @param txtCriteriaStanje  Textualno polje za unos dodatnog kriterijuma (stanje računa).
     * @param tblResults         Tabela za prikaz rezultata pretrage.
     * @param cmbSearchCriteria  Padajući meni za odabir kriterijuma pretrage.
     */
    
    public SearchUsersManager(String sqlUrl, JTextField txtCriteria, JTextField txtCriteriaStanje,
    		JTable tblResults, JComboBox<String> cmbSearchCriteria) {
        this.sqlUrl = sqlUrl;
        this.txtCriteria = txtCriteria;
        this.txtCriteriaStanje = txtCriteriaStanje;
        this.tblResults = tblResults;
        this.cmbSearchCriteria = cmbSearchCriteria;
        
    }
    
    /**
     * Vrši pretragu korisnika na osnovu izabranog kriterijuma i unesenih kriterijuma.
     * 
     * @param selectedCriteria Izabrani kriterijum pretrage.
     * @param criteria         Kriterijum za pretragu (JMBG, Ime, Prezime, Stanje racuna, Status).
     * @param criteriaStanje   Dodatni kriterijum za pretragu (stanje računa).
     * @return DefaultTableModel koji sadrži rezultate pretrage za prikaz u tabeli.
     */
    
    public DefaultTableModel searchByCriteria(String selectedCriteria, String criteria, String criteriaStanje) {
    	
        DefaultTableModel tableModel = new DefaultTableModel();

        //Prije početka postavljamo konekciju na null
        Connection connection = null;
        
        /*
         * Polje za korisnicko tekstualni kriterij resetujemo na prazno polje
         * da bi nakon svake pretrage to polje bilo prazno.
         */
        txtCriteria.setText("");
        
        // Ako korisnik odabere All kao kriterijum
        if ("All".equals(selectedCriteria)) {
            try {
            	// Pokušavamo dohvatiti konekciju sa bazom podataka
                connection = DriverManager.getConnection(sqlUrl);

                // Izvršavamo SQL upit koji dohvata sve korisnike iz baze podataka bez dodatnog kriterijuma.
                String sqlQuery = "SELECT FirstName, LastName, JMBG, StanjeRacuna, Status FROM Users";
                PreparedStatement prepareStatement = connection.prepareStatement(sqlQuery);
                ResultSet resultSet = prepareStatement.executeQuery();

                // Kreiramo model za tabelu
                tableModel = new DefaultTableModel();
                tblResults.setModel(tableModel);

                // Dodajemo kolone u model za tabelu
                tableModel.addColumn("First Name");
                tableModel.addColumn("Last Name");
                tableModel.addColumn("JMBG");
                tableModel.addColumn("Stanje Racuna");
                tableModel.addColumn("Status");

                // Ako smo dohvatili podatke popunjavamo tabelu sa podacima iz baze.
                while (resultSet.next()) {
                    String firstName = resultSet.getString("FirstName");
                    String lastName = resultSet.getString("LastName");
                    String jmbg = resultSet.getString("JMBG");
                    double stanjeRacuna = resultSet.getDouble("StanjeRacuna");
                    String status = resultSet.getString("Status");
                    txtCriteria.setEnabled(false);

                    // Dodajemo red u tabelu sa podacima
                    tableModel.addRow(new Object[]{firstName, lastName, jmbg, stanjeRacuna, status});
                }

                // Zatvaramo resurse
                resultSet.close();
                prepareStatement.close();
                connection.close();
            } 
            catch (Exception ex) {
            	//Ukoliko dodje do greske ispisujemo je.
            	System.out.println("Greška u SearchUsersManager 'All'");
                ex.printStackTrace();
            }
        } 
        
        //Ukoliko je korisnik odabrao JMBG kao kriterij za pretragu
        else if ("JMBG".equals(selectedCriteria)) {
            // Provjeravamo duzinu unesenog JMBG mora biti (13) karaktera
            if (criteria.length() != 13) {
                JOptionPane.showMessageDialog(searchUsersFrame, "JMBG mora imati 13 karaktera.");
            } 
            else {
                try {
                	// Pokušavamo uspostaviti konekciju sa bazom podataka.
                    connection = DriverManager.getConnection(sqlUrl);

                    // Izvršavamo SQL upit za dohvatanje korisnika po JMBG-u koji korisnik unosi u txtCriteria.
                    String sqlQuery = "SELECT FirstName, LastName, JMBG, StanjeRacuna, Status FROM Users WHERE JMBG = ?";
                    PreparedStatement prepareStatement = connection.prepareStatement(sqlQuery, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    prepareStatement.setString(1, criteria);
                    ResultSet resultSet = prepareStatement.executeQuery();

                    // Kreiramo model za tabelu
                    tableModel = new DefaultTableModel();
                    tblResults.setModel(tableModel);

                    // Dodajemo kolone u model za tabelu
                    tableModel.addColumn("First Name");
                    tableModel.addColumn("Last Name");
                    tableModel.addColumn("JMBG");
                    tableModel.addColumn("Stanje Racuna");
                    tableModel.addColumn("Status");

                    // Ukoliko ne postoji korisnik s unesenim JMBG-om u bazi podataka, obavjestavamo korisnika.
                    if (!resultSet.next()) {
                        JOptionPane.showMessageDialog(searchUsersFrame, "Korisnik sa tim JMBG-om nije u bazi podataka.");
                    } 
                    else {
                    	// Vracamo se na prvi red rezultata.
                        resultSet.beforeFirst();
                        // Ukoliko su pronadjeni rezultati popunjavmao tabelu sa podacima iz baze
                        while (resultSet.next()) {
                            String firstName = resultSet.getString("FirstName");
                            String lastName = resultSet.getString("LastName");
                            String jmbg = resultSet.getString("JMBG");
                            double stanjeRacuna = resultSet.getDouble("StanjeRacuna");
                            String status = resultSet.getString("Status");

                            // Dodajemo red u tabelu sa podacima
                            tableModel.addRow(new Object[]{firstName, lastName, jmbg, stanjeRacuna, status});
                        }
                    }

                    // Zatvaramo resurse
                    resultSet.close();
                    prepareStatement.close();
                    connection.close();
                } catch (Exception ex) {
                	//Ukoliko je doslo do greske ispisujemo u konzolu.
                	System.out.println("Došlo je do greške u SearchUsersManager 'JMBG'");
                    ex.printStackTrace();
                }
            }
        }
        // Ukoliko je korisnik odabrao Stanje Racuna kao kriterij iz drop down-a
        else if ("StanjeRacuna".equals(selectedCriteria)) {

            // Provjeravamo da li su polja za dodatno unosenje kriterijuma prazni.
            if (criteria.isEmpty() || criteriaStanje.isEmpty()) {
                JOptionPane.showMessageDialog(searchUsersFrame, "Oba kriterija su važna za pretragu.");
            } 
            else {
                try {
                	// Konvertujemo korisnicki unos u double tip podatka.
                    double stanjeRacunaOd = Double.parseDouble(criteria);
                    double stanjeRacunaDo = Double.parseDouble(criteriaStanje);
                    
                    // Provjeravamo da li je korisnik unjeo iznos manji od 0 u oba dodatna kriterija.
                    if (stanjeRacunaOd < 0 || stanjeRacunaDo < 0) {
                        JOptionPane.showMessageDialog(searchUsersFrame, "Unesite brojeve veće od 0 za stanje računa.");
                    } 
                    // Proveravamo da li je prvi dodatni kriterij veci od drugog dodatnog kriterijuma sto nije moguce.
                    else if (stanjeRacunaOd > stanjeRacunaDo) {
                        JOptionPane.showMessageDialog(searchUsersFrame, "Prvi kriterij (Od) ne može biti veći od drugog kriterija (Do).");
                    } 
                    //Ako prodje sve provjere prelazimo na glavni dio.
                    else {
                    	// Pokusavamo uspostaviti konekciju sa bazom podataka.
                        connection = DriverManager.getConnection(sqlUrl);
                        
                        // Izvrsavamo SQL upit za dohvatanje korisnika po stanju računa unutar raspona koje je korisnik unjeo
                        String sqlQuery = "SELECT FirstName, LastName, JMBG, StanjeRacuna, Status FROM Users WHERE StanjeRacuna BETWEEN ? AND ?";
                        PreparedStatement prepareStatement = connection.prepareStatement(sqlQuery, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                        prepareStatement.setDouble(1, stanjeRacunaOd);
                        prepareStatement.setDouble(2, stanjeRacunaDo);
                        ResultSet resultSet = prepareStatement.executeQuery();
                        
                        // Kreiramo model za tabelu
                        tableModel = new DefaultTableModel();
                        tblResults.setModel(tableModel);
                        
                        // Dodajemo kolone u model za tabelu
                        tableModel.addColumn("First Name");
                        tableModel.addColumn("Last Name");
                        tableModel.addColumn("JMBG");
                        tableModel.addColumn("Stanje Racuna");
                        tableModel.addColumn("Status");
                        
                        //Ukoliko ne pronadjemo korisnike koji imaju stanje racuna u odredjenom rasponu ispisujemo poruku
                        if(!resultSet.next()) {
                        	JOptionPane.showMessageDialog(searchUsersFrame, "Nisu pronadjeni korisnici koji imaju stanje racuna u unesenom rasponu.");
                        }
                        // Popunjavamo tabelu sa podacima iz baze ukoliko su pronadjeni rezultati
                        while (resultSet.next()) {
                            String firstName = resultSet.getString("FirstName");
                            String lastName = resultSet.getString("LastName");
                            String jmbg = resultSet.getString("JMBG");
                            double racun = resultSet.getDouble("StanjeRacuna");
                            String status = resultSet.getString("Status");
                            
                            // Dodajemo red u tabelu sa podacima
                            tableModel.addRow(new Object[]{firstName, lastName, jmbg, racun, status});
                        }
                        
                        // Zatvaramo resurse
                        resultSet.close();
                        prepareStatement.close();
                        connection.close();
                    }
                } 
                catch (NumberFormatException ex) {
                	//Ukoliko korisnik unese pogresan format ili string u polja ispisuje se poruka.
                    JOptionPane.showMessageDialog(searchUsersFrame, "Za ovaj tip kriterija možete unositi samo brojeve.");
                } 
                catch (Exception ex) {
                	//Ukoliko se pojavi neka druga greska ispisujemo poruku.
                	System.out.println("Greška se dogodila u SearchUsersManager 'Stanje Racuna'");
                    ex.printStackTrace();
                }
            }
        }
        // Ukoliko je korisnik odabrao kriterij First Name iz drop down-a
        else if("FirstName".equals(selectedCriteria)) {
        	// Provjeravamo da li je ostavio prazno polje i ispisujemo poruku
        	if(criteria.isEmpty()) {
        		JOptionPane.showMessageDialog(searchUsersFrame, "Ovo polje je obavezno za pretragu");
        	}
        	//Uostalom idemo na glavni dio.
        	else {
        		try {
        			// Pokušavamo uspostaviti konekciju sa bazom podataka.
                    connection = DriverManager.getConnection(sqlUrl);

                    // Izvršavamo SQL upit za dohvatanje korisnika po Imenu koje je korisnik unjeo u polje.
                    String sqlQuery = "SELECT FirstName, LastName, JMBG, StanjeRacuna, Status FROM Users WHERE FirstName = ?";
                    PreparedStatement prepareStatement = connection.prepareStatement(sqlQuery, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    prepareStatement.setString(1, criteria);
                    ResultSet resultSet = prepareStatement.executeQuery();

                    // Kreiramo model za tabelu
                    tableModel = new DefaultTableModel();
                    tblResults.setModel(tableModel);

                    // Dodajemo kolone u model za tabelu
                    tableModel.addColumn("First Name");
                    tableModel.addColumn("Last Name");
                    tableModel.addColumn("JMBG");
                    tableModel.addColumn("Stanje Racuna");
                    tableModel.addColumn("Status");

                    // Ukoliko ne pronadjemo korisnika sa unesenim imenom ispisujemo poruku.
                    if (!resultSet.next()) {
                        JOptionPane.showMessageDialog(searchUsersFrame, "Korisnik sa tim imenom nije u bazi podataka.");
                    } 
                    else {
                    	// Vracamo se na prvi red rezultata.
                        resultSet.beforeFirst();
                        // Popunjavamo tabelu sa podacima iz baze ukoliko je pronadjen korisnik.
                        while (resultSet.next()) {
                            String firstName = resultSet.getString("FirstName");
                            String lastName = resultSet.getString("LastName");
                            String jmbg = resultSet.getString("JMBG");
                            double stanjeRacuna = resultSet.getDouble("StanjeRacuna");
                            String status = resultSet.getString("Status");

                            // Dodajemo red u tabelu sa podacima
                            tableModel.addRow(new Object[]{firstName, lastName, jmbg, stanjeRacuna, status});
                        }
                    }

                    // Zatvaramo resurse
                    resultSet.close();
                    prepareStatement.close();
                    connection.close();
                } 
        		catch (Exception ex) {
        			// Ukoliko dodje do neocekivane greske ispisati poruku u konzolu
        			System.out.println("Došlo je do greške u SearchUsersManager 'FirstName'");
                    ex.printStackTrace();
                }
            }
        	
        		
        	
        }
        // Ukoliko je korisnik odabrao kriterijum pretrage LastName
        else if("LastName".equals(selectedCriteria)) {
        	// Ukoliko je polje za dodatni kriterij prazno ispisujemo poruku.
        	if(criteria.isEmpty()) {
        		JOptionPane.showMessageDialog(searchUsersFrame, "Ovo polje je obavezno za pretraživanje.");
        	}
        	// Uostalom idemo na glavni dio programa.
        	else {
        		try {
        			// Pokušavamo uspostaviti konekciju sa bazom podataka.
                    connection = DriverManager.getConnection(sqlUrl);

                    // Izvršavamo SQL upit za dohvatanje korisnika po unesenom prezimenu.
                    String sqlQuery = "SELECT FirstName, LastName, JMBG, StanjeRacuna, Status FROM Users WHERE LastName = ?";
                    PreparedStatement prepareStatement = connection.prepareStatement(sqlQuery, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    prepareStatement.setString(1, criteria);
                    ResultSet resultSet = prepareStatement.executeQuery();

                    // Kreiramo model za tabelu
                    tableModel = new DefaultTableModel();
                    tblResults.setModel(tableModel);

                    // Dodajemo kolone u model za tabelu
                    tableModel.addColumn("First Name");
                    tableModel.addColumn("Last Name");
                    tableModel.addColumn("JMBG");
                    tableModel.addColumn("Stanje Racuna");
                    tableModel.addColumn("Status");

                    // Provjeravamo postoji li korisnik s unesenim prezimenom, ako nema rezultata ispisujemo poruku.
                    if (!resultSet.next()) {
                        JOptionPane.showMessageDialog(searchUsersFrame, "Korisnik sa tim prezimenom nije u bazi podataka.");
                    } 
                    else {
                    	// Vracamo se na prvi red rezultata
                        resultSet.beforeFirst();
                        // Popunjavamo tabelu sa podacima iz baze ako smo pronasli korisnika
                        while (resultSet.next()) {
                            String firstName = resultSet.getString("FirstName");
                            String lastName = resultSet.getString("LastName");
                            String jmbg = resultSet.getString("JMBG");
                            double stanjeRacuna = resultSet.getDouble("StanjeRacuna");
                            String status = resultSet.getString("Status");

                            // Dodajemo red u tabelu sa podacima
                            tableModel.addRow(new Object[]{firstName, lastName, jmbg, stanjeRacuna, status});
                        }
                    }

                    // Zatvaramo resurse
                    resultSet.close();
                    prepareStatement.close();
                    connection.close();
                } 
        		// Ukoliko dodje do neocekivane greske ispisujemo poruku u konzolu
        		catch (Exception ex) {
        			System.out.println("Greška se dogodila u SearchUsersManager 'LastName'");
                    ex.printStackTrace();
                }
            
        	}
        }
        // Ukoliko je korisnik odabrao Status kao kriterij
        else if("Status".equals(selectedCriteria)) {
        	// Provjeravamo da li je ostavio prazno polje i ispisujemo poruku
        	if(criteria.isEmpty()) {
        		JOptionPane.showMessageDialog(searchUsersFrame, "Ovo polje je obavezno za unos." );
        	}
        	// Provjeravamo da li je uneseni kriterij jednak (A(a)ktivan ili D(d)eaktiviran) u suprotnom ispisujemo poruku.
        	else if(!criteria.equals("Aktivan") && !criteria.equals("Deaktiviran")
        			&& !criteria.equals("aktivan") && !criteria.equals("deaktiviran")) {
        		JOptionPane.showMessageDialog(searchUsersFrame, "U ovo polje možete upisati samo Aktivan ili Deaktiviran" );
        	}
        	// Ako prodje sve provjere idemo na glavni dio programa.
        	else {
        		try {
        			// Pokušavamo dohvatiti konekciju sa bazom podataka.
                    connection = DriverManager.getConnection(sqlUrl);

                    // Izvršavamo SQL upit za dohvatanje korisnika po unesenom Statusu u polje.
                    String sqlQuery = "SELECT FirstName, LastName, JMBG, StanjeRacuna, Status FROM Users WHERE Status = ?";
                    PreparedStatement prepareStatement = connection.prepareStatement(sqlQuery, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    prepareStatement.setString(1, criteria);
                    ResultSet resultSet = prepareStatement.executeQuery();

                    // Kreiramo model za tabelu
                    tableModel = new DefaultTableModel();
                    tblResults.setModel(tableModel);

                    // Dodajemo kolone u model za tabelu
                    tableModel.addColumn("First Name");
                    tableModel.addColumn("Last Name");
                    tableModel.addColumn("JMBG");
                    tableModel.addColumn("Stanje Racuna");
                    tableModel.addColumn("Status");

                    // Ukoliko ne postoji korisnik sa unesenim Statusom ispisujemo poruku.
                    if (!resultSet.next()) {
                        JOptionPane.showMessageDialog(searchUsersFrame, "U bazi podataka se ne nalazi korisnik sa tim statusom.");
                    } 
                    else {
                    	// Vracamo se na prvi red rezultata.
                        resultSet.beforeFirst();
                        // Popunjavamo tabelu sa podacima iz baze ako je pronadjen korisnik.
                        while (resultSet.next()) {
                            String firstName = resultSet.getString("FirstName");
                            String lastName = resultSet.getString("LastName");
                            String jmbg = resultSet.getString("JMBG");
                            double stanjeRacuna = resultSet.getDouble("StanjeRacuna");
                            String status = resultSet.getString("Status");

                            // Dodajemo red u tabelu sa podacima
                            tableModel.addRow(new Object[]{firstName, lastName, jmbg, stanjeRacuna, status});
                        }
                    }

                    // Zatvaramo resurse
                    resultSet.close();
                    prepareStatement.close();
                    connection.close();
                } 
        		// Ispisujemo poruku u konzolu ukoliko dodje do greske
        		catch (Exception ex) {
        			System.out.println("Greška se dogodila u SearchUsersManager 'Status'");
                    ex.printStackTrace();
                }
        	}
        }
        else {
            /*
             * Ukoliko budemo htjeli implementirati dodatne kriterijume 
             * odavdje cemo nastaviti.
             */
        }
        // Vracamo model za tabelu defaultno.
        return tableModel;
    }
}

