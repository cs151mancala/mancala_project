import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;

/**
 * The view class that deals with the visual aspects of the game
 * @author Trevor O'Neil, Phillip Nguyen, Kunda Wu
 * @copyright	05/04/2019
 * @version		1.0
 */
public class MancalaLabel extends JPanel implements ChangeListener
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Layout layout;  //The game can have different layouts (different board, marbles)
    private MancalaLogic model;    //Need a Model instance variable to access the data from it
    private int[] numMarblesInPits;
    private Ellipse2D.Double[] pits;    //An array of the pits as ellipses
    private int[] seeds;

    /**
     * Constructor that initializes the instance variables
     * @param model the model to access data from
     */
    public MancalaLabel(MancalaLogic model)
    {
        this.model = model;
        pits = new Ellipse2D.Double[MancalaLogic.NUMBER_OF_PITS];
        numMarblesInPits = model.getPits();
        seeds = new int[MancalaLogic.NUMBER_OF_PITS];

        for (int i = 0; i < seeds.length; i++)
        {
            seeds[i] = i;
        }

        setPits();
    }

    /**
     * Sets up each pit on the board as an ellipse and stores it in the pits array
     */
    private void setPits()
    {
        /*
        This block of code is for coordinates and dimensions of the mancala pits
         */
        int mancalaHeight = 406;
        int mancalaWidth = 88;
        Point mancalaBStartingPoint = new Point(18, 152);
        Point mancalaAStartingPoint = new Point(1054, 152);

        /*
        This block of code is for the coordinates and dimensions of the regular pits
         */
        int pitWidth = 126;
        int pitHeight = 146;
        Point pitBStartingPoint = new Point(894, 160);
        Point pitAStartingPoint = new Point(144, 413);
        int differenceInXCoord = 150;

        int translate = 0; //Scaler is just for moving the x coordinates to the next pit
        for (int i = 0; i < MancalaLogic.NUMBER_OF_PITS; i++)
        {
            if (i == MancalaLogic.MANCALA_B_INDEX) //If it's at the mancala B pit
            {
                pits[i] = new Ellipse2D.Double(mancalaBStartingPoint.x, mancalaBStartingPoint.y, mancalaWidth, mancalaHeight);
            }
            else if (i == MancalaLogic.MANCALA_A_INDEX)    //If it's at the mancala A pit
            {
                pits[i] = new Ellipse2D.Double(mancalaAStartingPoint.x, mancalaAStartingPoint.y, mancalaWidth, mancalaHeight);
            }
            else if (i >= MancalaLogic.FIRST_PIT_B_INDEX && i <= MancalaLogic.LAST_PIT_B_INDEX)   //If it's at the regular pits for player B
            {
                pits[i] = new Ellipse2D.Double(pitBStartingPoint.x - differenceInXCoord * (translate - 1), pitBStartingPoint.y, pitWidth, pitHeight);
            }
            else    //If it's at the regular pits for player A
            {
                pits[i] = new Ellipse2D.Double(pitAStartingPoint.x + differenceInXCoord * (translate - 1), pitAStartingPoint.y, pitWidth, pitHeight);
            }

            if (i == MancalaLogic.LAST_PIT_A_INDEX || i == MancalaLogic.LAST_PIT_B_INDEX) //If we reach the last regular pit for player A or B, reset the scaler
            {
                translate = 0;
            }
            else
            {
                translate++;
            }
        }

    }

    /**
     * Gets the array of pits as ellipses
     * @return the array of pits as ellipses
     */
    public Ellipse2D.Double[] getPits() {
        return pits;
    }

    /**
     * Sets the layout of the board and marbles
     * @param layout the layout of the board and marbles
     */
    public void setBoardLayout(Layout layout)
    {
        this.layout = layout;
        repaint();
    }

    /**
     * Draws the board, marbles, and text
     * @param g
     */
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.drawImage(layout.getBoardImage(), 0, 0, this);   //Draws the board

        /*
        This block of code draws the text for the player labels
         */
        g2.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        if (model.isPlayerATurn())
        {
            g2.setColor(Color.RED);
            g2.drawString("Player A", (MancalaFrame.FRAME_WIDTH / 2) - 35, MancalaFrame.FRAME_HEIGHT - 60);
            g2.setColor(layout.getFontColor());
            g2.drawString("Player B", (MancalaFrame.FRAME_WIDTH / 2) - 35, 60);
        }
        else
        {
            g2.setColor(Color.RED);
            g2.drawString("Player B", (MancalaFrame.FRAME_WIDTH / 2) - 35, 60);
            g2.setColor(layout.getFontColor());
            g2.drawString("Player A", (MancalaFrame.FRAME_WIDTH / 2) - 35, MancalaFrame.FRAME_HEIGHT - 60);
        }
        g2.drawString("Undos left: " + model.getNumUndosPlayerA(), (MancalaFrame.FRAME_WIDTH / 2) + 50, MancalaFrame.FRAME_HEIGHT - 60);
        g2.drawString("Undos left: " + model.getNumUndosPlayerB(), (MancalaFrame.FRAME_WIDTH / 2) + 50, 60);

        /*
        This block of code draws the marbles, marble count, and pit labels
         */
        Random rand = new Random();
        for (int i = 0; i < numMarblesInPits.length; i++)
        {
            Rectangle2D boundingBox = getPits()[i].getBounds2D();   //Gets the bounding box of the pit

            /*
            This block of code is for drawing the pit labels and number of marbles
             */
            int xCoord;
            int yCoord;
            g2.setColor(layout.getFontColor());
            if (i >= MancalaLogic.FIRST_PIT_A_INDEX && i <= MancalaLogic.LAST_PIT_A_INDEX)
            {
                xCoord = (int) boundingBox.getCenterX() - 20;
                yCoord = (int) boundingBox.getMaxY() + 5;
                g2.drawString("A" + i, xCoord, yCoord);

                xCoord = (int) boundingBox.getCenterX() - 12;
                yCoord = (int) boundingBox.getMinY() - 50;
            }
            else if (i >= MancalaLogic.FIRST_PIT_B_INDEX && i <= MancalaLogic.LAST_PIT_B_INDEX)
            {
                xCoord = (int) boundingBox.getCenterX() - 20;
                yCoord = (int) boundingBox.getMinY() - 50;

                g2.drawString("B" + (i - 7), xCoord, yCoord);

                xCoord = (int) boundingBox.getCenterX() - 12;
                yCoord = (int) boundingBox.getMaxY();
            }
            else if (i == MancalaLogic.MANCALA_A_INDEX)
            {
                xCoord = (int) boundingBox.getMinX() - 20;
                yCoord = (int) boundingBox.getMaxY() + 3;

                g2.drawString("Mancala A", xCoord, yCoord);

                xCoord = xCoord - 30;
                yCoord = (int) boundingBox.getCenterY() - 20;
            }
            else
            {
                xCoord = (int) boundingBox.getMinX();
                yCoord = (int) boundingBox.getMinY() - 40;

                g2.drawString("Mancala B", xCoord, yCoord);

                xCoord = (int) boundingBox.getMaxX() + 20;
                yCoord = (int) boundingBox.getCenterY() - 20;
            }
            g2.drawString("" + numMarblesInPits[i], xCoord, yCoord); //Draws he number of marbles

            /*
            This block of code is for drawing the marbles
             */
            if (model.getPrevPits()[i] != numMarblesInPits[i])
            {
                seeds[i]++;
            }
            rand.setSeed(seeds[i]);
            for (int j = 0; j < numMarblesInPits[i]; j++)
            {
                int randX = rand.nextInt((int) boundingBox.getWidth() - Layout.MARBLE_WIDTH * 2 + 15);
                int randY = rand.nextInt((int) boundingBox.getHeight() - Layout.MARBLE_HEIGHT * 2 - 15);

                g2.drawImage(layout.getMarbleImage(), (int) (boundingBox.getX() + randX), (int) (boundingBox.getY() + randY), this);

            }
        }
    }

    /**
     * Repaints the board when the state of the model changes
     * @return ChangeListener that repaints the board
     */
    @Override
    public void stateChanged(ChangeEvent e)
    {
        numMarblesInPits = model.getPits();
        this.repaint();
    }

}