import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Allows for players to type and receive messages from the other players
 * 
 * @author Satvik Shreesha
 * @version 5/26
 */
public class Client
{

    // A client has a socket to connect to the server and a reader and writer to
    // receive and send messages respectively.
    private Socket         socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String         username;

    /**
     * d sets up the field variables and allows for the player to type and
     * receive messages later on
     * 
     * @param socket
     *            the section of the ip address the player is connecting to
     * @param username
     *            the username of that player --> leads to a connection with
     *            ClientHandler and player classes
     */
    public Client(Socket socket, String username)
    {
        try
        {
            this.socket = socket;
            this.username = username;
            this.bufferedReader =
                new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter =
                new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        }
        catch (IOException e)
        {

            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }


    /**
     * actively running and types in messages to the thread through the
     * bufferedWriter
     */
    public void sendMessage()
    {
        try
        {
            // Initially send the username of the client.
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner scanner = new Scanner(System.in);

            // will keep running and sending whatever messages are typed

        }
        catch (IOException e)
        {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }


    // Listening for a message is blocking so need a separate thread for that.
    /**
     * actively running and listens to messages in the thread through the
     * bufferedReader
     */
    public void listenForMessage()
    {
        new Thread(new Runnable() {
            @Override
            public void run()
            {
                String msgFromGroupChat;
                // While there is still a connection with the server, continue
                // to listen for messages on a separate thread.
                while (socket.isConnected())
                {
                    try
                    {
                        // Get the messages sent from other users and print it
                        // to the console.
                        msgFromGroupChat = bufferedReader.readLine();

                        System.out.println(msgFromGroupChat);

                    }
                    catch (IOException e)
                    {
                        // Close everything gracefully.
                        closeEverything(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
        }).start();
    }


    /**
     * efficiently closes down all the aspects of the client
     * 
     * @param socket
     *            the socket that the client is connected to but will disconnect
     *            from
     * @param bufferedReader
     *            the tool that the client uses to read the messages --> will be
     *            closed down
     * @param bufferedWriter
     *            the tool that the client uses to write the messages -> will be
     *            closed down
     */
    public
        void
        closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter)
    {
        try
        {
            if (bufferedReader != null)
            {
                bufferedReader.close();
            }
            if (bufferedWriter != null)
            {
                bufferedWriter.close();
            }
            if (socket != null)
            {
                socket.close();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    // Run the program.
    public static void main(String[] args)
        throws IOException
    {

        // Get a username for the user and a socket connection.
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your username (nishad, chris, or satvik) for the game: ");
        String username = scanner.nextLine();
        // Create a socket to connect to the server.
        Socket socket = new Socket("localhost", 80);

        // Pass the socket and give the client a username.
        Client client = new Client(socket, username);
        // Infinite loop to read and send messages.

        client.listenForMessage();
        client.sendMessage();
    }
}
