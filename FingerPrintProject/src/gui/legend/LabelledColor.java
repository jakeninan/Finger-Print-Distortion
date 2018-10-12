/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gui.legend;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

public class LabelledColor extends JPanel
{
        //---------------------------------------------------------- CONSTANTS --//
        
        //---------------------------------------------------------- VARIABLES --//     
        private JPanel colorPanel;
        private JLabel text;
        
        //------------------------------------------------------- CONSTRUCTORS --//     
        public LabelledColor(Color color, String string) 
        {
                // Create objects
                colorPanel = new JPanel();
                text = new JLabel();
                Border colorBorder = BorderFactory.createLineBorder(color.darker(),3);
                        
                // Set values
                colorPanel.setBackground(color);
                colorPanel.setBorder(colorBorder);
                text.setPreferredSize(new Dimension(1,1));
                text.setMinimumSize(new Dimension(1,1));
                
                text.setText(string);
                
                // Set options
                setBackground(Color.black);
                text.setForeground(Color.gray);
                text.setPreferredSize(new Dimension(1,1));
                text.setVerticalAlignment(SwingConstants.CENTER);
                
                // Set layouts
                setLayout(new GridLayout(1,2));
                
                // Add elements
                add(colorPanel);
                add(text);
                
        }
        
        //------------------------------------------------------------ METHODS --//     

        //---------------------------------------------------- PRIVATE METHODS --//
}
