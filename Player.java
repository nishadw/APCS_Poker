import java.util.*;

public class Player
{

    private String                  name;
    private ArrayList<Card>         hand;
    private int                     money;
    private BettingWindow           bWindow;
    private static int              currentBet;
    private int                     myBet;
    private static int              prizeP;
    private boolean                 playing;
    private boolean                 isFirst;

    private boolean                 isLast;

    private PokerTable              table;
    private boolean                 locked;
    private boolean                 hasBetted;

    /**
     * list of players
     */
    public static ArrayList<Player> playerList = new ArrayList<>();

    /**
     * @param name
     *            player's name
     */
    public Player(String name)
    {
        this.name = name;
        hand = new ArrayList<Card>();
        money = 1000;
        currentBet = 0;
        myBet = 0;
        prizeP = 30;
        playing = true;
        hasBetted = false;
        // Whoever gets player nishad will always go first,
        // and so they will also be automatically unlocked
        // so that they may bet
        if (name.equals("nishad"))
        {
            isFirst = true;
            locked = false;
        }
        else
        {
            isFirst = false;
            locked = true;
        }
        if (name.equals("satvik"))
        {
            isLast = true;
        }
        else
        {
            isLast = false;
        }

    }


    /**
     * Brings the player back into the game after they fold the previous round
     */
    public void returnToGame()
    {
        playing = true;
    }


    /**
     * @param ptable
     *            the pokerTable this player has
     */
    public void setPokerTable(PokerTable ptable)
    {
        table = ptable;
    }


    /**
     * @return the pokerTable
     */
    public PokerTable getPokerTable()
    {
        return table;
    }


    /**
     * @return if the player is playing or not
     */
    public boolean isPlaying()
    {
        return playing;
    }


    /**
     * @return the player's hand
     */
    public ArrayList<Card> getHand()
    {
        return hand;
    }


    /**
     * @return the size of the player's hand
     */
    public int numCards()
    {
        return hand.size();
    }


    /**
     * @return the player's name
     */
    public String getName()
    {
        return name;
    }


    /**
     * @return how much money the player has
     */
    public double getMoney()
    {
        return money;
    }


    /**
     * @param lost
     *            amount of money lost
     * @return the updated amount of money
     */
    public double loseMoney(int lost)
    {
        money -= lost;
        return money;
    }


    /**
     * @param won
     *            amount of money won
     * @return the updated amount of money
     */
    public double winMoney(int won)
    {
        money += won;
        return money;
    }


    /**
     * @return if the player is broke
     */
    public boolean isBroke()
    {
        return money == 0;
    }


    /**
     * @return the bet that the player just put down
     */
    public int getMyBet()
    {
        return myBet;
    }


    /**
     * @param currentBet
     *            the currentBet, sent from the pokerTable
     */
    public void receiveCurrentBet(int currentBet)
    {
        this.currentBet = currentBet;
    }


    /**
     * @return the currentBet that the player is referring to
     */
    public int getCurrentBet()
    {
        return currentBet;
    }


    /**
     * this method is called after a winner is decided at the end of the round
     * so all the players may bet again at the start of the next round
     */
    public void reset()
    {
        hasBetted = false;
    }


    /**
     * @param card
     *            card to be checked
     * @return whether this player has that card or not
     */
    public boolean contains(Card card)
    {
        for (Card c : hand)
        {
            if (c.compareTo(card, true) == 0)
            {
                return true;
            }
        }
        return false;
    }


    /**
     * @param communityCards
     *            the communityCards on the table
     * @return the best hand that can be made with the player's hand and the
     *         communityCards
     */
    public ArrayList<Card> bestHand(ArrayList<Card> communityCards)
    {

        ComboComparator optimal = new ComboComparator(communityCards);
        return optimal.bestHand(hand);
    }


    /**
     * @param communityCards
     *            the communityCards on the table
     * @return the handRank of the player's bestHand; the number will be from
     *         the range 10-1 depending on how good the bestHand is
     */
    public int determineHand(ArrayList<Card> communityCards)
    {

        ComboComparator optimal = new ComboComparator(communityCards);
        return optimal.determineHand(bestHand(communityCards));
    }


    /**
     * Uses the number from type and converts it to an understandable string
     * (see comboComparator class to see what number corresponds to what type)
     */
    public String getHandType(int type, ArrayList<Card> communityCards)
    {

        ComboComparator optimal = new ComboComparator(communityCards);
        return optimal.getHandType(type);
    }


    /**
     * Opens this player's bettingWindow
     */
    public void openWindow()
    {
        bWindow = new BettingWindow(this);
    }


