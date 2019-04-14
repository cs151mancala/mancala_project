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
    private int[] prevPits; //This array holds the number of marbles in each pit in the previous turn
    private ArrayList<ChangeListener> listeners;
    private boolean playerATurn;

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
        prevPits = new int[NUMBER_OF_PITS];

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
    public void fillPitsWithStartingMarbles(int numberOfStartingMarbles) {
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
     * Checks if player A clicked pit A1-A6 or player B clicked pit B1-B6
     * @param index the index of the pit the player clicked on
     * @return true if turn is valid, false otherwise
     */
    private boolean turnIsValid(int index)
    {
        if (playerATurn)
        {
            if (index >= FIRST_PIT_A_INDEX && index <= LAST_PIT_A_INDEX)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            if (index >= FIRST_PIT_B_INDEX && index <= LAST_PIT_B_INDEX)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
    }

    /**
     * Gets the array that holds the number of marbles in each pit
     * @return
     */
    public int[] getPits()
    {
        return pits;
    }

    public int[] getPrevPits()
    {
        return prevPits;
    }

    /**update changes when hit*/
    public void update(int index)
    {
        /*
        This block of code fills the array of pits for the previous turn
         */
        for (int i = 0; i < prevPits.length; i++)
        {
            prevPits[i] = pits[i];
        }

        if (turnIsValid(index)) //If the player clicked on their pit
        {
            int numberOfMar = pits[index];    //number of mar in the hit pit
            pits[index] = 0;        //Empty the pit the player clicked on
            int i = index + 1;        //the starting pit
            int indexEndPit = (index + numberOfMar) % 14; //the index of the ending pit
            int numMarblesEndPit = pits[indexEndPit];  //Number of marbles in the ending pit

            while (numberOfMar > 0)
            {
                /*
                The if statement makes sure that the marble does not drop on the opponent's mancala
                 */
                if (playerATurn && i % 14 != MANCALA_B_INDEX ||
                    !playerATurn && i % 14 != MANCALA_A_INDEX)
                {
                    pits[i % 14]++;
                    numberOfMar--;
                }
                i++;
            }

            /*
            This block of code is for when the last marble the player drops is in
            an empty pit on their side, so they take that marble and all of the opponent's
            marbles on the opposite side and place it in their mancala
             */
            if (numMarblesEndPit == 0)
            {
                if (playerATurn && indexEndPit >= FIRST_PIT_A_INDEX && indexEndPit <= LAST_PIT_A_INDEX)
                {
                    pits[MANCALA_A_INDEX] += (pits[indexEndPit] + pits[14 - indexEndPit]);
                    pits[indexEndPit] = 0;
                    pits[14 - indexEndPit] = 0;
                }
                else if (!playerATurn && indexEndPit >= FIRST_PIT_B_INDEX && indexEndPit <= LAST_PIT_B_INDEX)
                {
                    int oppositeIndex;
                    if (indexEndPit == FIRST_PIT_B_INDEX)
                    {
                        oppositeIndex = 5;
                    }
                    else if (indexEndPit == FIRST_PIT_B_INDEX + 1)
                    {
                        oppositeIndex = 4;
                    }
                    else if (indexEndPit == FIRST_PIT_B_INDEX + 2)
                    {
                        oppositeIndex = 3;
                    }
                    else if (indexEndPit == FIRST_PIT_B_INDEX + 3)
                    {
                        oppositeIndex = 2;
                    }
                    else
                    {
                        oppositeIndex = 1;
                    }

                    pits[MANCALA_B_INDEX] += pits[indexEndPit] + pits[oppositeIndex];
                    pits[indexEndPit] = 0;
                    pits[oppositeIndex] = 0;
                }
            }


            /*
            This block of code is for when the last stone the player drops
            is in their own mancala, they get a free turn
             */
            if (playerATurn && indexEndPit != MANCALA_A_INDEX ||
                !playerATurn && indexEndPit != MANCALA_B_INDEX)
            {
                playerATurn = !playerATurn;
            }
        }
        else
        {
            System.out.println("Turn is invalid");
        }

        /*
        This block of code calculates the sum of A1-A6 for player A
        and B1-B6 for player B
         */
        int playerA_Pit = 0;
        int playerB_Pit = 0;
        for (int j = 1; j < 7; j++)
        {
            playerA_Pit += pits[j];
            playerB_Pit += pits[14-j];
        }

        /*
        This block of code checks if either player won
         */
        if (playerA_Pit == 0 || playerB_Pit == 0)
        {

            for (int j = 1; j < 7; j++) {
                pits[7] += pits[j];
                pits[0] += pits[14-j];
                pits[j] = 0;
                pits[14-j] = 0;
            }

            if (pits[7] > pits[0]) {
                System.out.println("Player A wins");
            } else {
                System.out.println("Player B wins");
            }
        }

        notifyChanges();
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
