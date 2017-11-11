import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;

import java.awt.*;
import java.io.File;

public class SwingTest {

    private Clip clip;
    private JFrame jfrm;
    private static String soundFile;

    public SwingTest(String text) throws Exception {
        jfrm = new JFrame("Testing");
        jfrm.setLayout(new GridLayout(2, 1, 1, 1));
        jfrm.add(new JTextArea(text));
        JPanel jp = new JPanel();
        jp.setLayout(new GridLayout(3, 6, 1, 1));
        for (int x = 0; x < 7; x++) {
            jp.add(new JLabel(""));
        }
        JButton yes = new JButton("Ok");
        JButton no = new JButton("Nah");
        yes.addActionListener(ae -> {
            System.out.println("yes");
            clip.stop();
            System.exit(0);
        });
        no.addActionListener(ae -> {
            System.out.println("no");
            clip.stop();
            jfrm.dispose();
        });
        jp.add(yes);
        jp.add(new JLabel(""));
        jp.add(new JLabel(""));
        jp.add(no);
        for (int x = 0; x < 7; x++) {
            jp.add(new JLabel(""));
        }
        jfrm.add(jp);
        jfrm.setSize(400, 400);
        jfrm.setVisible(true);
        clip = AudioSystem.getClip();
        // getAudioInputStream() also accepts a File or InputStream
        AudioInputStream ais = AudioSystem.getAudioInputStream(new File(
                soundFile));
        clip.open(ais);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    static void showNotification(final String text, int level) {
        if (level == 1) {
            soundFile = "unown.wav";
        } else {
            soundFile = "alert.wav";
        }
        SwingUtilities.invokeLater(() -> {
            try {
                new SwingTest(text);
            } catch (Exception e) {
                // I really don't care.
                e.printStackTrace();
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new SwingTest("testing");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}
