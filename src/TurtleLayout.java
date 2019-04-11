import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class TurtleLayout implements Layout
{
    Image marbleImage;
    Image boardImage;
    Color fontColor;

    public TurtleLayout()
    {
        try
        {
            this.marbleImage = ImageIO.read(new File("TurtleMarble.png"));
            this.boardImage = ImageIO.read(new File("TurtleBoard.png"));

            boardImage = boardImage.getScaledInstance(Controller.FRAME_WIDTH, Controller.FRAME_HEIGHT, Image.SCALE_DEFAULT);
            marbleImage = marbleImage.getScaledInstance(Layout.MARBLE_WIDTH, Layout.MARBLE_HEIGHT, Image.SCALE_DEFAULT);
        }
        catch (IOException e)
        {
            System.out.println("Cannot load image");
        }

        fontColor = new Color(245, 245, 245);
    }

    @Override
    public Image getMarbleImage() {
        return marbleImage;
    }

    @Override
    public Image getBoardImage() {
        return boardImage;
    }

    @Override
    public Color getFontColor() {
        return fontColor;
    }
}
