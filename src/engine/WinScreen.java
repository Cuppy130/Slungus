package engine;

import javax.swing.*;

import misc.Color;

import java.awt.*;

public class WinScreen {
    public static void show() {
        JFrame frame = new JFrame("You Win!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(640, 480);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());

        JLabel winLabel = new JLabel("Congratulations, You Win!", SwingConstants.CENTER);
        winLabel.setFont(new Font("Serif", Font.BOLD, 36));

        JButton closeButton = new JButton("Close");
        closeButton.setFont(new Font("SansSerif", Font.PLAIN, 18));
        closeButton.addActionListener(e -> frame.dispose());

        panel.add(winLabel, BorderLayout.CENTER);
        panel.add(closeButton, BorderLayout.SOUTH);

        frame.add(panel);
        frame.setVisible(true);
    }
}
