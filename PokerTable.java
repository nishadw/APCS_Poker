import java.util.*;
import java.awt.*;
import javax.swing.*;

/**
 * @version 5/23/2022
 * @author Nishad Wajge
 */
public class PokerTable
{

    private ArrayList<Picture> playerPokertables = new ArrayList<Picture>();
    private ArrayList<Player>  players;
    private Dealer             dealer;
    private ArrayList<Card>    communityCards;
    private Picture            monkey            = new Picture("LOGO.png");
    private Picture            back              = new Picture("back2.png");
    private int                currentBet;
    private int                prizePool;

    /**
     * @param p1
     *            the first player
     * @param p2
     *            the second player
     * @param p3
     *            the third player
     */
    public PokerTable(Player p1, Player p2, Player p3)
    {
        Picture temp = new Picture("pokerTable.jpg");
        temp.resize(3.0);

        dealer = new Dealer();

        players = new ArrayList<Player>();
        players.add(p1);
        players.add(p2);
        players.add(p3);

        // keep the adding of the cards here in case multiple players are
        // created but only a few "want" to play. But strategic to put in Player
        // class technically
        for (Player player : players)
        {
            dealer.addCardtoList(player.getHand(), player);
            dealer.addCardtoList(player.getHand(), player);
        }

        // adds a poker table for each player
        for (int i = 0; i < players.size(); i++)
        {
            playerPokertables.add(new Picture(temp, players.get(i)));
        }

        // community cards preset
        communityCards = new ArrayList<Card>();
        for (int i = 0; i < 5; i++)
        {
            dealer.addCardtoList(communityCards);
        }

        // monkey logo and back of card resized appropriately
        monkey.resize(0.2);
        back.resize(0.275);

        // sets the bet and prize pool
        currentBet = 0;
        prizePool = 30;
    }


    /**
     * @param player
     *            a player
     * @return returns its place in "line"
     */
    private int getID(Player player)
    {
        return players.indexOf(player);
    }


    /**
     * @return the ordered players
     */
    public ArrayList<Player> getPlayers()
    {
        return players;
    }


    /**
     * Given the player chosen, it will show only their cards and hide everyone
     * elses Logos and hidden cards only need to be changed once because the
     * actual picture is being changed, not a copy
     * 
     * @param player
     *            a given player
     */
    public void OpeningTable(Player player)
    {
        // MONKEY IMAGE
        for (int i = 0; i < monkey.width(); i++)
        {
            for (int j = 0; j < monkey.height() - 65; j++)
            {
                playerPokertables.get(getID(player)).set(i, j, new Color(monkey.getRGB(i, j)));
            }
        }

        // TOP CARDS
        for (int a = 0; a < 3; a++)
        {
            int bigSpace = a * (160 + 2 * back.width() + 40);
            int smallSpace = 40;

            // IF IT IS THAT PLAYER'S PLACE, THEN THE CARDS ARE SHOWN
            if (getID(player) == a)
            {
                ArrayList<Card> hand = player.getHand();
                Picture card1 = hand.get(0).getFront();
                Picture card2 = hand.get(1).getFront();

                for (int i = 0; i < card1.width() - 1; i++)
                {
                    for (int j = 0; j < card2.height() - 1; j++)
                    {

                        // LEFT SHOWN CARD
                        playerPokertables.get(getID(player))
                            .set(360 + i + bigSpace, 111 + j, new Color(card1.getRGB(i, j)));

                        // RIGHT SHOWN CARD
                        playerPokertables.get(getID(player)).set(
                            360 + i + back.width() + smallSpace + bigSpace,
                            111 + j,
                            new Color(card2.getRGB(i, j)));
                    }
                }
            }

            // THE OTHER'S HIDDEN CARDS
            else
            {
                for (int i = 3; i < back.width() - 1; i++)
                {
                    for (int j = 3; j < back.height() - 1; j++)
                    {

                        // LEFT HIDDEN CARD
                        playerPokertables.get(getID(player))
                            .set(360 + i + bigSpace, 111 + j, new Color(back.getRGB(i, j)));

                        // RIGHT HIDDEN CARD
                        playerPokertables.get(getID(player)).set(
                            360 + i + back.width() + smallSpace + bigSpace,
                            111 + j,
                            new Color(back.getRGB(i, j)));

                    }
                }
            }
        }

        // BOTTOM "SLIDING" CARDS
        for (int numOfCards = 0; numOfCards < 28; numOfCards++)
        {
            for (int i = 3; i < back.width() - 1; i++)
            {
                for (int j = 3; j < back.height() - 1; j++)
                {
                    playerPokertables.get(getID(player))
                        .set(590 + i + numOfCards * 20, 579 + j, new Color(back.getRGB(i, j)));
                }
            }
        }

        // TABLE IS SHOWN
        playerPokertables.get(getID(player)).show();
    }


