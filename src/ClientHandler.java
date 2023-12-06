
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
        // Ask user what he wants

        while (isConnected) {
            try {
                System.out.print("New loop");
                if (socket == null || inputStream == null || outputStream == null) {
                    isConnected = false;
                    break;
                }
                this.startMessages();
                // receive the answer from client
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
                outputStream.println("Input - Please choose SEND or READ or QUIT");
                outputStream.flush();
            } else {
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
                this.authorizedClient(true);
            } else if (receivedMessage.equalsIgnoreCase("login")) {
                this.authorizedClient(false);
            } else {
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
                this.sendAnEmail();
            } else if (receivedMessage.equalsIgnoreCase("read")) {
                this.readEmails();
            } else {
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
            // closing resources
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
            outputStream.println("Input - Please enter an email and a password and seperate by space");
            outputStream.flush();
            String msg = inputStream.readLine();
            String[] splitedMsg = msg.split("\\s+");
            if (splitedMsg.length != 2) {
                outputStream.println("Message - invalid input");
                outputStream.flush();
                return;
            }
            String email = splitedMsg[0];
            String password = splitedMsg[1];
            if (isNewUser) {
                this.register(email, password);
            } else {
                this.login(email, password);
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("Throw Exception");
            e.printStackTrace();
        }
    }

    private void register(String email, String password) {
        try {
            boolean isUserExist = FileManager.isUserExist(email, password);
            if (!isUserExist) {
                FileManager.saveCredintials(email, password);
                FileManager.createServerFolders(email);
                this.isClientAuthorized = true;
                this.clientEmail = email;
                outputStream.println("Message - Hello " + email);
                outputStream.flush();
            } else {
                outputStream.println("Message - This email is already existed");
                outputStream.flush();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("Throw Exception");
            e.printStackTrace();
        }
    }

    private void login(String email, String password) {
        try {
            boolean isUserExist = FileManager.isUserExist(email, password);
            if (isUserExist) {
                this.isClientAuthorized = true;
                this.clientEmail = email;
                outputStream.println("Message - Hello " + email);
                outputStream.flush();
            } else {
                outputStream.println("Message - The email or password is not correct");
                outputStream.flush();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("Throw Exception");
            e.printStackTrace();
        }
    }

    private void sendAnEmail() {
        try {
            outputStream.println("Input - RCPT TO");
            outputStream.flush();
            String email = inputStream.readLine();
            System.out.println(email);
            boolean isEmailExist = FileManager.isEmailExist(email);
            if (isEmailExist == true) {
                outputStream.println("Message - " + email + " ...Recipient ok");
                outputStream.flush();
                outputStream.println("Input - Enter the email body");
                outputStream.flush();
                String body = inputStream.readLine();
                FileManager.saveAnEmail(this.clientEmail, email, body);
                outputStream.println("Message - Email was send succesfully");
                System.out.println(body);
            } else {
                outputStream.println("Message - " + email + " ...Recipient is not exist");
                outputStream.flush();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("Throw Exception");
            e.printStackTrace();
        }
    }

    private void readEmails() {
        String inbox = FileManager.readEmails(this.clientEmail);
        outputStream.println("Message - Inbox for email " + this.clientEmail + "\n\n" + inbox);
        outputStream.flush();
    }
}
