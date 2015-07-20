package de.ees.group1.cs.gui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

public class ActiveOrderPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel statusLbl;
	private JTextField idTxt;
	private JTextField currentStepTxt;
	private JTextField nextStepTxt;

	/**
	 * Create the panel.
	 */
	public ActiveOrderPanel() {
		setMinimumSize(new Dimension(300, -1));
		setBorder(new TitledBorder(null, "aktueller Auftrag", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		setLayout(new MigLayout("", "[][][][grow]", "[][][]"));
		
		statusLbl = new JLabel();
		statusLbl.setText("Wartet");
		statusLbl.setPreferredSize(new Dimension(80, -1));
		statusLbl.setHorizontalAlignment(SwingConstants.CENTER);
		statusLbl.setBackground(Color.YELLOW);
		statusLbl.setOpaque(true);
		statusLbl.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		
		add(statusLbl, "span 1 3,grow");
		
		JLabel idLbl = new JLabel("ID:");
		add(idLbl, "cell 1 0");
		
		idTxt = new JTextField("-", 3);
		idTxt.setHorizontalAlignment(SwingConstants.CENTER);
		idTxt.setEditable(false);
		add(idTxt);
		
		JLabel currentStepLbl = new JLabel("aktueller Schritt:");
		add(currentStepLbl, "cell 1 1,span 2 1");
		
		currentStepTxt = new JTextField("-");
		currentStepTxt.setEditable(false);
		add(currentStepTxt, "cell 3 1, grow");
		
		JLabel nextStepLbl = new JLabel("nächster Schritt:");
		add(nextStepLbl, "cell 1 2, span 2 1");
		
		nextStepTxt = new JTextField("-");
		nextStepTxt.setEditable(false);
		add(nextStepTxt, "cell 3 2, grow");
		
//		JButton cancelBtn = new JButton("Abbrechen");
//		cancelBtn.addActionListener(cancelListener);
//		cancelBtn.setEnabled(false);
//		add(cancelBtn, "cell 3 0, right");
	}
	
	public void setOrderStatus() {
		
	}

}
