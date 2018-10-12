/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gui.picturechooser;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PicturePreviewPanel extends JPanel implements PropertyChangeListener
{
        //---------------------------------------------------------- CONSTANTS --//

        //---------------------------------------------------------- VARIABLES --//     
        private JLabel label;
        private int maxImgWidth;
        
        //------------------------------------------------------- CONSTRUCTORS --//     

        //------------------------------------------------------------ METHODS --//     

        //---------------------------------------------------- PRIVATE METHODS --//
        

        public PicturePreviewPanel() 
        {
                setLayout(new BorderLayout(5,5));
                setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
                add(new JLabel("Preview:"), BorderLayout.NORTH);
                label = new JLabel();
                label.setOpaque(false);
                label.setPreferredSize(new Dimension(200, 200));
                maxImgWidth = 195;
                label.setBorder(BorderFactory.createEtchedBorder());
                add(label, BorderLayout.CENTER);
        }

        public void propertyChange(PropertyChangeEvent evt) 
        {
                Icon icon = null;
                if(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(evt.getPropertyName())) 
                {
                        File newFile = (File) evt.getNewValue();
                        if(newFile != null) 
                        {
                                try 
                                {
                                        BufferedImage img = ImageIO.read(newFile);
                                        float width = img.getWidth();
                                        float height = img.getHeight();
                                        float scale = height / width;
                                        width = maxImgWidth;
                                        height = (width * scale); // height should be scaled from new width                                                     
                                        icon = new ImageIcon(img.getScaledInstance(Math.max(1, (int)width), Math.max(1, (int)height), Image.SCALE_SMOOTH));
                                }
                                catch(IOException e) 
                                {
                                        // couldn't read image.
                                }
                        }       
                        label.setIcon(icon);
                        this.repaint(); 
                }
        }
        
}