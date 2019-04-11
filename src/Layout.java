import java.awt.*;

public interface Layout
{
    int MARBLE_WIDTH = 30;
    int MARBLE_HEIGHT = 30;

    Image getMarbleImage();
    Image getBoardImage();
    Color getFontColor();
}
