package WLFB_Warehouse;
import java.net.*;
import java.io.*;

// This class creates the Supplier client setting up the socket and the in/out variables for client to server messages.
// it also creates the socket that connects to the warehouse server's port number 
// handles communication to the server from the Supplier


public class SupplierClient {
    public static void main(String[] args) throws IOException {


        Socket ClientSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        int WarehouseSocketNumber = 4545;
        String WarehouseServerName = "localhost";
        String ClientID = "Supplier";
                

        try {
            ClientSocket = new Socket(WarehouseServerName, WarehouseSocketNumber);
            out = new PrintWriter(ClientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(ClientSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: localhost ");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: "+ WarehouseSocketNumber);
            System.exit(1);
        }

        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String fromServer;
        String fromUser;

        System.out.println("Initialised " + ClientID + " client and IO connections");
        
        // This is modified as it's the client that speaks first

        while (true) {
            
            fromUser = stdIn.readLine();
            if (fromUser != null) {
                System.out.println(ClientID + " sending " + fromUser + " to WarehouseServer");
                out.println(fromUser);
            }
            fromServer = in.readLine();
            System.out.println(ClientID + " received " + fromServer + " from WarehouseServer");
        }
            
        
       // Tidy up - not really needed due to true condition in while loop
//        out.close();
//        in.close();
//        stdIn.close();
//        ClientSocket.close();
    }


}
