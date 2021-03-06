import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Card {
    private int v, s;
    private String value, suit, cardName;
    private boolean red, king, ace, up;
    private ImageIcon front, back, icon;

    Card(int v, int s){
        //Sets value
        {
            this.v = v;
            if (1 < v & v < 11) {
                value = Integer.toString(v);
            }
            else {
                switch (v) {
                    case 1 -> value =  "ace";
                    case 11 -> value = "jack";
                    case 12 -> value = "queen";
                    case 13 -> value = "king";
                    default -> value = "joker";
                }
                if (v == 13) {
                    king = true;
                    ace = false;
                }
                else if (v == 1) {
                    ace = true;
                    king = false;
                }
                else{
                    ace = false;
                    king = false;
                }
            }
        }
        //Sets suit
        this.s = s;
        switch (s) {
            case 1 -> {
                suit = "hearts";
                red = true;
            }
            case 2 -> {
                suit = "diamonds";
                red = true;
            }
            case 3 -> {
                suit = "spades";
                red = false;
            }
            case 4 -> {
                suit = "clubs";
                red = false;
            }
            default -> {
                suit = "red";
                red = true;
            }
        }

        up = false;

        ImageIcon imageIcon = new ImageIcon("redcardback.png");
        back = Resize(imageIcon);

        imageIcon = new ImageIcon(String.format("C:\\Users\\Poox\\Desktop\\CompSci\\Resume\\SolitareProject\\Solitare\\cardfaces\\%s_of_%s.png", value, suit));
        front = Resize(imageIcon);

        cardName = (value + " of " + suit);

    }
    Card(){}

    public void Display(){
        if (up)
            System.out.print(cardName);
        else
            System.out.print("Card is face down");
    }

    public String getCardName(){
        return cardName;
    }

    public int getValue(){return v;}

    public String getSuit(){
        return suit;
    }
    public int getSuitInt(){
        return s;
    }

    public void Flip(){
        up = !up;
    }

    public Boolean isUp(){
        return up;
    }
    public Boolean isDown(){
        return !up;
    }
    public Boolean isAce(){return ace;}
    public Boolean isKing(){return king;}

    public boolean isRed(){
        return red && up;
    }
    public boolean isBlack(){
        return !red && up;
    }

    public void setFlip(boolean up){
        this.up = up;
    }

    public void copyThisCard(Card c){
        v = c.v;
        s = c.s;

        value = c.value;
        suit = c.suit;
        cardName = c.cardName;

        red = c.red;
        king = c.king;
        ace = c.ace;
        up = c.up;

        front = c.front;
        back = c.back;
        icon = c.getCardIcon();
    }

    public ImageIcon Resize(ImageIcon imageIcon){
        imageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(90, 120,  java.awt.Image.SCALE_SMOOTH));
        return imageIcon;
    }
    public ImageIcon getCardIcon(){

        //System.out.println("getCardIcon called");

        if (isUp())
            return front;
        else
            return back;

    }
}
