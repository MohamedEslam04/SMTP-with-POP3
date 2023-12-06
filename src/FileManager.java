import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

// FileManager class for handling file operations
public class FileManager {

    final static String rootFolder = "content";
    final static String credentialsFile = "credentials.txt";

    // Creates the credentials file and root folder if they don't exist
    public static void createCredentialsFile() {
        try {
            File theDir = new File(rootFolder);
            if (!theDir.exists()) {
                theDir.mkdirs();
            }

            File myObj = new File(rootFolder + "/" + credentialsFile);
            myObj.createNewFile();
        } catch (Exception e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    // Creates a folder for a user to store their emails (inbox)
    public static void createServerFolders(String folderName) {
        try {
            File theDir = new File(rootFolder + "/" + folderName + "/" + "inbox");
            if (!theDir.exists()) {
                theDir.mkdirs();
            }
        } catch (Exception e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    // Appends the provided email and password to the credentials file
    public static void saveCredintials(String email, String password) {
        try {
            File file = new File(rootFolder + "/" + credentialsFile);
            FileWriter fr = new FileWriter(file, true);
            BufferedWriter br = new BufferedWriter(fr);
            PrintWriter pr = new PrintWriter(br);
            pr.println(email + " " + password);
            pr.close();
            br.close();
            fr.close();
            System.out.println("Successfully wrote to the file.");
        } catch (Exception e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    // Checks if a user with the given email and password exists in the credentials file
    public static boolean isUserExist(String email, String password) {
        try {
            File myObj = new File(rootFolder + "/" + credentialsFile);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] splitedData = data.split("\\s+");
                String savedEmail = splitedData[0];
                String savedPassword = splitedData[1];
                if (savedEmail.equals(email) && savedPassword.equals(password)) {
                    myReader.close();
                    return true;
                }
            }
            myReader.close();
        } catch (Exception e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return false;
    }

    // ... (Rest of the methods with comments)
}
