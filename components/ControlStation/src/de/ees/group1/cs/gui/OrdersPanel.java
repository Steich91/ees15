package de.ees.group1.cs.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

public class OrdersPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JPanel orderListPanel;
	
	/**
	 * Create the panel.
	 */
	public OrdersPanel(ActionListener addOrderListener) {
		setLayout(new BorderLayout());
		setBorder(new TitledBorder(null, "anstehende Auftr\u00E4ge", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		JPanel addOrderPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		addOrderPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK));
		
		JButton addOrderBtn = new JButton(new ImageIcon(getClass().getResource("/toolbarButtonGraphics/general/Add16.gif")));
		addOrderBtn.addActionListener(addOrderListener);
		addOrderPanel.add(addOrderBtn);
		
		add(addOrderPanel, BorderLayout.SOUTH);
		
		orderListPanel = new JPanel();
		orderListPanel.setLayout(new MigLayout("wrap 1", "grow", ""));
		
		JScrollPane scrollPane = new JScrollPane(orderListPanel);
		scrollPane.setMinimumSize(new Dimension(300,-1));
		scrollPane.setBorder(new EmptyBorder(0,0,0,0));
		add(scrollPane, BorderLayout.CENTER);
	}
	
	public void addOrderPanel(ListedOrderPanel p) {
		orderListPanel.add(p, "grow");
		orderListPanel.revalidate();
		orderListPanel.repaint();
	}
	
	public List<ListedOrderPanel> getOrderPanels() {
		List<ListedOrderPanel> list = new LinkedList<ListedOrderPanel>();
		for(Component c : orderListPanel.getComponents()) {
			if(c instanceof ListedOrderPanel) {
				list.add((ListedOrderPanel)c);
			}
		}
		return list;
	}
	
	public void removeOrderPanel(ListedOrderPanel p) {
		orderListPanel.remove(p);
		orderListPanel.revalidate();
		orderListPanel.repaint();
	}
}
