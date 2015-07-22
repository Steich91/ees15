package de.ees.group1.cs.controller;


import java.util.ListIterator;

import de.ees.group1.bt.BT_manager;
import de.ees.group1.com.IControlStation;
import de.ees.group1.cs.gui.IConnectionController;
import de.ees.group1.cs.gui.IOrderController;
import de.ees.group1.cs.gui.MainWindow;
import de.ees.group1.model.OrderList;
import de.ees.group1.model.ProductionOrder;
import de.ees.group1.model.ProductionStep;
import de.ees.group1.model.WorkstationType;

public class ControlStation implements IOrderController, IControlStation, IConnectionController{

	private ProductionOrder currentOrder;
	private ProductionStep currentStep;
	private int currentStepNumber;
	private OrderList list;
	private int statusNXT;
	private BT_manager btManager;
	private WorkingStationAll workingStation;
	private MainWindow mainWindow;
	private boolean isInWaitingPosition;
	private WorkingStation currentWorkStation;
	
	
	public ControlStation(MainWindow mainWindow){
		this.mainWindow=mainWindow;
		mainWindow.registerConnectionController(this);
		mainWindow.registerOrderController(this);
		mainWindow.registerWorkStationController(workingStation);

		btManager=new BT_manager();
		btManager.register(this);
		//Erzeugt die vier Arbeitsstationen
		workingStation=new WorkingStationAll();
		for (int i = 0; i < 4; i++) {
			new WorkingStation(btManager, i+1, workingStation);
		}
		//Erzeugt OrderList
		list=new OrderList();
		
		//Test
		currentOrder = new ProductionOrder(0);
		currentStep=new ProductionStep(WorkstationType.DRILL, 1,2);
		currentOrder.add(currentStep);
		currentStep=new ProductionStep(WorkstationType.LATHE, 3,2);
		currentOrder.add(1,currentStep);
		currentStep=new ProductionStep(WorkstationType.DRILL, 1,3);
		currentOrder.add(2,currentStep);
		list.setProductionOrder(currentOrder);
		
		currentOrder = new ProductionOrder(3);
		currentStep=new ProductionStep(WorkstationType.DRILL, 1,2);
		currentOrder.add(currentStep);
		currentStep=new ProductionStep(WorkstationType.LATHE, 4,2);
		currentOrder.add(1,currentStep);
		currentStep=new ProductionStep(WorkstationType.DRILL, 1,3);
		currentOrder.add(2,currentStep);
		
		list.setProductionOrder(currentOrder);
		
		currentOrder=new ProductionOrder(45);
		currentStep=new ProductionStep(WorkstationType.DRILL,3,4);
		currentOrder.add(currentStep);
		currentStep=new ProductionStep(WorkstationType.LATHE,2,3);
		currentOrder.add(currentStep);
		list.setProductionOrder(currentOrder);
		
		workingStation.workstationQualityUpdatedAction(1, 1);
		workingStation.workstationQualityUpdatedAction(2, 1);
		workingStation.workstationQualityUpdatedAction(3, 3);
		workingStation.workstationQualityUpdatedAction(4, 3);
		workingStation.workstationTypeUpdatedAction(1, WorkstationType.DRILL);
		workingStation.workstationTypeUpdatedAction(2, WorkstationType.LATHE);
		workingStation.workstationTypeUpdatedAction(3, WorkstationType.DRILL);
		workingStation.workstationTypeUpdatedAction(4, WorkstationType.LATHE);
		
		mainWindow.updateOrderList(list);
	}
	
	public BT_manager getManager(){
		
		return this.btManager;
		
	}
	
	/*
	 * �bergibt den gerade an den NXT �bermittleten Auftrag an die ControlStation
	 */	
	public void setCurrentOrder(){
		currentOrder=list.getFirstOrder();
		currentStepNumber=0;
	}
	
	/*
	 * F�gt der Liste mit den ProductionOrder einen neuen Auftrag zu.
	 */
	public void addProductionOrder(ProductionOrder order){
		list.setProductionOrder(order);
		
	}
	
	public int getStatusNXT(){
		return statusNXT;
	}
	
