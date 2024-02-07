import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * Provides GUI for a trader.
 * 
 * @author Chris Zhang
 * @version 5/26
 */
public class BettingWindow
    extends JFrame
    implements ActionListener
{

    private Player     myPlayer; // change to player

    private JButton    raiseBtn, checkBtn, foldBtn;
    // private JRadioButton buyBtn, sellBtn, limitBtn, marketBtn;
    private JTextField symbText;
    private JTextArea  msgArea;

    /**
     * Constructs a new trading window for a trader.
     * 
     * @param trader
     *            a trader that will own this window.
     */
    public BettingWindow(Player player)
    {
        super(player.getName());

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e)
            {
                myPlayer.close(); // close window
            }
        });

        myPlayer = player;

        JPanel panel = new JPanel();
        GridBagLayout gbLayout = new GridBagLayout(); // sets up the location
                                                      // data and what the
                                                      // coordinates are being
                                                      // compared to (upper left
                                                      // corner)
        panel.setLayout(gbLayout);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel label = new JLabel("Amount to bet:  ", JLabel.RIGHT);
        gbc.gridy = 8;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbLayout.setConstraints(label, gbc);
        panel.add(label);
        Color color = new Color(249, 228, 183);
        symbText = new JTextField(5);
        symbText.setBackground(color);
        gbc.gridy = 8;
        gbc.gridx = 2;
        gbc.gridwidth = 1;
        gbLayout.setConstraints(symbText, gbc);
        panel.add(symbText);

        JLabel filler = new JLabel(" ");
        gbc.gridy = 9;
        gbc.gridx = 0;
        gbc.gridwidth = 3;
        gbLayout.setConstraints(filler, gbc);
        panel.add(filler);

        label = new JLabel("My money: " + myPlayer.getMoney(), JLabel.CENTER);
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbLayout.setConstraints(label, gbc);
        panel.add(label);

        filler = new JLabel(" ");
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.gridwidth = 3;
        gbLayout.setConstraints(filler, gbc);
        panel.add(filler);

        label = new JLabel("Current Bet: " + myPlayer.getCurrentBet(), JLabel.CENTER);
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbLayout.setConstraints(label, gbc);
        panel.add(label);

        filler = new JLabel(" ");
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 3;
        gbLayout.setConstraints(filler, gbc);
        panel.add(filler);

        label = new JLabel("Prize Pool: " + myPlayer.getPrizePool(), JLabel.CENTER);
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbLayout.setConstraints(label, gbc);
        panel.add(label);

        filler = new JLabel(" ");
        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.gridwidth = 3;
        gbLayout.setConstraints(filler, gbc);
        panel.add(filler);

        label = new JLabel("My last bet: " + myPlayer.getMyBet(), JLabel.CENTER);
        gbc.gridy = 6;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbLayout.setConstraints(label, gbc);
        panel.add(label);

        filler = new JLabel(" ");
        gbc.gridy = 7;
        gbc.gridx = 0;
        gbc.gridwidth = 3;
        gbLayout.setConstraints(filler, gbc);
        panel.add(filler);

        // RAISE BUTTON
        raiseBtn = new JButton("Bet");
        raiseBtn.addActionListener(this);
        gbc.gridy = 10;
        gbc.gridx = 0;
        gbc.gridwidth = 6;
        gbLayout.setConstraints(raiseBtn, gbc);
        panel.add(raiseBtn);

        filler = new JLabel(" ");
        gbc.gridy = 11;
        gbc.gridx = 0;
        gbc.gridwidth = 3;
        gbLayout.setConstraints(filler, gbc);
        panel.add(filler);

        // CHECK BUTTON
        checkBtn = new JButton("Check"); // GONNA BE IN THE CENTER, not right or
                                         // left
        checkBtn.addActionListener(this);
        gbc.gridy = 12;
        gbc.gridx = 0;
        gbc.gridwidth = 6;
        gbLayout.setConstraints(checkBtn, gbc);
        panel.add(checkBtn);

        filler = new JLabel(" ");
        gbc.gridy = 13;
        gbc.gridx = 0;
        gbc.gridwidth = 3;
        gbLayout.setConstraints(filler, gbc);
        panel.add(filler);

        // FOLD BUTTON
        foldBtn = new JButton("Fold"); // GONNA BE IN THE CENTER, not right or
                                       // left
        foldBtn.addActionListener(this);
        gbc.gridy = 14;
        gbc.gridx = 0;
        gbc.gridwidth = 6;
        gbLayout.setConstraints(foldBtn, gbc);
        panel.add(foldBtn);

        filler = new JLabel(" ");
        gbc.gridy = 15;
        gbc.gridx = 0;
        gbc.gridwidth = 3;
        gbLayout.setConstraints(filler, gbc);
        panel.add(filler);

        Box b = Box.createHorizontalBox();
        b.add(panel);
        // b.add(areaScrollPane);
        Container c = getContentPane();
        c.add(b);

        setBounds(1250, 0, 300, 400);
        setVisible(true);
    }


    /**
     * Displays a message in this window's text area.
     * 
     * @param msg
     *            the message to be displayed.
     */
    public void showMessage(String msg)
    {
        msgArea.append(msg + "\n\n");
    }


    /**
     * Processes GUI events in this window.
     * 
     * @param e
     *            an event.
     */
    public void actionPerformed(ActionEvent e)
    {
        AbstractButton b = (AbstractButton)e.getSource();

        if (b == checkBtn) // check button
        {
            myPlayer.check();
        }
        else if (b == raiseBtn) // raise button
        {
            int raiseTo = Integer.parseInt(symbText.getText().trim().toUpperCase());
            myPlayer.raise(raiseTo);
        }
        else if (b == foldBtn) // fold button
        {
            myPlayer.fold();
        }
    }


    /**
     * error message for prev player has not gone yet or current bet has not
     * received yet
     */
    public void errorMessage1()
    {
        String errorMsg = "Previous player has not gone yet/Current bet not received yet";
        JOptionPane.showMessageDialog(this, errorMsg, "Betting failed", JOptionPane.ERROR_MESSAGE);
        symbText.setText("");
    }


    /**
     * error message for not enough money
     */
    public void errorMessage2()
    {
        String errorMsg = "Not enough money";
        JOptionPane.showMessageDialog(this, errorMsg, "Betting failed", JOptionPane.ERROR_MESSAGE);
        symbText.setText("");
    }


    /**
     * error message for Bet is not greater than or equal to 20 less than the
     * current highest bet
     */
    public void errorMessage3()
    {
        String errorMsg =
            "Bet is not greater than or equal to 20 less than the current highest bet";
        JOptionPane.showMessageDialog(this, errorMsg, "Betting failed", JOptionPane.ERROR_MESSAGE);
        symbText.setText("");
    }


    /**
     * error message for not betting 30 or greater or folding as the first
     * player
     */
    public void errorMessage4()
    {
        String errorMsg = "As first player, you must bet 30 or greater or fold";
        JOptionPane.showMessageDialog(this, errorMsg, "Betting failed", JOptionPane.ERROR_MESSAGE);
        symbText.setText("");
    }


    /**
     * error message for already betting
     */
    public void errorMessage5()
    {
        String errorMsg = "Already betted";
        JOptionPane.showMessageDialog(this, errorMsg, "Betting failed", JOptionPane.ERROR_MESSAGE);
        symbText.setText("");
    }
}
