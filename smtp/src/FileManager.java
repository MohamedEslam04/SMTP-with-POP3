
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class FileManager {

    final static String rootFolder = "content";
    final static String credentialsFile = "credentials.txt";

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
                System.out.println(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isEmailExist(String email) {
        try {
            File myObj = new File(rootFolder + "/" + credentialsFile);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] splitedData = data.split("\\s+");
                String savedEmail = splitedData[0];
                if (savedEmail.equals(email)) {
                    myReader.close();
                    return true;
                }
                System.out.println(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return false;
    }

    public static void saveAnEmail(String from, String rcpt, String body) {
        try {
            File file = new File(rootFolder + "/" + rcpt + "/" + "inbox/inbox.txt");
            FileWriter fr = new FileWriter(file, true);
            BufferedWriter br = new BufferedWriter(fr);
            PrintWriter pr = new PrintWriter(br);
            pr.println("From: " + from + "\n" + "Email body: " + body + "\n\n");
            pr.close();
            br.close();
            fr.close();
            System.out.println("Successfully wrote to the file.");
        } catch (Exception e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static String readEmails(String email) {
        try {
            String path = rootFolder + "/" + email + "/" + "inbox/inbox.txt";
            File file = new File(path);
            String content;
            Scanner scanner = new Scanner(file, String.valueOf(StandardCharsets.UTF_8));
            content = scanner.useDelimiter("\\A").next();
            return content;
        } catch (Exception e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return "";
    }

}
