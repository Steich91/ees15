package de.ees.group1.cs.gui;

import java.awt.EventQueue;
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
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;
import de.ees.group1.bt.BT_manager;
import de.ees.group1.cs.controller.ControlStation;
import de.ees.group1.model.ProductionOrder;
import de.ees.group1.model.WorkstationType;

public class MainWindow {

	private JFrame frmControlstation;
	private OrdersPanel ordersPanel;
	private IOrderController orderController;
	private IWorkstationController workstationController;
	private IConnectionController connectionController;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		/*ControlStation cs = new ControlStation(this);
		BT_manager man = cs.getManager();
		
		while(true){
			
			man.getMessage();
			
		}*/
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					ControlStation cs = new ControlStation(window);
					BT_manager man = cs.getManager();
					
					while(true){
						
						man.getMessage();
						
					}
					//window.frmControlstation.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});//*/
	}

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
			@Override
			public void activeOrderCanceledAction() {}
		};
		
		workstationController = new IWorkstationController() {
			@Override
			public void workstationTypeUpdatedAction(int id, WorkstationType type) {}
			@Override
			public void workstationQualityUpdatedAction(int id, int quality) {}
		};
		
		connectionController = new IConnectionController() {
			@Override public void connectBT(byte[] MAC) {}
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
					connectionController.connectBT(connDlg.macAddress);
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
		
		JPanel actOrderPanel = new ActiveOrderPanel();
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
		ListedOrderPanel[] orderPanels = ordersPanel.getOrderPanels();
		int i = 0;
		for(ProductionOrder order : list) {
			if(orderPanels.length < i+1) {
				addOrderPanel(order);
			} else {
				orderPanels[i].setOrder(order);
				orderPanels[i].update();				
			}
			i++;
		}
		//remove excessive panels
		for(;i < orderPanels.length; i++) {
			ordersPanel.removeOrderPanel(orderPanels[i]);
		}
		
	}
	
	public void updateActiveOrder(ProductionOrder order) {
		
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
}
