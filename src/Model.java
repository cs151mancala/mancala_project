import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.ArrayList;
import java.util.Arrays;

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
    private int numUndosPlayerA;
    private int numUndosPlayerB;
    private boolean repeatTurn;
    private boolean playerAWon;
    private boolean playerBWon;

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

        numUndosPlayerA = 3;

        repeatTurn = false;
        numUndosPlayerB = 3;
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
                pits[i] = 0;
                prevPits[i] = 0;
            }
            else
            {
                pits[i] = numberOfStartingMarbles;
                prevPits[i] = numberOfStartingMarbles;
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
     * @return the array that holds the number of marbles in eah pit
     */
    public int[] getPits()
    {
        return pits;
    }

    /**
     * Gets the array that holds the number of marbles in each pit from the previous turn
     * @return the array that holds the number of marbles in each pit from the previous turn
     */
    public int[] getPrevPits()
    {
        return prevPits;
    }

    /**
     * Updates the array when the player plays a turn
     * @param index the index of the pit the player clicked on
     */
    public void update(int index)
    {

        if (turnIsValid(index)) //If the player clicked on their pit
        {
            /*
            This block of code fills the array of pits for the previous turn
            */
            for (int i = 0; i < prevPits.length; i++)
            {
                prevPits[i] = pits[i];
            }

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
                        oppositeIndex = 6;
                    }
                    else if (indexEndPit == FIRST_PIT_B_INDEX + 1)
                    {
                        oppositeIndex = 5;
                    }
                    else if (indexEndPit == FIRST_PIT_B_INDEX + 2)
                    {
                        oppositeIndex = 4;
                    }
                    else if (indexEndPit == FIRST_PIT_B_INDEX + 3)
                    {
                        oppositeIndex = 3;
                    }
                    else if (indexEndPit == FIRST_PIT_B_INDEX + 4)
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
            repeatTurn = true;
            if (playerATurn && indexEndPit != MANCALA_A_INDEX ||
                !playerATurn && indexEndPit != MANCALA_B_INDEX)
            {
                playerATurn = !playerATurn;
                repeatTurn = false;
            }

            notifyChanges();
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
                pits[MANCALA_A_INDEX] += pits[j];
                pits[MANCALA_B_INDEX] += pits[14-j];
                pits[j] = 0;
                pits[14-j] = 0;
            }

            if (pits[MANCALA_A_INDEX] > pits[MANCALA_B_INDEX])
            {
                playerAWon = true;
            }
            else if (pits[MANCALA_B_INDEX] > MANCALA_A_INDEX)
            {
                playerBWon = true;
            }

            notifyChanges();
        }

    }

    /**
     * Undos the turn if the player chooses to do so
     */
    public void undo()
    {
        if (!Arrays.equals(pits, prevPits))
        {
            if (playerATurn && numUndosPlayerB > 0 && !repeatTurn ||
                playerATurn && numUndosPlayerA > 0 && repeatTurn)
            {
                if (!repeatTurn)
                {
                    numUndosPlayerB--;
                }
                else
                {
                    numUndosPlayerA--;
                }
                for (int i = 0; i < pits.length; i++)
                {
                    pits[i] = prevPits[i];
                }

                if (!repeatTurn)
                {
                    playerATurn = !playerATurn;
                }
                notifyChanges();
            }
            else if (!playerATurn && numUndosPlayerA > 0 && !repeatTurn ||
                    !playerATurn && numUndosPlayerB > 0 && repeatTurn)
            {
                if (!repeatTurn)
                {
                    numUndosPlayerA--;
                }
                else
                {
                    numUndosPlayerB--;
                }
                for (int i = 0; i < pits.length; i++)
                {
                    pits[i] = prevPits[i];
                }

                if (!repeatTurn)
                {
                    playerATurn = !playerATurn;
                }
                notifyChanges();
            }
        }
    }

    /**
     * Gets the number of undos for player A
     * @return the number of undos for player A
     */
    public int getNumUndosPlayerA()
    {
        return numUndosPlayerA;
    }

    /**
     * Gets the number of undos for player B
     * @return
     */
    public int getNumUndosPlayerB()
    {
        return numUndosPlayerB;
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

    /**
     * Checks if player A won the game
     * @return true if player A won the game, false otherwise
     */
    public boolean playerAWon() {
        return playerAWon;
    }

    /**
     * Checks if player B won the game
     * @return true if player B won the game, false otherwise
     */
    public boolean playerBWon() {
        return playerBWon;
    }

    /**
     * Checks if it's a tie game at the end
     * @return true if it's a tie game, false otherwise
     */
    public boolean tieGame()
    {
        if (pits[MANCALA_A_INDEX] != pits[MANCALA_B_INDEX])
        {
            return false;
        }

        int sum = 0;

        for (int index = FIRST_PIT_A_INDEX; index <= LAST_PIT_A_INDEX; index++)
        {
            sum += pits[index];
        }
        for (int index = FIRST_PIT_B_INDEX; index <= LAST_PIT_B_INDEX; index++)
        {
            sum += pits[index];
        }

        if (sum == 0 && pits[MANCALA_A_INDEX] == pits[MANCALA_B_INDEX])
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
