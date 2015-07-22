package de.ees.group1.cs.gui;

import de.ees.group1.model.ProductionOrder;

public interface IOrderController {
	
	public void orderCreatedAction(ProductionOrder order);
	
	public void orderRemovedAction(int orderID);
	
	public void moveOrderUp(int orderID);
	
	public void moveOrderDown(int orderID);
	
	public int getNextOrderId();

	public void orderUpdatedAction(ProductionOrder tmp);

}