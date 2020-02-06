/**
 * Ryan Valensa
 * Xavier Vogel
 */


import java.net.*;
import java.io.*;
import java.util.concurrent.TimeUnit;

public class PalindromeCheckerClient {
    private Socket clientSocket;
    private InputStream is;
    private InputStreamReader isr;
    private BufferedReader br;
    private DataOutputStream output;
    private BufferedReader in;

    //constructor that is called when a new client object is initalized.
    private PalindromeCheckerClient(int port, String address) {

        try {

            //starts the client socket with the IP address and port number given by
            //initalization
            clientSocket = new Socket(address, port);
            System.out.println("**CONNECTED**");

            //opens a new output stream to communicate with the server
            output = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException i) {

            System.out.println("**EXCEPTION: " + i);

        }

        String line = " ";


//----------------------------------------------------------------------------------------------------------------------
        //this loop keeps looping until the user inputs a string or hits "enter"
        while (!line.isEmpty()){

            //creates a new variable to grab keyboard input from the user
            in = new BufferedReader(new InputStreamReader(System.in));

            try
            {

                //this prompts the user to input some string and then grabs that string and puts it in
                //the variable 'line'. Finally the line variable is sent through the output stream
                //to the server
                System.out.print("Type something to see if it's a palindrome, or hit \n \"Enter\" to close connection: ");
                line = in.readLine();
                output.writeUTF(line);

                //if the user just its the enter key then break out of the while loop here
                if (line.isEmpty()) break;

                System.out.println();
                System.out.println("------------------------");
                System.out.println("**WAITING FOR RESPONSE**");
                System.out.println("------------------------");
                System.out.println();

                //this is a timer that waits 3 seconds before executing the next line of code. this is here
                //to make sure that the client has a response from the server BEFORE running the code.
                //If the next line of code is run before the server has time to respond we will crash and get an
                //exception
                TimeUnit.SECONDS.sleep(3);


                //this bit of code reads the servers response and prints it out on the screen for the
                //user to see
                is = clientSocket.getInputStream();
                isr = new InputStreamReader(is);
                br = new BufferedReader(isr);
                System.out.println(br.readLine());
                System.out.println("-------------------------------------------");
                System.out.println();


            }
            catch(IOException i)
            {
                System.out.println("**EXCEPTION**: " +i);
            } catch (InterruptedException e) {

                e.printStackTrace();
            }
        }

//----------------------------------------------------------------------------------------------------------------------

        //this bit of code happens then the user inputs a null string. This closes all
        //of the data streams and closes the socket connection
        try
        {
            System.out.println("**CLOSING CONNECTION**");
            in.close();
            output.close();
            clientSocket.close();
        }
        catch(IOException i)
        {
            System.out.println(i);
        }

    }


    //Main method that is called when the user runs the client. First it checks to see
    //if the user gives 2 command line arguments. If 2 command line arguments are given then
    //the user has given a port number and an IP address, and sets the variables accordingly.
    //Next, if the user has not given 2 command line arguments then the code checks if the
    //argument that they did give matches the regex "^[0-9]{4}". This regex stands for any 4
    //digits in a row. If this matches then the user has given a port number and that port number
    //is used. If not then the user has given an IP address and that iP address is used with port 1200
    public static void main (String[] args)
    {
        int portNum;
        String ipAdd;

        if (args.length == 2) {

            portNum = Integer.parseInt(args[0]);
            ipAdd = args[1];

        } else if (args[0].matches("^[0-9]{4}")) {

            portNum = Integer.parseInt(args[0]);
            ipAdd = "127.0.0.1";
        } else {

            ipAdd = args[0];
            portNum = 1200;
        }


        System.out.println(portNum + "   " + ipAdd);
        PalindromeCheckerClient client = new PalindromeCheckerClient(portNum, ipAdd);
    }

}
