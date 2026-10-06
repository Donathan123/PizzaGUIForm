import javax.swing.SwingUtilities;

/**
 * Main class for the Pizza Order GUI program.
 * It creates the order form frame on the Swing event thread and shows it.
 *
 * @author Donathan Chuon
 */
public class PizzaGUIRunner
{
    /**
     * Starts the program.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                PizzaGUIFrame frame = new PizzaGUIFrame();
                frame.setVisible(true);
            }
        });
    }
}
