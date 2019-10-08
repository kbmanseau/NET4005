import java.net.*;
import java.io.*;

public class server extends Thread {

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
        String filePath = "server\\" + inputFromClient;
        //String filePath = inputFromClient;
        System.out.println(filePath);
        System.out.println("Working Directory = " +
              System.getProperty("user.dir"));
        //Check if file exists
        File temp = new File(inputFromClient.toString());
        if (temp.getAbsoluteFile().exists() && !temp.isDirectory()){
            //.\server\paint.mp4
            //.\server\paint.mp4
            //Let the client know that the file exists
            //out.writeBoolean(true);
            out.writeBoolean(true);
            
            //********Send the file*********
            //Read in the file
            FileInputStream file = new FileInputStream(filePath);
            System.out.println(filePath);

            byte[] buffer = new byte[4096];
            file.read(buffer, 0, buffer.length);

            //Send the file through an output stream
            out.write(buffer, 0, buffer.length);

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