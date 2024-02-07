/**
 * @version 5/2/22
 * @author Nishad Wajge & Chris Zhang
 * https://www.improvemagic.com/all-playing-cards-names-with-pictures/
 */

public class Card {

    private String suit;
    private int value;
    private Picture front;
    private Picture back;

    /**
     * 
     * @param suit suit
     * @param value value from 2-14
     */
    public Card (String suit, int value) {
        this.suit = suit;
        this.value = value;

        front = new Picture(("" + suit.charAt(0)).toUpperCase() + value + ".png");
        back = new Picture("back2.png");
        
        front.resize(0.5 * 0.58);
        back.resize(0.5);
    }

    /**
     * 
     * @param suit suit
     * @param value value from 2-14
     * @param player player who has this card
     */
    public Card (String suit, int value, Player player) {
        this.suit = suit;
        this.value = value;

        front = new Picture(("" + suit.charAt(0)).toUpperCase() + value + ".png", player);
        back = new Picture("back2.png", player);
        
        front.resize(0.5 * 0.58);
        back.resize(0.5);
    }
    
    /**
     * 
     * @return suit
     */
    public String getSuit() {
        return suit;
    }

    /**
     * 
     * @return value
     */
    public int getValue() {
        return value;
    }

    /**
     * 
     * @return the front of the card
     */
    public Picture getFront() {
        return front;
    }

    /**
     * 
     * @return the back of the card
     */
    public Picture getBack() {
        return back;
    }

    /**
     * @return string of card name
     */
    public String toString() {
        return value + " of " + suit;
    }

    /**
     * 
     * @param card other card to be compared to
     * @param checkSuit boolean that decides whether
     *  this comparison will use the suit as a factor or not
     * @return positive if this card is greater than other card,
     *  negative if less than, 0 if card values are the same and 
     *  checkSuit is false, and 0 if the two cards are the same
     */
    public int compareTo(Card card, boolean checkSuit) {
        if (value > card.getValue()) {
            return 1;
        }
        else if (value < card.getValue()) {
            return -1;
        }
        else {
            if (checkSuit) {
                if (suit.compareTo(card.getSuit()) > 0) {
                    return 1;
                }
                else if (suit.compareTo(card.getSuit()) < 0){
                    return -1;
                }
                else {
                    return 0;
                }
            }
            else {
                return 0;
            }
        }
    }
}