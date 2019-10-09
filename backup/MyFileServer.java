/*
Alexandre Lacasse 101001105
Kyler Manseau 101003961

NET 4005
Assignment 1

*/

import java.net.*;
import java.io.*;

public class MyFileServer extends Thread {

    public static int[] sendFile(Socket client, int[] requests) throws IOException{
        
        //Open Input and output streams
        DataInputStream in = new DataInputStream(client.getInputStream());
        DataOutputStream out = new DataOutputStream(client.getOutputStream());
        
        // Get filename from client
        String inputFromClient = in.readUTF();

        //String filePath = inputFromClient;
        String filePath = "server\\" + inputFromClient;

        //Check if file exists
        File temp = new File(filePath);


        //Add to requests counter
        requests[0]++;
        System.out.println("REQ " + requests[0]+ ": File "+ inputFromClient +" requested from " + client.getLocalAddress());


        if (temp.getAbsoluteFile().exists() && !temp.isDirectory()){
                                    
            //Let the client know that the file exists
            out.writeBoolean(true);
            out.writeUTF("Server handled " + requests[0] + " requests, " + requests[1] + " requests were successful");
            //********Send the file*********
            //Read in the file
            FileInputStream file = new FileInputStream(filePath);

            int outCount;
            byte[] outBuffer = new byte[4096];
            while ((outCount = file.read(outBuffer)) > 0) {
                out.write(outBuffer, 0, outCount);
            }
            file.close();

            requests[1]++;  
            System.out.println("REQ "+ requests[0] + ": Successful");
            System.out.println("REQ "+ requests[0] + ": Total successful requests so far = " + requests[1]);
            System.out.println("REQ "+ requests[0] + ": File transfer Complete");
            
        } 
        else{
            System.out.println("REQ " + requests[0] + " Not Successful");
            out.writeBoolean(false);
            out.writeUTF("Server handled " + requests[0] + " requests, " + requests[1] + " requests were successful");
            System.out.println("REQ "+ requests[0] + ": Total successful requests so far = " + requests[1]);
            System.out.println("REQ "+ requests[0] + ": File transfer incomplete");
            
        }

        //Close streams
		out.close();
        in.close();
        return requests;
    }

    public static void clientConnections(ServerSocket server){
        int requests[] = {0,0};
        //[0] = total requests
        //[1] = successful requests
        while (true){
            try { 
                Socket client = server.accept();
                requests = sendFile(client,requests);
                //send variables to client
            } catch (IOException e){
                e.printStackTrace();
            }
        }  
    }

    public static void main(String [] args) throws Exception {
        ServerSocket server;
        int port = 8010;
        try {
            server = new ServerSocket(port);
            clientConnections(server);
            server.close();
        } catch (IOException e){
            e.printStackTrace();
        }        
    }
}