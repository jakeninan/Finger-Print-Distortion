package diploma.ui;

import Jama.Matrix;
import db.datas;
import diploma.CommonUtils;
import static diploma.DBCreator.ACTION;
import diploma.matching.FingerprintsDatabase;
import diploma.matching.Matcher;
import diploma.model.Finger;
import diploma.model.Fingerprint;
import ij.ImagePlus;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import libsvm.svmclassifiers;

public class MainFrame extends JFrame {

    public static final double THRESHOLD = 1;
    HashMap<Integer, Matrix> si = null;
    private MainFrame mainFrame;
    int refer;
    public static String loadedImagePath;
    ResultSet asd;
    private FingerprintPanel leftPanel;
    private JButton matchButton, loadButton;
    private JComboBox<Finger> fingerBox;
    private JComboBox<Fingerprint> imageBox;
    public static JLabel loadedFileLabel;
    //private JMenuItem addFingerItem, addFPImage;

    private FingerprintsDatabase fingerprintsDatabase;

    private JFileChooser fileChooser = new JFileChooser(".");

    private static Insets DEFAULT_INSETS = new Insets(0, 0, 0, 0);
    public static FingerprintPanel rightPanel;

    public MainFrame() throws Exception {

        mainFrame = this;

        setTitle("FINGER PRINT ");

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        addElements();
        addListeners();
        rightPanel.removeAll();
        fingerprintsDatabase = FingerprintsDatabase.loadExistent("db");
        // rightPanel.setToolTipText("hi");
        updateFingerLists();
    }

    private void updateFingerLists() {

        fingerBox.removeAllItems();
        for (Finger finger : fingerprintsDatabase.getFingerDb().keySet()) {
            fingerBox.addItem(finger);
        }
        fingerBox.setSelectedIndex(-1);
    }

    private void updateImageList(Fingerprint currentFingerprint) {

        imageBox.removeAllItems();

        if (fingerBox.getSelectedItem() != null) {
            for (Fingerprint fingerprint : mainFrame.getFingerprintsDatabase()
                    .getFingerprints((Finger) fingerBox.getSelectedItem())) {

                imageBox.addItem(fingerprint);
            }
        }

        if (currentFingerprint != null) {
            imageBox.setSelectedItem(currentFingerprint);
        } else {
            imageBox.setSelectedIndex(-1);
        }
    }

    private void addListeners() {

        loadButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

               
            }
        });

        matchButton.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

            }
        });
    }

    private void addElements() throws Exception {

        leftPanel = new FingerprintPanel();
        rightPanel = new FingerprintPanel();
        JLabel label = new JLabel("Test Image");//initialize the label
//do some stuff with label here maybe...
        leftPanel.add(label);//now add it
        JLabel labels = new JLabel("Rectified Image");//initialize the label
//do some stuff with label here maybe...
        rightPanel.add(labels);//now add it
        setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();

        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.insets = new Insets(28, 0, 0, 52);
        loadButton = new JButton("Load");
        add(loadButton, c);

        loadedFileLabel = new JLabel();
        c.gridx = 1;
        add(loadedFileLabel, c);
        c.insets = DEFAULT_INSETS;

        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        add(leftPanel, c);

        c.gridx = 2;
        c.gridy = 1;
        c.gridheight = 1;
        c.gridwidth = 1;
        matchButton = new JButton("Identify");
        add(matchButton, c);

        c.gridx = 3;
        c.gridwidth = 2;
        add(rightPanel, c);

        fingerBox = new JComboBox<>();
        imageBox = new JComboBox<>();
        fingerBox.setPreferredSize(new Dimension(100, 20));
        imageBox.setPreferredSize(new Dimension(100, 20));
        c.insets = new Insets(25, 0, 0, 0);
        c.gridwidth = 1;
        c.gridy = 0;
        add(fingerBox, c);

        c.gridx = 4;
        add(imageBox, c);
        c.insets = DEFAULT_INSETS;

        c.anchor = GridBagConstraints.LINE_END;
        c.gridx = 3;
        c.gridy = 2;

        c.anchor = GridBagConstraints.LINE_START;
        c.gridx = 4;

        setSize(730, 500);
        setResizable(false);
    }

    public FingerprintsDatabase getFingerprintsDatabase() {
        return fingerprintsDatabase;
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new MainFrame().setVisible(true);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Override
    public void dispose() {

        try {
            fingerprintsDatabase.saveDB("db");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        super.dispose();
    }
}