    /**
     * shows the community cards and thrown away cards
     * 
     * @param player
     *            the given player
     */
    public void subsequentTable(Player player)
    {

        // COMMUNITY CARDS FROM [1 TO 5]
        // NUM LIMIT 5 IS PRESET BECUASE OF FLASHPOKR RULES
        for (int num = 0; num < 5; num++)
        {
            Picture card = communityCards.get(num).getFront();
            int bigSpace = num * (card.width() + 40);

            for (int i = 0; i < card.width(); i++)
            {
                for (int j = 0; j < card.height(); j++)
                {
                    // LEFT SHOWN CARD
                    playerPokertables.get(getID(player))
                        .set(538 + i + bigSpace, 345 + j, new Color(card.getRGB(i, j)));

                }
            }
        }

        for (int trashed = 0; trashed < 5; trashed++)
        {
            for (int i = 0; i < back.width(); i++)
            {
                for (int j = 0; j < back.height(); j++)
                {
                    // HIDDEN CARDS ON THE RIGHT OF COMMUNITY CARDS
                    playerPokertables.get(getID(player)).set(
                        998 + i + back.width() + trashed * 20,
                        345 + j,
                        new Color(back.getRGB(i, j)));

                }
            }
        }

        // TABLE IS SHOWN
        playerPokertables.get(getID(player)).show();
    }


    private void close()
    {
        for (Player p : players)
        {
            playerPokertables.get(getID(p)).close();
        }
    }


