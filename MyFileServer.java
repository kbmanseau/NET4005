import java.net.*;
import java.io.*;

public class MyFileServer extends Thread {

    private int requests = 0;
    private int goodRequests = 0;

    public void addRequest(){ this.requests++; }
    public void addGoodRequest(){this.goodRequests++; }
    public int getRequests(){ return requests;}
    public int getGoodRequests(){ return goodRequests;}



    public static void sendFile(Socket client) throws IOException{
        
        //Open Input and output streams
        DataInputStream in = new DataInputStream(client.getInputStream());
        DataOutputStream out = new DataOutputStream(client.getOutputStream());
        
        // Get filename from client
        String inputFromClient = in.readUTF();
        
        //String filePath = inputFromClient;
        String filePath = "server\\" + inputFromClient;

        //Check if file exists
        File temp = new File(filePath);

        if (temp.getAbsoluteFile().exists() && !temp.isDirectory()){
            //Let the client know that the file exists
            out.writeBoolean(true);
            
            //********Send the file*********
            //Read in the file
            FileInputStream file = new FileInputStream(filePath);

            int outCount;
            byte[] outBuffer = new byte[4096];
            while ((outCount = file.read(outBuffer)) > 0) {
                out.write(outBuffer, 0, outCount);
            }
            file.close();
        }

        //Close streams
		out.close();
		in.close();
    }

    public static void clientConnections(ServerSocket server){
        while (true){
            try {
                Socket client = server.accept();
                sendFile(client);
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