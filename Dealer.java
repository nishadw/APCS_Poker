import java.util.*;

/**
 * @version 5/23/2022
 * @author Nishad Wajge
 */
public class Dealer
{

    private ArrayList<Card> deckOfCards;

    /**
     * creates an empty ArrayList for total card deck creates an empty ArrayList
     * for community cards
     */
    public Dealer()
    {
        deckOfCards = new ArrayList<Card>();
        createDeck();
    }


    /**
     * @return creates randomized deck
     */
    private ArrayList<Card> createDeck()
    {
        // 2 is lowest, ace (with value of 14) is highest
        for (int i = 2; i < 15; i++)
        {
            deckOfCards.add(new Card("Clubs", i));
            deckOfCards.add(new Card("Diamonds", i));
            deckOfCards.add(new Card("Hearts", i));
            deckOfCards.add(new Card("Spades", i));
        }

        Collections.shuffle(deckOfCards);
        return deckOfCards;
    }


    /**
     * will add a card to community cards list and remove from card deck
     * 
     * @param list
     *            the community cards list
     */
    public void addCardtoList(ArrayList<Card> list)
    {
        list.add(deckOfCards.remove(0));
    }


    /**
     * this method is needed for Picture class purposes
     * 
     * @param list
     *            the hand of the player
     * @param player
     *            the player
     */
    public void addCardtoList(ArrayList<Card> list, Player player)
    {
        Card card = deckOfCards.remove(0);
        list.add(new Card(card.getSuit(), card.getValue(), player));
    }
}
