package de.ees.group1.cs.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class BTConnectDialog extends JDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	
	private List<JTextField> tfList;
	private String[] defMAC = {"00","16","53","05","65","fd"};
	
	public boolean isCancled = true;
	public String macAddress;
	
	KeyListener hexByte = new KeyAdapter() {
		@Override
		public void keyTyped(KeyEvent e) {
			if(!e.isActionKey()) {
				char c = e.getKeyChar();
				if(Character.digit(c, 16) == -1) {
					e.consume();
				}
				
				JTextField source = ((JTextField)e.getSource());
				int targetLength = source.getText().length();
				if(targetLength > 1) {
					e.consume();
				}
			}
		}
	};
	
	Border badValueBorder = BorderFactory.createLineBorder(Color.RED, 3);
	Border goodValueBorder = BorderFactory.createEmptyBorder(3, 3, 3, 3);

	/**
	 * Create the dialog.
	 */
	public BTConnectDialog(JFrame owner) {
		super(owner);
		
		tfList = new LinkedList<JTextField>();
		
		setModal(true);
		setTitle("MAC Addresse des NXT eingeben...");
		setBounds(100, 100, 300, 100);
		setLocationRelativeTo(owner);
		setResizable(false);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.X_AXIS));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		{
			contentPanel.add(new JLabel("MAC:"));
			contentPanel.add(Box.createHorizontalStrut(5));
			for(int i = 0; i < 6; i++) {
				JTextField tf = new JTextField(2);
				tf.setBorder(goodValueBorder);
				tf.setMaximumSize(tf.getPreferredSize());
				tf.addKeyListener(hexByte);
				tf.setText(defMAC[i]);
				contentPanel.add(tf);
				tfList.add(tf);
				if(i < 5)
					contentPanel.add(new JLabel(":"));
			}
		}
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				okButton.addActionListener(this);
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				cancelButton.addActionListener(this);
				buttonPane.add(cancelButton);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand() == "OK") {
			byte[] macBytes = new byte[6];
			boolean valid = true;
			for(int i = 0; i < 6; i++) {
				try {
					macBytes[i] = (byte)Integer.parseInt(tfList.get(i).getText(), 16);
					tfList.get(i).setBorder(goodValueBorder);
				} catch(Exception ex) {
					tfList.get(i).setBorder(badValueBorder);
					valid = false;
				}
			}
			if(valid) {
				macAddress = "";
				for(int i = 0; i < macBytes.length; i++) {
					macAddress += String.format("%02X", macBytes[i]);
					if(i < macBytes.length - 1) {
						macAddress += ":";
					}
				}
				isCancled = false;
				dispose();
			}
		} else {
			dispose();
		}
	}

}
