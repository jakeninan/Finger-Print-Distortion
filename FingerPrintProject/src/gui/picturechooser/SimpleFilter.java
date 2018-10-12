/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gui.picturechooser;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class SimpleFilter extends FileFilter
{
        //---------------------------------------------------------- CONSTANTS --//

        //---------------------------------------------------------- VARIABLES --//     
        private String description;
        private String extension;
        
        //------------------------------------------------------- CONSTRUCTORS --//     
        public SimpleFilter(String description, String extension)
        {
                this.description = description;
                this.extension = extension;
        }
        //------------------------------------------------------------ METHODS --//     
        public boolean accept(File file)
        {
                if(file.isDirectory()) 
                { 
                        return true; 
                } 
                String myFile = file.getName().toLowerCase(); 
                return myFile.endsWith(extension);
        }
        
        public String getDescription()
        {
                return description;
        }
        
        //---------------------------------------------------- PRIVATE METHODS --//
}
