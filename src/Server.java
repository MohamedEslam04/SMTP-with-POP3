// Java implementation of Server side 
// It contains two classes : Server and ClientHandler 
// Save file as Server.java 

import java.io.*;
import java.net.*;

// Server class 
public class Server {

    public static void main(String[] args) throws IOException {
        final int port = 5056;
        final int maxbacklog = 1000;
        // server is listening on port 5056 
        InetAddress ip = InetAddress.getByName("localhost");
        ServerSocket socket = new ServerSocket(port, maxbacklog, ip);
        FileManager.createCredentialsFile();
        // running infinite loop for getting 
        // client request 
        while (true) {
            System.out.println(ip + " server with port number " + port + " is booted up.");
            Socket s = null;

            try {
                // socket object to receive incoming client requests 
                s = socket.accept();

                System.out.println("A new client is connected : " + s);
                BufferedReader inputStream = new BufferedReader(new InputStreamReader(s.getInputStream()));
                PrintWriter outputStream = new PrintWriter(s.getOutputStream());
                System.out.println("Assigning new thread for this client");

                // create a new thread object 
                Thread t = new ClientHandler(s, inputStream, outputStream);

                // Invoking the start() method 
                t.start();

            } catch (IOException e) {
                socket.close();
                s.close();
                e.printStackTrace();
            }
        }
    }
}
