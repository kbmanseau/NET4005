import java.net.*;
import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyFileServer {
    static class MultiThreadServer {
        
        public MultiThreadServer(){
            //Create pool of 10 threads
            ExecutorService executor = Executors.newFixedThreadPool(10);
            
            //Create stats object to keep track of stats
            ServerStatistics statistics = new ServerStatistics();

            //handle request from clients
            int port = 8011;
            ServerSocket server;

            try {
                server = new ServerSocket(port);
                while (true){
                    Socket client = null;
                    try {
                        client = server.accept();
                        Thread worker = new Thread(new ClientWorkerThread(server, client, statistics));
                        executor.execute(worker);
                        
                    } catch (IOException e){
                        server.close();
                        e.printStackTrace();
                    }
                }
            }
            catch (IOException e){
                e.printStackTrace();
            }
        } 
    }

    static class ClientWorkerThread implements Runnable {
        Socket client;
        ServerSocket server;
        ServerStatistics statistics;

        public ClientWorkerThread(ServerSocket server, Socket clientSocket, ServerStatistics serverStatistics){
            this.server = server;
            this.client = clientSocket;
            this.statistics = serverStatistics;
        }

        public void run(){
            try{
                //Open Input and output streams
                DataInputStream in = new DataInputStream(client.getInputStream());
                DataOutputStream out = new DataOutputStream(client.getOutputStream());
                
                // Get filename from client
                String inputFromClient = in.readUTF();

                //String filePath = inputFromClient;
                String filePath = "server\\" + inputFromClient;

                //Check if file exists
                File temp = new File(filePath);

                //Add to totalrequests counter and save it locally
                statistics.incrementTotalRequests();
                int requestTracker = statistics.getTotalRequests();

                //Save goodRequests locally
                int goodTracker = statistics.getGoodRequests();

                System.out.println("REQ " + requestTracker+ ": File "+ inputFromClient +" requested from " + client.getLocalAddress());

                if (temp.getAbsoluteFile().exists() && !temp.isDirectory()){
                                            
                    //Let the client know that the file exists
                    out.writeBoolean(true);
                    out.writeUTF("Server handled " + requestTracker + " requests, " + goodTracker + " requests were successful");
                    //********Send the file*********
                    //Read in the file
                    FileInputStream file = new FileInputStream(filePath);

                    int outCount;
                    byte[] outBuffer = new byte[4096];
                    while ((outCount = file.read(outBuffer)) > 0) {
                        out.write(outBuffer, 0, outCount);
                    }
                    file.close();

                    statistics.incrementGoodRequests(); 
                    System.out.println("REQ "+ requestTracker + ": Successful");
                    System.out.println("REQ "+ requestTracker + ": Total successful requests so far = " + goodTracker);
                    System.out.println("REQ "+ requestTracker + ": File transfer Complete");
                    
                } 
                else{
                    System.out.println("REQ " + requestTracker + " Not Successful");
                    out.writeBoolean(false);
                    out.writeUTF("Server handled " + requestTracker + " requests, " + goodTracker + " requests were successful");
                    System.out.println("REQ "+ requestTracker + ": Total successful requests so far = " + goodTracker);
                    System.out.println("REQ "+ requestTracker + ": File transfer incomplete");
                    
                }

                //Close streams
                out.close();
                in.close();
                return;
            }
            catch(Exception e){}
        }
    }

    
    static class ServerStatistics {
        private int totalRequests = 0;
        private int goodRequests = 0;

        public int getTotalRequests(){
            return this.totalRequests;
        }
        public int getGoodRequests(){
            return this.goodRequests;
        }

        public void incrementTotalRequests(){
            this.totalRequests++;
        }
        public void incrementGoodRequests(){
            this.goodRequests++;
        }
    }
    public static void main(String [] args) throws Exception {
        MultiThreadServer threadserver = new MultiThreadServer();       
    }
}
