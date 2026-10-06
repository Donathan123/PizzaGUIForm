import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A form for ordering one pizza.
 *
 * The user picks a crust with the radio buttons, a size from the combo box
 * and one or more toppings with the check boxes. Clicking Order shows the
 * order in the text area as a receipt with the sub-total, the 7% tax and
 * the total. Clear wipes the form and Quit asks before closing.
 *
 * @author Donathan Chuon
 */
public class PizzaGUIFrame extends JFrame
{
    private static final double TAX_RATE = 0.07;
    private static final double TOPPING_PRICE = 1.00;

    // Sizes and their base prices line up by index
    private static final String[] SIZES = {"Small", "Medium", "Large", "Super"};
    private static final double[] SIZE_PRICES = {8.00, 12.00, 16.00, 20.00};

    private static final String[] TOPPINGS = {"Pepperoni", "Sausage", "Mushrooms", "Onions",
            "Green Peppers", "Black Olives", "Pineapple", "Extra Cheese"};

    // Width of the receipt in characters
    private static final int RECEIPT_WIDTH = 41;

    private JPanel mainPnl;
    private JPanel titlePnl;
    private JPanel optionsPnl;
    private JPanel crustPnl;
    private JPanel sizePnl;
    private JPanel toppingsPnl;
    private JPanel receiptPnl;
    private JPanel buttonPnl;

    private JLabel titleLbl;

    private JRadioButton thinRB;
    private JRadioButton regularRB;
    private JRadioButton deepDishRB;
    private ButtonGroup crustGroup;

    private JComboBox<String> sizeCB;

    private JCheckBox[] toppingCBs;

    private JTextArea receiptTA;
    private JScrollPane scroller;

    private JButton orderBtn;
    private JButton clearBtn;
    private JButton quitBtn;

