
/**The tester class for the implementation of Mancala Game
 * @author Trevor O'Neil, Phillip Nguyen, Kunda Wu
 * @copyright	05/04/2019
 * @version		1.0
 * */

public class MancalaTest {
    public static void main(String[] args)
    {
        MancalaLogic model = new MancalaLogic();
        MancalaLabel view = new MancalaLabel(model);
        MancalaFrame controller = new MancalaFrame(model, view);
    }
}
