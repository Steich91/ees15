package de.ees.group1.cs.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import de.ees.group1.model.ProductionOrder;
import de.ees.group1.model.ProductionStep;
import de.ees.group1.model.WorkstationType;
import net.miginfocom.swing.MigLayout;

public class MainWindow {

	private JFrame frmControlstation;
	private OrdersPanel ordersPanel;
	private IOrderController orderController;
	private IWorkstationController workstationController;
	private IConnectionController connectionController;
	private ActiveOrderPanel actOrderPanel;

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
		
		//null objects to prevent checks for "null"
		orderController = new IOrderController() {
			@Override
			public void orderRemovedAction(int orderID) {}
			@Override
			public void orderCreatedAction(ProductionOrder order) {}
			@Override
			public int getNextOrderId() { return -1;}
			@Override
			public void moveOrderUp(int orderID) {}
			@Override
			public void moveOrderDown(int orderID) {}
			@Override
			public void orderUpdatedAction(ProductionOrder tmp) {}
		};
		
		workstationController = new IWorkstationController() {
			@Override
			public void workstationTypeUpdatedAction(int id, WorkstationType type) {}
			@Override
			public void workstationQualityUpdatedAction(int id, int quality) {}
		};
		
		connectionController = new IConnectionController() {
			@Override public boolean connectBT(String MAC) { return false; }
		};
		
		//TODO: just for testing
		//addOrderPanel(new ProductionOrder(20));
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmControlstation = new JFrame();
		frmControlstation.setTitle("ControlStation");
		frmControlstation.setBounds(100, 100, 800, 600);
		frmControlstation.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmControlstation.getContentPane().setLayout(new BoxLayout(frmControlstation.getContentPane(), BoxLayout.X_AXIS));
		
		JMenuBar menuBar = new JMenuBar();
		frmControlstation.setJMenuBar(menuBar);
		
		JMenu mnCom = new JMenu("Verbindung");
		menuBar.add(mnCom);
		
