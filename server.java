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
        
        // Get filename from client
        DataInputStream in = new DataInputStream(client.getInputStream());
        //String filename = in.readUTF();
     //System.out.println(in.readUTF());
        // Look for file and notify client if the file exists or not
        DataOutputStream dout = new DataOutputStream(client.getOutputStream());

      /*  File file = new File(".\\");
        File[] dirFiles = file.listFiles();
        
        boolean fileFound = false;
        for (File i : dirFiles){
            if (i.toString() == filename){
                fileFound = true;
            }
        }
        if (fileFound){
            dout.writeUTF("File "+filename+" found at server");
        }
        else {
            dout.writeUTF("File "+filename+" not found at server");
            System.exit(0);
        }

*/

        // Sending the file
        FileOutputStream out = new FileOutputStream("testfile.txt");
        byte[] buffer = new byte[4096];
            
        int filesize = 15123; // Send file size in separate msg
		int read = 0;
		int totalRead = 0;
		int remaining = filesize;
		while((read = in.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
			totalRead += read;
            remaining -= read;
            
			System.out.println("read " + totalRead + " bytes.");
			out.write(buffer, 0, read);
		}
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

    public static void main(String [] args){
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