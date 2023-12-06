import java.io.*;
import java.net.*;
import java.util.Scanner;

// Client class 
public class Client {

    public static void main(String[] args) throws IOException {
        final int port = 5056;
        String received;

        try {
            Scanner scanner = new Scanner(System.in);

            // Getting localhost IP
            InetAddress ip = InetAddress.getByName("localhost");

            // Establish the connection with the server on port 5056
            Socket socket = new Socket(ip, port);

            BufferedReader inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter outputStream = new PrintWriter(socket.getOutputStream());

            // The following loop performs the exchange of information between client and client handler
            while (true) {
                received = inputStream.readLine();
                System.out.println(received);

                if (received.contains("Input")) {
                    // User input
                    String sendmesg = scanner.nextLine();
                    outputStream.println(sendmesg);
                    outputStream.flush();

                    if (sendmesg.equals("quit")) {
                        break;
                    }
                }
            }

            // Closing resources
            System.out.println("Closing this connection: " + socket);
            System.out.println("Connection closed");
            scanner.close();
            inputStream.close();
            outputStream.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
