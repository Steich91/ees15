package de.ees.group1.bt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;
import de.ees.group1.model.Ack_Telegram;
import de.ees.group1.model.Order_Telegram;
import de.ees.group1.model.ProductionStep;
import de.ees.group1.model.State_Telegram;
import de.ees.group1.model.Step_Telegram;
import de.ees.group1.model.Telegramm;
import de.ees.group1.model.WorkstationType;

/**
 * 
 * @author sven
 *
 * BT_device ist das lokale Bluetooth-Gerät. Es funktioniert als Master.
 * 
 */
public class BT_device /*implements DiscoveryListener*/ {

	private NXTInfo nxtInfo = null;
	private NXTComm nxtComm = null;
	private InputStream dis = null;
	private OutputStream dos = null;
	private String mac = "00:16:53:05:65:FD";
	
	/**
	 * 
	 * Konstruktor der Klasse BT_device.
	 * 
	 */
		
	public BT_device(){
		
		try {
			this.nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
		} catch (NXTCommException e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	/**
	 * Stellt eine Verbindung zu dem Fahrzeug her.
	 * 
	 * @return true, wenn Verbindung hergestellt wurde, andernfalls false
	 * @throws IOException
	 */
	public boolean searchRemoteDevice() throws IOException{
		
		this.nxtInfo = new NXTInfo(NXTCommFactory.BLUETOOTH, "NXT", this.mac);
		
		try {
			
			this.nxtComm.open(this.nxtInfo);
			return true;
			
		} catch (NXTCommException e) {
			System.out.println(e.getMessage());
			return false;
			
		}
		
	}
	
	/**
	 * Definiert die zu Suchende MAC-Adresse
	 * @param mac MAC-Adresse
	 * 
	 */
	public void setSearchMAC(String mac){
		
		this.mac = mac; 
		
	}
	
	/**
	 * Stellt Streams über die Verbindung her.
	 * @return 
	 */
	public boolean startService(){
		
		this.dis = nxtComm.getInputStream();
		
		return true;
		
	}
	
	public Telegramm receiveMessage() throws IOException{
		
		String message;
		byte[] data; 
		int type;
		int length_1 = 0;
		int length_2 = 0;
		int length = 0;
		
		length_1 = dis.read()*16*16;
		length_1 = length_1 + dis.read();
		data = new byte[length_1];
		length = dis.read(data);
		length_2 = dis.read()*16*16;
		length_2 = length_2 + dis.read();
		
		if(length_1 != length_2){
			
			System.out.println("Telegramm fehlerhaft");
			return null;
			
		}
		
		type = data[2];
		
		switch(type){
		case 0: {
			if(data[3]== 0){
				return new Ack_Telegram(data[0], data[1], false);
			}else{
				return new Ack_Telegram(data[0], data[1], true);
			}
		}
		case 1: {
			System.out.println("Fehlerhaftes Telegramm");
			return null;
		}
		case 2: {
			ProductionStep prodStep = new ProductionStep();
			prodStep.setMinQualityLevel(data[5]);
			prodStep.setWorkTimeSeconds(data[4]);
			switch(data[3]){
			case 0:	{
				prodStep.setType(WorkstationType.NONE);
				break;
			}
			case 1: {
				prodStep.setType(WorkstationType.LATHE);
				break;
			}
			case 2: {
				prodStep.setType(WorkstationType.DRILL);
				break;
			}
			default: {
				prodStep.setType(WorkstationType.NONE);
			}
			}
			return new Step_Telegram(data[0], data[1], prodStep);
		}
		case 3:
			return new State_Telegram(data[0], data[1], data[2]);
		default:
		}
		
		message = new String(data);
		System.out.println("Telegram empfangen: Unbekannter Typ" + message);
		
		return null;
		
	}
	
	/**
	 * Überträgt ein Telegramm an den NXT.
	 * @param message Zu übertragende Nachricht.
	 * @return true, wenn Übertragung erfolgreich.
	 */
	public boolean sendMessage(Telegramm message){

		int length = 0;
		byte[] length_data = new byte[2];
		
		String transformed = message.transform();
		System.out.println(transformed);
		length = transformed.length();
		System.out.println(""+length);
		for(int i = 0; i<2; ++i){
			int shift = i << 3;
			length_data[1-i] = (byte)((length & (0xff << shift))>>shift);
		}
		
		byte[] data = message.concat(length_data, message.concat(transformed.getBytes(), length_data));
		System.out.println(data.toString());
		System.out.println(new String(data));
		
		try{
			
			this.dos = nxtComm.getOutputStream();
			this.dos.write(data);
			this.dos.flush();
			this.dos.close();
			return true;
			
		}catch(IOException e){
			
			return false;	
			
		}
			
	}
	
	/**
	 * Beendet die Verbindung mit dem Fahrzeug
	 * @throws IOException
	 */
	public void close() throws IOException{
		
		this.dis.close();
		
	}
	
	/**
	 * Wandelt ein Byte-Array in einen Hex-String um
	 * @param data Byte-Array
	 * @return Hex-String
	 */
	public String byteToHex(byte[] data){
		
		StringBuffer sb = new StringBuffer();
		
		for(int y = 0; y < data.length; y++){
			
			if(y > 0) sb.append(':');
			sb.append(Integer.toString((data[y] & 0xff) + 0x100, 16).substring(1));
			
		}
		
		return sb.toString();
		
	}
		
}
