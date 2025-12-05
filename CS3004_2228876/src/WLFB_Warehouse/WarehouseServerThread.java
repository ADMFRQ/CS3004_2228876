package WLFB_Warehouse;
import java.net.*;

import java.io.*;

import WLFB_Warehouse.WarehouseSharedState;

public class WarehouseServerThread extends Thread{
	
	  private Socket warehouseSocket = null;
	  private WarehouseSharedState myWarehouseSharedStateObject;
	  private String myWarehouseServerThreadName;
	  private double mySharedVariable;

	  
	  //Setup the thread
	  	public WarehouseServerThread(Socket warehouseSocket, String WarehouseServerThreadName, WarehouseSharedState SharedObject) {
		
//		  super(WarehouseServerThreadName);
		  this.warehouseSocket = warehouseSocket;
		  myWarehouseSharedStateObject = SharedObject;
		  myWarehouseServerThreadName = WarehouseServerThreadName;
		}
	  	
	    public void run() {
	        try {
	          System.out.println(myWarehouseServerThreadName + " initialising.");
	          PrintWriter out = new PrintWriter(warehouseSocket.getOutputStream(), true);
	          BufferedReader in = new BufferedReader(new InputStreamReader(warehouseSocket.getInputStream()));
	          String inputLine, outputLine;

	          while ((inputLine = in.readLine()) != null) {
	        	  // Get a lock first
	        	  try { 
	        		  myWarehouseSharedStateObject.acquireLock();  
	        		  outputLine = myWarehouseSharedStateObject.processInput(myWarehouseServerThreadName, inputLine);
	        		  out.println(outputLine);
	        		  myWarehouseSharedStateObject.releaseLock();  
	        	  } 
	        	  catch(InterruptedException e) {
	        		  System.err.println("Failed to get lock when reading: "+e);
	        	  }
	          }

	           out.close();
	           in.close();
	           warehouseSocket.close();

	        } catch (IOException e) {
	          e.printStackTrace();
	        }
	      }
}
