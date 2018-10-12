/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gui.legend;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class PanelLegend extends JPanel
{
        //---------------------------------------------------------- CONSTANTS --//
        private int SPACE = 4; 
        //---------------------------------------------------------- VARIABLES --//     
        private JLabel textDir;
        private JLabel textMinutiae;
        
        private LabelledColor colDirPos;
        private LabelledColor colDirNeg;
        private LabelledColor colDirVer;
        private LabelledColor colDirHor;

        private LabelledColor colMinEnd;
        private LabelledColor colMinInt;

        //------------------------------------------------------- CONSTRUCTORS --//     
        public PanelLegend() 
        {
                // Create objects
                textDir = new JLabel("Ridge direction");
                textMinutiae = new JLabel("Minutiae");
                Font titleFont = new Font("Arial",Font.BOLD,15);
                
                colDirPos = new LabelledColor (Color.green,  " Pos. (/)");
                colDirNeg = new LabelledColor (Color.yellow, " Neg. (\\)");
                colDirVer = new LabelledColor (Color.cyan,   " Ver. (|)");
                colDirHor = new LabelledColor (Color.red,    " Hor. (-)");

                colMinEnd = new LabelledColor (Color.blue,       " Endpoints");
                colMinInt = new LabelledColor (Color.magenta," Intersect.");
                
                // Options
                setBackground(Color.black);
                textDir.setBackground(Color.black);
                textMinutiae.setBackground(Color.black);
                textDir.setVerticalAlignment(SwingConstants.CENTER);
                textMinutiae.setVerticalAlignment(SwingConstants.CENTER);
                textDir.setFont(titleFont);
                textMinutiae.setFont(titleFont);
                textDir.setForeground(Color.gray);
                textMinutiae.setForeground(Color.gray);
                textDir.setPreferredSize(new Dimension(1,1));   
                textMinutiae.setPreferredSize(new Dimension(1,1));
                
                setPreferredSize(new Dimension(1,1));
                
                //setBackground(new Color (100,100,100));
                
                // Set layout
                setLayout(new GridLayout(8,1,SPACE,SPACE));
                
                // Add components
                add(textDir);
                add(colDirPos);
                add(colDirNeg);
                add(colDirVer);
                add(colDirHor);
                add(textMinutiae);
                add(colMinEnd);
                add(colMinInt);
        }

        //------------------------------------------------------------ METHODS --//     

        //---------------------------------------------------- PRIVATE METHODS --//
}
