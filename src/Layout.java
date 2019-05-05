import java.awt.*;

/**
 * An interface to help get layout of Mancala game. This follows the strategy pattern
 * @author Trevor O'Neil, Phillip Nguyen, Kunda Wu
 * @copyright	05/04/2019
 * @version		1.0
 */
public interface Layout {

    int MARBLE_WIDTH = 30;
    int MARBLE_HEIGHT = 30;
    Image getMarbleImage();
    Image getBoardImage();
    Color getFontColor();
}
