import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Controller class that connects the model and view class
 */
public class Controller extends JFrame {
    /*
     * This block of code is the dimensions of the frame
     */
    public static final int FRAME_WIDTH = 1152;
    public static final int FRAME_HEIGHT = 658;

    private Model model;
    private View view;

    /**
     * Constructor that initializes the instance variables
     * @param model the model
     * @param view the view
     */
    public Controller(Model model, View view)
    {
        this.model = model;
        this.view = view;
    }

    /**
     * Shows the game window and does everything that's needed when the program starts
     */
    public void initialize()
    {
        model.addChangeListener(view);   //Adds the view's change listener to the model
        model.fillPitsWithStartingMarbles(0);


        controllerPanel();
        view.setBoardLayout(new RegularLayout());
        this.add(view);
        this.addMouseListener(new Listener());
        this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        showSetMarblesWindow();
        showSetLayoutWindow();
    }


    private void controllerPanel() {

        JButton undoButton = new JButton("Undo");

        undoButton.addActionListener(e ->
        {
            model.undo();
        });

        JButton quitButton = new JButton("Quit");

        quitButton.addActionListener(e ->
        {
            System.exit(0);
        });

        JButton regular = new JButton("Regular layout");

        regular.addActionListener(e -> {
            view.setBoardLayout(new RegularLayout());
        });

        JButton tutle = new JButton("Tutle format");

        tutle.addActionListener(e -> {
            view.setBoardLayout(new TurtleLayout());
        });

        JButton reset4 = new JButton("Reset game [4]");
        JButton reset3 = new JButton("Reset game [3]");

        reset4.addChangeListener(e -> {
            model.fillPitsWithStartingMarbles(4);
        });
        reset3.addChangeListener(e -> {
            model.fillPitsWithStartingMarbles(3);
        });

        view.add(undoButton);
        view.add(reset3);
        view.add(reset4);
        view.add(tutle);
        view.add(regular);
        view.add(quitButton);
    }

    /**
     * Shows the window to set the initial number of marbles
     */
    private void showSetMarblesWindow()
    {
        JPanel setMarblesPanel = new JPanel();
        JRadioButton threeMarbles = new JRadioButton("3");
        JRadioButton fourMarbles = new JRadioButton("4");

        //ButtonGroup is so only one radio button can be selected
        ButtonGroup group = new ButtonGroup();
        group.add(threeMarbles);
        group.add(fourMarbles);
        threeMarbles.setSelected(true);

        setMarblesPanel.add(threeMarbles);
        setMarblesPanel.add(fourMarbles);

        int choice = JOptionPane.showOptionDialog(this, setMarblesPanel,
                "Initial number of marbles", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE, null, null, null);

        int numberOfMarbles = 0;
        if (choice == JOptionPane.OK_OPTION)
        {
            if (threeMarbles.isSelected())
            {
                numberOfMarbles = 3;
            }
            else
            {
                numberOfMarbles = 4;
            }
        }
        else
        {
            System.exit(0);
        }

        model.fillPitsWithStartingMarbles(numberOfMarbles);
    }

    /**
     * Shows the window to set the layout of the game
     */
    private void showSetLayoutWindow()
    {
        JPanel setLayoutPanel = new JPanel();
        JRadioButton regularLayout = new JRadioButton("Regular Layout");
        JRadioButton turtleLayout = new JRadioButton("Turtle Layout");

        //ButtonGroup is so only one radio button can be selected
        ButtonGroup group = new ButtonGroup();
        group.add(regularLayout);
        group.add(turtleLayout);
        regularLayout.setSelected(true);

        setLayoutPanel.add(regularLayout);
        setLayoutPanel.add(turtleLayout);

        int choice = JOptionPane.showOptionDialog(this, setLayoutPanel,
                "Layout", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE, null, null, null);

        if (choice == JOptionPane.OK_OPTION)
        {
            if (regularLayout.isSelected())
            {
                view.setBoardLayout(new RegularLayout());
            }
            else
            {
                view.setBoardLayout(new TurtleLayout());
            }
        }
        else
        {
            System.exit(0);
        }
    }

    /**
     * MouseListener class that listens when the user clicks on a pit
     */
    private class Listener extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {


            for (int i = 0; i < view.getPits().length; i++)
            {
                if (view.getPits()[i].contains(e.getPoint()))
                {
                    model.move(i);
                    if (model.turnInvalid()) {
                        JDialog error = new JDialog();
                        error.add(new JLabel("Turn is invalid"));
                        error.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
                        error.setLayout(new GridLayout(2, 2));
                        JButton ok = new JButton("Okay");
                        ok.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                error.dispose();
                            }
                        });
                        error.add(ok);
                        error.pack();
                        error.setVisible(true);
                    }
                }

                if (model.playerAWon() || model.playerBWon())
                {
                    int choice;
                    if (model.playerAWon())
                    {
                        choice = JOptionPane.showOptionDialog(Controller.this, "Player A won",
                                null, JOptionPane.OK_CANCEL_OPTION,
                                JOptionPane.PLAIN_MESSAGE, null, new String[]{"Restart", "Cancel"}, null);
                    }
                    else
                    {
                        choice = JOptionPane.showOptionDialog(Controller.this, "Player B won",
                                null, JOptionPane.OK_CANCEL_OPTION,
                                JOptionPane.PLAIN_MESSAGE, null, new String[]{"Restart", "Cancel"}, null);
                    }
                    if (choice == JOptionPane.OK_OPTION)
                    {
                        initialize();
                    }
                    else
                    {
                        System.exit(0);
                    }
                }
            }
        }
    }
}