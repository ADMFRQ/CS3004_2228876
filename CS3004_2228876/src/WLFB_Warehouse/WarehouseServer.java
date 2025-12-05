package WLFB_Warehouse;
import java.net.*;
import java.io.*;

import WLFB_Warehouse.WarehouseServerThread;
import WLFB_Warehouse.WarehouseSharedState;

import WLFB_Warehouse.SupplierClient;
import WLFB_Warehouse.CustomerAClient;
import WLFB_Warehouse.CustomerBClient;


public class WarehouseServer {
	  public static void main(String[] args) throws IOException {

			ServerSocket WarehouseServerSocket = null;
		    boolean listening = true;
		    String WarehouseServerName = "WarehouseServer";
		    int WarehouseServerNumber = 4545;
		    
		    int Apples = 1000;
		    int Oranges = 1000;

		    //Create the shared object in the global scope...
		    
		    WarehouseSharedState ourSharedStateObjects = new WarehouseSharedState(Apples, Oranges );

		        
		    // Make the server socket

		    try {
		      WarehouseServerSocket = new ServerSocket(WarehouseServerNumber);
		    } catch (IOException e) {
		      System.err.println("Could not start " + WarehouseServerName + " specified port.");
		      System.exit(-1);
		    }
		    System.out.println(WarehouseServerName + " started");

		    //Got to do this in the correct order with only four clients!  Can automate this...
		    
		    while (listening){
		      new WarehouseServerThread(WarehouseServerSocket.accept(), "CustomerAClientThread", ourSharedStateObjects).start();
		      new WarehouseServerThread(WarehouseServerSocket.accept(), "CustomerBClientThread", ourSharedStateObjects).start();
		      new WarehouseServerThread(WarehouseServerSocket.accept(), "SupplierClientThread", ourSharedStateObjects).start();
		      System.out.println("New " + WarehouseServerName + " thread started.");
		    }
		    WarehouseServerSocket.close();
		  } 
}
