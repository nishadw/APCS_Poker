
// 1. Open a socket.
// 2. Open an input stream and output stream to the socket.
// 3. Read from and write to the stream according to the server's protocol.
// 4. Close the streams.
// 5. Close the socket.

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * allows for the server to handle multiple clients at once, and connect the
 * different classes together for the game to work
 * @author Satvik Shreesha
 * @version 5/26
 */
public class ClientHandler
    implements Runnable
{
    /**
     * ArrayList of all the different clientHandlers that have been initiated
     */
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    /**
     * ArrayList of all the different Players that have been initiated
     */
    public static ArrayList<Player>        playerList     = new ArrayList<>();
    /**
     * The player nishad and opens all of its functionalities
     */
    public static Player                   nishad         = new Player("nishad");
    /**
     * The player chris and opens all of its functionalities
     */
    public static Player                   chris          = new Player("chris");
    /**
     * The player satvik and opens all of its functionalities
     */
    public static Player                   satvik         = new Player("satvik");
    /**
     * The player whos turn it is
     */
    public static Player                   currPlayer;
    private Socket                         socket;
    private BufferedReader                 bufferedReader;
    private BufferedWriter                 bufferedWriter;
    private String                         clientUsername;
    private Player                         player;

    /**
     * creates the client handler based on the socket the server passes
     * 
     * @param socket
     *            the portion of the ip address that the server is connecting to
     */
    public ClientHandler(Socket socket)
    {
        try
        {
            this.socket = socket;
            this.bufferedReader =
                new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter =
                new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            // setting our username and adding it to the list
            this.clientUsername = bufferedReader.readLine();
            clientHandlers.add(this);
            currPlayer = nishad;

            if (clientUsername.equals("nishad"))
            {
                playerList.add(nishad);
            }
            else if (clientUsername.equals("chris"))
            {
                playerList.add(chris);
            }
            else if (clientUsername.equals("satvik"))
            {
                playerList.add(satvik);
            }

            broadcastMessage("SERVER: " + clientUsername + " has entered the game!");

            if (playerList.size() == 3)
            {

                Server.create(nishad, chris, satvik);
            }

        }
        catch (IOException e)
        {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }


    /**
     * reads the thread and broadcasts it and avoids the blocking operation
     */
    @Override
    public void run()
    {
        String messageFromClient;
        while (socket.isConnected())
        {
            try
            {
                // Read what the client sent and then broadcast it to every
                // other client.
                messageFromClient = bufferedReader.readLine();
                broadcastMessage(messageFromClient);

            }
            catch (IOException e)
            {
                // Close everything gracefully.
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }


    /**
     * sets the currPlayer to the player whos turn is next and unlocks them
     */
    public static void getNext()
    {
        if (currPlayer == nishad)
        {
            currPlayer = chris;
        }
        else if (currPlayer == chris)
        {
            currPlayer = satvik;
        }
        else if (currPlayer == satvik)
        {
            currPlayer = nishad;
        }
        currPlayer.unlock();
    }


    /**
     * retrieves the playerList
     * 
     * @return the playerList
     */
    public static ArrayList<Player> getPlayerList()
    {
        return playerList;
    }


    /**
     * retrieves the Player nishad
     * 
     * @return nishad
     */
    public static Player getNishad()
    {
        return nishad;
    }


    /**
     * retrieves the Player chris
     * 
     * @return chris
     */
    public static Player getChris()
    {
        return chris;
    }


    /**
     * retrieves the player Satvik
     * 
     * @return satvik
     */
    public static Player getSatvik()
    {
        return satvik;
    }


    /**
     * retrieves the username of the ClientHandler
     * 
     * @return the username of the ClientHandler
     */
    public String getUsername()
    {
        return clientUsername;
    }


    /**
     * broadcasts a message to all the clientHandlers
     * 
     * @param messageToSend
     *            the message being broadcasted
     */
    public void broadcastMessage(String messageToSend)
    {
        for (ClientHandler clientHandler : clientHandlers)
        {
            try
            {
                clientHandler.bufferedWriter.write(messageToSend);
                clientHandler.bufferedWriter.newLine();
                clientHandler.bufferedWriter.flush();
            }
            catch (IOException e)
            {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }


    /**
     * removes the current clientHandler from the clientHandlers arraylist and
     * broadcasts that they have left
     */
    public void removeClientHandler()
    {
        clientHandlers.remove(this);
        playerList.remove(player);
        broadcastMessage("SERVER: " + clientUsername + " has left the game!");
    }


    /**
     * retrieves the clientHandler equivalent of the given player
     * 
     * @param p
     *            the player that it is getting the ClientHandler equivalent for
     * @return
     */
    public static ClientHandler getClientHandler(Player p)
    {
        String name = p.getName();

        for (ClientHandler c : clientHandlers)
        {
            if (c.getUsername().equals(name))
            {
                return c;
            }
        }

        return null;
    }


    /**
     * closes down this clientHandler efficiently
     * 
     * @param socket
     *            the current socket it is connected to
     * @param bufferedReader
     *            the tool that it is using to read the messages
     * @param bufferedWriter
     *            the tool that it is using to write the messages
     */
    public
        void
        closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter)
    {
        removeClientHandler();
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
}