    /**
     * Closes this player's bettingWindow
     */
    public void close()
    {
        bWindow.dispose();
    }


    /**
     * Unlocks this player for action
     */
    public void unlock()
    {
        locked = false;
    }


    /**
     * Locks this player from action
     */
    public void lock()
    {
        locked = true;
    }


    /**
     * Resets hand to an empty list
     */
    public void discardHand()
    {
        hand = new ArrayList<Card>();
    }


    /**
     * Matches the currentBet
     */
    public void check()
    {
        if (hasBetted == true)
        {
            // Can't check if already betted
            bWindow.errorMessage5();
        }
        else if (money < currentBet)
        {
            // Can't check if not sufficient money
            bWindow.errorMessage2();
        }
        else if (locked || isFirst)
        {
            // Can't check if there is no currentBet
            // or if locked
            bWindow.errorMessage1();
        }
        else
        {
            // player loses appropriate amount of money
            loseMoney(currentBet);
            // prizeP gains appropriate amount of money
            addToPrizePool(currentBet);
            // opens the table that shows all 5
            // community cards for this player
            getPokerTable().subsequentTable(this);
            hasBetted = true;
            myBet = currentBet;
            // sends out the updated currentBet &
            // updated prizeP for the system to read
            // and communicate to other players
            ClientHandler.getClientHandler(this).broadcastMessage(name + ": Check- " + currentBet);
            close();
            ClientHandler.getNext();
            if (isLast)
            {
                table.determineWinner();
            }
        }

    }


    /**
     * @param r
     *            the amount of money the player is betting
     */
    public void raise(int r)
    {
        // Can't bet if locked
        if (locked && !isFirst)
        {
            bWindow.errorMessage1();
        }
        // Can't bet more money than you actually have
        else if (r > money)
        {
            bWindow.errorMessage2();
        }
        // (Special betting rule for first person)
        // First person must bet 30 or greater
        else if (isFirst && r < 30)
        {
            bWindow.errorMessage4();
        }
        // Can't bet if already betted
        else if (hasBetted == true)
        {
            bWindow.errorMessage5();
        }
        else
        {
            // updates currentBet to r if
            // r is greater than currentBet
            if (r > currentBet)
            {
                currentBet = r;
                loseMoney(r);
                addToPrizePool(r);
                getPokerTable().subsequentTable(this);
                hasBetted = true;
                myBet = r;
                ClientHandler.getClientHandler(this)
                    .broadcastMessage(name + ": Bet- " + currentBet);
                close();
                ClientHandler.getNext();
                if (isLast)
                {
                    table.determineWinner();
                }
            }
            // bet has to be at least greater than or equal to
            // 20 less than the currentBet to be legal
            else if (r >= currentBet - 20)
            {
                loseMoney(r);
                addToPrizePool(r);
                getPokerTable().subsequentTable(this);
                hasBetted = true;
                myBet = r;
                ClientHandler.getClientHandler(this)
                    .broadcastMessage(name + ": Bet- " + currentBet);
                close();
                ClientHandler.getNext();
                if (isLast)
                {
                    table.determineWinner();
                }
            }
            // if r is equal to the currentBet
            // then it is equivalent to a check() call
            else if (r == currentBet)
            {
                check();
            }
            // Error for an illegal bet
            else
            {
                bWindow.errorMessage3();
            }
        }
    }


    /**
     * Player loses a set amount of money
     */
    public void fold()
    {
        // Can't fold if locked
        if (locked && !isFirst)
        {
            bWindow.errorMessage1();
        }
        // Can't fold if already betted
        else if (hasBetted == true)
        {
            bWindow.errorMessage5();
        }
        else
        {
            // updates the playing boolean
            playing = false;
            // loses 10 dollars
            if (money >= 10)
            {
                loseMoney(10);
            }
            // loses the rest of their money
            // if they have less than 10
            else
            {
                money = 0;
            }
            getPokerTable().subsequentTable(this);
            hasBetted = true;
            // sets myBet to 0
            myBet = 0;
            ClientHandler.getClientHandler(this).broadcastMessage(name + ": Fold- " + currentBet);

            close();
            ClientHandler.getNext();
            if (isLast)
            {
                table.determineWinner();
            }
        }
    }


    /**
     * @param prize
     *            the amount of money in the current prizePool received from the
     *            pokerTable
     */
    public void receivePrizePool(int prize)
    {
        prizeP = prize;
    }


    /**
     * @param money
     *            the amount of money to add to the prizeP
     */
    public static void addToPrizePool(int money)
    {
        prizeP += money;
    }


    /**
     * @return the prizeP to which this player is referring to
     */
    public int getPrizePool()
    {
        return prizeP;
    }
}
