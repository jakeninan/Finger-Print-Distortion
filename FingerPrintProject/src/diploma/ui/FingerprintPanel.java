package diploma.ui;

import diploma.CommonUtils;
import ij.ImagePlus;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

public class FingerprintPanel extends JPanel{

    public static final int IMG_WIDTH = 240;
   public static final int IMG_HEIGHT = 320;

    private Image image;

    public FingerprintPanel() {

        setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED),
                BorderFactory.createBevelBorder(BevelBorder.LOWERED)));

        setPreferredSize(new Dimension(IMG_WIDTH, IMG_HEIGHT));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (image != null) {
            setPreferredSize(new Dimension(image.getWidth(null), image.getHeight(null)));
        }
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(image, 1, 1, null);
    }

    public void setImage(String imagePath) {

        if (imagePath != null) {
            try {
                ImagePlus imagePlus = CommonUtils.resize(new ImagePlus(imagePath), IMG_WIDTH, IMG_HEIGHT);
                image = imagePlus.getBufferedImage();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            image = null;
        }

        repaint();
    }

    public void setImage(ImageIcon image) {

        this.image = image.getImage();
        repaint();
    }
}
