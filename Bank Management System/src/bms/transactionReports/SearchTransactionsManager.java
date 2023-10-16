package bms.transactionReports;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import org.jdatepicker.impl.JDatePickerImpl;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import bms.dbLoaderPackage.DbLoader;

/**
 * Manager za pretragu transakcija i izvoz rezultata u PDF formatu.
 */

public class SearchTransactionsManager {

	// Model tabele za prikaz rezultata pretrage
	private DefaultTableModel tableModel;
	
    private JTable resultTable;
    
    // kreiramo instancu klase da bi ucitali url baze podataka
 	static DbLoader dbLoader = new DbLoader();
 	// Pozivamo metod za dobijanje url-a baze podataka i spremamo u string
 	static String sqlUrl = dbLoader.getDatabaseUrl();
    
    // Datum od i do za pretragu
    private Date fromDate;
    private Date toDate;
    
    // Referenca na okvir na kojem se prikazuju rezultati
    private SearchTransactionsFrame searchTransactionsFrame;
    
    // Komponente za unos kriterijuma pretrage
    private JComboBox<String> transactionTypeComboBox;
    private JTextField jmbgTypeField;
    private JDatePickerImpl toDateDatePicker;
    private JDatePickerImpl fromDateDatePicker;
    
    /**
     * Konstruktor za SearchTransactionsManager.
     *
     * @param tableModel             Model tabele za prikaz rezultata pretrage.
     * @param fromDateDatePicker     DatePicker komponenta za izbor datuma "od".
     * @param toDateDatePicker       DatePicker komponenta za izbor datuma "do".
     * @param transactionTypeComboBox JComboBox za izbor tipa transakcije.
     * @param jmbgTypeField          TextField za unos JMBG-a korisnika.
     */
    
    
    public SearchTransactionsManager(DefaultTableModel tableModel, JDatePickerImpl fromDateDatePicker, JDatePickerImpl toDateDatePicker,
    		JComboBox<String> transactionTypeComboBox, JTextField jmbgTypeField) {
        this.tableModel = tableModel;
        this.transactionTypeComboBox = transactionTypeComboBox;
        this.jmbgTypeField = jmbgTypeField;
        this.toDateDatePicker = toDateDatePicker;
        this.fromDateDatePicker = fromDateDatePicker;
    }

