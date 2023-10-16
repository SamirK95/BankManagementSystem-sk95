package bms.ATM;
import java.awt.Button;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * Ova klasa služi za interakciju sa korisnikom pri korištenju bankomata 
 * 
 * Sadrži funkcionalnosti za dugmiće od (0 - 9).
 * 
 * Sadrži funkcionalnosti za brzo biranje (10, 20, 50, 100, 200).
 * 
 * Sadrži funkcionalnost za dugmic Cancel koji brise prethodni unos korisnika.
 */

public class AtmUIButtonManager {
	
	// Deklaracija svih dugmica za brojeve i akcije
	
    private Button btn1;    // Gumb za unos broja 1
    private Button btn2;    // Gumb za unos broja 2
    private Button btn3;    // Gumb za unos broja 3
    private Button btn4;    // Gumb za unos broja 4
    private Button btn5;    // Gumb za unos broja 5
    private Button btn6;    // Gumb za unos broja 6
    private Button btn7;    // Gumb za unos broja 7
    private Button btn8;    // Gumb za unos broja 8
    private Button btn9;    // Gumb za unos broja 9
    private Button btnCancel; // Gumb za brisanje unosa
    private Button btn0;    // Gumb za unos broja 0
    
    
    //Deklaracija dugmica u ovom slucaju za brzo podizanje, polaganje novca.
    
    private Button btn10; //Gumb za unos broja 10
    private Button btn20; //Gumb za unos broja 20
    private Button btn50; //Gumb za unos broja 50
    private Button btn100; //Gumb za unos broja 100
    private Button btn200; //Gumb za unos broja 200
    
	
    private AtmFrame mainFrame; // Referenca na glavni okvir aplikacije
    
