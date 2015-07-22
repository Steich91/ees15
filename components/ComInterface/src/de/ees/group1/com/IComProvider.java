/**
 * 
 */
package de.ees.group1.com;

import de.ees.group1.model.ProductionOrder;

/**
 * @author alex
 *
 */
public interface IComProvider {
	
	void register(IControlStation cs);
	
	void register(IWorkStation ws);
	
	boolean connectWithDevice(String mac);
	
	void transmitProductionOrder(ProductionOrder order);
	
	void transmitYes();
	
	void transmitNo();
	
	void transmitFinishedStep(boolean done);
	
	/*
	 * TODO: more still to be specified
	 */
	
}
