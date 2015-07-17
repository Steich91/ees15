package de.ees.group1.cs.controller;

import de.ees.group1.bt.BT_manager;
import de.ees.group1.com.IControlStation;
import de.ees.group1.cont.WorkStation;
import de.ees.group1.cs.gui.IOrderController;
import de.ees.group1.model.OrderList;
import de.ees.group1.model.ProductionOrder;
import de.ees.group1.model.ProductionStep;


public class ControlStation implements IOrderController, IControlStation {

	private ProductionOrder currentOrder;
	private ProductionStep currentStep;
	private int currentStepNumber;
	private OrderList list;
	private int statusNXT;
	private BT_manager btManager;
	private IControlStation cs;
	ß5
	public ControlStation(){
		btManager=new BT_manager();
		btManager.register(cs);
		//Erzeugt die vier Arbeitsstationen
		for (int i = 0; i < 4; i++) {
			new WorkStation(btManager, i+1);
		}
		//Erzeugt OrderList
		list=new OrderList();
	}
	@Override
	public void orderCreatedAction(ProductionOrder order) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void orderRemovedAction(int orderID) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reachedParkingPositionInd(int orderID, int nextWorkingStep) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getNextOrderId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void moveOrderUp(int orderID) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveOrderDown(int orderID) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void orderUpdatedAction(ProductionOrder tmp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void activeOrderCanceledAction() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reachedParkingPosistionInd(int nextWorkingStep) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void giveAcknowledgement(boolean answer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void transmitActualState(int state) {
		// TODO Auto-generated method stub
		
	}

}