    // Konstruktor klase
    public AtmUIButtonManager(AtmFrame mainFrame) {
    	
    	this.mainFrame = mainFrame;
    	
    	// Inicijalizacija svih dugmica bankomata i dodavanje akcijskih slušatelja
    	btn0 = new Button("0");
        btn1 = new Button("1");
        btn2 = new Button("2");
        btn3 = new Button("3");
        btn4 = new Button("4");
        btn5 = new Button("5");
        btn6 = new Button("6");
        btn7 = new Button("7");
        btn8 = new Button("8");
        btn9 = new Button("9");
        btnCancel = new Button("Cancel");

        
        btn10 = new Button("10");
        btn20 = new Button("20");
        btn50 = new Button("50");
        btn100 = new Button("100");
        btn200 = new Button("200");

        addActionListeners();
    }
    
    
    // Metoda za dodavanje akcijskih slušatelja svim dugmicima
    private void addActionListeners() {
    	
    	// Dodavanje akcijskog slušatelja za dugmic 1 koji dodaje broj 1 u unos
    	btn1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String currentText = mainFrame.userInput.getText();
				mainFrame.userInput.setText(currentText + "1");
				
				//Prilikom unosa pin-a kada korisnik pritisne tipku da se prikaze 1 na vec postojeci unos
				String currentTextPin = new String(mainFrame.pinField.getPassword());
				mainFrame.pinField.setText(currentTextPin + "1");
			}
		});
    	
    	
    	// Dodavanje akcijskog slušatelja za dugmic 2 koji dodaje broj 2 u unos
    	btn2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String currentText = mainFrame.userInput.getText();
				mainFrame.userInput.setText(currentText + "2");
				
				//Prilikom unosa pin-a kada korisnik pritisne tipku da se prikaze 2 na vec postojeci unos
				String currentTextPin = new String(mainFrame.pinField.getPassword());
				mainFrame.pinField.setText(currentTextPin + "2");
			}
		});
    	
    	// Dodavanje akcijskog slušatelja za dugmic 3 koji dodaje broj 3 u unos
    	btn3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String currentText = mainFrame.userInput.getText();
				mainFrame.userInput.setText(currentText + "3");
				
				//Prilikom unosa pin-a kada korisnik pritisne tipku da se prikaze 3 na vec postojeci unos
				String currentTextPin = new String(mainFrame.pinField.getPassword());
				mainFrame.pinField.setText(currentTextPin + "3");
			}
		});
    	
    	// Dodavanje akcijskog slušatelja za dugmic 4 koji dodaje broj 4 u unos
    	btn4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String currentText = mainFrame.userInput.getText();
				mainFrame.userInput.setText(currentText + "4");
				
				//Prilikom unosa pin-a kada korisnik pritisne tipku da se prikaze 4 na vec postojeci unos
				String currentTextPin = new String(mainFrame.pinField.getPassword());
				mainFrame.pinField.setText(currentTextPin + "4");
			}
		});
    	
    	// Dodavanje akcijskog slušatelja za dugmic 5 koji dodaje broj 5 u unos
    	btn5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String currentText = mainFrame.userInput.getText();
				mainFrame.userInput.setText(currentText + "5");
				
				//Prilikom unosa pin-a kada korisnik pritisne tipku da se prikaze 5 na vec postojeci unos
				String currentTextPin = new String(mainFrame.pinField.getPassword());
				mainFrame.pinField.setText(currentTextPin + "5");
			}
		});
    	
    	// Dodavanje akcijskog slušatelja za dugmic 6 koji dodaje broj 6 u unos
    	btn6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String currentText = mainFrame.userInput.getText();
				mainFrame.userInput.setText(currentText + "6");
				
				//Prilikom unosa pin-a kada korisnik pritisne tipku da se prikaze 6 na vec postojeci unos
				String currentTextPin = new String(mainFrame.pinField.getPassword());
				mainFrame.pinField.setText(currentTextPin + "6");
			}
		});
    	
    	// Dodavanje akcijskog slušatelja za dugmic 7 koji dodaje broj 7 u unos
    	btn7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String currentText = mainFrame.userInput.getText();
				mainFrame.userInput.setText(currentText + "7");
				
				//Prilikom unosa pin-a kada korisnik pritisne tipku da se prikaze 7 na vec postojeci unos
				String currentTextPin = new String(mainFrame.pinField.getPassword());
				mainFrame.pinField.setText(currentTextPin + "7");
			}
		});
    	
    	// Dodavanje akcijskog slušatelja za dugmic 8 koji dodaje broj 8 u unos
    	btn8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String currentText = mainFrame.userInput.getText();
				mainFrame.userInput.setText(currentText + "8");
				
				//Prilikom unosa pin-a kada korisnik pritisne tipku da se prikaze 8 na vec postojeci unos
				String currentTextPin = new String(mainFrame.pinField.getPassword());
				mainFrame.pinField.setText(currentTextPin + "8");
			}
		});
    	
    	// Dodavanje akcijskog slušatelja za dugmic 9 koji dodaje broj 9 u unos
    	btn9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String currentText = mainFrame.userInput.getText();
				mainFrame.userInput.setText(currentText + "9");
				
				//Prilikom unosa pin-a kada korisnik pritisne tipku da se prikaze 9 na vec postojeci unos
				String currentTextPin = new String(mainFrame.pinField.getPassword());
				mainFrame.pinField.setText(currentTextPin + "9");
			}
		});
    	
    	// Dodavanje akcijskog slušatelja za dugmic 0 koji dodaje broj 0 u unos
    	btn0.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String currentText = mainFrame.userInput.getText();
				mainFrame.userInput.setText(currentText + "0");
				
				//Prilikom unosa pin-a kada korisnik pritisne tipku da se prikaze 0 na vec postojeci unos
				String currentTextPin = new String(mainFrame.pinField.getPassword());
				mainFrame.pinField.setText(currentTextPin + "0");
			}
		});
    	
    	// Dodavanje akcijskog slušatelja za dugmic Cancel koji brise prethodni unos korisnika.
    	// Postavljanje boje dugmica na crveno
    	btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainFrame.userInput.setText("");
				
				//Brisanje prethodnog unosa prilikom unosa PIN-a
				mainFrame.pinField.setText("");
			}
		});
    	btnCancel.setBackground(new Color(255, 0, 0));
    	
    	
    	
    	// Dodavanje akcijskog slušatelja za brzi dugmic 10 koji dodaje broj 10 u unos.
    	// Zatim postavljamo poziciju i velicinu dugmica nakon akcijskog slušatelja.
    	btn10.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String currentText = mainFrame.userInput.getText();
				mainFrame.userInput.setText(currentText + "10"); 
			}
		});
		btn10.setBounds(69, 47, 85, 45);
		
		
		
		// Dodavanje akcijskog slušatelja za brzi dugmic 20 koji dodaje broj 20 u unos.
    	// Zatim postavljamo poziciju i velicinu dugmica nakon akcijskog slušatelja.
		btn20.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String currentText = mainFrame.userInput.getText();
				mainFrame.userInput.setText(currentText + "20"); 
			}
		});
		btn20.setBounds(69, 117, 85, 45);
		
		
		
		// Dodavanje akcijskog slušatelja za brzi dugmic 50 koji dodaje broj 50 u unos.
    	// Zatim postavljamo poziciju i velicinu dugmica nakon akcijskog slušatelja.
		btn50.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String currentText = mainFrame.userInput.getText();
				mainFrame.userInput.setText(currentText + "50"); 
			}
		});
		btn50.setBounds(69, 185, 85, 45);
    	
		
		
		// Dodavanje akcijskog slušatelja za brzi dugmic 100 koji dodaje broj 100 u unos.
    	// Zatim postavljamo poziciju i velicinu dugmica nakon akcijskog slušatelja.
		btn100.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String currentText = mainFrame.userInput.getText();
				mainFrame.userInput.setText(currentText + "100"); 
			}
		});
		btn100.setBounds(69, 247, 85, 45);
    	
    	
    	
		// Dodavanje akcijskog slušatelja za brzi dugmic 200 koji dodaje broj 200 u unos.
    	// Zatim postavljamo poziciju i velicinu dugmica nakon akcijskog slušatelja.
    	btn200.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String currentText = mainFrame.userInput.getText();
				mainFrame.userInput.setText(currentText + "200"); 
			}
		});
    	btn200.setBounds(589, 47, 85, 45);
    }
    
    
    
    /**
     * Getter metode za dugmice od 0 do 9 i Cancel 
     * 
     * Kako bi se omogućio pristup iz drugih dijelova aplikacije
     */
    
    //vraća btn1
    public Button getButton1() {
    	
    	return btn1;
    }
    
    //vraća btn2
    public Button getButton2() {
    	
    	return btn2;
    }
    
    //vraća btn3
    public Button getButton3() {
    	
    	return btn3;
    }
    
    //vraća btn4
	public Button getButton4() {
	    	
	    return btn4;
	}
	
	//vraća btn5
	public Button getButton5() {
		
		return btn5;
	}
	
	//vraća btn6
	public Button getButton6() {
		
		return btn6;
	}
	
	//vraća btn7
	public Button getButton7() {
    	
    	return btn7;
    }
	
	//vraća btn8
	public Button getButton8() {
		
		return btn8;
	}
	
	//vraća btn9
	public Button getButton9() {
		
		return btn9;
	}
	
	//vraća btn0
	public Button getButton0() {
    	
    	return btn0;
    }
	
	//vraća btnCancel
	public Button getButtonCancel() {
		
		return btnCancel;
	}
	
	
	
	/**
     * Getter metode za dugmice za brzo podizanje ili polaganje novca i slicno
     * 
     * Dugmici (10, 20, 50, 100, 200)
     * 
     * Kako bi se omogućio pristup iz drugih dijelova aplikacije
     */
	
	
	//vraća btn10
	public Button getButton10() {
		
		return btn10;
	}
	
	
	//vraća btn20
	public Button getButton20() {
		
		return btn20;
	}
	
	
	//vraća btn50
	public Button getButton50() {
		
		return btn50;
	}
	
	
	//vraća btn100
	public Button getButton100() {
		
		return btn100;
	}
		
	
	//vraća btn200
	public Button getButton200() {
		
		return btn200;
	}
	
	
    
}
