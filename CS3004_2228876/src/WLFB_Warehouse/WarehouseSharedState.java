package WLFB_Warehouse;
import java.net.*;
import java.io.*;

import WLFB_Warehouse.WarehouseSharedState;

// This class stores and updates the shared warehouse inventory of apples and oranges.
// It sets up the locks each thread uses to access the variables in order to maintain their integrity while having multiple clients trying to access them at the same time


public class WarehouseSharedState{
	
	private WarehouseSharedState mySharedObj;
	private String myThreadName;
	private int mySharedApples;
	private int mySharedOranges;
	private boolean accessing=false; // true a thread has a lock, false otherwise
	private int threadsWaiting=0; // number of waiting writers

// Constructor	
	
	WarehouseSharedState(int apples, int oranges) {
		mySharedApples = apples;
		mySharedOranges = oranges;
	}

//Attempt to acquire a lock
	
	  public synchronized void acquireLock() throws InterruptedException{
	        Thread me = Thread.currentThread(); // get a ref to the current thread
	        System.out.println(me.getName()+" is attempting to acquire a lock!");	
	        ++threadsWaiting;
		    while (accessing) {  // while someone else is accessing or threadsWaiting > 0
		      System.out.println(me.getName()+" waiting to get a lock as someone else is accessing...");
		      //wait for the lock to be released - see releaseLock() below
		      wait();
		    }
		    // nobody has got a lock so get one
		    --threadsWaiting;
		    accessing = true;
		    System.out.println(me.getName()+" got a lock!"); 
		  }

		  // Releases a lock to when a thread is finished
		  
		  public synchronized void releaseLock() {
			  //release the lock and tell everyone
		      accessing = false;
		      notifyAll();
		      Thread me = Thread.currentThread(); // get a ref to the current thread
		      System.out.println(me.getName()+" released a lock!");
		  }
	
	
    // input method that checks who the client thread is from and what requests they can send and what to do in response to them

	public synchronized String processInput(String myThreadName, String theInput) {
    		System.out.println(myThreadName + " received "+ theInput);
    		String theOutput = null;
    		
    		// Check who the client is
    		
    		if (myThreadName.equals("SupplierClientThread")) { // all the actions available for the Supplier
    			if (theInput.equalsIgnoreCase("Check_Stock")) {
    				System.out.println("Stock check:/n" + mySharedApples + " apples and " + mySharedOranges + " oranges.");
    				theOutput = "Action completed. Now there are " + mySharedApples + " apples and " + mySharedOranges + " oranges.";
    				
    			}
    			else if (theInput.startsWith("Add_Apples(")) {
    				
    				String numberString = theInput.substring(11, theInput.length() - 1);
    			    int number = Integer.parseInt(numberString);
    			    
    			    mySharedApples = mySharedApples + number;
    			    theOutput = "Added " + number + " apples to the stock. New stock: " + mySharedApples;
    			    
    			}
    			else if (theInput.startsWith("Add_Oranges(")) {
    				
    				String numberString = theInput.substring(12, theInput.length() - 1);
    			    int number = Integer.parseInt(numberString);
    			    
    			    mySharedOranges = mySharedOranges + number;
    			    theOutput = "Added " + number + " oranges to the stock. New stock: " + mySharedOranges;
    			    
    			}
    			else {
    				theOutput = "Did not understand request.";
    				
    			}
    		}
    			
    		else if (myThreadName.equals("CustomerAClientThread")) { // all the actions available for Customer A
    			if (theInput.equalsIgnoreCase("Check_Stock")) {
    				System.out.println("Stock check:/n" + mySharedApples + " apples and " + mySharedOranges + " oranges.");
    				theOutput = "Action completed. Now there are " + mySharedApples + " apples and " + mySharedOranges + " oranges.";
    			}
    			else if (theInput.startsWith("Buy_Apples(")) {
    				
    				String numberString = theInput.substring(11, theInput.length() - 1);
    			    int number = Integer.parseInt(numberString);
    			    
    			    mySharedApples = mySharedApples - number;
    			    theOutput = "Bought " + number + " apples from the stock. New stock: " + mySharedApples;
    			}
    			else if (theInput.startsWith("Buy_Oranges(")) {
    				
    				String numberString = theInput.substring(12, theInput.length() - 1);
    			    int number = Integer.parseInt(numberString);
    			    
    			    mySharedOranges = mySharedOranges - number;
    			    theOutput = "Bought " + number + " oranges from the stock. New stock: " + mySharedOranges;
    			}
    			else {
    				theOutput = "Did not understand request.";
    			}
    		}
    		else if (myThreadName.equals("CustomerBClientThread")) { // all the actions available for Customer B
    			if (theInput.equalsIgnoreCase("Check_Stock")) {
    				System.out.println("Stock check:/n" + mySharedApples + " apples and " + mySharedOranges + " oranges.");
    				theOutput = "Action completed. Now there are " + mySharedApples + " apples and " + mySharedOranges + " oranges.";
    			}
    			else if (theInput.startsWith("Buy_Apples(")) {
    				
    				String numberString = theInput.substring(11, theInput.length() - 1);
    			    int number = Integer.parseInt(numberString);
    			    
    			    mySharedApples = mySharedApples - number;
    			    theOutput = "Bought " + number + " apples from the stock. New stock: " + mySharedApples;
    			}
    			else if (theInput.startsWith("Buy_Oranges(")) {
    				
    				String numberString = theInput.substring(12, theInput.length() - 1);
    			    int number = Integer.parseInt(numberString);
    			    
    			    mySharedOranges = mySharedOranges - number;
    			    theOutput = "Bought " + number + " oranges from the stock. New stock: " + mySharedOranges;
    			}
    			else {
    				theOutput = "Did not understand request.";
    			}
    		}
    			
     	//Return the output message to the WarehouseServer
    		System.out.println(theOutput);
    		return theOutput;
    	}	
}