    /**
     * Determines the winner and allocates money
     * 
     * @param play
     *            the given player
     */
    public void determineWinner()
    {

        // Suit Hierarchy Ascending club diamond heart spade
        ArrayList<String> suit = new ArrayList<String>();
        suit.add("Clubs");
        suit.add("Diamonds");
        suit.add("Hearts");
        suit.add("Spades");

        // checks to see the best player, their hand, and what type it is
        ArrayList<Card> finalHand = new ArrayList<Card>();
        int finalHandType = 0;
        String finalPlayerName = "";

        for (Player p : players)
        {
            int bestTypeForPlayer = 0;

            if (p.isPlaying())
            {
                ArrayList<Card> bestListForPlayer = new ArrayList<Card>();
                bestListForPlayer = p.bestHand(communityCards);
                bestTypeForPlayer = p.determineHand(communityCards);

                if (bestTypeForPlayer > finalHandType)
                {
                    finalHandType = bestTypeForPlayer;
                    finalHand = new ArrayList<Card>(bestListForPlayer);
                    finalPlayerName = p.getName();
                }
                else if (bestTypeForPlayer == finalHandType)
                {

                    // Royal Flush (Ace to 10 && Same Suit)
                    if (finalHandType == 10)
                    {
                        int suitIncumbant = suit.indexOf(finalHand.get(0).getSuit());
                        int suitNew = suit.indexOf(bestListForPlayer.get(0).getSuit());

                        if (suitNew > suitIncumbant)
                        {
                            finalHand = new ArrayList<Card>(bestListForPlayer);
                            finalHandType = bestTypeForPlayer;
                            finalPlayerName = p.getName();
                        }

                        // For a tie
                        else if (suitNew == suitIncumbant)
                        {
                            finalPlayerName += " & " + p.getName();
                        }
                    }

                    // Straight Flush (Any 5 in order && Same Suit)
                    else if (finalHandType == 9)
                    {
                        if (order(bestListForPlayer)[bestListForPlayer.size()
                            - 1] > order(finalHand)[finalHand.size() - 1])
                        {
                            finalHand = new ArrayList<Card>(bestListForPlayer);
                            finalHandType = bestTypeForPlayer;
                            finalPlayerName = p.getName();
                        }

                        // For a tie
                        else if (order(bestListForPlayer)[bestListForPlayer.size()
                            - 1] == order(finalHand)[finalHand.size() - 1])
                        {
                            int i = bestListForPlayer.size() - 1;

                            while (bestListForPlayer.get(i).getValue() == finalHand.get(i)
                                .getValue())
                            {
                                i--;
                            }

                            if (bestListForPlayer.get(i).getValue() > finalHand.get(i).getValue())
                            {
                                finalHand = new ArrayList<Card>(bestListForPlayer);
                                finalHandType = bestTypeForPlayer;
                                finalPlayerName = p.getName();
                            }

                            // Use all the community cards
                            else if (bestListForPlayer.get(i).getValue() == finalHand.get(i)
                                .getValue())
                            {
                                finalPlayerName += " & " + p.getName();
                            }
                        }
                    }

                    // Four of a Kind (Any 4 && Same Value)
                    else if (finalHandType == 8)
                    {
                        if (order(bestListForPlayer)[bestListForPlayer.size()
                            - 2] > order(finalHand)[finalHand.size() - 2])
                        {
                            finalHand = new ArrayList<Card>(bestListForPlayer);
                            finalHandType = bestTypeForPlayer;
                            finalPlayerName = p.getName();
                        }

                        // For a tie
                        else if (order(bestListForPlayer)[bestListForPlayer.size()
                            - 2] == order(finalHand)[finalHand.size() - 2])
                        {
                            int i = bestListForPlayer.size() - 1;

                            while (bestListForPlayer.get(i).getValue() == finalHand.get(i)
                                .getValue())
                            {
                                i--;
                            }

                            if (bestListForPlayer.get(i).getValue() > finalHand.get(i).getValue())
                            {
                                finalHand = new ArrayList<Card>(bestListForPlayer);
                                finalHandType = bestTypeForPlayer;
                                finalPlayerName = p.getName();
                            }

                            // Use all the community cards
                            else if (bestListForPlayer.get(i).getValue() == finalHand.get(i)
                                .getValue())
                            {
                                finalPlayerName += " & " + p.getName();
                            }
                        }
                    }

                    // Full House (3 Cards Same Value && 2 Other Cards Same
                    // Value)
                    else if (finalHandType == 7)
                    {
                        if (order(bestListForPlayer)[bestListForPlayer.size()
                            - 3] > order(finalHand)[finalHand.size() - 3])
                        {
                            finalHand = new ArrayList<Card>(bestListForPlayer);
                            finalHandType = bestTypeForPlayer;
                            finalPlayerName = p.getName();
                        }

                        // For a tie
                        else if (order(bestListForPlayer)[bestListForPlayer.size()
                            - 3] == order(finalHand)[finalHand.size() - 3])
                        {
                            int i = bestListForPlayer.size() - 1;

                            while (bestListForPlayer.get(i).getValue() == finalHand.get(i)
                                .getValue())
                            {
                                i--;
                            }

                            if (bestListForPlayer.get(i).getValue() > finalHand.get(i).getValue())
                            {
                                finalHand = new ArrayList<Card>(bestListForPlayer);
                                finalHandType = bestTypeForPlayer;
                                finalPlayerName = p.getName();
                            }

                            // Use all the community cards
                            else if (bestListForPlayer.get(i).getValue() == finalHand.get(i)
                                .getValue())
                            {
                                finalPlayerName += " & " + p.getName();
                            }
                        }
                    }

                    // Flush (Same Suit)
                    else if (finalHandType == 6)
                    {
                        if (order(bestListForPlayer)[bestListForPlayer.size()
                            - 1] > order(finalHand)[finalHand.size() - 1])
                        {
                            finalHand = new ArrayList<Card>(bestListForPlayer);
                            finalHandType = bestTypeForPlayer;
                            finalPlayerName = p.getName();
                        }

                        // For a tie
                        else if (order(bestListForPlayer)[bestListForPlayer.size()
                            - 1] == order(finalHand)[finalHand.size() - 1])
                        {
                            int i = bestListForPlayer.size() - 1;

                            while (bestListForPlayer.get(i).getValue() == finalHand.get(i)
                                .getValue())
                            {
                                i--;
                            }

                            if (bestListForPlayer.get(i).getValue() > finalHand.get(i).getValue())
                            {
                                finalHand = new ArrayList<Card>(bestListForPlayer);
                                finalHandType = bestTypeForPlayer;
                                finalPlayerName = p.getName();
                            }

                            // Use all the community cards
                            else if (bestListForPlayer.get(i).getValue() == finalHand.get(i)
                                .getValue())
                            {
                                finalPlayerName += " & " + p.getName();
                            }
                        }
                    }

                    // Straight (Any 5 in order)
                    else if (finalHandType == 5)
                    {
                        if (order(bestListForPlayer)[bestListForPlayer.size()
                            - 1] > order(finalHand)[finalHand.size() - 1])
                        {
                            finalHand = new ArrayList<Card>(bestListForPlayer);
                            finalHandType = bestTypeForPlayer;
                            finalPlayerName = p.getName();
                        }

                        // For a tie
                        else if (order(bestListForPlayer)[bestListForPlayer.size()
                            - 1] == order(finalHand)[finalHand.size() - 1])
                        {
                            int i = bestListForPlayer.size() - 1;

                            while (bestListForPlayer.get(i).getValue() == finalHand.get(i)
                                .getValue())
                            {
                                i--;
                            }

                            if (bestListForPlayer.get(i).getValue() > finalHand.get(i).getValue())
                            {
                                finalHand = new ArrayList<Card>(bestListForPlayer);
                                finalHandType = bestTypeForPlayer;
                                finalPlayerName = p.getName();
                            }

                            // Use all the community cards
                            else if (bestListForPlayer.get(i).getValue() == finalHand.get(i)
                                .getValue())
                            {
                                finalPlayerName += " & " + p.getName();
                            }
                        }
                    }

                    // Three of a Kind (3 Cards Same Value)
                    else if (finalHandType == 4)
                    {
                        if (order(bestListForPlayer)[bestListForPlayer.size()
                            - 3] > order(finalHand)[finalHand.size() - 3])
                        {
                            finalHand = new ArrayList<Card>(bestListForPlayer);
                            finalHandType = bestTypeForPlayer;
                            finalPlayerName = p.getName();
                        }

                        // For a tie
                        else if (order(bestListForPlayer)[bestListForPlayer.size()
                            - 3] == order(finalHand)[finalHand.size() - 3])
                        {
                            int i = bestListForPlayer.size() - 1;

                            while (bestListForPlayer.get(i).getValue() == finalHand.get(i)
                                .getValue())
                            {
                                i--;
                            }

                            if (bestListForPlayer.get(i).getValue() > finalHand.get(i).getValue())
                            {
                                finalHand = new ArrayList<Card>(bestListForPlayer);
                                finalHandType = bestTypeForPlayer;
                                finalPlayerName = p.getName();
                            }

                            // Use all the community cards
                            else if (bestListForPlayer.get(i).getValue() == finalHand.get(i)
                                .getValue())
                            {
                                finalPlayerName += " & " + p.getName();
                            }
                        }
                    }

                    // Two Pair (2 Cards Same Value && 2 Other Cards Same Value)
                    else if (finalHandType == 3)
                    {
                        int valueIncumbant = 0;

                        for (int i = 0; i < 5; i++)
                        {
                            for (int j = i + 1; j < 5; j++)
                            {
                                if (finalHand.get(i).getValue() == finalHand.get(j).getValue()
                                    && finalHand.get(i).getValue() > valueIncumbant)
                                {
                                    valueIncumbant = finalHand.get(i).getValue();
                                }
                            }
                        }

                        int valueNew = 0;
                        for (int i = 0; i < 5; i++)
                        {
                            for (int j = i + 1; j < 5; j++)
                            {
                                if (bestListForPlayer.get(i).getValue() == bestListForPlayer.get(j)
                                    .getValue() && bestListForPlayer.get(i).getValue() > valueNew)
                                {
                                    valueNew = bestListForPlayer.get(i).getValue();
                                }
                            }
                        }

                        if (valueNew > valueIncumbant)
                        {
                            finalHand = new ArrayList<Card>(bestListForPlayer);
                            finalHandType = bestTypeForPlayer;
                            finalPlayerName = p.getName();
                        }

                        else if (valueNew == valueIncumbant)
                        {
                            int i = finalHand.size() - 1;

                            while (order(finalHand)[i] == order(bestListForPlayer)[i])
                            {
                                i--;
                            }

                            if (order(bestListForPlayer)[i] > order(finalHand)[i])
                            {
                                finalHand = new ArrayList<Card>(bestListForPlayer);
                                finalHandType = bestTypeForPlayer;
                                finalPlayerName = p.getName();
                            }

                            else if (order(bestListForPlayer)[i] == order(finalHand)[i])
                            {
                                finalPlayerName += " & " + p.getName();
                            }
                        }
                    }

                    // One Pair (2 Cards Same Value)
                    else if (finalHandType == 2)
                    {
                        int valueIncumbant = 0;

                        for (int i = 0; i < 5; i++)
                        {
                            for (int j = i + 1; j < 5; j++)
                            {
                                if (finalHand.get(i).getValue() == finalHand.get(j).getValue())
                                {
                                    valueIncumbant = finalHand.get(i).getValue();
                                }
                            }
                        }

                        int valueNew = 0;
                        for (int i = 0; i < 5; i++)
                        {
                            for (int j = i + 1; j < 5; j++)
                            {
                                if (bestListForPlayer.get(i).getValue() == bestListForPlayer.get(j)
                                    .getValue())
                                {
                                    valueNew = bestListForPlayer.get(i).getValue();
                                }
                            }
                        }

                        if (valueNew > valueIncumbant)
                        {
                            finalHand = new ArrayList<Card>(bestListForPlayer);
                            finalHandType = bestTypeForPlayer;
                            finalPlayerName = p.getName();
                        }

                        else if (valueNew == valueIncumbant)
                        {
                            int i = finalHand.size() - 1;

                            while (order(finalHand)[i] == order(bestListForPlayer)[i])
                            {
                                i--;
                            }

                            if (order(bestListForPlayer)[i] > order(finalHand)[i])
                            {
                                finalHand = new ArrayList<Card>(bestListForPlayer);
                                finalHandType = bestTypeForPlayer;
                                finalPlayerName = p.getName();
                            }

                            else if (order(bestListForPlayer)[i] == order(finalHand)[i])
                            {
                                finalPlayerName += " & " + p.getName();
                            }
                        }
                    }

                    // High Card
                    else if (finalHandType == 1)
                    {
                        int i = finalHand.size() - 1;

                        while (order(finalHand)[i] == order(bestListForPlayer)[i])
                        {
                            i--;
                        }

                        if (order(bestListForPlayer)[i] > order(finalHand)[i])
                        {
                            finalHand = new ArrayList<Card>(bestListForPlayer);
                            finalHandType = bestTypeForPlayer;
                            finalPlayerName = p.getName();
                        }

                        else if (order(bestListForPlayer)[i] == order(finalHand)[i])
                        {
                            finalPlayerName += " & " + p.getName();
                        }
                    }

                }

            }

            ClientHandler temp = ClientHandler.getClientHandler(p);
            if (bestTypeForPlayer == 0)
            {
                temp.broadcastMessage("Player " + p.getName() + " has folded");
            }
            else
            {
                temp.broadcastMessage(
                    "Player " + p.getName() + "'s handtype is "
                        + ComboComparator.getHandType(bestTypeForPlayer));
            }
        }

        String hand = "";
        for (Card card : finalHand)
        {
            String type = "";
            if (card.getValue() == 14)
            {
                type = "Ace";
            }
            else if (card.getValue() == 13)
            {
                type = "King";
            }
            else if (card.getValue() == 12)
            {
                type = "Queen";
            }
            else if (card.getValue() == 11)
            {
                type = "Jack";
            }
            else
            {
                type = "" + card.getValue();
            }
            hand += type + " of " + card.getSuit() + ", ";
        }

        int amountPlaying = 0;
        for (Player p : players)
        {
            if (p.isPlaying())
            {
                amountPlaying++;
            }
        }
        if (amountPlaying > 1)
        {
            String msg = "Player " + finalPlayerName + " won with a "
                + ComboComparator.getHandType(finalHandType) + "\n and a hand of "
                + hand.substring(0, hand.length() - 2);

            JOptionPane.showMessageDialog(
                new Picture("LOGO.png"),
                msg,
                "There's a winner!",
                JOptionPane.INFORMATION_MESSAGE);

            // Money allocated to winner
            for (Player p : players)
            {
                if (p.getName().equals(finalPlayerName))
                {
                    p.winMoney(p.getPrizePool());
                }
            }
        }
        else if (amountPlaying == 1)
        {
            String name = "";
            for (Player p : players)
            {
                if (p.isPlaying() == true)
                {
                    name = p.getName();
                    p.winMoney(30 + p.getPrizePool()); // net positive 60
                                                       // dollars to the guy
                                                       // whos still in the game
                }
            }
            String msg = "Everyone folded except " + name + ", so " + name + " wins 60 dollars";
            JOptionPane.showMessageDialog(
                new Picture("LOGO.png"),
                msg,
                "Everyone folded except " + name + "!",
                JOptionPane.INFORMATION_MESSAGE);
        }
        else
        {
            for (Player p : players)
            {
                if (p.getMoney() >= 10)
                {
                    p.loseMoney(10);
                }
                else
                {
                    p.loseMoney((int)p.getMoney());
                }
            }
            String msg =
                "Everyone folded so EVERYONE loses 20 dollars (those with less than or equal to 20 dollars are now broke)";
            JOptionPane.showMessageDialog(
                new Picture("LOGO.png"),
                msg,
                "Everyone folded!",
                JOptionPane.INFORMATION_MESSAGE);
        }

        prizePool = 30;
        currentBet = 0;
        boolean gameWon = false;

        for (Player p : players)
        {
            // resets game
            p.receivePrizePool(prizePool);
            p.receiveCurrentBet(currentBet);
            p.reset();
            p.returnToGame();

            if (p.getName().equals("nishad"))
            {
                p.unlock();
            }
            else
            {
                p.lock();
            }

            if (p.getMoney() >= 2300 || p.getMoney() == 0)
            {
                gameWon = true;
            }
        }
        if (gameWon)
        {
            String msg = "The game is over! Go bully the losers or something \n Player "
                + finalPlayerName + " has won!";
            JOptionPane.showMessageDialog(
                new Picture("LOGO.png"),
                msg,
                "Game's over!",
                JOptionPane.INFORMATION_MESSAGE);

            ClientHandler.clientHandlers.get(0).broadcastMessage("Game over");
            ClientHandler.clientHandlers.get(0).broadcastMessage("New game has begun!");
            /*
             * for (ClientHandler c : ClientHandler.clientHandlers.get(0)) {
             * c.broadcastMessage("Game over"); } ClientHandler temp =
             * ClientHandler.getClientHandler(ClientHandler.getChris());
             */

            for (Player p : players)
            {
                p.loseMoney((int)p.getMoney());
                p.winMoney(1000);
            }
        }
        else
        {
            String msg = "Next round begins!";
            JOptionPane.showMessageDialog(
                new Picture("LOGO.png"),
                msg,
                "New round",
                JOptionPane.INFORMATION_MESSAGE);

            ClientHandler.clientHandlers.get(0).broadcastMessage("New round");

            /*
             * for (ClientHandler c : ClientHandler.clientHandlers) {
             * c.broadcastMessage(msg); }
             */
        }

        close();
        for (Player p : players)
        {
            p.discardHand();
        }
        Server.create(players.get(0), players.get(1), players.get(2));

    }


    // BUNCH OF HELPER METHODS
    /**
     * helper method
     * 
     * @param hand
     *            hand of 5
     * @return finds highest card
     */
    private int[] order(ArrayList<Card> hand)
    {
        int[] values = new int[5];
        int i = 0;
        for (Card c : hand)
        {
            values[i] = c.getValue();
            if (i + 1 < 5)
            {
                i++;
            }
        }
        Arrays.sort(values);
        return values;
    }
}
