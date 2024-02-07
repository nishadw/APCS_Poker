import java.util.*;

/**
 * @author Nishad Wajge & Chris Zhang
 * @version 5/23/2022
 */
public class ComboComparator
{

    private ArrayList<Card>                       dealerHand;
    private int                                   handRank;
    private static final HashMap<Integer, String> HANDTYPE = new HashMap<Integer, String>() {
                                                               {
                                                                   put(1, "High Card");
                                                                   put(2, "One Pair");
                                                                   put(3, "Two Pair");
                                                                   put(4, "Three of a Kind");
                                                                   put(5, "Straight");
                                                                   put(6, "Flush");
                                                                   put(7, "Full House");
                                                                   put(8, "Four of a Kind");
                                                                   put(9, "Straight Flush");
                                                                   put(10, "Royal Flush");
                                                               }
                                                           };

    /**
     * @param hand
     *            the hand of the dealer (min 3 | max 5)
     */
    public ComboComparator(ArrayList<Card> hand)
    {
        dealerHand = hand;
        handRank = 0;
    }


    /**
     * @return gets the hand rank
     */
    public int getHandRank()
    {
        if (handRank == 0)
        {
            throw new IllegalCallerException("Need to run bestHand first");
        }

        return handRank;
    }


    /**
     * @param handRank
     *            the rank of the hand
     * @return the corresponding name [check HANDTYPE]
     */
    public static String getHandType(int handRank)
    {
        if (handRank < 1 || handRank > 10)
        {
            throw new IllegalArgumentException("Invalid rank (Input Parameter should be [1, 10])");
        }

        return HANDTYPE.get(handRank);
    }


    /**
     * @param playerHand
     *            is the player's hand, which is of length 2
     * @return the best combo of the dealer's hand and the player's hand
     */
    public ArrayList<Card> bestHand(ArrayList<Card> playerHand)
    {
        ArrayList<Card> totalCards = new ArrayList<Card>();
        totalCards.addAll(dealerHand);
        totalCards.addAll(playerHand);

        int bestHandRank = 0;
        ArrayList<Card> bestHand = new ArrayList<Card>();

        for (int firstCard = 0; firstCard <= totalCards.size() - 2; firstCard++)
        {
            for (int secondCard = firstCard + 1; secondCard <= totalCards.size() - 1; secondCard++)
            {
                ArrayList<Card> deck = new ArrayList<Card>(totalCards);
                deck.remove(secondCard);
                deck.remove(firstCard);

                int rank = determineHand(deck);

                if (rank > bestHandRank)
                {
                    bestHandRank = rank;
                    bestHand = deck;
                }
                else if (rank == bestHandRank)
                {
                    // need to check the actual numbers first (Pair of 9's
                    // better than Pair of 5's)
                    // then check suit
                }
            }
        }

        handRank = bestHandRank;
        return bestHand;
    }


    /**
     * @param totalHand
     *            the total list of dealer's cards + player's cards - 2
     * @return what type of hand this is
     */
    public int determineHand(ArrayList<Card> totalHand)
    {
        if ((lowestCard(totalHand) == 10) && inOrder(totalHand) && sameSuit(totalHand))
        {
            // Royal Flush (Ace to 10 && Same Suit)
            // return handRank = 10 + ArrayList<Card>;

            return 10;
        }
        else if (inOrder(totalHand) && sameSuit(totalHand))
        {
            // Straight Flush (Any 5 in order && Same Suit)
            // return 9 + ArrayList<Card>;

            return 9;
        }
        else if (sameValue(totalHand) == 4)
        {
            // Four of a Kind (Any 4 && Same Value)
            // return 8 + ArrayList<Card>;

            return 8;
        }
        else if (sameValue(totalHand) == 3)
        {
            // Full House (3 Cards Same Value && 2 Other Cards Same Value)
            // return 7 + ArrayList<Card>;

            return 7;
        }
        else if (sameSuit(totalHand))
        {
            // Flush (Same Suit)
            // return 6 + ArrayList<Card>;

            return 6;
        }
        else if (inOrder(totalHand))
        {
            // Straight (Any 5 in order)
            // return 5 + ArrayList<Card>;

            return 5;
        }
        else if (sameValue(totalHand) == 2)
        {
            // Three of a Kind (3 Cards Same Value)
            // return 4 + ArrayList<Card>;

            return 4;
        }
        else if (sameValue(totalHand) == 1)
        {
            // Two Pair (2 Cards Same Value && 2 Other Cards Same Value)
            // return 3 + ArrayList<Card>;

            return 3;
        }
        else if (sameValue(totalHand) == 0)
        {
            // One Pair (2 Cards Same Value)
            // return 2 + ArrayList<Card>;

            return 2;
        }
        else
        {
            // else if (sameValue(totalHand) == -1) {
            // High Card
            // return 1 + ArrayList<Card>;

            return 1;
        }
    }


    /**
     * helper method
     * 
     * @param hand
     *            hand of 5
     * @return to see if they are of the same suit
     */
    private boolean sameSuit(ArrayList<Card> hand)
    {
        int[] suitCounts = new int[4];
        for (Card c : hand)
        {
            if (c.getSuit() == "Clubs")
            {
                suitCounts[0]++;
            }
            else if (c.getSuit() == "Diamonds")
            {
                suitCounts[1]++;
            }
            else if (c.getSuit() == "Hearts")
            {
                suitCounts[2]++;
            }
            else if (c.getSuit() == "Spades")
            {
                suitCounts[3]++;
            }
        }
        Arrays.sort(suitCounts);
        if (suitCounts[3] == 5)
        {
            return true;
        }
        return false;
    }


    /**
     * helper method
     * 
     * @param hand
     *            hand of 5
     * @return if same value
     */
    private int sameValue(ArrayList<Card> hand)
    {
        int[] valueCounts = new int[13];
        for (Card c : hand)
        {
            // lowest card value possible is 2
            valueCounts[c.getValue() - 2]++;
        }
        Arrays.sort(valueCounts);
        if (valueCounts[12] == 4)
        {
            return 4;
        }
        else if (valueCounts[12] == 3 && valueCounts[11] == 2)
        {
            return 3;
        }
        else if (valueCounts[12] == 3)
        {
            return 2;
        }
        else if (valueCounts[12] == 2 && valueCounts[11] == 2)
        {
            return 1;
        }
        else if (valueCounts[12] == 2)
        {
            return 0;
        }
        else
        {
            return -1;
        }
    }


    /**
     * helper method
     * 
     * @param hand
     *            hand of 5
     * @return sees if all are in ascending order
     */
    private boolean inOrder(ArrayList<Card> hand)
    {
        int[] values = new int[5];
        int[] compare = new int[5];
        int i = 0;
        for (Card c : hand)
        {
            values[i] = c.getValue();
            compare[i] = c.getValue();
            if (i + 1 < 5)
            {
                i++;
            }
        }
        Arrays.sort(values);
        if (compare.equals(values))
        {
            return true;
        }
        return false;
    }


    /**
     * helper method
     * 
     * @param hand
     *            hand of 5
     * @return finds lowest card
     */
    private int lowestCard(ArrayList<Card> hand)
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
        return values[0];
    }
}