    /**
     * Builds the order form.
     */
    public PizzaGUIFrame()
    {
        mainPnl = new JPanel();
        mainPnl.setLayout(new BorderLayout(5, 5));
        mainPnl.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        createTitlePanel();
        createOptionsPanel();

        // Title and the three option panels go across the top
        JPanel topPnl = new JPanel();
        topPnl.setLayout(new BorderLayout());
        topPnl.add(titlePnl, BorderLayout.NORTH);
        topPnl.add(optionsPnl, BorderLayout.CENTER);
        mainPnl.add(topPnl, BorderLayout.NORTH);

        // The receipt goes right above the buttons
        createReceiptPanel();
        mainPnl.add(receiptPnl, BorderLayout.CENTER);

        createButtonPanel();
        mainPnl.add(buttonPnl, BorderLayout.SOUTH);

        add(mainPnl);

        setTitle("Pizza Order Form");
        setSize(760, 680);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // ---------------------------------------------------------------
    // GUI set up
    // ---------------------------------------------------------------

    /**
     * Creates the panel with the title of the form.
     */
    private void createTitlePanel()
    {
        titlePnl = new JPanel();
        titleLbl = new JLabel("Pizza Order Form", JLabel.CENTER);
        titleLbl.setFont(new Font("SansSerif", Font.BOLD, 28));
        titlePnl.add(titleLbl);
    }

    /**
     * Creates the panel that holds the crust, size and toppings panels
     * side by side.
     */
    private void createOptionsPanel()
    {
        optionsPnl = new JPanel();
        optionsPnl.setLayout(new GridBagLayout());

        createCrustPanel();
        createSizePanel();
        createToppingsPanel();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(0, 3, 0, 3);

        gbc.gridx = 0;
        gbc.weightx = 1.0;
        optionsPnl.add(crustPnl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        optionsPnl.add(sizePnl, gbc);

        // Toppings gets twice the room since it has two columns
        gbc.gridx = 2;
        gbc.weightx = 2.0;
        optionsPnl.add(toppingsPnl, gbc);
    }

    /**
     * Creates the crust panel: a set of radio buttons in one button group
     * so only one crust can be picked.
     */
    private void createCrustPanel()
    {
        crustPnl = new JPanel();
        crustPnl.setLayout(new GridLayout(3, 1));
        crustPnl.setBorder(new TitledBorder(new EtchedBorder(), "Crust"));

        thinRB = new JRadioButton("Thin");
        regularRB = new JRadioButton("Regular");
        deepDishRB = new JRadioButton("Deep-dish");

        crustGroup = new ButtonGroup();
        crustGroup.add(thinRB);
        crustGroup.add(regularRB);
        crustGroup.add(deepDishRB);

        crustPnl.add(thinRB);
        crustPnl.add(regularRB);
        crustPnl.add(deepDishRB);
    }

    /**
     * Creates the size panel: a combo box with the four sizes. It starts
     * with nothing picked so the user has to choose a size.
     */
    private void createSizePanel()
    {
        sizePnl = new JPanel();
        sizePnl.setLayout(new BorderLayout(0, 8));
        sizePnl.setBorder(new TitledBorder(new EtchedBorder(), "Size"));

        sizeCB = new JComboBox<String>(SIZES);
        sizeCB.setSelectedIndex(-1);

        JLabel priceLbl = new JLabel("<html>Small $8.00<br>Medium $12.00<br>Large $16.00<br>Super $20.00</html>");

        JPanel comboPnl = new JPanel();
        comboPnl.add(sizeCB);

        JPanel pricePnl = new JPanel();
        pricePnl.add(priceLbl);

        sizePnl.add(comboPnl, BorderLayout.NORTH);
        sizePnl.add(pricePnl, BorderLayout.CENTER);
    }

    /**
     * Creates the toppings panel: one check box for each topping, laid
     * out in two columns.
     */
    private void createToppingsPanel()
    {
        toppingsPnl = new JPanel();
        toppingsPnl.setLayout(new GridLayout(0, 2));
        toppingsPnl.setBorder(new TitledBorder(new EtchedBorder(), "Toppings ($1.00 each)"));

        toppingCBs = new JCheckBox[TOPPINGS.length];

        for (int i = 0; i < TOPPINGS.length; i++)
        {
            toppingCBs[i] = new JCheckBox(TOPPINGS[i]);
            toppingsPnl.add(toppingCBs[i]);
        }
    }

    /**
     * Creates the receipt panel: a read only text area in a scroll pane.
     * A monospaced font is used so the prices line up in a column.
     */
    private void createReceiptPanel()
    {
        receiptPnl = new JPanel();
        receiptPnl.setLayout(new BorderLayout());
        receiptPnl.setBorder(new TitledBorder(new EtchedBorder(), "Your Order"));

        receiptTA = new JTextArea(16, 45);
        receiptTA.setFont(new Font("Monospaced", Font.PLAIN, 14));
        receiptTA.setEditable(false);

        scroller = new JScrollPane(receiptTA);
        receiptPnl.add(scroller, BorderLayout.CENTER);
    }

    /**
     * Creates the button panel with the Order, Clear and Quit buttons.
     */
    private void createButtonPanel()
    {
        buttonPnl = new JPanel();
        buttonPnl.setLayout(new GridLayout(1, 3, 10, 0));

        orderBtn = new JButton("Order");
        clearBtn = new JButton("Clear");
        quitBtn = new JButton("Quit");

        orderBtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                placeOrder();
            }
        });

        clearBtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                clearForm();
            }
        });

        quitBtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                quit();
            }
        });

        buttonPnl.add(orderBtn);
        buttonPnl.add(clearBtn);
        buttonPnl.add(quitBtn);
    }

    // ---------------------------------------------------------------
    // Button actions
    // ---------------------------------------------------------------

    /**
     * Builds the order from the settings on the form and shows it in the
     * text area as a receipt. An order must have a crust, a size and at
     * least one topping. If something is missing the user is told what to
     * fix and no receipt is shown.
     */
    private void placeOrder()
    {
        String crust = getSelectedCrust();
        int sizeIndex = sizeCB.getSelectedIndex();
        int toppingCount = getToppingCount();

        if (crust.equals(""))
        {
            JOptionPane.showMessageDialog(this, "Please pick a crust.",
                    "Order Not Complete", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (sizeIndex == -1)
        {
            JOptionPane.showMessageDialog(this, "Please pick a size.",
                    "Order Not Complete", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (toppingCount == 0)
        {
            JOptionPane.showMessageDialog(this, "Please pick at least one topping.",
                    "Order Not Complete", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double subTotal = SIZE_PRICES[sizeIndex] + toppingCount * TOPPING_PRICE;
        double tax = subTotal * TAX_RATE;
        double total = subTotal + tax;

        String doubleLine = makeLine('=');
        String singleLine = makeLine('-');

        StringBuilder receipt = new StringBuilder();
        receipt.append(doubleLine).append("\n");
        receipt.append(formatLine(crust + " Crust, " + SIZES[sizeIndex], SIZE_PRICES[sizeIndex]));

        for (int i = 0; i < toppingCBs.length; i++)
        {
            if (toppingCBs[i].isSelected())
            {
                receipt.append(formatLine(TOPPINGS[i], TOPPING_PRICE));
            }
        }

        receipt.append("\n");
        receipt.append(formatLine("Sub-total:", subTotal));
        receipt.append(formatLine("Tax:", tax));
        receipt.append(singleLine).append("\n");
        receipt.append(formatLine("Total:", total));
        receipt.append(doubleLine).append("\n");

        receiptTA.setText(receipt.toString());
        receiptTA.setCaretPosition(0);
    }

    /**
     * Wipes every component on the form so it is ready for a new order.
     */
    private void clearForm()
    {
        crustGroup.clearSelection();
        sizeCB.setSelectedIndex(-1);

        for (int i = 0; i < toppingCBs.length; i++)
        {
            toppingCBs[i].setSelected(false);
        }

        receiptTA.setText("");
    }

    /**
     * Asks the user if they are sure and closes the program if they say yes.
     */
    private void quit()
    {
        int answer = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to quit?",
                "Quit", JOptionPane.YES_NO_OPTION);

        if (answer == JOptionPane.YES_OPTION)
        {
            System.exit(0);
        }
    }

    // ---------------------------------------------------------------
    // Helper methods
    // ---------------------------------------------------------------

    /**
     * Gets the crust the user picked.
     *
     * @return "Thin", "Regular" or "Deep-dish", or "" if none is picked
     */
    private String getSelectedCrust()
    {
        if (thinRB.isSelected())
        {
            return "Thin";
        }
        if (regularRB.isSelected())
        {
            return "Regular";
        }
        if (deepDishRB.isSelected())
        {
            return "Deep-dish";
        }
        return "";
    }

    /**
     * Counts how many toppings are checked.
     *
     * @return the number of toppings picked
     */
    private int getToppingCount()
    {
        int count = 0;

        for (int i = 0; i < toppingCBs.length; i++)
        {
            if (toppingCBs[i].isSelected())
            {
                count++;
            }
        }
        return count;
    }

    /**
     * Makes one line of the receipt with the item on the left and the
     * price lined up on the right.
     *
     * @param item  the description of the item
     * @param price the price of the item
     * @return the formatted line ending with a new line
     */
    private String formatLine(String item, double price)
    {
        String priceStr = String.format("$%.2f", price);
        return String.format("%-" + (RECEIPT_WIDTH - 10) + "s%10s%n", item, priceStr);
    }

    /**
     * Makes a divider line for the receipt.
     *
     * @param ch the character to repeat
     * @return a line of that character as wide as the receipt
     */
    private String makeLine(char ch)
    {
        StringBuilder line = new StringBuilder();

        for (int i = 0; i < RECEIPT_WIDTH; i++)
        {
            line.append(ch);
        }
        return line.toString();
    }
}
