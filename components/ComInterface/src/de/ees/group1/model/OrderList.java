
	package de.ees.group1.model;

	import java.util.LinkedList;
	/*
	 * This class ist a List which contains all ProductionOrders
	 */
	public class OrderList extends LinkedList<ProductionOrder>{

		/**
		 * version uid for serialization
		 */
		private static final long serialVersionUID = -8252618943092239548L;
		
		public OrderList(){
			
		}
		
		public void setProductionOrder(ProductionOrder order){
			this.add( order);
		}
		
		public ProductionOrder getFirstOrder(){
			return this.getFirst();
		}
	}

