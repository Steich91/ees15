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
		
		if(data.getId()>9){
			message = message + data.getId() + this.data.size();
		}else{
			message = message + "0" + data.getId() + this.data.size();
		}
		
		for(int i = 0; i< this.data.size(); i++){
			
			ProductionStep step = this.data.get(i);
			
			message = message + i;
			switch(step.getType()){
			case NONE: {
				message = message + 0;
				break;
			}
			case LATHE: {
				message = message + 1;
				break;
			}
			case DRILL: {
				message = message + 2;
				break;
			}
			default: message = message + 0;
			}
			message = message + (step.getWorkTimeSeconds()-1) + step.getMinQualityLevel();
			message = message + "|";
			
		}
		
		return message;
		
	}

}
