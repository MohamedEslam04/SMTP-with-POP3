import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

// ClientHandler class 
class ClientHandler extends Thread {

    final BufferedReader inputStream;
    final PrintWriter outputStream;
    final Socket socket;
    boolean isClientAuthorized = false;
    String received = "";
    String clientEmail = "";

    // Constructor
    public ClientHandler(Socket socket, BufferedReader inputStream, PrintWriter outputStream) {
        this.socket = socket;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    @Override
    public void run() {
        boolean isConnected = true;

        while (isConnected) {
            try {
                System.out.print("New loop");
                if (socket == null || inputStream == null || outputStream == null) {
                    isConnected = false;
                    break;
                }
                this.startMessages();

                // Receive the answer from the client
                System.out.println("received " + received);

                if (received.equalsIgnoreCase("quit")) {
                    this.closeServerConnection();
                    break;
                }

                if (this.isClientAuthorized) {
                    this.handleMessagesForAuthorizedUser(received);
                } else {
                    this.handleMessagesForUnauthorizedUser(received);
                }

            } catch (Exception e) {
                isConnected = false;
                System.out.println("error " + e.getMessage());
                e.printStackTrace();
            }
        }
        this.closeServerConnection();
    }

private void startMessages() {
    try {
        if (this.isClientAuthorized) {
            // Prompt for authorized user
            outputStream.println("Input - Please choose SEND or READ or QUIT");
            outputStream.flush();
        } else {
            // Prompt for unauthorized user
            outputStream.println("Input - Please choose REGISTER or LOGIN or QUIT");
            outputStream.flush();
        }
        received = inputStream.readLine();
    } catch (Exception e) {
        e.printStackTrace();
    }
}

private void handleMessagesForUnauthorizedUser(String receivedMessage) {
    try {
        if (receivedMessage.equalsIgnoreCase("register")) {
            // Handle user registration
            this.authorizedClient(true);
        } else if (receivedMessage.equalsIgnoreCase("login")) {
            // Handle user login
            this.authorizedClient(false);
        } else {
            // Invalid input
            outputStream.println("Message - Invalid input");
            outputStream.flush();
        }
    } catch (Exception e) {
        System.out.println("error " + e.getMessage());
        e.printStackTrace();
    }
}

private void handleMessagesForAuthorizedUser(String receivedMessage) {
    try {
        if (receivedMessage.equalsIgnoreCase("send")) {
            // Handle sending an email
            this.sendAnEmail();
        } else if (receivedMessage.equalsIgnoreCase("read")) {
            // Handle reading emails
            this.readEmails();
        } else {
            // Invalid input
            outputStream.println("Message - Invalid input");
            outputStream.flush();
        }
    } catch (Exception e) {
        System.out.println("error " + e.getMessage());
        e.printStackTrace();
    }
}

private void closeServerConnection() {
    try {
        // Closing resources
        this.inputStream.close();
        this.outputStream.close();
        this.socket.close();
        System.out.println("Client " + this.socket + " sends quit...");
        System.out.println("Closing this connection.");
    } catch (Exception e) {
        e.printStackTrace();
    }
}

private void authorizedClient(boolean isNewUser) {
    try {
        // Prompt for email and password
        outputStream.println("Input - Please enter an email and a password and separate by space");
        outputStream.flush();
        String msg = inputStream.readLine();
        String[] splitedMsg = msg.split("\\s+");

        if (splitedMsg.length != 2) {
            // Invalid input
            outputStream.println("Message - invalid input");
            outputStream.flush();
            return;
        }

        String email = splitedMsg[0];
        String password = splitedMsg[1];

        if (isNewUser) {
            // Handle user registration
            this.register(email, password);
        } else {
            // Handle user login
            this.login(email, password);
        }

    } catch (Exception e) {
        System.out.println("Throw Exception");
        e.printStackTrace();
    }
}

private void register(String email, String password) {
    try {
        // Check if the user already exists
        boolean isUserExist = FileManager.isUserExist(email, password);

        if (!isUserExist) {
            // Save credentials and create user folders
            FileManager.saveCredintials(email, password);
            FileManager.createServerFolders(email);

            // Update client authorization status
            this.isClientAuthorized = true;
            this.clientEmail = email;

            // Send a welcome message
            outputStream.println("Message - Hello " + email);
            outputStream.flush();
        } else {
            // User already exists
            outputStream.println("Message - This email is already existed");
            outputStream.flush();
        }
    } catch (Exception e) {
        System.out.println("Throw Exception");
        e.printStackTrace();
    }
}

private void login(String email, String password) {
    try {
        // Check if the user exists
        boolean isUserExist = FileManager.isUserExist(email, password);

        if (isUserExist) {
            // Update client authorization status
            this.isClientAuthorized = true;
            this.clientEmail = email;

            // Send a welcome message
            outputStream.println("Message - Hello " + email);
            outputStream.flush();
        } else {
            // Invalid email or password
            outputStream.println("Message - The email or password is not correct");
            outputStream.flush();
        }
    } catch (Exception e) {
        System.out.println("Throw Exception");
        e.printStackTrace();
    }
}

private void sendAnEmail() {
    try {
        // Prompt for recipient email
        outputStream.println("Input - RCPT TO");
        outputStream.flush();
        String email = inputStream.readLine();

        // Check if the recipient exists
        boolean isEmailExist = FileManager.isEmailExist(email);

        if (isEmailExist) {
            // Recipient is valid
            outputStream.println("Message - " + email + " ...Recipient ok");
            outputStream.flush();

            // Prompt for email body
            outputStream.println("Input - Enter the email body");
            outputStream.flush();
            String body = inputStream.readLine();

            // Save the email
            FileManager.saveAnEmail(this.clientEmail, email, body);

            // Send success message
            outputStream.println("Message - Email was send successfully");
            System.out.println(body);
        } else {
            // Recipient does not exist
            outputStream.println("Message - " + email + " ...Recipient is not exist");
            outputStream.flush();
        }
    } catch (Exception e) {
        System.out.println("Throw Exception");
        e.printStackTrace();
    }
}

private void readEmails() {
    try {
        // Read emails for the current user
        String inbox = FileManager.readEmails(this.clientEmail);

        // Send the inbox content to the client
        outputStream.println("Message - Inbox for email " + this.clientEmail + "\n\n" + inbox);
        outputStream.flush();
    } catch (Exception e) {
        System.out.println("Throw Exception");
        e.printStackTrace();
    }
}

}
