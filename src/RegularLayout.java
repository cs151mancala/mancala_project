import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**One of formatted mancala game layout(Concrete class) following stategy pattern
 * @author Trevor O'Neil, Phillip Nguyen, Kunda Wu
 * @copyright	05/04/2019
 * @version		1.0
 * */

public class RegularLayout implements Layout {
    Image marbleImage;	//the marble image
    Image boardImage;	//the board image
    Color fontColor;	//the font color

    /**Get these image from local library and compose the basic components from mancala game
     * */
    public RegularLayout()
    {
        try
        {
            this.marbleImage = ImageIO.read(new File("BlueMarble.png"));
            this.boardImage = ImageIO.read(new File("RegularBoard.png"));

            boardImage = boardImage.getScaledInstance(MancalaFrame.FRAME_WIDTH, MancalaFrame.FRAME_HEIGHT, Image.SCALE_DEFAULT);
            marbleImage = marbleImage.getScaledInstance(Layout.MARBLE_WIDTH, Layout.MARBLE_HEIGHT, Image.SCALE_DEFAULT);
        }
        catch (IOException e)
        {
            System.out.println("Cannot load image");
        }

        fontColor = Color.BLACK;
    }

    /**Get the marble Image*/
    @Override
    public Image getMarbleImage()
    {
        return marbleImage;
    }

    /**Get the board Image*/
    @Override
    public Image getBoardImage()
    {
        return boardImage;
    }

    /**Get the font color*/
    @Override
    public Color getFontColor()
    {
        return fontColor;
    }

}