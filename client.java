import java.net.*;
import java.io.*;



public class client {  

    public static void sendFile(String filename, Socket socket) throws IOException {
        //Code inspired from Carl Ekerot
        //https://gist.github.com/CarlEkerot/2693246
        
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        FileInputStream fileinput = new FileInputStream(filename);
        byte[] buffer = new byte[4096];
        while (fileinput.read(buffer) > 0){
            out.write(buffer);
        }

        out.close();
        fileinput.close();
    }

    public static void main(String[] args) {
        Socket socket;
        String host = new String(); 
        String filename = new String();
        int port = 0;

        // Collect data from command line
        if (args.length == 3){
            host = args[0];
            port = new Integer(args[1]);
            filename = args[2];
            try {
                socket = new Socket(host, port);
                sendFile(filename, socket);
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