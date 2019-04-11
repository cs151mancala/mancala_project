import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class RegularLayout implements Layout
{
    Image marbleImage;
    Image boardImage;
    Color fontColor;

    public RegularLayout()
    {
        try
        {
            this.marbleImage = ImageIO.read(new File("BlueMarble.png"));
            this.boardImage = ImageIO.read(new File("RegularBoard.png"));

            boardImage = boardImage.getScaledInstance(Controller.FRAME_WIDTH, Controller.FRAME_HEIGHT, Image.SCALE_DEFAULT);
            marbleImage = marbleImage.getScaledInstance(Layout.MARBLE_WIDTH, Layout.MARBLE_HEIGHT, Image.SCALE_DEFAULT);
        }
        catch (IOException e)
        {
            System.out.println("Cannot load image");
        }

        fontColor = Color.BLACK;
    }

    @Override
    public Image getMarbleImage()
    {
        return marbleImage;
    }

    @Override
    public Image getBoardImage()
    {
        return boardImage;
    }

    @Override
    public Color getFontColor()
    {
        return fontColor;
    }

}
