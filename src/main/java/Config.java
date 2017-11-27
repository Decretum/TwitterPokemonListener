import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Config {

    private JFrame jfrm;

    String f;
    String c;
    String r;
    boolean n;
    boolean m;
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

    public Config(String feed, String cities, String receiver, boolean showNotifications, boolean sendMessages) {
        f = feed;
        c = cities;
        r = receiver;
        n = showNotifications;
        m = sendMessages;

        jfrm = new JFrame("Configuration");

        JPanel panel1 = new JPanel(new GridLayout(2, 1, 1, 1));
        JPanel panel2 = new JPanel(new GridLayout(1, 2, 1, 1));
        JPanel panel3 = new JPanel(new FlowLayout());
        JPanel panel4 = new JPanel(new FlowLayout());
        JPanel panel5 = new JPanel(new BorderLayout());
        JPanel panel6 = new JPanel(new FlowLayout());
        JPanel panel7 = new JPanel(new FlowLayout());

        feedField.setText(feed);
        citiesTextArea.setText(cities);
        receiverField.setText(receiver);
        showNotificationsCheckbox.setSelected(showNotifications);
        sendMessagesCheckbox.setSelected(sendMessages);
        receiverField.setEnabled(sendMessages);
        update.setEnabled(false);

        KeyListener defaultKeyListener = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (areSettingsDifferent()) {
                    update.setEnabled(true);
                } else {
                    update.setEnabled(false);
                }
            }
        };

        feedField.addKeyListener(defaultKeyListener);
        citiesTextArea.addKeyListener(defaultKeyListener);
        receiverField.addKeyListener(defaultKeyListener);
        showNotificationsCheckbox.addActionListener((actionEvent) -> {
            if (areSettingsDifferent()) {
                update.setEnabled(true);
            } else {
                update.setEnabled(false);
            }
        });
        showNotificationsLabel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showNotificationsCheckbox.setSelected(!showNotificationsCheckbox.isSelected());
                if (areSettingsDifferent()) {
                    update.setEnabled(true);
                } else {
                    update.setEnabled(false);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        sendMessagesCheckbox.addActionListener((actionEvent) -> {
            receiverField.setEnabled(sendMessagesCheckbox.isSelected());
            if (areSettingsDifferent()) {
                update.setEnabled(true);
            } else {
                update.setEnabled(false);
            }
        });
        sendMessagesLabel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                sendMessagesCheckbox.setSelected(!sendMessagesCheckbox.isSelected());
                receiverField.setEnabled(sendMessagesCheckbox.isSelected());
                if (areSettingsDifferent()) {
                    update.setEnabled(true);
                } else {
                    update.setEnabled(false);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        update.addActionListener((actionEvent) -> {
            f = feedField.getText();
            c = citiesTextArea.getText();
            r = receiverField.getText();
            n = showNotificationsCheckbox.isSelected();
            m = sendMessagesCheckbox.isSelected();
            RareNotifier.updateSettings(f, c, r, n, m);
            update.setEnabled(false);
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

    public static void showConfig(String feed, String cities, String receiver, boolean showNotifications, boolean sendMessages) {
        SwingUtilities.invokeLater(() -> {
            new Config(feed, cities, receiver, showNotifications, sendMessages);
        });
    }

    private boolean areSettingsDifferent() {
        if (!f.equals(feedField.getText())) {
            return true;
        }
        if (!c.equals(citiesTextArea.getText())) {
            return true;
        }
        if (!r.equals(receiverField.getText())) {
            return true;
        }
        if (n != showNotificationsCheckbox.isSelected()) {
            return true;
        }
        if (m != sendMessagesCheckbox.isSelected()) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        showConfig("oko", "asdf", "aaa", true, true);
    }

}