	public void evaluateStatusNXT(){
		int status=getStatusNXT();
		if ((0<status) & (status<=22)){
			switch (status){
			case 1: case 5: case 9: case 13: case 17:
				currentWorkStation=workingStation.getWorkingStaion(1);
				break;
			case 2: case 6: case 10: case 14: case 18:
				currentWorkStation=workingStation.getWorkingStaion(2);
				break;
			case 3: case 7: case 11: case 15: case 19:
				currentWorkStation=workingStation.getWorkingStaion(3);
				break;
			case 4: case 8: case 12: case 16: case 20:
				currentWorkStation=workingStation.getWorkingStaion(4);
				break;
			case 21:
				//neuen Auftrag ansto�en
				break;
			case 22:
				//Meldung alles kaputt
				break;
			}
		}
		if ((0<status) & (status<=20)){
			switch (status){
			case 1: case 2: case 3: case 4: 
				currentWorkStation.setStatus(1);
				mainWindow.updateWorkstationState();
				break;
			case 5: case 6: case 7: case 8: 
				//action to "Weiterfahrt";
				break;
			case 9: case 10: case 11: case 12: 
				//arbeit beginnen;
				break;
			case 13: case 14: case 15: case 16: 
				currentWorkStation.setStatus(0);
				mainWindow.updateWorkstationState();
				currentStepNumber++;
				break;
			case 17: case 18: case 19: case 20: 
				//arbeit konnte nicht durchgef�hrt werden;
				break;
			}
		}
	}
	
	public void setCurrentStep(ProductionStep step) {
		currentStep=step;		
	}

	
	public ProductionStep getCurrentStep() {
		return currentStep;
	}


	public void orderCreatedAction(ProductionOrder order) {
		list.add(order);
		mainWindow.updateOrderList(list);
		if(isInWaitingPosition==true){
			reachedParkingPositionInd(21);
		}
	}

	
	
	public void orderRemovedAction(int orderID) {
		ListIterator<ProductionOrder> iterator=list.listIterator();
		while(iterator.hasNext()){
			ProductionOrder temp=iterator.next();
			if (temp.getId()==orderID){
				list.remove(temp);
			}
		}
		mainWindow.updateOrderList(list);
	}


	public int getNextOrderId() {
		return currentStepNumber++;
	}

	
	public void moveOrderDown(int orderID) {
		int i=0;
		ListIterator<ProductionOrder> iterator=list.listIterator();
		while(iterator.hasNext()&(i==0)){
			ProductionOrder temp=iterator.next();
			if (temp.getId()==orderID){
				if(iterator.hasNext()){
				list.remove(temp);
				int index =iterator.nextIndex();
				list.add(index, temp);
				i=1;
				}
			}
		}
		mainWindow.updateOrderList(list);
	}

	
	public void moveOrderUp(int orderID) {
		int i=0;
		ListIterator<ProductionOrder> iterator=list.listIterator();
		while(iterator.hasNext()&(i==0)){
			ProductionOrder temp=iterator.next();
			if (temp.getId()==orderID){
				if(iterator.hasNext()){
					list.remove(temp);
				int index =iterator.nextIndex();
				index=index-2;
				list.add(index, temp);
				i=1;
				}
			}
		}
		mainWindow.updateOrderList(list);
	}

	
	public void orderUpdatedAction(ProductionOrder tmp) {
		ListIterator<ProductionOrder> iterator=list.listIterator();
		while (iterator.hasNext()){
			ProductionOrder order =iterator.next();
			if(order.getId()==tmp.getId()){
				iterator.set(tmp);
			}
		}
		mainWindow.updateOrderList(list);
	}

	//Auftrag erfolgreich �bertragen, keine Reaktion 
	public void giveAcknowledgement(boolean answer) {
	}

	public void transmitActualState(int state) {
		statusNXT=state;
		evaluateStatusNXT();
		
	}

	public void reachedParkingPositionInd(int nextWorkingStep) {
		if (nextWorkingStep==21){
			if(isInWaitingPosition==false){
				list.remove(0);
			}
			if(list.isEmpty()==false){
			currentOrder=list.getFirstOrder();
			btManager.transmitProductionOrder(currentOrder);
			isInWaitingPosition=false;
			}
			else{
				isInWaitingPosition=true;
		}
	}
}

	@Override
	public boolean connectBT(String MAC) {
		return btManager.connectWithDevice(MAC);
	}
}
