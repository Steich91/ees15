package de.ees.group1.model;

public class Order_Telegram extends Telegramm{
	
	private ProductionOrder data = null;
	
	public Order_Telegram(int destination, int source, ProductionOrder data){
		super(destination, source, 1);
		
		this.data = data;
		
	}
	
	@Override
	public ProductionOrder getDataOrder(){
		
		return data;
		
	}
	
	@Override
	public String transform(){
		
		String message = null;
		
		message = ""+this.getDestination()+""+this.getSource()+""+this.getType();
		
		message = message + data.getId() + this.data.size();
		
		for(int i = 0; i< this.data.size(); i++){
			
			ProductionStep step = this.data.get(i);
			
			message = message + i;
			switch(step.getType()){
			case NONE: message = message + 0;
			case LATHE: message = message + 1;
			case DRILL: message = message + 2;
			default: 
			}
			message = message + step.getWorkTimeSeconds() + step.getMinQualityLevel();
			message = message + "|";
			
		}
		
		return message;
		
	}

}
