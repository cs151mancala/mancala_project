import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.ArrayList;

/**
 * The model class for Mancala that handles the game logic and data
 */
public class Model
{
    /*
    Note the array is = {Mancala B, A1, A2, A3, A4, A5, A6, Mancala A, B1, B2, B3, B4, B5, B6}
     */
    public static final int MANCALA_B_INDEX = 0;
    public static final int MANCALA_A_INDEX = 7;
    public static final int FIRST_PIT_B_INDEX = 8;
    public static final int LAST_PIT_B_INDEX = 13;
    public static final int FIRST_PIT_A_INDEX = 1;
    public static final int LAST_PIT_A_INDEX = 6;
    public static final int NUMBER_OF_PITS = 14;

    private int[] pits; //This array holds the number of marbles in each pit
    private ArrayList<ChangeListener> listeners;
    boolean playerATurn;

    /**
     * Constructor that initializes the instance variables
     */
    public Model()
    {
        /*
        This block of code initializes all the pits to have 0 marbles
         */
        pits = new int[NUMBER_OF_PITS];
        for (int i = 0; i < pits.length; i++)
        {
            pits[i] = 0;
        }

        listeners = new ArrayList<>();
        playerATurn = true;
    }

    /**
     * Called inside mutator methods to notify changes to the view
     */
    private void notifyChanges()
    {
        ChangeEvent event = new ChangeEvent(this);

        for (ChangeListener listener : listeners)
        {
            listener.stateChanged(event);
        }
    }

    /**
     * Fills all the pits (except the mancalas) with the number of starting marbles
     * @param numberOfStartingMarbles the number of starting marbles (either 3 or 4)
     */
    public void fillPitsWithStartingMarbles(int numberOfStartingMarbles)
    {
        for (int i = 0; i < pits.length; i++)
        {
            if (i == MANCALA_A_INDEX || i == MANCALA_B_INDEX)
            {
                continue;
            }
            else
            {
                pits[i] = numberOfStartingMarbles;
            }
        }

        notifyChanges();
    }

    /**
     * Gets the array that holds the number of marbles in each pit
     * @return
     */
    public int[] getPits()
    {
        return pits;
    }

    /**
     * Adds a change listener to the array list of listeners
     * @param listener the change listener to be added
     */
    public void addChangeListener(ChangeListener listener)
    {
        listeners.add(listener);
    }

    /**
     * Checks which is the player's turn
     * @return true if it's player A's turn, false if it's player B's turn
     */
    boolean isPlayerATurn()
    {
        return playerATurn;
    }
}
