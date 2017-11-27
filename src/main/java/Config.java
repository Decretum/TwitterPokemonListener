import javax.swing.*;
import java.awt.*;

public class Config {

    private JFrame jfrm;

    public Config(String feed, String cities, String receiver, boolean showNotifications, boolean sendMessages) {
        jfrm = new JFrame("Configuration");

        JPanel panel1 = new JPanel(new GridLayout(2, 1, 1, 1));
        JPanel panel2 = new JPanel(new GridLayout(1, 2, 1, 1));
        JPanel panel3 = new JPanel(new FlowLayout());
        JPanel panel4 = new JPanel(new FlowLayout());
        JPanel panel5 = new JPanel(new BorderLayout());
        JPanel panel6 = new JPanel(new FlowLayout());
        JPanel panel7 = new JPanel(new FlowLayout());

        JLabel feedLabel = new JLabel("Feed");
        JLabel citiesLabel = new JLabel("Cities");
        JLabel receiverLabel = new JLabel("Receiver");
        JLabel showNotificationsLabel = new JLabel("Show Notifications");
        JLabel sendMessagesLabel = new JLabel("Send Text Messages");
        JTextField feedField = new JTextField(20);
        JTextField receiverField = new JTextField(20);
        JTextArea citiesTextArea = new JTextArea();
        JCheckBox showNotificationsCheckbox = new JCheckBox();
        JCheckBox sendMessagesCheckbox = new JCheckBox();
        JButton update = new JButton("Update");

        feedField.setText(feed);
        citiesTextArea.setText(cities);
        receiverField.setText(receiver);
        showNotificationsCheckbox.setSelected(showNotifications);
        sendMessagesCheckbox.setSelected(sendMessages);
        receiverField.setEnabled(sendMessages);

        update.addActionListener((actionEvent) -> {
            RareNotifier.updateSettings(feedField.getText(), citiesTextArea.getText(), receiverField.getText(), showNotificationsCheckbox.isSelected(), sendMessagesCheckbox.isSelected());
        });
        sendMessagesCheckbox.addActionListener((actionEvent) -> {
            receiverField.setEnabled(sendMessagesCheckbox.isSelected());
        });

        panel7.add(feedLabel);
        panel7.add(feedField);
        panel6.add(receiverLabel);
        panel6.add(receiverField);
        panel5.add(citiesLabel, BorderLayout.NORTH);
        panel5.add(citiesTextArea, BorderLayout.CENTER);
        panel5.add(panel6, BorderLayout.SOUTH);
        panel3.add(showNotificationsCheckbox);
        panel3.add(showNotificationsLabel);
        panel4.add(sendMessagesCheckbox);
        panel4.add(sendMessagesLabel);
        panel2.add(panel3);
        panel2.add(panel4);
        panel1.add(panel2, BorderLayout.CENTER);
        panel1.add(update, BorderLayout.SOUTH);
        jfrm.add(panel7, BorderLayout.NORTH);
        jfrm.add(panel1, BorderLayout.SOUTH);
        jfrm.add(panel5, BorderLayout.CENTER);

        jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jfrm.setSize(400, 400);
        jfrm.setLocationRelativeTo(null);
        jfrm.setVisible(true);
    }

    static void showConfig(String feed, String cities, String receiver, boolean showNotifications, boolean sendMessages) {
        SwingUtilities.invokeLater(() -> {
            new Config(feed, cities, receiver, showNotifications, sendMessages);
        });
    }

    public static void main(String[] args) {
        showConfig("oko", "asdf", "aaa", true, true);
    }

}
