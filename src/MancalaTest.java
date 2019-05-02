public class MancalaTest
{
    public static void main(String[] args)
    {
        MancalaLogic model = new MancalaLogic();
        MancalaLabel view = new MancalaLabel(model);
        MancalaFrame controller = new MancalaFrame(model, view);
    }
}
