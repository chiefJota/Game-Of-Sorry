import java.util.*;
public class SorryCard {

    private int number;
    private String description;
    private String Sorry;

    //card constructor
    public SorryCard(int number, String description) {
        this.number = number;
        this.description = description;
    }

    //get number function
    public int getNumber() {
        return number;
    }

    //get description function
    public String getDescription() {
        return description;
    }

    //set number function
    public void setNumber() {
        this.number = number;
    }

    //set description
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * toString method takes the number and description
     * and returns them as a string
     */
    @Override
    public String toString() {

        return number + ": " + description;
    }
}
