import java.net.*;
import java.io.*;



public class client {  

    public static void requestFile(String filename, Socket socket) throws IOException {        
        //Open streams
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        DataInputStream in = new DataInputStream(socket.getInputStream());

        //Send filename to server
        out.writeUTF(filename);
        

        //Check if file was found
        if (in.readBoolean()) {
            System.out.println("Made it here");
            //Create input stream to receive the file
            byte[] buffer = new byte[4096];
            InputStream inputFile = socket.getInputStream();
            System.out.println(inputFile);

            //Output stream to write the file
            FileOutputStream fileOutput = new FileOutputStream(filename);
            inputFile.read(buffer, 0, buffer.length);
            fileOutput.write(buffer, 0, buffer.length);
            inputFile.close();
            fileOutput.close();
        }  
    }

    public static void main(String[] args) throws Exception {
        Socket socket;
        
        int port = new Integer(args[1]);
        String host = new String(args[0]); 
        String filename = new String(args[2]);

        // Collect data from command line
        if (args.length == 3){
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