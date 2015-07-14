package de.ees.group1.model;

import java.io.*;

public class Telegramm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int destination;
	private int source;
	private int type;
	
	public Telegramm(int dest, int source, int type){
		
		this.destination = dest;
		this.source = source;
		this.type = type;
		
	}
	
	public int getDestination(){
		
		return this.destination;
		
	}
	
	public int getSource(){
		
		return this.source;
		
	}
	
	public int getType(){
		
		return this.type;
		
	}
	
	public boolean getDataBool(){
		
		return false;
		
	}
	
	public ProductionStep getDataStep(){
		
		return null;
		
	}
	
	public ProductionOrder getDataOrder(){
		
		return null;
		
	}
	
	public int getDataInt(){
		
		return 0;
		
	}
	
	public byte[] concat(byte[] a, byte[] b){
		
		int alen = a.length;
		byte[] array = new byte[alen+b.length];
		
		System.arraycopy(a, 0, array, 0, alen);
		System.arraycopy(b, 0, array, alen, b.length);
		
		return array;
		
	}
	
	public String transform(){
		
		return null;

	}
	
}
