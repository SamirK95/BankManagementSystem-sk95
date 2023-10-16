package bms.transactionReports;

import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


/*
 * Ova klasa predstavlja korisnički interfejs za pretragu i prikaz transakcija. 
 * Omogućava korisnicima da unesu kriterijume pretrage i prikažu rezultate u tabeli. 
 * Također ima opciju izvoza rezultata u PDF fajl.
 */


public class SearchTransactionsFrame extends JFrame {

	private static final long serialVersionUID = 1L;

    private JTable resultTable;
    private DefaultTableModel tableModel;
    private SearchTransactionsManager searchTransactionsManager;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SearchTransactionsFrame frame = new SearchTransactionsFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
     * Inicijalizuje tabelu za prikaz rezultata pretrage i postavlja korisnički interfejs.
     */
	private void initializeResultTable() {
		tableModel = new DefaultTableModel(new String[]{"Ime", "Prezime", "JMBG", "Tip transakcije", "Iznos", "Datum transakcije "}, 0);
	    getContentPane().setLayout(null);
	    resultTable = new JTable(tableModel);

	    
	    resultTable.setEnabled(false);

	    
	    JScrollPane scrollPane = new JScrollPane(resultTable);
	    scrollPane.setBounds(0, 0, 0, 0);
	    getContentPane().add(scrollPane);
	}
	

	/**
     * Konstruktor klase `SearchTransactionsFrame`.
     */
	public SearchTransactionsFrame() {
		
		setTitle("Search Transactions");

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(618, 399);

        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 606, 84);
        panel.setLayout(new GridLayout(4, 8));
        
        Properties properties = new Properties();
        
        // Inicijalizacija modela za odabir datuma
        UtilDateModel fromDateModel = new UtilDateModel();
        UtilDateModel toDateModel = new UtilDateModel();

        // Kreiranje panela za odabir datuma
        JDatePanelImpl fromDatePanel = new JDatePanelImpl(fromDateModel, properties);
        JDatePanelImpl toDatePanel = new JDatePanelImpl(toDateModel, properties);
        
        // Kreiranje komponenti za unos i prikaz datuma
        JLabel lblFromDate = new JLabel("Od datuma:");
        JLabel fromDateDisplayLabel = new JLabel();
        JDatePickerImpl fromDateDatePicker = new JDatePickerImpl(fromDatePanel, null);
        
        
        JLabel lblToDate = new JLabel("Do datuma:");
        JLabel toDateDisplaylabel = new JLabel();
        JDatePickerImpl toDateDatePicker = new JDatePickerImpl(toDatePanel, null);
        
        // Kreiranje komponenata za odabir vrste transakcije
        JLabel lblDisplayTransaction = new JLabel ("");
        JLabel lblTransaction = new JLabel("Vrsta transakcije:");
        String[] transactionOptions = {"Deposit", "Withdraw", "All"};
        JComboBox<String> transactionTypeComboBox = new JComboBox<>(transactionOptions);
        
        // Kreiranje komponenata za unos i prikaz JMBG-a
        JLabel lblDisplayJMBG = new JLabel ("");
        JLabel lblJMBG = new JLabel("JMBG korisnika:");
        JTextField jmbgTypeField = new JTextField ();
        
        // Postavljanje početne selekcije u ComboBoxu
        transactionTypeComboBox.setSelectedItem("All");
        
        // Inicijalizacija tabele za prikaz rezultata
        initializeResultTable();
        
        searchTransactionsManager = new SearchTransactionsManager(tableModel, fromDateDatePicker, toDateDatePicker, transactionTypeComboBox, jmbgTypeField);
        

        // Dodavanje događaja za praćenje promjena datuma
        fromDateModel.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("value".equals(evt.getPropertyName())) {
                    Object newDate = evt.getNewValue();
                    if (newDate != null) {
                    	SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                        fromDateDisplayLabel.setText(dateFormat.format((Date) newDate));
                    } else {
                        fromDateDisplayLabel.setText("");
                    }
                }
            }
        });

        toDateModel.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("value".equals(evt.getPropertyName())) {
                    Object newDate = evt.getNewValue();
                    if (newDate != null) {
                    	SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                    	toDateDisplaylabel.setText(dateFormat.format((Date) newDate));
                    } else {
                        toDateDisplaylabel.setText("");
                    }
                }
            }
        });


        // Dodavanje akcije na dugme za pretragu.
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		
        		//Pozivamo metodu searchTransactions iz klase 'SearchTransactionsManager' u kojoj se nalazi funkcionalnost za dugmic.
        		searchTransactionsManager.searchTransactions();
        		
        	}
        });
        
        
        searchButton.setBounds(145, 104, 118, 21);
        getContentPane().add(searchButton);
        
        // Dodavanje akcije na dugme za izvoz u PDF.
        JButton printButton = new JButton("Export to PDF");
        printButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	//Pozivamo metodu exportToPDF iz klase 'SearchTransactionsManager' u kojoj se nalazi funkcionalnost za dugmic.
            	searchTransactionsManager.exportToPDF();

            }
        });
        printButton.setBounds(316, 104, 118, 21);
        getContentPane().add(printButton);
        

        // Dodavanje komponenata na korisnički interfejs
        panel.add(lblFromDate);
        panel.add(fromDateDisplayLabel);
        panel.add(fromDateDatePicker);
        
        panel.add(lblToDate);
        panel.add(toDateDisplaylabel);
        panel.add(toDateDatePicker);
        
        panel.add(lblTransaction);
        panel.add(lblDisplayTransaction);
        panel.add(transactionTypeComboBox);
        
        panel.add(lblJMBG);
        panel.add(lblDisplayJMBG);
        panel.add(jmbgTypeField);
        
        getContentPane().add(panel);
        
        // Dodavanje tabele za prikaz rezultata
        JScrollPane scrollPane = new JScrollPane(resultTable);
        scrollPane.setBounds(0, 135, 606, 228);
        getContentPane().add(scrollPane);
        

        setVisible(true);
	}
}


