import java.net.*;
import java.io.*;



public class MyFileClient {  

    public static void requestFile(String filename, Socket socket) throws IOException {        
        //Open streams
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        DataInputStream in = new DataInputStream(socket.getInputStream());

        //Send filename to server
        out.writeUTF(filename);
        

        //Check if file was found
        if (in.readBoolean()) {
            InputStream inputFile = socket.getInputStream();
            FileOutputStream fileOutput = new FileOutputStream(filename);

            int inCount;
            byte[] inBuffer = new byte[4096];
            while ((inCount = inputFile.read(inBuffer)) > 0) {
                fileOutput.write(inBuffer, 0, inCount);
            }
            
            inputFile.close();
            fileOutput.close();
        }  
    }

    public static void main(String[] args) throws Exception {
        Socket socket;

        // Collect data from command line
        if (args.length == 3){
            int port = new Integer(args[1]);
            String host = new String(args[0]); 
            String filename = new String(args[2]);
            try {
                socket = new Socket(host, port);
                requestFile(filename, socket);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        else if (args.length < 3){
            System.out.println("Arguments missing. Try again");
            System.out.println("command format: java client <server ip> <port> <file name>");
            System.exit(0);
        }
        else {
            System.out.println("Too many arguments. Try again");
            System.out.println("command format: java client <server ip> <port> <file name>");
            System.exit(0);
        }
    }
}