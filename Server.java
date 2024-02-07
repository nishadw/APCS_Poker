import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * creates the server that all the players can connect to and starts up
 * pokertables
 * 
 * @author Satvik Shreesha
 * @version 5/26
 */
public class Server
{

    private final ServerSocket serverSocket;

    /**
     * initializes the serverSocket variable and alllows for the players to
     * later connect to it
     * 
     * @param serverSocket
     *            is the socket that is being connected to
     */
    public Server(ServerSocket serverSocket)
    {
        this.serverSocket = serverSocket;
    }


    /**
     * starts up the server and waits for a player to join, then creates a
     * clientHandler and starts up the thread
     */
    public void startServer()
    {
        try
        {
            while (!serverSocket.isClosed())
            {
                // Will be closed in the Client Handler.
                Socket socket = serverSocket.accept();
                System.out.println("A new player has joined!");
                ClientHandler clientHandler = new ClientHandler(socket);
                Thread thread = new Thread(clientHandler);

                thread.start();
            }
        }
        catch (IOException e)
        {
            closeServerSocket();
        }
    }


    /**
     * creates the poker table (GUI and functionalities) and allows the actual
     * game to be played on
     * 
     * @param p1
     *            the first player that is going (based on turn)
     * @param p2
     *            the second player that is going
     * @param p3
     *            the third player that is going
     */
    public static void create(Player p1, Player p2, Player p3)
    {
        PokerTable table = new PokerTable(p1, p2, p3);

        p1.setPokerTable(table);
        p2.setPokerTable(table);
        p3.setPokerTable(table);

        table.OpeningTable(p1);
        table.OpeningTable(p2);
        table.OpeningTable(p3);
        p1.openWindow();
    }


    /**
     * closes the serverSocket nicely and easily
     */
    public void closeServerSocket()
    {
        try
        {
            if (serverSocket != null)
            {
                serverSocket.close();
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
        ServerSocket serverSocket = new ServerSocket(80);
        Server server = new Server(serverSocket);
        server.startServer();

    }

}
