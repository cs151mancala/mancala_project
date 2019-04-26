public class MancalaTest
{
    public static void main(String[] args)
    {
        Model model = new Model();
        View view = new View(model);
        Controller controller = new Controller(model, view);

        controller.initialize();

    }
}
