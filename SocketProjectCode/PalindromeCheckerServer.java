/**
 * Ryan Valensa
 *
 */



import java.net.*;
import java.io.*;

public class PalindromeCheckerServer {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private DataInputStream input;
    private PrintWriter output;

    //constructor
    public PalindromeCheckerServer (int port) {

        //starting the server with the port number given from the initalization
        try {

            serverSocket = new ServerSocket(port);
            System.out.println("**SERVER STARTED**");

            System.out.println("***WAITING FOR CLIENT***");

            //server accepts the client when client runs with the same port number on IP that the server is running on.
            clientSocket = serverSocket.accept();
            System.out.println("**CLIENT ACCEPTED**");

            //creates a new variable to grab input from the client stream.
            input = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));

            String line = " ";

//----------------------------------------------------------------------------------------------------------------------
            //this loop is where everything happens. It keeps looping until the client user inputs a null
            //string or hits "enter"
            while (!line.isEmpty()) {

                //opens a new output stream to send data back to the client
                output = new PrintWriter(clientSocket.getOutputStream());

                try {

                    //this reads the input from the client into the variable called line
                    line = input.readUTF();
                    //if the user just hits enter then break out of the while loop and close all connections
                    if (line.isEmpty()) break;

                    //calls the isPalindrome method on the line variable and ends the correct
                    //string back to the client to tell the user whether or not the string they sent
                    //the server is a palindrome or not
                    if (isPalindrome(line)) {

                        output.println("The String you entered IS a palindrome.");
                        output.flush();
                        System.out.println();

                    } else {
                        output.println("The String you entered is NOT a palindrome.");
                        output.flush();
                        System.out.println();
                    }
                } catch (IOException i) {
                    System.out.println("**EXCEPTION: " + i);
                }
            }
//----------------------------------------------------------------------------------------------------------------------

            //This section happens after the user hits "enter"
            System.out.println("**CLOSING CONNECTION**");

            // close connection
            clientSocket.close();
            input.close();


        } catch (IOException i) {
            System.out.println("**EXCEPTION: " + i);
        }
    }

    //method to determine if a string given by the client is a palindrome or not
    private boolean isPalindrome(String input) {

        boolean retValue;
        String cleanInput;
        String s1;
        String s2;

        //these two statements clean the string of all commas, spaces, dashes, periods, and
        //makes all characters int eh string lowercase
        cleanInput = input.replaceAll("[\\s\\-,.]", "");
        cleanInput = cleanInput.toLowerCase();

         //This statement determines if the string length is even or odd as different actions are needed
         //If the string length is even the 'if' branch is taken and the input is broken in half into 2
         //strings s1 and s2. s1 is the first half of the string and s2 is the second half of the string
         //If the input is an odd length then the else branch is taken. This branch does almost the same thing
         //except it gets rid of the middle character as that character is not needed.
         if (cleanInput.length() % 2 == 0) {
            s1 = cleanInput.substring(0, (cleanInput.length()/2));
            s2 = cleanInput.substring(cleanInput.length()/2);

         } else {

             s1 = cleanInput.substring(0, (cleanInput.length()/2));
             s2 = cleanInput.substring((cleanInput.length()/2) + 1);
         }

         //this reverses s2
         String s2Rev = new StringBuilder(s2).reverse().toString();

         //now that s2 is reversed we can compare it to s1. If s1 and s2Rev are exactly the same,
         //this means that the input is a palindrome. If the two strings are not the same then
         //the user did not send a palindrome.
         if (s1.equals(s2Rev)) retValue = true;
         else retValue = false;

        return retValue;

    }

    //This is the main method that is called when the server is run. if the user gives a
    //port number as a command line argument then that port number is used in the initalization
    //of the server. If the user does not give a port number then the port 1200 is used by default.
    public static void main(String[] args)
    {
        int portNum = 1200;

        if (args.length != 0) {

            portNum = Integer.parseInt(args[0]);
        }


        System.out.println(portNum);
        PalindromeCheckerServer server = new PalindromeCheckerServer(portNum);
    }

}
