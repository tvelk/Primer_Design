package my.primers;

import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import java.awt.Color;
import java.awt.SystemColor;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.UIManager.*;
import java.awt.Font;

public class PrimerDesign {

	private JFrame frame;
	private static ArrayList<String> aPrimers = new ArrayList<String>();
	private static ArrayList<String> bPrimers = new ArrayList<String>();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
		    // If Nimbus is not available, you can set the GUI to another look and feel.
		}
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PrimerDesign window = new PrimerDesign();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public PrimerDesign() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblEnterGenomicSequence = new JLabel("Enter genomic sequence below");
		lblEnterGenomicSequence.setBounds(20, 11, 153, 14);
		frame.getContentPane().add(lblEnterGenomicSequence);
		
		JButton btnForwardPrimer = new JButton("Forward Primer");
		btnForwardPrimer.setBounds(20, 91, 125, 23);
		frame.getContentPane().add(btnForwardPrimer);
		
		JButton btnReversePrimer = new JButton("Reverse Primer");
		btnReversePrimer.setBounds(151, 91, 125, 23);
		frame.getContentPane().add(btnReversePrimer);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(20, 36, 391, 44);
		frame.getContentPane().add(scrollPane);
		
		final JTextArea textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		
		JButton btnClear = new JButton("Clear");
		
		btnClear.setBounds(286, 91, 125, 23);
		frame.getContentPane().add(btnClear);
		
		JLabel lblIdealPrimers = new JLabel("Ideal Primers");
		lblIdealPrimers.setBounds(20, 125, 104, 14);
		frame.getContentPane().add(lblIdealPrimers);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(20, 150, 192, 101);
		frame.getContentPane().add(scrollPane_1);
		
		final JTextArea textArea_1 = new JTextArea();
		textArea_1.setFont(new Font("Monospaced", Font.PLAIN, 13));
		scrollPane_1.setViewportView(textArea_1);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(222, 151, 192, 100);
		frame.getContentPane().add(scrollPane_2);
		
		final JTextArea textArea_2 = new JTextArea();
		scrollPane_2.setViewportView(textArea_2);
		
		JLabel lblSecondaryPrimers = new JLabel("Secondary Primers");
		lblSecondaryPrimers.setBounds(222, 125, 118, 14);
		frame.getContentPane().add(lblSecondaryPrimers);
		
		//Making a forward primer
		btnForwardPrimer.addActionListener(new ActionListener() {
			
			//Forward primer button pressed
			public void actionPerformed(ActionEvent arg0) {
				
				PrimerDesign.makePrimer(true, textArea.getText());
				//ideal primers
				for(int i = 0; i < aPrimers.size(); i++){
					textArea_1.setText(textArea_1.getText() + aPrimers.get(i) + "\n");
				}
				//non ideal primers
				for(int j = 0; j < bPrimers.size(); j++){
					textArea_2.setText(textArea_2.getText() + bPrimers.get(j) + "\n");
				}
				
			}

		});
		
		//Making a reverse primer
		btnReversePrimer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				PrimerDesign.makePrimer(false, textArea.getText());
				//ideal primers
				for(int i = 0; i < aPrimers.size(); i++){
					textArea_1.setText(textArea_1.getText() + aPrimers.get(i) + "\n");
				}
				//non ideal primers
				for(int j = 0; j < bPrimers.size(); j++){
					textArea_2.setText(textArea_2.getText() + bPrimers.get(j) + "\n");
				}
			}
		});
		
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				textArea.setText("");
				textArea_1.setText("");
				textArea_2.setText("");
			}
		});
	}

	protected static void makePrimer(boolean forward, String genome) {
		
		aPrimers.clear();
		bPrimers.clear();
		
		int fivePrimeEnd = 0;
		String potPrimer = "";
		
		char dNTP = 'a';
		int atCount = 0;
		boolean tooManygc = false;
		int primerLength = 20;
		
		//For reverse primers
		if(!forward){
			genome = new StringBuilder(genome).reverse().toString();
		}
		
		while(fivePrimeEnd < genome.length() - primerLength){
				
			//Current 20 dNTP segment being tested
			potPrimer = genome.substring(fivePrimeEnd, fivePrimeEnd + primerLength);
				
			dNTP = potPrimer.charAt(0);
			if(dNTP == 'a' || dNTP == 't' || dNTP == 'A' || dNTP == 'T'){
				//Get AT:GC ratio and check for multiple gc's in a row (40:60)
				int gcsInRow = 0;
				for(int i = 0; i < primerLength; i++){
					if(potPrimer.charAt(i) == 'a' || potPrimer.charAt(i) == 't'
					|| potPrimer.charAt(i) == 'A' || potPrimer.charAt(i) == 'T'){
						atCount++;
						gcsInRow = 0;
					}
					else{
						gcsInRow++;
						
					}
					if(gcsInRow > 3){
						tooManygc = true;
					}
				}
					
				if(atCount == 8){
					if(potPrimer.charAt(18) == 'g' || potPrimer.charAt(18) == 'c' 
							|| potPrimer.charAt(18) == 'G' || potPrimer.charAt(18) == 'C'){
						if(potPrimer.charAt(19) == 'g' || potPrimer.charAt(19) == 'c' 
							|| potPrimer.charAt(19) == 'G' || potPrimer.charAt(19) == 'C'){
							//Ideal primers
							if(!tooManygc){
								//if making a reverse primer it must be flipped back
								if(!forward){
									potPrimer = new StringBuilder(potPrimer).reverse().toString();
								}
								aPrimers.add(potPrimer);
							}
							//Primer had more than 3 gc's in a row
							else{
								//if making a reverse primer it must be flipped back
								if(!forward){
									potPrimer = new StringBuilder(potPrimer).reverse().toString();
								}
								bPrimers.add(potPrimer);
							}
						}
					}
				}
			}
			atCount = 0;
			fivePrimeEnd++;
			tooManygc = false;
		}
	}
}