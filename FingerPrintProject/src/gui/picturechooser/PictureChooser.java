/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gui.picturechooser;


import javax.swing.JFileChooser;

public class PictureChooser extends JFileChooser
{
        //---------------------------------------------------------- CONSTANTS --//

        //---------------------------------------------------------- VARIABLES --//     

        //------------------------------------------------------- CONSTRUCTORS --//     
        public PictureChooser()
        {
                // Initialize properties
                setMultiSelectionEnabled(false);
                setDialogTitle("Select a fingerprint raster image");
                
                // Initial position
                setCurrentDirectory(new java.io.File("./data"));
                
                // Set filters
                setFileSelectionMode(JFileChooser.FILES_ONLY);
                setAcceptAllFileFilterUsed(false);
                
                addChoosableFileFilter(new SimpleFilter("PNG (*.png)",".png"));
                addChoosableFileFilter(new SimpleFilter("BMP (*.bmp)",".bmp"));
                addChoosableFileFilter(new SimpleFilter("GIF (*.gif)",".gif"));
                addChoosableFileFilter(new SimpleFilter("JPEG (*.jpg)",".jpg"));
                
                // Add picture previewer
                PicturePreviewPanel preview = new PicturePreviewPanel();
                setAccessory(preview);
                addPropertyChangeListener(preview);
        }

        //------------------------------------------------------------ METHODS --//     

        //---------------------------------------------------- PRIVATE METHODS --//
}