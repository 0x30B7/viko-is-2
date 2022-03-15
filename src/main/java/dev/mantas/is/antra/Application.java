package dev.mantas.is.antra;

import org.apache.commons.codec.binary.Hex;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.SecureRandom;
import java.util.Vector;

public class Application {

    public static void main(String[] args) {
        new Application();
    }

    private final JTextArea textInput;
    private final JTextArea textOutput;
    private final JTextArea textKey;
    private final JTextArea textIv;
    private final JComboBox<String> btnMode;

    public Application() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("IS Antra (DES)");
        frame.setSize(800, 450);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(null);

        JLabel lblInput = new JLabel("Input");
        lblInput.setSize(400, 16);
        lblInput.setLocation(5, 0);

        textInput = new JTextArea();
        textInput.setLineWrap(true);
        textInput.setSize(400, 200);
        textInput.setLocation(0, 16);

        JLabel lblOutput = new JLabel("Output");
        lblOutput.setSize(400, 16);
        lblOutput.setLocation(415, 0);

        textOutput = new JTextArea();
        textOutput.setLineWrap(true);
        textOutput.setSize(400, 200);
        textOutput.setLocation(410, 16);

        panel.add(lblInput);
        panel.add(textInput);
        panel.add(lblOutput);
        panel.add(textOutput);

        JLabel lblKey = new JLabel("Key");
        lblKey.setSize(400, 16);
        lblKey.setLocation(415, 250);

        textKey = new JTextArea();
        textKey.setSize(400, 50);
        textKey.setLocation(410, 266);

        JLabel lblIv = new JLabel("IV");
        lblIv.setSize(400, 16);
        lblIv.setLocation(415, 330);

        textIv = new JTextArea();
        textIv.setSize(400, 50);
        textIv.setLocation(410, 346);
        textIv.setEnabled(false);

        panel.add(lblKey);
        panel.add(textKey);
        panel.add(lblIv);
        panel.add(textIv);

        JButton btnEncrypt = new JButton("Encrypt");
        btnEncrypt.setSize(80, 24);
        btnEncrypt.setLocation(10, 266);

        btnEncrypt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    doCryptOperation(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        JButton btnDecrypt = new JButton("Decrypt");
        btnDecrypt.setSize(80, 24);
        btnDecrypt.setLocation(100, 266);

        btnDecrypt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    doCryptOperation(false);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        JButton btnGenKey = new JButton("Generate Key");
        btnGenKey.setSize(170, 24);
        btnGenKey.setLocation(10, 300);

        btnGenKey.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateKey(textKey, 8);
            }
        });

        JButton btnGenIV = new JButton("Generate IV");
        btnGenIV.setSize(170, 24);
        btnGenIV.setLocation(10, 330);

        btnGenIV.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateKey(textIv, 8);
            }
        });

        JLabel lblPadding = new JLabel("Padding");
        lblPadding.setSize(400, 16);
        lblPadding.setLocation(240, 250);

        Vector<String> modeOptions = new Vector<>();
        modeOptions.add("ECB");
        modeOptions.add("CBC");
        modeOptions.add("CFB");
        modeOptions.add("OFB");
        modeOptions.add("CTR");

        btnMode = new JComboBox<>(modeOptions);
        btnMode.setSize(80, 24);
        btnMode.setLocation(240, 266);

        btnMode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ("ECB".equals(btnMode.getSelectedItem().toString())) {
                    textIv.setEnabled(false);
                } else {
                    textIv.setEnabled(true);
                }
            }
        });

        panel.add(btnEncrypt);
        panel.add(btnDecrypt);
        panel.add(lblPadding);
        panel.add(btnMode);
        panel.add(btnGenKey);
        panel.add(btnGenIV);

        frame.setContentPane(panel);
        frame.setVisible(true);
    }

    private void doCryptOperation(boolean encrypt) {
        EncryptionHelper helper = new EncryptionHelper();

        String input_ = textInput.getText();

        if (input_ == null || input_.isEmpty()) {
            return;
        }

        String key_ = textKey.getText();

        if (key_ == null || (key_ = key_.trim()).isEmpty()) {
            return;
        }

        String iv_ = textIv.getText();

        if (iv_ == null || (iv_ = iv_.trim()).isEmpty()) {
            return;
        }

        try {
            helper.init(key_, (String) btnMode.getSelectedItem(), iv_);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            return;
        }

        try {
            textOutput.setText(encrypt ? helper.encrypt(input_) :  helper.decrypt(input_));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    private void generateKey(JTextArea target, int length) {
        byte[] key = new byte[length];

        SecureRandom random = new SecureRandom();
        random.nextBytes(key);

        target.setText(Hex.encodeHexString(key));
    }

}