    /**
     * Inicijalizujemo tabelu za prikaz rezultata pretrage.
     */
    public void initializeResultTable() {
        // Inicijalizujemo tabelu ovdje
    	tableModel = new DefaultTableModel(new String[]{"Ime", "Prezime", "JMBG", "Tip transakcije", "Iznos", "Datum transakcije "}, 0);
	    searchTransactionsFrame.getContentPane().setLayout(null);
	    resultTable = new JTable(tableModel);

	    // Postavljamo tabelu da se ne može uređivati ručno
	    resultTable.setEnabled(false);

	    // Dodajemo JTable u JScrollPane kako bismo omogućili pomicanje ako tabela postane velika
	    JScrollPane scrollPane = new JScrollPane(resultTable);
	    scrollPane.setBounds(0, 0, 0, 0);
	    searchTransactionsFrame.getContentPane().add(scrollPane);
    }
    
    
    /**
     * Pretražujemo transakcije na osnovu unijetih kriterijuma i prikazujemo rezultate u tabeli.
     */
    public void searchTransactions() {

                // Implementiramo funkcionalnost pretrage ovdje
            	tableModel.setRowCount(0);
        		
        		fromDate = (Date) fromDateDatePicker.getModel().getValue();
            	
            	toDate = (Date) toDateDatePicker.getModel().getValue();
            	
            	String selectedTransactionType = (String) transactionTypeComboBox.getSelectedItem();
            	
            	String inputJMBG = jmbgTypeField.getText();
            	
            	Connection connection = null;
        	    PreparedStatement preparedStatement = null;
        	    ResultSet resultSet = null;

        	    try {
        	        // Uspostavljamo konekciju sa bazom podataka
        	        connection = DriverManager.getConnection(sqlUrl);

        	        // Kreiramo SQL upit na osnovu unijetih kriterijuma
        	        String query = "SELECT * FROM Transactions WHERE 1=1";

        	        if (fromDate != null) {
        	            query += " AND TransactionsDateTime >= ?";
        	        }

        	        if (toDate != null) {
        	            query += " AND TransactionsDateTime <= ?";
        	        }

        	        if (!"All".equals(selectedTransactionType)) {
        	            query += " AND TransactionType = ?";
        	        }

        	        if (!inputJMBG.isEmpty()) {
        	            query += " AND JMBGClient = ?";
        	        }

        	        // Pripremamo SQL upit
        	        preparedStatement = connection.prepareStatement(query);

        	        int parameterIndex = 1;

        	        if (fromDate != null) {
        	            preparedStatement.setDate(parameterIndex++, new java.sql.Date(fromDate.getTime()));
        	        }

        	        if (toDate != null) {
        	            preparedStatement.setDate(parameterIndex++, new java.sql.Date(toDate.getTime()));
        	        }

        	        if (!"All".equals(selectedTransactionType)) {
        	            preparedStatement.setString(parameterIndex++, selectedTransactionType);
        	        }

        	        if (!inputJMBG.isEmpty()) {
        	            preparedStatement.setString(parameterIndex++, inputJMBG);
        	        }

        	        // Izvršavamo SQL upit
        	        resultSet = preparedStatement.executeQuery();
        	        
        	        boolean foundResults = false;
        	        while (resultSet.next()) {
        	        	foundResults = true;
        	            String firstName = resultSet.getString("FirstNameClient");
        	            String lastName = resultSet.getString("LastNameClient");
        	            String jmbgClient = resultSet.getString("JMBGClient");
        	            String transactionType = resultSet.getString("TransactionType");
        	            BigDecimal amount = BigDecimal.valueOf(resultSet.getDouble("Amount"));
        	            //Date transactionsDateTime = resultSet.getDate("TransactionsDateTime");
        	            Timestamp timestamp = resultSet.getTimestamp("TransactionsDateTime");
        	            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        	            String formattedDateTime = sdf.format(timestamp);

        	         // Dodajemo red u tabelu
                        tableModel.addRow(new Object[]{firstName, lastName, jmbgClient, transactionType, amount.toString(), formattedDateTime});
        	        }
        	        
        	     // Provjeravamo da li su pronađeni rezultati
                    if (!foundResults) {
                        JOptionPane.showMessageDialog(searchTransactionsFrame, "Za ovaj kriterij nije pronađena ni jedna transakcija.", "Nema rezultata", JOptionPane.INFORMATION_MESSAGE);
                    }

        	    } catch (SQLException eex) {
        	        eex.printStackTrace();
        	    } finally {
        	        // Zatvoramo sve resurse.
        	        if (resultSet != null) {
        	            try {
        	                resultSet.close();
        	            } catch (SQLException ex) {
        	                ex.printStackTrace();
        	            }
        	        }
        	        if (preparedStatement != null) {
        	            try {
        	                preparedStatement.close();
        	            } catch (SQLException exx) {
        	                exx.printStackTrace();
        	            }
        	        }
        	        if (connection != null) {
        	            try {
        	                connection.close();
        	            } catch (SQLException exxx) {
        	                exxx.printStackTrace();
        	            }
        	        }
        	    }
            	
        	 // Obavještavamo tabelu da je model promijenjen kako bi se ažurirao prikaz
                tableModel.fireTableDataChanged();
    }
    
    /**
     * Izvozimo rezultate pretrage u PDF formatu.
     */
    public void exportToPDF() {

            	if (tableModel.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(null, "Tabela je prazna, nema podataka za exportovanje.", "Upozorenje", JOptionPane.WARNING_MESSAGE);
                    return; 
                }

                Document document = new Document();
                try {
                    
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy__HH-mm-ss");
                    String fileName = "F:\\katan\\JavaPdf(Transakcije)\\transaction_report_" + sdf.format(new Date()) + ".pdf";
        
                    PdfWriter.getInstance(document, new FileOutputStream(fileName));

                    document.open();

                    PdfPTable table = new PdfPTable(6);

                    table.addCell("Ime");
                    table.addCell("Prezime");
                    table.addCell("JMBG");
                    table.addCell("Tip transakcije");
                    table.addCell("Iznos");
                    table.addCell("Datum transakcije");

                    for (int row = 0; row < tableModel.getRowCount(); row++) {
                        for (int column = 0; column < tableModel.getColumnCount(); column++) {
                            table.addCell(tableModel.getValueAt(row, column).toString());
                        }
                    }

                    document.add(table);
                    document.close();

                    JOptionPane.showMessageDialog(null, "PDF fajl je uspješno sačuvan", "Čuvanje PDF-a završeno", JOptionPane.INFORMATION_MESSAGE);
                } catch (DocumentException | IOException ex) {
                    JOptionPane.showMessageDialog(null, "Greška pri generisanju i čuvanju PDF fajla: " + ex.getMessage(), "Greška", JOptionPane.ERROR_MESSAGE);
                }
            }

}