		JMenuItem mntmMAC = new JMenuItem("mit NXT verbinden");
		mntmMAC.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				BTConnectDialog connDlg = new BTConnectDialog(frmControlstation);
				connDlg.setVisible(true);
				if(!connDlg.isCancled) {
					if(connectionController.connectBT(connDlg.macAddress))
						JOptionPane.showMessageDialog(frmControlstation, "Erfolgreich Verbunden!");
					else
						JOptionPane.showMessageDialog(frmControlstation, "Konnte nicht mit NXT verbinden!");
				}
			}
		});
		mnCom.add(mntmMAC);
		
		JMenu mnAuftrag = new JMenu("Auftrag");
		menuBar.add(mnAuftrag);
		
		JMenuItem mntmAnlegen = new JMenuItem("anlegen");
		mntmAnlegen.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				showAddOrderDialog();
			}
		});
		mnAuftrag.add(mntmAnlegen);
		
		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout("", "[50%][50%]", "[][grow][][][]"));
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		frmControlstation.getContentPane().add(panel);
		
		ordersPanel = new OrdersPanel(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				showAddOrderDialog();
			}
		});
		panel.add(ordersPanel, "cell 0 0,grow, span 1 6");
		
		actOrderPanel = new ActiveOrderPanel();
		panel.add(actOrderPanel, "cell 1 0,grow");
		
		for(int i = 1; i < 5; i++) {
			JPanel workstation = new WorkstationPanel(i,
				new ItemListener() {
					int id;
					
					@Override
					public void itemStateChanged(ItemEvent e) {
						if (e.getStateChange() == ItemEvent.SELECTED) {
							workstationController.workstationTypeUpdatedAction(id, (WorkstationType)e.getItem());
						}
					}
					
					public ItemListener setId(int id) {
						this.id = id;
						return this;
					}
				}.setId(i),
				new ItemListener() {
					int id;
					
					@Override
					public void itemStateChanged(ItemEvent e) {
						if (e.getStateChange() == ItemEvent.SELECTED) {
							workstationController.workstationQualityUpdatedAction(id, (Integer)e.getItem());
						}
					}
					
					public ItemListener setId(int id) {
						this.id = id;
						return this;
					}
				}.setId(i)
			);
			panel.add(workstation, "cell 1 "+(i+1)+",grow");
		}
	}
	
	public void registerOrderController(IOrderController controller) {
		this.orderController = controller;
	}
	
	public void registerWorkStationController(IWorkstationController controller) {
		this.workstationController = controller;
	}
	
	public void registerConnectionController(IConnectionController controller) {
		this.connectionController = controller;
	}
	
	public void updateOrderList(List<ProductionOrder> list) {
		List<ListedOrderPanel> orderPanels = ordersPanel.getOrderPanels();
		int i = 0;
		for(ProductionOrder order : list) {
			if(orderPanels.size() < i+1) {
				addOrderPanel(order);
			} else {
				orderPanels.get(i).setOrder(order);
				orderPanels.get(i).update();
			}
			i++;
		}
		//remove excessive panels
		for(;i < orderPanels.size(); i++) {
			ordersPanel.removeOrderPanel(orderPanels.get(i));
		}
		
	}
	
	public void updateActiveOrder(ProductionOrder activeOrder, int currentStepIndex) {
		Color color = Color.YELLOW;
		String state = "Wartet";
		String id = "-";
		String currStep = "-";
		String nextStep = "-";
		
		if(activeOrder != null)
		{
			color = Color.GREEN;
			state = "Activ";
			id = String.valueOf(activeOrder.getId());
		}
		
		if(currentStepIndex >= 0  && currentStepIndex < activeOrder.size()) {
			ProductionStep step = activeOrder.get(currentStepIndex);
			currStep = "#" + String.valueOf(currentStepIndex) + "/" + String.valueOf(activeOrder.size()) +
					": " + step.getType().toString() +
					" / " + step.getMinQualityLevel() +
					" / " + step.getWorkTimeSeconds() + "s";
		}
		
		int nextStepIndex = currentStepIndex + 1;
		
		if(nextStepIndex >= 0  && nextStepIndex < activeOrder.size()) {
			ProductionStep step = activeOrder.get(nextStepIndex);
			currStep = step.getType().toString() + " / " + step.getMinQualityLevel() + " / " + step.getWorkTimeSeconds() + "s";
		}
		
		actOrderPanel.setOrderStatus(color, state, id, currStep, nextStep);
	}
	
	public void updateWorkstationState() {
		
	}
	
	private void addOrderPanel(ProductionOrder order) {
		ListedOrderPanel panel = new ListedOrderPanel(order);
		panel.addActionListener(new ActionListener() {
			private ListedOrderPanel target;
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getActionCommand() == "Edit") {
					showEditOrderDialog(target.getOrder());
				} else if (e.getActionCommand() == "Up") {
					orderController.moveOrderUp(target.getOrder().getId());
				} else if (e.getActionCommand() == "Down") {
					orderController.moveOrderDown(target.getOrder().getId());
				} else if (e.getActionCommand() == "Del") {
					orderController.orderRemovedAction(target.getOrder().getId());
				}
			}
			
			public ActionListener init(ListedOrderPanel target) {
				this.target = target;
				return this;
			}
		}.init(panel));
		ordersPanel.addOrderPanel(panel);
	}
	
	private void showAddOrderDialog() {
		ProductionOrder proto = new ProductionOrder(orderController.getNextOrderId());
		ProductionOrderDialog prodOrderDialog = new ProductionOrderDialog(proto, frmControlstation);
		prodOrderDialog.setLocationRelativeTo(frmControlstation);
		prodOrderDialog.setModal(true);
		prodOrderDialog.setVisible(true);
		
		if(prodOrderDialog.isOrderValid()) {
			orderController.orderCreatedAction(proto);
			//TODO just for testing (should be done by the listener)
			//addOrderPanel(proto);
		}
	}
	
	private ProductionOrder showEditOrderDialog(ProductionOrder order) {
		ProductionOrder tmp = new ProductionOrder(order);
		ProductionOrderDialog prodOrderDialog = new ProductionOrderDialog(tmp, frmControlstation);
		prodOrderDialog.setModal(true);
		prodOrderDialog.setVisible(true);
		
		if (prodOrderDialog.isOrderValid()) {
			orderController.orderUpdatedAction(tmp);
			return tmp;
		}
		return order;
	}

	public void setVisible(boolean b) {
		frmControlstation.setVisible(b);
	}
}
