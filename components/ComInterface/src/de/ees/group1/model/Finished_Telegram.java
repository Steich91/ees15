package de.ees.group1.model;

public class Finished_Telegram extends Telegramm{

	private int data = 0;
	
	public Finished_Telegram(int dest, int source, int data){
		super(dest, source, 4);
		
		this.data = data;
		
	}
	
	public int getDataInt(){
		
		return data;
		
	}
	
	@Override
	public String transform(){
		
		String message = null;
		
		message = ""+this.getDestination()+""+this.getSource()+""+this.getType();
		
		message = message+""+this.data; 
		
		return message;
		
	}
	
}
